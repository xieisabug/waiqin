package com.sealion.serviceassistant.entity;

import org.apache.http.Header;

/**
 * ����ģ�鷵������
 */

public class NetBackDataEntity
{
	private Header[]  header;
	private String data;
	
	public Header[] getHeader()
	{
		return header;
	}
	public void setHeader(Header[] header)
	{
		this.header = header;
	}
	public String getData()
	{
		return data;
	}
	public void setData(String data)
	{
		this.data = data;
	}
	
}
