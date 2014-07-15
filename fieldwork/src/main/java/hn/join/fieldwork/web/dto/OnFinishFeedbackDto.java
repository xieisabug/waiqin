package hn.join.fieldwork.web.dto;

import java.util.Date;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "Srv_WorkCard_Order")
public class OnFinishFeedbackDto {
	
	@Element(name = "workCard_id")
	private String workCardId;

	@Element(name = "finish_time")
	private Date finishTime;
	
	@Element(name = "problem_solved", required = false)
	private String problemSolved="1";
	

	public OnFinishFeedbackDto() {
		super();
	}

	public OnFinishFeedbackDto(String workCardId, Date finishTime,String problemSolved) {
		super();
		this.workCardId = workCardId;
		this.finishTime = finishTime;
		this.problemSolved=problemSolved;
	}

	public String getWorkCardId() {
		return workCardId;
	}

	public void setWorkCardId(String workCardId) {
		this.workCardId = workCardId;
	}

	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	public String getProblemSolved() {
		return problemSolved;
	}

	public void setProblemSolved(String problemSolved) {
		this.problemSolved = problemSolved;
	}

	
	
	
}
