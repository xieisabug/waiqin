/**   
* @Title: QuestionEntity.java 
* @Package com.sealion.serviceassistant.entity 
* @Description: TODO(��һ�仰�������ļ���ʲô) 
* @author jack.lee titans.lee@gmail.com   
* @date 2013-2-18 ����9:46:22 
* @version V1.0
* Copyright: Copyright (c)2012
* Company: �����ж�ͨ�ż������޹�˾
*/
package com.sealion.serviceassistant.entity;

/**   
 * ������ʵ��
 */
public class QuestionEntity
{
	private int id;
	private String order_num;//����ID
	private String q_id; //����ID
	private int q_type_id; //��������ID
	private int q_category_id; //�������ID
	private String q_content; //��������
	private String q_answer; //�����
	private String q_type;//��������
	private String q_category;//�������
	private String insert_time; //д��ʱ��
	private String productId; //��ƷID
	private String productName; //��Ʒ����
	
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
	public String getQ_id()
	{
		return q_id;
	}
	public void setQ_id(String q_id)
	{
		this.q_id = q_id;
	}	
	public int getQ_type_id()
	{
		return q_type_id;
	}
	public void setQ_type_id(int q_type_id)
	{
		this.q_type_id = q_type_id;
	}
	public int getQ_category_id()
	{
		return q_category_id;
	}
	public void setQ_category_id(int q_category_id)
	{
		this.q_category_id = q_category_id;
	}
	public String getQ_content()
	{
		return q_content;
	}
	public void setQ_content(String q_content)
	{
		this.q_content = q_content;
	}
	public String getQ_answer()
	{
		return stringShow(q_answer);
	}
	public void setQ_answer(String q_answer)
	{
		this.q_answer = q_answer;
	}
	public String getQ_type()
	{
		return q_type;
	}
	public void setQ_type(String q_type)
	{
		this.q_type = q_type;
	}
	public String getQ_category()
	{
		return q_category;
	}
	public void setQ_category(String q_category)
	{
		this.q_category = q_category;
	}
	public String getInsert_time()
	{
		return insert_time;
	}
	public void setInsert_time(String insert_time)
	{
		this.insert_time = insert_time;
	}
	public String getProductId()
	{
		return productId;
	}
	public void setProductId(String productId)
	{
		this.productId = productId;
	}
	public String getProductName()
	{
		return productName;
	}
	public void setProductName(String productName)
	{
		this.productName = productName;
	}

	private String stringShow(String s)
	{
		if(s.equalsIgnoreCase("null"))
			return "";
		else
			return s;
	}
}
