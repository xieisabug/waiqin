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
 * <p> * Description: 保存通知详情 * </p>
 * <p> * Copyright: hn Copyright (c) 2012 * </p>
 * <p> * Company: hn * </p>
 * 
 * @author jack.lee E-mail: titans.lee@gmail.com
 * @version 创建时间：2012-9-30 下午07:33:06 类说明
 */
public class NoticeDB extends DbHelper
{
	private static final String TAG = NoticeDB.class.getSimpleName();

	public NoticeDB(Context context)
	{
		super(context);
	}

    /**
     * 分页查找通知信息
     * @param pageID 页码
     * @return 查找到的通知信息
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

				item.setId(cursor.getInt(cursor.getColumnIndex("id"))); // 获取第一列的值,第一列的索引从0开始
				item.setTitle(cursor.getString(cursor.getColumnIndex("title"))); // 公告通知标题
				// item.setContent(cursor.getString(cursor.getColumnIndex("content")));//内容
				item.setPublish_time(cursor.getString(cursor.getColumnIndex("publish_time"))); // 发布时间
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
     * 读取最新的通知
     * @return 最新的通知list
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

				item.setId(cursor.getInt(cursor.getColumnIndex("id"))); // 获取第一列的值,第一列的索引从0开始
				item.setTitle(cursor.getString(cursor.getColumnIndex("title"))); // 公告通知标题
				item.setContent(cursor.getString(cursor.getColumnIndex("content")));// 内容
				item.setPublish_time(cursor.getString(cursor.getColumnIndex("publish_time"))); // 发布时间
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
	 * 单个实体写入数据库
	 * @param nEntity 需要写入的通知实体
	 * @return 插入成功后返回的id
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
	 * 批量写入数据库
	 * @param noticeList 批量插入的通知信息
	 */
	public void addNoticeList(ArrayList<NoticeEntity> noticeList)
	{
		SQLiteDatabase db = null;
		ContentValues cv = null;
		try
		{
			db = this.getWritableDatabase();
			db.beginTransaction();// 手动设置开始事务
			// 数据插入操作循环
			for (NoticeEntity nEntity : noticeList)
			{
				cv = new ContentValues();
				cv.put("title", nEntity.getTitle());
				cv.put("content", nEntity.getContent());
				cv.put("sign", nEntity.getSign());
				cv.put("publish_time", nEntity.getPublish_time());
				db.insert(NOTICE_TABLE_NAME, null, cv);
			}
			db.setTransactionSuccessful();// 设置事务处理成功，不设置会自动回滚不提交
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
	 * 根据ID查询通知详细信息
	 * @param id 需要查询的id
	 * @return 查询到的通知
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
				item.setId(cursor.getInt(cursor.getColumnIndex("id"))); // 获取第一列的值,第一列的索引从0开始
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
	 * 更新工单状态标志
     * @param notice_id 通知id
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
     * 通过内容或者标题搜索通知
     * @param condition 内容
     * @return 查询到的通知实体
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

				item.setId(cursor.getInt(cursor.getColumnIndex("id"))); // 获取第一列的值,第一列的索引从0开始
				item.setTitle(cursor.getString(cursor.getColumnIndex("title"))); // 公告通知标题
				// item.setContent(cursor.getString(cursor.getColumnIndex("content")));//内容
				item.setPublish_time(cursor.getString(cursor.getColumnIndex("publish_time"))); // 发布时间
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
     * 删除小于某一天的数据
     * @param date 指定的日期
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
