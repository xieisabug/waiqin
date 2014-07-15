package com.sealion.serviceassistant.entity;
/**
 * ��λ��ص�ַ��Ϣ
 */
public class LocationEntity
{
    /**
     * id
     */
	private int id;
    /**
     * �û�ID
     */
	private String userId; //�û�ID
    /**
     * γ��
     */
	private double latitude; //γ��
    /**
     * ����
     */
	private double longitude; //����
    /**
     * ��ַ
     */
	private String address; //��ַ
    /**
     * ��λʱ��
     */
	private String time; //��λʱ��
    /**
     * ��������
     */
	private String areaCode; //��������
	
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
