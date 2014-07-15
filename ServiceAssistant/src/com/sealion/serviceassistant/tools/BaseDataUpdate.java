package com.sealion.serviceassistant.tools;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.sealion.serviceassistant.entity.NetBackDataEntity;
import com.sealion.serviceassistant.exception.NetAccessException;
import com.sealion.serviceassistant.net.HttpRestAchieve;

import android.content.res.Resources.NotFoundException;
import android.os.AsyncTask;
import android.util.Log;

public class BaseDataUpdate extends AsyncTask<String, Void, Void>
{
	private static final String TAG = BaseDataUpdate.class.getSimpleName();
	
	private String request_url;
	
	public BaseDataUpdate(String request_url)
	{
		this.request_url = request_url;
	}
	
	@Override
	protected Void doInBackground(String... params)
	{
		
		return null;
	}

	@Override
	protected void onPostExecute(Void result)
	{

	}
	
	public int postData(String... params)
	{
		int back_data = 0;
		HttpRestAchieve httpRestAchieve = new HttpRestAchieve();
		HashMap<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("dataup", params[0]);
		try
		{
			
			NetBackDataEntity netBackData = httpRestAchieve.postRequestData(request_url + "/fieldworker/login", paramsMap);
			String data = netBackData.getData();

			JSONObject jsonObject = new JSONObject(data);
			int result = jsonObject.getInt("resultCode");								
			if (result == 1)
			{				
				if (params[0].equals("revenue"))
				{
					
				}
				else if (params[1].equals("problem_category"))
				{
					
				}
				else if (params[1].equals("problem_type"))
				{
					
				}
				else if (params[1].equals("expense_item"))
				{
					
				}
				back_data = 0;
			}
			else
			{
				back_data = 1;
				
			}
			
		}
		catch (NotFoundException e)
		{
			back_data = 2;
			//Toast.makeText(this, "服务器错误！", Toast.LENGTH_LONG).show();
			e.printStackTrace();
			Log.d(TAG, e.getMessage());
		}
		catch (NetAccessException e)
		{
			back_data = 3;
			//Toast.makeText(this, "操作失败,网络异常！", Toast.LENGTH_LONG).show();
			e.printStackTrace();
			Log.d(TAG, e.getMessage());
		}
		catch (JSONException e)
		{
			back_data = 4;
			//Toast.makeText(this, "操作失败，数据解析异常！", Toast.LENGTH_LONG).show();
			e.printStackTrace();
			Log.d(TAG, e.getMessage());
		}
		
		return back_data;
	}
}
