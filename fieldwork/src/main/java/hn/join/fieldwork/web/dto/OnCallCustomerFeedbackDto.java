package hn.join.fieldwork.web.dto;

import java.util.Date;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "Srv_WorkCard_Order")
public class OnCallCustomerFeedbackDto {

	/**
	 * 派工单ID信息 int型 不能为空
	 */
	@Element(name = "workCard_id")
	private String workCardId;

	/**
	 * 手机拨打客户联系电话时间 date型（精确到时分秒）不能为空
	 */
	@Element(name = "call_customer_time")
	private Date callCustomerTime;
	
	

	public OnCallCustomerFeedbackDto(String workCardId, Date callCustomerTime) {
		super();
		this.workCardId = workCardId;
		this.callCustomerTime = callCustomerTime;
	}
	
	

	public OnCallCustomerFeedbackDto() {
		super();
	}



	public String getWorkCardId() {
		return workCardId;
	}

	public void setWorkCardId(String workCardId) {
		this.workCardId = workCardId;
	}

	public Date getCallCustomerTime() {
		return callCustomerTime;
	}

	public void setCallCustomerTime(Date callCustomerTime) {
		this.callCustomerTime = callCustomerTime;
	}

}
