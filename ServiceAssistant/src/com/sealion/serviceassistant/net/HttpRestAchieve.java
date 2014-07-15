package com.sealion.serviceassistant.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.params.ConnManagerPNames;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import com.sealion.serviceassistant.entity.NetBackDataEntity;
import com.sealion.serviceassistant.exception.NetAccessException;


import android.util.Log;


/**
 * 完成IHttpRest接口实现，基于HttpClient
 */
public class HttpRestAchieve implements IHttpRest {
    private final String TAG = this.getClass().getSimpleName();
    private HttpClient httpClient;

    /**
     * 连接超时时间
     */
    private static final int CONNECTION_TIMEOUT = 10000; /* 10 seconds */

    /**
     * 等待数据阻塞的时间
     */
    private static final int SOCKET_TIMEOUT = 10000; /* 10 seconds */

    /**
     * ManagedClientConnection创建超时的时间
     */
    private static final long MCC_TIMEOUT = 10000; /* 10 seconds */

    public HttpRestAchieve() {
        Log.d(TAG, "The HttpRestAchieve thread id = " + Thread.currentThread().getId() + "\n");
        getHttpClient();
    }

    /**
     * 设置超时时间
     * @param params 设置参数
     */
    private static void setTimeouts(HttpParams params) {
        params.setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
                CONNECTION_TIMEOUT);
        params.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, SOCKET_TIMEOUT);
        params.setLongParameter(ConnManagerPNames.TIMEOUT, MCC_TIMEOUT);
    }

    /**
     * 初始化httpclient请求对象
     */
    public void getHttpClient() {
        // create HttpClient instance
        httpClient = CustomHttpClient.getHttpClient();
    }

    /**
     * 从相应地址获取数据
     * @param url 获取的地址
     * @param paramsMap 参数列表
     * @return 返回的数据
     * @throws NetAccessException
     */
    @Override
    public NetBackDataEntity getRequestData(String url, Map<String, String> paramsMap) throws NetAccessException {
        // add get request parameters
        StringBuilder paramStr = new StringBuilder();

        for (String key : paramsMap.keySet()) {
            String value = paramsMap.get(key);
            paramStr.append("&");
            paramStr.append(key);
            paramStr.append("=");
            paramStr.append(value);
        }
        String prams = paramStr.toString();

        if (!prams.equals("")) {
            prams = prams.replaceFirst("&", "?");
            url += prams;
        }

        HttpRequestBase getRequest = new HttpGet(url);
        setTimeouts(getRequest.getParams());
        HttpResponse response;
        NetBackDataEntity netBackData;

        try {
            response = httpClient.execute(getRequest);
            if (response != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                netBackData = new NetBackDataEntity();
                netBackData.setHeader(response.getAllHeaders());
                netBackData.setData(EntityUtils.toString(response.getEntity()));
            } else {
                throw new NetAccessException("call get request function exception");
            }
        } catch (ClientProtocolException e) {
            if (e.getMessage() != null) {
                Log.d(TAG, e.getMessage());
            }

            throw new NetAccessException("call get request function exception" + e.getMessage());
        } catch (IOException e) {
            if (e.getMessage() != null) {
                Log.d(TAG, e.getMessage());
            }
            throw new NetAccessException("call get request function exception" + e.getMessage());
        }
        return netBackData;
    }

    /**
     * 发送数据到指定地址
     * @param url 发送的地址
     * @param paramsMap 发送的参数
     * @return 返回的数据
     * @throws NetAccessException
     */
    @Override
    public NetBackDataEntity postRequestData(String url, Map<String, String> paramsMap) throws NetAccessException {
        List<NameValuePair> param = new ArrayList<NameValuePair>();

        if (paramsMap != null) {
            // add parameters
            for (String key : paramsMap.keySet()) {
                param.add(new BasicNameValuePair(key, paramsMap.get(key)));
            }

        }

        Log.d(TAG, url);

        HttpPost request = new HttpPost(url);
        setTimeouts(request.getParams());
        NetBackDataEntity netBackData = null;
        try {
            HttpEntity entity = new UrlEncodedFormEntity(param, "UTF-8");
            request.setEntity(entity);
            HttpResponse response = httpClient.execute(request);
            netBackData = new NetBackDataEntity();
            if (response != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                netBackData.setHeader(response.getAllHeaders());
                netBackData.setData(EntityUtils.toString(response.getEntity(), "UTF-8"));
            } else {
                throw new NetAccessException("invoke post request function exception");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            if (e.getMessage() != null) {
                Log.d(TAG, e.getMessage());
            }
            throw new NetAccessException("invoke post request function exception");
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            if (e.getMessage() != null) {
                Log.d(TAG, e.getMessage());
            }
            throw new NetAccessException("invoke post request function exception");
        } catch (IOException e) {
            e.printStackTrace();
            if (e.getMessage() != null) {
                Log.d(TAG, e.getMessage());
            }
            throw new NetAccessException("invoke post request function exception");
        }
        return netBackData;
    }

    /**
     * 发送数据到指定地址
     * @param url 发送的地址
     * @param paramsMap 发送的参数
     * @return 返回的数据
     * @throws NetAccessException
     */
    @Override
    public NetBackDataEntity putRequestData(String url, Map<String, String> paramsMap) throws NetAccessException {
        List<NameValuePair> param = new ArrayList<NameValuePair>();

        // add parameters
        for (String key : paramsMap.keySet()) {
            param.add(new BasicNameValuePair(key, paramsMap.get(key)));
        }

        HttpPut request = new HttpPut(url);
        setTimeouts(request.getParams());
        NetBackDataEntity netBackData;
        try {
            HttpEntity entity = new UrlEncodedFormEntity(param, "UTF-8");
            request.setEntity(entity);

            HttpResponse response = httpClient.execute(request);
            netBackData = new NetBackDataEntity();

            if (response != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                netBackData.setHeader(response.getAllHeaders());
                netBackData.setData(EntityUtils.toString(response.getEntity()));
            } else {
                throw new NetAccessException("invoke delete request function exception");
            }
        } catch (UnsupportedEncodingException e) {
            if (e.getMessage() != null) {
                Log.d(TAG, e.getMessage());
            }
            throw new NetAccessException("invoke delete request function exception");
        } catch (ClientProtocolException e) {
            if (e.getMessage() != null) {
                Log.d(TAG, e.getMessage());
            }
            throw new NetAccessException("invoke delete request function exception");
        } catch (IOException e) {
            if (e.getMessage() != null) {
                Log.d(TAG, e.getMessage());
            }
            throw new NetAccessException("invoke delete request function exception");
        }
        return netBackData;
    }

    /**
     * 删除数据到指定地址
     * @param url 删除的地址
     * @return 返回的数据
     * @throws NetAccessException
     */
    @Override
    public NetBackDataEntity deleteRequestData(String url) throws NetAccessException {

        HttpDelete request = new HttpDelete(url);
        setTimeouts(request.getParams());
        NetBackDataEntity netBackData;
        try {
            HttpResponse response = httpClient.execute(request);
            netBackData = new NetBackDataEntity();

            if (response != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                netBackData.setHeader(response.getAllHeaders());
                netBackData.setData(EntityUtils.toString(response.getEntity()));
            } else {
                throw new NetAccessException("invoke delete request function exception");
            }
        } catch (UnsupportedEncodingException e) {
            if (e.getMessage() != null) {
                Log.d(TAG, e.getMessage());
            }
            throw new NetAccessException("invoke delete request function exception");
        } catch (ClientProtocolException e) {
            if (e.getMessage() != null) {
                Log.d(TAG, e.getMessage());
            }
            throw new NetAccessException("invoke delete request function exception");
        } catch (IOException e) {
            if (e.getMessage() != null) {
                Log.d(TAG, e.getMessage());
            }
            throw new NetAccessException("invoke delete request function exception");
        }

        return netBackData;
    }
}
