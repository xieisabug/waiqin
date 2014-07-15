package com.sealion.serviceassistant.db;

import java.util.ArrayList;

import com.sealion.serviceassistant.entity.Q_Type;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * @author jack.lee titans.lee@gmail.com
 * @version V1.0
 *          Copyright: Copyright (c)2012
 *          Company: 湖南中恩通信技术有限公司
 * @Title: QTypeDB.java
 * @Package com.sealion.serviceassistant.db
 * @Description: 问题类型基础数据初始化表
 * @date 2013-2-17 上午9:04:03
 */
public class QTypeDB extends DbHelper {
    private static final String TAG = QTypeDB.class.getSimpleName();

    public QTypeDB(Context context) {
        super(context);
    }

    /**
     * 获取问题类型总数
     *
     * @return 数量
     */
    public int getCountData() {
        int size = 0;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String sql = "select count(*) as count from " + Q_TYPE_TABLE_NAME;

        try {
            db = this.getWritableDatabase();
            String[] selectionArgs = null;
            cursor = db.rawQuery(sql, selectionArgs);
            if (cursor.moveToNext()) {
                size = cursor.getInt(cursor.getColumnIndex("count")); // 获取第一列的值,第一列的索引从0开始
            }
            return size;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * 通过问题类别id获取问题类型
     * @param q_category_id 问题类别id
     * @return 问题类型list
     */
    public ArrayList<Q_Type> getQTypeByCategoryId(String q_category_id) {
        ArrayList<Q_Type> list = null;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        Q_Type item = null;
        String sql = "select id,type_id,name from " + Q_TYPE_TABLE_NAME
                + " where q_category_id= '" + q_category_id + "'";
        Log.d(TAG, sql);
        try {
            db = this.getWritableDatabase();
            String[] selectionArgs = null;
            cursor = db.rawQuery(sql, selectionArgs);
            list = new ArrayList<Q_Type>();
            while (cursor.moveToNext()) {
                item = new Q_Type();
                item.setId(cursor.getInt(cursor.getColumnIndex("id"))); // 获取第一列的值,第一列的索引从0开始
                item.setType_id(cursor.getInt(cursor.getColumnIndex("type_id")));
                item.setName(cursor.getString(cursor.getColumnIndex("name")));
                list.add(item);
            }
            return list;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * 单个实体写入数据库
     * @param qtEntity 需要插入的问题类型
     * @return 插入成功返回的id
     */
    public long insert(Q_Type qtEntity) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("type_id", qtEntity.getType_id());
            cv.put("name", qtEntity.getName());
            cv.put("q_category_id", qtEntity.getQ_category_id());
            long row = db.insert(Q_TYPE_TABLE_NAME, null, cv);
            return row;
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * 批量插入问题类型
     * @param qtList 问题类型列表
     */
    public void batchInsert(ArrayList<Q_Type> qtList) {
        SQLiteDatabase db = null;
        ContentValues cv = null;
        try {
            db = this.getWritableDatabase();
            db.beginTransaction();//手动设置开始事务
            //数据插入操作循环
            for (Q_Type qtEntity : qtList) {
                cv = new ContentValues();
                cv.put("type_id", qtEntity.getType_id());
                cv.put("name", qtEntity.getName());
                cv.put("q_category_id", qtEntity.getQ_category_id());
                db.insert(Q_TYPE_TABLE_NAME, null, cv);
            }
            db.setTransactionSuccessful();//设置事务处理成功，不设置会自动回滚不提交
            db.endTransaction();
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * 更新问题类型
     * @param qtEntity 更新的实体
     */
    public void updateData(Q_Type qtEntity) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            String where = "type_id=?";
            String[] whereValue = {String.valueOf(qtEntity.getType_id())};
            ContentValues cv = new ContentValues();
            cv.put("q_category_id", qtEntity.getQ_category_id());
            cv.put("name", qtEntity.getName());
            db.update(Q_TYPE_TABLE_NAME, cv, where, whereValue);
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * 删除指定ID数据
     * @param type_id 问题类型id
     */
    public void deleteData(int type_id) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            String sql = "delete from " + Q_TYPE_TABLE_NAME + "where type_id=" + type_id;
            db.execSQL(sql);
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * 删除所有问题类型数据
     */
    public void deleteAllData() {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            String sql = "delete from " + Q_TYPE_TABLE_NAME;
            db.execSQL(sql);
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }
}
