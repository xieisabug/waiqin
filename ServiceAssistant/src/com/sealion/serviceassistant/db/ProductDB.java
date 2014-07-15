package com.sealion.serviceassistant.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.sealion.serviceassistant.entity.ProductEntity;

import java.util.ArrayList;

/**
 * ��Ʒ��Ϣ���ݿ����������
 *
 */
public class ProductDB extends DbHelper {
    private static final String TAG = ProductDB.class.getSimpleName();

    public ProductDB(Context context) {
        super(context);
    }

    /**
     * ��ȡ��Ʒ����
     *
     * @return ����
     */
    public int getCountData() {
        int size = 0;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String sql = "select count(*) as count from " + PRODUCT_TABLE_NAME;

        try {
            db = this.getWritableDatabase();
            if (db != null) {
                cursor = db.rawQuery(sql, null);
            }
            if (cursor != null) {
                if (cursor.moveToNext()) {
                    size = cursor.getInt(cursor.getColumnIndex("count")); // ��ȡ��һ�е�ֵ,��һ�е�������0��ʼ
                }
            }
            return size;
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
     * ��ȡ��Ʒ
     * @return ��Ʒid
     */
    public ProductEntity getProductById(int productId) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        ProductEntity item = null;
        String sql = "select id,productId,productName from " + PRODUCT_TABLE_NAME + " where productId="+productId;
        Log.d(TAG, sql);
        try {
            db = this.getWritableDatabase();
            if (db != null) {
                cursor = db.rawQuery(sql, null);
            }
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    item = new ProductEntity();
                    item.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    item.setProductId(cursor.getInt(cursor.getColumnIndex("productId")));
                    item.setProductName(cursor.getString(cursor.getColumnIndex("productName")));
                }
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
     * ��ȡ��Ʒ
     * @return ��Ʒid
     */
    public ArrayList<ProductEntity> getAllProduct() {
        ArrayList<ProductEntity> list;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        ProductEntity item;
        String sql = "select id,productId,productName from " + PRODUCT_TABLE_NAME;
        Log.d(TAG, sql);
        try {
            db = this.getWritableDatabase();
            if (db != null) {
                cursor = db.rawQuery(sql, null);
            }
            list = new ArrayList<ProductEntity>();
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    item = new ProductEntity();
                    item.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    item.setProductId(cursor.getInt(cursor.getColumnIndex("productId")));
                    item.setProductName(cursor.getString(cursor.getColumnIndex("productName")));
                    list.add(item);
                }
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
     * ���뵥���Ʒ��Ϣ
     * @param pEntity ��Ʒ��Ϣʵ��
     * @return ����Ӱ������
     */
    public long insert(ProductEntity pEntity) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("productId", pEntity.getProductId());
            cv.put("productName", pEntity.getProductName());
            if (db != null) {
                return db.insert(PRODUCT_TABLE_NAME, null, cv);
            }
            return 0;
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * ���������Ʒ��Ϣ
     * @param pList ��Ʒ��Ϣ�б�
     */
    public void batchInsert(ArrayList<ProductEntity> pList) {
        SQLiteDatabase db = null;
        ContentValues cv;
        try {
            db = this.getWritableDatabase();
            if (db != null) {
                db.beginTransaction();//�ֶ����ÿ�ʼ����
            }
            //���ݲ������ѭ��
            for (ProductEntity pEntity : pList) {
                cv = new ContentValues();
                cv.put("productId", pEntity.getProductId());
                cv.put("productName", pEntity.getProductName());
                if (db != null) {
                    db.insert(PRODUCT_TABLE_NAME, null, cv);
                }
            }
            if (db != null) {
                db.setTransactionSuccessful();//����������ɹ��������û��Զ��ع����ύ
                db.endTransaction();
            }
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * ���²�Ʒ��Ϣ
     * @param pEntity ���µ�ʵ��
     */
    public void updateData(ProductEntity pEntity) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            String where = "productId=?";
            String[] whereValue = {String.valueOf(pEntity.getProductId())};
            ContentValues cv = new ContentValues();
            cv.put("productName", pEntity.getProductName());
            if (db != null) {
                db.update(PRODUCT_TABLE_NAME, cv, where, whereValue);
            }
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * ɾ��ָ��ID����
     * @param productId ��������id
     */
    public void deleteData(int productId) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            String sql = "delete from " + PRODUCT_TABLE_NAME + "where productId=" + productId;
            if (db != null) {
                db.execSQL(sql);
            }
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * ɾ������������������
     */
    public void deleteAllData() {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            String sql = "delete from " + PRODUCT_TABLE_NAME;
            if (db != null) {
                db.execSQL(sql);
            }
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }
}
