package com.sealion.serviceassistant.mqtt;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import com.sealion.serviceassistant.db.*;
import com.sealion.serviceassistant.entity.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sealion.serviceassistant.DesktopActivity;
import com.sealion.serviceassistant.OrderDetailActivity;
import com.sealion.serviceassistant.OrderListActivity;
import com.sealion.serviceassistant.R;
import com.sealion.serviceassistant.exception.NetAccessException;
import com.sealion.serviceassistant.net.HttpRestAchieve;
import com.sealion.serviceassistant.tools.DateTools;
import com.sealion.serviceassistant.tools.FinalVariables;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources.NotFoundException;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/**
 * Mqttʹ�ð�����
 */
public class MqttAdhere {
    private static final String TAG = MqttAdhere.class.getSimpleName();

    // Notification manager to displaying arrived push notifications
    private static NotificationManager mNotifMan;
    // Notification title
    private static String NEW_NOTIF_TITLE = "�µĹ���";
    private static String NOTICE_NOTIF_TITLE = "�µ�֪ͨ";
    private static String CHANGE_ORDER_TITLE = "����֪ͨ";

    // Notification id
    private static final int NOTIF_CONNECTED = 0;

    // Display the topbar notification

    /**
     * @param ctx
     * @param text
     * @param sign 0:�µĹ���, 1:�µ�֪ͨ, 2:����֪ͨ
     */
    private static void showNotification(Context ctx, String text, int sign) {
        mNotifMan = (NotificationManager) ctx.getSystemService(ctx.NOTIFICATION_SERVICE);

        Notification n = new Notification();

        n.flags |= Notification.FLAG_SHOW_LIGHTS;
        n.flags |= Notification.FLAG_AUTO_CANCEL;

        n.defaults = Notification.DEFAULT_ALL;

        n.icon = R.drawable.logo_report;
        n.when = System.currentTimeMillis();

        // Simply open the parent activity
        PendingIntent pi = null;

        // Change the name of the notification here
        if (sign == 0) {
            pi = PendingIntent.getActivity(ctx, 0, new Intent(ctx, DesktopActivity.class), 0);
            n.setLatestEventInfo(ctx, NEW_NOTIF_TITLE, text, pi);
        } else if (sign == 1) {
            pi = PendingIntent.getActivity(ctx, 0, new Intent(ctx, DesktopActivity.class), 0);
            n.setLatestEventInfo(ctx, NOTICE_NOTIF_TITLE, text, pi);
        } else if (sign == 2) {
            pi = PendingIntent.getActivity(ctx, 0, new Intent(ctx, DesktopActivity.class), 0);
            n.setLatestEventInfo(ctx, CHANGE_ORDER_TITLE, text, pi);
        }
        mNotifMan.notify(NOTIF_CONNECTED, n);
    }

    public boolean insertOrderMessageToDB(Context ctx, String message) {

        Log.d(TAG, message);
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(message);
            OrderListDB orderListDB = new OrderListDB(ctx);
            OrderDetailListDB odListDB = new OrderDetailListDB(ctx);
            OrderEntity oEntity = new OrderEntity();
            long workCardId = jsonObject.getLong("workCardId");
            //workCardIDString=jsonObject.getString("workCardId");

            oEntity.setWorkCardId(workCardId);// �ɹ���ID
            oEntity.setServiceDate(jsonObject.getString("serviceDate"));// ��������
            int workOrderType = jsonObject.getInt("workOrderType");
            if (workOrderType != 1) workOrderType = 2;
            oEntity.setWorkOrderType(workOrderType);// �ɹ����
            oEntity.setVisit_type(jsonObject.getString("visitType"));
            oEntity.setHandle_result(jsonObject.getString("handleResult"));
            oEntity.setProduct_status(jsonObject.getString("productStatus"));

            oEntity.setFieldWorkerId(jsonObject.getInt("fieldWorkerId")); // ������ԱID
            oEntity.setFieldWorkerName(jsonObject.getString("fieldWorkerName")); // ������Ա����
            oEntity.setWorkOrderDescription(jsonObject.getString("workOrderDescription")); // �ɹ�˵��
            oEntity.setExpectArriveTime(jsonObject.getString("expectArriveTime")); // ������ԱԤ�Ƶ���ʱ��
            oEntity.setOrderToken(jsonObject.getString("orderToken"));
            oEntity.setOptimal_path(jsonObject.getString("route"));

            oEntity.setChargeType(jsonObject.getInt("chargeType")); // �Ƿ��շ�(1:��,2:��)
            String chargeMoney = jsonObject.getString("chargeMoney");
            if (chargeMoney == null || chargeMoney.equals("") || chargeMoney.equals("null")) {
                oEntity.setChargeMoney("0.0"); // �շѽ��
            } else {
                oEntity.setChargeMoney(chargeMoney); // �շѽ��
            }
            oEntity.setUrgency(jsonObject.getInt("urgency")); // �����Ľ����̶�(0:�ǽ���,1:����)
            oEntity.setCity(jsonObject.getString("city")); // �ͻ����ڳ���(�������ƣ����糤ɳ�У���̶�У�����������)

            JSONObject customerObj = jsonObject.getJSONObject("customerDto");

            oEntity.setCustomerId(customerObj.getLong("customerId"));// �ͻ�ID
            oEntity.setLinkPerson(customerObj.getString("linkPerson"));// �ͻ���ϵ��
            oEntity.setCustomerName(customerObj.getString("customerName"));// �ͻ���˾����
            oEntity.setTaxCode(customerObj.getString("taxCode"));// ˰��
            oEntity.setDepartmentId(customerObj.getInt("departmentId"));// ��������ID
            oEntity.setDepartmentName(customerObj.getString("departmentName"));// ������������
            if (workOrderType != 2) {
                try {
                    oEntity.setRevenueId(customerObj.getInt("revenueId"));

                } catch (JSONException e) {
                    oEntity.setRevenueId(0);
                    //e.printStackTrace();
                }

                try {
                    oEntity.setRevenueName(customerObj.getString("revenueName"));// ����˰��־�����

                } catch (JSONException e) {
                    oEntity.setRevenueName("");// ����˰��־�����
                    //e.printStackTrace();
                }

                //oEntity.setRevenueId(customerObj.getInt("revenueId"));// ����˰��־�ID
                //oEntity.setRevenueName(customerObj.getString("revenueName"));// ����˰��־�����
            }
            oEntity.setCustomerAddr(customerObj.getString("customerAddr"));// ���ŵ�ַ
            oEntity.setCustomerMobile(customerObj.getString("customerMobile"));// �ͻ��ֻ�
            oEntity.setCustomerTel(customerObj.getString("customerTel"));// �ͻ���ϵ�绰
            oEntity.setCustomerCounty(customerObj.getString("customerCounty"));// �ͻ�����������
            oEntity.setCustomerLatitude(customerObj.getString("customerLatitude"));// �ͻ���ַ����γ��
            oEntity.setCustomerLongitude(customerObj.getString("customerLongitude"));// �ͻ���ַ���ھ���
            oEntity.setReceiveOrderTime(DateTools.getFormatDateAndTime());


            ArrayList<QuestionEntity> qList = null;
            if (workOrderType != 2) {
                qList = new ArrayList<QuestionEntity>();
                QuestionEntity qEntity = null;

                JSONArray problemList = jsonObject.getJSONArray("problemList");
                for (int i = 0; i < problemList.length(); i++) {
                    qEntity = new QuestionEntity();
                    JSONObject qa = problemList.getJSONObject(i);
//					QCategoryDB db = new QCategoryDB(ctx);

                    qEntity.setQ_id(qa.getString("problemId"));
                    qEntity.setQ_type_id(qa.getInt("questionTypId"));
                    qEntity.setQ_category_id(qa.getInt("questionTypPid"));
                    qEntity.setQ_content(qa.getString("questionDesc"));
                    qEntity.setQ_answer(qa.getString("solution"));
                    qEntity.setOrder_num(workCardId + "");
                    qEntity.setProductId(qa.getString("productId"));
                    qEntity.setProductName(qa.getString("productName"));

                    qList.add(qEntity);
                }
            } else {
                JSONArray problemList = jsonObject.getJSONArray("problemList");
                JSONObject qa = problemList.getJSONObject(0);
//					QCategoryDB db = new QCategoryDB(ctx);

                oEntity.setProductName(qa.getString("productName"));

            }

            if (!orderListDB.SelectByOrderId(workCardId)) {
                // д�빤�����������ݿ�
                long id = orderListDB.insert(oEntity);

                if (qList != null && qList.size() > 0) {
                    QuestionListDB qlDB = new QuestionListDB(ctx);
                    qlDB.batchInsert(qList);
                }
                // д�빤����ǰ״̬���������ݿ�
                OrderStepEntity osEntity = new OrderStepEntity();
                osEntity.setOper_time(DateTools.getFormatDateAndTime());
                osEntity.setOrder_num(workCardId + "");
                osEntity.setOrder_state(FinalVariables.ORDER_STATE_NOT_READ); // �¹�����δ�Ķ�״̬
                odListDB.insert(osEntity);

                if (id > 0) {
                    showNotification(ctx, "���յ��µĹ�����������Ϊ��" + workCardId, 0);
                    SharedPreferences sp = ctx.getSharedPreferences("USER_CARD", Context.MODE_PRIVATE);
                    new PostData().execute(sp.getString("USERNAME", ""), workCardId + "", DateTools.getFormatDateAndTime(), ctx.getResources().getString(R.string.request_url));
                    return true;
                    // ��������ͨ���������ù����Ѿ��յ�
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

    }

    public void insertNoticeMessageToDB(Context ctx, String message, String request_url, String cityCode) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(message);

            if (jsonObject.getString("title").equals("data_update_notification")) {
                String content = jsonObject.getString("content");
                if (content.equals("revenue")) {
                    new PostBaseData(request_url, ctx).execute("revenue");
                } else if (content.equals("problem_category")) {
                    new PostBaseData(request_url, ctx).execute("problem_category");
                } else if (content.equals("problem_type")) {
                    new PostBaseData(request_url, ctx).execute("problem_type");
                } else if (content.equals("expense_item")) {
                    new PostBaseData(request_url, ctx).execute("expense_item");
                }
            } else {
                String city = jsonObject.getString("city");
                if (city == null || city.equals("") || city.equals("null") || city.equals(cityCode)) {
                    NoticeDB noticeDB = new NoticeDB(ctx);
                    NoticeEntity nEntity = new NoticeEntity();
                    nEntity.setContent(jsonObject.getString("content"));
                    nEntity.setTitle(jsonObject.getString("title"));
                    nEntity.setSign(0);// δ�Ķ�
                    nEntity.setPublish_time(jsonObject.getString("publishDate"));

                    long id = noticeDB.insert(nEntity);
                    if (id > 0) {
                        showNotification(ctx, "���յ��µ�֪ͨ " + jsonObject.getString("title"), 1);
                    }
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private class PostData extends AsyncTask<String, Void, Integer> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Integer doInBackground(String... params) {
            postData(params[0], params[1], params[2], params[3]);
            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {

        }
    }

    // ����http����֪ͨ�����Ѿ����յ�����
    public void postData(String... params) {
        HttpRestAchieve httpRestAchieve = new HttpRestAchieve();
        HashMap<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("workerNo", params[0]);
        paramsMap.put("workOrderNo", params[1]);
        paramsMap.put("confirmTime", params[2]);
        try {

            NetBackDataEntity netBackData = httpRestAchieve.postRequestData(params[3] + "/fieldworker/biz/confirm", paramsMap);
            String data = netBackData.getData();
            JSONObject jsonObject = new JSONObject(data);
            int result = jsonObject.getInt("resultCode");
            if (result == 1) {
                Log.d(TAG, "֪ͨ�õ������ɹ���");
            } else {
                Log.d(TAG, "֪ͨ�õ�����ʧ�ܣ�");
            }

        } catch (NotFoundException e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        } catch (NetAccessException e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }
    }

    // �÷�����ʱ����
    public void DisposeOrderChange(Context ctx, String message) {
        OrderListDB olDB = null;
        OrderDetailListDB odListDB = null;

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(message);
            String order_num = jsonObject.getString("workOrderNo");
            boolean agree = jsonObject.getBoolean("agree");

            olDB = new OrderListDB(ctx);
            odListDB = new OrderDetailListDB(ctx);
            OrderStepEntity osEntity = new OrderStepEntity();
            osEntity.setOper_time(DateTools.getFormatDateAndTime());
            osEntity.setOrder_num(order_num);

            // ����ɹ������
            if (agree) {
                olDB.updateSignData(order_num, FinalVariables.ORDER_STATE_APPLY_CHANGE);
                osEntity.setOrder_state(FinalVariables.ORDER_STATE_APPLY_CHANGE); // ��ɹ���
            } else {
                // olDB.updateSignData(order_num,
                // FinalVariables.ORDER_STATE_APPLY_NOT_PASS);
                // osEntity.setOrder_state(FinalVariables.ORDER_STATE_APPLY_NOT_PASS);
                // //��ɹ���
            }

            // д�빤����ǰ״̬���������ݿ�
            odListDB.insert(osEntity);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public class PostBaseData extends AsyncTask<String, Void, Integer> {
        private String request_url;
        private Context ctx;

        public PostBaseData(String request_url, Context ctx) {
            this.request_url = request_url;
            this.ctx = ctx;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Integer doInBackground(String... params) {
            int back_data = 0;
            HttpRestAchieve httpRestAchieve = new HttpRestAchieve();
            HashMap<String, String> paramsMap = new HashMap<String, String>();
            paramsMap.put("dataType", params[0]);

            try {

                NetBackDataEntity netBackData = httpRestAchieve.postRequestData(request_url + "/dataUpdate/", paramsMap);
                String data = netBackData.getData();
                Log.d(TAG, "==:" + data);
                if (data != null && !data.equals("")) {
                    ParseData(ctx, params[0], data);
                }
            } catch (NotFoundException e) {
                back_data = 2;
                e.printStackTrace();
                Log.d(TAG, e.getMessage());
            } catch (NetAccessException e) {
                back_data = 3;
                e.printStackTrace();
                Log.d(TAG, e.getMessage());
            }
            return back_data;
        }

        @Override
        protected void onPostExecute(Integer result) {

        }
    }

    public static void ParseData(Context ctx, String type, String data) {
        try {
            JSONArray arr = new JSONArray(data);
            if (type.equals("revenue")) // ˰�����ݸ���
            {

            } else if (type.equals("problem_category")) // �������
            {
                ArrayList<Q_Category> qcList = new ArrayList<Q_Category>();
                Q_Category q_Category = null;

                for (int i = 0; i < arr.length(); i++) {
                    q_Category = new Q_Category();
                    JSONObject temp = (JSONObject) arr.get(i);
                    q_Category.setCategory_id(temp.getInt("id"));
                    q_Category.setName(temp.getString("name"));
                    q_Category.setProductId(temp.getString("productId"));
                    q_Category.setProductName(temp.getString("productName"));
                    qcList.add(q_Category);
                }

                QCategoryDB db = new QCategoryDB(ctx);
                db.deleteAllData();
                db.batchInsert(qcList);
            } else if (type.equals("problem_type")) // ��������
            {
                ArrayList<Q_Type> qtList = new ArrayList<Q_Type>();
                Q_Type q_type = null;

                for (int i = 0; i < arr.length(); i++) {
                    q_type = new Q_Type();
                    JSONObject temp = (JSONObject) arr.get(i);
                    q_type.setType_id(temp.getInt("id"));
                    q_type.setQ_category_id(temp.getInt("problemCategoryId"));
                    q_type.setName(temp.getString("name"));
                    qtList.add(q_type);
                }

                QTypeDB db = new QTypeDB(ctx);
                db.deleteAllData();
                db.batchInsert(qtList);
            } else if (type.equals("expense_item")) // ����
            {
                ArrayList<CostEntity> costList = new ArrayList<CostEntity>();
                CostEntity cEntity = null;

                for (int i = 0; i < arr.length(); i++) {
                    cEntity = new CostEntity();
                    JSONObject temp = (JSONObject) arr.get(i);
                    cEntity.setCost_id(temp.getInt("id"));
                    cEntity.setName(temp.getString("name"));
                    costList.add(cEntity);
                }

                CostDB cvDB = new CostDB(ctx);
                cvDB.deleteAllData();
                cvDB.batchInsert(costList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String changeCharset(String str, String newCharset) {
        if (str != null) {
            // ��Ĭ���ַ���������ַ�����
            byte[] bs = str.getBytes();
            // ���µ��ַ����������ַ���
            try {
                return new String(bs, newCharset);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
