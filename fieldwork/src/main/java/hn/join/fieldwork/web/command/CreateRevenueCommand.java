package hn.join.fieldwork.web.command;

public class CreateRevenueCommand {

	private Integer id;

	private String revenueCode;

	private String revenueName;

	private String cityCode;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRevenueCode() {
		return revenueCode;
	}

	public void setRevenueCode(String revenueCode) {
		this.revenueCode = revenueCode;
	}

	public String getRevenueName() {
		return revenueName;
	}

	public void setRevenueName(String revenueName) {
		this.revenueName = revenueName;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CreateRevenueCommand [id=").append(id)
				.append(", revenueCode=").append(revenueCode)
				.append(", revenueName=").append(revenueName)
				.append(", cityCode=").append(cityCode).append("]");
		return builder.toString();
	}

}
