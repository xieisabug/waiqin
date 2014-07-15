package com.sealion.serviceassistant.gps;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.sealion.serviceassistant.DesktopActivity;
import com.sealion.serviceassistant.R;
import com.sealion.serviceassistant.db.LocationDB;
import com.sealion.serviceassistant.db.OrderListDB;
import com.sealion.serviceassistant.entity.LocationEntity;
import com.sealion.serviceassistant.entity.NetBackDataEntity;
import com.sealion.serviceassistant.exception.NetAccessException;
import com.sealion.serviceassistant.net.HttpRestAchieve;
import com.sealion.serviceassistant.tools.DateTools;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources.NotFoundException;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

/**
 * GPS����
 */
public class GpsService extends Service {
    private static final String TAG = GpsService.class.getSimpleName();
    private static NotificationManager mNotifMan;
    private static final int NOTIF_CONNECTED = 0;

    //use for location
    private LocationClient mLocationClient = null;
    private SharedPreferences sp = null;
    private SharedPreferences set_sp = null;
    private String request_url = null;
    private BDLocationListener bdLocation = null;
    private final static String NEW_NOTIF_TITLE = "���Ķ��µĹ���";

    private LocationDB lDB = null;

    private Timer circleNewsTimer = new Timer();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return 0;
    }

    TimerTask circleNewsTask = new TimerTask() {
        @Override
        public void run() {
            //��ʱ��ɨ���Ƿ����µ�δ�Ķ��Ĺ���
            OrderListDB olDB = new OrderListDB(GpsService.this);
            int count = olDB.CountOrdersByCondition(1);
            if (count > 0) {
                showNotification(GpsService.this, "���� " + count + " ��δ�Ķ��Ĺ�����");
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        sp = getSharedPreferences("USER_CARD", Context.MODE_PRIVATE);
        request_url = this.getResources().getString(R.string.request_url);
        lDB = new LocationDB(this);

        LocationAddress();

        if (mLocationClient != null) {
            mLocationClient.start();
        }

        set_sp = getSharedPreferences("SETTING_DATA", Context.MODE_PRIVATE);
        int minutes = set_sp.getInt("TIME_TIPS", 10);
        circleNewsTimer.schedule(circleNewsTask, 1000, 1000 * 60 * minutes);

        //��ʱ�������������Ƿ����ǩ��,�ȼ��ʱ���Ƿ���8:00 - 8:30 ,����Ƿ�ǩ��
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {

        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.unRegisterLocationListener(bdLocation);

            mLocationClient.stop();
            mLocationClient = null;

            Log.d(TAG, "gps service is destroy..............");
        }
        super.onDestroy();
    }

    public void LocationAddress() {
        mLocationClient = new LocationClient(this);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);                                //��gps
        option.setCoorType("bd09ll");                            //������������Ϊbd09ll
        option.setPriority(LocationClientOption.NetWorkFirst);    //������������
        option.setProdName("ServiceAssistant");                        //���ò�Ʒ������
        option.setScanSpan(30000 * 20);                                //��ʱ��λ��ÿ��10�����Ӷ�λһ�Ρ�

        mLocationClient.setLocOption(option);

        bdLocation = new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                if (location == null)
                    return;
                LocationEntity lEntity = new LocationEntity();
                lEntity.setLatitude(Double.parseDouble(String.format("%.5f", location.getLatitude())));
                lEntity.setLongitude(Double.parseDouble(String.format("%.5f", location.getLongitude())));

                if (location.getLocType() == BDLocation.TypeGpsLocation) {
                    lEntity.setAddress("");
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                    lEntity.setAddress(location.getAddrStr());
                }

                lEntity.setTime(DateTools.getFormatDateAndTime());

                //location.getLocType()
                //location.getRadius()
                //mLocationClient.getVersion()
                //location.getSatelliteNumber()
                //location.getSpeed()

                //����REST�ӿ�ͬ�����ݵ�����������γ����Ϣ��

                new Connection().execute(lEntity);
            }

            public void onReceivePoi(BDLocation location) {
                //return ;
            }
        };
        mLocationClient.registerLocationListener(bdLocation);
    }

    private class Connection extends AsyncTask<LocationEntity, Void, Void> {


        @Override
        protected Void doInBackground(LocationEntity... params) {
            sendLaticuteInfo(params[0]);
            return null;
        }

    }

    /**
     * ��ɾ�γ����Ϣͬ����������,�����쳣ʱ������д���������ݿ⣬��������ʱ��ͬ����������
     *
     * @param lEntity
     */
    public void sendLaticuteInfo(LocationEntity lEntity) {
        if (sp.getString("USERNAME", "").equals("") || sp.getString("AREAID", "").equals("")) {
            return;
        }
        HttpRestAchieve httpRestAchieve = new HttpRestAchieve();
        HashMap<String, String> paramsMap = new HashMap<String, String>();

        paramsMap.put("fieldWorkerNo", sp.getString("USERNAME", "")); //
        paramsMap.put("areaCode", sp.getString("AREAID", ""));

        paramsMap.put("latitude", lEntity.getLatitude() + "");
        paramsMap.put("longitude", lEntity.getLongitude() + "");
        paramsMap.put("address", lEntity.getAddress());
        paramsMap.put("createTime", lEntity.getTime());
        //ȡ����ע��ť������¼�
        try {
            NetBackDataEntity netBackData = httpRestAchieve.postRequestData(request_url
                    + "/fieldworker/biz/apply-track", paramsMap);
            String data = netBackData.getData();
            Log.d(TAG, "����" + data);
            JSONObject jsonObject = new JSONObject(data);
            int result = jsonObject.getInt("resultCode");
            if (result == 1) {
                Log.d(TAG, "����GPS���ݳɹ�.......");
            } else {
                //��¼���ݵ��������ݿ⣬�ȴ������绷�������ͬ��
                //lDB.insert(lEntity);
            }
        } catch (NotFoundException e) {
            lDB.insert(lEntity);
            //Toast.makeText(this, "����������", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        } catch (NetAccessException e) {
            lDB.insert(lEntity);
            //Toast.makeText(this, "����ʧ��,�����쳣��", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        } catch (JSONException e) {
            lDB.insert(lEntity);
            //Toast.makeText(this, "����ʧ�ܣ����ݽ����쳣��", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }
    }

    private static void showNotification(Context ctx, String text) {
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

        pi = PendingIntent.getActivity(ctx, 0, new Intent(ctx, DesktopActivity.class), 0);
        n.setLatestEventInfo(ctx, NEW_NOTIF_TITLE, text, pi);

        mNotifMan.notify(NOTIF_CONNECTED, n);
    }

}
