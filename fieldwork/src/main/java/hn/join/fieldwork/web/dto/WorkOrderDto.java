package hn.join.fieldwork.web.dto;

import hn.join.fieldwork.utils.CustomDateTimeSerializer;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "workOrder")
public class WorkOrderDto {

	@Override
	public String toString() {
		return "WorkOrderDto [workCardId=" + workCardId + ", serviceDate="
				+ serviceDate + ", workOrderType=" + workOrderType
				+ ", visitType=" + visitType + ", customerDto=" + customerDto
				+ ", fieldWorkerId=" + fieldWorkerId + ", fieldWorkerName="
				+ fieldWorkerName + ", workOrderDescription="
				+ workOrderDescription + ", expectArriveTime="
				+ expectArriveTime + ", problemList=" + problemList
				+ ", chargeType=" + chargeType + ", chargeMoney=" + chargeMoney
				+ ", urgency=" + urgency + ", city=" + city + ", orderToken="
				+ orderToken + ", returnProductId=" + returnProductId
				+ ", returnProductName=" + returnProductName
				+ ", productStatus=" + productStatus + ", handleResult="
				+ handleResult + ", route=" + route + "]";
	}



	/**
	 * 派工单ID int型(必填)
	 */
	@Element(name = "workCardId")
	private Long workCardId;

	/**
	 * 服务日期(格式:yyyy-MM-ss) (必填)
	 */
	@Element(name = "serviceDate")
	private Date serviceDate;

	/**
	 * 派工类别,int型 0：直接派工；2：回访派工(必填)
	 */
	@Element(name = "workOrderType")
	private Integer workOrderType;
	
	/**
	 * 回访类型,varchar(64)(可填)
	 */
	@Element(name = "visitType",required=false)
	private String visitType;

	/**
	 * 客户
	 */
	@Element(name = "customer")
	private CustomerDto customerDto;

	/**
	 * 服务人员ID,int型(必填)
	 */
	@Element(name = "fieldWorkerId")
	private Integer fieldWorkerId;

	/**
	 * 服务人员姓名,varchar(64)(必填)
	 */
	@Element(name = "fieldWorkerName")
	private String fieldWorkerName;

	/**
	 * 派工说明,varchar(1000)(可填)
	 */
	@Element(name = "workOrderDescription", required = false)
	private String workOrderDescription;

	/**
	 * 服务人员预计到达时间(格式:yyyy-MM-dd)(可填)
	 */
	@Element(name = "expectArriveTime", required = false)
	private Date expectArriveTime;


	/**
	 * 问题列表
	 */
	@ElementList(name = "problemList",required=false)
	private LinkedList<ProblemDto> problemList;

	/**
	 * 是否收费(1:是,2:否) int(必填)
	 */
	@Element(name = "chargeType", required = false)
	private Integer chargeType;

	/**
	 * 收费金额,varchar(10)(可填)
	 */
	@Element(name = "chargeMoney", required = false)
	private Float chargeMoney;

	/**
	 * 工单的紧急程度(1:一般,2:紧急,3:非常紧急),int(必填)
	 */
	@Element(name = "urgency", required = false)
	private Integer urgency;

	/**
	 * 客户所在城市(城市名称，比如长沙市，湘潭市，湘西自治州) varchar(32)(必填)
	 */
	@Element(name = "city")
	private String city;
	
	@Element(name="orderToken",required=false)
	private String orderToken;

	@Element(name = "returnProductId",required = false)
	private String returnProductId;
	
	private String returnProductName;

    @Element(name = "productStatus",required = false)
    private String productStatus;

    @Element(name = "handleResult",required = false)
    private String handleResult;
    
    @Element(name = "route",required = false)
    private String route;
    
    @Element(name = "updateId", required = false)
    private Integer updateId;

	public Long getWorkCardId() {
		return workCardId;
	}

	public void setWorkCardId(Long workCardId) {
		this.workCardId = workCardId;
	}

	@JsonSerialize(using = CustomDateTimeSerializer.class)
	public Date getServiceDate() {
		return serviceDate;
	}

	public void setServiceDate(Date serviceDate) {
		this.serviceDate = serviceDate;
	}

	public Integer getWorkOrderType() {
		return workOrderType;
	}

	public void setWorkOrderType(Integer workOrderType) {
		this.workOrderType = workOrderType;
	}
	
	

	public String getVisitType() {
		return visitType;
	}

	public void setVisitType(String visitType) {
		this.visitType = visitType;
	}

	public CustomerDto getCustomerDto() {
		return customerDto;
	}

	public void setCustomerDto(CustomerDto customerDto) {
		this.customerDto = customerDto;
	}

	public Integer getFieldWorkerId() {
		return fieldWorkerId;
	}

	public void setFieldWorkerId(Integer fieldWorkerId) {
		this.fieldWorkerId = fieldWorkerId;
	}

	public String getFieldWorkerName() {
		return fieldWorkerName;
	}

	public void setFieldWorkerName(String fieldWorkerName) {
		this.fieldWorkerName = fieldWorkerName;
	}

	public String getWorkOrderDescription() {
		return workOrderDescription;
	}

	public void setWorkOrderDescription(String workOrderDescription) {
		this.workOrderDescription = workOrderDescription;
	}

	@JsonSerialize(using = CustomDateTimeSerializer.class)
	public Date getExpectArriveTime() {
		return expectArriveTime;
	}

	public void setExpectArriveTime(Date expectArriveTime) {
		this.expectArriveTime = expectArriveTime;
	}

	public String getReturnProductId() {
		return returnProductId;
	}

	public void setReturnProductId(String returnProductId) {
		this.returnProductId = returnProductId;
	}
	
	

	public String getReturnProductName() {
		return returnProductName;
	}

	public void setReturnProductName(String returnProductName) {
		this.returnProductName = returnProductName;
	}

	public List<ProblemDto> getProblemList() {
		return problemList;
	}

	public void setProblemList(LinkedList<ProblemDto> problemList) {
		this.problemList = problemList;
	}

	public Integer getChargeType() {
		return chargeType;
	}

	public void setChargeType(Integer chargeType) {
		this.chargeType = chargeType;
	}

	public Float getChargeMoney() {
		return chargeMoney;
	}

	public void setChargeMoney(Float chargeMoney) {
		this.chargeMoney = chargeMoney;
	}

	public Integer getUrgency() {
		return urgency;
	}

	public void setUrgency(Integer urgency) {
		this.urgency = urgency;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	

	
	public String getOrderToken() {
		return orderToken;
	}

	public void setOrderToken(String orderToken) {
		this.orderToken = orderToken;
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
    
    

    public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}



	public Integer getUpdateId() {
		return updateId;
	}

	public void setUpdateId(Integer updateId) {
		this.updateId = updateId;
	}



	@Root(name = "problem")
	public static class ProblemDto {
    	
    
    	

		/**
		 * 问题ID int (必填)
		 */
		@Element(name = "questionId")
		private Long problemId;
		
		/**
		 * 问题类型ID int(必填)
		 */
		@Element(name = "questionTypId")
		private Integer questionTypId;

		/**
		 * 问题类别ID int (必填)
		 */
		@Element(name = "questionTypPid")
		private Integer questionTypPid;
		
		/**
		 * 产品ID
		 */
		@Element(name = "product_id",required=false)
		private Integer productId=729;
		
		private String productName;

		/**
		 * 问题描述 varchar(1000) (必填)
		 */
		@Element(name = "questionDesc")
		private String questionDesc;
		
		/**
		 * 问题解答 varchar(1000) (可选)
		 */
		@Element(name="solution",required=false)
		private String solution;
		

	

		public Long getProblemId() {
			return problemId;
		}

		public void setProblemId(Long problemId) {
			this.problemId = problemId;
		}

		public Integer getQuestionTypId() {
			return questionTypId;
		}

		public void setQuestionTypId(Integer questionTypId) {
			this.questionTypId = questionTypId;
		}

		public Integer getQuestionTypPid() {
			return questionTypPid;
		}

		public void setQuestionTypPid(Integer questionTypPid) {
			this.questionTypPid = questionTypPid;
		}

		public String getQuestionDesc() {
			return questionDesc;
		}

		public void setQuestionDesc(String questionDesc) {
			this.questionDesc = questionDesc;
		}

		public String getSolution() {
			return solution;
		}

		public void setSolution(String solution) {
			this.solution = solution;
		}

		public Integer getProductId() {
			return productId;
		}

		public void setProductId(Integer productId) {
			this.productId = productId;
		}

		public String getProductName() {
			return productName;
		}

		public void setProductName(String productName) {
			this.productName = productName;
		}
		
		
		
		
	}

}
