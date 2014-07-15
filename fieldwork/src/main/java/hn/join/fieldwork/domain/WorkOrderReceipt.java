package hn.join.fieldwork.domain;

import java.util.Date;
import java.util.List;

import org.simpleframework.xml.Element;

public class WorkOrderReceipt extends BaseDomain {

	private static final long serialVersionUID = 6842443695011491673L;
	/**解决方案*/
	private Integer id;

	/**客户信息修改标志 Flag '0'为不修改'1'为修改*/
	private String customerUpdatFlag;

	/**原派工单ID*/
	private String workOrderNo;

	/**服务日期*/
	private Date serviceDate;

	/**纸制单据号*/
	private String orderCode;

	/**客户上门地址*/
	private String customerAddr;

	/**客户地址维度*/
	private String latitude;

	/**客户地址经度*/
	private String longitude;

	/**客户联系人*/
	private String linkPerson;

	/**客户联系电话*/
	private String customerTelphone;

	/**客户手机*/
	private String customerMobile;

	/**到达现场时间*/
	private Date arriveDate;

	/**服务完成时间*/
	private Date serviceEndDate;

	/**客户签名*/
	private String customerSignature;

	/**客户是否满意(产品)*/
	private String customerIfSatisfied_product;
	
	/**客户是否满意(服务)*/
	private String customerIfSatisfied_service;

	/**费用过程*/
	private String spendingProcess;

	/**解决方式*/
	private String flag;

	/**备注*/
	private String notes;

	private List<WorkOrderProblem> problems;

	private List<SpendingInfo> spendings;
	
	/**回访类型*/
	private String visitType;
	/**产品状态*/
	private String productStatus;
	/**处理结果*/
	private String handleResult;
	/**硬件编码*/
	private String hardwareCode;
	/**软件版本*/
	private String softwareVersion;
	/**环境*/
	private String environment;
	
	private String isCharge; //是否收费 0:不收 1：收
	
	private String isChargeValue; //收费金额
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCustomerUpdatFlag() {
		return customerUpdatFlag;
	}

	public void setCustomerUpdatFlag(String customerUpdatFlag) {
		this.customerUpdatFlag = customerUpdatFlag;
	}

	public String getWorkOrderNo() {
		return workOrderNo;
	}

	public void setWorkOrderNo(String workOrderNo) {
		this.workOrderNo = workOrderNo;
	}

	public Date getServiceDate() {
		return serviceDate;
	}

	public void setServiceDate(Date serviceDate) {
		this.serviceDate = serviceDate;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getCustomerAddr() {
		return customerAddr;
	}

	public void setCustomerAddr(String customerAddr) {
		this.customerAddr = customerAddr;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLinkPerson() {
		return linkPerson;
	}

	public void setLinkPerson(String linkPerson) {
		this.linkPerson = linkPerson;
	}

	public String getCustomerTelphone() {
		return customerTelphone;
	}

	public void setCustomerTelphone(String customerTelphone) {
		this.customerTelphone = customerTelphone;
	}

	public String getCustomerMobile() {
		return customerMobile;
	}

	public void setCustomerMobile(String customerMobile) {
		this.customerMobile = customerMobile;
	}

	public Date getArriveDate() {
		return arriveDate;
	}

	public void setArriveDate(Date arriveDate) {
		this.arriveDate = arriveDate;
	}

	public Date getServiceEndDate() {
		return serviceEndDate;
	}

	public void setServiceEndDate(Date serviceEndDate) {
		this.serviceEndDate = serviceEndDate;
	}

	public String getCustomerSignature() {
		return customerSignature;
	}

	public void setCustomerSignature(String customerSignature) {
		this.customerSignature = customerSignature;
	}



	public String getCustomerIfSatisfied_product() {
		return customerIfSatisfied_product;
	}

	public void setCustomerIfSatisfied_product(String customerIfSatisfied_product) {
		this.customerIfSatisfied_product = customerIfSatisfied_product;
	}

	public String getCustomerIfSatisfied_service() {
		return customerIfSatisfied_service;
	}

	public void setCustomerIfSatisfied_service(String customerIfSatisfied_service) {
		this.customerIfSatisfied_service = customerIfSatisfied_service;
	}

	public String getSpendingProcess() {
		return spendingProcess;
	}

	public void setSpendingProcess(String spendingProcess) {
		this.spendingProcess = spendingProcess;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public List<WorkOrderProblem> getProblems() {
		return problems;
	}

	public void setProblems(List<WorkOrderProblem> problems) {
		this.problems = problems;
	}

	public List<SpendingInfo> getSpendings() {
		return spendings;
	}

	public void setSpendings(List<SpendingInfo> spendings) {
		this.spendings = spendings;
	}
	
	

	public String getVisitType() {
		return visitType;
	}

	public void setVisitType(String visitType) {
		this.visitType = visitType;
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
	
	public String getHardwareCode() {
		return hardwareCode;
	}

	public void setHardwareCode(String hardwareCode) {
		this.hardwareCode = hardwareCode;
	}

	public String getSoftwareVersion() {
		return softwareVersion;
	}

	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}
	

	public String getIsCharge() {
		return isCharge;
	}

	public void setIsCharge(String isCharge) {
		this.isCharge = isCharge;
	}

	public String getIsChargeValue() {
		return isChargeValue;
	}

	public void setIsChargeValue(String isChargeValue) {
		this.isChargeValue = isChargeValue;
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
		WorkOrderReceipt other = (WorkOrderReceipt) obj;
		if (workOrderNo == null) {
			if (other.workOrderNo != null)
				return false;
		} else if (!workOrderNo.equals(other.workOrderNo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "WorkOrderReceipt [id=" + id + ", customerUpdatFlag="
				+ customerUpdatFlag + ", workOrderNo=" + workOrderNo
				+ ", serviceDate=" + serviceDate + ", orderCode=" + orderCode
				+ ", customerAddr=" + customerAddr + ", latitude=" + latitude
				+ ", longitude=" + longitude + ", linkPerson=" + linkPerson
				+ ", customerTelphone=" + customerTelphone
				+ ", customerMobile=" + customerMobile + ", arriveDate="
				+ arriveDate + ", serviceEndDate=" + serviceEndDate
				+ ", customerSignature=" + customerSignature
				+ ", customerIfSatisfied_product="
				+ customerIfSatisfied_product
				+ ", customerIfSatisfied_service="
				+ customerIfSatisfied_service + ", spendingProcess="
				+ spendingProcess + ", flag=" + flag + ", notes=" + notes
				+ ", problems=" + problems + ", spendings=" + spendings
				+ ", visitType=" + visitType + ", productStatus="
				+ productStatus + ", handleResult=" + handleResult
				+ ", hardwareCode=" + hardwareCode + ", softwareVersion="
				+ softwareVersion + ", environment=" + environment
				+ ", isCharge=" + isCharge + ", isChargeValue=" + isChargeValue
				+ "]";
	}


	
	

}
