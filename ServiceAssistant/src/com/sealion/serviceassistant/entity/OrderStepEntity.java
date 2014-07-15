package com.sealion.serviceassistant.entity;

/**
 * 工单步骤实例
 */
public class OrderStepEntity
{
	private int id;
	private String order_num;
	private int order_state;
	private String oper_time;
	
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
	public int getOrder_state()
	{
		return order_state;
	}
	public void setOrder_state(int order_state)
	{
		this.order_state = order_state;
	}
	public String getOper_time()
	{
		return oper_time;
	}
	public void setOper_time(String oper_time)
	{
		this.oper_time = oper_time;
	}
}
