package com.sealion.serviceassistant.db;

import java.util.ArrayList;

import com.sealion.serviceassistant.entity.OrderStepEntity;
import com.sealion.serviceassistant.tools.FinalVariables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * @Title: OrderDetailListDB.java
 * @Package com.sealion.serviceassistant.db
 * @Description: ���������������¼������ÿ��������
 * @author jack.lee titans.lee@gmail.com
 * @date 2013-2-17 ����9:13:49
 * @version V1.0 Copyright: Copyright (c)2012 Company: �����ж�ͨ�ż������޹�˾
 */
public class OrderDetailListDB extends DbHelper
{
	private static final String TAG = OrderDetailListDB.class.getSimpleName();

	public OrderDetailListDB(Context context)
	{
		super(context);
	}

	/**
	 * ����ʵ��д�����ݿ�
	 * @param osEntity ����ʵ��
	 * @return ���뷵�ص�id
	 */
	public long insert(OrderStepEntity osEntity)
	{
		SQLiteDatabase db = null;
		try
		{
			db = this.getWritableDatabase();
			ContentValues cv = new ContentValues();
			cv.put("order_num", osEntity.getOrder_num());
			cv.put("order_state", osEntity.getOrder_state());
			cv.put("oper_time", osEntity.getOper_time());
            long row = 0;
            if (db != null) {
                row = db.insert(ORDER_STEP_TABLE_NAME, null, cv);
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
     * ���ݹ����Ż�ȡ��������
     * @param order_num ������
     * @return ��������list
     */
	public ArrayList<OrderStepEntity> getOrderStepByOrderNum(String order_num)
	{
		SQLiteDatabase db = null;
		ArrayList<OrderStepEntity> data = null;
		Cursor cursor = null;
		String sql = "select id,order_num,order_state,oper_time from " + ORDER_STEP_TABLE_NAME + "  where order_num = '" + order_num + "' order by oper_time asc";

		Log.d(TAG, "step slq :" + sql);

		try
		{
			db = this.getWritableDatabase();
			String[] selectionArgs = null;
			cursor = db.rawQuery(sql, selectionArgs);

			data = new ArrayList<OrderStepEntity>();
			OrderStepEntity osEntity = null;

			while (cursor.moveToNext())
			{
				osEntity = new OrderStepEntity();

				osEntity.setId(cursor.getInt(cursor.getColumnIndex("id"))); // ��ȡ��һ�е�ֵ,��һ�е�������0��ʼ
				osEntity.setOrder_num(cursor.getString(cursor.getColumnIndex("order_num"))); // ������
				osEntity.setOrder_state(cursor.getInt(cursor.getColumnIndex("order_state")));// ״̬
				osEntity.setOper_time(cursor.getString(cursor.getColumnIndex("oper_time"))); // ����ʱ��
				data.add(osEntity);
			}
			return data;
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
     * ͨ�������Ż�õִ�Ŀ��ʱ��
     * @param order_num ��������
     * @param state ����״̬
     * @return �ִ�ʱ��
     */
	public String getArriveTargetTimeByOrderNum(String order_num, int state)
	{
		SQLiteDatabase db = null;
		String oper_time = "";
		Cursor cursor = null;
		String sql = "select oper_time from " + ORDER_STEP_TABLE_NAME + "  where order_num = '" + order_num + "' and order_state = " + state;

		Log.d(TAG, "step slq :" + sql);

		try
		{
			db = this.getWritableDatabase();
			String[] selectionArgs = null;
			cursor = db.rawQuery(sql, selectionArgs);

			if (cursor.moveToNext())
			{
				oper_time = cursor.getString(cursor.getColumnIndex("oper_time"));

			}
			return oper_time;
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
			String sql = "delete from " + ORDER_STEP_TABLE_NAME + " where oper_time < '" + date + "'";
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
