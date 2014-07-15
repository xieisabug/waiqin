package com.sealion.serviceassistant.db;

import java.util.ArrayList;

import com.sealion.serviceassistant.entity.FileEntity;
import com.sealion.serviceassistant.tools.DateTools;
import com.sealion.serviceassistant.tools.FinalVariables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * <p>
 * Title: FileManageDB.java
 * </p>
 * <p>
 * Description: 录制文件信息的记录
 * </p>
 * <p>
 * Copyright: hn Copyright (c) 2012
 * </p>
 * <p>
 * Company: hn
 * </p>
 *
 * @author jack.lee E-mail: titans.lee@gmail.com
 * @version 创建时间：2012-8-17 上午10:09:56 类说明:外服人员采集文件数据库记录，包括上传信息
 */
public class FileManageDB extends DbHelper {
    private static final String TAG = FileManageDB.class.getSimpleName();

    public FileManageDB(Context context) {
        super(context);
    }

    /**
     * 分页查找
     *
     * @param pageID    页数
     * @param filetype  文件类型 0：图片，1音频
     * @param filestate 文件状态（0：未上传，1：上传完毕）
     * @return 文件实体
     */
    public ArrayList<FileEntity> getDataByPage(int pageID, int filetype, int filestate) {
        SQLiteDatabase db = null;
        ArrayList<FileEntity> data = null;
        Cursor cursor = null;
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("select id,filename,order_num,");
        sBuilder.append("filestate,filetype from ");
        sBuilder.append(FILE_MANAGE_TABLE_NAME);
        sBuilder.append(" where 1=1");
        if (filetype >= 0) {
            sBuilder.append(" and filetype = " + filetype);
        }
        if (filestate >= 0) {
            sBuilder.append(" and filestate = " + filestate);
        }
        if (pageID >= 0) {
            sBuilder.append(" order by id desc Limit " + PAGE_SIZE + " Offset " + String.valueOf(pageID * PAGE_SIZE));
        }
        try {
            db = this.getWritableDatabase();
            String[] selectionArgs = null;
            cursor = db.rawQuery(sBuilder.toString(), selectionArgs);

            data = new ArrayList<FileEntity>();
            FileEntity item = null;

            while (cursor.moveToNext()) {
                item = new FileEntity();

                item.setId(cursor.getInt(cursor.getColumnIndex("id"))); // 获取第一列的值,第一列的索引从0开始
                item.setOrder_num(cursor.getString(cursor.getColumnIndex("order_num")));
                item.setFilename(cursor.getString(cursor.getColumnIndex("filename")));
                item.setFilestate(cursor.getInt(cursor.getColumnIndex("filestate")));
                item.setFiletype(cursor.getInt(cursor.getColumnIndex("filetype")));
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
     * 查询通过工单号关联的文件
     * @param order_num 工单号
     * @return 获取到的文件数据
     */
    public ArrayList<FileEntity> getDataByOrderNum(String order_num) {
        SQLiteDatabase db = null;
        ArrayList<FileEntity> data = null;
        Cursor cursor = null;
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("select id,filename,order_num,");
        sBuilder.append("filestate,filetype from ");
        sBuilder.append(FILE_MANAGE_TABLE_NAME);
        sBuilder.append(" where order_num = '" + order_num + "' and filestate = 0");

        try {
            db = this.getWritableDatabase();
            String[] selectionArgs = null;
            cursor = db.rawQuery(sBuilder.toString(), selectionArgs);

            data = new ArrayList<FileEntity>();
            FileEntity item = null;

            while (cursor.moveToNext()) {
                item = new FileEntity();

                item.setId(cursor.getInt(cursor.getColumnIndex("id"))); // 获取第一列的值,第一列的索引从0开始
                item.setOrder_num(cursor.getString(cursor.getColumnIndex("order_num")));
                item.setFilename(cursor.getString(cursor.getColumnIndex("filename")));
                item.setFilestate(cursor.getInt(cursor.getColumnIndex("filestate")));
                item.setFiletype(cursor.getInt(cursor.getColumnIndex("filetype")));
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
     * 获取所有上传的数据
     * @return 文件实体list
     */
    public ArrayList<FileEntity> getAllUnUploadData() {
        SQLiteDatabase db = null;
        ArrayList<FileEntity> data = null;
        Cursor cursor = null;
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("select a.id,a.filename,a.order_num,");
        sBuilder.append("a.filestate,a.filetype from ");
        sBuilder.append(FILE_MANAGE_TABLE_NAME + " a," + ORDER_LIST_TABLE_NAME + " b ");
        sBuilder.append(" where  filestate = 0 and a.order_num = b.workCardId and b.order_sign = " + FinalVariables.ORDER_STATE_ORDER_COMPLISH);

        try {
            db = this.getWritableDatabase();
            String[] selectionArgs = null;
            cursor = db.rawQuery(sBuilder.toString(), selectionArgs);

            data = new ArrayList<FileEntity>();
            FileEntity item = null;

            while (cursor.moveToNext()) {
                item = new FileEntity();
                item.setId(cursor.getInt(cursor.getColumnIndex("id"))); // 获取第一列的值,第一列的索引从0开始
                item.setOrder_num(cursor.getString(cursor.getColumnIndex("order_num")));
                item.setFilename(cursor.getString(cursor.getColumnIndex("filename")));
                item.setFilestate(cursor.getInt(cursor.getColumnIndex("filestate")));
                item.setFiletype(cursor.getInt(cursor.getColumnIndex("filetype")));
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
     *  根据id 来更新 文件上传状态
     * @param id 文件id
     */
    public void updateUploadState(int id) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            String where = "id=?";
            String[] whereValue = {String.valueOf(id)};
            ContentValues cv = new ContentValues();
            cv.put("filestate", 1);
            db.update(FILE_MANAGE_TABLE_NAME, cv, where, whereValue);
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * 插入文件数据
     * @param fileEntity 文件实体
     * @return 返回的id
     */
    public long insert(FileEntity fileEntity) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("order_num", fileEntity.getOrder_num());
            cv.put("filename", fileEntity.getFilename());
            cv.put("filestate", fileEntity.getFilestate());
            cv.put("filetype", fileEntity.getFiletype());
            cv.put("insert_time", DateTools.getFormatDateAndTime());
            long row = 0;
            if (db != null) {
                row = db.insert(FILE_MANAGE_TABLE_NAME, null, cv);
            }
            return row;
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * 通过文件名来删除文件
     * @param filename 文件名称
     */
    public void deleteByFilename(String filename) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            String where = "filename=?";
            String[] whereValue = {filename};
            db.delete(FILE_MANAGE_TABLE_NAME, where, whereValue);
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * 统计未上传的类型和数量 int[0] 为图片，int[1]为音频
     * @return 各个类型的数量
     */
    public int[] countUnUpLoadFiles() {
        int[] array = new int[2];
        SQLiteDatabase db = null;
        Cursor cursor = null;
        StringBuilder sBuilder = new StringBuilder();
        // sBuilder.append("select count(*) as count,filetype from ");
        // sBuilder.append(FILE_MANAGE_TABLE_NAME);
        // sBuilder.append(" where filestate = 0 group by filetype ");

        sBuilder.append("select count(*) as count,filetype  from ");
        sBuilder.append(FILE_MANAGE_TABLE_NAME + " as a," + ORDER_LIST_TABLE_NAME + " as b");
        sBuilder.append(" where a.filestate = 0 and a.order_num = b.workCardId and b.order_sign =" + FinalVariables.ORDER_STATE_ORDER_COMPLISH + " group by filetype");
        try {
            db = this.getWritableDatabase();
            String[] selectionArgs = null;
            cursor = db.rawQuery(sBuilder.toString(), selectionArgs);

            while (cursor.moveToNext()) {
                if (cursor.getInt(cursor.getColumnIndex("filetype")) == 0) {
                    array[0] = cursor.getInt(cursor.getColumnIndex("count"));
                } else if (cursor.getInt(cursor.getColumnIndex("filetype")) == 1) {
                    array[1] = cursor.getInt(cursor.getColumnIndex("count"));
                }
            }
            return array;
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
     * 按照工单号统计为上传的文件数
     * @param order_num 工单号
     * @return 数量
     */
    public int countUnUpLoadFilesByOrderNum(String order_num) {
        int count = 0;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("select count(*) as count  from ");
        sBuilder.append(FILE_MANAGE_TABLE_NAME + " as a," + ORDER_LIST_TABLE_NAME + " as b");
        sBuilder.append(" where a.filestate = 0 and a.order_num = '" + order_num + "'" + " and a.order_num = b.workCardId and b.order_sign ="
                + FinalVariables.ORDER_STATE_ORDER_COMPLISH);

        Log.d(TAG, sBuilder.toString());
        try {
            db = this.getWritableDatabase();
            String[] selectionArgs = null;
            cursor = db.rawQuery(sBuilder.toString(), selectionArgs);

            if (cursor.moveToNext()) {
                count = cursor.getInt(cursor.getColumnIndex("count"));
            }
            return count;
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
     * 根据工单号读取所有的文件
     * @param order_num 工单号
     * @return 所读取的文件的实体
     */
    public ArrayList<FileEntity> getAllDataByOrderNum(String order_num) {
        SQLiteDatabase db = null;
        ArrayList<FileEntity> data = null;
        Cursor cursor = null;
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("select id,filename,order_num,");
        sBuilder.append("filestate,filetype from ");
        sBuilder.append(FILE_MANAGE_TABLE_NAME);
        sBuilder.append(" where order_num = '" + order_num + "'");

        try {
            db = this.getWritableDatabase();
            String[] selectionArgs = null;
            cursor = db.rawQuery(sBuilder.toString(), selectionArgs);

            data = new ArrayList<FileEntity>();
            FileEntity item = null;

            while (cursor.moveToNext()) {
                item = new FileEntity();

                item.setId(cursor.getInt(cursor.getColumnIndex("id"))); // 获取第一列的值,第一列的索引从0开始
                item.setOrder_num(cursor.getString(cursor.getColumnIndex("order_num")));
                item.setFilename(cursor.getString(cursor.getColumnIndex("filename")));
                item.setFilestate(cursor.getInt(cursor.getColumnIndex("filestate")));
                item.setFiletype(cursor.getInt(cursor.getColumnIndex("filetype")));
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
     * 删除小于某天的时间的所有数据
     * @param date 指定的日期
     */
    public void deleteOldData(String date) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            String sql = "delete from " + FILE_MANAGE_TABLE_NAME + " where insert_time < '" + date + "'";
            db.execSQL(sql);
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }
}
