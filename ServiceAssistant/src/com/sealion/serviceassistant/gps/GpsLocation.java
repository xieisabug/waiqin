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
 * GPS辅助类
 */
public class GpsLocation {
    private Context ctx;

    public GpsLocation(Context ctx) {
        this.ctx = ctx;
    }

    /**
     * GPS是否开启
     *
     * @return GPS是否开启
     */
    public boolean isGPSEnable() {
        /*
		 * 用Setting.System来读取也可以，只是这是更旧的用法 String str =
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
     * 打开，关闭GPS
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
     * 获取位置信息
     *
     * @return 位置信息实体
     */
    public LocationEntity getLocation() {
        // 获取位置管理服务
        LocationManager locationManager;
        String serviceName = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) ctx.getSystemService(serviceName);
        // 查找到服务信息
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE); // 高精度
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW); // 低功耗
        String provider = locationManager.getBestProvider(criteria, true); // 获取GPS信息
        LocationEntity lEntity = new LocationEntity();
        if (provider != null && !provider.equals("")) {
            Location location = locationManager.getLastKnownLocation(provider); // 通过GPS获取位置
            if (location != null) {
                lEntity.setLatitude(Double.parseDouble(String.format("%.5f", location.getLatitude())));
                lEntity.setLongitude(Double.parseDouble(String.format("%.5f", location.getLongitude())));
            }

        } else {
            Toast.makeText(ctx, "不能获取GPS信息，是否没插入SIM卡？", Toast.LENGTH_LONG).show();
        }
        return lEntity;
    }

}
