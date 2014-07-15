package com.sealion.serviceassistant.db;

import java.util.ArrayList;

import com.sealion.serviceassistant.entity.LocationEntity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author jack.lee titans.lee@gmail.com
 * @version V1.0
 *          Copyright: Copyright (c)2012
 *          Company: �����ж�ͨ�ż������޹�˾
 * @Title: LocationDB.java
 * @Package com.sealion.serviceassistant.db
 * @Description: ��¼�����Աλ����Ϣ����¼���ȣ�γ�� �Լ�����
 * @date 2013-2-17 ����9:16:32
 */
public class LocationDB extends DbHelper {
    private static final String TAG = LocationDB.class.getSimpleName();

    public LocationDB(Context ctx) {
        super(ctx);
    }

    /**
     * ��ȡ������Աλ����Ϣ
     * @return λ����Ϣ��list
     */
    public ArrayList<LocationEntity> getAllLocationData() {
        SQLiteDatabase db = null;
        ArrayList<LocationEntity> data = null;
        Cursor cursor = null;
        String sql = " select id, userid,lititude,longitude," +
                " address, time,areacode from " + LOCATION_TABLE_NAME;
        try {
            db = this.getWritableDatabase();
            String[] selectionArgs = null;
            cursor = db.rawQuery(sql, selectionArgs);

            data = new ArrayList<LocationEntity>();
            LocationEntity item = null;

            while (cursor.moveToNext()) {
                item = new LocationEntity();

                item.setId(cursor.getInt(cursor.getColumnIndex("id"))); // ��ȡ��һ�е�ֵ,��һ�е�������0��ʼ
                item.setUserId(cursor.getString(cursor.getColumnIndex("userId"))); //�û�ID
                item.setLatitude(cursor.getDouble(cursor.getColumnIndex("latitude"))); //γ��
                item.setLongitude(cursor.getDouble(cursor.getColumnIndex("longitude"))); //����
                item.setAddress(cursor.getString(cursor.getColumnIndex("address"))); //��ַ
                item.setTime(cursor.getString(cursor.getColumnIndex("time"))); //ʱ��
                item.setAreaCode(cursor.getString(cursor.getColumnIndex("areacode")));
                data.add(item);
            }
            return data;
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
     * ����һ��λ����Ϣ
     * @param loEntity ��Ҫ�����λ����Ϣ
     * @return ����ɹ����id
     */
    public long insert(LocationEntity loEntity) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("userid", loEntity.getUserId());
            cv.put("lititude", loEntity.getLatitude());
            cv.put("longitude", loEntity.getLongitude());
            cv.put("address", loEntity.getAddress());
            cv.put("time", loEntity.getTime());
            cv.put("areaCode", loEntity.getAreaCode());
            long row = 0;
            if (db != null) {
                row = db.insert(LOCATION_TABLE_NAME, null, cv);
            }
            return row;
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * ɾ������λ����Ϣ����
     */
    public void deleteLocationData() {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            String sql = "delete from " + LOCATION_TABLE_NAME;
            db.execSQL(sql);
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }
}
