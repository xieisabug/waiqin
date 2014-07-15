/**   
* @Title: QuestionEntity.java 
* @Package com.sealion.serviceassistant.entity 
* @Description: TODO(用一句话描述该文件做什么) 
* @author jack.lee titans.lee@gmail.com   
* @date 2013-2-18 上午9:46:22 
* @version V1.0
* Copyright: Copyright (c)2012
* Company: 湖南中恩通信技术有限公司
*/
package com.sealion.serviceassistant.entity;

/**   
 * 问题类实体
 */
public class QuestionEntity
{
	private int id;
	private String order_num;//工单ID
	private String q_id; //问题ID
	private int q_type_id; //问题类型ID
	private int q_category_id; //问题类别ID
	private String q_content; //问题内容
	private String q_answer; //问题答案
	private String q_type;//问题类型
	private String q_category;//问题类别
	private String insert_time; //写入时间
	private String productId; //产品ID
	private String productName; //产品名字
	
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
