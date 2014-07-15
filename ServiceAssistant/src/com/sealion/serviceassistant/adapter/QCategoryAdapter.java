/**   
* @Title: QCategoryAdapter.java 
* @Package com.sealion.serviceassistant.adapter 
* @Description: TODO(用一句话描述该文件做什么) 
* @author jack.lee titans.lee@gmail.com   
* @date 2013-3-5 下午4:53:10 
* @version V1.0
* Copyright: Copyright (c)2012
* Company: 湖南中恩通信技术有限公司
*/
package com.sealion.serviceassistant.adapter;

import java.util.List;

import com.sealion.serviceassistant.R;
import com.sealion.serviceassistant.entity.Q_Category;
import com.sealion.serviceassistant.entity.Q_Type;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**   
 * @Title: QCategoryAdapter.java 
 * @Package com.sealion.serviceassistant.adapter 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author jack.lee titans.lee@gmail.com   
 * @date 2013-3-5 下午4:53:10 
 * @version V1.0
 * Copyright: Copyright (c)2012
 * Company: 湖南中恩通信技术有限公司
 */
public class QCategoryAdapter extends BaseAdapter
{
	private List<Q_Category> mList = null;
	private Context mContext = null;
	
	public QCategoryAdapter(Context context, List<Q_Category> pList)
	{
		this.mContext = context;
		this.mList = pList;
	}
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount()
	{
		return mList.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position)
	{
		return mList.get(position);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position)
	{
		return position;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		LayoutInflater _LayoutInflater=LayoutInflater.from(mContext);  
        convertView=_LayoutInflater.inflate(R.layout.spinner_template, null);  
        if(convertView!=null)  
        {  
            TextView _TextView1=(TextView)convertView.findViewById(R.id.name);
            _TextView1.setText(mList.get(position).getName()); 
            _TextView1.setTag(mList.get(position));
        }  
		return convertView;
	}

}
