package com.sealion.serviceassistant.db;

import java.util.ArrayList;

import com.sealion.serviceassistant.entity.CostEntity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author jack.lee titans.lee@gmail.com
 * @version V1.0
 *          Copyright: Copyright (c)2012
 *          Company: 湖南中恩通信技术有限公司
 * @Title: CostDB.java
 * @Package com.sealion.serviceassistant.db
 * @Description: 费用类别基础数据表
 * @date 2013-2-17 上午9:07:09
 */
public class CostDB extends DbHelper {
    private static final String TAG = CostDB.class.getSimpleName();

    public CostDB(Context context) {
        super(context);
    }

    /**
     * 获取所有费用类别
     * @return 所有费用类别组成的list
     */
    public ArrayList<CostEntity> getCost() {
        ArrayList<CostEntity> list;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        CostEntity item;
        String sql = "select id,cost_id,name from " + COST_TABLE_NAME;

        try {
            db = this.getWritableDatabase();
            String[] selectionArgs = null;
            cursor = db.rawQuery(sql, selectionArgs);
            list = new ArrayList<CostEntity>();
            while (cursor.moveToNext()) {
                item = new CostEntity();
                item.setId(cursor.getInt(cursor.getColumnIndex("id"))); // 获取第一列的值,第一列的索引从0开始
                item.setCost_id(cursor.getInt(cursor.getColumnIndex("cost_id")));
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
     * 单个费用类别实体写入数据库
     *
     * @param cEntity 需要插入的费用类别
     * @return 插入数据库后的id
     */
    public long insert(CostEntity cEntity) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();

            cv.put("cost_id", cEntity.getCost_id());
            cv.put("name", cEntity.getName()); //费用名称

            long row = 0;
            if (db != null) {
                row = db.insert(COST_TABLE_NAME, null, cv);
            }
            return row;
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * 批量的插入费用类别实体到数据库
     * @param costList 需要批量插入的费用类别实体
     */
    public void batchInsert(ArrayList<CostEntity> costList) {
        SQLiteDatabase db = null;
        ContentValues cv;
        try {
            db = this.getWritableDatabase();
            db.beginTransaction();//手动设置开始事务
            //数据插入操作循环
            for (CostEntity cEntity : costList) {
                cv = new ContentValues();
                cv.put("cost_id", cEntity.getCost_id());
                cv.put("name", cEntity.getName()); //费用名称
                db.insert(COST_TABLE_NAME, null, cv);
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
     * 更新费用类别实体（更新其名称）
     * @param cEntity 更改后的费用类别实体
     */
    public void updateData(CostEntity cEntity) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            String where = "cost_id=?";
            String[] whereValue = {String.valueOf(cEntity.getCost_id())};
            ContentValues cv = new ContentValues();
            cv.put("name", cEntity.getName());
            db.update(COST_TABLE_NAME, cv, where, whereValue);
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * 删除指定ID数据
     * @param cost_id 需要删除的费用类别id
     */
    public void deleteData(int cost_id) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            String sql = "delete from " + COST_TABLE_NAME + "where cost_id=" + cost_id;
            db.execSQL(sql);
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * 删除所有费用类别数据
     */
    public void deleteAllData() {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            String sql = "delete from " + COST_TABLE_NAME;
            db.execSQL(sql);
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }
}
