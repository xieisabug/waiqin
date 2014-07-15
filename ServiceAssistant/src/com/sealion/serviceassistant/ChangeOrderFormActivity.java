package com.sealion.serviceassistant;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.sealion.serviceassistant.db.OrderChangeDB;
import com.sealion.serviceassistant.entity.NetBackDataEntity;
import com.sealion.serviceassistant.entity.OrderChangeEntity;
import com.sealion.serviceassistant.entity.OrderStepEntity;
import com.sealion.serviceassistant.exception.NetAccessException;
import com.sealion.serviceassistant.net.HttpRestAchieve;
import com.sealion.serviceassistant.superactivity.OrderActivity;
import com.sealion.serviceassistant.tools.DateTools;
import com.sealion.serviceassistant.tools.EditTextLengthConstants;
import com.sealion.serviceassistant.tools.FinalVariables;
import com.sealion.serviceassistant.tools.StringUtils;

import android.app.Activity;
import android.content.res.Resources.NotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**   
* ������ɹ�����
*/
public class ChangeOrderFormActivity extends OrderActivity
{
	private static final String TAG = ChangeOrderFormActivity.class.getSimpleName();
	
	private EditText change_course_edittext = null;
	private ImageView back_image = null;
	private TextView top_bar_title = null;
	private OrderChangeDB ocDB = null;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.change_order_form);
		
		change_course_edittext = (EditText)this.findViewById(R.id.change_course_edittext);
		
		StringUtils.lengthFilter(ChangeOrderFormActivity.this, change_course_edittext, 
				EditTextLengthConstants.CHANGE_ORDER_LENGTH, "�������ԭ���ܳ���"+EditTextLengthConstants.CHANGE_ORDER_LENGTH+"����");
		
		Bundle bundle = this.getIntent().getExtras();
		order_num = bundle.getString("order_num");
		order_customer_mobile_value_text = bundle.getString("order_customer_mobile_value_text");
		order_customer_phone_value_text = bundle.getString("order_customer_phone_value_text");
		customer_address = bundle.getString("customer_address");
		
		back_image = (ImageView)this.findViewById(R.id.back_image);
		back_image.setOnClickListener(new View.OnClickListener()
		{			
			@Override
			public void onClick(View v)
			{
				setResult(RESULT_CANCELED, null);
				finish();
			}
		});
		
		top_bar_title = (TextView)this.findViewById(R.id.top_bar_title);
		top_bar_title.setText("������ɹ���");
		ocDB = new OrderChangeDB(this);
	}
	
	public void CancelBtnClick(View target)
	{
		setResult(RESULT_CANCELED, null);
		this.finish();
	}
	
	public void ChangeOrderBtnClick(View target)
	{
		String change_course_text = change_course_edittext.getText().toString();
		
		if (change_course_text == null || change_course_text.equals(""))
		{
			Toast.makeText(this, "����ԭ����Ϊ��!", Toast.LENGTH_LONG).show();
			return;
		}
		
		new PostData().execute(
				order_num, //������
				sp.getString("USERNAME", ""), //�����Ա����
				change_course_text,//����ԭ��
				DateTools.getFormatDateAndTime()
				);
	}
	
	private class PostData extends AsyncTask<String,Void,Integer>
	{
		String reason;
		String unacceptTime;
		
		@Override
		protected void onPreExecute()
		{
			ShowDialog();
		}
		
		@Override
		protected Integer doInBackground(String... params)
		{
			int result = postData(params);
			reason = params[2];
			unacceptTime = params[3];
			return result;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			// ���¹���״̬Ϊ���
			olDB.updateFinishType(order_num, FinalVariables.ORDER_FINISH_TYPE_CHANGE_SOLVE);
			
			//д�빤����ǰ״̬���������ݿ�
			OrderStepEntity osEntity = new OrderStepEntity();
			osEntity.setOper_time(DateTools.getFormatDateAndTime());
			osEntity.setOrder_num(order_num);
			osEntity.setOrder_state(FinalVariables.ORDER_STATE_ORDER_COMPLISH); //��ɹ���
			odListDB.insert(osEntity);
			
			OrderChangeEntity ocEntity = new OrderChangeEntity();
			ocEntity.setOrder_num(order_num);
			ocEntity.setChange_reason(reason);
			ocEntity.setChange_time(unacceptTime);
			ocDB.insert(ocEntity);
			
			//doInBackground����ʱ���������仰˵������doInBackgroundִ����󴥷�
			//�����result��������doInBackgroundִ�к�ķ���ֵ������������"ִ�����"
			CancelDialog();
			
			if (result == 0)
			{
				Toast.makeText(ChangeOrderFormActivity.this, "�����ɹ���", Toast.LENGTH_LONG).show();
				setResult(RESULT_OK, null);
				finish();
			}
			else if (result == 1)
			{
				Toast.makeText(ChangeOrderFormActivity.this, "����ʧ�ܣ������ԣ�", Toast.LENGTH_LONG).show();
			}
			else if (result == 2)
			{
				Toast.makeText(ChangeOrderFormActivity.this, "����������", Toast.LENGTH_LONG).show();
			}
			else if (result == 3)
			{
				Toast.makeText(ChangeOrderFormActivity.this, "����ʧ��,�����쳣��", Toast.LENGTH_LONG).show();
			}
			else if (result == 4)
			{
				Toast.makeText(ChangeOrderFormActivity.this, "����ʧ�ܣ����ݽ����쳣��", Toast.LENGTH_LONG).show();
			}
		}
	}
	
	public int postData(String... params)
	{
		HttpRestAchieve httpRestAchieve = new HttpRestAchieve();
		HashMap<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("workerNo", params[1]); //
		paramsMap.put("workOrderNo", params[0]);
		paramsMap.put("reason", params[2]);
		paramsMap.put("unacceptTime", params[3]);
		
		int back_data = 0;
		//ȡ����ע��ť������¼�				
		try
		{
			NetBackDataEntity netBackData = httpRestAchieve.postRequestData(request_url 
					+ "/fieldworker/biz/reassign", paramsMap);
			String data = netBackData.getData();
			Log.d(TAG, "����"+data);
			JSONObject jsonObject = new JSONObject(data);
			int result = jsonObject.getInt("resultCode");								
			if (result == 1)
			{
				back_data = 0;
				Log.d(TAG, "�������ݳɹ�");
			}
			else
			{
				//��¼���ݵ��������ݿ⣬�ȴ������绷�������ͬ��
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
}
