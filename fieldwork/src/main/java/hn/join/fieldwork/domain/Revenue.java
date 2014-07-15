package hn.join.fieldwork.domain;
/**
 * 税务局对象
 * @author chenjinlong
 *
 */
public class Revenue extends BaseDomain{
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8845295854170571502L;
	/**主键*/
	private Integer id;
	/**税务局编码*/
	private String revenueCode;
	/**税务局名称*/
	private String revenueName;
	/**城市编码*/
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((revenueCode == null) ? 0 : revenueCode.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Revenue other = (Revenue) obj;
		if (revenueCode == null) {
			if (other.revenueCode != null)
				return false;
		} else if (!revenueCode.equals(other.revenueCode))
			return false;
		return true;
	}
	
	
	

}
