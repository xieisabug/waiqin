package hn.join.fieldwork.web.dto;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "Srv_Service_Order")
public class ReceiptFeedbackDto {

	public final static ReceiptSpendingInfo DEFAULT_SPENDING_ITEM = new ReceiptSpendingInfo(
			3, "基本补助", "6.0");

	@Element(name = "service_head")
	private ReceiptHead serviceHead;

	@ElementList(name = "question_body", required = false)
	private LinkedList<ReceiptQuestion> questionBody = new LinkedList<ReceiptQuestion>();

	@ElementList(name = "spending_info_body")
	private LinkedList<ReceiptSpendingInfo> spendingInfoBody = new LinkedList<ReceiptSpendingInfo>();

	public ReceiptFeedbackDto() {
		super();
	}

	public ReceiptFeedbackDto(ReceiptHead serviceHead,
			LinkedList<ReceiptQuestion> questionBody,
			LinkedList<ReceiptSpendingInfo> spendingInfoBody) {
		super();
		this.serviceHead = serviceHead;
		this.questionBody = questionBody;
		this.spendingInfoBody = spendingInfoBody;
	}

	public ReceiptHead getServiceHead() {
		return serviceHead;
	}

	public void setServiceHead(ReceiptHead serviceHead) {
		this.serviceHead = serviceHead;
	}

	public List<ReceiptQuestion> getQuestionBody() {
		return questionBody;
	}

	public void setQuestionBody(LinkedList<ReceiptQuestion> questionBody) {
		this.questionBody = questionBody;
	}

	public List<ReceiptSpendingInfo> getSpendingInfoBody() {
		return spendingInfoBody;
	}

	public void setSpendingInfoBody(
			LinkedList<ReceiptSpendingInfo> spendingInfoBody) {
		this.spendingInfoBody = spendingInfoBody;
	}

	public void addQuestion(ReceiptQuestion question) {
		this.questionBody.add(question);
	}

	public void addSpendingInfo(ReceiptSpendingInfo spendingInfo) {
		this.spendingInfoBody.add(spendingInfo);
	}

	/**
	 * 服务回单主表信息
	 * 
	 * @author Administrator
	 * 
	 */
	public static class ReceiptHead {
		/**
		 * 客户信息修改标志为customerUpdateFlag '0'为不修改'1'为修改
		 */
		@Attribute(name = "customerUpdatFlag")
		private String customerUpdatFlag;

		@Attribute(name = "orderTyp")
		private String orderTyp;

		@Element(name = "workCard_id")
		private String workCardId;

		/**
		 * 服务日期 date型 不能为空 精确到时分秒
		 */
		@Element(name = "service_date")
		private Date serviceDate;

		/**
		 * 纸制单据号 varchar(32) 不能为空
		 */
		@Element(name = "order_code")
		private String orderCode;

		/**
		 * 客户上门地址 varchar(256) 不能为空
		 */
		@Element(name = "customer_addr")
		private String customerAddr;

		/**
		 * 客户地址经度
		 */
		@Element(name = "longitude")
		private String longitude;

		/**
		 * 客户地址纬度
		 */
		@Element(name = "latitude")
		private String latitude;

		/**
		 * 客户联系人 varchar(64) 不能为空
		 */
		@Element(name = "link_person")
		private String linkPerson;

		/**
		 * 客户联系电话 varchar(64) 不能为空
		 */
		@Element(name = "customer_telphone")
		private String customerTelphone;

		/**
		 * 客户手机 varchar(64) 不能为空
		 */
		@Element(name = "customer_mobile")
		private String customerMobile;

		/**
		 * 到达现场时间 不能为空
		 */
		@Element(name = "arrive_date")
		private Date arriveDate;

		/**
		 * 服务完成时间 不能为空
		 */
		@Element(name = "service_end_date")
		private Date serviceEndDate;

		/**
		 * 客户签名 varchar(32) 不能为空
		 */
		@Element(name = "customer_signature")
		private String customerSignature;

		/**
		 * 客户是否满意 产品 char(1)
		 */
		@Element(name = "customer_if_satisfied_product", required = false)
		private String customerIfSatisfied_product;

		/**
		 * 客户是否满意 服务 char(1)
		 */
		@Element(name = "customer_if_satisfied_service", required = false)
		private String customerIfSatisfied_service;

		/**
		 * 费用过程 varchar(500)
		 */
		@Element(name = "spending_process")
		private String spendingProcess;

		/**
		 * 解决方式 char(1)
		 */
		@Element(name = "flag")
		private String flag;

		/**
		 * 备注 varchar(256)
		 */
		@Element(name = "notes")
		private String notes;

		@Element(name = "productStatus", required = false)
		private String productStatus;

		@Element(name = "handleResult", required = false)
		private String handleResult;

		// <hardwareCode>硬件型号 varchar(128)</hardwareCode>
		@Element(name = "hardwareCode", required = false)
		private String hardwareCode;

		// <softwareVersion>软件版本 varchar(128)</softwareVersion>
		@Element(name = "softwareVersion", required = false)
		private String softwareVersion;

		// <environment>使用环境 varchar(256)</environment>
		@Element(name = "environment", required = false)
		private String environment;
		
		// <isCharge>是否收费 varchar(1)</isCharge>
		@Element(name = "isCharge", required = false)
		private String isCharge;
		
		// <isChargeValue>收费金额 varchar(20)</isChargeValue>
		@Element(name = "isChargeValue", required = false)
		private String isChargeValue;

        //回访类型
        @Element(name = "visitType", required = false)
        private String visitType;


		public String getCustomerUpdatFlag() {
			return customerUpdatFlag;
		}

		public void setCustomerUpdatFlag(String customerUpdatFlag) {
			this.customerUpdatFlag = customerUpdatFlag;
		}

		public String getWorkCardId() {
			return workCardId;
		}

		public void setWorkCardId(String workCardId) {
			this.workCardId = workCardId;
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

		public String getLongitude() {
			return longitude;
		}

		public void setLongitude(String longitude) {
			this.longitude = longitude;
		}

		public String getLatitude() {
			return latitude;
		}

		public void setLatitude(String latitude) {
			this.latitude = latitude;
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

		public void setCustomerIfSatisfied_product(
				String customerIfSatisfied_product) {
			this.customerIfSatisfied_product = customerIfSatisfied_product;
		}

		public String getCustomerIfSatisfied_service() {
			return customerIfSatisfied_service;
		}

		public void setCustomerIfSatisfied_service(
				String customerIfSatisfied_service) {
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

		public String getOrderTyp() {
			return orderTyp;
		}

		public void setOrderTyp(String orderTyp) {
			this.orderTyp = orderTyp;
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

        public String getVisitType() {
            return visitType;
        }

        public void setVisitType(String visitType) {
            this.visitType = visitType;
        }
    }

	@Root(name = "problem")
	public static class ReceiptQuestion {

		/**
		 * 问题ID int 不能为空-
		 */
		@Element(name = "question_id")
		private Long questionId;

		@Element(name = "productId", required = false)
		private String productId;

		/**
		 * 问题类型ID int 不能为空
		 */
		@Element(name = "question_typ_id")
		private Integer questionTypId;

		/**
		 * 问题类别ID int 不能为空
		 */
		@Element(name = "question_typ_p_id")
		private Integer questionTypPId;

		/**
		 * 问题描述 varchar(1000) 不能为空
		 */
		@Element(name = "question_desc")
		private String questionDesc;

		/**
		 * 解决方法 varchar(1000) 不能为空
		 */
		@Element(name = "solution_method")
		private String solutionMethod;

		public ReceiptQuestion() {
			super();
		}

		public String getProductId() {
			return productId;
		}

		public void setProductId(String productId) {
			this.productId = productId;
		}

		public Long getQuestionId() {
			return questionId;
		}

		public void setQuestionId(Long questionId) {
			this.questionId = questionId;
		}

		public Integer getQuestionTypId() {
			return questionTypId;
		}

		public void setQuestionTypId(Integer questionTypId) {
			this.questionTypId = questionTypId;
		}

		public Integer getQuestionTypPId() {
			return questionTypPId;
		}

		public void setQuestionTypPId(Integer questionTypPId) {
			this.questionTypPId = questionTypPId;
		}

		public String getQuestionDesc() {
			return questionDesc;
		}

		public void setQuestionDesc(String questionDesc) {
			this.questionDesc = questionDesc;
		}

		public String getSolutionMethod() {
			return solutionMethod;
		}

		public void setSolutionMethod(String solutionMethod) {
			this.solutionMethod = solutionMethod;
		}

	}

	@Root(name = "spending_info")
	public static class ReceiptSpendingInfo {

		/**
		 * 费用项目ID int 不能为空
		 */
		@Element(name = "spending_item_id")
		private Integer spendingItemId;

		/**
		 * 费用项目名称 varchar(64) 不能为空
		 */
		@Element(name = "spending_name")
		private String spendingName;

		/**
		 * 产生费用 money 不能为空
		 */
		@Element(name = "spending")
		private String spending;

		public ReceiptSpendingInfo() {
			super();
		}

		public ReceiptSpendingInfo(Integer spendingItemId, String spendingName,
				String spending) {
			super();
			this.spendingItemId = spendingItemId;
			this.spendingName = spendingName;
			this.spending = spending;
		}

		public Integer getSpendingItemId() {
			return spendingItemId;
		}

		public void setSpendingItemId(Integer spendingItemId) {
			this.spendingItemId = spendingItemId;
		}

		public String getSpendingName() {
			return spendingName;
		}

		public void setSpendingName(String spendingName) {
			this.spendingName = spendingName;
		}

		public String getSpending() {
			return spending;
		}

		public void setSpending(String spending) {
			this.spending = spending;
		}

	}

}
