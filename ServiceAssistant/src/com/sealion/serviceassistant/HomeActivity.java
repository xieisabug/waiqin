package com.sealion.serviceassistant;

import java.util.ArrayList;

import com.sealion.serviceassistant.db.NoticeDB;
import com.sealion.serviceassistant.db.OrderListDB;
import com.sealion.serviceassistant.entity.NoticeEntity;
import com.sealion.serviceassistant.tools.FinalVariables;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 首页，提供工单的统计基本信息和最新通知公告
 */
public class HomeActivity extends Activity
{
	private static final String TAG = HomeActivity.class.getSimpleName();

	public static final String ORDER_TYPE_SELECTED = "com.sealion.serviceassistant.ORDER_TYPE_SELECTED";
	public static final String NOTICE_CLICK = "com.sealion.serviceassistant.NOTICE_CLICK";
	
	private TextView urgency_text = null;
	private TextView new_order_text = null;
	private TextView underway_order_text = null;
	private TextView complete_order_text = null;
	private TextView back_order_text = null;
	private TextView maintain_order_text = null;
	
	private ImageView notice_icon_01 = null;
	private TextView notice_title_01 = null;
	private TextView notice_time_01 = null;

	private RelativeLayout notice_layout = null;

	private OrderListDB olDB = null;
	private NoticeDB nDB = null;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.home);

		urgency_text = (TextView) this.findViewById(R.id.urgency_text);
		new_order_text = (TextView) this.findViewById(R.id.new_order_text);
		underway_order_text = (TextView) this.findViewById(R.id.underway_order_text);
		complete_order_text = (TextView) this.findViewById(R.id.complete_order_text);
		back_order_text = (TextView) this.findViewById(R.id.back_order_text);
		maintain_order_text = (TextView)this.findViewById(R.id.maintain_order_text);
		
		notice_icon_01 = (ImageView) this.findViewById(R.id.notice_icon_01);
		notice_title_01 = (TextView) this.findViewById(R.id.notice_title_01);
		notice_time_01 = (TextView) this.findViewById(R.id.notice_time_01);

		olDB = new OrderListDB(this);
		nDB = new NoticeDB(this);

		notice_layout = (RelativeLayout) this.findViewById(R.id.notice_layout);
		notice_layout.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				int id = Integer.parseInt(notice_title_01.getTag().toString());
				NoticeDB noticeDB = new NoticeDB(HomeActivity.this);
				noticeDB.updateSignData(id); //更新已阅读标记
				
				Intent intent = new Intent(HomeActivity.this,NoticeDetailActivity.class);
				intent.putExtra("notice_id", id);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onStart()
	{
		super.onStart();
	}

	@Override
	public void onResume()
	{
		super.onResume();
		int count0 = olDB.CountOrdersByCondition(0); // 统计未完成的紧急工单数
		int count1 = olDB.CountOrdersByCondition(1); // 统计所有的新工单数
		int count2 = olDB.CountOrdersByCondition(2); // 统计所有的未完成工单数
		int count3 = olDB.CountOrdersByCondition(3); // 统计所有完成工单数
		int count4 = olDB.CountOrdersByCondition(4); // 统计所有回访工单
		int count5 = olDB.CountOrdersByCondition(5); // 统计所有维护工单
		urgency_text.setText("您有 " + count0 + " 条未完成紧急工单");
		new_order_text.setText("您有 " + count1 + " 条普通新工单");
		underway_order_text.setText("您有 " + count2 + " 条未完成的工单");
		complete_order_text.setText("您已经完成 " + count3 + " 条工单");
		back_order_text.setText("您已经有" + count4 + "条回访工单");
		maintain_order_text.setText("您已经有" + count5 + "条维护工单");
		
		ArrayList<NoticeEntity> noticeList = nDB.getTopOneData();

		int size = noticeList.size();
		if (size > 0)
		{
			notice_layout.setVisibility(View.VISIBLE);
			ListNoticeLayout(noticeList.get(0));
		}
	}

	// 紧急工单
	public void UrgencyOrderLayoutClick(View target)
	{
		Intent it = new Intent(ORDER_TYPE_SELECTED);
		it.putExtra("type", FinalVariables.ORDER_TYPE_URGENCY);
		sendBroadcast(it);
	}

	// 普通工单
	public void NormalOrderLayoutClick(View target)
	{
		Intent it = new Intent(ORDER_TYPE_SELECTED);
		it.putExtra("type", FinalVariables.ORDER_TYPE_NEW);
		sendBroadcast(it);
	}

	// 未完成工单
	public void NoComplishLayoutClick(View target)
	{
		Intent it = new Intent(ORDER_TYPE_SELECTED);
		it.putExtra("type", FinalVariables.ORDER_TYPE_NOT_COMPLISH);
		sendBroadcast(it);
	}

	// 已完成工单
	public void ComplishLayoutClick(View target)
	{
		Intent it = new Intent(ORDER_TYPE_SELECTED);
		it.putExtra("type", FinalVariables.ORDER_TYPE_COMPLISH);
		sendBroadcast(it);
	}

	// 回访工单
	public void BackLayoutClick(View target)
	{
		Intent it = new Intent(ORDER_TYPE_SELECTED);
		it.putExtra("type", FinalVariables.ORDER_TYPE_BACK);
		sendBroadcast(it);
	}

	//维护工单
	public void MaintainLayoutClick(View target)
	{
		Intent it = new Intent(ORDER_TYPE_SELECTED);
		it.putExtra("type", FinalVariables.ORDER_TYPE_MAINTAIN);
		sendBroadcast(it);
	}
	
	public void LookMoreBtnClick(View target)
	{
		this.startActivity(new Intent(this, NoticeActivity.class));
	}

	public void ListNoticeLayout(NoticeEntity nEntity)
	{

		if (nEntity.getSign() == 1) // 已经阅读
		{
			notice_icon_01.setBackgroundResource(R.drawable.notice_01);
		}
		else if (nEntity.getSign() == 0) // 未阅读
		{
			notice_icon_01.setBackgroundResource(R.drawable.notice);
		}

		notice_title_01.setText(nEntity.getTitle());
		notice_title_01.setTag(nEntity.getId());
		notice_time_01.setText(nEntity.getPublish_time());

	}
	
	public void MoreNoticeBtnClick(View target)
	{
		Intent it = new Intent(NOTICE_CLICK);
		sendBroadcast(it);
	}
}
