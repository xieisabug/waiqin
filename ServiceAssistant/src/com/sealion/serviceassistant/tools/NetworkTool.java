package com.sealion.serviceassistant.tools;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.sealion.serviceassistant.LoginActivity;
import com.sealion.serviceassistant.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Log;

/**
 * 网络工具类
 * 包含各种跟网络有关的工具方法
 */
public class NetworkTool
{
	
	/**
	 * 获取网址内容
	 * 
	 * @param url 连接的地址
	 * @return 返回从相应的网址获取到的内容
	 * @throws Exception 抛出IOException
	 */
	public static String getContent(String url) throws Exception
	{
		StringBuilder sb = new StringBuilder();

		HttpClient client = new DefaultHttpClient();
		HttpParams httpParams = client.getParams();
		// 设置网络超时参数
		HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
		HttpConnectionParams.setSoTimeout(httpParams, 5000);
		HttpResponse response = client.execute(new HttpGet(url));
		HttpEntity entity = response.getEntity();
		if (entity != null)
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(),
					"UTF-8"), 8192);

			String line;
			while ((line = reader.readLine()) != null)
			{
				sb.append(line).append("\n");
			}
			reader.close();
		}
		return sb.toString();
	}
	
	/**
	 * 获取网络类型，返回值是ConnectivityManager中的常量类型
	 * @param ctx 程序运行的上下文
	 * @return 0:wifi,1:3g
	 */
	public static int getNetType(Context ctx)
	{
		int result = -1;
		ConnectivityManager connect = (ConnectivityManager)
			ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetwork = connect.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null ) {
            if (wifiNetwork.getState() == NetworkInfo.State.CONNECTED){
                result = ConnectivityManager.TYPE_WIFI; //wifi
            }
        }
        NetworkInfo threeGNetwork = connect.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (threeGNetwork != null) {
            if (threeGNetwork.getState() == NetworkInfo.State.CONNECTED) {
                result = ConnectivityManager.TYPE_MOBILE; //3g
            }
        }
        return result;
	}

    /**
     * 获取当前本地的Ip地址
     * @return ip地址，如果找不到则返回空字符串
     */
	public static String getLocalIpAddress()
	{
		try
		{
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)
			{
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
				{
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress())
					{
						return inetAddress.getHostAddress();
					}
				}
			}
		}
		catch (SocketException ex)
		{
			//Log.e(TAG, ex.toString());
		}
		return "";
	}

}
