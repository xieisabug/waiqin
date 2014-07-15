package com.sealion.serviceassistant.superactivity;

import com.sealion.serviceassistant.R;
import com.sealion.serviceassistant.db.OrderDetailListDB;
import com.sealion.serviceassistant.db.OrderListDB;
import com.sealion.serviceassistant.widget.ViewHandler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public abstract class SuperActivity extends Activity
{
	protected ProgressDialog progressDig = null;
	protected SharedPreferences sp = null;
	protected String request_url = null;

	protected OrderListDB olDB = null;
	protected OrderDetailListDB odListDB = null;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences("USER_CARD", Context.MODE_PRIVATE);
		request_url = this.getResources().getString(R.string.request_url);
		
		odListDB = new OrderDetailListDB(this);
		olDB = new OrderListDB(this);
	}

	// 加载数据对话框
	public void ShowDialog()
	{
		progressDig = ViewHandler.creteProgressDialog(this, "加载中...");		
	}

	public void CancelDialog()
	{
		if (progressDig != null && progressDig.isShowing())
		{
			progressDig.dismiss();
		}
	}
}
