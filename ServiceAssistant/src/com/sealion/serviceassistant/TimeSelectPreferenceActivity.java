package com.sealion.serviceassistant;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * 时间选择设置界面
 */
public class TimeSelectPreferenceActivity  extends PreferenceActivity implements OnSharedPreferenceChangeListener
{
	private ListPreference lp = null;
	private SharedPreferences sp = null;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.addPreferencesFromResource(R.xml.time_preference);
		sp = getSharedPreferences("SETTING_DATA", Context.MODE_PRIVATE);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);  
        prefs.registerOnSharedPreferenceChangeListener(this); 
        lp = (ListPreference) findPreference("timeSelectListPreference");        
        lp.setTitle("当前时间间隔为"+sp.getInt("TIME_TIPS", 10)+"分钟)");
        lp.setValue(sp.getInt("TIME_TIPS", 10)+"");
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
	{
		Editor edit = sp.edit();
		
		edit.putInt("TIME_TIPS", Integer.parseInt(lp.getValue()));
		edit.commit();
		if (lp != null)
		{
			lp.setTitle("当前时间间隔为"+sp.getInt("TIME_TIPS", 10)+"分钟)");
		}
	}
}
