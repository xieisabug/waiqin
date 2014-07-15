package com.sealion.serviceassistant;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.sealion.serviceassistant.adapter.OrderListAdapter;
import com.sealion.serviceassistant.entity.NetBackDataEntity;
import com.sealion.serviceassistant.entity.OrderEntity;
import com.sealion.serviceassistant.entity.OrderStepEntity;
import com.sealion.serviceassistant.exception.NetAccessException;
import com.sealion.serviceassistant.net.HttpRestAchieve;
import com.sealion.serviceassistant.superactivity.OrderActivity;
import com.sealion.serviceassistant.tools.DateTools;
import com.sealion.serviceassistant.tools.FinalVariables;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources.NotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

/**
 * �����б����
 */
public class OrderListActivity extends OrderActivity
{
	private static final String TAG = OrderListActivity.class.getSimpleName();

	private ListView orderlist = null;
	private ConditionSearchCast cCast = null;
	private OrderListAdapter oListAdapter = null;
	private ArrayList<OrderEntity> orderList = null;
	private EditText search_value = null;

	private int type_to_order = 0;

	private int page = 0;

	private static final int PAGE_SIZE = 10;

	private Intent intent = null;

	private OrderEntity oEntity = null;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		Log.d(TAG, "orderlist activity on create");
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.orderlist);

		search_value = (EditText) this.findViewById(R.id.search_value);

		orderlist = (ListView) this.findViewById(R.id.orderlist);		
	}

	@Override
	public void onStart()
	{
		Log.d(TAG, "orderlist activity on start");
		super.onStart();
	}

	@Override
	public void onResume()
	{
		Log.d(TAG, "orderlist activity on resume");
		super.onResume();
		page = 0;
		search_value.setText("");
		
		orderlist.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3)
			{
				oEntity = (OrderEntity) orderlist.getItemAtPosition(arg2);
				oEntity = olDB.SelectById(oEntity.getId());
				if (oEntity.getOrder_sign() != FinalVariables.ORDER_STATE_ORDER_COMPLISH) // ������ǰ����δ���״̬
				{
					if (oEntity.getOrder_sign() == FinalVariables.ORDER_STATE_NOT_READ) // ���Ϊ�¹���
					{
						new PostReadData().execute(sp.getString("USERNAME", ""), oEntity.getWorkCardId() + "", DateTools.getFormatDateAndTime());
					}
					else
					{
						if (oEntity.getOrder_sign() == FinalVariables.ORDER_STATE_ARRIVE_TARGET) // ��ǰ�������ڵ����ֳ�״̬
						{
							intent = new Intent(OrderListActivity.this, CompleteOrderPanelActivity.class);
						}
						else if (oEntity.getOrder_sign() == FinalVariables.ORDER_STATE_APPLY_CHANGE) // ��ǰ���������������״̬
						{
							intent = new Intent(OrderListActivity.this, ChangeOrderFormActivity.class);
						}
						else if (oEntity.getOrder_sign() == FinalVariables.ORDER_STATE_PHONE_SOLVE) // ��ǰ���ڵ绰���״̬
						{
							intent = new Intent(OrderListActivity.this, CompleteOrderPanelActivity.class);
							intent.putExtra("phoneDispose", 1);
						}
						else
						{
							intent = new Intent(OrderListActivity.this, OrderDetailActivity.class);
						}
						
						if (intent != null)
						{
							intent.putExtra("order_num", oEntity.getWorkCardId() + "");
							intent.putExtra("workOrderType", oEntity.getWorkOrderType());
							intent.putExtra("id", oEntity.getId());
							startActivity(intent);
						}
					}
				}
				else
				// ������ǰ�������״̬,����ݹ���������������ʾ������ϸ��Ϣ
				{
					intent = new Intent(OrderListActivity.this, OrderDetailViewActivity.class);
					intent.putExtra("order_num", oEntity.getWorkCardId() + "");
					intent.putExtra("id", oEntity.getId());
					startActivity(intent);
				}
			}
		});
		
		cCast = new ConditionSearchCast(this);
		cCast.registerAction("SEARCH_SEARCH_BROADCAST");
	}

	@Override
	public void onPause()
	{
		Log.d(TAG, "orderlist activity on pause");
		super.onPause();
		unregisterReceiver(cCast);
	}

	@Override
	public void onStop()
	{
		Log.d(TAG, "orderlist activity on stop");
		super.onStop();
	}

	@Override
	public void onDestroy()
	{
		Log.d(TAG, "orderlist activity on destroy");
		super.onDestroy();
	}

	public class ConditionSearchCast extends BroadcastReceiver
	{
		Context context;

		public ConditionSearchCast(Context c)
		{
			context = c;
		}

		// ��̬ע��
		public void registerAction(String action)
		{
			IntentFilter filter = new IntentFilter();
			filter.addAction(action);
			context.registerReceiver(this, filter);
		}

		@Override
		public void onReceive(Context arg0, Intent intent)
		{
			Log.d(TAG, "receive broadcast from list...");
			orderlist.setOnScrollListener(listener);
			page = 0;
			type_to_order = intent.getExtras().getInt("position");
			orderList = olDB.getDataByPage(page, type_to_order);
			page++;
			if (orderList.size() > 0)
			{
				if (orderList.size() < PAGE_SIZE)
				{
					orderlist.setOnScrollListener(null);
				}				
			}
			else
			{
				orderlist.setOnScrollListener(null);
				Toast.makeText(OrderListActivity.this, "û�з�������������!", Toast.LENGTH_SHORT).show();
			}
			
			oListAdapter = new OrderListAdapter(OrderListActivity.this, orderList);
			oListAdapter.notifyDataSetChanged();
			orderlist.setAdapter(oListAdapter);
		}
	}

    /**
     * ����������������������һ�������Զ���ѯ���ظ��������
     */
	private AbsListView.OnScrollListener listener = new AbsListView.OnScrollListener()
	{

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState)
		{
			if (view.getLastVisiblePosition() == view.getCount() - 1)
			{
				loadMoreData();
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
		{

		}
	};

	public void loadMoreData()
	{
		
		new Handler().postDelayed(new Runnable(){    
		    public void run() {
		    	ArrayList<OrderEntity> loadorderList = olDB.getDataByPage(page, type_to_order);
		    	page++;
		    	
				orderList.addAll(loadorderList);
								
				oListAdapter = new OrderListAdapter(OrderListActivity.this, orderList);
				oListAdapter.notifyDataSetChanged();
				orderlist.setAdapter(oListAdapter);
				if (loadorderList.size() < PAGE_SIZE)
				{
					//removeListViewFooter();
					orderlist.setOnScrollListener(null);
					Toast.makeText(OrderListActivity.this, "����ȫ��������!", Toast.LENGTH_SHORT).show();
				}    
		    }    
		 }, 1000);
	}

	public void SearchBtnClick(View target)
	{
		EditText search_value = (EditText) this.findViewById(R.id.search_value);
		String condition = search_value.getText().toString();
		if (condition == null || condition.equals(""))
		{
			Toast.makeText(this, "��������������", Toast.LENGTH_LONG).show();
		}
		else
		{
			orderList = olDB.getDataByCondition(condition);

			if (orderList.size() > 0)
			{				
				oListAdapter = new OrderListAdapter(OrderListActivity.this, orderList);
				orderlist.setAdapter(oListAdapter);
				oListAdapter.notifyDataSetChanged();
			}
			else
			{
				Toast.makeText(this, "û�з�������������", Toast.LENGTH_LONG).show();
			}
			search_value.setText("");
		}
	}

	//�����Ѿ��Ķ�
	private class PostReadData extends AsyncTask<String, Void, Integer>
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
			if (olDB.selectOrderSignById(oEntity.getWorkCardId() + "") == FinalVariables.ORDER_STATE_IS_READ)
			{
				back_data = -1;
			}
			else
			{				
				HttpRestAchieve httpRestAchieve = new HttpRestAchieve();
				HashMap<String, String> paramsMap = new HashMap<String, String>();
				paramsMap.put("workerNo", params[0]);
				paramsMap.put("workOrderNo", params[1]);
				paramsMap.put("viewTime", params[2]);
				try
				{

					NetBackDataEntity netBackData = httpRestAchieve.postRequestData(request_url + "/fieldworker/biz/view", paramsMap);
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
			}
			
			return back_data;
		}

		@Override
		protected void onPostExecute(Integer result)
		{
			CancelDialog();
			if (result != -1)
			{
				if (oEntity != null)
				{
					// ���¹���״̬Ϊ�Ѿ��Ķ�
					olDB.updateSignData(oEntity.getWorkCardId() + "", FinalVariables.ORDER_STATE_IS_READ);

					// д�빤����ǰ����״̬���������ݿ�
					OrderStepEntity osEntity = new OrderStepEntity();
					osEntity.setOper_time(DateTools.getFormatDateAndTime());
					osEntity.setOrder_num(oEntity.getWorkCardId() + "");
					osEntity.setOrder_state(FinalVariables.ORDER_STATE_IS_READ); // �¹������Ķ�״̬
					odListDB.insert(osEntity);

					intent = new Intent(OrderListActivity.this, OrderDetailActivity.class);
					intent.putExtra("order_num", oEntity.getWorkCardId() + "");
					intent.putExtra("workOrderType", oEntity.getWorkOrderType());
					intent.putExtra("id", oEntity.getId());
					startActivity(intent);
				}

				if (result == 1)
				{
					Toast.makeText(OrderListActivity.this, "����ʧ�ܣ������ԣ�", Toast.LENGTH_LONG).show();
				}
				else if (result == 2)
				{
					Toast.makeText(OrderListActivity.this, "����������", Toast.LENGTH_LONG).show();
				}
				else if (result == 3)
				{
					Toast.makeText(OrderListActivity.this, "����ʧ��,�����쳣��", Toast.LENGTH_LONG).show();
				}
				else if (result == 4)
				{
					Toast.makeText(OrderListActivity.this, "����ʧ�ܣ����ݽ����쳣��", Toast.LENGTH_LONG).show();
				}
			}			
		}
	}
}
