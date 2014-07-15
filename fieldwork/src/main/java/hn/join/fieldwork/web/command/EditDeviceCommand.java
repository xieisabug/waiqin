package hn.join.fieldwork.web.command;

public class EditDeviceCommand extends CreateDeviceCommand {
	
	private String deviceId;

	private String memo;
	
	
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	

}
