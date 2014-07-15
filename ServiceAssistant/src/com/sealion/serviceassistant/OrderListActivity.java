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
 * 工单列表界面
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
				if (oEntity.getOrder_sign() != FinalVariables.ORDER_STATE_ORDER_COMPLISH) // 工单当前处于未完成状态
				{
					if (oEntity.getOrder_sign() == FinalVariables.ORDER_STATE_NOT_READ) // 如果为新工单
					{
						new PostReadData().execute(sp.getString("USERNAME", ""), oEntity.getWorkCardId() + "", DateTools.getFormatDateAndTime());
					}
					else
					{
						if (oEntity.getOrder_sign() == FinalVariables.ORDER_STATE_ARRIVE_TARGET) // 当前工单处于到达现场状态
						{
							intent = new Intent(OrderListActivity.this, CompleteOrderPanelActivity.class);
						}
						else if (oEntity.getOrder_sign() == FinalVariables.ORDER_STATE_APPLY_CHANGE) // 当前工单处于申请改派状态
						{
							intent = new Intent(OrderListActivity.this, ChangeOrderFormActivity.class);
						}
						else if (oEntity.getOrder_sign() == FinalVariables.ORDER_STATE_PHONE_SOLVE) // 当前处于电话解决状态
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
				// 工单当前处于完成状态,则根据工单的完成情况，显示工单详细信息
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

		// 动态注册
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
				Toast.makeText(OrderListActivity.this, "没有符合条件的数据!", Toast.LENGTH_SHORT).show();
			}
			
			oListAdapter = new OrderListAdapter(OrderListActivity.this, orderList);
			oListAdapter.notifyDataSetChanged();
			orderlist.setAdapter(oListAdapter);
		}
	}

    /**
     * 滑动监听，如果滑到了最后一条，则自动查询加载更多的数据
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
					Toast.makeText(OrderListActivity.this, "数据全部加载完!", Toast.LENGTH_SHORT).show();
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
			Toast.makeText(this, "请输入搜索条件", Toast.LENGTH_LONG).show();
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
				Toast.makeText(this, "没有符合条件的数据", Toast.LENGTH_LONG).show();
			}
			search_value.setText("");
		}
	}

	//更新已经阅读
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
					// 更新工单状态为已经阅读
					olDB.updateSignData(oEntity.getWorkCardId() + "", FinalVariables.ORDER_STATE_IS_READ);

					// 写入工单当前步骤状态到本地数据库
					OrderStepEntity osEntity = new OrderStepEntity();
					osEntity.setOper_time(DateTools.getFormatDateAndTime());
					osEntity.setOrder_num(oEntity.getWorkCardId() + "");
					osEntity.setOrder_state(FinalVariables.ORDER_STATE_IS_READ); // 新工单，阅读状态
					odListDB.insert(osEntity);

					intent = new Intent(OrderListActivity.this, OrderDetailActivity.class);
					intent.putExtra("order_num", oEntity.getWorkCardId() + "");
					intent.putExtra("workOrderType", oEntity.getWorkOrderType());
					intent.putExtra("id", oEntity.getId());
					startActivity(intent);
				}

				if (result == 1)
				{
					Toast.makeText(OrderListActivity.this, "操作失败，请重试！", Toast.LENGTH_LONG).show();
				}
				else if (result == 2)
				{
					Toast.makeText(OrderListActivity.this, "服务器错误！", Toast.LENGTH_LONG).show();
				}
				else if (result == 3)
				{
					Toast.makeText(OrderListActivity.this, "操作失败,网络异常！", Toast.LENGTH_LONG).show();
				}
				else if (result == 4)
				{
					Toast.makeText(OrderListActivity.this, "操作失败，数据解析异常！", Toast.LENGTH_LONG).show();
				}
			}			
		}
	}
}
