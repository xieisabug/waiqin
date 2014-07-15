package com.sealion.serviceassistant;

import com.sealion.serviceassistant.db.NoticeDB;
import com.sealion.serviceassistant.entity.NoticeEntity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 通知详细信息显示界面，根据用户在通知列表中点击的通知ID，从数据库中查询该通知的详细信息进行显示.
 */
public class NoticeDetailActivity extends Activity
{
	private NoticeDB noticeDB = null;
	private TextView notice_title = null;
	private TextView notice_time = null;
	private TextView notice_content = null;
	private ImageView topbar_dropdown_btn = null;
	private ImageView top_bar_logo = null;
	private ImageView back_img_btn = null;
	private TextView top_bar_title = null;
	private ImageView sign_in = null;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.notice_detail);

		final int notice_id = this.getIntent().getExtras().getInt("notice_id");

		back_img_btn = (ImageView) this.findViewById(R.id.back_img_btn);

		top_bar_title = (TextView) this.findViewById(R.id.top_bar_title);
		top_bar_title.setText("通知详情");

		top_bar_logo = (ImageView) this.findViewById(R.id.top_bar_logo);
		top_bar_logo.setVisibility(View.GONE);
		back_img_btn.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
//				Intent in = new Intent();
//				in.putExtra("id", notice_id);
//				setResult(RESULT_OK, in);
				finish();
			}
		});

		topbar_dropdown_btn = (ImageView) this.findViewById(R.id.topbar_dropdown_btn);
		topbar_dropdown_btn.setVisibility(View.GONE);

		sign_in = (ImageView) this.findViewById(R.id.sign_in);
		sign_in.setVisibility(View.GONE);

		notice_title = (TextView) this.findViewById(R.id.notice_title);
		notice_time = (TextView) this.findViewById(R.id.notice_time);
		notice_content = (TextView) this.findViewById(R.id.notice_content);

		noticeDB = new NoticeDB(this);

		NoticeEntity nEntity = noticeDB.selectById(notice_id);

		notice_title.setText(nEntity.getTitle());
		notice_time.setText(nEntity.getPublish_time());
		notice_content.setText(nEntity.getContent());
	}
	
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event)
//	{
//
//		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
//		{
//			
//			return true;
//		}
//		return super.onKeyDown(keyCode, event);
//
//	}
}
