/**
 * @Title: QuestionListDB.java
 * @Package com.sealion.serviceassistant.db
 * @Description: TODO(用一句话描述该文件做什么)
 * @author jack.lee titans.lee@gmail.com   
 * @date 2013-2-18 上午9:55:19 
 * @version V1.0
 * Copyright: Copyright (c)2012
 * Company: 湖南中恩通信技术有限公司
 */
package com.sealion.serviceassistant.db;

import java.util.ArrayList;

import com.sealion.serviceassistant.entity.QuestionEntity;
import com.sealion.serviceassistant.tools.DateTools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * @author jack.lee titans.lee@gmail.com
 * @version V1.0 Copyright: Copyright (c)2012 Company: 湖南中恩通信技术有限公司
 * @Title: QuestionListDB.java
 * @Package com.sealion.serviceassistant.db
 * @Description: 问题表数据库的基本操作
 * @date 2013-2-18 上午9:55:19
 */
public class QuestionListDB extends DbHelper {
    private static final String TAG = QuestionListDB.class.getSimpleName();

    public QuestionListDB(Context context) {
        super(context);
    }

    /**
     * 通过工单号获得所有问题
     * @param order_num 工单号
     * @return 问题列表
     */
    public ArrayList<QuestionEntity> getQuestionByOrderNum(String order_num) {
        ArrayList<QuestionEntity> list = null;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        QuestionEntity item = null;
        String sql = "select id,order_num,q_id,q_content,q_type_id,q_category_id," +
                "q_answer,q_type,q_category,productId,productName from " + QUESTION_TABLE_NAME
                + " where order_num= '" + order_num + "'";

        try {
            db = this.getWritableDatabase();
            String[] selectionArgs = null;
            cursor = db.rawQuery(sql, selectionArgs);
            list = new ArrayList<QuestionEntity>();
            while (cursor.moveToNext()) {
                item = new QuestionEntity();
                item.setId(cursor.getInt(cursor.getColumnIndex("id"))); // 获取第一列的值,第一列的索引从0开始
                item.setQ_id(cursor.getString(cursor.getColumnIndex("q_id")));
                item.setOrder_num(cursor.getString(cursor.getColumnIndex("order_num")));
                item.setQ_content(cursor.getString(cursor.getColumnIndex("q_content")));
                item.setQ_answer(cursor.getString(cursor.getColumnIndex("q_answer")));
                item.setQ_type_id(cursor.getInt(cursor.getColumnIndex("q_type_id")));
                item.setQ_category_id(cursor.getInt(cursor.getColumnIndex("q_category_id")));
                item.setQ_type(cursor.getString(cursor.getColumnIndex("q_type")));
                item.setQ_category(cursor.getString(cursor.getColumnIndex("q_category")));
                item.setProductId(cursor.getString(cursor.getColumnIndex("productId")));
                item.setProductName(cursor.getString(cursor.getColumnIndex("productName")));
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
     * @param qEntity 要插入的问题实体
     * @return 插入成功后返回的id
     */
    public long insert(QuestionEntity qEntity) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("order_num", qEntity.getOrder_num());
            cv.put("q_id", qEntity.getId());
            cv.put("q_content", qEntity.getQ_content());
            cv.put("q_answer", qEntity.getQ_answer());
            cv.put("q_type_id", qEntity.getQ_type_id());
            cv.put("q_category_id", qEntity.getQ_category_id());
            cv.put("q_type", qEntity.getQ_type());
            cv.put("q_category", qEntity.getQ_category());
            cv.put("insert_time", DateTools.getFormatDateAndTime());
            cv.put("productId", qEntity.getProductId());
            cv.put("productName", qEntity.getProductName());
            long row = 0;
            if (db != null) {
                row = db.insert(QUESTION_TABLE_NAME, null, cv);
            }
            return row;
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * 批量插入问题列表
     * @param qList 问题列表list
     */
    public void batchInsert(ArrayList<QuestionEntity> qList) {
        SQLiteDatabase db = null;
        ContentValues cv = null;
        try {
            db = this.getWritableDatabase();
            db.beginTransaction();// 手动设置开始事务
            // 数据插入操作循环
            for (QuestionEntity qEntity : qList) {
                cv = new ContentValues();
                cv.put("order_num", qEntity.getOrder_num());
                cv.put("q_id", qEntity.getQ_id());
                cv.put("q_content", qEntity.getQ_content());
                cv.put("q_answer", qEntity.getQ_answer());
                cv.put("q_type_id", qEntity.getQ_type_id());
                cv.put("q_category_id", qEntity.getQ_category_id());
                cv.put("q_type", qEntity.getQ_type());
                cv.put("q_category", qEntity.getQ_category());
                cv.put("insert_time", DateTools.getFormatDateAndTime());
                cv.put("productId", qEntity.getProductId());
                cv.put("productName", qEntity.getProductName());
                db.insert(QUESTION_TABLE_NAME, null, cv);
            }
            db.setTransactionSuccessful();// 设置事务处理成功，不设置会自动回滚不提交
            db.endTransaction();
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * 更新问题内容
     * @param q_id 问题id
     * @param qEntity 问题实体
     */
    public void updateQuestionContent(String q_id, QuestionEntity qEntity) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            String where = "q_id=?";
            String[] whereValue = {q_id};
            ContentValues cv = new ContentValues();
            cv.put("q_content", qEntity.getQ_content());
            cv.put("q_answer", qEntity.getQ_answer());
            cv.put("q_type_id", qEntity.getQ_type_id());
            cv.put("q_category_id", qEntity.getQ_category_id());
            cv.put("q_type", qEntity.getQ_type());
            cv.put("q_category", qEntity.getQ_category());
            cv.put("productId", qEntity.getProductId());
            cv.put("productName", qEntity.getProductName());
            db.update(QUESTION_TABLE_NAME, cv, where, whereValue);
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * 删除指定日期后的数据
     * @param date 指定的日期
     */
    public void deleteOldData(String date) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            String sql = "delete from " + QUESTION_TABLE_NAME + " where insert_time < '" + date + "'";
            db.execSQL(sql);
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * 通过工单号查询是否有问题
     * @param order_num 工单号
     * @return 是否有问题
     */
    public boolean haveNotAnswerQuestion(String order_num) {
        boolean result = false;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String sql = "select id from " + QUESTION_TABLE_NAME
                + " where q_answer is null and order_num= '" + order_num + "'";

        try {
            db = this.getWritableDatabase();
            String[] selectionArgs = null;
            cursor = db.rawQuery(sql, selectionArgs);
            if (cursor.moveToNext()) {
                result = true;
            }
            return result;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
    }
}
