package com.sealion.serviceassistant.db;

import java.util.ArrayList;

import com.sealion.serviceassistant.entity.Q_Category;
import com.sealion.serviceassistant.entity.Q_Type;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**   
* �������������ݱ�
*/
public class QCategoryDB extends DbHelper
{
	private static final String TAG = QCategoryDB.class.getSimpleName();
	
	public QCategoryDB(Context context)
	{
		super(context);
	}

    /**
     * ��ȡ���е��������
     * @return ��������б�
     */
	public ArrayList<Q_Category> getQ_Category()
	{
		ArrayList<Q_Category> list;
		SQLiteDatabase db = null;
		Cursor cursor = null;
		Q_Category item;
		String sql = "select id,category_id,name,productName,productId from " + Q_CATEGORY_TABLE_NAME ;

		try
		{
			db = this.getWritableDatabase();
			String[] selectionArgs = null;
			cursor = db.rawQuery(sql, selectionArgs);
			list = new ArrayList<Q_Category>();
			while (cursor.moveToNext())
			{		
				item = new Q_Category();
				item.setId(cursor.getInt(cursor.getColumnIndex("id"))); // ��ȡ��һ�е�ֵ,��һ�е�������0��ʼ
				item.setCategory_id(cursor.getInt(cursor.getColumnIndex("category_id")));
				item.setName(cursor.getString(cursor.getColumnIndex("name")));
				item.setProductId(cursor.getString(cursor.getColumnIndex("productId")));
				item.setProductName(cursor.getString(cursor.getColumnIndex("productName")));
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
     * ͨ��id��ȡ�������
     * @param categoryId �������id
     * @return �������
     */
	public Q_Category getQ_CategoryById(int categoryId)
	{
		SQLiteDatabase db = null;
		Cursor cursor = null;
		Q_Category item = null;
		String sql = "select id,category_id,name,productName,productId from " + Q_CATEGORY_TABLE_NAME +" where category_id="+categoryId;

		try
		{
			db = this.getWritableDatabase();
			String[] selectionArgs = null;
			cursor = db.rawQuery(sql, selectionArgs);
			while (cursor.moveToNext())
			{		
				item = new Q_Category();
				item.setId(cursor.getInt(cursor.getColumnIndex("id"))); // ��ȡ��һ�е�ֵ,��һ�е�������0��ʼ
				item.setCategory_id(cursor.getInt(cursor.getColumnIndex("category_id")));
				item.setName(cursor.getString(cursor.getColumnIndex("name")));
				item.setProductId(cursor.getString(cursor.getColumnIndex("productId")));
				item.setProductName(cursor.getString(cursor.getColumnIndex("productName")));
			}
			return item;
		}catch(Exception ex){
			System.out.println(ex.getMessage());
			return null;

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
	 * @param qcEntity Ҫ������������
	 * @return ����ɹ����ص�id
	 */
	public long insert(Q_Category qcEntity)
	{
		SQLiteDatabase db = null;
		try
		{
			db = this.getWritableDatabase();
			ContentValues cv = new ContentValues();
			
			cv.put("category_id",qcEntity.getCategory_id());
			cv.put("name",qcEntity.getName());
			cv.put("productId", qcEntity.getProductId());
			cv.put("productName", qcEntity.getProductName());
			long row = db.insert(Q_CATEGORY_TABLE_NAME, null, cv);
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
     * ���������������
     * @param qcList ��������б�
     */
	public void batchInsert(ArrayList<Q_Category> qcList)
	{
		SQLiteDatabase db = null;
		ContentValues cv;
		try
		{
			db = this.getWritableDatabase();
			db.beginTransaction();//�ֶ����ÿ�ʼ����
			//���ݲ������ѭ��
			for (Q_Category qcEntity : qcList)
			{
				cv = new ContentValues();
				cv.put("category_id",qcEntity.getCategory_id());
				cv.put("name",qcEntity.getName());
				cv.put("productId", qcEntity.getProductId());
				cv.put("productName", qcEntity.getProductName());
				db.insert(Q_CATEGORY_TABLE_NAME, null, cv);
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
     * �����������
     * @param qcEntity ���µ�ʵ��
     */
	public void updateData(Q_Category qcEntity)
	{
		SQLiteDatabase db = null;
		try
		{
			db = this.getWritableDatabase();
			String where = "category_id=?";
			String[] whereValue = { String.valueOf(qcEntity.getCategory_id()) };
			ContentValues cv = new ContentValues();
			cv.put("name", qcEntity.getName());
			cv.put("productId", qcEntity.getProductId());
			cv.put("productName", qcEntity.getProductName());
			db.update(Q_CATEGORY_TABLE_NAME, cv, where, whereValue);
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
     * @param category_id �������id
	 */
	public void deleteData(int category_id)
	{
		SQLiteDatabase db = null;
		try
		{
			db = this.getWritableDatabase();
			String sql = "delete from " + Q_CATEGORY_TABLE_NAME +"where category_id=" + category_id;			
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
     * ɾ�����������������
     */
	public void deleteAllData()
	{
		SQLiteDatabase db = null;
		try
		{
			db = this.getWritableDatabase();
			String sql = "delete from " + Q_CATEGORY_TABLE_NAME;			
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
