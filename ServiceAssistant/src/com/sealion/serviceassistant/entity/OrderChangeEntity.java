package com.sealion.serviceassistant.entity;

/**
 * ��������ʵ��
 */
public class OrderChangeEntity
{
	private int id;
	private String order_num; //������
	private String change_reason; //����ԭ��
	private String change_require; //�ͻ�Ҫ��
	private String recommend_engineer; //�Ƽ�����ʦ
	private String change_time; //����ʱ��
	
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
	public String getChange_reason()
	{
		return change_reason;
	}
	public void setChange_reason(String change_reason)
	{
		this.change_reason = change_reason;
	}
	public String getChange_require()
	{
		return change_require;
	}
	public void setChange_require(String change_require)
	{
		this.change_require = change_require;
	}
	public String getRecommend_engineer()
	{
		return recommend_engineer;
	}
	public void setRecommend_engineer(String recommend_engineer)
	{
		this.recommend_engineer = recommend_engineer;
	}
	public String getChange_time()
	{
		return change_time;
	}
	public void setChange_time(String change_time)
	{
		this.change_time = change_time;
	}
		
}
