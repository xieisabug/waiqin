package com.sealion.serviceassistant.adapter;

import java.util.ArrayList;

import com.sealion.serviceassistant.R;
import com.sealion.serviceassistant.entity.NoticeEntity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * <p> * Title: NotieListAdapter.java * </p>
 * <p> * Description: * </p>
 * <p> * Copyright: hn Copyright (c) 2012 * </p>
 * <p> * Company: hn </p> * 
 * @author jack.lee E-mail: titans.lee@gmail.com
 * @version 创建时间：2012-10-23 上午10:38:34 类说明
 */
public class NoticeListAdapter extends BaseAdapter
{
	private ArrayList<NoticeEntity> list;
	private LayoutInflater mInflater;

	public NoticeListAdapter(Context ctx, ArrayList<NoticeEntity> list)
	{
		this.list = list;
		this.mInflater = LayoutInflater.from(ctx);
	}

	@Override
	public int getCount()
	{
		return list.size();
	}

	@Override
	public Object getItem(int position)
	{
		return list.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

//	@Override
//	public boolean isEnabled(int position)
//	{
//		return false;
//	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		NoticeEntity nEntity = list.get(position);

		ViewHolder holder = null;
		if (convertView == null)
		{
			holder = new ViewHolder();

			convertView = mInflater.inflate(R.layout.noticelist_item, null);
			holder.notice_icon = (ImageView) convertView.findViewById(R.id.notice_icon);
			holder.notice_title = (TextView) convertView.findViewById(R.id.notice_title);
			holder.notice_time = (TextView) convertView.findViewById(R.id.notice_time);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}

		if (nEntity.getSign() == 0) // 未阅读
		{
			holder.notice_icon.setBackgroundResource(R.drawable.notice);
		}
		else
		// 已阅读
		{
			holder.notice_icon.setBackgroundResource(R.drawable.notice_01);
		}
		holder.notice_title.setText(nEntity.getTitle());
		holder.notice_title.setTag(nEntity.getId());
		holder.notice_time.setText(nEntity.getPublish_time());
		return convertView;
	}

	public final class ViewHolder
	{
		public ImageView notice_icon;
		public TextView notice_title;
		public TextView notice_time;
	}
}
