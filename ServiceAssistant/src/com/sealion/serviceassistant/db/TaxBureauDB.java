package com.sealion.serviceassistant.db;

import java.util.ArrayList;

import com.sealion.serviceassistant.entity.Q_Category;
import com.sealion.serviceassistant.entity.TaxBureauEntity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**   
* @Title: TaxBureauDB.java 
* @Package com.sealion.serviceassistant.db 
* @Description: 税务机关数据初始化 
* @author jack.lee titans.lee@gmail.com   
* @date 2013-2-17 上午9:04:49 
* @version V1.0
* Copyright: Copyright (c)2012
* Company: 湖南中恩通信技术有限公司
*/
public class TaxBureauDB extends DbHelper
{
	private static final String TAG = TaxBureauDB.class.getSimpleName();
	
	public TaxBureauDB(Context context)
	{
		super(context);		
	}

    /**
     * 通过城市编码获取税务机关信息
     * @param city 城市编码
     * @return 税务机关信息list
     */
	public ArrayList<TaxBureauEntity> getTaxBureauByCityCode(String city)
	{
		ArrayList<TaxBureauEntity> list = null;
		SQLiteDatabase db = null;
		Cursor cursor = null;
		TaxBureauEntity item = null;
		String sql = "select id,bureau_id,bureau_name from " + TAX_BUREAU_TABLE_NAME 
				+ " where city= '"+city+"'";

		try
		{
			db = this.getWritableDatabase();
			String[] selectionArgs = null;
			cursor = db.rawQuery(sql, selectionArgs);
			list = new ArrayList<TaxBureauEntity>();
			while (cursor.moveToNext())
			{		
				item = new TaxBureauEntity();
				item.setId(cursor.getInt(cursor.getColumnIndex("id"))); // 获取第一列的值,第一列的索引从0开始
				item.setBureau_id(cursor.getInt(cursor.getColumnIndex("bureau_id")));
				item.setBureau_name(cursor.getString(cursor.getColumnIndex("bureau_name")));
				list.add(item);
			}
			return list;
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
	 * 单个实体写入数据库
	 * @param tbEntity 税务分局信息
	 * @return 插入成功后返回的id
	 */
	public long insert(TaxBureauEntity tbEntity)
	{
		SQLiteDatabase db = null;
		try
		{
			db = this.getWritableDatabase();
			ContentValues cv = new ContentValues();
			
			cv.put("bureau_id",tbEntity.getBureau_id());
			cv.put("bureau_name",tbEntity.getBureau_name()); //问题描述
			cv.put("city",tbEntity.getCity()); //问题解决

            long row = 0;
            if (db != null) {
                row = db.insert(TAX_BUREAU_TABLE_NAME, null, cv);
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
     * 批量插入税务分局信息
     * @param taxList 税务分局list
     */
	public void batchInsert(ArrayList<TaxBureauEntity> taxList)
	{
		SQLiteDatabase db = null;
		ContentValues cv = null;
		try
		{
			db = this.getWritableDatabase();
			db.beginTransaction();//手动设置开始事务
			//数据插入操作循环
			for (TaxBureauEntity tbEntity : taxList)
			{
				cv = new ContentValues();

				cv.put("bureau_id",tbEntity.getBureau_id());
				cv.put("bureau_name",tbEntity.getBureau_name()); //问题描述
				cv.put("city",tbEntity.getCity()); //问题解决
				db.insert(TAX_BUREAU_TABLE_NAME, null, cv);
			}
			db.setTransactionSuccessful();//设置事务处理成功，不设置会自动回滚不提交
			db.endTransaction();
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
     * 更新税务分局信息
     * @param tbEntity 税务分局实体信息
     */
	public void updateData(TaxBureauEntity tbEntity)
	{
		SQLiteDatabase db = null;
		try
		{
			db = this.getWritableDatabase();
			String where = "bureau_id=?";
			String[] whereValue = { String.valueOf(tbEntity.getBureau_id()) };
			ContentValues cv = new ContentValues();
			cv.put("bureau_name", tbEntity.getBureau_name());
			cv.put("city", tbEntity.getCity());
			db.update(TAX_BUREAU_TABLE_NAME, cv, where, whereValue);
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
	 * 删除指定ID数据
     * @param bureau_id 税务分局id
	 */
	public void deleteData(int bureau_id)
	{
		SQLiteDatabase db = null;
		try
		{
			db = this.getWritableDatabase();
			String sql = "delete from " + TAX_BUREAU_TABLE_NAME +"where bureau_id=" + bureau_id;			
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

    /**
     * 删除所有数据
     */
	public void deleteAllData()
	{
		SQLiteDatabase db = null;
		try
		{
			db = this.getWritableDatabase();
			String sql = "delete from " + TAX_BUREAU_TABLE_NAME ;			
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
