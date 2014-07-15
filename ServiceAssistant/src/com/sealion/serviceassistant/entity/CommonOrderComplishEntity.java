package com.sealion.serviceassistant.entity;

/**
 * ��ͨ�������ʵ��
 */
public class CommonOrderComplishEntity
{
    /**
     * id
     */
	private int id;
    /**
     * ������
     */
	private String order_num;
    /**
     * �ͻ�����
     */
	private String customer_name; //�ͻ�����
    /**
     * ˰��
     */
	private String customer_tax;  //˰��
    /**
     * ˰��־�
     */
	private String tax_officer;  //˰��־�
    /**
     * ���ŵ�ַ
     */
	private String service_address;  //���ŵ�ַ
    /**
     * ��ϵ��
     */
	private String contact_name;  //��ϵ��
    /**
     * �ͻ��绰
     */
	private String customer_tel;  //�ͻ��绰
    /**
     * �ͻ��ֻ�
     */
	private String customer_mobile;  //�ͻ��ֻ�
    /**
     * ����ʱ��
     */
	private String service_time;  //����ʱ��
    /**
     * ���ݺ�
     */
	private String number_value;  //���ݺ�
    /**
     * �ͻ�ǩ��
     */
	private String customer_sign;  //�ͻ�ǩ��
    /**
     * ���ù���
     */
	private String customer_charge;  //���ù���
    /**
     * ��ע
     */
	private String customer_remark;  //��ע
    /**
     * ����ʽ
     * 0������ 1���û�����
     */
	private int customer_solve;  //����ʽ 0:���� 1���û�����
    /**
     * ����ͺż����
     */
	private String software_type;  //����ͺż����
    /**
     * ����汾��
     */
	private String software_version;  //����汾��
    /**
     * ʹ�û���
     */
	private String software_env_value;  // ʹ�û���
    /**
     * �Ƿ���������
     */
	private int is_send_to_server; //�Ƿ���������
    /**
     * �Ƿ��շ�
     * 0������ 1����
     */
	private int is_charge; //�Ƿ��շ� 0:���� 1����
    /**
     * �շѽ��
     */
	private String is_charge_value; //�շѽ��
    /**
     * ��������ʦ����
     */
	private String tec_name; //��������ʦ����
	
	//�طù���--------------------------------
    /**
     * �ط�����
     */
	private String visit_type; //�ط�����
    /**
     * ��Ʒ�������
     */
	private String visit_product_case; //��Ʒ�������
    /**
     * ������
     */
	private String visit_dispose_result; //������
    /**
     * ��������
     * 0�������� 1������ 2��������
     */
	private int service_evaluate; //�������� 0�������⣬1�����⣬2��������
    /**
     * ��Ʒ����
     * 0�������� 1������ 2��������
     */
	private int product_evaluate; //��Ʒ����0�������⣬1�����⣬2��������
	
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
