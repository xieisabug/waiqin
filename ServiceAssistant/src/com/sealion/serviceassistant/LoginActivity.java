package com.sealion.serviceassistant;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.sealion.serviceassistant.entity.NetBackDataEntity;
import com.sealion.serviceassistant.exception.NetAccessException;
import com.sealion.serviceassistant.net.HttpRestAchieve;
import com.sealion.serviceassistant.superactivity.SuperActivity;
import com.sealion.serviceassistant.tools.Config;
import com.sealion.serviceassistant.tools.DateTools;
import com.sealion.serviceassistant.tools.NetworkTool;
import com.sealion.serviceassistant.tools.UpdateApplication;
import com.sealion.serviceassistant.widget.PopWin;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources.NotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ��¼���û������û�����������е�¼���û���Ϊ�󶨵��ֻ���
 */
public class LoginActivity extends SuperActivity {
	private static final String TAG = "";
	private EditText username = null;
	private EditText password = null;
	private CheckBox remember_pwd_check = null;
	private TextView forget_pwd = null;
	protected SharedPreferences check_sp = null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.login);

		username = (EditText) this.findViewById(R.id.username);
		password = (EditText) this.findViewById(R.id.password);
		remember_pwd_check = (CheckBox) this.findViewById(R.id.remember_pwd);
		check_sp = getSharedPreferences("CHECK_CARD", Context.MODE_PRIVATE);

		forget_pwd = (TextView) this.findViewById(R.id.forget_pwd);

		forget_pwd.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				PopWin popWin = new PopWin(LoginActivity.this);
				popWin.initPopWindow(getResources()
						.getString(R.string.find_pwd), 0, null, null);
			}
		});
	}

	@Override
	public void onStart() {
		super.onStart();
		UmengUpdateAgent.update(this);
		String userid = sp.getString("PHONENO", "");
		username.setText(userid);

		int REMPWD = sp.getInt("REMPWD", 0);
		if (REMPWD == 1) {
			String pwd = sp.getString("PASSWORD", "");
			password.setText(pwd);
            remember_pwd_check.setChecked(true);
		}
		// debug

//		username.setText("15367898032");
//		password.setText("111111");
        //������������������Ƿ���Ҫ���£�������粿����������ʾ�������
		if (NetworkTool.getLocalIpAddress().equals("")) {
			Builder builder = new AlertDialog.Builder(LoginActivity.this);
			builder.setMessage("��������?")
					.setTitle("�������")
					.setPositiveButton("��������",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int id) {
									startActivity(new Intent(
											Settings.ACTION_WIRELESS_SETTINGS));
								}
							})
					.setNegativeButton("ȡ��",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
			builder.show();
		} else {
			final UpdateApplication update = new UpdateApplication(
					getResources().getString(R.string.update_version_address),
					LoginActivity.this);

			new AsyncTask<Void, Void, Integer>() {
				@Override
				protected void onPreExecute() {

				}

				@Override
				protected Integer doInBackground(Void... params) {

					return update.getServerVerCode();
				}

				@Override
				protected void onPostExecute(Integer result) {
					int vercode = Config.getVerCode(LoginActivity.this);

					if (result > vercode) {
						update.doNewVersionUpdate();
					}
				}
			}.execute();

		}
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

    /**
     * �����¼��ťִ�е��¼�
     * @param target ����İ�ť
     */
	public void LoginBtnClick(View target) {
		String username_value = username.getText().toString();
		String password_value = password.getText().toString();

		// this.startActivity(new Intent(this,DesktopActivity.class));
		// this.startActivity(new Intent(this,SolvePaperActivity.class));
		// this.finish();
		TelephonyManager tele = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);
		String deviceId = tele.getDeviceId();
		// executeLoginReauest(username_value,password_value,"123456");
		if (username_value.equals("")) {
			Toast.makeText(this, "�û�������Ϊ�գ�", Toast.LENGTH_LONG).show();
			super.CancelDialog();
			return;
		}

		if (password_value.equals("")) {
			Toast.makeText(this, "���벻��Ϊ�գ�", Toast.LENGTH_LONG).show();
			super.CancelDialog();
			return;
		}

		// new
		// PostData().execute(username_value,password_value,"a100002398fdbe");
		new PostData().execute(username_value, password_value,
				deviceId.toLowerCase());
	}

    /**
     * �첽��¼
     */
	private class PostData extends AsyncTask<String, Void, Integer> {
		@Override
		protected void onPreExecute() {
			ShowDialog();
		}

		@Override
		protected Integer doInBackground(String... params) {
            return postData(params[0], params[1], params[2]);
		}

		@Override
		protected void onPostExecute(Integer result) {

			// doInBackground����ʱ���������仰˵������doInBackgroundִ����󴥷�
			// �����result��������doInBackgroundִ�к�ķ���ֵ������������"ִ�����"
			CancelDialog();

			if (result == 0) {
				CancelDialog();
				startActivity(new Intent(LoginActivity.this,
						DesktopActivity.class));
				finish();
			} else if (result == 1) {
				Toast.makeText(LoginActivity.this, "����ʧ�ܣ������ԣ�",
						Toast.LENGTH_LONG).show();
				PopWin popWin = new PopWin(LoginActivity.this);
				popWin.initPopWindow("���ڵ�ǰû�������κ�����,�����Ƿ��������ִ���,�Ƿ�������ߵ�¼��", 3,
						null, LoginActivity.this);
			} else if (result == 2) {
				Toast.makeText(LoginActivity.this, "�������ڲ�����",
						Toast.LENGTH_LONG).show();
			} else if (result == 3) {
				Toast.makeText(LoginActivity.this, "����ʧ��,�����쳣��",
						Toast.LENGTH_LONG).show();
				PopWin popWin = new PopWin(LoginActivity.this);
				popWin.initPopWindow("���ڵ�ǰû�������κ�����,�����Ƿ��������ִ���,�Ƿ�������ߵ�¼��", 3,
						null, LoginActivity.this);
			} else if (result == 4) {
				Toast.makeText(LoginActivity.this, "����ʧ�ܣ����ݽ����쳣��",
						Toast.LENGTH_LONG).show();
			} else if (result == 5) {
				Toast.makeText(LoginActivity.this, "����ʧ�ܣ��û������������",
						Toast.LENGTH_LONG).show();
			}
		}
	}

    /**
     * ��¼����
     * @param params ��¼�Ĳ���
     * @return �������
     */
	public int postData(String... params) {
		int back_data;
		HttpRestAchieve httpRestAchieve = new HttpRestAchieve();
		HashMap<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("phoneNo", params[0]);
		paramsMap.put("password", params[1]);
		// Master Version use 99999999999999
		String UMENG_CHANNEL = Config.getMetaValue(this, "UMENG_CHANNEL");
		Log.d("70Apps","UMENG_CHANNEL"+ UMENG_CHANNEL);

		if (UMENG_CHANNEL.equalsIgnoreCase("master")) {
			Log.d("70Apps", "master");
			paramsMap.put("meid", "99999999999999");
		} else {
			Log.d("70Apps", "user");
			paramsMap.put("meid", params[2]);
		}

		// Log.d("70Apps",params[2]);
		// paramsMap.put("meid", "a1000008888888");15367898179
		// paramsMap.put("meid", "a1000023995870");//15367898034

		try {

			NetBackDataEntity netBackData = httpRestAchieve.postRequestData(
					request_url + "/fieldworker/login", paramsMap);
			String data = netBackData.getData();

			JSONObject jsonObject = new JSONObject(data);
			int result = jsonObject.getInt("resultCode");
			if (result == 1) {
				JSONObject loginObj = jsonObject.getJSONObject("fieldWorker");
				JSONObject departmentObj = loginObj.getJSONObject("department");
				// ��¼�ɹ��󽫵�¼��Ϣд�뵽SharedPreference
				Editor edit = sp.edit();
				edit.putString("AREAID", departmentObj.getString("city")); // �������
				edit.putString("USERNAME", loginObj.getString("id")); // �û�ID
				edit.putString("FULLNAME", loginObj.getString("fullname")); // �û�����
				// edit.putString("WORKERNO", loginObj.getString("workerNo"));
				edit.putString("PHONENO", params[0]);
				if (remember_pwd_check.isChecked()) // ��ס����
				{
					edit.putString("PASSWORD", params[1]); // �û�ID
					edit.putInt("REMPWD", 1); // ��ס����
				}

				edit.commit();
				// ��ת����������ҳ��
				String check_in_date = check_sp.getString("CHECK_IN_DATE", "");
				if (!check_in_date.equals(DateTools.getFormatDate())) {
					// ǩ������д�뵱ǰʱ��
					Editor check_sp_edit = check_sp.edit();
					check_sp_edit.putString("CHECK_IN_DATE",
							DateTools.getFormatDate());
					check_sp_edit.commit();
				}

				back_data = 0;
			} else if (result == 3) {
				back_data = 5;
			} else {
				back_data = 1;
			}

		} catch (NotFoundException e) {
			back_data = 2;
			// Toast.makeText(this, "����������", Toast.LENGTH_LONG).show();
			e.printStackTrace();
			Log.d(TAG, e.getMessage());
		} catch (NetAccessException e) {
			back_data = 3;
			// Toast.makeText(this, "����ʧ��,�����쳣��", Toast.LENGTH_LONG).show();
			e.printStackTrace();
			Log.d(TAG, e.getMessage());
		} catch (JSONException e) {
			back_data = 4;
			// Toast.makeText(this, "����ʧ�ܣ����ݽ����쳣��", Toast.LENGTH_LONG).show();
			e.printStackTrace();
			Log.d(TAG, e.getMessage());
		}

		return back_data;
	}

}
