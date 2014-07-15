package hn.join.fieldwork.web;

import hn.join.fieldwork.domain.SpendingInfo;
import hn.join.fieldwork.domain.WorkOrderProblem;
import hn.join.fieldwork.domain.WorkOrderReceipt;
import hn.join.fieldwork.service.CustomerService;
import hn.join.fieldwork.service.TrackInfoService;
import hn.join.fieldwork.service.WorkOrderService;
import hn.join.fieldwork.utils.AudioUtil;
import hn.join.fieldwork.utils.DateTimeUtil;
import hn.join.fieldwork.utils.ImageUtil;
import hn.join.fieldwork.utils.JsonUtil;
import hn.join.fieldwork.utils.SystemConstants;
import hn.join.fieldwork.web.command.CreateTrackInfoCommand;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.support.json.JSONUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Controller
@RequestMapping(value = "/fieldworker/biz")
public class FieldWorkerBizController {

	private static final Logger LOG = Logger
			.getLogger(FieldWorkerBizController.class);

	@Autowired
	private WorkOrderService workOrderService;

	@Autowired
	private TrackInfoService trackInfoService;
	
	@Autowired
	private CustomerService customerService;

	/**
	 * 接收工单
	 * 
	 */
	@RequestMapping(value = "confirm", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public String confirm(@RequestParam(required = true) Long workerNo,
			@RequestParam(required = true) String workOrderNo,
			@RequestParam(required = false) String confirmTime) {
		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;
		Date _confirmTime = null;
		if (StringUtils.isEmpty(confirmTime)) {
			_confirmTime = new Date();
		} else {
			_confirmTime = DateTimeUtil.parseAsYYYYMMddHHmmss(confirmTime);
		}
		try {
			workOrderService.confirm(workerNo, workOrderNo, _confirmTime);
			resultCode = SystemConstants.result_code_on_success;
		} catch (Exception ex) {
			LOG.error("工单确认失败,workOrderNo:" + workOrderNo + ",confirmTime:"
					+ confirmTime, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);
	}

	/**
	 * 查看工单
	 * 
	 */
	@RequestMapping(value = "view", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public String view(@RequestParam(required = true) Long workerNo,
			@RequestParam(required = true) String workOrderNo,
			@RequestParam(required = false) String viewTime) {
		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;
		Date _viewTime = null;
		if (StringUtils.isEmpty(viewTime)) {
			_viewTime = new Date();
		} else {
			_viewTime = DateTimeUtil.parseAsYYYYMMddHHmmss(viewTime);
		}
		try {
			workOrderService.view(workerNo, workOrderNo, _viewTime);
			resultCode = SystemConstants.result_code_on_success;
		} catch (Exception ex) {
			LOG.error("提交工单查看状态失败,workOrderNo:" + workOrderNo + ",viewTime:"
					+ viewTime, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);

	}

	/**
	 * 接受工单
	 * 
	 */
	@RequestMapping(value = "accept", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public String accept(@RequestParam(required = true) Long workerNo,
			@RequestParam(required = true) String workOrderNo,
			@RequestParam(required = false) String acceptTime) {
		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;
		Date _acceptTime = null;
		if (StringUtils.isEmpty(acceptTime)) {
			_acceptTime = new Date();
		} else {
			_acceptTime = DateTimeUtil.parseAsYYYYMMddHHmmss(acceptTime);
		}
		try {
			workOrderService.accept(workerNo, workOrderNo, _acceptTime);
			resultCode = SystemConstants.result_code_on_success;
		} catch (Exception ex) {
			LOG.error("提交工单接受状态失败,workOrderNo:" + workOrderNo + ",acceptTime:"
					+ acceptTime, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);
	}

	/**
	 * 申请改派
	 * 
	 */
	@RequestMapping(value = "reassign", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public String reassign(@RequestParam(required = true) Long workerNo,
			@RequestParam(required = true) String workOrderNo,
			@RequestParam(required = false) String unacceptTime,
			@RequestParam(required = false) String reason) {
		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;
		Date _unacceptTime = null;
		if (StringUtils.isEmpty(unacceptTime)) {
			_unacceptTime = new Date();
		} else {
			_unacceptTime = DateTimeUtil.parseAsYYYYMMddHHmmss(unacceptTime);
		}
		try {
			workOrderService.reassign(workerNo, workOrderNo, reason,
					_unacceptTime);
			resultCode = SystemConstants.result_code_on_success;
		} catch (Exception ex) {
			LOG.error("工单申请该派失败,workOrderNo:" + workOrderNo + ",unacceptTime:"
					+ unacceptTime, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);
	}

	/**
	 * 外服人员联系客户
	 * 
	 */
	@RequestMapping(value = "callCustomer", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public String callCustomer(@RequestParam(required = true) Long workerNo,
			@RequestParam(required = true) String workOrderNo,
			@RequestParam(required = false) String callTime) {
		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;
		Date _callTime = null;
		if (StringUtils.isEmpty(callTime)) {
			_callTime = new Date();
		} else {
			_callTime = DateTimeUtil.parseAsYYYYMMddHHmmss(callTime);
		}
		try {
			workOrderService.callCustomer(workerNo, workOrderNo, _callTime);
			resultCode = SystemConstants.result_code_on_success;
		} catch (Exception ex) {
			LOG.error("提交联系客户时间失败,workOrderNo:" + workOrderNo + ",callTime:"
					+ callTime, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);
	}

	/**
	 * 
	 * 到达客户所在地
	 */
	@RequestMapping(value = "arrive", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public String arrive(@RequestParam(required = true) Long workerNo,
			@RequestParam(required = true) String workOrderNo,
			@RequestParam(required = false) String arriveTime) {
		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;
		Date _arriveTime = null;
		if (StringUtils.isEmpty(arriveTime)) {
			_arriveTime = new Date();
		} else {
			_arriveTime = DateTimeUtil.parseAsYYYYMMddHHmmss(arriveTime);
		}
		try {
			workOrderService.arrival(workerNo, workOrderNo, _arriveTime);
			resultCode = SystemConstants.result_code_on_success;
		} catch (Exception ex) {
			LOG.error("提交到达时间失败,workOrderNo:" + workOrderNo + ",arriveTime:"
					+ arriveTime, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);
	}

	/**
	 * 工单完成
	 */
	@RequestMapping(value = "finish", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public String finish(@RequestParam(required = true) Long workerId,
			@RequestParam(required = true) String workOrderNo,
			@RequestParam(required = false) String finishTime,
			@RequestParam(required = false,defaultValue="1") String problemSolved) {
		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;
		Date _finishTime = null;
		if (StringUtils.isEmpty(finishTime)) {
			_finishTime = new Date();
		} else {
			_finishTime = DateTimeUtil.parseAsYYYYMMddHHmmss(finishTime);
		}
		try {
			workOrderService.finish(workerId, workOrderNo, _finishTime,problemSolved);
			resultCode = SystemConstants.result_code_on_success;
		} catch (Exception ex) {
			LOG.error("提交完成时间失败,workOrderNo:" + workOrderNo + ",finishTime:"
					+ finishTime, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);
	}
	/**
	 * 提交远程解决方案
	 * @param httpServletRequest
	 * @return
	 */
	@RequestMapping(value = "apply-remote-solution", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public String applyRemoteSolution(HttpServletRequest httpServletRequest) {
		String workerNo = httpServletRequest.getParameter("workerNo");
		String workOrderNo = httpServletRequest.getParameter("workOrderNo");
		// String[] questionId = httpServletRequest
		// .getParameterValues("questionId");
		// // 问题类型ID
		// String[] questionTypId = httpServletRequest
		// .getParameterValues("questionTypId");
		// String[] questionTypPId = httpServletRequest
		// .getParameterValues("questionTypPId");
		// String[] questionDesc = httpServletRequest
		// .getParameterValues("questionDesc");
		// String[] solutionMethod = httpServletRequest
		// .getParameterValues("solutionMethod");

		String[] productId;
		if (!StringUtils.isEmpty(httpServletRequest.getParameter("productId"))) {
			productId = StringUtils.split(
					httpServletRequest.getParameter("productId"), "^|");
		} else {
			productId = new String[0];
		}

		String[] questionId;
		if (!StringUtils.isEmpty(httpServletRequest.getParameter("questionId"))) {
			questionId = StringUtils.split(
					httpServletRequest.getParameter("questionId"), "^|");
		} else {
			questionId = new String[0];
		}
		String[] questionTypId;
		if (!StringUtils.isEmpty(httpServletRequest
				.getParameter("questionTypId"))) {
			questionTypId = StringUtils.split(
					httpServletRequest.getParameter("questionTypId"), "^|");
		} else {
			questionTypId = new String[0];
		}
		String[] questionTypPId;
		if (!StringUtils.isEmpty(httpServletRequest
				.getParameter("questionTypPId"))) {
			questionTypPId = StringUtils.split(
					httpServletRequest.getParameter("questionTypPId"), "^|");
		} else {
			questionTypPId = new String[0];
		}
		String[] questionDesc;
		if (!StringUtils.isEmpty(httpServletRequest
				.getParameter("questionDesc"))) {
			questionDesc = StringUtils.split(
					httpServletRequest.getParameter("questionDesc"), "^|");
		} else {
			questionDesc = new String[0];
		}
		String[] solutionMethod;
		if (!StringUtils.isEmpty(httpServletRequest
				.getParameter("solutionMethod"))) {
			solutionMethod = StringUtils.split(
					httpServletRequest.getParameter("solutionMethod"), "^|");
		} else {
			solutionMethod = new String[0];
		}

		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;
		try {
			List<WorkOrderProblem> problems = generateWorkOrderProblems(
					workOrderNo, productId, questionId, questionTypId,
					questionTypPId, questionDesc, solutionMethod);
			workOrderService.applyRemoteSolution(workOrderNo, problems);
			resultCode = SystemConstants.result_code_on_success;
		} catch (Exception ex) {
			LOG.error(
					"提交远程解决方案失败,workerNo:" + workerNo + ",workOrderNo:"
							+ workOrderNo + ",questionId:"
							+ Arrays.toString(questionId) + ",questionTypId:"
							+ Arrays.toString(questionTypId)
							+ ",questionTypPId"
							+ Arrays.toString(questionTypPId)
							+ ",questionDesc:" + Arrays.toString(questionDesc)
							+ ",solutionMethod:"
							+ Arrays.toString(solutionMethod), ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);
	}

	private List<WorkOrderProblem> generateWorkOrderProblems(
			String workOrderNo, String[] productId, String[] questionId,
			String[] questionTypId, String[] questionTypPId,
			String[] questionDesc, String[] solutionMethod) {
		if ((productId.length != questionId.length || questionId.length != questionTypId.length)
				|| (questionId.length != questionTypPId.length)
				|| (questionId.length != questionDesc.length)
				|| (questionId.length != solutionMethod.length)) {
			throw new RuntimeException("问题元素的数组长度不一致");
		}
		List<WorkOrderProblem> problems = Lists
				.newArrayListWithCapacity(questionId.length);
		for (int i = 0; i < questionId.length; i++) {
			WorkOrderProblem problem = new WorkOrderProblem(workOrderNo,
					productId[i], Long.parseLong(questionId[i]),
					Integer.parseInt(questionTypId[i]),
					Integer.parseInt(questionTypPId[i]), questionDesc[i],
					solutionMethod[i]);
			problems.add(problem);
		}
		return problems;
	}
	/**
	 * 提交工单回执
	 * @param httpServletRequest
	 * @return
	 */
	@RequestMapping(value = "apply-receipt", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public String applyReceipt(HttpServletRequest httpServletRequest) {
		WorkOrderReceipt receipt = null;
		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;
		try {
			receipt = generateWorkOrderReceipt(httpServletRequest);
			workOrderService.applyWorkOrderReceipt(receipt);
			resultCode = SystemConstants.result_code_on_success;
		} catch (Exception ex) {
			LOG.error("提交工单回执失败,receipt:" + receipt, ex);
			results.put("error", ex.getMessage());
		}
		System.out.println("applyReceipt workOrderNo:"
				+ httpServletRequest.getParameter("workOrderNo") + ", result:"
				+ resultCode);
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);
	}

	private WorkOrderReceipt generateWorkOrderReceipt(HttpServletRequest request) {

		Date now = new Date();
		WorkOrderReceipt receipt = new WorkOrderReceipt();
		receipt.setCustomerUpdatFlag(request.getParameter("customerUpdatFlag"));
		String workOrderNo = request.getParameter("workOrderNo");
		receipt.setWorkOrderNo(workOrderNo);
		String serviceDate = request.getParameter("serviceDate");
		if (StringUtils.isEmpty(serviceDate)) {
			receipt.setServiceDate(now);
		} else {
			serviceDate = serviceDate.replace("：", ":");
			if(serviceDate.length()==16){
				serviceDate = serviceDate+":00";
			}else if(serviceDate.length()== 17){
				serviceDate = serviceDate+"00";
			}
			receipt.setServiceDate(DateTimeUtil
					.parseAsYYYYMMddHHmmss(serviceDate));
		}
		receipt.setOrderCode(request.getParameter("orderCode"));
		receipt.setCustomerAddr(request.getParameter("customerAddr"));
		receipt.setLatitude(request.getParameter("latitude"));
		receipt.setLongitude(request.getParameter("longitude"));
		receipt.setLinkPerson(request.getParameter("linkPerson"));
		receipt.setCustomerTelphone(request.getParameter("customerTelphone"));
		receipt.setCustomerMobile(request.getParameter("customerMobile"));
		String arriveDate = request.getParameter("arriveDate");
		if (StringUtils.isEmpty(arriveDate)) {
			receipt.setArriveDate(now);
		} else {
			receipt.setArriveDate(DateTimeUtil
					.parseAsYYYYMMddHHmmss(arriveDate));
		}
		String serviceEndDate = request.getParameter("serviceEndDate");
		if (StringUtils.isEmpty(serviceEndDate)) {
			receipt.setServiceEndDate(now);
		} else {
			receipt.setServiceEndDate(DateTimeUtil
					.parseAsYYYYMMddHHmmss(serviceEndDate));
		}
		receipt.setCustomerSignature(request.getParameter("customerSignature"));
		receipt.setCustomerIfSatisfied_product(request
				.getParameter("customerIfSatisfied_product"));
		receipt.setCustomerIfSatisfied_service(request
				.getParameter("customerIfSatisfied_service"));
		receipt.setSpendingProcess(request.getParameter("spendingProcess"));
		receipt.setFlag(request.getParameter("flag"));
		receipt.setNotes(request.getParameter("notes"));
		
		receipt.setIsCharge(request.getParameter("isCharge"));
		receipt.setIsChargeValue(request.getParameter("isChargeValue"));

		String[] productId;
		if (!StringUtils.isEmpty(request.getParameter("productId"))) {
			productId = StringUtils.split(request.getParameter("productId"),
					"^|");
		} else {
			productId = new String[0];
		}

		String[] questionId;
		if (!StringUtils.isEmpty(request.getParameter("questionId"))) {
			questionId = StringUtils.split(request.getParameter("questionId"),
					"^|");
		} else {
			questionId = new String[0];
		}
		String[] questionTypId;
		if (!StringUtils.isEmpty(request.getParameter("questionTypId"))) {
			questionTypId = StringUtils.split(
					request.getParameter("questionTypId"), "^|");
		} else {
			questionTypId = new String[0];
		}
		String[] questionTypPId;
		if (!StringUtils.isEmpty(request.getParameter("questionTypPId"))) {
			questionTypPId = StringUtils.split(
					request.getParameter("questionTypPId"), "^|");
		} else {
			questionTypPId = new String[0];
		}
		String[] questionDesc;
		if (!StringUtils.isEmpty(request.getParameter("questionDesc"))) {
			questionDesc = StringUtils.split(
					request.getParameter("questionDesc"), "^|");
		} else {
			questionDesc = new String[0];
		}
		String[] solutionMethod;
		if (!StringUtils.isEmpty(request.getParameter("solutionMethod"))) {
			solutionMethod = StringUtils.split(
					request.getParameter("solutionMethod"), "^|");
		} else {
			solutionMethod = new String[0];
		}
		List<WorkOrderProblem> problems = generateWorkOrderProblems(
				workOrderNo, productId, questionId, questionTypId,
				questionTypPId, questionDesc, solutionMethod);
		receipt.setProblems(problems);

		String[] spendingItemId;
		if (!StringUtils.isEmpty(request.getParameter("spendingItemId"))) {
			spendingItemId = StringUtils.split(
					request.getParameter("spendingItemId"), "^|");
		} else {
			spendingItemId = new String[0];
		}
		String[] spendingName;
		if (!StringUtils.isEmpty(request.getParameter("spendingName"))) {
			spendingName = StringUtils.split(
					request.getParameter("spendingName"), "^|");
		} else {
			spendingName = new String[0];
		}
		String[] spending;
		if (!StringUtils.isEmpty(request.getParameter("spending"))) {
			spending = StringUtils
					.split(request.getParameter("spending"), "^|");
		} else {
			spending = new String[0];
		}

		List<SpendingInfo> spendings = generateSpendings(workOrderNo,
				spendingItemId, spendingName, spending);
		receipt.setSpendings(spendings);

		String visitType = request.getParameter("visitType");
	    receipt.setVisitType(visitType);
		String productStatus = request.getParameter("productStatus");
		receipt.setProductStatus(productStatus);
		String handleResult = request.getParameter("handleResult");
		receipt.setHandleResult(handleResult);

		String hardwareCode = request.getParameter("hardwareCode");
		if (!StringUtils.isEmpty(hardwareCode)) {
			receipt.setHardwareCode(hardwareCode);
		}

		String softwareVersion = request.getParameter("softwareVersion");
		if (!StringUtils.isEmpty(softwareVersion)) {
			receipt.setSoftwareVersion(softwareVersion);
		}

		String environment = request.getParameter("environment");
		if (!StringUtils.isEmpty(environment)) {
			receipt.setEnvironment(environment);
		}
        System.out.println("返回的工单，转换为对象后："+receipt.toString());
        return receipt;

	}

	private List<SpendingInfo> generateSpendings(String workOrderNo,
			String[] spendingItemId, String[] spendingName, String[] spending) {
		if ((spendingItemId.length != spendingName.length)
				|| spendingItemId.length != spending.length)
			throw new RuntimeException("费用元素的数组长度不一致");
		List<SpendingInfo> spendings = Lists
				.newArrayListWithCapacity(spendingItemId.length);
		for (int i = 0; i < spendingItemId.length; i++) {
			SpendingInfo _spendingInfo = new SpendingInfo(workOrderNo,
					Integer.parseInt(spendingItemId[i]), spendingName[i],
					spending[i]);
			spendings.add(_spendingInfo);
		}
		return spendings;
	}
	/**
	 * 提交位置信息
	 * @param command
	 * @return
	 */
	@RequestMapping(value = "apply-track", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public String applyTrackInfo(CreateTrackInfoCommand command) {
//        System.out.println("applyTrackInfo start");
        Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;
		try {
			trackInfoService.newTrackInfo(command);
//            System.out.println("applyTrackInfo try");
            resultCode = SystemConstants.result_code_on_success;
		} catch (Exception ex) {
			LOG.error("提交位置信息失败,command:" + command, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);
	}

	@RequestMapping(value = "remove-image", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public String removeImage(
			@RequestParam(required = true) String fieldworkerId,
			@RequestParam(required = true) String workOrderNo,
			@RequestParam String imageFileName) {
		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;
		if (ImageUtil
				.removeImageFile(fieldworkerId, workOrderNo, imageFileName)) {
			resultCode = SystemConstants.result_code_on_success;
		}
		results.put("resultCode", resultCode);
		return JSONUtils.toJSONString(results);
	}

	@RequestMapping(value = "remove-audio", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public String removeAudio(
			@RequestParam(required = true) String fieldworkerId,
			@RequestParam(required = true) String workOrderNo,
			@RequestParam(required = true) String audioFileName) {

		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;
		if (AudioUtil
				.removeAudioFile(fieldworkerId, workOrderNo, audioFileName)) {
			resultCode = SystemConstants.result_code_on_success;
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);

	}
	/**
     *修改客户基本信息入口
	 * @param cust_name
	 * @param cust_tax_code
	 * @param addr
	 * @param contract
	 * @param tel
	 * @param mobile
	 * @return
	 *  @author chenjl
	 *  @version 2014年6月11日 下午8:03:25
	 */
	@RequestMapping(value = "cust_info", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public String updateCustomerInfo(
			@RequestParam(required = true) String cust_name,
			@RequestParam(required = true) String cust_tax_code,
			@RequestParam(required = true) String addr,
			@RequestParam(required = true) String contract,
			@RequestParam(required = true) String tel,
			@RequestParam(required = true) String mobile){
		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;
		try {
			customerService.updateCustomerInfo(cust_name, cust_tax_code, addr, contract, tel, mobile);
			resultCode = SystemConstants.result_code_on_success;
		} catch (Exception ex) {
			LOG.error("修改客户基本信息失败:",ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);
	}

}
