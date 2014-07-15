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
 * @Description: 工单处理情况表，记录工单的每个处理步骤
 * @author jack.lee titans.lee@gmail.com
 * @date 2013-2-17 上午9:13:49
 * @version V1.0 Copyright: Copyright (c)2012 Company: 湖南中恩通信技术有限公司
 */
public class OrderDetailListDB extends DbHelper
{
	private static final String TAG = OrderDetailListDB.class.getSimpleName();

	public OrderDetailListDB(Context context)
	{
		super(context);
	}

	/**
	 * 单个实体写入数据库
	 * @param osEntity 步骤实体
	 * @return 插入返回的id
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
     * 根据工单号获取工单步骤
     * @param order_num 工单号
     * @return 工单步骤list
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

				osEntity.setId(cursor.getInt(cursor.getColumnIndex("id"))); // 获取第一列的值,第一列的索引从0开始
				osEntity.setOrder_num(cursor.getString(cursor.getColumnIndex("order_num"))); // 工单号
				osEntity.setOrder_state(cursor.getInt(cursor.getColumnIndex("order_state")));// 状态
				osEntity.setOper_time(cursor.getString(cursor.getColumnIndex("oper_time"))); // 操作时间
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
     * 通过工单号获得抵达目标时间
     * @param order_num 工单号码
     * @param state 工单状态
     * @return 抵达时间
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
     * 删除小于某一天的数据
     * @param date 指定的日期
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
