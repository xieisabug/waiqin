package com.sealion.serviceassistant;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.sealion.serviceassistant.entity.NetBackDataEntity;
import com.sealion.serviceassistant.exception.NetAccessException;
import com.sealion.serviceassistant.net.HttpRestAchieve;
import com.sealion.serviceassistant.superactivity.ServiceAssistantActivity;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources.NotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * 完成用户登录密码修改的功能，入口在DesktopActivity
 * 通过原密码，设置新密码
 */
public class ChangePwdActivity extends ServiceAssistantActivity
{
	private static final String TAG = ChangePwdActivity.class.getSimpleName();
	
	private EditText old_pwd = null;
	private EditText new_pwd = null;
	private EditText repeat_new_pwd = null;
	
	private ImageView sign_in = null;
	
	public void  onCreate(Bundle savedInstanceState)
	{
		this.setContentView(R.layout.changepwd);
		super.onCreate(savedInstanceState);
		top_bar_title.setText("重置密码");
		topbar_dropdown_btn.setVisibility(View.GONE);
		sign_in = (ImageView)this.findViewById(R.id.sign_in);
		sign_in.setVisibility(View.GONE);
		
		ImageView top_bar_logo = (ImageView)this.findViewById(R.id.top_bar_logo);
		top_bar_logo.setVisibility(View.GONE);
	}
	
	public void ChangeBtnClick(View target)
	{
		old_pwd = (EditText)this.findViewById(R.id.old_pwd);
		new_pwd = (EditText)this.findViewById(R.id.new_pwd);
		repeat_new_pwd = (EditText)this.findViewById(R.id.repeat_new_pwd);
		
		String old_pwd_value = old_pwd.getText().toString();
		String new_pwd_value = new_pwd.getText().toString();
		String repeat_new_pwd_value = repeat_new_pwd.getText().toString();
		
		if ( old_pwd_value == null || old_pwd_value.equals(""))
		{
			Toast.makeText(this, "旧密码不能为空！", Toast.LENGTH_LONG).show();
			return;
		}
		
		if ( old_pwd_value.length() < 6 || old_pwd_value.length() > 20)
		{
			Toast.makeText(this, "密码不能小于6位或者大于20位！", Toast.LENGTH_LONG).show();
			return;
		}
		
		if (new_pwd_value == null || new_pwd_value.equals(""))
		{
			Toast.makeText(this, "新密码不能为空！", Toast.LENGTH_LONG).show();
			return;
		}
		
		if (repeat_new_pwd_value == null || repeat_new_pwd_value.equals(""))
		{
			Toast.makeText(this, "新密码不能为空！", Toast.LENGTH_LONG).show();
			return;
		}
		
		if ( new_pwd_value.length() < 6 || new_pwd_value.length() > 20)
		{
			Toast.makeText(this, "密码不能小于6位或者大于20位！", Toast.LENGTH_LONG).show();
			return;
		}
		
		if ( repeat_new_pwd_value.length() < 6 || repeat_new_pwd_value.length() > 20)
		{
			Toast.makeText(this, "密码不能小于6位或者大于20位！", Toast.LENGTH_LONG).show();
			return;
		}
				
		if (!new_pwd_value.equals(repeat_new_pwd_value))
		{
			Toast.makeText(this, "两次输入的密码不相等！", Toast.LENGTH_LONG).show();
			return;
		}
		
	    new PostData().execute(sp.getString("USERNAME", ""),old_pwd_value,new_pwd_value);
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
			int result = postData(params[0],params[1],params[2]);
			return result;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			
			//doInBackground返回时触发，换句话说，就是doInBackground执行完后触发
			//这里的result就是上面doInBackground执行后的返回值，所以这里是"执行完毕"
			CancelDialog();
			
			if (result == 0)
			{
				Toast.makeText(ChangePwdActivity.this, "修改成功！", Toast.LENGTH_LONG).show();
				finish();
			}
			else if (result == 1)
			{
				Toast.makeText(ChangePwdActivity.this, "操作失败，请重试！", Toast.LENGTH_LONG).show();
			}
			else if (result == 2)
			{
				Toast.makeText(ChangePwdActivity.this, "服务器错误！", Toast.LENGTH_LONG).show();
			}
			else if (result == 3)
			{
				Toast.makeText(ChangePwdActivity.this, "操作失败,网络异常！", Toast.LENGTH_LONG).show();
			}
			else if (result == 4)
			{
				Toast.makeText(ChangePwdActivity.this, "操作失败，数据解析异常！", Toast.LENGTH_LONG).show();
			}
		}
	}
	
	public int postData(String... params)
	{
		int back_data = 0;
		HttpRestAchieve httpRestAchieve = new HttpRestAchieve();
		HashMap<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("fieldWorkerNo", params[0]);
		paramsMap.put("oldPassword", params[1]);
		paramsMap.put("newPassword", params[2]);
		try
		{
			
			NetBackDataEntity netBackData = httpRestAchieve.postRequestData(request_url + "/fieldworker/change-password", paramsMap);
			String data = netBackData.getData();
			//Log.d(TAG, "==:" + data);
			JSONObject jsonObject = new JSONObject(data);
			int result = jsonObject.getInt("resultCode");								
			if (result == 1)
			{
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
