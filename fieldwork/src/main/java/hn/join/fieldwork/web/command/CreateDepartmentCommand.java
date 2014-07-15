package hn.join.fieldwork.web.command;

public class CreateDepartmentCommand {
	
	private Integer id;
	
	private String departmentCode;
	
	private String departmentName;
	
	private String city;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDepartmentCode() {
		return departmentCode;
	}

	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CreateDepartmentCommand [id=").append(id)
				.append(", departmentCode=").append(departmentCode)
				.append(", departmentName=").append(departmentName)
				.append(", city=").append(city).append("]");
		return builder.toString();
	}

	

}
