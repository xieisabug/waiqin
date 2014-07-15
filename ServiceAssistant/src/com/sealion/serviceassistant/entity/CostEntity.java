package com.sealion.serviceassistant.entity;

/**
 * ¿ªÏúÊµÀý
 */
public class CostEntity
{
	private int id;
	private int cost_id;
	private String name;
	private String value;
	private String order_num;
	private String insert_time;
	
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public int getCost_id()
	{
		return cost_id;
	}
	public void setCost_id(int cost_id)
	{
		this.cost_id = cost_id;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getValue()
	{
		return value;
	}
	public void setValue(String value)
	{
		this.value = value;
	}
	public String getOrder_num()
	{
		return order_num;
	}
	public void setOrder_num(String order_num)
	{
		this.order_num = order_num;
	}
	public String getInsert_time()
	{
		return insert_time;
	}
	public void setInsert_time(String insert_time)
	{
		this.insert_time = insert_time;
	}
	
}
