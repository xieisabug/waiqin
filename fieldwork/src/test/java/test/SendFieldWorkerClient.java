package test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.IOUtils;

import hn.join.fieldwork.utils.XmlUtil;
import hn.join.fieldwork.web.dto.FieldWorkerDto;

public class SendFieldWorkerClient {

	private static final HttpClient httpClient = new HttpClient();
	
	public static void main(String[] args) {
		String url = "http://222.240.218.91:20000/fieldwork/interface/fieldworker";
		setupHttpClient();
		
		try {
			String fieldWorkerDtoXml = createFieldWorkerDto();
			System.out.println(fieldWorkerDtoXml);
			String result = doCall(fieldWorkerDtoXml, url);
			System.out.println(result);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	
	private static void setupHttpClient() {
		HttpConnectionManagerParams params = new HttpConnectionManagerParams();
		httpClient.getParams().setCookiePolicy(CookiePolicy.DEFAULT);
		params.setSoTimeout(5000);
		params.setConnectionTimeout(60 * 1000);
		params.setDefaultMaxConnectionsPerHost(4);
		params.setMaxTotalConnections(8);
		MultiThreadedHttpConnectionManager manager = new MultiThreadedHttpConnectionManager();
		manager.setParams(params);
		httpClient.setHttpConnectionManager(manager);

	}

	private static String doCall(String requestXml, String url)
			throws IOException {
		Base64 base64 = new Base64();
		byte[] base64Bytes = base64.encode(requestXml.getBytes("UTF-8"));
		PostMethod post = new PostMethod(url);
		RequestEntity requestEntity = new ByteArrayRequestEntity(base64Bytes);
		post.setRequestEntity(requestEntity);
		post.setRequestHeader("Content-type",
				"application/x-www-form-urlencoded; charset=UTF-8");
		post.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());
		int result = 0;
		InputStream in = null;
		byte[] resultBytes = null;
		try {
			result = httpClient.executeMethod(post);
			if (result == HttpStatus.SC_OK) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				in = post.getResponseBodyAsStream();
				IOUtils.copy(in, baos);
				resultBytes = baos.toByteArray();
			} else {
				throw new IOException("远程服务器错误,返回 HTTP状态码:" + result);
			}

		} catch (HttpException e) {
			throw new IOException(e);
		} finally {
			IOUtils.closeQuietly(in);
			post.releaseConnection();
		}
		String resultXml = new String(resultBytes, "UTF-8");
		return resultXml;
	}
	
	private static String createFieldWorkerDto() throws Exception {
		FieldWorkerDto fieldWorkerDto = new FieldWorkerDto();
		fieldWorkerDto.setOp("U");
		fieldWorkerDto.setFullname("周青洪");
		fieldWorkerDto.setFieldWorkerNo("00293");
		fieldWorkerDto.setFieldWorkerId(282);
		fieldWorkerDto.setDepartmentId(722);
		fieldWorkerDto.setDepartmentName("衡阳服务部");
		fieldWorkerDto.setDepartmentNo("0104");
		return XmlUtil.toXml(fieldWorkerDto);

	}

}
