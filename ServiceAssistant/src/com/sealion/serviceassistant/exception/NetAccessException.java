package com.sealion.serviceassistant.exception;

/**
 * 
* @System Name：移动税务APP
* @Model Name : 网络访问异常自定�?* @Model Function�?
* @Progrmmer：�?jack.lee�?
* @version：v0.0.1
* @create time�?011-12-31 下午04:02:22
* @tools：Eclipse Indigo Service Release 1 java1.6
* @last update time�?011-12-31
 */

public class NetAccessException extends Exception
{
	private static final long serialVersionUID = 1L;

	public NetAccessException()
	{
		super();
	}
	
	public NetAccessException(String msg)
	{
		super(msg);
	}
	
	public NetAccessException(String msg, Throwable cause)
	{
		super(msg, cause);
	}
	
	public NetAccessException(Throwable cause)
	{
		super(cause);
	}
}
