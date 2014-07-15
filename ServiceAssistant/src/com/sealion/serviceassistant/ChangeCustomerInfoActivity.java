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
* 修改客户的基本信息.
*/
public class ChangeCustomerInfoActivity extends OrderActivity
{
	private static final String TAG = ChangeCustomerInfoActivity.class.getSimpleName();
	
	private RadioButton order_customer_address = null; //工单中客户地址 button
	private RadioButton gps_customer_address = null; //通过gps定位的地址button
	private RadioButton input_customer_address = null; //客户输入地址
	
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
			
			//doInBackground返回时触发，换句话说，就是doInBackground执行完后触发
			//这里的result就是上面doInBackground执行后的返回值，所以这里是"执行完毕"
			CancelDialog();
			
			if (result == 0)
			{
				Toast.makeText(ChangeCustomerInfoActivity.this, "操作成功！", Toast.LENGTH_LONG).show();
			}
			else if (result == 1)
			{
				Toast.makeText(ChangeCustomerInfoActivity.this, "操作失败，请重试！", Toast.LENGTH_LONG).show();
			}
			else if (result == 2)
			{
				Toast.makeText(ChangeCustomerInfoActivity.this, "服务器错误！", Toast.LENGTH_LONG).show();
			}
			else if (result == 3)
			{
				Toast.makeText(ChangeCustomerInfoActivity.this, "操作失败,网络异常！", Toast.LENGTH_LONG).show();
			}
			else if (result == 4)
			{
				Toast.makeText(ChangeCustomerInfoActivity.this, "操作失败，数据解析异常！", Toast.LENGTH_LONG).show();
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
		//取消关注按钮被点击事件				
		try
		{
			NetBackDataEntity netBackData = httpRestAchieve.postRequestData(request_url 
					+ "/trackinfo/feed.json", paramsMap);
			String data = netBackData.getData();
			Log.d(TAG, "返回"+data);
			JSONObject jsonObject = new JSONObject(data);
			boolean result = jsonObject.getBoolean("succ");								
			if (result)
			{
				back_data = 0;
				Log.d(TAG, "发送数据成功");
			}
			else
			{
				//记录数据到本地数据库，等待有网络环境则进行同步
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
	
	public void CancelBtnClick(View target)
	{
		this.finish();
	}
}
