package com.sealion.serviceassistant;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.sealion.serviceassistant.entity.NetBackDataEntity;
import com.sealion.serviceassistant.exception.NetAccessException;
import com.sealion.serviceassistant.gps.GpsService;
import com.sealion.serviceassistant.mqtt.MQTTService;
import com.sealion.serviceassistant.mqtt.MqttAdhere;
import com.sealion.serviceassistant.net.HttpRestAchieve;
import com.sealion.serviceassistant.tools.Config;
import com.sealion.serviceassistant.tools.FinalVariables;
import com.sealion.serviceassistant.tools.NetworkTool;
import com.sealion.serviceassistant.tools.UpdateApplication;
import com.sealion.serviceassistant.widget.PopMenu;
import com.sealion.serviceassistant.widget.ViewHandler;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources.NotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

/**
 * APP的操作桌面
 */
public class DesktopActivity extends ActivityGroup {
	private static final String TAG = DesktopActivity.class.getSimpleName();

	private int selected = 0;
	public LinearLayout container;
	private ImageView drop_down_menu = null;//下拉按钮
    private ImageView sign_in = null;//签到按钮
    private PopMenu popMenu = null;
    private ImageView top_bar_logo = null;
    private TextView top_bar_title = null;

	private ProgressDialog progressDig = null;
    private StatusUpdateReceiver statusUpdateIntentReceiver;

	private MQTTMessageReceiver messageIntentReceiver;
    private OrderTypeReceiver orderTypeReceiver;

	private NoticeReceiver noticeReceiver;

	private SharedPreferences sp = null;
    private Intent svc = null;

	private Intent gpsService = null;

	private int type_to_order = 0;

	private Button orderManageBtnClick = null;
    private ImageView back_img_btn = null;
	private long exitTime = 0;
	private String USERNAME = "";

    /**
     * 控件初始化，注册事件，启动mqtt服务，注册receiver，gps服务初始化
     */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.desktop);
		drop_down_menu = (ImageView) this
				.findViewById(R.id.topbar_dropdown_btn);
		sign_in = (ImageView) this.findViewById(R.id.sign_in);
		sign_in.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 签到事件
				new PostSignInData().execute();
			}
		});
		container = (LinearLayout) findViewById(R.id.Container);
		top_bar_logo = (ImageView) findViewById(R.id.top_bar_logo);
		top_bar_title = (TextView) this.findViewById(R.id.top_bar_title);
		orderManageBtnClick = (Button) findViewById(R.id.orderManageBtnClick);

		// 隐藏返回按钮
		back_img_btn = (ImageView) this.findViewById(R.id.back_img_btn);
		back_img_btn.setVisibility(View.GONE);

		switchActivity(selected);
		if (selected == 0) {
			setImageViewBgNormal(1, 2, 3);
			Button btn = (Button) this.findViewById(R.id.homeBtnClick);
			setImageViewBgFocus(btn, 0);
			top_bar_title.setText("");
		}
		sp = getSharedPreferences("USER_CARD", Context.MODE_PRIVATE);
		USERNAME = sp.getString("USERNAME", "");
		// MQTT服务初始化-------------------------------------------------------------------------
		SharedPreferences settings = getSharedPreferences(MQTTService.APP_ID, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("broker",
				this.getResources().getString(R.string.server_ip_address)); // CHANGE
																			// ME
																			// to
																			// your
																			// broker
																			// address
		editor.putString("topic", "/orderinfo/" + USERNAME + "/#,/notification"); // CHANGE
																					// ME
																					// to
																					// your
																					// topic
		editor.putString("USERNAME", USERNAME);
		editor.commit();

		statusUpdateIntentReceiver = new StatusUpdateReceiver();
		IntentFilter intentSFilter = new IntentFilter(
				MQTTService.MQTT_STATUS_INTENT);
		registerReceiver(statusUpdateIntentReceiver, intentSFilter);

		messageIntentReceiver = new MQTTMessageReceiver();
		IntentFilter intentCFilter = new IntentFilter(
				MQTTService.MQTT_MSG_RECEIVED_INTENT);
		registerReceiver(messageIntentReceiver, intentCFilter);

		orderTypeReceiver = new OrderTypeReceiver();
		IntentFilter intentFilter = new IntentFilter(
				HomeActivity.ORDER_TYPE_SELECTED);
		registerReceiver(orderTypeReceiver, intentFilter);

		noticeReceiver = new NoticeReceiver();
		IntentFilter noticeFilter = new IntentFilter(HomeActivity.NOTICE_CLICK);
		registerReceiver(noticeReceiver, noticeFilter);

		svc = new Intent(this, MQTTService.class);
		startService(svc);

		// GPS服务初始化--------------------------------------------------------------------------
		gpsService = new Intent(this, GpsService.class);
		startService(gpsService);
	}

	@Override
	public void onStart() {
		super.onStart();
		UmengUpdateAgent.update(this);
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

	public void switchActivity(int selected) {
		container.removeAllViews();
		Intent intent = null;
		String tag = "";
		if (selected == 0) {
			intent = new Intent(DesktopActivity.this, HomeActivity.class);
			hideDropDownMenu();
			showSignInBtn();
			top_bar_title.setText("主页");
			tag = "tabActivity01";
		} else if (selected == 1) {
			intent = new Intent(DesktopActivity.this, OrderListActivity.class);
			tag = "tabActivity02";
			hideSignInBtn();
			showDropDownMenu();
		} else if (selected == 2) {
			intent = new Intent(DesktopActivity.this, NoticeActivity.class);
			hideDropDownMenu();
			hideSignInBtn();
			top_bar_title.setText("公告通知");
			tag = "tabActivity03";
		}
		Log.d(TAG, "type_to_order to start : " + type_to_order);
		Window subActivity = getLocalActivityManager().startActivity(tag,
				intent);
		// overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
		// Animation anim = AnimationUtils.loadAnimation(this,
		// R.anim.push_left_in);
		// 容器添加View
		// container.setBackgroundColor(this.getResources().getColor(R.drawable.content_panel_bg));
		container.addView(subActivity.getDecorView(), LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		// container.startAnimation(anim);
	}

	public void HomeBtnClick(View target) {
		setImageViewBgNormal(1, 2, 3);
		setImageViewBgFocus(target, 0);
		top_bar_title.setText("");
		switchActivity(0);
	}

	public void OrderManageBtnClick(View target) {
		activiMenu(target);
		top_bar_title.setText("工单管理");
		switchActivity(1);

		Intent it = new Intent(HomeActivity.ORDER_TYPE_SELECTED);
		it.putExtra("type", FinalVariables.ORDER_TYPE_ALL);
		sendBroadcast(it);
	}

	public void NoticeBtnClick(View target) {
		setImageViewBgNormal(0, 1, 3);
		setImageViewBgFocus(target, 2);
		top_bar_title.setText("");
		switchActivity(2);
	}

	public void MoreBtnClick(View target) {
		hideDropDownMenu();
		setImageViewBgNormal(0, 1, 2);
		setImageViewBgFocus(target, 3);
		popMenu = new PopMenu(this);
		popMenu.addItems(new String[] { "重置密码", "同步基础数据", "版本升级", "上传文件",
				"WIFI设置", "设置", "关于", "退出程序" });

		// 弹出菜单监听器
		OnItemClickListener popmenuItemClickListener = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent intent = null;
				if (position == FinalVariables.MORE_MENU_CHANGE_PWD) // 重置密码
				{
					intent = new Intent(DesktopActivity.this,
							ChangePwdActivity.class);
				} else if (position == FinalVariables.MORE_MENU_SYNC_DATA) { // 同步基础数据
					new PostBaseData().execute("revenue", "problem_category",
							"problem_type", "expense_item");
				} else if (position == FinalVariables.MORE_MENU_VERSION_UPDATE) { // 版本升级
					if (NetworkTool.getLocalIpAddress().equals("")) {
						Builder builder = new AlertDialog.Builder(
								DesktopActivity.this);
						builder.setMessage("设置网络?")
								.setTitle("网络错误")
								.setPositiveButton("设置网络",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialog,
													int id) {
												startActivity(new Intent(
														Settings.ACTION_WIRELESS_SETTINGS));
											}
										})
								.setNegativeButton("取消",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialog,
													int id) {
												dialog.cancel();
											}
										});
						builder.show();
					} else {
						final UpdateApplication update = new UpdateApplication(
								getResources().getString(
										R.string.update_version_address),
								DesktopActivity.this);

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
								int vercode = Config
										.getVerCode(DesktopActivity.this);

								if (result > vercode) {
									update.doNewVersionUpdate();
								}
							}
						}.execute();

					}
				} else if (position == FinalVariables.MORE_MENU_UPLOAD_FILE)// 上传文件
				{
					intent = new Intent(DesktopActivity.this,
							OrderFileUploadActivity.class);
				} else if (position == FinalVariables.MORE_MENU_LOGOUT) // 退出程序
				{
					dialog();
				} else if (position == FinalVariables.MORE_MENU_SETTING) // 设置
				{
					intent = new Intent(DesktopActivity.this,
							SettingActivity.class);
				} else if (position == FinalVariables.MORE_MENU_ABOUT) // 关于
				{
					intent = new Intent(DesktopActivity.this,
							AboutActivity.class);
				} else if (position == FinalVariables.MORE_MENU_WIFI_SETTING) {
					intent = new Intent("android.settings.WIFI_SETTINGS");
				}
				if (intent != null) {
					startActivity(intent);
				}
				popMenu.dismiss();
			}
		};

		// 菜单项点击监听器
		popMenu.setOnItemClickListener(popmenuItemClickListener);
		popMenu.showAsDropDown(target);
	}

	private void ExitApp() {
		Log.d(TAG, "退出应用！");
		if (svc != null) {
			stopService(svc);
			Log.d(TAG, "stop the mqtt service....");
		}

		if (gpsService != null) {
			stopService(gpsService);
			Log.d(TAG, "stop the gps service....");
		}
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
	}

	AlertDialog.Builder builder = null;

	protected void dialog() {
		builder = new Builder(DesktopActivity.this);
		builder.setMessage("确定要退出外勤管理吗?");
		builder.setTitle("提示");
		builder.setPositiveButton("确认",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();

						finish();
						ExitApp();
					}
				});
		builder.setNegativeButton("取消",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						builder = null;
					}
				});
		builder.create().show();
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (event.getAction() == KeyEvent.ACTION_DOWN
					&& event.getRepeatCount() == 0) {
				if ((System.currentTimeMillis() - exitTime) > 2000) {
					Toast.makeText(getApplicationContext(), "再按一次退出程序",
							Toast.LENGTH_SHORT).show();
					exitTime = System.currentTimeMillis();
				} else {
					Intent home = new Intent(Intent.ACTION_MAIN);
					home.addCategory(Intent.CATEGORY_HOME);
					startActivity(home);
				}
			}
			return true;
		}
		return super.dispatchKeyEvent(event);
	}

    /**
     * 设置图片背景变为未点击状态
     * @param args 按钮位置数组
     */
	private void setImageViewBgNormal(int... args) {
		View view;
		for (int num : args) {
			if (num == 0) {
				view = this.findViewById(R.id.homeBtnClick);
				view.setBackgroundResource(R.drawable.home_normal);
			} else if (num == 1) {
				view = this.findViewById(R.id.orderManageBtnClick);
				view.setBackgroundResource(R.drawable.order_normal);
			} else if (num == 2) {
				view = this.findViewById(R.id.noticeBtnClick);
				view.setBackgroundResource(R.drawable.notice_normal);
			} else {
				view = this.findViewById(R.id.moreBtnClick);
				view.setBackgroundResource(R.drawable.more_normal);
			}
		}

	}

    /**
     * 设置被点击的按钮为按下背景
     * @param view 被点击的按钮
     * @param num 点击按钮的位置
     */
	private void setImageViewBgFocus(View view, int num) {
		if (num == 0) {
			view.setBackgroundResource(R.drawable.home_focus);
		} else if (num == 1) {
			view.setBackgroundResource(R.drawable.order_focus);
		} else if (num == 2) {
			view.setBackgroundResource(R.drawable.notice_focus);
		} else {
			view.setBackgroundResource(R.drawable.more_focus);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		/*
		 * add()方法的四个参数，依次是： 1、组别，如果不分组的话就写Menu.NONE,
		 * 2、Id，这个很重要，Android根据这个Id来确定不同的菜单 3、顺序，那个菜单现在在前面由这个参数的大小决定
		 * 4、文本，菜单的显示文本
		 */
		menu.add(Menu.NONE, Menu.FIRST + 1, 1, "退出").setIcon(
				R.drawable.iocn_out);

		// return true才会起作用
		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case Menu.FIRST + 1:
			dialog();
			break;
		}
		return false;
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.d(TAG, "stop the desktop activity....");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "destory the desktop activity....");
		// Intent svc = new Intent(this, MQTTService.class);

		unregisterReceiver(statusUpdateIntentReceiver);
		unregisterReceiver(messageIntentReceiver);
		unregisterReceiver(orderTypeReceiver);
		unregisterReceiver(noticeReceiver);
	}

	public class StatusUpdateReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle notificationData = intent.getExtras();
			String newStatus = notificationData
					.getString(MQTTService.MQTT_STATUS_MSG);
			Log.d(TAG, newStatus);
		}
	}

	public class MQTTMessageReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle notificationData = intent.getExtras();
			/* The topic of this message. */
			String newTopic = notificationData
					.getString(MQTTService.MQTT_MSG_RECEIVED_TOPIC);
			/* The message payload. */
			String newData = notificationData
					.getString(MQTTService.MQTT_MSG_RECEIVED_MSG);
			Log.d(TAG, newTopic + ":" + newData);
			/* Display the payload on the text label. */
			// mText.setText(newData);
		}
	}

	public class NoticeReceiver extends BroadcastReceiver {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.content.BroadcastReceiver#onReceive(android.content.Context,
		 * android.content.Intent)
		 */
		@Override
		public void onReceive(Context context, Intent intent) {
			setImageViewBgNormal(0, 1, 3);
			Button btn = (Button) findViewById(R.id.noticeBtnClick);
			setImageViewBgFocus(btn, 2);
			top_bar_title.setText("");
			switchActivity(2);
		}

	}

	public class OrderTypeReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			int type = intent.getExtras().getInt("type");

			Log.d(TAG, "receive msg " + type);

			if (type == FinalVariables.ORDER_TYPE_URGENCY) // 紧急工单
			{
				type_to_order = FinalVariables.ORDER_TYPE_URGENCY;
				top_bar_title.setText("紧急工单");
			} else if (type == FinalVariables.ORDER_TYPE_NEW) // 普通工单
			{
				type_to_order = FinalVariables.ORDER_TYPE_NEW;
				top_bar_title.setText("普通工单");
			} else if (type == FinalVariables.ORDER_TYPE_COMPLISH) // 完成工单
			{
				type_to_order = FinalVariables.ORDER_TYPE_COMPLISH;
				top_bar_title.setText("完成工单");
			} else if (type == FinalVariables.ORDER_TYPE_NOT_COMPLISH) // 未完成工单
			{
				type_to_order = FinalVariables.ORDER_TYPE_NOT_COMPLISH;
				top_bar_title.setText("未完成工单");
			} else if (type == FinalVariables.ORDER_TYPE_BACK) {
				type_to_order = FinalVariables.ORDER_TYPE_BACK;
				top_bar_title.setText("回访工单");
			}
			switchActivity(1);
			activiMenu(orderManageBtnClick);

			Intent it = new Intent("SEARCH_SEARCH_BROADCAST");
			it.putExtra("position", type_to_order);
			DesktopActivity.this.sendBroadcast(it);
		}
	}

	public void activiMenu(View target) {
		setImageViewBgNormal(0, 2, 3);
		setImageViewBgFocus(target, 1);
		if (drop_down_menu != null) {
			drop_down_menu.setVisibility(View.VISIBLE);
			// 初始化弹出菜单
			popMenu = new PopMenu(this);

            // 为弹出式菜单添加菜单项
			final String[] typeArray = new String[] { "显示全部工单", "显示普通新工单",
					"显示未完成工单", "显示已完成工单", "显示紧急工单", "显示回访工单" };
			popMenu.addItems(typeArray);

			// 弹出菜单监听器
			OnItemClickListener popmenuItemClickListener = new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					popMenu.ClearAllSelectItem();
					ImageView iView = (ImageView) view
							.findViewById(R.id.select_item);
					iView.setBackgroundResource(R.drawable.pop_up_check_box);

					Intent intent = new Intent("SEARCH_SEARCH_BROADCAST");
					if (position == FinalVariables.ORDER_TYPE_ALL) // 显示全部工单
					{
						top_bar_title.setText("全部工单");
						intent.putExtra("position",
								FinalVariables.ORDER_TYPE_ALL);
					} else if (position == FinalVariables.ORDER_TYPE_NEW) // 显示新工单
					{
						top_bar_title.setText("新工单");
						intent.putExtra("position",
								FinalVariables.ORDER_TYPE_NEW);
					} else if (position == FinalVariables.ORDER_TYPE_NOT_COMPLISH)// 显示未完成工单
					{
						top_bar_title.setText("未完成工单");
						intent.putExtra("position",
								FinalVariables.ORDER_TYPE_NOT_COMPLISH);
					} else if (position == FinalVariables.ORDER_TYPE_COMPLISH) // 显示已完成工单
					{
						top_bar_title.setText("已完成工单");
						intent.putExtra("position",
								FinalVariables.ORDER_TYPE_COMPLISH);
					} else if (position == FinalVariables.ORDER_TYPE_URGENCY) {
						top_bar_title.setText("紧急工单");
						intent.putExtra("position",
								FinalVariables.ORDER_TYPE_URGENCY);
					} else if (position == FinalVariables.ORDER_TYPE_BACK) {
						top_bar_title.setText("回访工单");
						intent.putExtra("position",
								FinalVariables.ORDER_TYPE_BACK);
					}
					DesktopActivity.this.sendBroadcast(intent);
					popMenu.dismiss();
				}
			};

			// 按钮监听器
			OnClickListener onViewClick = new OnClickListener() {
				@Override
				public void onClick(View v) {
					popMenu.showAsDropDown(v);
				}
			};

			// 菜单项点击监听器
			popMenu.setOnItemClickListener(popmenuItemClickListener);

			drop_down_menu.setOnClickListener(onViewClick);
		}
	}

	private void hideDropDownMenu() {
		if (drop_down_menu != null) {
			drop_down_menu.setVisibility(View.GONE);
		}
	}

	private void showDropDownMenu() {
		if (drop_down_menu != null) {
			drop_down_menu.setVisibility(View.VISIBLE);
		}
	}

	private void hideSignInBtn() {
		if (sign_in != null) {
			sign_in.setVisibility(View.GONE);
		}
	}

	private void showSignInBtn() {
		if (sign_in != null) {
			sign_in.setVisibility(View.VISIBLE);
		}
	}

	public class PostBaseData extends AsyncTask<String, Void, Integer> {
		@Override
		protected void onPreExecute() {
			progressDig = ViewHandler.creteProgressDialog(DesktopActivity.this,
					"处理中...");
		}

		@Override
		protected Integer doInBackground(String... params) {
			int back_data = 0;
			HttpRestAchieve httpRestAchieve = new HttpRestAchieve();
			for (String s : params) {
				HashMap<String, String> paramsMap = new HashMap<String, String>();
				paramsMap.put("dataType", s);

				try {

					NetBackDataEntity netBackData = httpRestAchieve
							.postRequestData(
									getResources().getString(
											R.string.request_url)
											+ "/dataUpdate/", paramsMap);
					String data = netBackData.getData();
                    Log.d(TAG, "data:");
                    Log.d(TAG, data);
					if (data != null && !data.equals("")) {
						MqttAdhere.ParseData(DesktopActivity.this, params[0],
								data);
						back_data = 0;
					} else {
						back_data = 1;
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
			}
            Log.d(TAG, "更新基础数据，结果back_data为：" + back_data);
			return back_data;
		}

		@Override
		protected void onPostExecute(Integer result) {
			if (progressDig != null) {
				progressDig.cancel();
			}
			if (result == 0) {
				Toast.makeText(DesktopActivity.this, "更新成功！", Toast.LENGTH_LONG)
						.show();
			} else if (result == 1) {
				Toast.makeText(DesktopActivity.this, "无需要更新的数据！",
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(DesktopActivity.this, "更新失败,请稍候重试！",
						Toast.LENGTH_LONG).show();
			}
		}
	}

	public class PostSignInData extends AsyncTask<String, Void, Integer> {
		@Override
		protected void onPreExecute() {
			progressDig = ViewHandler.creteProgressDialog(DesktopActivity.this,
					"处理中...");
		}

		@Override
		protected Integer doInBackground(String... params) {
			int back_data = 0;
			HttpRestAchieve httpRestAchieve = new HttpRestAchieve();

			HashMap<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.put("fieldWorkerNo", USERNAME);

			try {

				NetBackDataEntity netBackData = httpRestAchieve
						.postRequestData(
								getResources().getString(R.string.request_url)
										+ "/fieldworker/checkin", paramsMap);
				String data = netBackData.getData();
				JSONObject jObject = new JSONObject(data);
				int r = jObject.getInt("resultCode");
				if (r == 1) {
					int result = jObject.getInt("result");
					if (result == 2) {
						back_data = 1; // 提前签到
					} else if (r == 3) {
						back_data = 4; // 不必签到
					} else if (r == 5) {
						back_data = 5; // 已经签到
					} else {
						back_data = 0;
					}
				} else {
					back_data = 3; // 服务器错误
				}

			} catch (NotFoundException e) {
				back_data = 2;
				e.printStackTrace();
				Log.d(TAG, e.getMessage());
			} catch (NetAccessException e) {
				back_data = 3;
				e.printStackTrace();
				Log.d(TAG, e.getMessage());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return back_data;
		}

		@Override
		protected void onPostExecute(Integer result) {
			if (progressDig != null) {
				progressDig.cancel();
			}
			if (result == 0) {
				Toast.makeText(DesktopActivity.this, "签到成功！", Toast.LENGTH_LONG)
						.show();
			} else if (result == 1) {
				Toast.makeText(DesktopActivity.this, "请勿提前签到！",
						Toast.LENGTH_LONG).show();
			} else if (result == 4) {
				Toast.makeText(DesktopActivity.this, "不必签到！", Toast.LENGTH_LONG)
						.show();
			} else if (result == 5) {
				Toast.makeText(DesktopActivity.this, "已经签到！", Toast.LENGTH_LONG)
						.show();
			} else {
				Toast.makeText(DesktopActivity.this, "服务器错误！",
						Toast.LENGTH_LONG).show();
			}
		}
	}
}
