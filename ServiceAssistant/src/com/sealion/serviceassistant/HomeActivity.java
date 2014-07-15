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
 * ��ҳ���ṩ������ͳ�ƻ�����Ϣ������֪ͨ����
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
				noticeDB.updateSignData(id); //�������Ķ����
				
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
		int count0 = olDB.CountOrdersByCondition(0); // ͳ��δ��ɵĽ���������
		int count1 = olDB.CountOrdersByCondition(1); // ͳ�����е��¹�����
		int count2 = olDB.CountOrdersByCondition(2); // ͳ�����е�δ��ɹ�����
		int count3 = olDB.CountOrdersByCondition(3); // ͳ��������ɹ�����
		int count4 = olDB.CountOrdersByCondition(4); // ͳ�����лطù���
		int count5 = olDB.CountOrdersByCondition(5); // ͳ������ά������
		urgency_text.setText("���� " + count0 + " ��δ��ɽ�������");
		new_order_text.setText("���� " + count1 + " ����ͨ�¹���");
		underway_order_text.setText("���� " + count2 + " ��δ��ɵĹ���");
		complete_order_text.setText("���Ѿ���� " + count3 + " ������");
		back_order_text.setText("���Ѿ���" + count4 + "���طù���");
		maintain_order_text.setText("���Ѿ���" + count5 + "��ά������");
		
		ArrayList<NoticeEntity> noticeList = nDB.getTopOneData();

		int size = noticeList.size();
		if (size > 0)
		{
			notice_layout.setVisibility(View.VISIBLE);
			ListNoticeLayout(noticeList.get(0));
		}
	}

	// ��������
	public void UrgencyOrderLayoutClick(View target)
	{
		Intent it = new Intent(ORDER_TYPE_SELECTED);
		it.putExtra("type", FinalVariables.ORDER_TYPE_URGENCY);
		sendBroadcast(it);
	}

	// ��ͨ����
	public void NormalOrderLayoutClick(View target)
	{
		Intent it = new Intent(ORDER_TYPE_SELECTED);
		it.putExtra("type", FinalVariables.ORDER_TYPE_NEW);
		sendBroadcast(it);
	}

	// δ��ɹ���
	public void NoComplishLayoutClick(View target)
	{
		Intent it = new Intent(ORDER_TYPE_SELECTED);
		it.putExtra("type", FinalVariables.ORDER_TYPE_NOT_COMPLISH);
		sendBroadcast(it);
	}

	// ����ɹ���
	public void ComplishLayoutClick(View target)
	{
		Intent it = new Intent(ORDER_TYPE_SELECTED);
		it.putExtra("type", FinalVariables.ORDER_TYPE_COMPLISH);
		sendBroadcast(it);
	}

	// �طù���
	public void BackLayoutClick(View target)
	{
		Intent it = new Intent(ORDER_TYPE_SELECTED);
		it.putExtra("type", FinalVariables.ORDER_TYPE_BACK);
		sendBroadcast(it);
	}

	//ά������
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

		if (nEntity.getSign() == 1) // �Ѿ��Ķ�
		{
			notice_icon_01.setBackgroundResource(R.drawable.notice_01);
		}
		else if (nEntity.getSign() == 0) // δ�Ķ�
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
