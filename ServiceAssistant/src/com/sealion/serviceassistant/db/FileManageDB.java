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
 * Description: ¼���ļ���Ϣ�ļ�¼
 * </p>
 * <p>
 * Copyright: hn Copyright (c) 2012
 * </p>
 * <p>
 * Company: hn
 * </p>
 *
 * @author jack.lee E-mail: titans.lee@gmail.com
 * @version ����ʱ�䣺2012-8-17 ����10:09:56 ��˵��:�����Ա�ɼ��ļ����ݿ��¼�������ϴ���Ϣ
 */
public class FileManageDB extends DbHelper {
    private static final String TAG = FileManageDB.class.getSimpleName();

    public FileManageDB(Context context) {
        super(context);
    }

    /**
     * ��ҳ����
     *
     * @param pageID    ҳ��
     * @param filetype  �ļ����� 0��ͼƬ��1��Ƶ
     * @param filestate �ļ�״̬��0��δ�ϴ���1���ϴ���ϣ�
     * @return �ļ�ʵ��
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

                item.setId(cursor.getInt(cursor.getColumnIndex("id"))); // ��ȡ��һ�е�ֵ,��һ�е�������0��ʼ
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
     * ��ѯͨ�������Ź������ļ�
     * @param order_num ������
     * @return ��ȡ�����ļ�����
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

                item.setId(cursor.getInt(cursor.getColumnIndex("id"))); // ��ȡ��һ�е�ֵ,��һ�е�������0��ʼ
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
     * ��ȡ�����ϴ�������
     * @return �ļ�ʵ��list
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
                item.setId(cursor.getInt(cursor.getColumnIndex("id"))); // ��ȡ��һ�е�ֵ,��һ�е�������0��ʼ
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
     *  ����id ������ �ļ��ϴ�״̬
     * @param id �ļ�id
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
     * �����ļ�����
     * @param fileEntity �ļ�ʵ��
     * @return ���ص�id
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
     * ͨ���ļ�����ɾ���ļ�
     * @param filename �ļ�����
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
     * ͳ��δ�ϴ������ͺ����� int[0] ΪͼƬ��int[1]Ϊ��Ƶ
     * @return �������͵�����
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
     * ���չ�����ͳ��Ϊ�ϴ����ļ���
     * @param order_num ������
     * @return ����
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
     * ���ݹ����Ŷ�ȡ���е��ļ�
     * @param order_num ������
     * @return ����ȡ���ļ���ʵ��
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

                item.setId(cursor.getInt(cursor.getColumnIndex("id"))); // ��ȡ��һ�е�ֵ,��һ�е�������0��ʼ
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
     * ɾ��С��ĳ���ʱ�����������
     * @param date ָ��������
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
