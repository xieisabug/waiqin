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
 * 弹出式窗口，可用于提示信息。
 * 使用方法：
 * PopWin popWin = new PopWin(ChangeCustomerInfoActivity.this);
 * 再调用initPopWindow方法
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
	 * @param page 0:登录页
	 */
	public void initPopWindow(String text_tips,int page,final String order_num,final Activity act)
	{
		// 加载popupWindow的布局文件
		View contentView = LayoutInflater.from(ctx).inflate(R.layout.alert_dialog, null);
		contentView.getBackground().setAlpha(110);
		// 声明一个弹出框
		final PopupWindow popupWindow = new PopupWindow(contentView.findViewById(R.id.alert_layout),
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		// 为弹出框设定自定义的布局
		popupWindow.setContentView(contentView);

		final TextView tips_content = (TextView) contentView.findViewById(R.id.tips_content);
		tips_content.setText(text_tips); 
		popupWindow.setFocusable(true);
		/*
		 * popupWindow.showAsDropDown（View view）弹出对话框，位置在紧挨着view组件9
		 * showAsDropDown(View anchor, int xoff, int yoff)弹出对话框，位置在紧挨着view组件，x y
		 * 代表着偏移量 showAtLocation(View parent, int gravity, int x, int y)弹出对话框
		 * parent 父布局 gravity 依靠父布局的位置如Gravity.CENTER x y 坐标值
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
			
			//返回
			button_sure.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					popupWindow.dismiss();
				}
			});
			
			//继续
			button_cancel.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					OrderDetailListDB odListDB = new OrderDetailListDB(ctx);
					OrderListDB olDB = new OrderListDB(ctx);
					
					//更新工单状态为到达目的地
					olDB.updateSignData(order_num,  FinalVariables.ORDER_STATE_ARRIVE_TARGET);
					
					//写入工单当前状态到本地数据库
					OrderStepEntity osEntity = new OrderStepEntity();
					osEntity.setOper_time(DateTools.getFormatDateAndTime());
					osEntity.setOrder_num(order_num);
					osEntity.setOrder_state(FinalVariables.ORDER_STATE_ARRIVE_TARGET); //新工单，未阅读状态
					odListDB.insert(osEntity);
					popupWindow.dismiss();
				}
			});
		}
		else if (page == 2)
		{
			button_sure.setText(ctx.getResources().getString(R.string.back));
			button_cancel.setText(ctx.getResources().getString(R.string.continues));
			
			//返回
			button_sure.setOnClickListener(new OnClickListener()
			{
				@Override  
				public void onClick(View v)
				{					
					popupWindow.dismiss();
					act.finish();
				}
			});
			
			//继续
			button_cancel.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					
					popupWindow.dismiss();
				}
			});
		}
		else if (page == 3) //离线登录对话框
		{
			button_sure.setText(ctx.getResources().getString(R.string.back));
			button_cancel.setText(ctx.getResources().getString(R.string.offline_login));
			//返回
			button_sure.setOnClickListener(new OnClickListener()
			{
				@Override  
				public void onClick(View v)
				{					
					popupWindow.dismiss();
				}
			});
			
			//离线登录
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
