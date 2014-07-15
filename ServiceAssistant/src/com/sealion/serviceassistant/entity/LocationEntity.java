package com.sealion.serviceassistant.entity;
/**
 * 定位相关地址信息
 */
public class LocationEntity
{
    /**
     * id
     */
	private int id;
    /**
     * 用户ID
     */
	private String userId; //用户ID
    /**
     * 纬度
     */
	private double latitude; //纬度
    /**
     * 经度
     */
	private double longitude; //经度
    /**
     * 地址
     */
	private String address; //地址
    /**
     * 定位时间
     */
	private String time; //定位时间
    /**
     * 地区代码
     */
	private String areaCode; //地区代码
	
	public String getUserId()
	{
		return userId;
	}
	public void setUserId(String userId)
	{
		this.userId = userId;
	}
	public double getLatitude()
	{
		return latitude;
	}
	public void setLatitude(double latitude)
	{
		this.latitude = latitude;
	}
	public double getLongitude()
	{
		return longitude;
	}
	public void setLongitude(double longitude)
	{
		this.longitude = longitude;
	}
	public String getAddress()
	{
		return address;
	}
	public void setAddress(String address)
	{
		this.address = address;
	}
	public String getTime()
	{
		return time;
	}
	public void setTime(String time)
	{
		this.time = time;
	}
	public String getAreaCode()
	{
		return areaCode;
	}
	public void setAreaCode(String areaCode)
	{
		this.areaCode = areaCode;
	}
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	
}
