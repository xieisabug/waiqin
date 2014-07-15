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
 *          Company: �����ж�ͨ�ż������޹�˾
 * @Title: CostDB.java
 * @Package com.sealion.serviceassistant.db
 * @Description: �������������ݱ�
 * @date 2013-2-17 ����9:07:09
 */
public class CostDB extends DbHelper {
    private static final String TAG = CostDB.class.getSimpleName();

    public CostDB(Context context) {
        super(context);
    }

    /**
     * ��ȡ���з������
     * @return ���з��������ɵ�list
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
                item.setId(cursor.getInt(cursor.getColumnIndex("id"))); // ��ȡ��һ�е�ֵ,��һ�е�������0��ʼ
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
     * �����������ʵ��д�����ݿ�
     *
     * @param cEntity ��Ҫ����ķ������
     * @return �������ݿ���id
     */
    public long insert(CostEntity cEntity) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();

            cv.put("cost_id", cEntity.getCost_id());
            cv.put("name", cEntity.getName()); //��������

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
     * �����Ĳ���������ʵ�嵽���ݿ�
     * @param costList ��Ҫ��������ķ������ʵ��
     */
    public void batchInsert(ArrayList<CostEntity> costList) {
        SQLiteDatabase db = null;
        ContentValues cv;
        try {
            db = this.getWritableDatabase();
            db.beginTransaction();//�ֶ����ÿ�ʼ����
            //���ݲ������ѭ��
            for (CostEntity cEntity : costList) {
                cv = new ContentValues();
                cv.put("cost_id", cEntity.getCost_id());
                cv.put("name", cEntity.getName()); //��������
                db.insert(COST_TABLE_NAME, null, cv);
            }
            db.setTransactionSuccessful();//����������ɹ��������û��Զ��ع����ύ
            db.endTransaction();
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * ���·������ʵ�壨���������ƣ�
     * @param cEntity ���ĺ�ķ������ʵ��
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
     * ɾ��ָ��ID����
     * @param cost_id ��Ҫɾ���ķ������id
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
     * ɾ�����з����������
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
