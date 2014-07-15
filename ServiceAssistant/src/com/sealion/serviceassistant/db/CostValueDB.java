package com.sealion.serviceassistant.db;

import java.util.ArrayList;

import com.sealion.serviceassistant.entity.CostEntity;
import com.sealion.serviceassistant.tools.DateTools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author jack.lee titans.lee@gmail.com
 * @version V1.0 Copyright: Copyright (c)2012 Company: �����ж�ͨ�ż������޹�˾
 * @Title: CostValueDB.java
 * @Package com.sealion.serviceassistant.db
 * @Description: ���ñ�����
 * @date 2013-3-6 ����11:31:29
 */
public class CostValueDB extends DbHelper {
    private static final String TAG = CostValueDB.class.getSimpleName();

    public CostValueDB(Context context) {
        super(context);
    }

    /**
     * ͨ�������Ż�ȡ���ñ�����Ϣ
     * @param order_num ��������
     * @return ���ñ�����Ϣ�б�
     */
    public ArrayList<CostEntity> getCost(String order_num) {
        ArrayList<CostEntity> list;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        CostEntity item;
        String sql = "select id,order_num,cost_id,cost_name,cost_value from " + COST_VALUE_TABLE_NAME + " where order_num = '" + order_num + "'";

        try {
            db = this.getWritableDatabase();
            String[] selectionArgs = null;
            cursor = db.rawQuery(sql, selectionArgs);
            list = new ArrayList<CostEntity>();
            while (cursor.moveToNext()) {
                item = new CostEntity();
                item.setId(cursor.getInt(cursor.getColumnIndex("id"))); // ��ȡ��һ�е�ֵ,��һ�е�������0��ʼ
                item.setName(cursor.getString(cursor.getColumnIndex("cost_name")));
                item.setValue(cursor.getString(cursor.getColumnIndex("cost_value")));
                item.setOrder_num(cursor.getString(cursor.getColumnIndex("order_num")));
                item.setCost_id(cursor.getInt(cursor.getColumnIndex("cost_id")));
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
     * ����������ñ�����Ϣ
     * @param costList ��Ҫ��������ķ��ñ�����Ϣ
     */
    public void batchInsert(ArrayList<CostEntity> costList) {
        SQLiteDatabase db = null;
        ContentValues cv;
        try {
            db = this.getWritableDatabase();
            db.beginTransaction();// �ֶ����ÿ�ʼ����
            // ���ݲ������ѭ��
            for (CostEntity cEntity : costList) {
                cv = new ContentValues();
                cv.put("order_num", cEntity.getOrder_num());
                cv.put("cost_name", cEntity.getName());// ��������
                cv.put("cost_value", cEntity.getValue());
                cv.put("cost_id", cEntity.getCost_id());
                cv.put("insert_time", DateTools.getFormatDateAndTime());
                db.insert(COST_VALUE_TABLE_NAME, null, cv);
            }
            db.setTransactionSuccessful();// ����������ɹ��������û��Զ��ع����ύ
            db.endTransaction();
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * ɾ��ĳ��ʱ��֮ǰ������
     * @param date ��Ҫɾ����ʱ��
     */
    public void deleteOldData(String date) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            String sql = "delete from " + COST_VALUE_TABLE_NAME + " where insert_time < '" + date + "'";
            db.execSQL(sql);
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }
}
