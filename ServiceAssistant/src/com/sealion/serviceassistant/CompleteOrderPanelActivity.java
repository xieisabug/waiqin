/**   
 * @Title: CompleteOrderPanelActivity.java 
 * @Package com.sealion.serviceassistant 
 * @Description: TODO(��һ�仰�������ļ���ʲô) 
 * @author jack.lee titans.lee@gmail.com   
 * @date 2013-3-4 ����4:19:38 
 * @version V1.0
 * Copyright: Copyright (c)2012
 * Company: �����ж�ͨ�ż������޹�˾
 */
package com.sealion.serviceassistant;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources.NotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.sealion.serviceassistant.db.CommonOrderComplishDB;
import com.sealion.serviceassistant.db.CostValueDB;
import com.sealion.serviceassistant.db.OrderDetailListDB;
import com.sealion.serviceassistant.db.QuestionListDB;
import com.sealion.serviceassistant.entity.CommonOrderComplishEntity;
import com.sealion.serviceassistant.entity.CostEntity;
import com.sealion.serviceassistant.entity.NetBackDataEntity;
import com.sealion.serviceassistant.entity.OrderStepEntity;
import com.sealion.serviceassistant.entity.QuestionEntity;
import com.sealion.serviceassistant.exception.NetAccessException;
import com.sealion.serviceassistant.net.HttpRestAchieve;
import com.sealion.serviceassistant.superactivity.OrderActivity;
import com.sealion.serviceassistant.tools.DateTools;
import com.sealion.serviceassistant.tools.FinalVariables;

/**
 * ����ֻ�������塣
 */
public class CompleteOrderPanelActivity extends OrderActivity
{
	private static final String TAG = CompleteOrderPanelActivity.class.getSimpleName();

	private TextView top_bar_title = null;
	private int id;
	private int workOrderType;
	private double longitude; // ����
	private double latitude; // γ��
	private int phoneDispose; // �Ƿ�Ϊ�绰���
	private ImageView back_image;
	private String arriveDate;
	private SharedPreferences sp = null;
	private LocationClient mLocationClient = null;
	private BDLocationListener bdLocation = null;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.completepanel);
		sp = getSharedPreferences("SETTING_DATA", Context.MODE_PRIVATE);
		top_bar_title = (TextView) this.findViewById(R.id.top_bar_title);
		back_image = (ImageView) this.findViewById(R.id.back_image);
		back_image.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
		Bundle bundle = this.getIntent().getExtras();
		order_num = bundle.getString("order_num");
		id = bundle.getInt("id");
		workOrderType = bundle.getInt("workOrderType");
		top_bar_title.setText("������  " + order_num);
		order_customer_mobile_value_text = bundle.getString("order_customer_mobile_value_text");
		order_customer_phone_value_text = bundle.getString("order_customer_phone_value_text");
		customer_address = bundle.getString("customer_address");

		if (workOrderType == 2)
		{
			Button answer_question_btn = (Button) this.findViewById(R.id.answer_question_btn);
			answer_question_btn.setVisibility(View.GONE);
		}

		phoneDispose = bundle.getInt("phoneDispose");
		if (phoneDispose == 1)
		{
			Button base_info_btn = (Button) this.findViewById(R.id.base_info_btn);
			Button photo_video_btn = (Button) this.findViewById(R.id.photo_video_btn);
			Button print_btn = (Button) this.findViewById(R.id.print_btn);
			base_info_btn.setVisibility(View.GONE);
			photo_video_btn.setVisibility(View.GONE);
			print_btn.setVisibility(View.GONE);
		}

		OrderDetailListDB odB = new OrderDetailListDB(this);
		arriveDate = odB.getArriveTargetTimeByOrderNum(order_num, FinalVariables.ORDER_STATE_ARRIVE_TARGET);

		if (sp.getInt("RECORD", 0) == 1 && phoneDispose != 1)
		{
			// ¼������
			Toast.makeText(this, "��ǵ�Ϊ�ù����������պ�¼��!!!", Toast.LENGTH_LONG).show();
		}
		
		LocationAddress();
		
		if (mLocationClient != null)
		{
			mLocationClient.start();
		}	
	}
	
	public void LocationAddress()
	{
		mLocationClient = new LocationClient(this);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // ��gps
		option.setCoorType("bd09ll"); // ������������Ϊbd09ll
		option.setPriority(LocationClientOption.NetWorkFirst); // ������������
		option.setProdName("ServiceAssistant"); // ���ò�Ʒ������
		option.setScanSpan(30000 * 20); // ��ʱ��λ��ÿ��10�����Ӷ�λһ�Ρ�

		mLocationClient.setLocOption(option);

		bdLocation = new BDLocationListener()
		{
			@Override
			public void onReceiveLocation(BDLocation location)
			{
				if (location == null)
					return;
				latitude = Double.parseDouble(String.format("%.5f", location.getLatitude()));
				longitude = Double.parseDouble(String.format("%.5f", location.getLongitude()));
				
			}

			public void onReceivePoi(BDLocation location)
			{
				// return ;
			}
		};
		mLocationClient.registerLocationListener(bdLocation);
	}
	 
	@Override
	public void onStart()
	{
		super.onStart();
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		if (mLocationClient != null && mLocationClient.isStarted())
		{
			mLocationClient.unRegisterLocationListener(bdLocation);

			mLocationClient.stop();
			mLocationClient = null;
		}

	}
	 
	// ������Ϣ�ͷ��ñ���¼��
	public void BaseInfoAndFeeBtnClick(View target)
	{
		CommonOrderComplishDB cocDB = new CommonOrderComplishDB(this);
		if (cocDB.getCountByOrder_num(order_num) == 0)
		{
			Intent intent = new Intent(CompleteOrderPanelActivity.this, ReceiptBaseInfoActivity.class);
			intent.putExtra("order_num", order_num);
			intent.putExtra("id", id);
			intent.putExtra("workOrderType", workOrderType);
			this.startActivityForResult(intent, 0);
		}
		else
		{
			Toast.makeText(this, "�Ѿ�¼�������Ϣ�������ظ�¼�룡", Toast.LENGTH_LONG).show();
		}
	}

	// ������
	public void AnswerQuestionBtnClick(View target)
	{
		Intent intent = new Intent(CompleteOrderPanelActivity.this, AnswerQuestionActivity.class);
		intent.putExtra("order_num", order_num);
		this.startActivity(intent);
	}

	// ��Ƭ����Ƶ��Ϣ
	public void PhotoAndVideoBtnClick(View target)
	{
		Intent intent = new Intent(CompleteOrderPanelActivity.this, AudioAndPictureActivity.class);
		intent.putExtra("order_num", order_num);
		this.startActivity(intent);
	}

	// ��ӡԤ��
	public void PrintReviewBtnClick(View target)
	{
		CommonOrderComplishDB cocDB = new CommonOrderComplishDB(this);
		CommonOrderComplishEntity ccEntity = cocDB.SelectById(order_num);

		QuestionListDB qlDB = new QuestionListDB(this);

		if (ccEntity == null)
		{
			Toast.makeText(this, "����ɻص�������Ϣ¼��!", Toast.LENGTH_LONG).show();
		}
		else if (qlDB.haveNotAnswerQuestion(order_num) && workOrderType != 2)
		{
			Toast.makeText(this, "�����������!", Toast.LENGTH_LONG).show();
		}
		else
		{
			// �жϹ����Ƿ���ɣ�����Ѿ������ֱ�ӽ����ӡ���棬�������ύ��ɹ���
			if (olDB.selectOrderSignById(order_num) != FinalVariables.ORDER_STATE_ORDER_COMPLISH)
			{
				CommonOrderComplishDB ccDB = new CommonOrderComplishDB(this);
				CommonOrderComplishEntity cocEntity = ccDB.SelectById(order_num);

				ArrayList<QuestionEntity> qList = qlDB.getQuestionByOrderNum(order_num);
				CostValueDB cvDB = new CostValueDB(this);
				ArrayList<CostEntity> cList = cvDB.getCost(order_num);
				if (workOrderType == 1) // �ύֱ���ɹ�����
				{
					if (qlDB.haveNotAnswerQuestion(order_num))
					{
						Toast.makeText(this, "��������δ���,���Ƚ�����⣡", Toast.LENGTH_LONG).show();
					}
					else
					{
						new PostComplishData(cocEntity, qList, cList, 1, 1,1).execute(sp.getString("USERNAME", ""), order_num, DateTools.getFormatDateAndTime());
					}
				}
				else if (workOrderType == 2) // �طù���
				{
					new PostComplishData(cocEntity, qList, cList, 2, 1,1).execute(sp.getString("USERNAME", ""), order_num, DateTools.getFormatDateAndTime());
				}
			}
			else
			{
				Intent intent = new Intent(CompleteOrderPanelActivity.this, BTPrinterActivity.class);
				intent.putExtra("order_num", order_num);
				this.startActivity(intent);
			}

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == 0) // �Զ�ת�Ƶ�����ش�
		{
			if (resultCode == RESULT_OK) // �����ɹ�
			{

				QuestionListDB qlDB = new QuestionListDB(this);

				if (qlDB.haveNotAnswerQuestion(order_num))
				{
					Intent intent = new Intent(CompleteOrderPanelActivity.this, AnswerQuestionActivity.class);
					intent.putExtra("order_num", order_num);
					this.startActivity(intent);
				}
				else
				{
					Toast.makeText(this, "����ɹ������ߴ�ӡ����", Toast.LENGTH_LONG).show();
				}
			}
		}
	}

	// ��ɹ���
	public void CompleteOrderBtnClick(View target)
	{		
		if (phoneDispose == 1)// �绰�����ɹ���
		{
			QuestionListDB qlDB = new QuestionListDB(this);
			if (qlDB.haveNotAnswerQuestion(order_num))
			{
				Toast.makeText(this, "��������δ���,���Ƚ�����⣡", Toast.LENGTH_LONG).show();
			}
			else
			{
				ArrayList<QuestionEntity> qList = qlDB.getQuestionByOrderNum(order_num);
				new PostComplishData(null, qList, null, 1, 2,0).execute(sp.getString("USERNAME", ""), order_num, DateTools.getFormatDateAndTime());
			}
		}
		else
		{
			CommonOrderComplishDB ccDB = new CommonOrderComplishDB(this);
			if (ccDB.getCountByOrder_num(order_num) == 0) // δ��д�ص������Ի����Ƿ���Ҫ��ɹ���
			{
				new AlertDialog.Builder(this).setTitle("ѡ��δ��д�ص���Ϣ���Ƿ���ɹ�����").setIcon(android.R.drawable.ic_dialog_info)
						.setSingleChoiceItems(new String[]{"����δ���","�����ѽ��"}, 1, new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int which)
							{
								new PostFinishData().execute(sp.getString("USERNAME", ""), order_num, DateTools.getFormatDateAndTime(),which+"");
								dialog.dismiss();
							}
						}).setNegativeButton("ȡ��", new OnClickListener()
						{

							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								dialog.dismiss();
							}
						}).show();
				
			}
			else
			{
				if (olDB.selectOrderSignById(order_num) != FinalVariables.ORDER_STATE_ORDER_COMPLISH)
				{
					CommonOrderComplishEntity cocEntity = ccDB.SelectById(order_num);
					QuestionListDB qlDB = new QuestionListDB(this);

					ArrayList<QuestionEntity> qList = qlDB.getQuestionByOrderNum(order_num);
					CostValueDB cvDB = new CostValueDB(this);
					ArrayList<CostEntity> cList = cvDB.getCost(order_num);
					if (workOrderType == 1) // �ύֱ���ɹ�����
					{
						if (qlDB.haveNotAnswerQuestion(order_num))
						{
							Toast.makeText(this, "��������δ���,���Ƚ�����⣡", Toast.LENGTH_LONG).show();
						}
						else
						{
							new PostComplishData(cocEntity, qList, cList, 1, 1,0).execute(sp.getString("USERNAME", ""), order_num, DateTools.getFormatDateAndTime());
						}
					}
					else if (workOrderType == 2) // �طù���
					{
						new PostComplishData(cocEntity, qList, cList, 2, 1,0).execute(sp.getString("USERNAME", ""), order_num, DateTools.getFormatDateAndTime());
					}
				}
				else
				{
					Toast.makeText(this, "��������������ύ�������ظ��ύ��", Toast.LENGTH_LONG).show();
				}
			}
		}
	}

	public class PostFinishData extends AsyncTask<String, Void, Integer>
	{
		@Override
		protected void onPreExecute()
		{
			ShowDialog();
		}
		
		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Integer doInBackground(String... params)
		{
			int back_data = 0;
			HttpRestAchieve httpRestAchieve = new HttpRestAchieve();
			HashMap<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.put("workerId", params[0]);
			paramsMap.put("workOrderNo", params[1]);
			paramsMap.put("finishTime", params[2]);
			paramsMap.put("problemSolved",params[3]);
			try
			{

				NetBackDataEntity netBackData = httpRestAchieve.postRequestData(request_url + "/fieldworker/biz/finish", paramsMap);
				String data = netBackData.getData();
				Log.i("70Apps",data);
				JSONObject jsonObject = new JSONObject(data);
				int result = jsonObject.getInt("resultCode");
				if (result == 1)
				{
					ComplishData();
					setResult(RESULT_OK, null);
					finish();
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
			showTips(result);
		}
	}

	// ��ɹ���
	public class PostComplishData extends AsyncTask<String, Void, Integer>
	{
		private CommonOrderComplishEntity cocEntity;
		private ArrayList<QuestionEntity> qList;
		private int workOrderType;
		private int type;
		private ArrayList<CostEntity> cList;
		private int is_print = 0;
		
		public PostComplishData(CommonOrderComplishEntity cocEntity, ArrayList<QuestionEntity> qList, ArrayList<CostEntity> cList, int workOrderType, int type,int is_print)
		{
			this.cocEntity = cocEntity;
			this.qList = qList;
			this.workOrderType = workOrderType;
			this.type = type;
			this.cList = cList;
			this.is_print = is_print;
		}

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
			try
			{

				if (type == 1) // ��ͨ���
				{
					HashMap<String, String> applyMap = new HashMap<String, String>();
					// ��Ӳ�����ֵ��
					applyMap.put("customerUpdatFlag", cocEntity.getIs_send_to_server() + ""); // ��־���ͻ������Ƿ����
					//applyMap.put("workOrderNo", cocEntity.getOrder_num()); // ������
					applyMap.put("workOrderNo", cocEntity.getOrder_num()); // ������
					applyMap.put("serviceDate", cocEntity.getService_time()); // ����ʱ��
					applyMap.put("orderCode", cocEntity.getNumber_value()); // ���ݺ�
					applyMap.put("customerAddr", cocEntity.getService_address()); // �ͻ���ַ
					applyMap.put("latitude", latitude + ""); // γ��
					applyMap.put("longitude", longitude + ""); // ����
					applyMap.put("linkPerson", cocEntity.getContact_name()); // ��ϵ��
					applyMap.put("customerTelphone", cocEntity.getCustomer_tel()); // ��ϵ�绰
					applyMap.put("customerMobile", cocEntity.getCustomer_mobile()); // ��ϵ�ֻ�
					applyMap.put("arriveDate", arriveDate);// ����ʱ��
					applyMap.put("serviceEndDate", ""); // �����ֹʱ��
					applyMap.put("customerSignature", cocEntity.getCustomer_sign()); // �ͻ�ǩ��

					applyMap.put("spendingProcess", cocEntity.getCustomer_charge()); // ���ù���
					applyMap.put("flag", "1"); // �����ʽ
					applyMap.put("notes", cocEntity.getCustomer_remark()); // ��ע
					
					applyMap.put("isCharge", cocEntity.getIs_charge()+""); //�Ƿ��շ�
					applyMap.put("isChargeValue", cocEntity.getIs_charge_value()); //�շѽ��

					if (workOrderType == 2)// �طù���
					{
						applyMap.put("visitType", cocEntity.getVisit_type());
						applyMap.put("productStatus", cocEntity.getVisit_product_case());
						applyMap.put("handleResult", cocEntity.getVisit_dispose_result());
						applyMap.put("customerIfSatisfied_product", cocEntity.getProduct_evaluate()+"");
						applyMap.put("customerIfSatisfied_service", cocEntity.getService_evaluate()+"");
					}

					if (workOrderType == 1)
					{
						StringBuilder questionId = new StringBuilder();
						StringBuilder questionTypId = new StringBuilder();
						StringBuilder questionTypPId = new StringBuilder();
						StringBuilder questionDesc = new StringBuilder();
						StringBuilder solutionMethod = new StringBuilder();
						StringBuilder productId = new StringBuilder();
						
						for (QuestionEntity qEntity : qList)
						{
							questionId.append(qEntity.getQ_id() + "^|");
							questionTypId.append(qEntity.getQ_type_id() + "^|");
							questionTypPId.append(qEntity.getQ_category_id() + "^|");
							questionDesc.append(qEntity.getQ_content() + "^|");
							solutionMethod.append(qEntity.getQ_answer() + "^|");
							productId.append(qEntity.getProductId()+"^|");
						}

						applyMap.put("questionId", questionId.toString());
						applyMap.put("questionTypId", questionTypId.toString());
						applyMap.put("questionTypPId", questionTypPId.toString());
						applyMap.put("questionDesc", questionDesc.toString());
						applyMap.put("solutionMethod", solutionMethod.toString());
						applyMap.put("productId",productId.toString());
						applyMap.put("hardwareCode",cocEntity.getSoftware_type());
						applyMap.put("softwareVersion",cocEntity.getSoftware_version());
						applyMap.put("environment",cocEntity.getSoftware_env_value());
					}

					if (cList != null && cList.size() > 0)
					{
						StringBuilder spendingItemId = new StringBuilder();
						StringBuilder spendingName = new StringBuilder();
						StringBuilder spending = new StringBuilder();

						for (CostEntity costEntity : cList)
						{
							spendingItemId.append(costEntity.getCost_id() + "^|");
							spendingName.append(costEntity.getName() + "^|");
							spending.append(costEntity.getValue() + "^|");
						}
						applyMap.put("spendingItemId", spendingItemId.toString());
						applyMap.put("spendingName", spendingName.toString());
						applyMap.put("spending", spending.toString());
					}
                    Log.d(TAG + " ��ͨ���", applyMap.toString());
					NetBackDataEntity netData = httpRestAchieve.postRequestData(request_url + "/fieldworker/biz/apply-receipt", applyMap);
					String d = netData.getData();
					JSONObject jObject = new JSONObject(d);
					int r = jObject.getInt("resultCode");
					if (r == 1)
					{
						ComplishData();
						back_data = 0;
						finish();
					}
					else
					{
						back_data = 1;
					}
				}
				else if (type == 2) // �绰���
				{
					// �绰���
					HashMap<String, String> applyMap = new HashMap<String, String>();
					StringBuilder questionId = new StringBuilder();
					StringBuilder questionTypId = new StringBuilder();
					StringBuilder questionTypPId = new StringBuilder();
					StringBuilder questionDesc = new StringBuilder();
					StringBuilder solutionMethod = new StringBuilder();
					StringBuilder productId = new StringBuilder();
					
					applyMap.put("workerNo", params[0]);
					applyMap.put("workOrderNo", params[1]); // ������
					for (QuestionEntity qEntity : qList)
					{
						questionId.append(qEntity.getQ_id() + "^|");
						questionTypId.append(qEntity.getQ_type_id() + "^|");
						questionTypPId.append(qEntity.getQ_category_id() + "^|");
						questionDesc.append(qEntity.getQ_content() + "^|");
						solutionMethod.append(qEntity.getQ_answer() + "^|");
						productId.append(qEntity.getProductId()+"^|");
					}
					
					applyMap.put("productId",productId.toString());
					applyMap.put("questionId", questionId.toString());
					applyMap.put("questionTypId", questionTypId.toString());
					applyMap.put("questionTypPId", questionTypPId.toString());
					applyMap.put("questionDesc", questionDesc.toString());
					applyMap.put("solutionMethod", solutionMethod.toString());
                    Log.d(TAG + " �绰���", applyMap.toString());

                    NetBackDataEntity netData = httpRestAchieve.postRequestData(request_url + "/fieldworker/biz/apply-remote-solution", applyMap);
					String d = netData.getData();
					JSONObject jObject = new JSONObject(d);
					int r = jObject.getInt("resultCode");
					if (r == 1)
					{
						ComplishData();
						back_data = 0;
						setResult(RESULT_OK, null);
						finish();
					}
					else
					{
						back_data = 1;
					}
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
			showTips(result);
			if (is_print == 1)
			{
				Intent intent = new Intent(CompleteOrderPanelActivity.this, BTPrinterActivity.class);
				intent.putExtra("order_num", order_num);
				startActivity(intent);
			}
		}
	}

	private void showTips(int result)
	{
		CancelDialog();
		
		if (result == 0)
		{
			Toast.makeText(CompleteOrderPanelActivity.this, "�����ɹ���", Toast.LENGTH_LONG).show();
		}
		else if (result == 1)
		{
			Toast.makeText(CompleteOrderPanelActivity.this, "����ʧ�ܣ������ԣ�", Toast.LENGTH_LONG).show();
		}
		else if (result == 2)
		{
			Toast.makeText(CompleteOrderPanelActivity.this, "����������", Toast.LENGTH_LONG).show();
		}
		else if (result == 3)
		{
			Toast.makeText(CompleteOrderPanelActivity.this, "����ʧ��,�����쳣��", Toast.LENGTH_LONG).show();
		}
		else if (result == 4)
		{
			Toast.makeText(CompleteOrderPanelActivity.this, "����ʧ�ܣ����ݽ����쳣��", Toast.LENGTH_LONG).show();
		}
	}

	public void ComplishData()
	{
		// д�빤����ǰ״̬���������ݿ�
		OrderStepEntity osEntity = new OrderStepEntity();
		osEntity.setOper_time(DateTools.getFormatDateAndTime());
		osEntity.setOrder_num(order_num);
		osEntity.setOrder_state(FinalVariables.ORDER_STATE_ORDER_COMPLISH); // ��ɹ���
		odListDB.insert(osEntity);
		// ���¹���״̬Ϊ���
		if(phoneDispose == 1)
		{
			olDB.updateFinishType(order_num, FinalVariables.ORDER_FINISH_TYPE_PHONE_SOLVE);
		}
		else if (workOrderType == 2)
		{
			olDB.updateFinishType(order_num, FinalVariables.ORDER_FINISH_TYPE_VISIT_SOLVE);
		}
		else
		{
			olDB.updateFinishType(order_num, FinalVariables.ORDER_FINISH_TYPE_SPOT_SOLVE);
		}
	}

}
