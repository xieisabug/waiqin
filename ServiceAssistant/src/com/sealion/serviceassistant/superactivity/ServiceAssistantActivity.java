package com.sealion.serviceassistant.superactivity;

import com.sealion.serviceassistant.R;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class ServiceAssistantActivity extends SuperActivity
{
	protected ImageView back_img_btn = null;
	protected TextView top_bar_title = null;
	protected ImageView topbar_dropdown_btn = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);	
		
		back_img_btn = (ImageView)this.findViewById(R.id.back_img_btn);
		back_img_btn.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
		
		top_bar_title = (TextView)this.findViewById(R.id.top_bar_title);
		topbar_dropdown_btn = (ImageView)this.findViewById(R.id.topbar_dropdown_btn);
	}
	
	public void initializeTopBar()
	{

	}
	
	public void BackBtnClick(View target)
	{
		finish();
	}
	
}
