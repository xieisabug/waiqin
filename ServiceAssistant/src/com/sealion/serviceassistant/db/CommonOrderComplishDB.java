package com.sealion.serviceassistant.db;

import com.sealion.serviceassistant.entity.CommonOrderComplishEntity;
import com.sealion.serviceassistant.tools.DateTools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @Title: CommonOrderComplishDB.java
 * @Package com.sealion.serviceassistant.db
 * @Description: 普通工单回执单
 * @author jack.lee titans.lee@gmail.com
 * @date 2013-2-17 上午9:20:46
 * @version V1.0 Copyright: Copyright (c)2012 Company: 湖南中恩通信技术有限公司
 */
public class CommonOrderComplishDB extends DbHelper
{
	private static final String TAG = CommonOrderComplishDB.class.getSimpleName();

	public CommonOrderComplishDB(Context context)
	{
		super(context);
	}

	/**
	 * 单个实体写入数据库
	 * @param cocEntity 插入的工单
	 * @return 插入数据的ID
	 */
	public long insert(CommonOrderComplishEntity cocEntity)
	{
		SQLiteDatabase db = null;
		try
		{
			db = this.getWritableDatabase();
			ContentValues cv = new ContentValues();

			cv.put("order_num", cocEntity.getOrder_num());
			cv.put("customer_name", cocEntity.getCustomer_name()); // 客户名称
			cv.put("customer_tax", cocEntity.getCustomer_tax()); // 税号
			cv.put("tax_officer", cocEntity.getTax_officer()); // 税务分局
			cv.put("service_address", cocEntity.getService_address()); // 上门地址
			cv.put("contact_name", cocEntity.getContact_name()); // 联系人
			cv.put("customer_tel", cocEntity.getCustomer_tel()); // 客户电话
			cv.put("customer_mobile", cocEntity.getCustomer_mobile()); // 客户手机
			cv.put("service_time", cocEntity.getService_time()); // 到达时间
			cv.put("number_value", cocEntity.getNumber_value()); // 单据号
			cv.put("customer_sign", cocEntity.getCustomer_sign()); // 客户签名
			cv.put("customer_charge", cocEntity.getCustomer_charge()); // 费用过程
			cv.put("customer_remark", cocEntity.getCustomer_remark()); // 备注
			cv.put("customer_solve", cocEntity.getCustomer_solve()); // 解决方式
			cv.put("software_type", cocEntity.getSoftware_type()); // 软件型号及编号
			cv.put("software_version", cocEntity.getSoftware_version()); // 软件版本号
			cv.put("software_env_value", cocEntity.getSoftware_env_value()); // 使用环境
			cv.put("visit_type", cocEntity.getVisit_type()); // 回访类型
			cv.put("visit_product_case", cocEntity.getVisit_product_case()); // 产品运行情况
			cv.put("visit_dispose_result", cocEntity.getVisit_dispose_result()); // 处理结果
			cv.put("is_send_to_server", cocEntity.getIs_send_to_server()); // 是否发往服务器修改信息
			cv.put("is_charge", cocEntity.getIs_charge()); // 是否收费 0:不收 1：收
			cv.put("is_charge_value", cocEntity.getIs_charge_value());// 收费金额
			cv.put("tec_name", cocEntity.getTec_name());// 技术工程师
			cv.put("service_evaluate", cocEntity.getService_evaluate());// 服务评价
																		// 2：很满意，1：满意，0：不满意
			cv.put("product_evaluate", cocEntity.getProduct_evaluate());// 产品评价2：很满意，1：满意，0：不满意
			cv.put("insert_time", DateTools.getFormatDateAndTime());
            long row = 0;
            if (db != null) {
                row = db.insert(COMMON_ORDER_COMPLISH_TABLE_NAME, null, cv);
            }
            return row;
		}
		finally
		{
			if (db != null)
			{
				db.close();
			}
		}
	}

    /**
     * 通过工单号码获取工单个数
     * @param order_num 工单号
     * @return 工单个数
     */
	public int getCountByOrder_num(String order_num)
	{
		SQLiteDatabase db = null;
		Cursor cursor = null;
		String sql = "select count(*) as count from " + COMMON_ORDER_COMPLISH_TABLE_NAME + " where order_num= '" + order_num + "'";
		int count = 0;
		try
		{
			db = this.getWritableDatabase();
			String[] selectionArgs = null;
			cursor = db.rawQuery(sql, selectionArgs);

			if (cursor.moveToNext())
			{
				count = cursor.getInt(cursor.getColumnIndex("count"));
			}
			return count;
		}
		finally
		{
			if (cursor != null)
			{
				cursor.close();
			}
			if (db != null)
			{
				db.close();
			}
		}
	}

	/**
	 * 根据工单号查询工单详细信息
	 * @param order_num 工单号
	 * @return 工单信息
	 */
	public CommonOrderComplishEntity SelectById(String order_num)
	{
		SQLiteDatabase db = null;
		Cursor cursor = null;
		CommonOrderComplishEntity item = null;
		String sql = "select id,order_num,customer_name,customer_tax,tax_officer,service_address,"
				+ " contact_name,customer_tel,customer_mobile,service_time,number_value,customer_sign," + " customer_charge,customer_remark,customer_solve,software_type,"
				+ " software_version,software_env_value,visit_type,visit_product_case,is_send_to_server,"
				+ " visit_dispose_result,is_charge,is_charge_value,tec_name,service_evaluate,product_evaluate " + "  from " + COMMON_ORDER_COMPLISH_TABLE_NAME
				+ " where order_num= '" + order_num + "'";

		try
		{
			db = this.getWritableDatabase();
			String[] selectionArgs = null;
			cursor = db.rawQuery(sql, selectionArgs);

			if (cursor.moveToNext())
			{
				item = new CommonOrderComplishEntity();
				item.setId(cursor.getInt(cursor.getColumnIndex("id"))); // 获取第一列的值,第一列的索引从0开始
				item.setOrder_num(cursor.getString(cursor.getColumnIndex("order_num")));
				item.setCustomer_name(cursor.getString(cursor.getColumnIndex("customer_name")));
				item.setCustomer_tax(cursor.getString(cursor.getColumnIndex("customer_tax")));
				item.setTax_officer(cursor.getString(cursor.getColumnIndex("tax_officer")));
				item.setService_time(cursor.getString(cursor.getColumnIndex("service_time")));
				item.setService_address(cursor.getString(cursor.getColumnIndex("service_address")));
				item.setContact_name(cursor.getString(cursor.getColumnIndex("contact_name")));
				item.setCustomer_tel(cursor.getString(cursor.getColumnIndex("customer_tel")));
				item.setCustomer_mobile(cursor.getString(cursor.getColumnIndex("customer_mobile")));
				item.setNumber_value(cursor.getString(cursor.getColumnIndex("number_value")));
				item.setCustomer_sign(cursor.getString(cursor.getColumnIndex("customer_sign")));
				item.setCustomer_charge(cursor.getString(cursor.getColumnIndex("customer_charge")));
				item.setCustomer_remark(cursor.getString(cursor.getColumnIndex("customer_remark")));
				item.setCustomer_solve(cursor.getInt(cursor.getColumnIndex("customer_solve")));
				item.setSoftware_type(cursor.getString(cursor.getColumnIndex("software_type")));
				item.setSoftware_version(cursor.getString(cursor.getColumnIndex("software_version")));
				item.setSoftware_env_value(cursor.getString(cursor.getColumnIndex("software_env_value")));
				item.setVisit_type(cursor.getString(cursor.getColumnIndex("visit_type")));
				item.setVisit_product_case(cursor.getString(cursor.getColumnIndex("visit_product_case")));
				item.setVisit_dispose_result(cursor.getString(cursor.getColumnIndex("visit_dispose_result")));
				item.setIs_send_to_server(cursor.getInt(cursor.getColumnIndex("is_send_to_server")));
				item.setIs_charge(cursor.getInt(cursor.getColumnIndex("is_charge")));
				item.setIs_charge_value(cursor.getString(cursor.getColumnIndex("is_charge_value")));
				item.setTec_name(cursor.getString(cursor.getColumnIndex("tec_name")));
				item.setService_evaluate(cursor.getInt(cursor.getColumnIndex("service_evaluate")));
				item.setProduct_evaluate(cursor.getInt(cursor.getColumnIndex("product_evaluate")));
			}
			return item;
		}
		finally
		{
			if (cursor != null)
			{
				cursor.close();
			}
			if (db != null)
			{
				db.close();
			}
		}
	}

    /**
     * 删除日期之前的信息
     * @param date 要删除的日期
     */
	public void deleteOldData(String date)
	{
		SQLiteDatabase db = null;
		try
		{
			db = this.getWritableDatabase();
			String sql = "delete from " + COMMON_ORDER_COMPLISH_TABLE_NAME + " where insert_time < '" + date + "'";
			db.execSQL(sql);
		}
		finally
		{
			if (db != null)
			{
				db.close();
			}
		}
	}
}
