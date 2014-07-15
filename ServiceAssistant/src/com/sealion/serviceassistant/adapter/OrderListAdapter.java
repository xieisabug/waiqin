package com.sealion.serviceassistant.adapter;

import java.util.ArrayList;

import com.sealion.serviceassistant.R;
import com.sealion.serviceassistant.entity.OrderEntity;
import com.sealion.serviceassistant.tools.FinalVariables;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * <p> * Title: OrderListAdapter.java </p> 
 * <p> * Description: * </p>
 * <p> * Copyright: hn Copyright (c) 2012 * </p>
 * <p> * Company: hn * </p>
 * 
 * @author jack.lee E-mail: titans.lee@gmail.com
 * @version 创建时间：2012-10-22 下午05:40:16 类说明
 */
public class OrderListAdapter extends BaseAdapter
{
	private ArrayList<OrderEntity> list = null;
	private LayoutInflater mInflater;

	public OrderListAdapter(Context ctx, ArrayList<OrderEntity> list)
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
	public Object getItem(int arg0)
	{
		return list.get(arg0);
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
		OrderEntity oEntity = list.get(position);
		ViewHolder holder = null;
		if (convertView == null)
		{

			holder = new ViewHolder();

			convertView = mInflater.inflate(R.layout.orderlist_item, null);
			holder.img_type = (ImageView) convertView.findViewById(R.id.img_type);
			holder.list_item_title = (TextView) convertView.findViewById(R.id.list_item_title);
			holder.list_item_create_time = (TextView) convertView.findViewById(R.id.list_item_create_time);
			holder.omit_content = (TextView) convertView.findViewById(R.id.omit_content);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		int order_type = oEntity.getWorkOrderType();//1:直接派工，2：回访派工
		if (order_type == 1) // 直接派工
		{
			holder.img_type.setBackgroundResource(R.drawable.icon_maintain);
		}
		else if (order_type == 2) // 回访派工
		{
			holder.img_type.setBackgroundResource(R.drawable.back_view_icon);
		}
		
		holder.list_item_title.setText(oEntity.getCustomerName());
		holder.list_item_create_time.setText(oEntity.getReceiveOrderTime()+"/"+oEntity.getWorkCardId());
		holder.omit_content.setText("单据号 " + oEntity.getOrderToken());
		return convertView;
	}

	public final class ViewHolder
	{
		public ImageView img_type;
		public TextView list_item_title;
		public TextView list_item_create_time;
		public TextView omit_content;
	}
}
