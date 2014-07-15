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
* @Description: ˰��������ݳ�ʼ�� 
* @author jack.lee titans.lee@gmail.com   
* @date 2013-2-17 ����9:04:49 
* @version V1.0
* Copyright: Copyright (c)2012
* Company: �����ж�ͨ�ż������޹�˾
*/
public class TaxBureauDB extends DbHelper
{
	private static final String TAG = TaxBureauDB.class.getSimpleName();
	
	public TaxBureauDB(Context context)
	{
		super(context);		
	}

    /**
     * ͨ�����б����ȡ˰�������Ϣ
     * @param city ���б���
     * @return ˰�������Ϣlist
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
				item.setId(cursor.getInt(cursor.getColumnIndex("id"))); // ��ȡ��һ�е�ֵ,��һ�е�������0��ʼ
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
	 * ����ʵ��д�����ݿ�
	 * @param tbEntity ˰��־���Ϣ
	 * @return ����ɹ��󷵻ص�id
	 */
	public long insert(TaxBureauEntity tbEntity)
	{
		SQLiteDatabase db = null;
		try
		{
			db = this.getWritableDatabase();
			ContentValues cv = new ContentValues();
			
			cv.put("bureau_id",tbEntity.getBureau_id());
			cv.put("bureau_name",tbEntity.getBureau_name()); //��������
			cv.put("city",tbEntity.getCity()); //������

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
     * ��������˰��־���Ϣ
     * @param taxList ˰��־�list
     */
	public void batchInsert(ArrayList<TaxBureauEntity> taxList)
	{
		SQLiteDatabase db = null;
		ContentValues cv = null;
		try
		{
			db = this.getWritableDatabase();
			db.beginTransaction();//�ֶ����ÿ�ʼ����
			//���ݲ������ѭ��
			for (TaxBureauEntity tbEntity : taxList)
			{
				cv = new ContentValues();

				cv.put("bureau_id",tbEntity.getBureau_id());
				cv.put("bureau_name",tbEntity.getBureau_name()); //��������
				cv.put("city",tbEntity.getCity()); //������
				db.insert(TAX_BUREAU_TABLE_NAME, null, cv);
			}
			db.setTransactionSuccessful();//����������ɹ��������û��Զ��ع����ύ
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
     * ����˰��־���Ϣ
     * @param tbEntity ˰��־�ʵ����Ϣ
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
	 * ɾ��ָ��ID����
     * @param bureau_id ˰��־�id
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
     * ɾ����������
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
