package hn.join.fieldwork.web.command;

public class PersonSettingCommand {
	
	private String userId;
	
	private String fullname;
	
	private String telNo;
	
	private String mobileNo;
	
	private String email;
	
//	private String password;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getTelNo() {
		return telNo;
	}

	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

//	public String getPassword() {
//		return password;
//	}
//
//	public void setPassword(String password) {
//		this.password = password;
//	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PersonSettingCommand [userId=").append(userId)
				.append(", fullname=").append(fullname).append(", telNo=")
				.append(telNo).append(", mobileNo=").append(mobileNo)
				.append(", email=").append(email).append("]");
		return builder.toString();
	}
	
	

}
