package com.sealion.serviceassistant.superactivity;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.sealion.serviceassistant.BusLineSearch;
import com.sealion.serviceassistant.R;
import com.sealion.serviceassistant.entity.NetBackDataEntity;
import com.sealion.serviceassistant.exception.NetAccessException;
import com.sealion.serviceassistant.net.HttpRestAchieve;
import com.sealion.serviceassistant.tools.DateTools;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources.NotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public abstract class OrderActivity extends SuperActivity
{
	protected String order_customer_mobile_value_text;
	protected String order_customer_phone_value_text;
	protected String customer_address;
	protected String order_num;
	private Intent intent = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

	}

	public void MapBtnClick(View target)
	{
		Intent intent = new Intent(this, BusLineSearch.class);
		intent.putExtra("customer_address", customer_address);
		this.startActivity(intent);
	}

	public void ContactBtnClick(View target)
	{
		Dialog dialog = new AlertDialog.Builder(this).setIcon(android.R.drawable.btn_star).setTitle("ѡ��").setMessage("ѡ����Ҫ����ĵ绰����").setPositiveButton("�����ֻ�", new OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				if (order_customer_mobile_value_text != null && order_customer_mobile_value_text.startsWith("01"))
				{
					order_customer_mobile_value_text = order_customer_mobile_value_text.substring(1, order_customer_mobile_value_text.length());					
				}
				intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + order_customer_mobile_value_text));
				new PostData().execute(sp.getString("WORKERNO", ""), order_num, DateTools.getFormatDateAndTime());
				// startActivity(intent);
				dialog.dismiss();
			}
		}).setNegativeButton("����̻�", new OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + order_customer_phone_value_text));
				new PostData().execute(sp.getString("WORKERNO", ""), order_num, DateTools.getFormatDateAndTime());
				// startActivity(intent);
				dialog.dismiss();
			}
		}).create();

		dialog.show();
	}

	public void ContactBackgroundBtnClick(View target)
	{
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + this.getResources().getString(R.string.office_phone)));
		startActivity(intent);
	}

	private class PostData extends AsyncTask<String,Void,Integer>
	{		
		@Override
		protected void onPreExecute()
		{
			ShowDialog();
		}
		
		@Override
		protected Integer doInBackground(String... params)
		{
			int result = postData(params);
			return result;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
						
			//doInBackground����ʱ���������仰˵������doInBackgroundִ����󴥷�
			//�����result��������doInBackgroundִ�к�ķ���ֵ������������"ִ�����"
			CancelDialog();
			
			if (result == 0)
			{
				if (intent != null)
				{
					startActivity(intent);
				}				
				Toast.makeText(OrderActivity.this, "�����ɹ���", Toast.LENGTH_LONG).show();
			}
			else if (result == 1)
			{
				Toast.makeText(OrderActivity.this, "����ʧ�ܣ������ԣ�", Toast.LENGTH_LONG).show();
			}
			else if (result == 2)
			{
				Toast.makeText(OrderActivity.this, "����������", Toast.LENGTH_LONG).show();
			}
			else if (result == 3)
			{
				Toast.makeText(OrderActivity.this, "����ʧ��,�����쳣��", Toast.LENGTH_LONG).show();
			}
			else if (result == 4)
			{
				Toast.makeText(OrderActivity.this, "����ʧ�ܣ����ݽ����쳣��", Toast.LENGTH_LONG).show();
			}
		}
	}
	
	public int postData(String... params)
	{
		HttpRestAchieve httpRestAchieve = new HttpRestAchieve();
		HashMap<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("workerNo", params[0]); //
		paramsMap.put("workOrderNo", params[1]);
		paramsMap.put("callTime", params[2]);
		
		int back_data = 0;
		//ȡ����ע��ť������¼�				
		try
		{
			NetBackDataEntity netBackData = httpRestAchieve.postRequestData(request_url 
					+ "/fieldworker/biz/callCustomer", paramsMap);
			String data = netBackData.getData();
			JSONObject jsonObject = new JSONObject(data);
			int result = jsonObject.getInt("resultCode");								
			if (result == 1)
			{
				back_data = 0;
			}
			else
			{
				//��¼���ݵ��������ݿ⣬�ȴ������绷�������ͬ��
				back_data = 1;
			}
		}
		catch (NotFoundException e)
		{
			back_data = 2;
			e.printStackTrace();
		}
		catch (NetAccessException e)
		{
			back_data = 3;
			e.printStackTrace();
		}
		catch (JSONException e)
		{
			back_data = 4;
			e.printStackTrace();
		}
		return back_data;
	}
}
