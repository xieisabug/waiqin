package com.sealion.serviceassistant.db;

import com.sealion.serviceassistant.entity.OrderChangeEntity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @Title: OrderChangeDB.java
 * @Package com.sealion.serviceassistant.db
 * @Description: 订单申请改派
 * @author jack.lee titans.lee@gmail.com
 * @date 2013-2-17 上午9:11:52
 * @version V1.0 Copyright: Copyright (c)2012 Company: 湖南中恩通信技术有限公司
 */
public class OrderChangeDB extends DbHelper
{
	private static final String TAG = OrderChangeDB.class.getSimpleName();

	public OrderChangeDB(Context context)
	{
		super(context);
	}

	/**
	 * 单个实体写入数据库
	 * @param ocEntity 工单改派实体
	 * @return 插入后返回的id
	 */
	public long insert(OrderChangeEntity ocEntity)
	{
		SQLiteDatabase db = null;
		try
		{
			db = this.getWritableDatabase();
			ContentValues cv = new ContentValues();

			cv.put("order_num", ocEntity.getOrder_num());
			cv.put("change_reason", ocEntity.getChange_reason()); // 改派原因
			cv.put("change_time", ocEntity.getChange_time()); // 申请时间

            long row = 0;
            if (db != null) {
                row = db.insert(ORDER_CHANGE_TABLE_NAME, null, cv);
            }
            return row;
		}
		finally
		{
			if (db != null)
			{
				db.close();
			}
		}
	}

	/**
	 * 根据ID查询工单详细信息
	 * @param order_num 工单号
	 * @return 工单改派实体
	 */
	public OrderChangeEntity selectById(String order_num)
	{
		SQLiteDatabase db = null;
		Cursor cursor = null;
		OrderChangeEntity item = new OrderChangeEntity();
		String sql = "select id,order_num,change_reason,change_time from " + ORDER_CHANGE_TABLE_NAME + " where order_num= '" + order_num + "'";

		try
		{
			db = this.getWritableDatabase();
			String[] selectionArgs = null;
			cursor = db.rawQuery(sql, selectionArgs);

			if (cursor.moveToNext())
			{
				item.setId(cursor.getInt(cursor.getColumnIndex("id"))); // 获取第一列的值,第一列的索引从0开始
				item.setOrder_num(cursor.getString(cursor.getColumnIndex("order_num")));
				item.setChange_reason(cursor.getString(cursor.getColumnIndex("change_reason")));
				item.setChange_time(cursor.getString(cursor.getColumnIndex("change_time")));
			}
			return item;
		}
		finally
		{
			if (cursor != null)
			{
				cursor.close();
			}
			if (db != null)
			{
				db.close();
			}
		}
	}

    /**
     * 删除小于某一天的数据
     * @param date 指定的日期
     */
	public void deleteOldData(String date)
	{
		SQLiteDatabase db = null;
		try
		{
			db = this.getWritableDatabase();
			String sql = "delete from " + ORDER_CHANGE_TABLE_NAME + " where change_time < '" + date + "'";
			db.execSQL(sql);
		}
		finally
		{
			if (db != null)
			{
				db.close();
			}
		}
	}
}
