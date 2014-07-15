package com.sealion.serviceassistant.widget;

import com.sealion.serviceassistant.DesktopActivity;
import com.sealion.serviceassistant.R;
import com.sealion.serviceassistant.db.OrderDetailListDB;
import com.sealion.serviceassistant.db.OrderListDB;
import com.sealion.serviceassistant.entity.OrderStepEntity;
import com.sealion.serviceassistant.tools.DateTools;
import com.sealion.serviceassistant.tools.FinalVariables;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * ����ʽ���ڣ���������ʾ��Ϣ��
 * ʹ�÷�����
 * PopWin popWin = new PopWin(ChangeCustomerInfoActivity.this);
 * �ٵ���initPopWindow����
 */
public class PopWin
{
	private Context ctx;
	
	public PopWin(Context ctx)
	{
		this.ctx = ctx;
	}
			
	/**
	 * 
	 * @param text_tips
	 * @param page 0:��¼ҳ
	 */
	public void initPopWindow(String text_tips,int page,final String order_num,final Activity act)
	{
		// ����popupWindow�Ĳ����ļ�
		View contentView = LayoutInflater.from(ctx).inflate(R.layout.alert_dialog, null);
		contentView.getBackground().setAlpha(110);
		// ����һ��������
		final PopupWindow popupWindow = new PopupWindow(contentView.findViewById(R.id.alert_layout),
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		// Ϊ�������趨�Զ���Ĳ���
		popupWindow.setContentView(contentView);

		final TextView tips_content = (TextView) contentView.findViewById(R.id.tips_content);
		tips_content.setText(text_tips); 
		popupWindow.setFocusable(true);
		/*
		 * popupWindow.showAsDropDown��View view�������Ի���λ���ڽ�����view���9
		 * showAsDropDown(View anchor, int xoff, int yoff)�����Ի���λ���ڽ�����view�����x y
		 * ������ƫ���� showAtLocation(View parent, int gravity, int x, int y)�����Ի���
		 * parent ������ gravity ���������ֵ�λ����Gravity.CENTER x y ����ֵ
		 */
		popupWindow.showAsDropDown(contentView);
		
		Button button_sure = (Button) contentView.findViewById(R.id.dialog_calcel);	
		Button button_cancel = (Button) contentView.findViewById(R.id.dialog_makesure);
		
		if(page == 0)
		{
			button_sure.setText(ctx.getResources().getString(R.string.make_sure));
			button_cancel.setText(ctx.getResources().getString(R.string.cancel));
			
			button_sure.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					popupWindow.dismiss();
				}
			});
			
			button_cancel.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					popupWindow.dismiss();
				}
			});

		}
		else if (page == 1)
		{
			button_sure.setText(ctx.getResources().getString(R.string.back));
			button_cancel.setText(ctx.getResources().getString(R.string.continues));
			
			//����
			button_sure.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					popupWindow.dismiss();
				}
			});
			
			//����
			button_cancel.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					OrderDetailListDB odListDB = new OrderDetailListDB(ctx);
					OrderListDB olDB = new OrderListDB(ctx);
					
					//���¹���״̬Ϊ����Ŀ�ĵ�
					olDB.updateSignData(order_num,  FinalVariables.ORDER_STATE_ARRIVE_TARGET);
					
					//д�빤����ǰ״̬���������ݿ�
					OrderStepEntity osEntity = new OrderStepEntity();
					osEntity.setOper_time(DateTools.getFormatDateAndTime());
					osEntity.setOrder_num(order_num);
					osEntity.setOrder_state(FinalVariables.ORDER_STATE_ARRIVE_TARGET); //�¹�����δ�Ķ�״̬
					odListDB.insert(osEntity);
					popupWindow.dismiss();
				}
			});
		}
		else if (page == 2)
		{
			button_sure.setText(ctx.getResources().getString(R.string.back));
			button_cancel.setText(ctx.getResources().getString(R.string.continues));
			
			//����
			button_sure.setOnClickListener(new OnClickListener()
			{
				@Override  
				public void onClick(View v)
				{					
					popupWindow.dismiss();
					act.finish();
				}
			});
			
			//����
			button_cancel.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					
					popupWindow.dismiss();
				}
			});
		}
		else if (page == 3) //���ߵ�¼�Ի���
		{
			button_sure.setText(ctx.getResources().getString(R.string.back));
			button_cancel.setText(ctx.getResources().getString(R.string.offline_login));
			//����
			button_sure.setOnClickListener(new OnClickListener()
			{
				@Override  
				public void onClick(View v)
				{					
					popupWindow.dismiss();
				}
			});
			
			//���ߵ�¼
			button_cancel.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{					
					popupWindow.dismiss();
					ctx.startActivity(new Intent(act,DesktopActivity.class));
					act.finish();
				}
			});
		}
	}
}
