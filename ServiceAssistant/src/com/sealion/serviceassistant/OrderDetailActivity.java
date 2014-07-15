package com.sealion.serviceassistant;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.sealion.serviceassistant.db.OrderDetailListDB;
import com.sealion.serviceassistant.db.OrderListDB;
import com.sealion.serviceassistant.entity.NetBackDataEntity;
import com.sealion.serviceassistant.entity.OrderEntity;
import com.sealion.serviceassistant.entity.OrderStepEntity;
import com.sealion.serviceassistant.exception.NetAccessException;
import com.sealion.serviceassistant.net.HttpRestAchieve;
import com.sealion.serviceassistant.superactivity.OrderActivity;
import com.sealion.serviceassistant.tools.DateTools;
import com.sealion.serviceassistant.tools.FinalVariables;

import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 工单详情.
 */
public class OrderDetailActivity extends OrderActivity
{
	private static final String TAG = OrderDetailActivity.class.getSimpleName();

	private WindowManager wm = null;
	private View view = null;
	private TextView top_bar_title = null;
	private int id; // 工单ID
	private int workOrderType = 0;

	private int order_state = 0; // 工单当前的状态

	private TextView order_id_value = null; // 派工单ID
	private TextView order_service_time_value = null; // 服务日期
	private TextView order_send_type_value = null; // 派工类别
	private TextView order_remark_value = null; // 派工说明
	private TextView order_service_arrive_time_value = null; // 服务人员预计到达时间
	private TextView order_is_charge_value = null; // 是否收费
	private TextView order_charge_money_value = null; // 收费金额
	private TextView order_urgency_state_value = null; // 工单紧急程度
	private TextView order_contact_name_value = null; // 客户联系人
	private TextView order_customer_name_value = null; // 客户公司名称
	private TextView order_contact_tax_num_value = null; // 税号
	private TextView order_tax_department_value = null; // 所属税务分局名称
	private TextView order_customer_address_value = null; // 上门地址
	private TextView order_customer_mobile_value = null; // 客户手机
	private TextView order_customer_phone_value = null; // 客户联系电话
	private TextView current_state_value = null; // 工单当前状态
	private TextView order_optimal_path_value = null; //最优路径
	private TextView service_product_value = null; //服务产品
	
	private TableRow service_product_tablerow = null;
	
	private OrderListDB olDB = null;

	private OrderDetailListDB odListDB = null;

	private ImageView back_image = null;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.order_detail);

		top_bar_title = (TextView) this.findViewById(R.id.top_bar_title);
		Intent intent = this.getIntent();
		order_num = intent.getExtras().getString("order_num");
		id = intent.getExtras().getInt("id");

		odListDB = new OrderDetailListDB(this);

		top_bar_title.setText("工单号  " + order_num);

		order_id_value = (TextView) this.findViewById(R.id.order_id_value); // 派工单ID
		order_service_time_value = (TextView) this.findViewById(R.id.order_service_time_value); // 服务日期
		order_send_type_value = (TextView) this.findViewById(R.id.order_send_type_value); // 派工类别
		order_remark_value = (TextView) this.findViewById(R.id.order_remark_value); // 派工说明
		order_service_arrive_time_value = (TextView) this.findViewById(R.id.order_service_arrive_time_value); // 服务人员预计到达时间
		order_is_charge_value = (TextView) this.findViewById(R.id.order_is_charge_value); // 是否收费
		order_charge_money_value = (TextView) this.findViewById(R.id.order_charge_money_value); // 收费金额
		order_urgency_state_value = (TextView) this.findViewById(R.id.order_urgency_state_value); // 工单紧急程度
		order_contact_name_value = (TextView) this.findViewById(R.id.order_contact_name_value); // 客户联系人
		order_customer_name_value = (TextView) this.findViewById(R.id.order_customer_name_value); // 客户公司名称
		order_contact_tax_num_value = (TextView) this.findViewById(R.id.order_contact_tax_num_value); // 税号
		order_tax_department_value = (TextView) this.findViewById(R.id.order_tax_department_value); // 所属税务分局名称
		order_customer_address_value = (TextView) this.findViewById(R.id.order_customer_address_value); // 上门地址
		order_customer_mobile_value = (TextView) this.findViewById(R.id.order_customer_mobile_value); // 客户手机
		order_customer_phone_value = (TextView) this.findViewById(R.id.order_customer_phone_value); // 客户联系电话
		current_state_value = (TextView) this.findViewById(R.id.current_state_value);
		order_optimal_path_value = (TextView)this.findViewById(R.id.order_optimal_path_value);
		service_product_value = (TextView)this.findViewById(R.id.service_product_value);
		
		back_image = (ImageView) this.findViewById(R.id.back_image);
		back_image.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
		olDB = new OrderListDB(this);

		if (olDB != null)
		{
			OrderEntity oEntity = olDB.SelectById(id);
			current_state_value.setText(CurrentOrderState(oEntity.getOrder_sign()));
			order_id_value.setText(oEntity.getWorkCardId() + "");
			order_service_time_value.setText(oEntity.getServiceDate()); // 服务日期
			workOrderType = oEntity.getWorkOrderType();
			if (workOrderType == 1)
			{
				order_send_type_value.setText("服务派工"); // 派工类别
			}
			else// if (workOrderType == 2)
			{
				order_send_type_value.setText("回访派工");
				service_product_tablerow = (TableRow)this.findViewById(R.id.service_product_tablerow);
				service_product_tablerow.setVisibility(View.VISIBLE);
				service_product_value.setText(oEntity.getProductName());
			}
			order_remark_value.setText(FliterString(oEntity.getWorkOrderDescription())); // 派工说明
			order_service_arrive_time_value.setText(FliterString(oEntity.getExpectArriveTime())); // 服务人员预计到达时间
			if (oEntity.getChargeType() == 1)
			{
				order_is_charge_value.setText("是"); // 是否收费
			}
			else
			{
				order_is_charge_value.setText("否"); // 是否收费
			}

			order_charge_money_value.setText(oEntity.getChargeMoney()); // 收费金额
			if (oEntity.getUrgency() == OrderListDB.NOT_URGENCY)
			{
				order_urgency_state_value.setText("非紧急"); // 工单紧急程度
			}
			else if (oEntity.getUrgency() == OrderListDB.URGENCY)
			{
				order_urgency_state_value.setText("紧急"); // 工单紧急程度
			}
			

			order_contact_name_value.setText(FliterString(oEntity.getLinkPerson())); // 客户联系人
			order_customer_name_value.setText(FliterString(oEntity.getCustomerName())); // 客户公司名称
			order_contact_tax_num_value.setText(FliterString(oEntity.getTaxCode())); // 税号
			order_tax_department_value.setText(FliterString(oEntity.getRevenueName())); // 所属税务分局名称
			order_customer_address_value.setText(FliterString(oEntity.getCustomerAddr())); // 上门地址
			order_customer_mobile_value.setText(FliterString(oEntity.getCustomerMobile()));			
			order_customer_mobile_value_text = oEntity.getCustomerMobile();
			order_customer_phone_value.setText(FliterString(oEntity.getCustomerTel())); // 客户联系电话
			order_customer_phone_value_text = oEntity.getCustomerTel();
			order_optimal_path_value.setText(FliterString(oEntity.getOptimal_path()));//最优路径
			order_state = oEntity.getOrder_sign();
		}
	}

	@Override
	public void onStart()
	{
		Log.d(TAG, "order detail on start...");
		super.onStart();
	}

	@Override
	public void onResume()
	{
		super.onResume();
		createFloatWin();
		Log.d(TAG, "order detail on resume...");
	}

	@Override
	public void onPause()
	{
		super.onPause();
		
		Log.d(TAG, "order detail on pause...");
	}

	@Override
	public void onStop()
	{
		Log.d(TAG, "order detail on stop...");
		super.onStop();
		destoryFloatWin();
	}

	@Override
	public void onDestroy()
	{
		Log.d(TAG, "order detail on destroy...");		
		super.onDestroy();
		CancelDialog();
	}
	// 查看问题详情
	public void CheckQuestionBtnClick(View target)
	{
		Intent intent = new Intent(OrderDetailActivity.this, CheckQuestionActivity.class);
		intent.putExtra("order_num", order_id_value.getText().toString());
		this.startActivity(intent);
	}

	// 接受工单
	public void AcceptOrderBtnClick(View target)
	{
		new PostAcceptData().execute(sp.getString("USERNAME", ""), order_num, DateTools.getFormatDateAndTime());
	}

	// 申请改派
	public void ChangeOrderBtnClick(View target)
	{
		// 更新工单状态为申请改派
		olDB.updateSignData(order_num, FinalVariables.ORDER_STATE_APPLY_CHANGE);

		// 写入工单当前状态到本地数据库
		OrderStepEntity osEntity = new OrderStepEntity();
		osEntity.setOper_time(DateTools.getFormatDateAndTime());
		osEntity.setOrder_num(order_num);
		osEntity.setOrder_state(FinalVariables.ORDER_STATE_ACCEPT_TASK); // 改派工单
		odListDB.insert(osEntity);

		Intent intent = new Intent(this, ChangeOrderFormActivity.class);
		intent.putExtra("order_num", order_num);
		intent.putExtra("order_customer_mobile_value_text", order_customer_mobile_value_text);
		intent.putExtra("order_customer_phone_value_text", order_customer_phone_value_text);
		intent.putExtra("customer_address", customer_address);
		this.startActivityForResult(intent,0);
	}

	// 到达现场
	public void ArriveTargetBtnClick(View target)
	{
		new PostArriveData().execute(sp.getString("USERNAME", ""), order_num, DateTools.getFormatDateAndTime());
		this.finish();
	}

	// 电话处理
	public void CallDisposeBtnClick(View target)
	{
		// 更新工单状态为电话解决
		olDB.updateSignData(order_num, FinalVariables.ORDER_STATE_PHONE_SOLVE);

		// 写入工单当前状态到本地数据库
		
		OrderStepEntity osEntity = new OrderStepEntity();
		osEntity.setOper_time(DateTools.getFormatDateAndTime());
		osEntity.setOrder_num(order_num);
		osEntity.setOrder_state(FinalVariables.ORDER_STATE_PHONE_SOLVE); // 电话解决
		odListDB.insert(osEntity);

		Intent intent = new Intent(OrderDetailActivity.this, CompleteOrderPanelActivity.class);
		
		intent.putExtra("order_num", order_num);
		intent.putExtra("workOrderType", workOrderType);
		intent.putExtra("id", id);
		intent.putExtra("order_customer_mobile_value_text", order_customer_mobile_value_text);
		intent.putExtra("order_customer_phone_value_text", order_customer_phone_value_text);
		intent.putExtra("customer_address", customer_address);
		intent.putExtra("phoneDispose", 1);
		this.startActivityForResult(intent,1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == 0)  //申请改派
		{
			if (resultCode == RESULT_OK) 
			{
				finish();
			}
		}
		else if (requestCode == 1) //电话解决
		{
			if (resultCode == RESULT_OK) 
			{
				finish();
			}
		}
	}

	private void destoryFloatWin()
	{
		if (wm != null && view != null)
		{
			Log.d(TAG, "remove win.....");
			wm.removeView(view);
			wm = null;
			view = null;
		}
	}

	private void createFloatWin()
	{
		if (wm == null && view == null)
		{
			wm = (WindowManager) getApplicationContext().getSystemService("window");
			WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();

			/**
			 * 以下都是WindowManager.LayoutParams的相关属性 具体用途请参考SDK文档
			 */
			wmParams.type = 2002; // 这里是关键，你也可以试试2003
			wmParams.format = 1;
			wmParams.gravity = Gravity.RIGHT | Gravity.BOTTOM;
			/**
			 * 这里的flags也很关键 代码实际是wmParams.flags |= FLAG_NOT_FOCUSABLE;
			 * 40的由来是wmParams的默认属性（32）+ FLAG_NOT_FOCUSABLE（8）
			 */
			wmParams.flags = 40;
			wmParams.width = LayoutParams.WRAP_CONTENT;
			wmParams.height = LayoutParams.WRAP_CONTENT;
			wmParams.y = 150;
			wmParams.x = Gravity.RIGHT;

			if (workOrderType == 1) // 加载派工单菜单
			{
				if (order_state == 0)
				{
					view = View.inflate(OrderDetailActivity.this, R.layout.pop_accept_type_btn, null);
				}
				else if (order_state == 1)
				{
					view = View.inflate(OrderDetailActivity.this, R.layout.pop_accept_type_btn, null);
				}
				else if (order_state == 2)
				{
					view = View.inflate(OrderDetailActivity.this, R.layout.pop_dispose_type_btn, null);
				}
			}
			else// if (workOrderType == 2)// 加载回访工单菜单
			{
				if (order_state == 0)
				{
					view = View.inflate(OrderDetailActivity.this, R.layout.pop_visit_type_btn, null);
				}
				else if (order_state == 1)
				{
					view = View.inflate(OrderDetailActivity.this, R.layout.pop_visit_type_btn, null);
				}
				else if (order_state == 2)
				{
					view = View.inflate(OrderDetailActivity.this, R.layout.pop_visit_arrive_btn, null);
				}
			}
			if (view != null)
			{
				wm.addView(view, wmParams); // 创建View
			}
		}
	}

	private String CurrentOrderState(int state)
	{
		// 0：未阅读，1:已经阅读，2:接受任务，
		// 3：申请改派，4：改派审批通过，5：改派审批未通过，
		// 6：到达现场，7:电话解决， 10：工单完成

		if (state == FinalVariables.ORDER_STATE_NOT_READ || state == FinalVariables.ORDER_STATE_IS_READ)
		{
			return this.getResources().getString(R.string.order_state_new);
		}
		else if (state == FinalVariables.ORDER_STATE_ACCEPT_TASK)
		{
			return this.getResources().getString(R.string.order_state_accept);
		}
		else if (state == FinalVariables.ORDER_STATE_APPLY_CHANGE)
		{
			return this.getResources().getString(R.string.order_state_change);
		}
		// else if (state == 4)
		// {
		// return
		// this.getResources().getString(R.string.order_state_change_accept);
		// }
		// else if (state == 5)
		// {
		// return
		// this.getResources().getString(R.string.order_state_change_refuse);
		// }
		else if (state == FinalVariables.ORDER_STATE_PHONE_SOLVE)
		{
			return this.getResources().getString(R.string.order_state_order_state);
		}
		else if (state == FinalVariables.ORDER_STATE_ORDER_COMPLISH)
		{
			return this.getResources().getString(R.string.order_complish);
		}
		else
		{
			return "";
		}
	}

	// 接受工单操作
	private class PostAcceptData extends AsyncTask<String, Void, Integer>
	{
		@Override
		protected void onPreExecute()
		{
			ShowDialog();
		}

		@Override
		protected Integer doInBackground(String... params)
		{
			int back_data = 0;
			HttpRestAchieve httpRestAchieve = new HttpRestAchieve();
			HashMap<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.put("workerNo", params[0]);
			paramsMap.put("workOrderNo", params[1]);
			paramsMap.put("acceptTime", params[2]);
			try
			{

				NetBackDataEntity netBackData = httpRestAchieve.postRequestData(request_url + "/fieldworker/biz/accept", paramsMap);
				String data = netBackData.getData();
				JSONObject jsonObject = new JSONObject(data);
				int result = jsonObject.getInt("resultCode");
				if (result == 1)
				{
					back_data = 0;
				}
				else
				{
					back_data = 1;

				}
			}
			catch (NotFoundException e)
			{
				back_data = 2;
				e.printStackTrace();
				Log.d(TAG, e.getMessage());
			}
			catch (NetAccessException e)
			{
				back_data = 3;
				e.printStackTrace();
				Log.d(TAG, e.getMessage());
			}
			catch (JSONException e)
			{
				back_data = 4;
				e.printStackTrace();
				Log.d(TAG, e.getMessage());
			}
			return back_data;
		}

		@Override
		protected void onPostExecute(Integer result)
		{
			if (result == 0)
			{
				destoryFloatWin();
				order_state = 2;
				createFloatWin();
				// 更新工单状态为已接受
				olDB.updateSignData(order_num, FinalVariables.ORDER_STATE_ACCEPT_TASK);

				// 写入工单当前状态到本地数据库
				OrderStepEntity osEntity = new OrderStepEntity();
				osEntity.setOper_time(DateTools.getFormatDateAndTime());
				osEntity.setOrder_num(order_num);
				osEntity.setOrder_state(FinalVariables.ORDER_STATE_ACCEPT_TASK); // 新工单，未阅读状态
				odListDB.insert(osEntity);

				// doInBackground返回时触发，换句话说，就是doInBackground执行完后触发
				// 这里的result就是上面doInBackground执行后的返回值，所以这里是"执行完毕"
				CancelDialog();
			}
			else if (result == 1)
			{
				Toast.makeText(OrderDetailActivity.this, "操作失败，请重试！", Toast.LENGTH_LONG).show();
			}
			else if (result == 2)
			{
				Toast.makeText(OrderDetailActivity.this, "服务器错误！", Toast.LENGTH_LONG).show();
			}
			else if (result == 3)
			{
				Toast.makeText(OrderDetailActivity.this, "操作失败,网络异常！", Toast.LENGTH_LONG).show();
			}
			else if (result == 4)
			{
				Toast.makeText(OrderDetailActivity.this, "操作失败，数据解析异常！", Toast.LENGTH_LONG).show();
			}
		}
	}

	// 到达现场
	private class PostArriveData extends AsyncTask<String, Void, Integer>
	{
		@Override
		protected void onPreExecute()
		{
			ShowDialog();
		}

		@Override
		protected Integer doInBackground(String... params)
		{
			int back_data = 0;
			HttpRestAchieve httpRestAchieve = new HttpRestAchieve();
			HashMap<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.put("workerNo", params[0]);
			paramsMap.put("workOrderNo", params[1]);
			paramsMap.put("arriveTime", params[2]);
			try
			{

				NetBackDataEntity netBackData = httpRestAchieve.postRequestData(request_url + "/fieldworker/biz/arrive", paramsMap);
				String data = netBackData.getData();
				JSONObject jsonObject = new JSONObject(data);
				int result = jsonObject.getInt("resultCode");
				if (result == 1)
				{
					back_data = 0;
				}
				else
				{
					back_data = 1;

				}
			}
			catch (NotFoundException e)
			{
				back_data = 2;
				e.printStackTrace();
				Log.d(TAG, e.getMessage());
			}
			catch (NetAccessException e)
			{
				back_data = 3;
				e.printStackTrace();
				Log.d(TAG, e.getMessage());
			}
			catch (JSONException e)
			{
				back_data = 4;
				e.printStackTrace();
				Log.d(TAG, e.getMessage());
			}
			return back_data;
		}

		@Override
		protected void onPostExecute(Integer result)
		{
			

			// doInBackground返回时触发，换句话说，就是doInBackground执行完后触发
			// 这里的result就是上面doInBackground执行后的返回值，所以这里是"执行完毕"
			

			if (result == 0)
			{
				destoryFloatWin();
				CancelDialog();
				// 更新工单状态为到达现场
				olDB.updateSignData(order_num, FinalVariables.ORDER_STATE_ARRIVE_TARGET);

				// 写入工单当前状态到本地数据库
				OrderStepEntity osEntity = new OrderStepEntity();
				osEntity.setOper_time(DateTools.getFormatDateAndTime());
				osEntity.setOrder_num(order_num);
				osEntity.setOrder_state(FinalVariables.ORDER_STATE_ARRIVE_TARGET); // 到达现场
				odListDB.insert(osEntity);
				Intent intent = new Intent(OrderDetailActivity.this, CompleteOrderPanelActivity.class);

				intent.putExtra("order_num", order_num);
				intent.putExtra("workOrderType", workOrderType);
				intent.putExtra("id", id);
				intent.putExtra("order_customer_mobile_value_text", order_customer_mobile_value_text);
				intent.putExtra("order_customer_phone_value_text", order_customer_phone_value_text);
				intent.putExtra("customer_address", customer_address);
				intent.putExtra("phoneDispose", 0);
				startActivity(intent);
			}
			else if (result == 1)
			{
				Toast.makeText(OrderDetailActivity.this, "操作失败，请重试！", Toast.LENGTH_LONG).show();
			}
			else if (result == 2)
			{
				Toast.makeText(OrderDetailActivity.this, "服务器错误！", Toast.LENGTH_LONG).show();
			}
			else if (result == 3)
			{
				Toast.makeText(OrderDetailActivity.this, "操作失败,网络异常！", Toast.LENGTH_LONG).show();
			}
			else if (result == 4)
			{
				Toast.makeText(OrderDetailActivity.this, "操作失败，数据解析异常！", Toast.LENGTH_LONG).show();
			}
		}
	}
	
	public String FliterString(String str)
	{
		if (str == null || str.trim().equals("") || str.trim().equals("null"))
		{
			return "";
		}
		else
		{
			return str;
		}
	}
}
