package hn.join.fieldwork.web.command;

import hn.join.fieldwork.utils.CustomDateTimeSerializer;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

public class CheckinCommand {

	private Long checkinId;

	private Long fieldWorkerId;

	private String fieldWorkerName;

	// private Integer departmentId;

	// private String departmentCode;

	private String departmentName;

	@JsonSerialize(using = CustomDateTimeSerializer.class)
	private Date userCheckinDateTime;

	@JsonSerialize(using = CustomDateTimeSerializer.class)
	private Date systemCheckinDateTime;

	public Long getCheckinId() {
		return checkinId;
	}

	public void setCheckinId(Long checkinId) {
		this.checkinId = checkinId;
	}

	public Long getFieldWorkerId() {
		return fieldWorkerId;
	}

	public void setFieldWorkerId(Long fieldWorkerId) {
		this.fieldWorkerId = fieldWorkerId;
	}

	public String getFieldWorkerName() {
		return fieldWorkerName;
	}

	public void setFieldWorkerName(String fieldWorkerName) {
		this.fieldWorkerName = fieldWorkerName;
	}

	// public Integer getDepartmentId() {
	// return departmentId;
	// }
	//
	// public void setDepartmentId(Integer departmentId) {
	// this.departmentId = departmentId;
	// }

	// public String getDepartmentCode() {
	// return departmentCode;
	// }
	//
	// public void setDepartmentCode(String departmentCode) {
	// this.departmentCode = departmentCode;
	// }

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public Date getUserCheckinDateTime() {
		return userCheckinDateTime;
	}

	public void setUserCheckinDateTime(Date userCheckinDateTime) {
		this.userCheckinDateTime = userCheckinDateTime;
	}

	public Date getSystemCheckinDateTime() {
		return systemCheckinDateTime;
	}

	public void setSystemCheckinDateTime(Date systemCheckinDateTime) {
		this.systemCheckinDateTime = systemCheckinDateTime;
	}

}
