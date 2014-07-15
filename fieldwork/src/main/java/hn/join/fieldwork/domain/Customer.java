package hn.join.fieldwork.domain;

import java.util.Date;
/*
 * 客户对象
 */
public class Customer extends BaseDomain{
	

	private static final long serialVersionUID = 4891723454556094164L;
	/**
	 * 主键
	 */
	private Long id;
	/**客户地址*/
	private String customerAddress;
	/**客户名称*/
	private String customerName;
	/**客户联系人*/
	private String contact;
	/**客户区域编码*/
	private String areaCode;
	/**客户所在城市*/
	private String county;
	/**创建日期*/
	private Date createTime;
	/**客户电话*/
	private String tel;
	/**客户手机号码*/
	private String mobile;
	/**客户纬度*/
	private Float latitude;
	/**客户经度*/
	private Float longitude;
	/**客户服务单位*/
	private Integer departmentId;
	/**客户税务局ID*/
	private Integer revenueId;
	/**客户乘号*/
	private String taxCode;
	
//	private  List<CustomerJournal> journals;
	/*客户标志*/
	private  short tag;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

	
	

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public Integer getRevenueId() {
		return revenueId;
	}

	public void setRevenueId(Integer revenueId) {
		this.revenueId = revenueId;
	}

	public String getTaxCode() {
		return taxCode;
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}
	
	

//	@JSONField(serialize=false)
//	public List<CustomerJournal> getJournals() {
//		return journals;
//	}
//
//	public void setJournals(List<CustomerJournal> journals) {
//		this.journals = journals;
//	}

	public short getTag() {
		return tag;
	}

	public void setTag(short tag) {
		this.tag = tag;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Customer other = (Customer) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	


	

}
