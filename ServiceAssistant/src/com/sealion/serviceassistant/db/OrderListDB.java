package com.sealion.serviceassistant.db;

import java.util.ArrayList;

import com.sealion.serviceassistant.entity.OrderEntity;
import com.sealion.serviceassistant.tools.FinalVariables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * <p>Title: OrderListDB.java</p>
 * <p>Description: 记录工单信息表</p>
 * <p>Copyright: hn Copyright (c) 2012</p>
 * <p>Company: hn</p>
 *
 * @author jack.lee E-mail: titans.lee@gmail.com
 * @version 创建时间：2012-8-13 上午10:07:25
 *          类说明 所有工单的详细信息，包括按页查找，按照ID查询工单详细信息，增加，删除等操作
 */
public class OrderListDB extends DbHelper {
    private static final String TAG = "com.sealion.servicemanage.db.OrderListDB";

    public static final int NOT_URGENCY = 1;
    public static final int URGENCY = 2;

    public OrderListDB(Context context) {
        super(context);
    }

    // 根据条件分页查找

    /**
     * 分页查找工单信息
     *
     * @param pageID   页码
     * @param position 查询的工单类型
     * @return 查询的工单list
     */
    public ArrayList<OrderEntity> getDataByPage(int pageID, int position) {
        SQLiteDatabase db = null;
        ArrayList<OrderEntity> data;
        Cursor cursor = null;
        String sql = "";
        if (position == FinalVariables.ORDER_TYPE_ALL)//显示全部工单
        {
            sql = "select id,workCardId,workOrderType,order_sign,customerName," +
                    " receiveOrderTime,orderToken from " + ORDER_LIST_TABLE_NAME
                    + " order by order_sign,receiveOrderTime asc Limit " + PAGE_SIZE + " Offset "
                    + String.valueOf(pageID * PAGE_SIZE);
        } else if (position == FinalVariables.ORDER_TYPE_NEW) //显示新工单
        {
            sql = "select id,workCardId,workOrderType,order_sign,customerName," +
                    " receiveOrderTime,orderToken from " + ORDER_LIST_TABLE_NAME
                    + "  where urgency = "+NOT_URGENCY+" and order_sign = " + FinalVariables.ORDER_STATE_NOT_READ
                    + " order by receiveOrderTime desc Limit " + PAGE_SIZE + " Offset "
                    + String.valueOf(pageID * PAGE_SIZE);
        } else if (position == FinalVariables.ORDER_TYPE_NOT_COMPLISH)//显示未完成工单
        {
            sql = "select id,workCardId,workOrderType,order_sign,customerName," +
                    " receiveOrderTime,orderToken from " + ORDER_LIST_TABLE_NAME
                    + " where order_sign != " + FinalVariables.ORDER_STATE_ORDER_COMPLISH
                    + " order by receiveOrderTime desc Limit " + PAGE_SIZE + " Offset "
                    + String.valueOf(pageID * PAGE_SIZE);
        } else if (position == FinalVariables.ORDER_TYPE_COMPLISH)//显示已完成工单
        {
            sql = "select id,workCardId,workOrderType,order_sign,customerName," +
                    " receiveOrderTime,orderToken from " + ORDER_LIST_TABLE_NAME
                    + " where order_sign = " + FinalVariables.ORDER_STATE_ORDER_COMPLISH
                    + " order by id desc Limit " + PAGE_SIZE + " Offset "
                    + String.valueOf(pageID * PAGE_SIZE);
        } else if (position == FinalVariables.ORDER_TYPE_URGENCY)//显示紧急工单
        {
            sql = "select id,workCardId,workOrderType,order_sign,customerName," +
                    " receiveOrderTime,orderToken from " + ORDER_LIST_TABLE_NAME
                    + " where order_sign != " + FinalVariables.ORDER_STATE_ORDER_COMPLISH
                    + "  and urgency = "+URGENCY+" order by id desc Limit " + PAGE_SIZE + " Offset "
                    + String.valueOf(pageID * PAGE_SIZE);
        } else if (position == FinalVariables.ORDER_TYPE_BACK) //回访工单
        {
            sql = "select id,workCardId,workOrderType,order_sign,customerName," +
                    " receiveOrderTime,orderToken from " + ORDER_LIST_TABLE_NAME
                    + " where  workOrderType = 2 order by order_sign,receiveOrderTime asc Limit " + PAGE_SIZE + " Offset "
                    + String.valueOf(pageID * PAGE_SIZE);
        } else if (position == FinalVariables.ORDER_TYPE_MAINTAIN) //维护工单
        {
            sql = "select id,workCardId,workOrderType,order_sign,customerName," +
                    " receiveOrderTime,orderToken from " + ORDER_LIST_TABLE_NAME
                    + " where  workOrderType = 1 order by order_sign,receiveOrderTime asc Limit " + PAGE_SIZE + " Offset "
                    + String.valueOf(pageID * PAGE_SIZE);
        }
        Log.d(TAG, sql);
        try {
            db = this.getWritableDatabase();
            if (db != null) {
                cursor = db.rawQuery(sql, null);
            }

            data = new ArrayList<OrderEntity>();
            OrderEntity item;

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    item = new OrderEntity();

                    item.setId(cursor.getInt(cursor.getColumnIndex("id"))); // 获取第一列的值,第一列的索引从0开始
                    item.setWorkCardId(cursor.getLong(cursor.getColumnIndex("workCardId"))); //工单号
                    item.setOrder_sign(cursor.getInt(cursor.getColumnIndex("order_sign")));//工单状态标记
                    item.setReceiveOrderTime(cursor.getString(cursor.getColumnIndex("receiveOrderTime"))); //派单时间
                    item.setCustomerName(cursor.getString(cursor.getColumnIndex("customerName"))); //客户名称
                    item.setWorkOrderType(cursor.getInt(cursor.getColumnIndex("workOrderType")));
                    item.setOrderToken(cursor.getString(cursor.getColumnIndex("orderToken")));//单据号
                    data.add(item);
                }
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
     * 单个实体写入数据库
     *
     * @param oEntity 插入的工单实体
     * @return 插入成功返回的id
     */
    public long insert(OrderEntity oEntity) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("workCardId", oEntity.getWorkCardId());    //派工单ID int型(必填)
            cv.put("serviceDate", oEntity.getServiceDate());    //服务日期(格式:yyyyMMddHHmmss) (必填)
            cv.put("workOrderType", oEntity.getWorkOrderType());    //派工类别,int型 0：直接派工；2：回访派工(必填)
            cv.put("customerId", oEntity.getCustomerId());    //客户ID,int型(必填)
            cv.put("linkPerson", oEntity.getLinkPerson());    //客户联系人 (必填)
            cv.put("customerName", oEntity.getCustomerName());    //客户公司名称(必填)
            cv.put("taxCode", oEntity.getTaxCode());    //税号(可填)
            cv.put("departmentId", oEntity.getDepartmentId());    //所属部门ID,int型(必填)
            cv.put("departmentName", oEntity.getDepartmentName());    //所属部门名称(必填)
            cv.put("revenueId", oEntity.getRevenueId());        //所属税务分局ID,int型(必填)
            cv.put("revenueName", oEntity.getRevenueName());    //所属税务分局名称(必填)
            cv.put("customerAddr", oEntity.getCustomerAddr());    //上门地址(必填)
            cv.put("customerMobile", oEntity.getCustomerMobile());    //客户手机(必填)
            cv.put("customerTel", oEntity.getCustomerTel());    //客户联系电话(必填)
            cv.put("customerCounty", oEntity.getCustomerCounty());    //客户所在区、县(可填)
            cv.put("customerLatitude", oEntity.getCustomerLatitude());    //客户地址所在纬度(可填)
            cv.put("customerLongitude", oEntity.getCustomerLongitude());    //客户地址所在经度(可填)
            cv.put("fieldWorkerId", oEntity.getFieldWorkerId());    //服务人员ID,int型(必填)
            cv.put("fieldWorkerName", oEntity.getFieldWorkerName()); //服务人员姓名(必填)
            cv.put("workOrderDescription", oEntity.getWorkOrderDescription()); //派工说明(可填)
            cv.put("expectArriveTime", oEntity.getExpectArriveTime()); //服务人员预计到达时间(格式:yyyyMMddHHmmss)(可填)
            cv.put("productName", oEntity.getProductName()); //所属产品名称
            cv.put("chargeType", oEntity.getChargeType()); //是否收费(1:是,2:否) int(必填)
            cv.put("chargeMoney", oEntity.getChargeMoney()); //收费金额(可填)
            cv.put("urgency", oEntity.getUrgency()); //工单的紧急程度(0:一般,1:紧急),int(必填)
            cv.put("city", oEntity.getCity()); //客户所在城市(城市名称，比如长沙市，湘潭市，湘西自治州)(必填)
            cv.put("order_sign", oEntity.getOrder_sign()); //工单状态标记（0：未阅读，1:已经阅读，2:接受任务，
            // 3：申请改派，4：改派审批通过，5：改派审批未通过，
            // 6：到达现场，7:电话解决  10：工单完成）
            cv.put("upload_sign", oEntity.getUpload_sign()); //0：未上传任务，1：任务上传完成
            cv.put("finish_type", oEntity.getFinish_type()); //工单完成类型  默认为0， 1：现场解决 ，2：电话解决，3，改派
            cv.put("is_task", oEntity.getIs_task()); //是否拥有任务要上传 0:没有，1：有
            cv.put("receiveOrderTime", oEntity.getReceiveOrderTime()); //工单接收时间
            cv.put("orderToken", oEntity.getOrderToken());//单据号
            cv.put("visit_type", oEntity.getVisit_type()); //回访类型
            cv.put("product_status", oEntity.getProduct_status()); //产品状态
            cv.put("handle_result", oEntity.getHandle_result()); //处理结果
            cv.put("optimal_path", oEntity.getOptimal_path());//最优路径
            long row = 0;
            if (db != null) {
                row = db.insert(ORDER_LIST_TABLE_NAME, null, cv);
            }
            //long row=1;
            return row;
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * 批量写入数据库
     *
     * @param orderList 批量写入的实体
     */
    public void addOrderList(ArrayList<OrderEntity> orderList) {
        SQLiteDatabase db = null;
        ContentValues cv;
        try {
            db = this.getWritableDatabase();
            if (db != null) {
                db.beginTransaction();//手动设置开始事务
            }
            //数据插入操作循环
            for (OrderEntity oEntity : orderList) {
                cv = new ContentValues();
                cv.put("workCardId", oEntity.getWorkCardId());    //派工单ID int型(必填)
                cv.put("serviceDate", oEntity.getServiceDate());    //服务日期(格式:yyyyMMddHHmmss) (必填)
                cv.put("workOrderType", oEntity.getWorkOrderType());    //派工类别,int型 0：直接派工；2：回访派工(必填)
                cv.put("customerId", oEntity.getCustomerId());    //客户ID,int型(必填)
                cv.put("linkPerson", oEntity.getLinkPerson());    //客户联系人 (必填)
                cv.put("customerName", oEntity.getCustomerName());    //客户公司名称(必填)
                cv.put("taxCode", oEntity.getTaxCode());    //税号(可填)
                cv.put("departmentId", oEntity.getDepartmentId());    //所属部门ID,int型(必填)
                cv.put("departmentName", oEntity.getDepartmentName());    //所属部门名称(必填)
                cv.put("revenueId", oEntity.getRevenueId());        //所属税务分局ID,int型(必填)
                cv.put("revenueName", oEntity.getRevenueName());    //所属税务分局名称(必填)
                cv.put("customerAddr", oEntity.getCustomerAddr());    //上门地址(必填)
                cv.put("customerMobile", oEntity.getCustomerMobile());    //客户手机(必填)
                cv.put("customerTel", oEntity.getCustomerTel());    //客户联系电话(必填)
                cv.put("customerCounty", oEntity.getCustomerCounty());    //客户所在区、县(可填)
                cv.put("customerLatitude", oEntity.getCustomerLatitude());    //客户地址所在纬度(可填)
                cv.put("customerLongitude", oEntity.getCustomerLongitude());    //客户地址所在经度(可填)
                cv.put("fieldWorkerId", oEntity.getFieldWorkerId());    //服务人员ID,int型(必填)
                cv.put("fieldWorkerName", oEntity.getFieldWorkerName()); //服务人员姓名(必填)
                cv.put("workOrderDescription", oEntity.getWorkOrderDescription()); //派工说明(可填)
                cv.put("expectArriveTime", oEntity.getExpectArriveTime()); //服务人员预计到达时间(格式:yyyyMMddHHmmss)(可填)
                cv.put("productName", oEntity.getProductName()); //所属产品名称
                cv.put("chargeType", oEntity.getChargeType()); //是否收费(1:是,2:否) int(必填)
                cv.put("chargeMoney", oEntity.getChargeMoney()); //收费金额(可填)
                cv.put("urgency", oEntity.getUrgency()); ////工单的紧急程度(0:一般,1:紧急),int(必填)
                cv.put("city", oEntity.getCity()); //客户所在城市(城市名称，比如长沙市，湘潭市，湘西自治州)(必填)
                cv.put("order_sign", oEntity.getOrder_sign()); //工单状态标记（0：未阅读，1:已经阅读，2:接受任务，
                // 3：申请改派，4：改派审批通过，5：改派审批未通过，
                // 6：到达现场，7:电话解决  10：工单完成）
                cv.put("upload_sign", oEntity.getUpload_sign()); //0：未上传任务，1：任务上传完成
                cv.put("finish_type", oEntity.getFinish_type()); //工单完成类型  默认为0， 1：现场解决 ，2：电话解决，3，改派
                cv.put("is_task", oEntity.getIs_task()); //是否拥有任务要上传 0:没有，1：有
                cv.put("receiveOrderTime", oEntity.getReceiveOrderTime()); //工单接收时间
                cv.put("orderToken", oEntity.getOrderToken());//单据号
                cv.put("visit_type", oEntity.getVisit_type()); //回访类型
                cv.put("product_status", oEntity.getProduct_status()); //产品状态
                cv.put("handle_result", oEntity.getHandle_result()); //处理结果
                cv.put("optimal_path", oEntity.getOptimal_path());//最优路径
                if (db != null) {
                    db.insert(ORDER_LIST_TABLE_NAME, null, cv);
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
     * 通过工单号查询所属产品名称
     *
     * @param orderId 工单号
     * @return 产品名称
     */
    public String SelectProductNameByOrderID(String orderId) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String productName = "";
        String sql = "select productName from " + ORDER_LIST_TABLE_NAME
                + " where workCardId= " + orderId;

        try {
            db = this.getWritableDatabase();
            if (db != null) {
                cursor = db.rawQuery(sql, null);
            }

            if (cursor != null) {
                if (cursor.moveToNext()) {
                    productName = cursor.getString(cursor.getColumnIndex("productName"));
                }
            }
            return productName;
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
     * 根据ID查询工单详细信息
     *
     * @param id 工单id
     * @return 工单实体信息
     */
    public OrderEntity SelectById(int id) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        OrderEntity item = new OrderEntity();
        String sql = "select id,workCardId,serviceDate,workOrderType,customerId,linkPerson,customerName," +
                " taxCode,departmentId,departmentName,revenueId,revenueName,customerAddr,customerMobile," +
                " customerTel,customerCounty,customerLatitude,customerLongitude,fieldWorkerId,fieldWorkerName," +
                " workOrderDescription,expectArriveTime,productName,chargeType,chargeMoney,urgency,city," +
                " order_sign,upload_sign,finish_type,is_task,receiveOrderTime,orderToken," +
                " visit_type,product_status,handle_result,optimal_path from " + ORDER_LIST_TABLE_NAME
                + " where id= " + id;

        try {
            db = this.getWritableDatabase();
            if (db != null) {
                cursor = db.rawQuery(sql, null);
            }

            if (cursor != null) {
                if (cursor.moveToNext()) {
                    item.setId(cursor.getInt(cursor.getColumnIndex("id"))); // 获取第一列的值,第一列的索引从0开始
                    item.setWorkCardId(cursor.getLong(cursor.getColumnIndex("workCardId")));    //派工单ID int型(必填)
                    item.setServiceDate(cursor.getString(cursor.getColumnIndex("serviceDate")));    //服务日期(格式:yyyyMMddHHmmss) (必填)
                    item.setWorkOrderType(cursor.getInt(cursor.getColumnIndex("workOrderType")));    //派工类别,int型 0：直接派工；2：回访派工(必填)
                    item.setCustomerId(cursor.getInt(cursor.getColumnIndex("customerId")));    //客户ID,int型(必填)
                    item.setLinkPerson(cursor.getString(cursor.getColumnIndex("linkPerson")));    //客户联系人 (必填)
                    item.setCustomerName(cursor.getString(cursor.getColumnIndex("customerName")));    //客户公司名称(必填)
                    item.setTaxCode(cursor.getString(cursor.getColumnIndex("taxCode")));    //税号(可填)
                    item.setDepartmentId(cursor.getInt(cursor.getColumnIndex("departmentId")));    //所属部门ID,int型(必填)
                    item.setDepartmentName(cursor.getString(cursor.getColumnIndex("departmentName")));    //所属部门名称(必填)
                    item.setRevenueId(cursor.getInt(cursor.getColumnIndex("revenueId")));        //所属税务分局ID,int型(必填)
                    item.setRevenueName(cursor.getString(cursor.getColumnIndex("revenueName")));    //所属税务分局名称(必填)
                    item.setCustomerAddr(cursor.getString(cursor.getColumnIndex("customerAddr")));    //上门地址(必填)
                    item.setCustomerMobile(cursor.getString(cursor.getColumnIndex("customerMobile")));    //客户手机(必填)
                    item.setCustomerTel(cursor.getString(cursor.getColumnIndex("customerTel")));    //客户联系电话(必填)
                    item.setCustomerCounty(cursor.getString(cursor.getColumnIndex("customerCounty")));    //客户所在区、县(可填)
                    item.setCustomerLatitude(cursor.getString(cursor.getColumnIndex("customerLatitude")));    //客户地址所在纬度(可填)
                    item.setCustomerLongitude(cursor.getString(cursor.getColumnIndex("customerLongitude")));    //客户地址所在经度(可填)
                    item.setFieldWorkerId(cursor.getInt(cursor.getColumnIndex("fieldWorkerId")));    //服务人员ID,int型(必填)
                    item.setFieldWorkerName(cursor.getString(cursor.getColumnIndex("fieldWorkerName"))); //服务人员姓名(必填)
                    item.setWorkOrderDescription(cursor.getString(cursor.getColumnIndex("workOrderDescription"))); //派工说明(可填)
                    item.setExpectArriveTime(cursor.getString(cursor.getColumnIndex("expectArriveTime"))); //服务人员预计到达时间(格式:yyyyMMddHHmmss)(可填)
                    item.setProductName(cursor.getString(cursor.getColumnIndex("productName"))); //所属产品名称(回访单)
                    item.setChargeType(cursor.getInt(cursor.getColumnIndex("chargeType"))); //是否收费(1:是,2:否) int(必填)
                    item.setChargeMoney(cursor.getString(cursor.getColumnIndex("chargeMoney"))); //收费金额(可填)
                    item.setUrgency(cursor.getInt(cursor.getColumnIndex("urgency"))); ////工单的紧急程度(1:一般,2:紧急),int(必填)
                    item.setCity(cursor.getString(cursor.getColumnIndex("city"))); //客户所在城市(城市名称，比如长沙市，湘潭市，湘西自治州)(必填)
                    item.setOrder_sign(cursor.getInt(cursor.getColumnIndex("order_sign"))); //工单状态标记（0：未阅读，1:已经阅读，2:接受任务， 3：申请改派，4：改派审批通过，5：改派审批未通过， 6：到达现场，7:电话解决  10：工单完成）
                    item.setUpload_sign(cursor.getInt(cursor.getColumnIndex("upload_sign"))); //0：未上传任务，1：任务上传完成
                    item.setFinish_type(cursor.getInt(cursor.getColumnIndex("finish_type"))); //工单完成类型  默认为0， 1：现场解决 ，2：电话解决，3，改派, 4.回访工单
                    item.setIs_task(cursor.getInt(cursor.getColumnIndex("is_task"))); //是否拥有任务要上传 0:没有，1：有
                    item.setReceiveOrderTime(cursor.getString(cursor.getColumnIndex("receiveOrderTime"))); //工单接收时间
                    item.setOrderToken(cursor.getString(cursor.getColumnIndex("orderToken")));
                    item.setVisit_type(cursor.getString(cursor.getColumnIndex("visit_type")));
                    item.setProduct_status(cursor.getString(cursor.getColumnIndex("product_status")));
                    item.setHandle_result(cursor.getString(cursor.getColumnIndex("handle_result")));
                    item.setOptimal_path(cursor.getString(cursor.getColumnIndex("optimal_path")));
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
     * 根据workCardId查询工单详细信息,避免重复
     *
     * @param workCardId 派工单id
     * @return 是否有重复
     */
    public boolean SelectByOrderId(long workCardId) {
        boolean result = false;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String sql = "select * from " + ORDER_LIST_TABLE_NAME + " where workCardId= '" + workCardId + "' ";
        try {
            db = this.getWritableDatabase();
            if (db != null) {
                cursor = db.rawQuery(sql, null);
            }

            if (cursor != null) {
                if (cursor.moveToNext()) {
                    result = true;
                }
            }

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return result;
    }

    /**
     * 按照新工单，读取5个最新的未读，未完成工单
     *
     * @return 工单列表
     */
    public ArrayList<OrderEntity> get5ConditionData() {
        SQLiteDatabase db = null;
        ArrayList<OrderEntity> data;
        Cursor cursor = null;
        String sql = "select id,workCardId,order_sign," +
                " receiveOrderTime from " + ORDER_LIST_TABLE_NAME
                + " where (order_sign < " + FinalVariables.ORDER_STATE_ARRIVE_TARGET + " && order_sign != "
                + FinalVariables.ORDER_STATE_APPLY_CHANGE + ") order by receiveOrderTime desc Limit 5 ";
        try {
            db = this.getWritableDatabase();
            if (db != null) {
                cursor = db.rawQuery(sql, null);
            }

            data = new ArrayList<OrderEntity>();
            OrderEntity item;

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    item = new OrderEntity();

                    item.setId(cursor.getInt(cursor.getColumnIndex("id"))); // 获取第一列的值,第一列的索引从0开始
                    item.setWorkCardId(cursor.getLong(cursor.getColumnIndex("workCardId"))); //工单号
                    item.setOrder_sign(cursor.getInt(cursor.getColumnIndex("order_sign")));//工单状态标记
                    item.setReceiveOrderTime(cursor.getString(cursor.getColumnIndex("receiveOrderTime"))); //派单时间
                    //item.setQ_type(cursor.getString(cursor.getColumnIndex("q_type"))); //问题类别
                    data.add(item);
                }
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
     * 根据条件来统计工单
     *
     * @param type 类型
     * @return 工单数量
     */
    public int CountOrdersByCondition(int type) {
        SQLiteDatabase db = null;
        int result = 0;
        Cursor cursor = null;

        String sql = "";

        //统计未完成的紧急工单数
        if (type == 0) {
            sql = "select count(*) as result from " + ORDER_LIST_TABLE_NAME + " where (" +
                    "  order_sign != " + FinalVariables.ORDER_STATE_ORDER_COMPLISH + ") "
                    + " and urgency = " + URGENCY;
        } else if (type == 1)//统计所有的新工单数
        {
            sql = "select count(*) as result from " + ORDER_LIST_TABLE_NAME + " where urgency = " + NOT_URGENCY + " and order_sign = "
                    + FinalVariables.ORDER_STATE_NOT_READ;
        } else if (type == 2)//统计所有的未完成工单数
        {
            sql = "select count(*) as result from " + ORDER_LIST_TABLE_NAME + " where ( order_sign != "
                    + FinalVariables.ORDER_STATE_ORDER_COMPLISH + ")  ";
        } else if (type == 3)//统计所有完成工单数
        {

            sql = "select count(*) as result from " + ORDER_LIST_TABLE_NAME + " where ( order_sign = "
                    + FinalVariables.ORDER_STATE_ORDER_COMPLISH + ")  ";
        } else if (type == 4) //统计回访工单数
        {
            sql = "select count(*) as result from " + ORDER_LIST_TABLE_NAME + " where ( workOrderType = 2)  ";
        } else if (type == 5) {
            sql = "select count(*) as result from " + ORDER_LIST_TABLE_NAME + " where ( workOrderType = 1)  ";
        }

        try {
            db = this.getWritableDatabase();

            if (db != null) {
                cursor = db.rawQuery(sql, null);
            }
            if (cursor != null) {
                if (cursor.moveToNext()) {
                    result = cursor.getInt(cursor.getColumnIndex("result"));
                }
            }
            return result;
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
     * 更新工单状态标志
     * @param order_num   工单号码
     * @param order_state 工单状态
     */
    public void updateSignData(String order_num, int order_state) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            String where = "workCardId=?";
            String[] whereValue = {order_num};
            ContentValues cv = new ContentValues();

            if (order_state == FinalVariables.ORDER_STATE_NOT_READ) {
                cv.put("order_sign", FinalVariables.ORDER_STATE_NOT_READ);
            } else if (order_state == FinalVariables.ORDER_STATE_IS_READ) {
                cv.put("order_sign", FinalVariables.ORDER_STATE_IS_READ);
            } else if (order_state == FinalVariables.ORDER_STATE_ACCEPT_TASK) {
                cv.put("order_sign", FinalVariables.ORDER_STATE_ACCEPT_TASK);
            } else if (order_state == FinalVariables.ORDER_STATE_APPLY_CHANGE) {
                cv.put("order_sign", FinalVariables.ORDER_STATE_APPLY_CHANGE);
            }
//			else if (order_state == FinalVariables.ORDER_STATE_APPLY_NOT_PASS)
//			{
//				sql = sql + FinalVariables.ORDER_STATE_APPLY_NOT_PASS;
//			}
//			else if (order_state == FinalVariables.ORDER_STATE_APPLY_PASS)
//			{
//				sql = sql + FinalVariables.ORDER_STATE_APPLY_PASS;
//			}
            else if (order_state == FinalVariables.ORDER_STATE_ARRIVE_TARGET) {
                cv.put("order_sign", FinalVariables.ORDER_STATE_ARRIVE_TARGET);
            } else if (order_state == FinalVariables.ORDER_STATE_ORDER_COMPLISH) {
                cv.put("order_sign", FinalVariables.ORDER_STATE_ORDER_COMPLISH);
            } else if (order_state == FinalVariables.ORDER_STATE_PHONE_SOLVE) {
                cv.put("order_sign", FinalVariables.ORDER_STATE_PHONE_SOLVE);
            }
            if (db != null) {
                db.update(ORDER_LIST_TABLE_NAME, cv, where, whereValue);
            }
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * 更新工单状态标志
     * @param order_num   工单号码
     * @param finish_type 结束类型
     */
    public void updateFinishType(String order_num, int finish_type) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            String where = "workCardId=?";
            String[] whereValue = {order_num};
            ContentValues cv = new ContentValues();
            if (finish_type == FinalVariables.ORDER_FINISH_TYPE_SPOT_SOLVE) {
                cv.put("finish_type", FinalVariables.ORDER_FINISH_TYPE_SPOT_SOLVE); //现场解决
            } else if (finish_type == FinalVariables.ORDER_FINISH_TYPE_PHONE_SOLVE) //电话解决
            {
                cv.put("finish_type", FinalVariables.ORDER_FINISH_TYPE_PHONE_SOLVE);
            } else if (finish_type == FinalVariables.ORDER_FINISH_TYPE_CHANGE_SOLVE) //改派
            {
                cv.put("finish_type", FinalVariables.ORDER_FINISH_TYPE_CHANGE_SOLVE);
            } else if (finish_type == FinalVariables.ORDER_FINISH_TYPE_VISIT_SOLVE) //回访工单
            {
                cv.put("finish_type", FinalVariables.ORDER_FINISH_TYPE_VISIT_SOLVE);
            }
            cv.put("order_sign", FinalVariables.ORDER_STATE_ORDER_COMPLISH); //完成工单

            if (db != null) {
                db.update(ORDER_LIST_TABLE_NAME, cv, where, whereValue);
            }
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * 模糊条件查询
     * @param condition 内容
     * @return 查询到的工单实体
     */
    public ArrayList<OrderEntity> getDataByCondition(String condition) {
        SQLiteDatabase db = null;
        ArrayList<OrderEntity> data;
        Cursor cursor = null;
        String sql = "select id,workCardId,order_sign," +
                " receiveOrderTime,customerName,workOrderType,orderToken from " + ORDER_LIST_TABLE_NAME
                + " where (workCardId like '%" + condition + "%' or customerName like '%" + condition + "%')";
        Log.d(TAG, sql);
        try {
            db = this.getWritableDatabase();
            if (db != null) {
                cursor = db.rawQuery(sql, null);
            }

            data = new ArrayList<OrderEntity>();
            OrderEntity item;

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    item = new OrderEntity();

                    item.setId(cursor.getInt(cursor.getColumnIndex("id"))); // 获取第一列的值,第一列的索引从0开始
                    item.setWorkCardId(cursor.getLong(cursor.getColumnIndex("workCardId"))); //工单号
                    item.setOrder_sign(cursor.getInt(cursor.getColumnIndex("order_sign")));//工单状态标记
                    item.setReceiveOrderTime(cursor.getString(cursor.getColumnIndex("receiveOrderTime"))); //派单时间
                    item.setCustomerName(cursor.getString(cursor.getColumnIndex("customerName")));
                    item.setWorkOrderType(cursor.getInt(cursor.getColumnIndex("workOrderType")));
                    item.setOrderToken(cursor.getString(cursor.getColumnIndex("orderToken")));
                    data.add(item);
                }
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
     * 获取没有上传的记录
     * @return 查询到的工单实体list
     */
    public ArrayList<OrderEntity> getDataByNoUpload() {
        SQLiteDatabase db = null;
        ArrayList<OrderEntity> data;
        Cursor cursor = null;

        String sql = "select id,workCardId,order_sign," +
                " receiveOrderTime,orderToken,workOrderType,customerName from " + ORDER_LIST_TABLE_NAME
                + " where upload_sign = 0 and is_task = 1 and order_sign = "
                + FinalVariables.ORDER_STATE_ORDER_COMPLISH;

        try {
            db = this.getWritableDatabase();
            if (db != null) {
                cursor = db.rawQuery(sql, null);
            }

            data = new ArrayList<OrderEntity>();
            OrderEntity item;

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    item = new OrderEntity();

                    item.setId(cursor.getInt(cursor.getColumnIndex("id"))); // 获取第一列的值,第一列的索引从0开始
                    item.setWorkCardId(cursor.getLong(cursor.getColumnIndex("workCardId"))); //工单号
                    item.setOrder_sign(cursor.getInt(cursor.getColumnIndex("order_sign")));//工单状态标记
                    item.setReceiveOrderTime(cursor.getString(cursor.getColumnIndex("receiveOrderTime"))); //派单时间
                    item.setOrderToken(cursor.getString(cursor.getColumnIndex("orderToken")));
                    item.setWorkOrderType(cursor.getInt(cursor.getColumnIndex("workOrderType")));
                    item.setCustomerName(cursor.getString(cursor.getColumnIndex("customerName")));
                    //item.setQ_content(cursor.getString(cursor.getColumnIndex("q_content"))); //问题类别
                    data.add(item);
                }
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
     * 更新工单任务状态标志
     * @param order_num 工单号
     */
    public void updateUploadTaskSign(String order_num) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            if (db != null) {
                db.execSQL("update " + ORDER_LIST_TABLE_NAME +
                        " set is_task = 1 where workCardId='" + order_num + "'");
            }
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * 更新工单文件上传标识
     * @param order_num 工单id
     */
    public void updateUploadFinishType(String order_num) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            if (db != null) {
                db.execSQL("update " + ORDER_LIST_TABLE_NAME +
                        " set upload_sign = 1 where workCardId='" + order_num + "'");
            }
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }


    /**
     * 根据ID查询工单状态标记
     * @param order_num 工单id
     * @return 工单状态标记
     */
    public int selectOrderSignById(String order_num) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        int result = 0;
        String sql = "select order_sign from " + ORDER_LIST_TABLE_NAME
                + " where workCardId= '" + order_num + "'";

        try {
            db = this.getWritableDatabase();
            if (db != null) {
                cursor = db.rawQuery(sql, null);
            }

            if (cursor != null) {
                if (cursor.moveToNext()) {
                    result = cursor.getInt(cursor.getColumnIndex("order_sign")); // 获取第一列的值,第一列的索引从0开始
                }
            }
            return result;
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
     * 删除小于某一天的数据
     * @param date 指定的日期
     */
    public void deleteOldData(String date) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            String sql = "delete from " + ORDER_LIST_TABLE_NAME + " where receiveOrderTime < '" + date + "'";
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
