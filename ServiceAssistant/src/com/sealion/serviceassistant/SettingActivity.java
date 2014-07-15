package com.sealion.serviceassistant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

/**   
* 系统设置，包括提醒时间设置和录影提醒设置
*/
public class SettingActivity extends Activity
{
	private SharedPreferences sp = null;
	private ImageView topbar_dropdown_btn = null;
	private TextView top_bar_title = null;
	private ToggleButton record_open_off = null;
	private RelativeLayout time_tips_layout = null;
	private ImageView back_img_btn = null;
	private ImageView sign_in = null;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.setting);
		sp = getSharedPreferences("SETTING_DATA", Context.MODE_PRIVATE);
		time_tips_layout = (RelativeLayout)this.findViewById(R.id.time_tips_layout);
		
		sign_in = (ImageView)this.findViewById(R.id.sign_in);
		sign_in.setVisibility(View.GONE);
		
		back_img_btn = (ImageView)this.findViewById(R.id.back_img_btn);
		back_img_btn.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
		time_tips_layout.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				startActivity(new Intent(SettingActivity.this,TimeSelectPreferenceActivity.class));
			}
		});
        topbar_dropdown_btn = (ImageView)this.findViewById(R.id.topbar_dropdown_btn);
		topbar_dropdown_btn.setVisibility(View.GONE);
		
		top_bar_title = (TextView)this.findViewById(R.id.top_bar_title);
		top_bar_title.setText(this.getResources().getString(R.string.setting));
		
		record_open_off = (ToggleButton)this.findViewById(R.id.record_open_off);
		
		if(sp.getInt("RECORD", 0) == 0)
		{
			record_open_off.setChecked(false);
		}
		else
		{
			record_open_off.setChecked(true);
		}
		
		record_open_off.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Editor edit = sp.edit();
								
				if (record_open_off.isChecked()) //开
				{
					edit.putInt("RECORD", 1);
					record_open_off.setChecked(true);
					record_open_off.setBackgroundResource(R.drawable.setting_open);
				}
				else
				{
					edit.putInt("RECORD", 0);	//关闭
					record_open_off.setChecked(false);
					record_open_off.setBackgroundResource(R.drawable.setting_close);
				}
				
				edit.commit();
			}
		});
	}
}
