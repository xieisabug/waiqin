package com.sealion.serviceassistant.entity;

/**
 * 问题类别
 */
public class Q_Category
{
	private int id;
	private int category_id;
	private String name;
	private String productId;
	private String productName;
	
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public int getCategory_id()
	{
		return category_id;
	}
	public void setCategory_id(int category_id)
	{
		this.category_id = category_id;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
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
	
	
}
