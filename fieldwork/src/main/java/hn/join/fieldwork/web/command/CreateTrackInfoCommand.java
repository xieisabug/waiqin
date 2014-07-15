package hn.join.fieldwork.web.command;

public class CreateTrackInfoCommand {

	private String fieldWorkerNo;

	private String areaCode;

	private Float latitude;

	private Float longitude;

	private String address;

	private String createTime;

	public String getFieldWorkerNo() {
		return fieldWorkerNo;
	}

	public void setFieldWorkerNo(String fieldWorkerNo) {
		this.fieldWorkerNo = fieldWorkerNo;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public Float getLatitude() {
		return latitude;
	}

	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}

	public Float getLongitude() {
		return longitude;
	}

	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CreateTrackInfoCommand [fieldWorkerNo=")
				.append(fieldWorkerNo).append(", areaCode=").append(areaCode)
				.append(", latitude=").append(latitude).append(", longitude=")
				.append(longitude).append(", address=").append(address)
				.append(", createTime=").append(createTime).append("]");
		return builder.toString();
	}
	
	
	

}
