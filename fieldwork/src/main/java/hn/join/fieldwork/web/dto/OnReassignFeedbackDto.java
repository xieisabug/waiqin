package hn.join.fieldwork.web.dto;

import java.util.Date;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "Srv_WorkCard_Order")
public class OnReassignFeedbackDto {

	@Element(name = "workCard_id")
	private String workCardId;

	@Element(name = "reason")
	private String reason;
	
	@Element(name = "change_time")
	private Date reassignTime;

	public OnReassignFeedbackDto() {
		super();
	}

	public OnReassignFeedbackDto(String workCardId, String reason,Date reassignTime) {
		super();
		this.workCardId = workCardId;
		this.reason = reason;
		this.reassignTime = reassignTime;
	}

	public String getWorkCardId() {
		return workCardId;
	}

	public void setWorkCardId(String workCardId) {
		this.workCardId = workCardId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Date getReassignTime() {
		return reassignTime;
	}

	public void setReassignTime(Date reassignTime) {
		this.reassignTime = reassignTime;
	}
	
	

}
