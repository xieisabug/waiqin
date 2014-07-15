package hn.join.fieldwork.web.dto;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "fieldWorker")
public class FieldWorkerDto {

	/**
	 * 服务人员ID int(必填)
	 */
	@Element(name = "fieldWorkerId")
	private Integer fieldWorkerId;

	/**
	 * 服务人员工号 varchar(64)(必填)
	 */
	@Element(name = "fieldWorkerNo")
	private String fieldWorkerNo;

	/**
	 * 姓名 varchar(32)(必填)
	 */
	@Element(name = "fullname")
	private String fullname;

	/**
	 * 所属部门ID int(必填)
	 */
	@Element(name = "departmentId")
	private Integer departmentId;

	/**
	 * 所属部门编号 varchar(32)(必填)
	 */
	@Element(name = "departmentNo")
	private String departmentNo;

	/**
	 * 所属部门名称 varchar(64)(必填)
	 */
	@Element(name = "departmentName")
	private String departmentName;

	/**
	 * 操作代码(I:新增,U:更新,D:删除) varchar(1)(必填)
	 */
	@Element(name = "op")
	private String op;

	public Integer getFieldWorkerId() {
		return fieldWorkerId;
	}

	public void setFieldWorkerId(Integer fieldWorkerId) {
		this.fieldWorkerId = fieldWorkerId;
	}

	public String getFieldWorkerNo() {
		return fieldWorkerNo;
	}

	public void setFieldWorkerNo(String fieldWorkerNo) {
		this.fieldWorkerNo = fieldWorkerNo;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentNo() {
		return departmentNo;
	}

	public void setDepartmentNo(String departmentNo) {
		this.departmentNo = departmentNo;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

}
