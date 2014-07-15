package com.sealion.serviceassistant.db;

import java.util.ArrayList;

import com.sealion.serviceassistant.entity.NoticeEntity;
import com.sealion.serviceassistant.tools.FinalVariables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * <p> * Title: NotieDB.java * </p>
 * <p> * Description: ����֪ͨ���� * </p>
 * <p> * Copyright: hn Copyright (c) 2012 * </p>
 * <p> * Company: hn * </p>
 * 
 * @author jack.lee E-mail: titans.lee@gmail.com
 * @version ����ʱ�䣺2012-9-30 ����07:33:06 ��˵��
 */
public class NoticeDB extends DbHelper
{
	private static final String TAG = NoticeDB.class.getSimpleName();

	public NoticeDB(Context context)
	{
		super(context);
	}

    /**
     * ��ҳ����֪ͨ��Ϣ
     * @param pageID ҳ��
     * @return ���ҵ���֪ͨ��Ϣ
     */
	public ArrayList<NoticeEntity> getDataByPage(int pageID)
	{
		SQLiteDatabase db = null;
		ArrayList<NoticeEntity> data = null;
		Cursor cursor = null;
		String sql = "select id,title, sign, publish_time from " + NOTICE_TABLE_NAME 
				+ " order by id desc Limit " + PAGE_SIZE + " Offset "
				+ String.valueOf(pageID * PAGE_SIZE);
		try
		{
			db = this.getWritableDatabase();
			String[] selectionArgs = null;
			cursor = db.rawQuery(sql, selectionArgs);

			data = new ArrayList<NoticeEntity>();
			NoticeEntity item = null;

			while (cursor.moveToNext())
			{
				item = new NoticeEntity();

				item.setId(cursor.getInt(cursor.getColumnIndex("id"))); // ��ȡ��һ�е�ֵ,��һ�е�������0��ʼ
				item.setTitle(cursor.getString(cursor.getColumnIndex("title"))); // ����֪ͨ����
				// item.setContent(cursor.getString(cursor.getColumnIndex("content")));//����
				item.setPublish_time(cursor.getString(cursor.getColumnIndex("publish_time"))); // ����ʱ��
				item.setSign(cursor.getInt(cursor.getColumnIndex("sign")));
				data.add(item);
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
     * ��ȡ���µ�֪ͨ
     * @return ���µ�֪ͨlist
     */
	public ArrayList<NoticeEntity> getTopOneData()
	{
		SQLiteDatabase db = null;
		ArrayList<NoticeEntity> data = null;
		Cursor cursor = null;
		String sql = "select id,title,content,publish_time,sign from " + NOTICE_TABLE_NAME + "  order by id desc Limit 1 ";
		try
		{
			db = this.getWritableDatabase();
			String[] selectionArgs = null;
			cursor = db.rawQuery(sql, selectionArgs);

			data = new ArrayList<NoticeEntity>();
			NoticeEntity item = null;

			if (cursor.moveToNext())
			{
				item = new NoticeEntity();

				item.setId(cursor.getInt(cursor.getColumnIndex("id"))); // ��ȡ��һ�е�ֵ,��һ�е�������0��ʼ
				item.setTitle(cursor.getString(cursor.getColumnIndex("title"))); // ����֪ͨ����
				item.setContent(cursor.getString(cursor.getColumnIndex("content")));// ����
				item.setPublish_time(cursor.getString(cursor.getColumnIndex("publish_time"))); // ����ʱ��
				data.add(item);
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
	 * ����ʵ��д�����ݿ�
	 * @param nEntity ��Ҫд���֪ͨʵ��
	 * @return ����ɹ��󷵻ص�id
	 */
	public long insert(NoticeEntity nEntity)
	{
		SQLiteDatabase db = null;
		try
		{
			db = this.getWritableDatabase();
			ContentValues cv = new ContentValues();
			cv.put("title", nEntity.getTitle());
			cv.put("content", nEntity.getContent());
			cv.put("sign", nEntity.getSign());
			cv.put("publish_time", nEntity.getPublish_time());
            long row = 0;
            if (db != null) {
                row = db.insert(NOTICE_TABLE_NAME, null, cv);
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
	 * ����д�����ݿ�
	 * @param noticeList ���������֪ͨ��Ϣ
	 */
	public void addNoticeList(ArrayList<NoticeEntity> noticeList)
	{
		SQLiteDatabase db = null;
		ContentValues cv = null;
		try
		{
			db = this.getWritableDatabase();
			db.beginTransaction();// �ֶ����ÿ�ʼ����
			// ���ݲ������ѭ��
			for (NoticeEntity nEntity : noticeList)
			{
				cv = new ContentValues();
				cv.put("title", nEntity.getTitle());
				cv.put("content", nEntity.getContent());
				cv.put("sign", nEntity.getSign());
				cv.put("publish_time", nEntity.getPublish_time());
				db.insert(NOTICE_TABLE_NAME, null, cv);
			}
			db.setTransactionSuccessful();// ����������ɹ��������û��Զ��ع����ύ
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
	 * ����ID��ѯ֪ͨ��ϸ��Ϣ
	 * @param id ��Ҫ��ѯ��id
	 * @return ��ѯ����֪ͨ
	 */
	public NoticeEntity selectById(int id)
	{
		SQLiteDatabase db = null;
		Cursor cursor = null;
		NoticeEntity item = new NoticeEntity();
		String sql = "select id,title,content,sign,publish_time from " + NOTICE_TABLE_NAME + " where id= " + id;

		try
		{
			db = this.getWritableDatabase();
			String[] selectionArgs = null;
			cursor = db.rawQuery(sql, selectionArgs);

			if (cursor.moveToNext())
			{
				item.setId(cursor.getInt(cursor.getColumnIndex("id"))); // ��ȡ��һ�е�ֵ,��һ�е�������0��ʼ
				item.setTitle(cursor.getString(cursor.getColumnIndex("title")));
				item.setContent(cursor.getString(cursor.getColumnIndex("content")));
				item.setSign(cursor.getInt(cursor.getColumnIndex("sign")));
				item.setPublish_time(cursor.getString(cursor.getColumnIndex("publish_time")));
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
	 * ���¹���״̬��־
     * @param notice_id ֪ͨid
	 */
	public void updateSignData(int notice_id)
	{
		SQLiteDatabase db = null;
		try
		{
			db = this.getWritableDatabase();

			String sql = "update " + NOTICE_TABLE_NAME + " set sign = 1  where id = " + notice_id;
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
     * ͨ�����ݻ��߱�������֪ͨ
     * @param condition ����
     * @return ��ѯ����֪ͨʵ��
     */
	public ArrayList<NoticeEntity> getDataByCondition(String condition)
	{
		SQLiteDatabase db = null;
		ArrayList<NoticeEntity> data = null;
		Cursor cursor = null;
		String sql = "select id,title," + " sign, publish_time from " + NOTICE_TABLE_NAME + " where content like '%" + condition + "%' or title like '%" + condition + "%'";
		try
		{
			db = this.getWritableDatabase();
			String[] selectionArgs = null;
			cursor = db.rawQuery(sql, selectionArgs);

			data = new ArrayList<NoticeEntity>();
			NoticeEntity item = null;

			while (cursor.moveToNext())
			{
				item = new NoticeEntity();

				item.setId(cursor.getInt(cursor.getColumnIndex("id"))); // ��ȡ��һ�е�ֵ,��һ�е�������0��ʼ
				item.setTitle(cursor.getString(cursor.getColumnIndex("title"))); // ����֪ͨ����
				// item.setContent(cursor.getString(cursor.getColumnIndex("content")));//����
				item.setPublish_time(cursor.getString(cursor.getColumnIndex("publish_time"))); // ����ʱ��
				item.setSign(cursor.getInt(cursor.getColumnIndex("sign")));
				data.add(item);
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
     * ɾ��С��ĳһ�������
     * @param date ָ��������
     */
	public void deleteOldData(String date)
	{
		SQLiteDatabase db = null;
		try
		{
			db = this.getWritableDatabase();
			String sql = "delete from " + NOTICE_TABLE_NAME + " where publish_time < '" + date + "'";
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
