package com.sealion.serviceassistant.adapter;

import java.util.ArrayList;

import com.sealion.serviceassistant.R;
import com.sealion.serviceassistant.entity.MediaAttributeEntity;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class MediaGridAdapter extends BaseAdapter
{
	private class GridHolder{
		ImageView imageView;
	}
	
	private Context context;
	private ArrayList<MediaAttributeEntity> list;
	private LayoutInflater mInflater;
	
	public MediaGridAdapter(Context c,ArrayList<MediaAttributeEntity> list)
	{
		super();
		this.context = c;
		this.list = list;
		 mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		GridHolder holder;  
        if (convertView == null) 
        {     
            convertView = mInflater.inflate(R.layout.media_list_item, null);     
            holder = new GridHolder();  
            holder.imageView = (ImageView)convertView.findViewById(R.id.media_item_img);
            convertView.setTag(holder);     
  
        }
        else
        {  
             holder = (GridHolder) convertView.getTag();     
  
        }
        MediaAttributeEntity maEntity = list.get(position);
        Bitmap info = maEntity.getBitmap();  
        if (info != null) 
        {     
            holder.imageView.setImageBitmap(info);  
            holder.imageView.setTag(maEntity);
        }  
        return convertView;  
	}

}
