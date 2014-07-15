package hn.join.fieldwork.web.command;

public class CreateUserCommand {

	private String username;

	private String password;

	private String email;

	private String fullname;

	private String mobileNo;

	private String telNo;

	private String workerNo;

	private Integer roleId;

	private String areaCode;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getTelNo() {
		return telNo;
	}

	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}

	public String getWorkerNo() {
		return workerNo;
	}

	public void setWorkerNo(String workerNo) {
		this.workerNo = workerNo;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CreateUserCommand [username=").append(username)
				.append(", password=").append(password).append(", email=")
				.append(email).append(", fullname=").append(fullname)
				.append(", mobileNo=").append(mobileNo).append(", telNo=")
				.append(telNo).append(", workerNo=").append(workerNo)
				.append(", roleId=").append(roleId).append(", areaCode=")
				.append(areaCode).append("]");
		return builder.toString();
	}

	// public Long getParentId() {
	// return parentId;
	// }
	//
	// public void setParentId(Long parentId) {
	// this.parentId = parentId;
	// }
	//

}
