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
     * ��ȡ�汾����
     * @param context ������
     * @return �汾����
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
     * ��ȡ�汾��
     * @param context ������
     * @return �汾��
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
     * ��ȡӦ����
     * @param context ������
     * @return Ӧ����
     */
	public static String getAppName(Context context)
	{
        return context.getResources().getText(R.string.app_name).toString();
	}

    /**
     * ��ȡһ��UUID
     * @return UUIDֵ
     */
	public static long getUUID()
	{
		UUID generator = UUID.randomUUID();
		return generator.getMostSignificantBits();
	}

    /**
     * ��ȡmetadataԪ�����е�ֵ
     * @param context ������
     * @param metaKey ��
     * @return metadataֵ
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
