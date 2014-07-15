package test;

import hn.join.fieldwork.utils.XmlUtil;
import hn.join.fieldwork.web.dto.CustomerDto;
import hn.join.fieldwork.web.dto.WorkOrderDto;
import hn.join.fieldwork.web.dto.WorkOrderDto.ProblemDto;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

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
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;

public class SendOrderClient {

	private static DateFormat dateFormat = new SimpleDateFormat("MMddHHmmss");

	private static final long one_day_in_mills = 24 * 60 * 60 * 1000L;

	private static final HttpClient httpClient = new HttpClient();

	static int problem_id =740;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(System.getProperty("file.encoding"));
		String url = "http://192.168.1.55:8080/fieldwork-new/interface/work-order";
//		String url = "http://192.168.1.123:8280/fieldwork-new/interface/work-order";
		setupHttpClient();
		for(int i=0;i<2;i++){
		try {
			String workOrderDtoXml = createWorkOrderDto(i);
//			String workOrderDtoXml=FileUtils.readFileToString(new File("src/test/java/test/1.xml"));
//			System.out.println(workOrderDtoXml);
			String result = doCall(workOrderDtoXml, url);
			System.out.println(result);
			Thread.sleep(1000);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
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
		byte[] base64Bytes = base64.encode(requestXml.getBytes("utf-8"));
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

	private static String createWorkOrderDto(int i) throws Exception {
		DateFormat wordCardIdFormat = new SimpleDateFormat("MMddHHmmss");
		WorkOrderDto workOrderDto = new WorkOrderDto();
		workOrderDto.setWorkCardId(Long.parseLong(wordCardIdFormat.format(new Date())));
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		workOrderDto.setServiceDate(new Date());
		workOrderDto.setWorkOrderType(1);
		workOrderDto.setCustomerDto(createCustomDto());
		workOrderDto.setFieldWorkerId(42);
		workOrderDto.setFieldWorkerName("孟卫国");
//		int x=i%7;
//		if(x==0){
//			workOrderDto.setFieldWorkerId(53);
//			workOrderDto.setFieldWorkerName("蒋民洪");
//		}else if(x==1){
//			workOrderDto.setFieldWorkerId(50);
//			workOrderDto.setFieldWorkerName("胡高仁");
//		}else if(x==2){
//			workOrderDto.setFieldWorkerId(42);
//			workOrderDto.setFieldWorkerName("孟卫国");
//		}else if(x==3){
//			workOrderDto.setFieldWorkerId(47);
//			workOrderDto.setFieldWorkerName("雷栋材");
//		}else if(x==4){
//			workOrderDto.setFieldWorkerId(45);
//			workOrderDto.setFieldWorkerName("吕军");
//		}else if(x==5){
//			workOrderDto.setFieldWorkerId(55);
//			workOrderDto.setFieldWorkerName("龙万紫");
//		}else if(x==6){
//			workOrderDto.setFieldWorkerId(61);
//			workOrderDto.setFieldWorkerName("杨彬");
//		}
		workOrderDto
				.setWorkOrderDescription("我是派工说明:序号:"+i);
		workOrderDto.setExpectArriveTime(new Date(System.currentTimeMillis()
				+ one_day_in_mills));
		workOrderDto.setProblemList(createProblemList());
		workOrderDto.setChargeType(1);
		workOrderDto.setChargeMoney(220f);
		workOrderDto.setUrgency(1);
		workOrderDto.setCity("长沙市");
//		workOrderDto.setReturnProductId("2");
//		workOrderDto.setVisitType("我是回复类型1");
//		workOrderDto.setProductStatus("产品运行稳定");
//		workOrderDto.setHandleResult("我是处理结果");
		workOrderDto.setRoute(StringEscapeUtils.escapeXml(FileUtils.readFileToString(new File("src/test/java/test/1.txt"))));
		return XmlUtil.toXml(workOrderDto);
	}

	private static CustomerDto createCustomDto() {
		CustomerDto customerDto = new CustomerDto();
		customerDto.setCustomerId((long) 11103);
		customerDto.setLinkPerson("小丽");
		customerDto.setCustomerName("小丽衣橱");
		customerDto.setTaxCode("1-77-111-0987");
		customerDto.setDepartmentId(724);
		customerDto.setDepartmentName("长沙服务部");
		customerDto.setRevenueId(143010300);
		customerDto.setRevenueName("长沙市天心区国家税务局");
		customerDto.setCustomerAddr("东塘友谊商城");
//		customerDto.setCustomerMobile("18973188380");
		customerDto.setCustomerTel("073182237773");
//		customerDto.setCustomerLatitude(28.19408f);
//		customerDto.setCustomerLongitude(112.99213f);
		return customerDto;
	}

	private static LinkedList<ProblemDto> createProblemList() {
		LinkedList<ProblemDto> problems = new LinkedList<ProblemDto>();
		ProblemDto p1 = new ProblemDto();
		p1.setProblemId(Long.valueOf((problem_id++)));
		p1.setQuestionDesc("机器不能启动");
		p1.setQuestionTypId(200);
		p1.setQuestionTypPid(200);
		p1.setSolution("我是标准答案:"+System.currentTimeMillis());
		problems.add(p1);

//		ProblemDto p2 = new ProblemDto();
//		p2.setProblemId(problem_id++);
//		p2.setQuestionDesc("不能上网");
//		p2.setQuestionTypId(82);
//		p2.setQuestionTypPid(52);
//		p2.setSolution("我是标准答案:"+System.currentTimeMillis());
//		problems.add(p2);
		return problems;
	}

}
