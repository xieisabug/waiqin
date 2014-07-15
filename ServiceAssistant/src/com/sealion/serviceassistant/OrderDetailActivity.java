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
 * ��������.
 */
public class OrderDetailActivity extends OrderActivity
{
	private static final String TAG = OrderDetailActivity.class.getSimpleName();

	private WindowManager wm = null;
	private View view = null;
	private TextView top_bar_title = null;
	private int id; // ����ID
	private int workOrderType = 0;

	private int order_state = 0; // ������ǰ��״̬

	private TextView order_id_value = null; // �ɹ���ID
	private TextView order_service_time_value = null; // ��������
	private TextView order_send_type_value = null; // �ɹ����
	private TextView order_remark_value = null; // �ɹ�˵��
	private TextView order_service_arrive_time_value = null; // ������ԱԤ�Ƶ���ʱ��
	private TextView order_is_charge_value = null; // �Ƿ��շ�
	private TextView order_charge_money_value = null; // �շѽ��
	private TextView order_urgency_state_value = null; // ���������̶�
	private TextView order_contact_name_value = null; // �ͻ���ϵ��
	private TextView order_customer_name_value = null; // �ͻ���˾����
	private TextView order_contact_tax_num_value = null; // ˰��
	private TextView order_tax_department_value = null; // ����˰��־�����
	private TextView order_customer_address_value = null; // ���ŵ�ַ
	private TextView order_customer_mobile_value = null; // �ͻ��ֻ�
	private TextView order_customer_phone_value = null; // �ͻ���ϵ�绰
	private TextView current_state_value = null; // ������ǰ״̬
	private TextView order_optimal_path_value = null; //����·��
	private TextView service_product_value = null; //�����Ʒ
	
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

		top_bar_title.setText("������  " + order_num);

		order_id_value = (TextView) this.findViewById(R.id.order_id_value); // �ɹ���ID
		order_service_time_value = (TextView) this.findViewById(R.id.order_service_time_value); // ��������
		order_send_type_value = (TextView) this.findViewById(R.id.order_send_type_value); // �ɹ����
		order_remark_value = (TextView) this.findViewById(R.id.order_remark_value); // �ɹ�˵��
		order_service_arrive_time_value = (TextView) this.findViewById(R.id.order_service_arrive_time_value); // ������ԱԤ�Ƶ���ʱ��
		order_is_charge_value = (TextView) this.findViewById(R.id.order_is_charge_value); // �Ƿ��շ�
		order_charge_money_value = (TextView) this.findViewById(R.id.order_charge_money_value); // �շѽ��
		order_urgency_state_value = (TextView) this.findViewById(R.id.order_urgency_state_value); // ���������̶�
		order_contact_name_value = (TextView) this.findViewById(R.id.order_contact_name_value); // �ͻ���ϵ��
		order_customer_name_value = (TextView) this.findViewById(R.id.order_customer_name_value); // �ͻ���˾����
		order_contact_tax_num_value = (TextView) this.findViewById(R.id.order_contact_tax_num_value); // ˰��
		order_tax_department_value = (TextView) this.findViewById(R.id.order_tax_department_value); // ����˰��־�����
		order_customer_address_value = (TextView) this.findViewById(R.id.order_customer_address_value); // ���ŵ�ַ
		order_customer_mobile_value = (TextView) this.findViewById(R.id.order_customer_mobile_value); // �ͻ��ֻ�
		order_customer_phone_value = (TextView) this.findViewById(R.id.order_customer_phone_value); // �ͻ���ϵ�绰
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
			order_service_time_value.setText(oEntity.getServiceDate()); // ��������
			workOrderType = oEntity.getWorkOrderType();
			if (workOrderType == 1)
			{
				order_send_type_value.setText("�����ɹ�"); // �ɹ����
			}
			else// if (workOrderType == 2)
			{
				order_send_type_value.setText("�ط��ɹ�");
				service_product_tablerow = (TableRow)this.findViewById(R.id.service_product_tablerow);
				service_product_tablerow.setVisibility(View.VISIBLE);
				service_product_value.setText(oEntity.getProductName());
			}
			order_remark_value.setText(FliterString(oEntity.getWorkOrderDescription())); // �ɹ�˵��
			order_service_arrive_time_value.setText(FliterString(oEntity.getExpectArriveTime())); // ������ԱԤ�Ƶ���ʱ��
			if (oEntity.getChargeType() == 1)
			{
				order_is_charge_value.setText("��"); // �Ƿ��շ�
			}
			else
			{
				order_is_charge_value.setText("��"); // �Ƿ��շ�
			}

			order_charge_money_value.setText(oEntity.getChargeMoney()); // �շѽ��
			if (oEntity.getUrgency() == OrderListDB.NOT_URGENCY)
			{
				order_urgency_state_value.setText("�ǽ���"); // ���������̶�
			}
			else if (oEntity.getUrgency() == OrderListDB.URGENCY)
			{
				order_urgency_state_value.setText("����"); // ���������̶�
			}
			

			order_contact_name_value.setText(FliterString(oEntity.getLinkPerson())); // �ͻ���ϵ��
			order_customer_name_value.setText(FliterString(oEntity.getCustomerName())); // �ͻ���˾����
			order_contact_tax_num_value.setText(FliterString(oEntity.getTaxCode())); // ˰��
			order_tax_department_value.setText(FliterString(oEntity.getRevenueName())); // ����˰��־�����
			order_customer_address_value.setText(FliterString(oEntity.getCustomerAddr())); // ���ŵ�ַ
			order_customer_mobile_value.setText(FliterString(oEntity.getCustomerMobile()));			
			order_customer_mobile_value_text = oEntity.getCustomerMobile();
			order_customer_phone_value.setText(FliterString(oEntity.getCustomerTel())); // �ͻ���ϵ�绰
			order_customer_phone_value_text = oEntity.getCustomerTel();
			order_optimal_path_value.setText(FliterString(oEntity.getOptimal_path()));//����·��
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
	// �鿴��������
	public void CheckQuestionBtnClick(View target)
	{
		Intent intent = new Intent(OrderDetailActivity.this, CheckQuestionActivity.class);
		intent.putExtra("order_num", order_id_value.getText().toString());
		this.startActivity(intent);
	}

	// ���ܹ���
	public void AcceptOrderBtnClick(View target)
	{
		new PostAcceptData().execute(sp.getString("USERNAME", ""), order_num, DateTools.getFormatDateAndTime());
	}

	// �������
	public void ChangeOrderBtnClick(View target)
	{
		// ���¹���״̬Ϊ�������
		olDB.updateSignData(order_num, FinalVariables.ORDER_STATE_APPLY_CHANGE);

		// д�빤����ǰ״̬���������ݿ�
		OrderStepEntity osEntity = new OrderStepEntity();
		osEntity.setOper_time(DateTools.getFormatDateAndTime());
		osEntity.setOrder_num(order_num);
		osEntity.setOrder_state(FinalVariables.ORDER_STATE_ACCEPT_TASK); // ���ɹ���
		odListDB.insert(osEntity);

		Intent intent = new Intent(this, ChangeOrderFormActivity.class);
		intent.putExtra("order_num", order_num);
		intent.putExtra("order_customer_mobile_value_text", order_customer_mobile_value_text);
		intent.putExtra("order_customer_phone_value_text", order_customer_phone_value_text);
		intent.putExtra("customer_address", customer_address);
		this.startActivityForResult(intent,0);
	}

	// �����ֳ�
	public void ArriveTargetBtnClick(View target)
	{
		new PostArriveData().execute(sp.getString("USERNAME", ""), order_num, DateTools.getFormatDateAndTime());
		this.finish();
	}

	// �绰����
	public void CallDisposeBtnClick(View target)
	{
		// ���¹���״̬Ϊ�绰���
		olDB.updateSignData(order_num, FinalVariables.ORDER_STATE_PHONE_SOLVE);

		// д�빤����ǰ״̬���������ݿ�
		
		OrderStepEntity osEntity = new OrderStepEntity();
		osEntity.setOper_time(DateTools.getFormatDateAndTime());
		osEntity.setOrder_num(order_num);
		osEntity.setOrder_state(FinalVariables.ORDER_STATE_PHONE_SOLVE); // �绰���
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
		if (requestCode == 0)  //�������
		{
			if (resultCode == RESULT_OK) 
			{
				finish();
			}
		}
		else if (requestCode == 1) //�绰���
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
			 * ���¶���WindowManager.LayoutParams��������� ������;��ο�SDK�ĵ�
			 */
			wmParams.type = 2002; // �����ǹؼ�����Ҳ��������2003
			wmParams.format = 1;
			wmParams.gravity = Gravity.RIGHT | Gravity.BOTTOM;
			/**
			 * �����flagsҲ�ܹؼ� ����ʵ����wmParams.flags |= FLAG_NOT_FOCUSABLE;
			 * 40��������wmParams��Ĭ�����ԣ�32��+ FLAG_NOT_FOCUSABLE��8��
			 */
			wmParams.flags = 40;
			wmParams.width = LayoutParams.WRAP_CONTENT;
			wmParams.height = LayoutParams.WRAP_CONTENT;
			wmParams.y = 150;
			wmParams.x = Gravity.RIGHT;

			if (workOrderType == 1) // �����ɹ����˵�
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
			else// if (workOrderType == 2)// ���ػطù����˵�
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
				wm.addView(view, wmParams); // ����View
			}
		}
	}

	private String CurrentOrderState(int state)
	{
		// 0��δ�Ķ���1:�Ѿ��Ķ���2:��������
		// 3��������ɣ�4����������ͨ����5����������δͨ����
		// 6�������ֳ���7:�绰����� 10���������

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

	// ���ܹ�������
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
				// ���¹���״̬Ϊ�ѽ���
				olDB.updateSignData(order_num, FinalVariables.ORDER_STATE_ACCEPT_TASK);

				// д�빤����ǰ״̬���������ݿ�
				OrderStepEntity osEntity = new OrderStepEntity();
				osEntity.setOper_time(DateTools.getFormatDateAndTime());
				osEntity.setOrder_num(order_num);
				osEntity.setOrder_state(FinalVariables.ORDER_STATE_ACCEPT_TASK); // �¹�����δ�Ķ�״̬
				odListDB.insert(osEntity);

				// doInBackground����ʱ���������仰˵������doInBackgroundִ����󴥷�
				// �����result��������doInBackgroundִ�к�ķ���ֵ������������"ִ�����"
				CancelDialog();
			}
			else if (result == 1)
			{
				Toast.makeText(OrderDetailActivity.this, "����ʧ�ܣ������ԣ�", Toast.LENGTH_LONG).show();
			}
			else if (result == 2)
			{
				Toast.makeText(OrderDetailActivity.this, "����������", Toast.LENGTH_LONG).show();
			}
			else if (result == 3)
			{
				Toast.makeText(OrderDetailActivity.this, "����ʧ��,�����쳣��", Toast.LENGTH_LONG).show();
			}
			else if (result == 4)
			{
				Toast.makeText(OrderDetailActivity.this, "����ʧ�ܣ����ݽ����쳣��", Toast.LENGTH_LONG).show();
			}
		}
	}

	// �����ֳ�
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
			

			// doInBackground����ʱ���������仰˵������doInBackgroundִ����󴥷�
			// �����result��������doInBackgroundִ�к�ķ���ֵ������������"ִ�����"
			

			if (result == 0)
			{
				destoryFloatWin();
				CancelDialog();
				// ���¹���״̬Ϊ�����ֳ�
				olDB.updateSignData(order_num, FinalVariables.ORDER_STATE_ARRIVE_TARGET);

				// д�빤����ǰ״̬���������ݿ�
				OrderStepEntity osEntity = new OrderStepEntity();
				osEntity.setOper_time(DateTools.getFormatDateAndTime());
				osEntity.setOrder_num(order_num);
				osEntity.setOrder_state(FinalVariables.ORDER_STATE_ARRIVE_TARGET); // �����ֳ�
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
				Toast.makeText(OrderDetailActivity.this, "����ʧ�ܣ������ԣ�", Toast.LENGTH_LONG).show();
			}
			else if (result == 2)
			{
				Toast.makeText(OrderDetailActivity.this, "����������", Toast.LENGTH_LONG).show();
			}
			else if (result == 3)
			{
				Toast.makeText(OrderDetailActivity.this, "����ʧ��,�����쳣��", Toast.LENGTH_LONG).show();
			}
			else if (result == 4)
			{
				Toast.makeText(OrderDetailActivity.this, "����ʧ�ܣ����ݽ����쳣��", Toast.LENGTH_LONG).show();
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
