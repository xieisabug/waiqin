package com.sealion.serviceassistant.widget;

import com.sealion.serviceassistant.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * ����ʹ��һЩview����
 */
public class ViewHandler
{
    /**
     * ����һ��loading�Ի���
     * @param context ������
     * @param text ��ʾ��Ϣ
     * @return loading�Ի���
     */
	public static ProgressDialog creteProgressDialog(Context context, String text)
	{
		final ProgressDialog dlg = new ProgressDialog(context);
		dlg.show();
		dlg.setContentView(R.layout.loading);
		LinearLayout root = (LinearLayout) dlg.findViewById(R.id.progressDialog);
		root.setGravity(android.view.Gravity.CENTER);
		LoadingView mLoadView = new LoadingView(context);
		mLoadView.setDrawableResId(R.drawable.icon_30by30_whitespin);
		root.addView(mLoadView);
		TextView alert = new TextView(context);
		alert.setText(text);
		root.addView(alert);
		return dlg;
	}
}
