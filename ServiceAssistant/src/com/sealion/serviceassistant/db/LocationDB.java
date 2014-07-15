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
 *          Company: 湖南中恩通信技术有限公司
 * @Title: LocationDB.java
 * @Package com.sealion.serviceassistant.db
 * @Description: 记录外服人员位置信息表，记录经度，纬度 以及地区
 * @date 2013-2-17 上午9:16:32
 */
public class LocationDB extends DbHelper {
    private static final String TAG = LocationDB.class.getSimpleName();

    public LocationDB(Context ctx) {
        super(ctx);
    }

    /**
     * 获取所有人员位置信息
     * @return 位置信息的list
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

                item.setId(cursor.getInt(cursor.getColumnIndex("id"))); // 获取第一列的值,第一列的索引从0开始
                item.setUserId(cursor.getString(cursor.getColumnIndex("userId"))); //用户ID
                item.setLatitude(cursor.getDouble(cursor.getColumnIndex("latitude"))); //纬度
                item.setLongitude(cursor.getDouble(cursor.getColumnIndex("longitude"))); //经度
                item.setAddress(cursor.getString(cursor.getColumnIndex("address"))); //地址
                item.setTime(cursor.getString(cursor.getColumnIndex("time"))); //时间
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
     * 插入一个位置信息
     * @param loEntity 需要插入的位置信息
     * @return 插入成功后的id
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
     * 删除所有位置信息数据
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
