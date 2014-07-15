package hn.join.fieldwork.web.dto;

import java.util.Date;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "Srv_WorkCard_Order")
public class OnTerminalReceiveFeedbackDto {

	/**
	 * 派工单ID信息 int型 不能为空
	 */
	@Element(name = "workCard_id")
	private String workCardId;

	/**
	 * 手机接收派工单时间 date型（精确到时分秒）不能为空
	 */
	@Element(name = "receive_time")
	private Date receiveTime;
	
	
	

	public OnTerminalReceiveFeedbackDto(String workCardId, Date receiveTime) {
		super();
		this.workCardId = workCardId;
		this.receiveTime = receiveTime;
	}
	
	

	public OnTerminalReceiveFeedbackDto() {
		super();
	}



	public String getWorkCardId() {
		return workCardId;
	}

	public void setWorkCardId(String workCardId) {
		this.workCardId = workCardId;
	}

	public Date getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}

}
