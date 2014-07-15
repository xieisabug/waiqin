package com.sealion.serviceassistant.entity;

/**
 * 普通工单解决实例
 */
public class CommonOrderComplishEntity
{
    /**
     * id
     */
	private int id;
    /**
     * 工单号
     */
	private String order_num;
    /**
     * 客户名称
     */
	private String customer_name; //客户名称
    /**
     * 税号
     */
	private String customer_tax;  //税号
    /**
     * 税务分局
     */
	private String tax_officer;  //税务分局
    /**
     * 上门地址
     */
	private String service_address;  //上门地址
    /**
     * 联系人
     */
	private String contact_name;  //联系人
    /**
     * 客户电话
     */
	private String customer_tel;  //客户电话
    /**
     * 客户手机
     */
	private String customer_mobile;  //客户手机
    /**
     * 到达时间
     */
	private String service_time;  //到达时间
    /**
     * 单据号
     */
	private String number_value;  //单据号
    /**
     * 客户签名
     */
	private String customer_sign;  //客户签名
    /**
     * 费用过程
     */
	private String customer_charge;  //费用过程
    /**
     * 备注
     */
	private String customer_remark;  //备注
    /**
     * 服务方式
     * 0：上门 1：用户送修
     */
	private int customer_solve;  //服务方式 0:上门 1：用户送修
    /**
     * 软件型号及编号
     */
	private String software_type;  //软件型号及编号
    /**
     * 软件版本号
     */
	private String software_version;  //软件版本号
    /**
     * 使用环境
     */
	private String software_env_value;  // 使用环境
    /**
     * 是否发往服务器
     */
	private int is_send_to_server; //是否发往服务器
    /**
     * 是否收费
     * 0：不收 1：收
     */
	private int is_charge; //是否收费 0:不收 1：收
    /**
     * 收费金额
     */
	private String is_charge_value; //收费金额
    /**
     * 技术工程师姓名
     */
	private String tec_name; //技术工程师姓名
	
	//回访工单--------------------------------
    /**
     * 回访类型
     */
	private String visit_type; //回访类型
    /**
     * 产品运行情况
     */
	private String visit_product_case; //产品运行情况
    /**
     * 处理结果
     */
	private String visit_dispose_result; //处理结果
    /**
     * 服务评价
     * 0：很满意 1：满意 2：不满意
     */
	private int service_evaluate; //服务评价 0：很满意，1：满意，2：不满意
    /**
     * 产品评价
     * 0：很满意 1：满意 2：不满意
     */
	private int product_evaluate; //产品评价0：很满意，1：满意，2：不满意
	
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public String getOrder_num()
	{
		return order_num;
	}
	public void setOrder_num(String order_num)
	{
		this.order_num = order_num;
	}
	public String getCustomer_name()
	{
		return customer_name;
	}
	public void setCustomer_name(String customer_name)
	{
		this.customer_name = customer_name;
	}
	public String getCustomer_tax()
	{
		return customer_tax;
	}
	public void setCustomer_tax(String customer_tax)
	{
		this.customer_tax = customer_tax;
	}
	public String getTax_officer()
	{
		return tax_officer;
	}
	public void setTax_officer(String tax_officer)
	{
		this.tax_officer = tax_officer;
	}
	public String getService_address()
	{
		return service_address;
	}
	public void setService_address(String service_address)
	{
		this.service_address = service_address;
	}
	public String getContact_name()
	{
		return contact_name;
	}
	public void setContact_name(String contact_name)
	{
		this.contact_name = contact_name;
	}
	public String getCustomer_tel()
	{
		return customer_tel;
	}
	public void setCustomer_tel(String customer_tel)
	{
		this.customer_tel = customer_tel;
	}
	public String getCustomer_mobile()
	{
		return customer_mobile;
	}
	public void setCustomer_mobile(String customer_mobile)
	{
		this.customer_mobile = customer_mobile;
	}
	public String getService_time()
	{
		return service_time;
	}
	public void setService_time(String service_time)
	{
		this.service_time = service_time;
	}
	public String getNumber_value()
	{
		return number_value;
	}
	public void setNumber_value(String number_value)
	{
		this.number_value = number_value;
	}
	public String getCustomer_sign()
	{
		return customer_sign;
	}
	public void setCustomer_sign(String customer_sign)
	{
		this.customer_sign = customer_sign;
	}
	public String getCustomer_charge()
	{
		return customer_charge;
	}
	public void setCustomer_charge(String customer_charge)
	{
		this.customer_charge = customer_charge;
	}
	public String getCustomer_remark()
	{
		return customer_remark;
	}
	public void setCustomer_remark(String customer_remark)
	{
		this.customer_remark = customer_remark;
	}
	
	public int getCustomer_solve()
	{
		return customer_solve;
	}
	public void setCustomer_solve(int customer_solve)
	{
		this.customer_solve = customer_solve;
	}
	public String getSoftware_type()
	{
		return software_type;
	}
	public void setSoftware_type(String software_type)
	{
		this.software_type = software_type;
	}
	public String getSoftware_version()
	{
		return software_version;
	}
	public void setSoftware_version(String software_version)
	{
		this.software_version = software_version;
	}
	public String getSoftware_env_value()
	{
		return software_env_value;
	}
	public void setSoftware_env_value(String software_env_value)
	{
		this.software_env_value = software_env_value;
	}
	public String getVisit_type()
	{
		return visit_type;
	}
	public void setVisit_type(String visit_type)
	{
		this.visit_type = visit_type;
	}
	public String getVisit_product_case()
	{
		return visit_product_case;
	}
	public void setVisit_product_case(String visit_product_case)
	{
		this.visit_product_case = visit_product_case;
	}
	public String getVisit_dispose_result()
	{
		return visit_dispose_result;
	}
	public void setVisit_dispose_result(String visit_dispose_result)
	{
		this.visit_dispose_result = visit_dispose_result;
	}
	public int getIs_send_to_server()
	{
		return is_send_to_server;
	}
	public void setIs_send_to_server(int is_send_to_server)
	{
		this.is_send_to_server = is_send_to_server;
	}
	public int getIs_charge()
	{
		return is_charge;
	}
	public void setIs_charge(int is_charge)
	{
		this.is_charge = is_charge;
	}
	public String getIs_charge_value()
	{
		return is_charge_value;
	}
	public void setIs_charge_value(String is_charge_value)
	{
		this.is_charge_value = is_charge_value;
	}
	public String getTec_name()
	{
		return tec_name;
	}
	public void setTec_name(String tec_name)
	{
		this.tec_name = tec_name;
	}
	public int getService_evaluate()
	{
		return service_evaluate;
	}
	public void setService_evaluate(int service_evaluate)
	{
		this.service_evaluate = service_evaluate;
	}
	public int getProduct_evaluate()
	{
		return product_evaluate;
	}
	public void setProduct_evaluate(int product_evaluate)
	{
		this.product_evaluate = product_evaluate;
	}

    @Override
    public String toString() {
        return "CommonOrderComplishEntity{" +
                "id=" + id +
                ", order_num='" + order_num + '\'' +
                ", customer_name='" + customer_name + '\'' +
                ", customer_tax='" + customer_tax + '\'' +
                ", tax_officer='" + tax_officer + '\'' +
                ", service_address='" + service_address + '\'' +
                ", contact_name='" + contact_name + '\'' +
                ", customer_tel='" + customer_tel + '\'' +
                ", customer_mobile='" + customer_mobile + '\'' +
                ", service_time='" + service_time + '\'' +
                ", number_value='" + number_value + '\'' +
                ", customer_sign='" + customer_sign + '\'' +
                ", customer_charge='" + customer_charge + '\'' +
                ", customer_remark='" + customer_remark + '\'' +
                ", customer_solve=" + customer_solve +
                ", software_type='" + software_type + '\'' +
                ", software_version='" + software_version + '\'' +
                ", software_env_value='" + software_env_value + '\'' +
                ", is_send_to_server=" + is_send_to_server +
                ", is_charge=" + is_charge +
                ", is_charge_value='" + is_charge_value + '\'' +
                ", tec_name='" + tec_name + '\'' +
                ", visit_type='" + visit_type + '\'' +
                ", visit_product_case='" + visit_product_case + '\'' +
                ", visit_dispose_result='" + visit_dispose_result + '\'' +
                ", service_evaluate=" + service_evaluate +
                ", product_evaluate=" + product_evaluate +
                '}';
    }
}
