package com.sealion.serviceassistant.entity;

/**
 * 问题类型
 */
public class Q_Type
{
	private int id;
	private int type_id;
	private int q_category_id;
	private String name;
	
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public int getType_id()
	{
		return type_id;
	}
	public void setType_id(int type_id)
	{
		this.type_id = type_id;
	}
	public int getQ_category_id()
	{
		return q_category_id;
	}
	public void setQ_category_id(int q_category_id)
	{
		this.q_category_id = q_category_id;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
}
