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
 * <p>Description: ��¼������Ϣ��</p>
 * <p>Copyright: hn Copyright (c) 2012</p>
 * <p>Company: hn</p>
 *
 * @author jack.lee E-mail: titans.lee@gmail.com
 * @version ����ʱ�䣺2012-8-13 ����10:07:25
 *          ��˵�� ���й�������ϸ��Ϣ��������ҳ���ң�����ID��ѯ������ϸ��Ϣ�����ӣ�ɾ���Ȳ���
 */
public class OrderListDB extends DbHelper {
    private static final String TAG = "com.sealion.servicemanage.db.OrderListDB";

    public static final int NOT_URGENCY = 1;
    public static final int URGENCY = 2;

    public OrderListDB(Context context) {
        super(context);
    }

    // ����������ҳ����

    /**
     * ��ҳ���ҹ�����Ϣ
     *
     * @param pageID   ҳ��
     * @param position ��ѯ�Ĺ�������
     * @return ��ѯ�Ĺ���list
     */
    public ArrayList<OrderEntity> getDataByPage(int pageID, int position) {
        SQLiteDatabase db = null;
        ArrayList<OrderEntity> data;
        Cursor cursor = null;
        String sql = "";
        if (position == FinalVariables.ORDER_TYPE_ALL)//��ʾȫ������
        {
            sql = "select id,workCardId,workOrderType,order_sign,customerName," +
                    " receiveOrderTime,orderToken from " + ORDER_LIST_TABLE_NAME
                    + " order by order_sign,receiveOrderTime asc Limit " + PAGE_SIZE + " Offset "
                    + String.valueOf(pageID * PAGE_SIZE);
        } else if (position == FinalVariables.ORDER_TYPE_NEW) //��ʾ�¹���
        {
            sql = "select id,workCardId,workOrderType,order_sign,customerName," +
                    " receiveOrderTime,orderToken from " + ORDER_LIST_TABLE_NAME
                    + "  where urgency = "+NOT_URGENCY+" and order_sign = " + FinalVariables.ORDER_STATE_NOT_READ
                    + " order by receiveOrderTime desc Limit " + PAGE_SIZE + " Offset "
                    + String.valueOf(pageID * PAGE_SIZE);
        } else if (position == FinalVariables.ORDER_TYPE_NOT_COMPLISH)//��ʾδ��ɹ���
        {
            sql = "select id,workCardId,workOrderType,order_sign,customerName," +
                    " receiveOrderTime,orderToken from " + ORDER_LIST_TABLE_NAME
                    + " where order_sign != " + FinalVariables.ORDER_STATE_ORDER_COMPLISH
                    + " order by receiveOrderTime desc Limit " + PAGE_SIZE + " Offset "
                    + String.valueOf(pageID * PAGE_SIZE);
        } else if (position == FinalVariables.ORDER_TYPE_COMPLISH)//��ʾ����ɹ���
        {
            sql = "select id,workCardId,workOrderType,order_sign,customerName," +
                    " receiveOrderTime,orderToken from " + ORDER_LIST_TABLE_NAME
                    + " where order_sign = " + FinalVariables.ORDER_STATE_ORDER_COMPLISH
                    + " order by id desc Limit " + PAGE_SIZE + " Offset "
                    + String.valueOf(pageID * PAGE_SIZE);
        } else if (position == FinalVariables.ORDER_TYPE_URGENCY)//��ʾ��������
        {
            sql = "select id,workCardId,workOrderType,order_sign,customerName," +
                    " receiveOrderTime,orderToken from " + ORDER_LIST_TABLE_NAME
                    + " where order_sign != " + FinalVariables.ORDER_STATE_ORDER_COMPLISH
                    + "  and urgency = "+URGENCY+" order by id desc Limit " + PAGE_SIZE + " Offset "
                    + String.valueOf(pageID * PAGE_SIZE);
        } else if (position == FinalVariables.ORDER_TYPE_BACK) //�طù���
        {
            sql = "select id,workCardId,workOrderType,order_sign,customerName," +
                    " receiveOrderTime,orderToken from " + ORDER_LIST_TABLE_NAME
                    + " where  workOrderType = 2 order by order_sign,receiveOrderTime asc Limit " + PAGE_SIZE + " Offset "
                    + String.valueOf(pageID * PAGE_SIZE);
        } else if (position == FinalVariables.ORDER_TYPE_MAINTAIN) //ά������
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

                    item.setId(cursor.getInt(cursor.getColumnIndex("id"))); // ��ȡ��һ�е�ֵ,��һ�е�������0��ʼ
                    item.setWorkCardId(cursor.getLong(cursor.getColumnIndex("workCardId"))); //������
                    item.setOrder_sign(cursor.getInt(cursor.getColumnIndex("order_sign")));//����״̬���
                    item.setReceiveOrderTime(cursor.getString(cursor.getColumnIndex("receiveOrderTime"))); //�ɵ�ʱ��
                    item.setCustomerName(cursor.getString(cursor.getColumnIndex("customerName"))); //�ͻ�����
                    item.setWorkOrderType(cursor.getInt(cursor.getColumnIndex("workOrderType")));
                    item.setOrderToken(cursor.getString(cursor.getColumnIndex("orderToken")));//���ݺ�
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
     * ����ʵ��д�����ݿ�
     *
     * @param oEntity ����Ĺ���ʵ��
     * @return ����ɹ����ص�id
     */
    public long insert(OrderEntity oEntity) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("workCardId", oEntity.getWorkCardId());    //�ɹ���ID int��(����)
            cv.put("serviceDate", oEntity.getServiceDate());    //��������(��ʽ:yyyyMMddHHmmss) (����)
            cv.put("workOrderType", oEntity.getWorkOrderType());    //�ɹ����,int�� 0��ֱ���ɹ���2���ط��ɹ�(����)
            cv.put("customerId", oEntity.getCustomerId());    //�ͻ�ID,int��(����)
            cv.put("linkPerson", oEntity.getLinkPerson());    //�ͻ���ϵ�� (����)
            cv.put("customerName", oEntity.getCustomerName());    //�ͻ���˾����(����)
            cv.put("taxCode", oEntity.getTaxCode());    //˰��(����)
            cv.put("departmentId", oEntity.getDepartmentId());    //��������ID,int��(����)
            cv.put("departmentName", oEntity.getDepartmentName());    //������������(����)
            cv.put("revenueId", oEntity.getRevenueId());        //����˰��־�ID,int��(����)
            cv.put("revenueName", oEntity.getRevenueName());    //����˰��־�����(����)
            cv.put("customerAddr", oEntity.getCustomerAddr());    //���ŵ�ַ(����)
            cv.put("customerMobile", oEntity.getCustomerMobile());    //�ͻ��ֻ�(����)
            cv.put("customerTel", oEntity.getCustomerTel());    //�ͻ���ϵ�绰(����)
            cv.put("customerCounty", oEntity.getCustomerCounty());    //�ͻ�����������(����)
            cv.put("customerLatitude", oEntity.getCustomerLatitude());    //�ͻ���ַ����γ��(����)
            cv.put("customerLongitude", oEntity.getCustomerLongitude());    //�ͻ���ַ���ھ���(����)
            cv.put("fieldWorkerId", oEntity.getFieldWorkerId());    //������ԱID,int��(����)
            cv.put("fieldWorkerName", oEntity.getFieldWorkerName()); //������Ա����(����)
            cv.put("workOrderDescription", oEntity.getWorkOrderDescription()); //�ɹ�˵��(����)
            cv.put("expectArriveTime", oEntity.getExpectArriveTime()); //������ԱԤ�Ƶ���ʱ��(��ʽ:yyyyMMddHHmmss)(����)
            cv.put("productName", oEntity.getProductName()); //������Ʒ����
            cv.put("chargeType", oEntity.getChargeType()); //�Ƿ��շ�(1:��,2:��) int(����)
            cv.put("chargeMoney", oEntity.getChargeMoney()); //�շѽ��(����)
            cv.put("urgency", oEntity.getUrgency()); //�����Ľ����̶�(0:һ��,1:����),int(����)
            cv.put("city", oEntity.getCity()); //�ͻ����ڳ���(�������ƣ����糤ɳ�У���̶�У�����������)(����)
            cv.put("order_sign", oEntity.getOrder_sign()); //����״̬��ǣ�0��δ�Ķ���1:�Ѿ��Ķ���2:��������
            // 3��������ɣ�4����������ͨ����5����������δͨ����
            // 6�������ֳ���7:�绰���  10��������ɣ�
            cv.put("upload_sign", oEntity.getUpload_sign()); //0��δ�ϴ�����1�������ϴ����
            cv.put("finish_type", oEntity.getFinish_type()); //�����������  Ĭ��Ϊ0�� 1���ֳ���� ��2���绰�����3������
            cv.put("is_task", oEntity.getIs_task()); //�Ƿ�ӵ������Ҫ�ϴ� 0:û�У�1����
            cv.put("receiveOrderTime", oEntity.getReceiveOrderTime()); //��������ʱ��
            cv.put("orderToken", oEntity.getOrderToken());//���ݺ�
            cv.put("visit_type", oEntity.getVisit_type()); //�ط�����
            cv.put("product_status", oEntity.getProduct_status()); //��Ʒ״̬
            cv.put("handle_result", oEntity.getHandle_result()); //������
            cv.put("optimal_path", oEntity.getOptimal_path());//����·��
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
     * ����д�����ݿ�
     *
     * @param orderList ����д���ʵ��
     */
    public void addOrderList(ArrayList<OrderEntity> orderList) {
        SQLiteDatabase db = null;
        ContentValues cv;
        try {
            db = this.getWritableDatabase();
            if (db != null) {
                db.beginTransaction();//�ֶ����ÿ�ʼ����
            }
            //���ݲ������ѭ��
            for (OrderEntity oEntity : orderList) {
                cv = new ContentValues();
                cv.put("workCardId", oEntity.getWorkCardId());    //�ɹ���ID int��(����)
                cv.put("serviceDate", oEntity.getServiceDate());    //��������(��ʽ:yyyyMMddHHmmss) (����)
                cv.put("workOrderType", oEntity.getWorkOrderType());    //�ɹ����,int�� 0��ֱ���ɹ���2���ط��ɹ�(����)
                cv.put("customerId", oEntity.getCustomerId());    //�ͻ�ID,int��(����)
                cv.put("linkPerson", oEntity.getLinkPerson());    //�ͻ���ϵ�� (����)
                cv.put("customerName", oEntity.getCustomerName());    //�ͻ���˾����(����)
                cv.put("taxCode", oEntity.getTaxCode());    //˰��(����)
                cv.put("departmentId", oEntity.getDepartmentId());    //��������ID,int��(����)
                cv.put("departmentName", oEntity.getDepartmentName());    //������������(����)
                cv.put("revenueId", oEntity.getRevenueId());        //����˰��־�ID,int��(����)
                cv.put("revenueName", oEntity.getRevenueName());    //����˰��־�����(����)
                cv.put("customerAddr", oEntity.getCustomerAddr());    //���ŵ�ַ(����)
                cv.put("customerMobile", oEntity.getCustomerMobile());    //�ͻ��ֻ�(����)
                cv.put("customerTel", oEntity.getCustomerTel());    //�ͻ���ϵ�绰(����)
                cv.put("customerCounty", oEntity.getCustomerCounty());    //�ͻ�����������(����)
                cv.put("customerLatitude", oEntity.getCustomerLatitude());    //�ͻ���ַ����γ��(����)
                cv.put("customerLongitude", oEntity.getCustomerLongitude());    //�ͻ���ַ���ھ���(����)
                cv.put("fieldWorkerId", oEntity.getFieldWorkerId());    //������ԱID,int��(����)
                cv.put("fieldWorkerName", oEntity.getFieldWorkerName()); //������Ա����(����)
                cv.put("workOrderDescription", oEntity.getWorkOrderDescription()); //�ɹ�˵��(����)
                cv.put("expectArriveTime", oEntity.getExpectArriveTime()); //������ԱԤ�Ƶ���ʱ��(��ʽ:yyyyMMddHHmmss)(����)
                cv.put("productName", oEntity.getProductName()); //������Ʒ����
                cv.put("chargeType", oEntity.getChargeType()); //�Ƿ��շ�(1:��,2:��) int(����)
                cv.put("chargeMoney", oEntity.getChargeMoney()); //�շѽ��(����)
                cv.put("urgency", oEntity.getUrgency()); ////�����Ľ����̶�(0:һ��,1:����),int(����)
                cv.put("city", oEntity.getCity()); //�ͻ����ڳ���(�������ƣ����糤ɳ�У���̶�У�����������)(����)
                cv.put("order_sign", oEntity.getOrder_sign()); //����״̬��ǣ�0��δ�Ķ���1:�Ѿ��Ķ���2:��������
                // 3��������ɣ�4����������ͨ����5����������δͨ����
                // 6�������ֳ���7:�绰���  10��������ɣ�
                cv.put("upload_sign", oEntity.getUpload_sign()); //0��δ�ϴ�����1�������ϴ����
                cv.put("finish_type", oEntity.getFinish_type()); //�����������  Ĭ��Ϊ0�� 1���ֳ���� ��2���绰�����3������
                cv.put("is_task", oEntity.getIs_task()); //�Ƿ�ӵ������Ҫ�ϴ� 0:û�У�1����
                cv.put("receiveOrderTime", oEntity.getReceiveOrderTime()); //��������ʱ��
                cv.put("orderToken", oEntity.getOrderToken());//���ݺ�
                cv.put("visit_type", oEntity.getVisit_type()); //�ط�����
                cv.put("product_status", oEntity.getProduct_status()); //��Ʒ״̬
                cv.put("handle_result", oEntity.getHandle_result()); //������
                cv.put("optimal_path", oEntity.getOptimal_path());//����·��
                if (db != null) {
                    db.insert(ORDER_LIST_TABLE_NAME, null, cv);
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
     * ͨ�������Ų�ѯ������Ʒ����
     *
     * @param orderId ������
     * @return ��Ʒ����
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
     * ����ID��ѯ������ϸ��Ϣ
     *
     * @param id ����id
     * @return ����ʵ����Ϣ
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
                    item.setId(cursor.getInt(cursor.getColumnIndex("id"))); // ��ȡ��һ�е�ֵ,��һ�е�������0��ʼ
                    item.setWorkCardId(cursor.getLong(cursor.getColumnIndex("workCardId")));    //�ɹ���ID int��(����)
                    item.setServiceDate(cursor.getString(cursor.getColumnIndex("serviceDate")));    //��������(��ʽ:yyyyMMddHHmmss) (����)
                    item.setWorkOrderType(cursor.getInt(cursor.getColumnIndex("workOrderType")));    //�ɹ����,int�� 0��ֱ���ɹ���2���ط��ɹ�(����)
                    item.setCustomerId(cursor.getInt(cursor.getColumnIndex("customerId")));    //�ͻ�ID,int��(����)
                    item.setLinkPerson(cursor.getString(cursor.getColumnIndex("linkPerson")));    //�ͻ���ϵ�� (����)
                    item.setCustomerName(cursor.getString(cursor.getColumnIndex("customerName")));    //�ͻ���˾����(����)
                    item.setTaxCode(cursor.getString(cursor.getColumnIndex("taxCode")));    //˰��(����)
                    item.setDepartmentId(cursor.getInt(cursor.getColumnIndex("departmentId")));    //��������ID,int��(����)
                    item.setDepartmentName(cursor.getString(cursor.getColumnIndex("departmentName")));    //������������(����)
                    item.setRevenueId(cursor.getInt(cursor.getColumnIndex("revenueId")));        //����˰��־�ID,int��(����)
                    item.setRevenueName(cursor.getString(cursor.getColumnIndex("revenueName")));    //����˰��־�����(����)
                    item.setCustomerAddr(cursor.getString(cursor.getColumnIndex("customerAddr")));    //���ŵ�ַ(����)
                    item.setCustomerMobile(cursor.getString(cursor.getColumnIndex("customerMobile")));    //�ͻ��ֻ�(����)
                    item.setCustomerTel(cursor.getString(cursor.getColumnIndex("customerTel")));    //�ͻ���ϵ�绰(����)
                    item.setCustomerCounty(cursor.getString(cursor.getColumnIndex("customerCounty")));    //�ͻ�����������(����)
                    item.setCustomerLatitude(cursor.getString(cursor.getColumnIndex("customerLatitude")));    //�ͻ���ַ����γ��(����)
                    item.setCustomerLongitude(cursor.getString(cursor.getColumnIndex("customerLongitude")));    //�ͻ���ַ���ھ���(����)
                    item.setFieldWorkerId(cursor.getInt(cursor.getColumnIndex("fieldWorkerId")));    //������ԱID,int��(����)
                    item.setFieldWorkerName(cursor.getString(cursor.getColumnIndex("fieldWorkerName"))); //������Ա����(����)
                    item.setWorkOrderDescription(cursor.getString(cursor.getColumnIndex("workOrderDescription"))); //�ɹ�˵��(����)
                    item.setExpectArriveTime(cursor.getString(cursor.getColumnIndex("expectArriveTime"))); //������ԱԤ�Ƶ���ʱ��(��ʽ:yyyyMMddHHmmss)(����)
                    item.setProductName(cursor.getString(cursor.getColumnIndex("productName"))); //������Ʒ����(�طõ�)
                    item.setChargeType(cursor.getInt(cursor.getColumnIndex("chargeType"))); //�Ƿ��շ�(1:��,2:��) int(����)
                    item.setChargeMoney(cursor.getString(cursor.getColumnIndex("chargeMoney"))); //�շѽ��(����)
                    item.setUrgency(cursor.getInt(cursor.getColumnIndex("urgency"))); ////�����Ľ����̶�(1:һ��,2:����),int(����)
                    item.setCity(cursor.getString(cursor.getColumnIndex("city"))); //�ͻ����ڳ���(�������ƣ����糤ɳ�У���̶�У�����������)(����)
                    item.setOrder_sign(cursor.getInt(cursor.getColumnIndex("order_sign"))); //����״̬��ǣ�0��δ�Ķ���1:�Ѿ��Ķ���2:�������� 3��������ɣ�4����������ͨ����5����������δͨ���� 6�������ֳ���7:�绰���  10��������ɣ�
                    item.setUpload_sign(cursor.getInt(cursor.getColumnIndex("upload_sign"))); //0��δ�ϴ�����1�������ϴ����
                    item.setFinish_type(cursor.getInt(cursor.getColumnIndex("finish_type"))); //�����������  Ĭ��Ϊ0�� 1���ֳ���� ��2���绰�����3������, 4.�طù���
                    item.setIs_task(cursor.getInt(cursor.getColumnIndex("is_task"))); //�Ƿ�ӵ������Ҫ�ϴ� 0:û�У�1����
                    item.setReceiveOrderTime(cursor.getString(cursor.getColumnIndex("receiveOrderTime"))); //��������ʱ��
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
     * ����workCardId��ѯ������ϸ��Ϣ,�����ظ�
     *
     * @param workCardId �ɹ���id
     * @return �Ƿ����ظ�
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
     * �����¹�������ȡ5�����µ�δ����δ��ɹ���
     *
     * @return �����б�
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

                    item.setId(cursor.getInt(cursor.getColumnIndex("id"))); // ��ȡ��һ�е�ֵ,��һ�е�������0��ʼ
                    item.setWorkCardId(cursor.getLong(cursor.getColumnIndex("workCardId"))); //������
                    item.setOrder_sign(cursor.getInt(cursor.getColumnIndex("order_sign")));//����״̬���
                    item.setReceiveOrderTime(cursor.getString(cursor.getColumnIndex("receiveOrderTime"))); //�ɵ�ʱ��
                    //item.setQ_type(cursor.getString(cursor.getColumnIndex("q_type"))); //�������
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
     * ����������ͳ�ƹ���
     *
     * @param type ����
     * @return ��������
     */
    public int CountOrdersByCondition(int type) {
        SQLiteDatabase db = null;
        int result = 0;
        Cursor cursor = null;

        String sql = "";

        //ͳ��δ��ɵĽ���������
        if (type == 0) {
            sql = "select count(*) as result from " + ORDER_LIST_TABLE_NAME + " where (" +
                    "  order_sign != " + FinalVariables.ORDER_STATE_ORDER_COMPLISH + ") "
                    + " and urgency = " + URGENCY;
        } else if (type == 1)//ͳ�����е��¹�����
        {
            sql = "select count(*) as result from " + ORDER_LIST_TABLE_NAME + " where urgency = " + NOT_URGENCY + " and order_sign = "
                    + FinalVariables.ORDER_STATE_NOT_READ;
        } else if (type == 2)//ͳ�����е�δ��ɹ�����
        {
            sql = "select count(*) as result from " + ORDER_LIST_TABLE_NAME + " where ( order_sign != "
                    + FinalVariables.ORDER_STATE_ORDER_COMPLISH + ")  ";
        } else if (type == 3)//ͳ��������ɹ�����
        {

            sql = "select count(*) as result from " + ORDER_LIST_TABLE_NAME + " where ( order_sign = "
                    + FinalVariables.ORDER_STATE_ORDER_COMPLISH + ")  ";
        } else if (type == 4) //ͳ�ƻطù�����
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
     * ���¹���״̬��־
     * @param order_num   ��������
     * @param order_state ����״̬
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
     * ���¹���״̬��־
     * @param order_num   ��������
     * @param finish_type ��������
     */
    public void updateFinishType(String order_num, int finish_type) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            String where = "workCardId=?";
            String[] whereValue = {order_num};
            ContentValues cv = new ContentValues();
            if (finish_type == FinalVariables.ORDER_FINISH_TYPE_SPOT_SOLVE) {
                cv.put("finish_type", FinalVariables.ORDER_FINISH_TYPE_SPOT_SOLVE); //�ֳ����
            } else if (finish_type == FinalVariables.ORDER_FINISH_TYPE_PHONE_SOLVE) //�绰���
            {
                cv.put("finish_type", FinalVariables.ORDER_FINISH_TYPE_PHONE_SOLVE);
            } else if (finish_type == FinalVariables.ORDER_FINISH_TYPE_CHANGE_SOLVE) //����
            {
                cv.put("finish_type", FinalVariables.ORDER_FINISH_TYPE_CHANGE_SOLVE);
            } else if (finish_type == FinalVariables.ORDER_FINISH_TYPE_VISIT_SOLVE) //�طù���
            {
                cv.put("finish_type", FinalVariables.ORDER_FINISH_TYPE_VISIT_SOLVE);
            }
            cv.put("order_sign", FinalVariables.ORDER_STATE_ORDER_COMPLISH); //��ɹ���

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
     * ģ��������ѯ
     * @param condition ����
     * @return ��ѯ���Ĺ���ʵ��
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

                    item.setId(cursor.getInt(cursor.getColumnIndex("id"))); // ��ȡ��һ�е�ֵ,��һ�е�������0��ʼ
                    item.setWorkCardId(cursor.getLong(cursor.getColumnIndex("workCardId"))); //������
                    item.setOrder_sign(cursor.getInt(cursor.getColumnIndex("order_sign")));//����״̬���
                    item.setReceiveOrderTime(cursor.getString(cursor.getColumnIndex("receiveOrderTime"))); //�ɵ�ʱ��
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
     * ��ȡû���ϴ��ļ�¼
     * @return ��ѯ���Ĺ���ʵ��list
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

                    item.setId(cursor.getInt(cursor.getColumnIndex("id"))); // ��ȡ��һ�е�ֵ,��һ�е�������0��ʼ
                    item.setWorkCardId(cursor.getLong(cursor.getColumnIndex("workCardId"))); //������
                    item.setOrder_sign(cursor.getInt(cursor.getColumnIndex("order_sign")));//����״̬���
                    item.setReceiveOrderTime(cursor.getString(cursor.getColumnIndex("receiveOrderTime"))); //�ɵ�ʱ��
                    item.setOrderToken(cursor.getString(cursor.getColumnIndex("orderToken")));
                    item.setWorkOrderType(cursor.getInt(cursor.getColumnIndex("workOrderType")));
                    item.setCustomerName(cursor.getString(cursor.getColumnIndex("customerName")));
                    //item.setQ_content(cursor.getString(cursor.getColumnIndex("q_content"))); //�������
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
     * ���¹�������״̬��־
     * @param order_num ������
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
     * ���¹����ļ��ϴ���ʶ
     * @param order_num ����id
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
     * ����ID��ѯ����״̬���
     * @param order_num ����id
     * @return ����״̬���
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
                    result = cursor.getInt(cursor.getColumnIndex("order_sign")); // ��ȡ��һ�е�ֵ,��һ�е�������0��ʼ
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
     * ɾ��С��ĳһ�������
     * @param date ָ��������
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
