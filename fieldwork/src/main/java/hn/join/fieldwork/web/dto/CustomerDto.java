package hn.join.fieldwork.web.dto;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "customer")
public class CustomerDto {

	/**
	 * 客户ID,int型(必填)
	 */
	@Element(name = "customerId")
	private Long customerId;

	/**
	 * 客户联系人 ,varchar(64)(必填)
	 */
	@Element(name = "linkPerson")
	private String linkPerson;

	/**
	 * 客户公司名称(必填)
	 */
	@Element(name = "customerName")
	private String customerName;

	/**
	 * 税号(可填)
	 */
	@Element(name = "taxCode", required = false)
	private String taxCode;

	/**
	 * 所属部门ID,int型(必填)
	 */
	@Element(name = "departmentId")
	private Integer departmentId;

	/**
	 * 所属部门名称,varchar(64)(必填)
	 */
	@Element(name = "departmentName",required = false)
	private String departmentName;

	/**
	 * 所属税务分局ID,int型
	 */
	@Element(name = "revenueId",required = false)
	private Integer revenueId;

	/**
	 * 所属税务分局名称,varchar(64)
	 */
	@Element(name = "revenueName", required = false)
	private String revenueName;

	/**
	 * 上门地址,varchar(256)(必填)
	 */
	@Element(name = "customerAddr")
	private String customerAddr;

	/**
	 * 客户手机 varchar(64) 
	 */
	@Element(name = "customerMobile",required=false)
	private String customerMobile;

	/**
	 * 客户联系电话 varchar(64) 
	 */
	@Element(name = "customerTel",required=false)
	private String customerTel;

	/**
	 * 客户所在区、县,varchar(32)(可填)
	 */
	@Element(name = "customerCounty", required = false)
	private String customerCounty;

	/**
	 * 客户地址所在纬度,varchar(9)(可填)
	 */
	@Element(name = "customerLatitude", required = false)
	private Float customerLatitude;

	/**
	 * 客户地址所在经度,varchar(9)(可填)
	 */
	@Element(name = "customerLongitude", required = false)
	private Float customerLongitude;

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getLinkPerson() {
		return linkPerson;
	}

	public void setLinkPerson(String linkPerson) {
		this.linkPerson = linkPerson;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getTaxCode() {
		return taxCode;
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public Integer getRevenueId() {
		return revenueId;
	}

	public void setRevenueId(Integer revenueId) {
		this.revenueId = revenueId;
	}

	public String getRevenueName() {
		return revenueName;
	}

	public void setRevenueName(String revenueName) {
		this.revenueName = revenueName;
	}

	public String getCustomerAddr() {
		return customerAddr;
	}

	public void setCustomerAddr(String customerAddr) {
		this.customerAddr = customerAddr;
	}

	public String getCustomerMobile() {
		return customerMobile;
	}

	public void setCustomerMobile(String customerMobile) {
		this.customerMobile = customerMobile;
	}

	public String getCustomerTel() {
		return customerTel;
	}

	public void setCustomerTel(String customerTel) {
		this.customerTel = customerTel;
	}

	public String getCustomerCounty() {
		return customerCounty;
	}

	public void setCustomerCounty(String customerCounty) {
		this.customerCounty = customerCounty;
	}

	public Float getCustomerLatitude() {
		return customerLatitude;
	}

	public void setCustomerLatitude(Float customerLatitude) {
		this.customerLatitude = customerLatitude;
	}

	public Float getCustomerLongitude() {
		return customerLongitude;
	}

	public void setCustomerLongitude(Float customerLongitude) {
		this.customerLongitude = customerLongitude;
	}
	
	public CustomerDto(){}
	/**
	 * 构造方法
	 * @param customerName
	 * @param tax_code
	 * @param customerAddr
	 * @param linkPerson
	 * @param customerTel
	 * @param customerMobile
	 * @author chenjl 2014-06-11 19:36
	 */
	public  CustomerDto(String customerName,String tax_code,String customerAddr,String linkPerson,String customerTel,String customerMobile) {
		super();
		this.customerName=customerName;
		this.taxCode=tax_code;
		this.customerAddr=customerAddr;
		this.linkPerson=linkPerson;
		this.customerTel=customerTel;
		this.customerMobile=customerMobile;
	}

}
