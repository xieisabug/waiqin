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
 * 登录，用户输入用户名和密码进行登录，用户名为绑定的手机号
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
        //如果网络正常，则检测是否需要更新，如果网络部正常，则提示网络错误。
		if (NetworkTool.getLocalIpAddress().equals("")) {
			Builder builder = new AlertDialog.Builder(LoginActivity.this);
			builder.setMessage("设置网络?")
					.setTitle("网络错误")
					.setPositiveButton("设置网络",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int id) {
									startActivity(new Intent(
											Settings.ACTION_WIRELESS_SETTINGS));
								}
							})
					.setNegativeButton("取消",
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
     * 点击登录按钮执行的事件
     * @param target 点击的按钮
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
			Toast.makeText(this, "用户名不能为空！", Toast.LENGTH_LONG).show();
			super.CancelDialog();
			return;
		}

		if (password_value.equals("")) {
			Toast.makeText(this, "密码不能为空！", Toast.LENGTH_LONG).show();
			super.CancelDialog();
			return;
		}

		// new
		// PostData().execute(username_value,password_value,"a100002398fdbe");
		new PostData().execute(username_value, password_value,
				deviceId.toLowerCase());
	}

    /**
     * 异步登录
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

			// doInBackground返回时触发，换句话说，就是doInBackground执行完后触发
			// 这里的result就是上面doInBackground执行后的返回值，所以这里是"执行完毕"
			CancelDialog();

			if (result == 0) {
				CancelDialog();
				startActivity(new Intent(LoginActivity.this,
						DesktopActivity.class));
				finish();
			} else if (result == 1) {
				Toast.makeText(LoginActivity.this, "操作失败，请重试！",
						Toast.LENGTH_LONG).show();
				PopWin popWin = new PopWin(LoginActivity.this);
				popWin.initPopWindow("由于当前没有连接任何网络,或者是服务器出现错误,是否进行离线登录？", 3,
						null, LoginActivity.this);
			} else if (result == 2) {
				Toast.makeText(LoginActivity.this, "服务器内部错误！",
						Toast.LENGTH_LONG).show();
			} else if (result == 3) {
				Toast.makeText(LoginActivity.this, "操作失败,网络异常！",
						Toast.LENGTH_LONG).show();
				PopWin popWin = new PopWin(LoginActivity.this);
				popWin.initPopWindow("由于当前没有连接任何网络,或者是服务器出现错误,是否进行离线登录？", 3,
						null, LoginActivity.this);
			} else if (result == 4) {
				Toast.makeText(LoginActivity.this, "操作失败，数据解析异常！",
						Toast.LENGTH_LONG).show();
			} else if (result == 5) {
				Toast.makeText(LoginActivity.this, "操作失败，用户名或密码错误！",
						Toast.LENGTH_LONG).show();
			}
		}
	}

    /**
     * 登录操作
     * @param params 登录的参数
     * @return 错误代码
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
				// 登录成功后将登录信息写入到SharedPreference
				Editor edit = sp.edit();
				edit.putString("AREAID", departmentObj.getString("city")); // 区域代码
				edit.putString("USERNAME", loginObj.getString("id")); // 用户ID
				edit.putString("FULLNAME", loginObj.getString("fullname")); // 用户姓名
				// edit.putString("WORKERNO", loginObj.getString("workerNo"));
				edit.putString("PHONENO", params[0]);
				if (remember_pwd_check.isChecked()) // 记住密码
				{
					edit.putString("PASSWORD", params[1]); // 用户ID
					edit.putInt("REMPWD", 1); // 记住密码
				}

				edit.commit();
				// 跳转到个人中心页面
				String check_in_date = check_sp.getString("CHECK_IN_DATE", "");
				if (!check_in_date.equals(DateTools.getFormatDate())) {
					// 签到并且写入当前时间
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
			// Toast.makeText(this, "服务器错误！", Toast.LENGTH_LONG).show();
			e.printStackTrace();
			Log.d(TAG, e.getMessage());
		} catch (NetAccessException e) {
			back_data = 3;
			// Toast.makeText(this, "操作失败,网络异常！", Toast.LENGTH_LONG).show();
			e.printStackTrace();
			Log.d(TAG, e.getMessage());
		} catch (JSONException e) {
			back_data = 4;
			// Toast.makeText(this, "操作失败，数据解析异常！", Toast.LENGTH_LONG).show();
			e.printStackTrace();
			Log.d(TAG, e.getMessage());
		}

		return back_data;
	}

}
