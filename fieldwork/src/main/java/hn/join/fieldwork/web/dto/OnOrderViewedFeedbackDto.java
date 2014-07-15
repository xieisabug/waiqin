package hn.join.fieldwork.web.dto;

import java.util.Date;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "Srv_WorkCard_Order")
public class OnOrderViewedFeedbackDto {

	/**
	 * 派工单ID信息 int型 不能为空
	 */
	@Element(name = "workCard_id")
	private String workCard_id;

	/**
	 * 手机阅读派工单时间 date型（精确到时分秒）不能为空
	 */
	@Element(name = "read_time")
	private Date readTime;

	public OnOrderViewedFeedbackDto(String workCard_id, Date readTime) {
		super();
		this.workCard_id = workCard_id;
		this.readTime = readTime;
	}

	public OnOrderViewedFeedbackDto() {
		super();
	}

	public String getWorkCard_id() {
		return workCard_id;
	}

	public void setWorkCard_id(String workCard_id) {
		this.workCard_id = workCard_id;
	}

	public Date getReadTime() {
		return readTime;
	}

	public void setReadTime(Date readTime) {
		this.readTime = readTime;
	}

}
