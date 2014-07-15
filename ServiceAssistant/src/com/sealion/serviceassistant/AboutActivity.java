package com.sealion.serviceassistant;

import com.sealion.serviceassistant.superactivity.ServiceAssistantActivity;
import com.sealion.serviceassistant.tools.Config;

import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/** 
 * 该APP基本信息简要介绍activity
 * 有账号信息，用户信息，手机MEID，版本信息等
 */
public class AboutActivity extends ServiceAssistantActivity
{
	private TextView about_user_info_value = null; //用户信息
	private TextView user_info_value = null; //帐号信息
	private TextView meid_value = null; //meid设备号
	private TextView version_info_value = null; //版本号
	private ImageView sign_in = null;
	
	public void onCreate(Bundle savedInstanceState)
	{
		this.setContentView(R.layout.about);
		super.onCreate(savedInstanceState);		
		
		topbar_dropdown_btn.setVisibility(View.GONE);
		sign_in = (ImageView)this.findViewById(R.id.sign_in);
		sign_in.setVisibility(View.GONE);
		
		about_user_info_value = (TextView)this.findViewById(R.id.about_user_info_value);
		user_info_value = (TextView)this.findViewById(R.id.user_info_value);
		meid_value = (TextView)this.findViewById(R.id.meid_value);
		version_info_value = (TextView)this.findViewById(R.id.version_info_value);
		
		about_user_info_value.setText(sp.getString("USERNAME", ""));
		user_info_value.setText(sp.getString("FULLNAME", ""));
		TelephonyManager tele = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
		String deviceId = tele.getDeviceId();
		meid_value.setText(deviceId);
		version_info_value.setText("版本号:"+Config.getVerCode(this) + "版本名称:"+Config.getVerName(this));
	}
}
