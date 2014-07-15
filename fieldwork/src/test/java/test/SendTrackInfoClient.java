package test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.IOUtils;

public class SendTrackInfoClient {

	private static final HttpClient httpClient = new HttpClient();

	private static String url = "http://192.168.1.55:48080/fieldwork-new/fieldworker/biz/apply-track";

	private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * @param args
	 * @throws java.io.IOException
	 */
	public static void main(String[] args) throws IOException {

		setupHttpClient();
		String r=doCall("61","长沙市",28.19409f,112.99212f,"湖南省长沙市芙蓉区建湘路38",dateFormat.format(new Date()));
		System.out.println(r);

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

	private static String doCall(String fieldWorkerNo, String areaCode,
			Float latitude, Float longitude, String address,String createTime) throws IOException {
		PostMethod post = new PostMethod(url);
		post.setRequestHeader("Content-type",
				"application/x-www-form-urlencoded; charset=UTF-8");
		post.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());
		post.addParameter("fieldWorkerNo", fieldWorkerNo);
		post.addParameter("areaCode", areaCode);
		post.addParameter("latitude", String.valueOf(latitude));
		post.addParameter("longitude",String.valueOf(longitude));
		post.addParameter("address", address);
		post.addParameter("createTime", createTime);
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
				return new String(resultBytes);
			} else {
				throw new IOException("远程服务器错误,返回 HTTP状态码:" + result);
			}

		} catch (HttpException e) {
			throw new IOException(e);
		} finally {
			IOUtils.closeQuietly(in);
			post.releaseConnection();
		}
	}
}
