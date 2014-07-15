package hn.join.fieldwork.service;

import hn.join.fieldwork.domain.Customer;
import hn.join.fieldwork.domain.FieldWorker;
import hn.join.fieldwork.domain.SpendingInfo;
import hn.join.fieldwork.domain.SyncDataTransferObject;
import hn.join.fieldwork.domain.SyncDataTransferObject.DtoType;
import hn.join.fieldwork.domain.WorkOrder;
import hn.join.fieldwork.domain.WorkOrder.ChargeType;
import hn.join.fieldwork.domain.WorkOrder.WorkOrderStatus;
import hn.join.fieldwork.domain.WorkOrder.WorkOrderType;
import hn.join.fieldwork.domain.WorkOrderProblem;
import hn.join.fieldwork.domain.WorkOrderReceipt;
import hn.join.fieldwork.persistence.CustomerMapper;
import hn.join.fieldwork.persistence.FieldWorkerMapper;
import hn.join.fieldwork.persistence.SQLQueryResult;
import hn.join.fieldwork.persistence.SpendingInfoMapper;
import hn.join.fieldwork.persistence.WorkOrderMapper;
import hn.join.fieldwork.persistence.WorkOrderProblemMapper;
import hn.join.fieldwork.persistence.WorkOrderReceiptMapper;
import hn.join.fieldwork.service.SystemEventBus.FinishOrderRequest;
import hn.join.fieldwork.service.SystemEventBus.NewOrderRequest;
import hn.join.fieldwork.service.SystemEventBus.SendClientMessageRequest;
import hn.join.fieldwork.service.SystemEventBus.WorkOrderConfirmRequest;
import hn.join.fieldwork.utils.XmlUtil;
import hn.join.fieldwork.web.command.WorkOrderLess;
import hn.join.fieldwork.web.command.WorkOrderTimeout;
import hn.join.fieldwork.web.dto.CustomerDto;
import hn.join.fieldwork.web.dto.OnArriveFeedbackDto;
import hn.join.fieldwork.web.dto.OnCallCustomerFeedbackDto;
import hn.join.fieldwork.web.dto.OnFinishFeedbackDto;
import hn.join.fieldwork.web.dto.OnOrderViewedFeedbackDto;
import hn.join.fieldwork.web.dto.OnReassignFeedbackDto;
import hn.join.fieldwork.web.dto.OnTerminalReceiveFeedbackDto;
import hn.join.fieldwork.web.dto.ReceiptFeedbackDto;
import hn.join.fieldwork.web.dto.ReceiptFeedbackDto.ReceiptHead;
import hn.join.fieldwork.web.dto.ReceiptFeedbackDto.ReceiptQuestion;
import hn.join.fieldwork.web.dto.ReceiptFeedbackDto.ReceiptSpendingInfo;
import hn.join.fieldwork.web.dto.RemoteSolutionFeedbackDto;
import hn.join.fieldwork.web.dto.RemoteSolutionFeedbackDto.RemoteSolutionHead;
import hn.join.fieldwork.web.dto.RemoteSolutionFeedbackDto.RemoteSolutionQuestion;
import hn.join.fieldwork.web.dto.WorkOrderDto;
import hn.join.fieldwork.web.dto.WorkOrderDto.ProblemDto;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

/**
 * 工单
 * 
 * @author aisino_lzw
 * 
 */
@Service
public class WorkOrderService {

	private final static Logger LOG = Logger
			.getLogger(WorkOrderService.class);

	@Autowired
	private WorkOrderMapper workOrderMapper;

	@Autowired
	private WorkOrderReceiptMapper workOrderReceiptMapper;

	@Autowired
	private FieldWorkerMapper fieldWorkerMapper;

	@Autowired
	private CustomerMapper customerMapper;

	@Autowired
	private WorkOrderProblemMapper workOrderProblemMapper;

	@Autowired
	private SpendingInfoMapper spendingInfoMapper;

	@Autowired
	private SystemEventBus systemEventBus;

	@Autowired
	private OrderTokenService orderTokenService;

	@Autowired
	private SmsClientService smsClientService;

	@Autowired
	private SystemParameterService systemParameterService;

	@Autowired
	private ProductService productService;

	/**
	 * 创建工单
	 * 
	 * @param workOrderDto
	 * @throws DataAccessException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void createWorkOrder(WorkOrderDto workOrderDto)
			throws DataAccessException {

		WorkOrder workOrder = newWorkOrder(workOrderDto);
		Customer customer = newCustomer(workOrderDto.getCustomerDto(),
				workOrderDto.getCity());
		// if(workOrderDto.getProductId()!=null)
		// workOrderDto.setProductName(productService.getNameById(workOrderDto.getProductId()));

		FieldWorker fieldWorker = fieldWorkerMapper.getById(workOrder
				.getFieldWorkerId());
		//判断是否为改派工单
		if(workOrder.getUpdateId()!= 0){
			workOrderMapper.updateWorkOrder(workOrder);
		}else {
			workOrderMapper.insertWorkOrder(workOrder);
		}

		Long customerCount = customerMapper.countById(customer.getId());
		if (customerCount == null || customerCount == 0) {
			customerMapper.insertCustomer(customer);
		} else {
			customerMapper.updateCustomer(customer);
		}

		if (workOrderDto.getProblemList() != null
				&& workOrderDto.getProblemList().size() > 0) {
			List<WorkOrderProblem> problems = newWorkOrderProblem(
					workOrderDto.getProblemList(),
					String.valueOf(workOrderDto.getWorkCardId()));
			//判断是否为改派工单
			if(workOrder.getUpdateId()!=0){
				for (WorkOrderProblem workOrderProblem : problems) {
					workOrderProblemMapper.updateProblemList(workOrderProblem);
				}
			}else{
				workOrderProblemMapper.insertProblems(problems);
			}
		}
		NewOrderRequest newOrderRequest = new NewOrderRequest(workOrder, workOrderDto, fieldWorker.getFullname());
		LOG.debug("发送的工单："+newOrderRequest);
		systemEventBus
				.postFieldWorkerUpdateRequestOnNewOrder(newOrderRequest);
		
	}

	/**
	 * 根据工单id获取工单
	 * 
	 * @param workOrderNo
	 * @return
	 */
	public WorkOrder getByWorkOrderNo(String workOrderNo) {
		return workOrderMapper.getByWorkOrderNo(workOrderNo);
	}

	/**
	 * 确认工单
	 * 
	 * @param fieldWorkerId
	 * @param workOrderNo
	 * @param confirmTime
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public void confirm(Long fieldWorkerId, String workOrderNo, Date confirmTime)
			throws Exception {

		long confirmTimeCount = workOrderMapper.countStatusByWorkOrderNo(
				workOrderNo, "confirm_time");
		if (confirmTimeCount < 1) {
			workOrderMapper.updateWhenConfirm(workOrderNo, confirmTime);
			systemEventBus
					.postWorkOrderConfirmRequest(new WorkOrderConfirmRequest(
							workOrderNo, confirmTime));
			SendClientMessageRequest sendClientMessageRequest = new SendClientMessageRequest(
					workOrderNo);
			systemEventBus.postSendClientMessage(sendClientMessageRequest);
			SyncDataTransferObject syncObject = createByOnTerminalReceiveFeedbackDto(
					workOrderNo, confirmTime);
			systemEventBus.postSyncData(syncObject);
		}

	}

	/**
	 * 创建同步对象
	 * 
	 * @param workOrderNo
	 * @param confirmTime
	 * @return
	 * @throws Exception
	 */
	private SyncDataTransferObject createByOnTerminalReceiveFeedbackDto(
			String workOrderNo, Date confirmTime) throws Exception {
		OnTerminalReceiveFeedbackDto dtoObject = new OnTerminalReceiveFeedbackDto(
				workOrderNo, confirmTime);
		String dtoXml = XmlUtil.toXml(dtoObject);
		SyncDataTransferObject syncObject = new SyncDataTransferObject(
				DtoType.order_received, dtoXml);
		return syncObject;
	}

	/**
	 * 工单展示
	 * 
	 * @param fieldWorkerId
	 * @param workOrderNo
	 * @param viewTime
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public void view(Long fieldWorkerId, String workOrderNo, Date viewTime)
			throws Exception {
		long viewTimeCount = workOrderMapper.countStatusByWorkOrderNo(
				workOrderNo, "view_time");
		if (viewTimeCount < 1) {
			workOrderMapper.updateWhenView(workOrderNo, viewTime);
			SyncDataTransferObject syncObject = createByOnOrderViewedFeedbackDto(
					workOrderNo, viewTime);
			systemEventBus.postSyncData(syncObject);
		}
	}

	/**
	 * 根据工单规则创建同步对象
	 * 
	 * @param workOrderNo
	 * @param viewTime
	 * @return
	 * @throws Exception
	 */
	private SyncDataTransferObject createByOnOrderViewedFeedbackDto(
			String workOrderNo, Date viewTime) throws Exception {
		OnOrderViewedFeedbackDto dtoObject = new OnOrderViewedFeedbackDto(
				workOrderNo, viewTime);
		String dtoXml = XmlUtil.toXml(dtoObject);
		SyncDataTransferObject syncObject = new SyncDataTransferObject(
				DtoType.order_view, dtoXml);
		return syncObject;
	}

	/**
	 * 超时处理
	 * 
	 * @param fieldWorkerId
	 * @param workOrderNo
	 */
	@Transactional(rollbackFor = Exception.class)
	public void timeout(Long fieldWorkerId, String workOrderNo) {
		workOrderMapper.updateWhenTimeout(workOrderNo);
		if ("1".equals(systemParameterService
				.getValueByName(SystemParameterService.PARAM_NAME_SMS_FIELDWORKER_ON_TIMEOUT_ENABLE))) {
			FieldWorker fieldWorker = fieldWorkerMapper
					.getFieldWorkerWithDeviceById(fieldWorkerId);
			String smsMessage = systemParameterService
					.getValueByName(SystemParameterService.PARAM_NAME_SMS_FIELDWORKER_ON_TIMEOUT_MESSAGE)
					+ workOrderNo;
			smsClientService.sendMessage(fieldWorker.getDevice().getPhoneNo(),
					smsMessage);
		}

	}

	/**
	 * 接收处理
	 * 
	 * @param fieldWorkerId
	 * @param workOrderNo
	 * @param acceptTime
	 */
	@Transactional(rollbackFor = Exception.class)
	public void accept(Long fieldWorkerId, String workOrderNo, Date acceptTime) {
		long acceptTimeCount = workOrderMapper.countStatusByWorkOrderNo(
				workOrderNo, "accept_time");
		if (acceptTimeCount < 1) {
			workOrderMapper.updateWhenAccept(workOrderNo, acceptTime);
			if ("1".equals(systemParameterService
					.getValueByName(SystemParameterService.PARAM_NAME_SMS_CUSTOMER_ON_ACCEPT_ENABLE))) {
				String customerMobile = workOrderMapper
						.getCustomerMobileByWorkOrderNo(workOrderNo);
				if (!StringUtils.isEmpty(customerMobile)) {
					FieldWorker fieldWorker = fieldWorkerMapper
							.getFieldWorkerWithDeviceById(fieldWorkerId);
					String smsMessage = systemParameterService
							.getValueByName(SystemParameterService.PARAM_NAME_SMS_CUSTOMER_ON_ACCEPT_MESSAGE)
							+ " 员工姓名:"
							+ fieldWorker.getFullname()
							+ ",电话:"
							+ fieldWorker.getDevice().getPhoneNo();
					smsClientService.sendMessage(customerMobile, smsMessage);
				}
			}
		}

	}

	/**
	 * 分配工单
	 * 
	 * @param fieldWorkerId
	 * @param workOrderNo
	 * @param reason
	 * @param reassignTime
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public void reassign(Long fieldWorkerId, String workOrderNo, String reason,
			Date reassignTime) throws Exception {
		System.out.println("reassign call()");
		long reassignTimeCount = workOrderMapper.countStatusByWorkOrderNo(
				workOrderNo, "reassign_time");

		if (reassignTimeCount < 1) {
			System.out.println("reassign create");
			workOrderMapper.updateWhenReassign(workOrderNo, reason,
					reassignTime);
			WorkOrder workOrder = workOrderMapper.getByWorkOrderNo(workOrderNo);
			FieldWorker fieldWorker = fieldWorkerMapper.getById(fieldWorkerId);
			systemEventBus
					.postFieldWorkerUpdateRequestOnFinishOrder(new FinishOrderRequest(
							workOrder.getAreaCode(), fieldWorker.getId(),
							fieldWorker.getFullname(), reassignTime.getTime(),
							workOrderNo));
			SyncDataTransferObject syncObject = createByOnReassignFeedbackDto(
					workOrderNo, reason, reassignTime);
			systemEventBus.postSyncData(syncObject);
		}
	}

	/**
	 * 根据分配规则创建同步对象
	 * 
	 * @param workOrderNo
	 * @param reason
	 * @param reassignTime
	 * @return
	 * @throws Exception
	 */
	private SyncDataTransferObject createByOnReassignFeedbackDto(
			String workOrderNo, String reason, Date reassignTime)
			throws Exception {
		OnReassignFeedbackDto dtoObject = new OnReassignFeedbackDto(
				workOrderNo, reason, reassignTime);
		String dtoXml = XmlUtil.toXml(dtoObject);
		SyncDataTransferObject syncObject = new SyncDataTransferObject(
				DtoType.order_reassign, dtoXml);
		return syncObject;

	}

	/**
	 * 关联客户
	 * 
	 * @param fieldWorkerId
	 * @param workOrderNo
	 * @param callTime
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public void callCustomer(Long fieldWorkerId, String workOrderNo,
			Date callTime) throws Exception {
		long callTimeCount = workOrderMapper.countStatusByWorkOrderNo(
				workOrderNo, "call_time");
		if (callTimeCount < 1) {
			workOrderMapper.updateWhenCallCustomer(workOrderNo, callTime);
			SyncDataTransferObject syncObject = createByOnCallCustomerFeedbackDto(
					workOrderNo, callTime);
			systemEventBus.postSyncData(syncObject);
		}
	}

	/**
	 * 根据关联客户规则创建同步对象
	 * 
	 * @param workOrderNo
	 * @param callTime
	 * @return
	 * @throws Exception
	 */
	private SyncDataTransferObject createByOnCallCustomerFeedbackDto(
			String workOrderNo, Date callTime) throws Exception {
		OnCallCustomerFeedbackDto dtoObject = new OnCallCustomerFeedbackDto(
				workOrderNo, callTime);
		String dtoXml = XmlUtil.toXml(dtoObject);
		SyncDataTransferObject syncObject = new SyncDataTransferObject(
				DtoType.customer_call, dtoXml);
		return syncObject;
	}

	/**
	 * 到达现场处理
	 * 
	 * @param fieldWorkerId
	 * @param workOrderNo
	 * @param arriveTime
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public void arrival(Long fieldWorkerId, String workOrderNo, Date arriveTime)
			throws Exception {
		long arrivalTimeCount = workOrderMapper.countStatusByWorkOrderNo(
				workOrderNo, "arrival_time");
		if (arrivalTimeCount < 1) {
			workOrderMapper.updateWhenArrival(workOrderNo, arriveTime);
			SyncDataTransferObject syncObject = createByOnArriveFeedbackDto(
					workOrderNo, arriveTime);
			systemEventBus.postSyncData(syncObject);
		}
	}

	/**
	 * 根据到达现场返回状态创建同步对象
	 * 
	 * @param workOrderNo
	 * @param arriveTime
	 * @return
	 * @throws Exception
	 */
	private SyncDataTransferObject createByOnArriveFeedbackDto(
			String workOrderNo, Date arriveTime) throws Exception {
		OnArriveFeedbackDto dtoObject = new OnArriveFeedbackDto(workOrderNo,
				arriveTime);
		String dtoXml = XmlUtil.toXml(dtoObject);
		SyncDataTransferObject syncObject = new SyncDataTransferObject(
				DtoType.dest_arrive, dtoXml);
		return syncObject;

	}

	/**
	 * 完成工单处理
	 * 
	 * @param fieldWorkerId
	 * @param workOrderNo
	 * @param finishTime
	 * @param problemSolved
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public void finish(Long fieldWorkerId, String workOrderNo, Date finishTime,
			String problemSolved) throws Exception {
		long finishTimeCount = workOrderMapper.countStatusByWorkOrderNo(
				workOrderNo, "finish_time");
		if (finishTimeCount < 1) {
			updateFieldWorkerOnFinish(workOrderNo, finishTime);
			SyncDataTransferObject syncObject = createByOnFinishFeedbackDto(
					workOrderNo, finishTime, problemSolved);
			systemEventBus.postSyncData(syncObject);
		}

	}

	/**
	 * 更新外勤员工信息当完成工单时
	 * 
	 * @param workOrderNo
	 * @param finishTime
	 */
	private void updateFieldWorkerOnFinish(String workOrderNo, Date finishTime) {
		workOrderMapper.updateWhenFinish(workOrderNo, finishTime);
		WorkOrder workOrder = workOrderMapper.getByWorkOrderNo(workOrderNo);
		FieldWorker fieldWorker = fieldWorkerMapper.getById(workOrder
				.getFieldWorkerId());
		systemEventBus
				.postFieldWorkerUpdateRequestOnFinishOrder(new FinishOrderRequest(
						workOrder.getAreaCode(), fieldWorker.getId(),
						fieldWorker.getFullname(), finishTime.getTime(),
						workOrderNo));
	}

	/**
	 * 创建完成工单返回状态
	 * 
	 * @param workOrderNo
	 * @param finishTime
	 * @param problemSolved
	 * @return
	 * @throws Exception
	 */
	private SyncDataTransferObject createByOnFinishFeedbackDto(
			String workOrderNo, Date finishTime, String problemSolved)
			throws Exception {
		OnFinishFeedbackDto dtoObject = new OnFinishFeedbackDto(workOrderNo,
				finishTime, problemSolved);
		String dtoXml = XmlUtil.toXml(dtoObject);
		SyncDataTransferObject syncObject = new SyncDataTransferObject(
				DtoType.order_finish, dtoXml);
		return syncObject;

	}

	/**
	 * 接受远程解决
	 * 
	 * @param workOrderNo
	 * @param problems
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public void applyRemoteSolution(String workOrderNo,
			List<WorkOrderProblem> problems) throws Exception {
		long finishTimeCount = workOrderMapper.countStatusByWorkOrderNo(
				workOrderNo, "finish_time");
		if (finishTimeCount < 1) {
			Date finishTime = new Date();
			updateFieldWorkerOnFinish(workOrderNo, finishTime);
			for (WorkOrderProblem problem : problems)
				workOrderProblemMapper.updateProblem(problem);
			SyncDataTransferObject syncObject = createByRemoteSolutionFeedbackDto(
					workOrderNo, problems);
			systemEventBus.postSyncData(syncObject);
		}

	}

	/**
	 * 创建远程解决返回状态
	 * 
	 * @param workOrderNo
	 * @param problems
	 * @return
	 * @throws Exception
	 */
	private SyncDataTransferObject createByRemoteSolutionFeedbackDto(
			String workOrderNo, List<WorkOrderProblem> problems)
			throws Exception {
		RemoteSolutionFeedbackDto dtoObject = new RemoteSolutionFeedbackDto();
		LinkedList<RemoteSolutionQuestion> questions = Lists.newLinkedList();
		for (WorkOrderProblem problem : problems) {
			RemoteSolutionQuestion question = new RemoteSolutionQuestion();
			question.setQuestionId(problem.getsId());
			question.setProductId(problem.getProductId());
			question.setQuestionTypId(problem.getProblemTypeId());
			question.setQuestionTypPId(problem.getProblemCategoryId());
			question.setQuestionDesc(problem.getProblemDetail());
			question.setSolutionMethod(problem.getSolutionMethod());
			questions.add(question);
		}
		dtoObject.setHead(new RemoteSolutionHead(workOrderNo));
		dtoObject.setQuestionBody(questions);
		String dtoXml = XmlUtil.toXml(dtoObject);
		SyncDataTransferObject syncObject = new SyncDataTransferObject(
				DtoType.solution_by_remote, dtoXml);
		return syncObject;
	}

	/**
	 * 接收工单
	 * 
	 * @param receipt
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public void applyWorkOrderReceipt(WorkOrderReceipt receipt)
			throws Exception {
		int _count = workOrderReceiptMapper.countByWorkOrderNo(receipt
				.getWorkOrderNo());
		if (_count == 0) {
			Date finishTime = new Date();
			updateCustomerOnApplyReceipt(receipt);
			updateFieldWorkerOnFinish(receipt.getWorkOrderNo(), finishTime);
			workOrderReceiptMapper.insertWorkOrderReceipt(receipt);
			if (receipt.getProblems() != null
					&& !receipt.getProblems().isEmpty())
				for (WorkOrderProblem problem : receipt.getProblems())
					workOrderProblemMapper.updateProblem(problem);
			if (receipt.getSpendings() != null
					&& !receipt.getSpendings().isEmpty())
				spendingInfoMapper.insertSpendings(receipt.getSpendings());
			SyncDataTransferObject syncObject = createByReceiptFeedbackDto(receipt);
			systemEventBus.postSyncData(syncObject);
		}
	}

	/**
	 * 更新工单接收返回状态
	 * 
	 * @param receipt
	 */
	private void updateCustomerOnApplyReceipt(WorkOrderReceipt receipt) {
		Long customerId = workOrderMapper
				.findCustomerIdByWorkOrderNo(receipt.getWorkOrderNo());
		Customer customer = customerMapper.getById(customerId);
		customer.setCustomerAddress(receipt.getCustomerAddr());
		if (!StringUtils.isEmpty(receipt.getLatitude())
				&& Float.valueOf(receipt.getLatitude()).intValue() != 0
				&& !StringUtils.isEmpty(receipt.getLongitude())
				&& Float.valueOf(receipt.getLongitude()).intValue() != 0) {

			customer.setLatitude(Float.valueOf(receipt.getLatitude()));
			customer.setLongitude(Float.valueOf(receipt.getLongitude()));
		}
		customer.setContact(receipt.getLinkPerson());
		customer.setTel(receipt.getCustomerTelphone());
		customer.setMobile(receipt.getCustomerMobile());
		customerMapper.updateCustomer(customer);

	}

	/**
	 * 创建工单接收返回状态
	 * 
	 * @param receipt
	 * @return
	 * @throws Exception
	 */
	private SyncDataTransferObject createByReceiptFeedbackDto(
			WorkOrderReceipt receipt) throws Exception {
		ReceiptFeedbackDto dtoObject = new ReceiptFeedbackDto();
		ReceiptHead receiptHead = new ReceiptHead();
		receiptHead.setWorkCardId(receipt.getWorkOrderNo());
		receiptHead.setCustomerUpdatFlag(receipt.getCustomerUpdatFlag());
		if (StringUtils.isEmpty(receipt.getVisitType())) {
			receiptHead.setOrderTyp("0");
		} else {
			receiptHead.setOrderTyp("1");
		}
		// receiptHead.setOrderTyp("0");
		receiptHead.setServiceDate(receipt.getServiceDate());
		receiptHead.setOrderCode(receipt.getOrderCode());
		receiptHead.setCustomerAddr(receipt.getCustomerAddr());
		receiptHead.setLatitude(receipt.getLatitude());
		receiptHead.setLongitude(receipt.getLongitude());
		receiptHead.setLinkPerson(receipt.getLinkPerson());
		receiptHead.setCustomerTelphone(receipt.getCustomerTelphone());
		receiptHead.setCustomerMobile(receipt.getCustomerMobile());
		receiptHead.setArriveDate(receipt.getArriveDate());
		receiptHead.setServiceEndDate(receipt.getServiceEndDate());
		receiptHead.setCustomerSignature(receipt.getCustomerSignature());
		receiptHead.setCustomerIfSatisfied_product(receipt
				.getCustomerIfSatisfied_product());
		receiptHead.setCustomerIfSatisfied_service(receipt
				.getCustomerIfSatisfied_service());
		receiptHead.setSpendingProcess(receipt.getSpendingProcess());
		receiptHead.setFlag(receipt.getFlag());
		receiptHead.setNotes(receipt.getNotes());
		receiptHead.setHandleResult(receipt.getHandleResult());
		receiptHead.setProductStatus(receipt.getProductStatus());
		receiptHead.setHardwareCode(receipt.getHardwareCode());
		receiptHead.setSoftwareVersion(receipt.getSoftwareVersion());
		receiptHead.setEnvironment(receipt.getEnvironment());
        receiptHead.setVisitType(receipt.getVisitType());

		//是否收费----------------------------------------------------------
		receiptHead.setIsCharge(receipt.getIsCharge());
		receiptHead.setIsChargeValue(receipt.getIsChargeValue());
		
		//----------------------------------------------------------
		dtoObject.setServiceHead(receiptHead);
		LinkedList<ReceiptQuestion> questionBody = Lists.newLinkedList();
		for (WorkOrderProblem problem : receipt.getProblems()) {
			ReceiptQuestion question = new ReceiptQuestion();
			question.setQuestionId(problem.getsId());
			question.setProductId(problem.getProductId());
			question.setQuestionTypId(problem.getProblemTypeId());
			question.setQuestionTypPId(problem.getProblemCategoryId());
			question.setQuestionDesc(problem.getProblemDetail());
			question.setSolutionMethod(problem.getSolutionMethod());
			questionBody.add(question);
		}
		dtoObject.setQuestionBody(questionBody);

		LinkedList<ReceiptSpendingInfo> spendingInfoBody = Lists
				.newLinkedList();
		spendingInfoBody.add(ReceiptFeedbackDto.DEFAULT_SPENDING_ITEM);
		if (receipt.getSpendings() != null && receipt.getSpendings().size() > 0) {
			for (SpendingInfo spending : receipt.getSpendings()) {
				ReceiptSpendingInfo spendingInfo = new ReceiptSpendingInfo();
				spendingInfo.setSpendingItemId(spending.getSpendingItemId());
				spendingInfo.setSpendingName(spending.getSpendingName());
				spendingInfo.setSpending(spending.getSpending());
				spendingInfoBody.add(spendingInfo);
			}
		}

		dtoObject.setSpendingInfoBody(spendingInfoBody);
		String dtoXml = XmlUtil.toXml(dtoObject);
		SyncDataTransferObject syncObject = new SyncDataTransferObject(
				DtoType.receipt_feed, dtoXml);
		return syncObject;

	}

	/**
	 * 根据员工id查询未完成工单
	 * 
	 * @param fieldWorkerId
	 * @return
	 */
	public List<String> findUnfinishedWorkOrdersByFieldWorkerId(
			Long fieldWorkerId) {
		return workOrderMapper
				.findUnfinishedWorkOrdersByFieldWorkerId(fieldWorkerId);
	}

	/**
	 * 根据工单号查询未完成工单
	 * 
	 * @param workOrderNo
	 * @return
	 */
	public Long findFieldWorkerIdByWorkOrderNo(String workOrderNo) {
		return workOrderMapper.findFieldWorkerIdByWorkOrderNo(workOrderNo);
	}

	/**
	 * 检查客户信息
	 * 
	 * @param workOrderDto
	 */
	private void check(WorkOrderDto workOrderDto) {
		if (StringUtils.isEmpty(workOrderDto.getCustomerDto()
				.getCustomerMobile())
				&& StringUtils.isEmpty(workOrderDto.getCustomerDto()
						.getCustomerTel())) {
			throw new IllegalStateException("客户信息不完整,手机和电话号码都为空");
		}
	}

	/**
	 * 创建新工单
	 * 
	 * @param workOrderDto
	 * @return
	 */
	private WorkOrder newWorkOrder(WorkOrderDto workOrderDto) {
		check(workOrderDto);
		Date now = new Date();
		WorkOrder order = new WorkOrder();
		order.setWorkOrderNo(String.valueOf(workOrderDto.getWorkCardId()));
		order.setServiceDate(workOrderDto.getServiceDate());
		int _workOrderType = workOrderDto.getWorkOrderType();
		WorkOrderType workOrderType;
		String orderToken;
		switch (_workOrderType) {
		case 1:
			workOrderType = WorkOrderType.repair;
			orderToken = orderTokenService.getRepairToken();
			break;
		case 2:
			workOrderType = WorkOrderType.visit;
			orderToken = orderTokenService.getVisitToken();
			break;
		default:
			workOrderType = WorkOrderType.repair;
			orderToken = orderTokenService.getRepairToken();
		}
		order.setWorkOrderType(workOrderType);
		if (workOrderType == WorkOrderType.visit) {
			order.setVisitType(workOrderDto.getVisitType());
			order.setProductStatus(workOrderDto.getProductStatus());
			order.setHandleResult(workOrderDto.getHandleResult());
			if (!StringUtils.isEmpty(workOrderDto.getReturnProductId())) {
				String[] returnProductIdArray = StringUtils.split(
						workOrderDto.getReturnProductId(), ",");
				StringBuilder returnProductName = new StringBuilder();
				for (String _returnProductId : returnProductIdArray) {
					returnProductName.append(
							productService.getNameById(Integer
									.parseInt(_returnProductId))).append(",");
				}
				workOrderDto.setReturnProductName(returnProductName.substring(
						0, returnProductName.length() - 1));
			}

		}
		order.setOrderToken(orderToken);
		workOrderDto.setOrderToken(orderToken);

		order.setDescription(workOrderDto.getWorkOrderDescription());
		Integer _chargeType = workOrderDto.getChargeType();
		ChargeType chargeType;
		if (_chargeType == null) {
			chargeType = ChargeType.free;
		} else {
			switch (_chargeType) {
			case 1:
				chargeType = ChargeType.no_free;
				break;
			case 2:
				chargeType = ChargeType.free;
				break;
			default:
				chargeType = ChargeType.free;
				break;
			}
		}
		order.setChargeType(chargeType);
		order.setChargeMoney(workOrderDto.getChargeMoney() != null ? workOrderDto
				.getChargeMoney().floatValue() : 0f);
		CustomerDto customerDto = workOrderDto.getCustomerDto();
		order.setCustomerId(customerDto.getCustomerId());
		order.setCustomerName(customerDto.getCustomerName());
		order.setLinkPerson(customerDto.getLinkPerson());
		order.setCustomerAddress(customerDto.getCustomerAddr());
		order.setCustomerMobile(StringUtils.isEmpty(customerDto
				.getCustomerMobile()) ? customerDto.getCustomerTel()
				: customerDto.getCustomerMobile());
		order.setCustomerTel(customerDto.getCustomerTel());
		order.setExpectArriveTime(workOrderDto.getExpectArriveTime());
		order.setUrgency(workOrderDto.getUrgency());
		order.setReceiveTime(now);
		order.setDispatchTime(now);
		order.setFieldWorkerId(new Long(workOrderDto.getFieldWorkerId()));
		order.setStatus(WorkOrderStatus.dispatch);
		order.setAreaCode(workOrderDto.getCity());
		order.setUpdateId(workOrderDto.getUpdateId());
		// if (!StringUtils.isEmpty(workOrderDto.getReturnProductId()))
		// order.setReturnProductId(Integer.parseInt(workOrderDto
		// .getReturnProductId()));
		return order;

	}

	/**
	 * 新建客户
	 * 
	 * @param customerDto
	 * @param city
	 * @return
	 */
	private Customer newCustomer(CustomerDto customerDto, String city) {
		Customer customer = new Customer();
		customer.setId(customerDto.getCustomerId());
		customer.setContact(customerDto.getLinkPerson());
		customer.setCustomerName(customerDto.getCustomerName());
		customer.setCustomerAddress(customerDto.getCustomerAddr());
		customer.setAreaCode(city);
		customer.setCounty(customerDto.getCustomerCounty());
		customer.setTaxCode(customerDto.getTaxCode());
		customer.setMobile(customerDto.getCustomerMobile());
		customer.setTel(customerDto.getCustomerTel());
		customer.setRevenueId(customerDto.getRevenueId());
		customer.setDepartmentId(customerDto.getDepartmentId());
		customer.setLatitude(customerDto.getCustomerLatitude());
		customer.setLongitude(customerDto.getCustomerLongitude());
		return customer;
	}

	/**
	 * 新建问题工单
	 * 
	 * @param problemList
	 * @param workOrderNo
	 * @return
	 */
	private List<WorkOrderProblem> newWorkOrderProblem(
			List<ProblemDto> problemList, String workOrderNo) {
		List<WorkOrderProblem> problems = new ArrayList<WorkOrderProblem>(
				problemList.size());
		for (ProblemDto _problemDto : problemList) {
			_problemDto.setProductName(productService.getNameById(_problemDto
					.getProductId()));
			WorkOrderProblem _problem = new WorkOrderProblem(workOrderNo,
					_problemDto.getProblemId(), _problemDto.getQuestionTypId(),
					_problemDto.getQuestionTypPid(),
					_problemDto.getQuestionDesc(), _problemDto.getSolution(),
					String.valueOf(_problemDto.getProductId()),
					_problemDto.getProductName());
			problems.add(_problem);
		}
		return problems;

	}

	/**
	 * 查询某个时间段工单
	 * 
	 * @param fieldWorkerId
	 * @param fromTime
	 * @param toTime
	 * @return
	 */
	public List<WorkOrderLess> findWorkOrderLessFinishTimeBetween(
			Long fieldWorkerId, Date fromTime, Date toTime) {
		return workOrderMapper.findByBetweenFinishTime(fieldWorkerId, fromTime,
				toTime);
	}

	/**
	 * 查询超时工单
	 * 
	 * @param fullname
	 * @param city
	 * @param page
	 * @param rows
	 * @return
	 */
	public SQLQueryResult<WorkOrderTimeout> findWorkOrderTimeout(
			String fullname, String city, Integer page, Integer rows) {
		SQLQueryResult<WorkOrderTimeout> queryResult = null;

		long _count = workOrderMapper.countWorkOrderTimeoutBy(fullname, city);
		if (_count != 0) {
			Integer offset = null;
			if (page != null && rows != null)
				offset = (page - 1) * rows;
			List<WorkOrderTimeout> _result = workOrderMapper
					.findWorkOrderTimeoutBy(fullname, city, offset, rows);
			queryResult = new SQLQueryResult<WorkOrderTimeout>(_count, _result);
		} else {
			queryResult = SQLQueryResult.EMPTY_RESULT;
		}
		return queryResult;
	}

	/**
	 * 查询问题工单
	 * 
	 * @param workOrderNo
	 * @return
	 */
	public List<WorkOrderProblem> findWorkOrderProblem(String workOrderNo) {
		return workOrderProblemMapper.getByWorkOrderNo(workOrderNo);
	}
}
