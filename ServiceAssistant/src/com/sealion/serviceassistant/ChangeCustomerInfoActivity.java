package com.sealion.serviceassistant;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.sealion.serviceassistant.entity.NetBackDataEntity;
import com.sealion.serviceassistant.exception.NetAccessException;
import com.sealion.serviceassistant.net.HttpRestAchieve;
import com.sealion.serviceassistant.superactivity.OrderActivity;
import com.sealion.serviceassistant.widget.PopWin;

import android.content.res.Resources.NotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

/**   
* �޸Ŀͻ��Ļ�����Ϣ.
*/
public class ChangeCustomerInfoActivity extends OrderActivity
{
	private static final String TAG = ChangeCustomerInfoActivity.class.getSimpleName();
	
	private RadioButton order_customer_address = null; //�����пͻ���ַ button
	private RadioButton gps_customer_address = null; //ͨ��gps��λ�ĵ�ַbutton
	private RadioButton input_customer_address = null; //�ͻ������ַ
	
	private EditText order_customer_address_edittext = null;
	private EditText gps_customer_address_edittext = null;
	private EditText input_customer_address_edittext = null;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.change_customer_info);
		
		order_customer_address = (RadioButton)this.findViewById(R.id.order_customer_address);
		gps_customer_address = (RadioButton)this.findViewById(R.id.gps_customer_address);
		input_customer_address = (RadioButton)this.findViewById(R.id.input_customer_address);
		
		order_customer_address_edittext = (EditText)this.findViewById(R.id.order_customer_address_edittext);
		gps_customer_address_edittext = (EditText)this.findViewById(R.id.gps_customer_address_edittext);
		input_customer_address_edittext = (EditText)this.findViewById(R.id.input_customer_address_edittext);
		
		PopWin popWin = new PopWin(ChangeCustomerInfoActivity.this);
		popWin.initPopWindow(getResources().getString(R.string.solve_tips),2,null,ChangeCustomerInfoActivity.this);
	}
	
	public void AddBtnClick(View target)
	{
		String address = "";
		if (order_customer_address.isChecked())
		{
			String order_customer_address_edittext_text = order_customer_address_edittext.getText().toString();
			if (order_customer_address_edittext_text == null || order_customer_address_edittext_text.equals(""))
			{
				Toast.makeText(this, "", Toast.LENGTH_LONG).show();
				return;
			}
			else
			{
				address = order_customer_address_edittext_text;
			}
		}
		
		if (gps_customer_address.isChecked())
		{
			String gps_customer_address_edittext_text = gps_customer_address_edittext.getText().toString();
			if (gps_customer_address_edittext_text == null || gps_customer_address_edittext_text.equals(""))
			{
				Toast.makeText(this, "", Toast.LENGTH_LONG).show();
				return;
			}
			else
			{
				address = gps_customer_address_edittext_text;
			}
		}
		
		if (input_customer_address.isChecked())
		{
			String input_customer_address_edittext_text = input_customer_address_edittext.getText().toString();
			if (input_customer_address_edittext_text == null || input_customer_address_edittext_text.equals(""))
			{
				Toast.makeText(this, "", Toast.LENGTH_LONG).show();
				return;
			}
			else
			{
				address = input_customer_address_edittext_text;
			}
		}
		
		new PostData().execute(address);
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
			int result = postData(params[0]);
			return result;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			
			//doInBackground����ʱ���������仰˵������doInBackgroundִ����󴥷�
			//�����result��������doInBackgroundִ�к�ķ���ֵ������������"ִ�����"
			CancelDialog();
			
			if (result == 0)
			{
				Toast.makeText(ChangeCustomerInfoActivity.this, "�����ɹ���", Toast.LENGTH_LONG).show();
			}
			else if (result == 1)
			{
				Toast.makeText(ChangeCustomerInfoActivity.this, "����ʧ�ܣ������ԣ�", Toast.LENGTH_LONG).show();
			}
			else if (result == 2)
			{
				Toast.makeText(ChangeCustomerInfoActivity.this, "����������", Toast.LENGTH_LONG).show();
			}
			else if (result == 3)
			{
				Toast.makeText(ChangeCustomerInfoActivity.this, "����ʧ��,�����쳣��", Toast.LENGTH_LONG).show();
			}
			else if (result == 4)
			{
				Toast.makeText(ChangeCustomerInfoActivity.this, "����ʧ�ܣ����ݽ����쳣��", Toast.LENGTH_LONG).show();
			}
		}
	}
	
	public int postData(String... params)
	{
		HttpRestAchieve httpRestAchieve = new HttpRestAchieve();
		HashMap<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("question_describe", params[0]); //
		paramsMap.put("solve_method", params[1]);
		paramsMap.put("is_repeat_order", params[2]);
				
		int back_data = 0;
		//ȡ����ע��ť������¼�				
		try
		{
			NetBackDataEntity netBackData = httpRestAchieve.postRequestData(request_url 
					+ "/trackinfo/feed.json", paramsMap);
			String data = netBackData.getData();
			Log.d(TAG, "����"+data);
			JSONObject jsonObject = new JSONObject(data);
			boolean result = jsonObject.getBoolean("succ");								
			if (result)
			{
				back_data = 0;
				Log.d(TAG, "�������ݳɹ�");
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
			//Toast.makeText(this, "����������", Toast.LENGTH_LONG).show();
			e.printStackTrace();
			Log.d(TAG, e.getMessage());
		}
		catch (NetAccessException e)
		{
			back_data = 3;
			//Toast.makeText(this, "����ʧ��,�����쳣��", Toast.LENGTH_LONG).show();
			e.printStackTrace();
			Log.d(TAG, e.getMessage());
		}
		catch (JSONException e)
		{
			back_data = 4;
			//Toast.makeText(this, "����ʧ�ܣ����ݽ����쳣��", Toast.LENGTH_LONG).show();
			e.printStackTrace();
			Log.d(TAG, e.getMessage());
		}
		return back_data;
	}
	
	public void CancelBtnClick(View target)
	{
		this.finish();
	}
}
