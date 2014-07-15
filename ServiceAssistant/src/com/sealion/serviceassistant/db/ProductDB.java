package com.sealion.serviceassistant.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.sealion.serviceassistant.entity.ProductEntity;

import java.util.ArrayList;

/**
 * 产品信息数据库操作工具类
 *
 */
public class ProductDB extends DbHelper {
    private static final String TAG = ProductDB.class.getSimpleName();

    public ProductDB(Context context) {
        super(context);
    }

    /**
     * 获取产品总数
     *
     * @return 数量
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
                    size = cursor.getInt(cursor.getColumnIndex("count")); // 获取第一列的值,第一列的索引从0开始
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
     * 获取产品
     * @return 产品id
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
     * 获取产品
     * @return 产品id
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
     * 插入单体产品信息
     * @param pEntity 产品信息实例
     * @return 插入影响行数
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
     * 批量插入产品信息
     * @param pList 产品信息列表
     */
    public void batchInsert(ArrayList<ProductEntity> pList) {
        SQLiteDatabase db = null;
        ContentValues cv;
        try {
            db = this.getWritableDatabase();
            if (db != null) {
                db.beginTransaction();//手动设置开始事务
            }
            //数据插入操作循环
            for (ProductEntity pEntity : pList) {
                cv = new ContentValues();
                cv.put("productId", pEntity.getProductId());
                cv.put("productName", pEntity.getProductName());
                if (db != null) {
                    db.insert(PRODUCT_TABLE_NAME, null, cv);
                }
            }
            if (db != null) {
                db.setTransactionSuccessful();//设置事务处理成功，不设置会自动回滚不提交
                db.endTransaction();
            }
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * 更新产品信息
     * @param pEntity 更新的实体
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
     * 删除指定ID数据
     * @param productId 问题类型id
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
     * 删除所有问题类型数据
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
