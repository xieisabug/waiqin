/**   
* @Title: QTypeAdapter.java 
* @Package com.sealion.serviceassistant.adapter 
* @Description: TODO(用一句话描述该文件做什么) 
* @author jack.lee titans.lee@gmail.com   
* @date 2013-3-5 下午4:52:41 
* @version V1.0
* Copyright: Copyright (c)2012
* Company: 湖南中恩通信技术有限公司
*/
package com.sealion.serviceassistant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.sealion.serviceassistant.R;
import com.sealion.serviceassistant.entity.ProductEntity;

import java.util.List;

/**   
 * 产品信息适配器
 */
public class ProductAdapter extends BaseAdapter
{
	private List<ProductEntity> pList = null;
	private Context mContext = null;

	public ProductAdapter(Context context, List<ProductEntity> pList)
	{
		this.mContext = context;
		this.pList = pList;
	}
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount()
	{
		return pList.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position)
	{
		return pList.get(position);
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
            _TextView1.setText(pList.get(position).getProductName());
            _TextView1.setTag(pList.get(position).getProductId());
        }  
		return convertView;
	}

}
