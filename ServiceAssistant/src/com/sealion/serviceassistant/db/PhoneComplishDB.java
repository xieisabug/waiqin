package com.sealion.serviceassistant.db;

import com.sealion.serviceassistant.entity.PhoneComplishEntity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * <p>Title: PhoneComplishDB.java</p>
 * <p>Description: 电话完成信息表</p>
 * <p>Copyright: hn Copyright (c) 2012</p>
 * <p>Company: hn</p>
 *
 * @author jack.lee E-mail: titans.lee@gmail.com
 * @version 创建时间：2012-8-13 上午10:07:25
 */
public class PhoneComplishDB extends DbHelper {
    private static final String TAG = PhoneComplishDB.class.getSimpleName();

    public PhoneComplishDB(Context context) {
        super(context);
    }

    /**
     * 单个实体写入数据库
     * @param pcEntity 电话完成实体
     * @return 插入成功后返回的id
     */
    public long insert(PhoneComplishEntity pcEntity) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();

            cv.put("order_num", pcEntity.getOrder_num());
            cv.put("q_describe", pcEntity.getQ_describe()); // 问题描述
            cv.put("q_solve", pcEntity.getQ_solve()); // 问题解决
            cv.put("solve_time", pcEntity.getSolve_time()); // 解决时间
            cv.put("is_create_new_order", pcEntity.getIs_create_new_order()); // 是否生成新工单，0:否，1：是

            long row = 0;
            if (db != null) {
                row = db.insert(PHONE_COMPLISH_TABLE_NAME, null, cv);
            }
            return row;
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * 根据ID查询工单详细信息
     * @param order_num 通过工单号获取电话完成信息
     * @return 电话完成实体
     */
    public PhoneComplishEntity SelectById(String order_num) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        PhoneComplishEntity item = new PhoneComplishEntity();
        String sql = "select id,order_num,q_describe,q_solve,solve_time,is_create_new_order from " + PHONE_COMPLISH_TABLE_NAME + " where order_num= '" + order_num + "'";

        try {
            db = this.getWritableDatabase();
            String[] selectionArgs = null;
            cursor = db.rawQuery(sql, selectionArgs);

            if (cursor.moveToNext()) {
                item.setId(cursor.getInt(cursor.getColumnIndex("id"))); // 获取第一列的值,第一列的索引从0开始
                item.setOrder_num(cursor.getString(cursor.getColumnIndex("order_num")));
                item.setQ_describe(cursor.getString(cursor.getColumnIndex("q_describe")));
                item.setQ_solve(cursor.getString(cursor.getColumnIndex("q_solve")));
                item.setSolve_time(cursor.getString(cursor.getColumnIndex("solve_time")));
                item.setIs_create_new_order(cursor.getInt(cursor.getColumnIndex("is_create_new_order")));
            }
            return item;
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
     * 删除小于某一天的数据
     * @param date 指定的日期
     */
    public void deleteOldData(String date) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            String sql = "delete from " + PHONE_COMPLISH_TABLE_NAME + " where solve_time < '" + date + "'";
            db.execSQL(sql);
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }
}
