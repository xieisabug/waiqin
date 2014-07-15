package com.sealion.serviceassistant.entity;

/**
 * 税务局实例
 */
public class TaxBureauEntity
{
	private int id;
	private int bureau_id;
	private String bureau_name;
	private String city;
	
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public int getBureau_id()
	{
		return bureau_id;
	}
	public void setBureau_id(int bureau_id)
	{
		this.bureau_id = bureau_id;
	}
	public String getBureau_name()
	{
		return bureau_name;
	}
	public void setBureau_name(String bureau_name)
	{
		this.bureau_name = bureau_name;
	}
	public String getCity()
	{
		return city;
	}
	public void setCity(String city)
	{
		this.city = city;
	}
}
