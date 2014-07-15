package com.sealion.serviceassistant.db;

import com.sealion.serviceassistant.entity.PhoneComplishEntity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * <p>Title: PhoneComplishDB.java</p>
 * <p>Description: �绰�����Ϣ��</p>
 * <p>Copyright: hn Copyright (c) 2012</p>
 * <p>Company: hn</p>
 *
 * @author jack.lee E-mail: titans.lee@gmail.com
 * @version ����ʱ�䣺2012-8-13 ����10:07:25
 */
public class PhoneComplishDB extends DbHelper {
    private static final String TAG = PhoneComplishDB.class.getSimpleName();

    public PhoneComplishDB(Context context) {
        super(context);
    }

    /**
     * ����ʵ��д�����ݿ�
     * @param pcEntity �绰���ʵ��
     * @return ����ɹ��󷵻ص�id
     */
    public long insert(PhoneComplishEntity pcEntity) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();

            cv.put("order_num", pcEntity.getOrder_num());
            cv.put("q_describe", pcEntity.getQ_describe()); // ��������
            cv.put("q_solve", pcEntity.getQ_solve()); // ������
            cv.put("solve_time", pcEntity.getSolve_time()); // ���ʱ��
            cv.put("is_create_new_order", pcEntity.getIs_create_new_order()); // �Ƿ������¹�����0:��1����

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
     * ����ID��ѯ������ϸ��Ϣ
     * @param order_num ͨ�������Ż�ȡ�绰�����Ϣ
     * @return �绰���ʵ��
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
                item.setId(cursor.getInt(cursor.getColumnIndex("id"))); // ��ȡ��һ�е�ֵ,��һ�е�������0��ʼ
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
     * ɾ��С��ĳһ�������
     * @param date ָ��������
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
