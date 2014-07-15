package com.sealion.serviceassistant.db;

import com.sealion.serviceassistant.entity.OrderChangeEntity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @Title: OrderChangeDB.java
 * @Package com.sealion.serviceassistant.db
 * @Description: �����������
 * @author jack.lee titans.lee@gmail.com
 * @date 2013-2-17 ����9:11:52
 * @version V1.0 Copyright: Copyright (c)2012 Company: �����ж�ͨ�ż������޹�˾
 */
public class OrderChangeDB extends DbHelper
{
	private static final String TAG = OrderChangeDB.class.getSimpleName();

	public OrderChangeDB(Context context)
	{
		super(context);
	}

	/**
	 * ����ʵ��д�����ݿ�
	 * @param ocEntity ��������ʵ��
	 * @return ����󷵻ص�id
	 */
	public long insert(OrderChangeEntity ocEntity)
	{
		SQLiteDatabase db = null;
		try
		{
			db = this.getWritableDatabase();
			ContentValues cv = new ContentValues();

			cv.put("order_num", ocEntity.getOrder_num());
			cv.put("change_reason", ocEntity.getChange_reason()); // ����ԭ��
			cv.put("change_time", ocEntity.getChange_time()); // ����ʱ��

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
	 * ����ID��ѯ������ϸ��Ϣ
	 * @param order_num ������
	 * @return ��������ʵ��
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
				item.setId(cursor.getInt(cursor.getColumnIndex("id"))); // ��ȡ��һ�е�ֵ,��һ�е�������0��ʼ
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
     * ɾ��С��ĳһ�������
     * @param date ָ��������
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
