package hn.join.fieldwork.web.dto;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
/**
 * 用于更改客户基本信息的DTO
 * @author chenjl
 * @version time:2014年6月11日 下午8:42:24
 *
 */
@Root(name="cust")
public class CustInfoDto {

	/**
	 * 客户ID,int型
	 */
	@Element(name = "customerId", required = false)
	private Integer customerId;

	/**
	 * 客户联系人 ,varchar(64)
	 */
	@Element(name = "linkPerson", required = false)
	private String linkPerson;

	/**
	 * 客户公司名称
	 */
	@Element(name = "customerName", required = false)
	private String customerName;

	/**
	 * 税号(可填)
	 */
	@Element(name = "taxCode", required = false)
	private String taxCode;

	
	/**
	 * 上门地址,varchar(256)
	 */
	@Element(name = "customerAddr", required = false)
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
	
	
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
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
	public CustInfoDto(){}
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
	public  CustInfoDto(String customerName,String tax_code,String customerAddr,String linkPerson,String customerTel,String customerMobile) {
		super();
		this.customerName=customerName;
		this.taxCode=tax_code;
		this.customerAddr=customerAddr;
		this.linkPerson=linkPerson;
		this.customerTel=customerTel;
		this.customerMobile=customerMobile;
	}
}
