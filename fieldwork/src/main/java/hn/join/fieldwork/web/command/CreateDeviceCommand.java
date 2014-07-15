package hn.join.fieldwork.web.command;

import hn.join.fieldwork.domain.Device.DeviceStatus;

public class CreateDeviceCommand {

	private String phoneNo;

	private String meid;

	private String model;

	private String currentAmount;

	private String consumeCategory;
	
	private String areaCode;

	private String status = DeviceStatus.ACTIVE.name();


	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getMeid() {
		return meid;
	}

	public void setMeid(String meid) {
		this.meid = meid;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getCurrentAmount() {
		return currentAmount;
	}

	public void setCurrentAmount(String currentAmount) {
		this.currentAmount = currentAmount;
	}

	public String getConsumeCategory() {
		return consumeCategory;
	}

	public void setConsumeCategory(String consumeCategory) {
		this.consumeCategory = consumeCategory;
	}
	
	

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
	

}
