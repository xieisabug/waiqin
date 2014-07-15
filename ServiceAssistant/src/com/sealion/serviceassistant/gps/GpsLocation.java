package com.sealion.serviceassistant.gps;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.widget.Toast;
import com.sealion.serviceassistant.entity.LocationEntity;

/**
 * GPS������
 */
public class GpsLocation {
    private Context ctx;

    public GpsLocation(Context ctx) {
        this.ctx = ctx;
    }

    /**
     * GPS�Ƿ���
     *
     * @return GPS�Ƿ���
     */
    public boolean isGPSEnable() {
        /*
		 * ��Setting.System����ȡҲ���ԣ�ֻ�����Ǹ��ɵ��÷� String str =
		 * Settings.System.getString(getContentResolver(),
		 * Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		 */
        String str = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (str != null) {
            return str.contains("gps");
        } else {
            return false;
        }
    }

    /**
     * �򿪣��ر�GPS
     */
    public void toggleGPS() {
        Intent gpsIntent = new Intent();
        gpsIntent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
        gpsIntent.addCategory("android.intent.category.ALTERNATIVE");
        gpsIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(ctx, 0, gpsIntent, 0).send();
        } catch (CanceledException e) {
            e.printStackTrace();
        }
    }

    /**
     * ��ȡλ����Ϣ
     *
     * @return λ����Ϣʵ��
     */
    public LocationEntity getLocation() {
        // ��ȡλ�ù������
        LocationManager locationManager;
        String serviceName = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) ctx.getSystemService(serviceName);
        // ���ҵ�������Ϣ
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE); // �߾���
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW); // �͹���
        String provider = locationManager.getBestProvider(criteria, true); // ��ȡGPS��Ϣ
        LocationEntity lEntity = new LocationEntity();
        if (provider != null && !provider.equals("")) {
            Location location = locationManager.getLastKnownLocation(provider); // ͨ��GPS��ȡλ��
            if (location != null) {
                lEntity.setLatitude(Double.parseDouble(String.format("%.5f", location.getLatitude())));
                lEntity.setLongitude(Double.parseDouble(String.format("%.5f", location.getLongitude())));
            }

        } else {
            Toast.makeText(ctx, "���ܻ�ȡGPS��Ϣ���Ƿ�û����SIM����", Toast.LENGTH_LONG).show();
        }
        return lEntity;
    }

}
