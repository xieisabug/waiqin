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
 * Mqtt使用帮助类
 */
public class MqttAdhere {
    private static final String TAG = MqttAdhere.class.getSimpleName();

    // Notification manager to displaying arrived push notifications
    private static NotificationManager mNotifMan;
    // Notification title
    private static String NEW_NOTIF_TITLE = "新的工单";
    private static String NOTICE_NOTIF_TITLE = "新的通知";
    private static String CHANGE_ORDER_TITLE = "改派通知";

    // Notification id
    private static final int NOTIF_CONNECTED = 0;

    // Display the topbar notification

    /**
     * @param ctx
     * @param text
     * @param sign 0:新的工单, 1:新的通知, 2:改派通知
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

            oEntity.setWorkCardId(workCardId);// 派工单ID
            oEntity.setServiceDate(jsonObject.getString("serviceDate"));// 服务日期
            int workOrderType = jsonObject.getInt("workOrderType");
            if (workOrderType != 1) workOrderType = 2;
            oEntity.setWorkOrderType(workOrderType);// 派工类别
            oEntity.setVisit_type(jsonObject.getString("visitType"));
            oEntity.setHandle_result(jsonObject.getString("handleResult"));
            oEntity.setProduct_status(jsonObject.getString("productStatus"));

            oEntity.setFieldWorkerId(jsonObject.getInt("fieldWorkerId")); // 服务人员ID
            oEntity.setFieldWorkerName(jsonObject.getString("fieldWorkerName")); // 服务人员姓名
            oEntity.setWorkOrderDescription(jsonObject.getString("workOrderDescription")); // 派工说明
            oEntity.setExpectArriveTime(jsonObject.getString("expectArriveTime")); // 服务人员预计到达时间
            oEntity.setOrderToken(jsonObject.getString("orderToken"));
            oEntity.setOptimal_path(jsonObject.getString("route"));

            oEntity.setChargeType(jsonObject.getInt("chargeType")); // 是否收费(1:是,2:否)
            String chargeMoney = jsonObject.getString("chargeMoney");
            if (chargeMoney == null || chargeMoney.equals("") || chargeMoney.equals("null")) {
                oEntity.setChargeMoney("0.0"); // 收费金额
            } else {
                oEntity.setChargeMoney(chargeMoney); // 收费金额
            }
            oEntity.setUrgency(jsonObject.getInt("urgency")); // 工单的紧急程度(0:非紧急,1:紧急)
            oEntity.setCity(jsonObject.getString("city")); // 客户所在城市(城市名称，比如长沙市，湘潭市，湘西自治州)

            JSONObject customerObj = jsonObject.getJSONObject("customerDto");

            oEntity.setCustomerId(customerObj.getLong("customerId"));// 客户ID
            oEntity.setLinkPerson(customerObj.getString("linkPerson"));// 客户联系人
            oEntity.setCustomerName(customerObj.getString("customerName"));// 客户公司名称
            oEntity.setTaxCode(customerObj.getString("taxCode"));// 税号
            oEntity.setDepartmentId(customerObj.getInt("departmentId"));// 所属部门ID
            oEntity.setDepartmentName(customerObj.getString("departmentName"));// 所属部门名称
            if (workOrderType != 2) {
                try {
                    oEntity.setRevenueId(customerObj.getInt("revenueId"));

                } catch (JSONException e) {
                    oEntity.setRevenueId(0);
                    //e.printStackTrace();
                }

                try {
                    oEntity.setRevenueName(customerObj.getString("revenueName"));// 所属税务分局名称

                } catch (JSONException e) {
                    oEntity.setRevenueName("");// 所属税务分局名称
                    //e.printStackTrace();
                }

                //oEntity.setRevenueId(customerObj.getInt("revenueId"));// 所属税务分局ID
                //oEntity.setRevenueName(customerObj.getString("revenueName"));// 所属税务分局名称
            }
            oEntity.setCustomerAddr(customerObj.getString("customerAddr"));// 上门地址
            oEntity.setCustomerMobile(customerObj.getString("customerMobile"));// 客户手机
            oEntity.setCustomerTel(customerObj.getString("customerTel"));// 客户联系电话
            oEntity.setCustomerCounty(customerObj.getString("customerCounty"));// 客户所在区、县
            oEntity.setCustomerLatitude(customerObj.getString("customerLatitude"));// 客户地址所在纬度
            oEntity.setCustomerLongitude(customerObj.getString("customerLongitude"));// 客户地址所在经度
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
                // 写入工单到本地数据库
                long id = orderListDB.insert(oEntity);

                if (qList != null && qList.size() > 0) {
                    QuestionListDB qlDB = new QuestionListDB(ctx);
                    qlDB.batchInsert(qList);
                }
                // 写入工单当前状态到本地数据库
                OrderStepEntity osEntity = new OrderStepEntity();
                osEntity.setOper_time(DateTools.getFormatDateAndTime());
                osEntity.setOrder_num(workCardId + "");
                osEntity.setOrder_state(FinalVariables.ORDER_STATE_NOT_READ); // 新工单，未阅读状态
                odListDB.insert(osEntity);

                if (id > 0) {
                    showNotification(ctx, "接收到新的工单，工单号为：" + workCardId, 0);
                    SharedPreferences sp = ctx.getSharedPreferences("USER_CARD", Context.MODE_PRIVATE);
                    new PostData().execute(sp.getString("USERNAME", ""), workCardId + "", DateTools.getFormatDateAndTime(), ctx.getResources().getString(R.string.request_url));
                    return true;
                    // 调用请求通过服务器该工单已经收到
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
                    nEntity.setSign(0);// 未阅读
                    nEntity.setPublish_time(jsonObject.getString("publishDate"));

                    long id = noticeDB.insert(nEntity);
                    if (id > 0) {
                        showNotification(ctx, "接收到新的通知 " + jsonObject.getString("title"), 1);
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

    // 发送http请求通知服务已经接收到工单
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
                Log.d(TAG, "通知得到工单成功！");
            } else {
                Log.d(TAG, "通知得到工单失败！");
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

    // 该方法暂时不用
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

            // 置完成工单标记
            if (agree) {
                olDB.updateSignData(order_num, FinalVariables.ORDER_STATE_APPLY_CHANGE);
                osEntity.setOrder_state(FinalVariables.ORDER_STATE_APPLY_CHANGE); // 完成工单
            } else {
                // olDB.updateSignData(order_num,
                // FinalVariables.ORDER_STATE_APPLY_NOT_PASS);
                // osEntity.setOrder_state(FinalVariables.ORDER_STATE_APPLY_NOT_PASS);
                // //完成工单
            }

            // 写入工单当前状态到本地数据库
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
            if (type.equals("revenue")) // 税务数据更新
            {

            } else if (type.equals("problem_category")) // 问题类别
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
            } else if (type.equals("problem_type")) // 问题类型
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
            } else if (type.equals("expense_item")) // 费用
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
            // 用默认字符编码解码字符串。
            byte[] bs = str.getBytes();
            // 用新的字符编码生成字符串
            try {
                return new String(bs, newCharset);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
