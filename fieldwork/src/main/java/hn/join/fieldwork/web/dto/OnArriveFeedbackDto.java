package hn.join.fieldwork.web.dto;

import java.util.Date;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "Srv_WorkCard_Order")
public class OnArriveFeedbackDto {

	@Element(name = "workCard_id")
	private String workCardId;

	@Element(name = "arrived_time")
	private Date arrivedTime;

	public OnArriveFeedbackDto() {
		super();
	}

	public OnArriveFeedbackDto(String workCardId, Date arrivedTime) {
		super();
		this.workCardId = workCardId;
		this.arrivedTime = arrivedTime;
	}

	public String getWorkCardId() {
		return workCardId;
	}

	public void setWorkCardId(String workCardId) {
		this.workCardId = workCardId;
	}

	public Date getArrivedTime() {
		return arrivedTime;
	}

	public void setArrivedTime(Date arrivedTime) {
		this.arrivedTime = arrivedTime;
	}

}
