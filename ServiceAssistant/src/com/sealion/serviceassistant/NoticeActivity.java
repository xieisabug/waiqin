package com.sealion.serviceassistant;

import java.util.ArrayList;

import com.sealion.serviceassistant.adapter.NoticeListAdapter;
import com.sealion.serviceassistant.db.NoticeDB;
import com.sealion.serviceassistant.entity.NoticeEntity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * 界面加载时从数据库读取最新10条数据 ，如果多于10条数据，用户可以点击加载更多的按钮加载数据
 */
public class NoticeActivity extends Activity
{
	private ArrayList<NoticeEntity> list = null;
	private ListView noticelist = null;
	private NoticeDB noticeDB = null;
	private int page_num = 0;

	private NoticeListAdapter noticeAdapter = null;
	private static final int PAGE_SIZE = 10;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.noticelist);

		noticeDB = new NoticeDB(this);		
	}
	
	@Override
	public void onStart()
	{
		Log.d("", "on start ...................");
		super.onStart();
		list = noticeDB.getDataByPage(page_num);

		noticelist = (ListView) this.findViewById(R.id.noticelist);
		noticeAdapter = new NoticeListAdapter(this, list);
		noticelist.setAdapter(noticeAdapter);
		noticelist.setOnScrollListener(listener);

		noticelist.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3)
			{
				TextView notice_title = (TextView) view.findViewById(R.id.notice_title);
				int id = Integer.parseInt(notice_title.getTag().toString());

				noticeDB.updateSignData(id); // 更新已阅读标记

				Intent intent = new Intent(NoticeActivity.this, NoticeDetailActivity.class);
				intent.putExtra("notice_id", id);
				startActivity(intent);
			}
		});
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		Log.d("", "on resume....................");
		list = noticeDB.getDataByPage(page_num);
		noticeAdapter = new NoticeListAdapter(this, list);
		noticelist.setAdapter(noticeAdapter);
		noticeAdapter.notifyDataSetChanged();
	}
	
	private AbsListView.OnScrollListener listener = new AbsListView.OnScrollListener()
	{

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState)
		{
			if (view.getLastVisiblePosition() == view.getCount() - 1)
			{
				loadMoreData();
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
		{

		}
	};
//
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data)
//	{
//		super.onActivityResult(requestCode, resultCode, data);
//	}

	public void SearchBtnClick(View target)
	{
		EditText notice_search_value = (EditText) this.findViewById(R.id.notice_search_value);
		String search_value = notice_search_value.getText().toString();
		if (search_value == null || search_value.equals(""))
		{
			Toast.makeText(this, "请输入搜索条件", Toast.LENGTH_LONG).show();
		}
		else
		{
			list = noticeDB.getDataByCondition(search_value);
			if (list.size() == 0)
			{
				Toast.makeText(this, "没有符合条件的数据", Toast.LENGTH_LONG).show();
			}			
			noticeAdapter.notifyDataSetChanged();
			noticeAdapter = new NoticeListAdapter(this, list);
			noticelist.setAdapter(noticeAdapter);
			notice_search_value.setText("");
		}
	}

	public void loadMoreData()
	{
		page_num++;
		Toast.makeText(this, "当前加载第" + page_num + "页数据", Toast.LENGTH_SHORT).show();
		new Handler().postDelayed(new Runnable()
		{
			public void run()
			{
				ArrayList<NoticeEntity> loadNoticeList = noticeDB.getDataByPage(page_num);
				list.addAll(loadNoticeList);
				noticeAdapter.notifyDataSetChanged();
				noticeAdapter = new NoticeListAdapter(NoticeActivity.this, list);
				noticelist.setAdapter(noticeAdapter);
				if (loadNoticeList.size() < PAGE_SIZE)
				{
					page_num = 0;
					Toast.makeText(NoticeActivity.this, "数据全部加载完!", Toast.LENGTH_LONG).show();
				}
			}
		}, 1000);
	}
}
