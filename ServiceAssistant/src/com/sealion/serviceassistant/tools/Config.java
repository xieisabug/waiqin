package com.sealion.serviceassistant.tools;

import java.util.UUID;

import com.sealion.serviceassistant.R;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;

public class Config
{
	private static final String TAG = "Config";

	public static final String UPDATE_APKNAME = "ServiceAssistant.apk";
	public static final String UPDATE_VERJSON = "ver.json";
	public static final String UPDATE_SAVENAME = "ServiceAssistant.apk";

    /**
     * 获取版本代码
     * @param context 上下文
     * @return 版本代码
     */
	public static int getVerCode(Context context)
	{
		int verCode = -1;
		try
		{
			verCode = context.getPackageManager().getPackageInfo("com.sealion.serviceassistant", 0).versionCode;
		}
		catch (NameNotFoundException e)
		{
			Log.e(TAG, e.getMessage());
		}
		return verCode;
	}

    /**
     * 获取版本名
     * @param context 上下文
     * @return 版本名
     */
	public static String getVerName(Context context)
	{
		String verName = "";
		try
		{
			verName = context.getPackageManager().getPackageInfo("com.sealion.serviceassistant", 0).versionName;
		}
		catch (NameNotFoundException e)
		{
			Log.e(TAG, e.getMessage());
		}
		return verName;

	}

    /**
     * 获取应用名
     * @param context 上下文
     * @return 应用名
     */
	public static String getAppName(Context context)
	{
        return context.getResources().getText(R.string.app_name).toString();
	}

    /**
     * 获取一个UUID
     * @return UUID值
     */
	public static long getUUID()
	{
		UUID generator = UUID.randomUUID();
		return generator.getMostSignificantBits();
	}

    /**
     * 获取metadata元数据中的值
     * @param context 上下文
     * @param metaKey 键
     * @return metadata值
     */
	public static String getMetaValue(Context context, String metaKey) {
		Bundle metaData = null;
		String apiKey = null;
		if (context == null || metaKey == null) {
			return null;
		}
		try {
			ApplicationInfo ai = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			if (null != ai) {
				metaData = ai.metaData;
			}
			if (null != metaData) {
				apiKey = metaData.getString(metaKey);
			}
		} catch (NameNotFoundException e) {

		}
		return apiKey;
	}
}
