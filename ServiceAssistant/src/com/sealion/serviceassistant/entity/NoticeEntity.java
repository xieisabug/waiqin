package com.sealion.serviceassistant.entity;
/**
 * 通知实例
 */
public class NoticeEntity
{
	private int id;
	private String title;
	private String publish_time;
	private String content;
	private int sign; //0:未阅读，1：已阅读
	
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	public String getPublish_time()
	{
		return publish_time;
	}
	public void setPublish_time(String publish_time)
	{
		this.publish_time = publish_time;
	}
	public String getContent()
	{
		return content;
	}
	public void setContent(String content)
	{
		this.content = content;
	}
	public int getSign()
	{
		return sign;
	}
	public void setSign(int sign)
	{
		this.sign = sign;
	}
}
