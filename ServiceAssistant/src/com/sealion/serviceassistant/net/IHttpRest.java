package com.sealion.serviceassistant.net;

import java.util.Map;

import com.sealion.serviceassistant.entity.NetBackDataEntity;
import com.sealion.serviceassistant.exception.NetAccessException;


/**
 * 
* @System Name：外勤管�? APP
* @Model Name :
* @Model Function：完成网络访问的基本功能接口，采用REST风格的形式，
* 											get,post,put,delete分别对应查，提交，修改，删除
* @Progrmmer：jack.lee
* @version：v0.0.1
* @create time�?011-12-31 下午12:45:00
* @tools：Eclipse Indigo Service Release 1 java1.6
* @last update time�?011-12-31
 */
public interface IHttpRest
{
	/**
	 * get the data by the url
	 * @param url   : get path for the request
	 * @return
	 */
	public NetBackDataEntity getRequestData(String url, Map<String,String>  paramsMap)  throws NetAccessException ;
	
	/**
	 * post data by the url
	 * @param url  : post path for the post request
	 * @param dataMap : entity data map
	 * @return
	 */
	public NetBackDataEntity postRequestData(String url, Map<String,String>  paramsMap) throws NetAccessException ;
	
	/**
	 * update data by the url
	 * @param url : update data url path
	 * @param dataMap : update data
	 * @return
	 */
	public NetBackDataEntity putRequestData(String url,Map<String,String>  paramsMap) throws NetAccessException ;
	
	/**
	 * delete data by the url
	 * @param url : delete data url path
	 * @param dataMap : delete data map (ie: id)
	 * @return
	 */
	public NetBackDataEntity deleteRequestData(String url) throws NetAccessException ;
}
