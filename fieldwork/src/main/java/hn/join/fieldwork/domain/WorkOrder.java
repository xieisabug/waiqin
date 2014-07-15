package hn.join.fieldwork.domain;

import hn.join.fieldwork.utils.CustomDateTimeSerializer;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

public class WorkOrder extends BaseDomain {

	private static final long serialVersionUID = -4741663326612261915L;

	public enum WorkOrderStatus {
		dispatch, confirm,view,timeout, accept, reassign,call, arrival, finish;
	}

	public enum WorkOrderType {
		repair, visit;
	}

	public enum ChargeType {
		free, no_free;
	}
	/**主键*/
	private Long id;
	/**工单号*/
	private String workOrderNo;
	
	/**单据号*/
	private String orderToken;
	/**工单类别*/
	private WorkOrderType workOrderType;
	
	/**回访类型*/
	private String visitType;

	/**服务日期*/
	private Date serviceDate;
	/**产品ID*/
	private Integer productId;
	/**区域编码*/
	private String areaCode;
	/**是否收费*/
	private ChargeType chargeType;
	/**收费金额*/
	private float chargeMoney;

	/**派工说明*/
	private String description;
	/**客户ID*/
	private Long customerId;

	/**客户公司姓名*/
	private String customerName;
	
	/**客户联系人*/
	private String linkPerson;
	
	/**客户固定电话*/
	private String customerTel;
	/**客户移动电话*/
	private String customerMobile;
	/**客户地址*/
	private String customerAddress;
	/**工单要求服务时间*/
	private Date expectArriveTime;
	/**工单紧急程度*/
	private Integer urgency;
	/**外勤人员ID*/
	private Long fieldWorkerId;

	/**
	 * 本系统接收到上游系统工单的时间
	 */
	private Date receiveTime;

	/**
	 * 工单派发时间
	 */
	private Date dispatchTime;

	/**
	 * 终端接收到工单时间
	 */
	private Date confirmTime;

	
	/**
	 * 工单查看时间
	 */
	private Date viewTime;
	
	/**
	 * 外服人员确认接受订单的时间
	 */
	private Date acceptTime;

	/**
	 * 外服人员确认不接受订单的时间
	 */
	private Date reassignTime;

	/**
	 * 联系客户时间
	 */
	private Date callTime;
	
	/**
	 * 外服人员到达目的地时间
	 */
	private Date arrivalTime;

	/**
	 * 工单完成时间
	 */
	private Date finishTime;
	/**工单状态*/
	private WorkOrderStatus status;
	
	/**
	 * 外勤人员
	 */
	private FieldWorker fieldworker;
	
	/**
	 * 客户
	 */
	private Customer customer;
	
	/**
	 * 申请改派原因
	 */
	private String reason;

    /**
     * 产品运行状态
     */
    private String productStatus;


    /**
     * 处理结果
     */
    private String handleResult;
    
    private Integer returnProductId;
    
    private Integer updateId;
    

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getWorkOrderNo() {
		return workOrderNo;
	}

	public void setWorkOrderNo(String workOrderNo) {
		this.workOrderNo = workOrderNo;
	}
	
	
	
	

	public String getVisitType() {
		return visitType;
	}

	public void setVisitType(String visitType) {
		this.visitType = visitType;
	}

	public String getOrderToken() {
		return orderToken;
	}

	public void setOrderToken(String orderToken) {
		this.orderToken = orderToken;
	}

	@JsonSerialize(using = CustomDateTimeSerializer.class)
	public Date getServiceDate() {
		return serviceDate;
	}

	public void setServiceDate(Date serviceDate) {
		this.serviceDate = serviceDate;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	

	public String getLinkPerson() {
		return linkPerson;
	}

	public void setLinkPerson(String linkPerson) {
		this.linkPerson = linkPerson;
	}

	public String getCustomerTel() {
		return customerTel;
	}

	public void setCustomerTel(String customerTel) {
		this.customerTel = customerTel;
	}

	public String getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}

	@JsonIgnore
	public FieldWorker getFieldworker() {
		return fieldworker;
	}

	public void setFieldworker(FieldWorker fieldworker) {
		this.fieldworker = fieldworker;
	}

	@JsonSerialize(using = CustomDateTimeSerializer.class)
	public Date getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}

	@JsonSerialize(using = CustomDateTimeSerializer.class)
	public Date getDispatchTime() {
		return dispatchTime;
	}

	public void setDispatchTime(Date dispatchTime) {
		this.dispatchTime = dispatchTime;
	}

	@JsonSerialize(using = CustomDateTimeSerializer.class)
	public Date getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(Date confirmTime) {
		this.confirmTime = confirmTime;
	}
	
	
	@JsonSerialize(using = CustomDateTimeSerializer.class)
	public Date getViewTime() {
		return viewTime;
	}

	public void setViewTime(Date viewTime) {
		this.viewTime = viewTime;
	}

	@JsonSerialize(using = CustomDateTimeSerializer.class)
	public Date getAcceptTime() {
		return acceptTime;
	}

	public void setAcceptTime(Date acceptTime) {
		this.acceptTime = acceptTime;
	}

	@JsonSerialize(using = CustomDateTimeSerializer.class)
	public Date getReassignTime() {
		return reassignTime;
	}

	public void setReassignTime(Date reassignTime) {
		this.reassignTime = reassignTime;
	}
	
	@JsonSerialize(using = CustomDateTimeSerializer.class)
	public Date getCallTime() {
		return callTime;
	}

	public void setCallTime(Date callTime) {
		this.callTime = callTime;
	}

	@JsonSerialize(using = CustomDateTimeSerializer.class)
	public Date getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(Date arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	@JsonSerialize(using = CustomDateTimeSerializer.class)
	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	public WorkOrderType getWorkOrderType() {
		return workOrderType;
	}

	public void setWorkOrderType(WorkOrderType workOrderType) {
		this.workOrderType = workOrderType;
	}

	public ChargeType getChargeType() {
		return chargeType;
	}

	public void setChargeType(ChargeType chargeType) {
		this.chargeType = chargeType;
	}

	public String getCustomerMobile() {
		return customerMobile;
	}

	public void setCustomerMobile(String customerMobile) {
		this.customerMobile = customerMobile;
	}

	@JsonSerialize(using = CustomDateTimeSerializer.class)
	public Date getExpectArriveTime() {
		return expectArriveTime;
	}

	public void setExpectArriveTime(Date expectArriveTime) {
		this.expectArriveTime = expectArriveTime;
	}

	public Integer getUrgency() {
		return urgency;
	}

	public void setUrgency(Integer urgency) {
		this.urgency = urgency;
	}

	public WorkOrderStatus getStatus() {
		return status;
	}

	public void setStatus(WorkOrderStatus status) {
		this.status = status;
	}

	public float getChargeMoney() {
		return chargeMoney;
	}

	public void setChargeMoney(float chargeMoney) {
		this.chargeMoney = chargeMoney;
	}

	public Long getFieldWorkerId() {
		return fieldWorkerId;
	}

	public void setFieldWorkerId(Long fieldWorkerId) {
		this.fieldWorkerId = fieldWorkerId;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	@JsonIgnore
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }

    public String getHandleResult() {
        return handleResult;
    }

    public void setHandleResult(String handleResult) {
        this.handleResult = handleResult;
    }
    


	public Integer getReturnProductId() {
		return returnProductId;
	}

	public void setReturnProductId(Integer returnProductId) {
		this.returnProductId = returnProductId;
	}
	
	

	public Integer getUpdateId() {
		return updateId;
	}

	public void setUpdateId(Integer updateId) {
		this.updateId = updateId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((workOrderNo == null) ? 0 : workOrderNo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WorkOrder other = (WorkOrder) obj;
		if (workOrderNo == null) {
			if (other.workOrderNo != null)
				return false;
		} else if (!workOrderNo.equals(other.workOrderNo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "WorkOrder [id=" + id + ", workOrderNo=" + workOrderNo
				+ ", orderToken=" + orderToken + ", workOrderType="
				+ workOrderType + ", visitType=" + visitType + ", serviceDate="
				+ serviceDate + ", productId=" + productId + ", areaCode="
				+ areaCode + ", chargeType=" + chargeType + ", chargeMoney="
				+ chargeMoney + ", description=" + description
				+ ", customerId=" + customerId + ", customerName="
				+ customerName + ", linkPerson=" + linkPerson
				+ ", customerTel=" + customerTel + ", customerMobile="
				+ customerMobile + ", customerAddress=" + customerAddress
				+ ", expectArriveTime=" + expectArriveTime + ", urgency="
				+ urgency + ", fieldWorkerId=" + fieldWorkerId
				+ ", receiveTime=" + receiveTime + ", dispatchTime="
				+ dispatchTime + ", confirmTime=" + confirmTime + ", viewTime="
				+ viewTime + ", acceptTime=" + acceptTime + ", reassignTime="
				+ reassignTime + ", callTime=" + callTime + ", arrivalTime="
				+ arrivalTime + ", finishTime=" + finishTime + ", status="
				+ status + ", fieldworker=" + fieldworker + ", customer="
				+ customer + ", reason=" + reason + ", productStatus="
				+ productStatus + ", handleResult=" + handleResult
				+ ", returnProductId=" + returnProductId + "]";
	}
	
	

}
