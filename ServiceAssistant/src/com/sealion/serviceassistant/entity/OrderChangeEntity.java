package com.sealion.serviceassistant.entity;

/**
 * 工单改派实例
 */
public class OrderChangeEntity
{
	private int id;
	private String order_num; //工单号
	private String change_reason; //改派原因
	private String change_require; //客户要求
	private String recommend_engineer; //推荐工程师
	private String change_time; //申请时间
	
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
