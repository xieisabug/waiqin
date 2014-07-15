package com.sealion.serviceassistant;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.sealion.serviceassistant.entity.NetBackDataEntity;
import com.sealion.serviceassistant.entity.OrderStepEntity;
import com.sealion.serviceassistant.exception.NetAccessException;
import com.sealion.serviceassistant.net.HttpRestAchieve;
import com.sealion.serviceassistant.superactivity.OrderActivity;
import com.sealion.serviceassistant.tools.DateTools;
import com.sealion.serviceassistant.tools.FinalVariables;

import android.content.res.Resources.NotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

/**   
*  �绰�����ɹ���.
*/
public class PhoneSolveFormActivity extends OrderActivity
{
	private static final String TAG = PhoneSolveFormActivity.class.getSimpleName();
	private EditText question_describe_edittext = null;
	private EditText solve_method_edittext = null;
	private RadioButton radio_btn_yes = null;
	private RadioButton radio_btn_no = null;
	
	private ImageView back_image = null;
	private TextView top_bar_title = null;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.phone_solve_form);
		
		question_describe_edittext = (EditText)this.findViewById(R.id.question_describe_edittext);
		solve_method_edittext = (EditText)this.findViewById(R.id.solve_method_edittext);
		
		radio_btn_yes = (RadioButton)this.findViewById(R.id.radio_btn_yes);
		radio_btn_no = (RadioButton)this.findViewById(R.id.radio_btn_no);
		
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
				finish();
			}
		});
		
		top_bar_title = (TextView)this.findViewById(R.id.top_bar_title);
		top_bar_title.setText("�绰�����ִ��");
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
//		new Handler().postDelayed(new Runnable(){
//		    public void run() {
//		    	PopWin popWin = new PopWin(PhoneSolveFormActivity.this);
//				popWin.initPopWindow(getResources().getString(R.string.solve_tips),1,order_num,null);
//		    }
//		 }, 1000);
		
	}
	
	public void PhoneSolveBtnClick(View target)
	{
		String question_describe_value = question_describe_edittext.getText().toString();
		String solve_method_value = solve_method_edittext.getText().toString();
		String is_repeat_order = "";
		if (question_describe_value == null || question_describe_value.equals(""))
		{
			Toast.makeText(this, "����д��������", Toast.LENGTH_LONG).show();
			return;
		}
		
		if (solve_method_value == null || solve_method_value.equals(""))
		{
			Toast.makeText(this, "����д�������", Toast.LENGTH_LONG).show();
			return;
		}
		if (radio_btn_yes.isChecked())
		{
			is_repeat_order = "��";
		}
		else if(radio_btn_no.isChecked())
		{
			is_repeat_order = "��";
		}
		
		// ���¹���״̬Ϊ���,�����ǵ绰���״̬
		olDB.updateFinishType(order_num, FinalVariables.ORDER_FINISH_TYPE_PHONE_SOLVE);
		
		//д�빤����ǰ״̬���������ݿ�
		OrderStepEntity osEntity = new OrderStepEntity();
		osEntity.setOper_time(DateTools.getFormatDateAndTime());
		osEntity.setOrder_num(order_num);
		osEntity.setOrder_state(FinalVariables.ORDER_STATE_ORDER_COMPLISH); //��ɹ���
		odListDB.insert(osEntity);
		
		new PostData().execute(sp.getString("USERNAME", ""),order_num,
				question_describe_value,solve_method_value,DateTools.getFormatDateAndTime(),is_repeat_order);
	}
	
	private class PostData extends AsyncTask<String,Void,Integer>
	{

		@Override
		protected void onPreExecute()
		{
			ShowDialog();
		}
		
		@Override
		protected Integer doInBackground(String... params)
		{
            return postData(params[0],params[1],params[2],params[3],params[4],params[5]);
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			//doInBackground����ʱ���������仰˵������doInBackgroundִ����󴥷�
			//�����result��������doInBackgroundִ�к�ķ���ֵ������������"ִ�����"
			CancelDialog();
			
			if (result == 0)
			{
				Toast.makeText(PhoneSolveFormActivity.this, "�����ɹ���", Toast.LENGTH_LONG).show();
			}
			else if (result == 1)
			{
				Toast.makeText(PhoneSolveFormActivity.this, "����ʧ�ܣ������ԣ�", Toast.LENGTH_LONG).show();
			}
			else if (result == 2)
			{
				Toast.makeText(PhoneSolveFormActivity.this, "����������", Toast.LENGTH_LONG).show();
			}
			else if (result == 3)
			{
				Toast.makeText(PhoneSolveFormActivity.this, "����ʧ��,�����쳣��", Toast.LENGTH_LONG).show();
			}
			else if (result == 4)
			{
				Toast.makeText(PhoneSolveFormActivity.this, "����ʧ�ܣ����ݽ����쳣��", Toast.LENGTH_LONG).show();
			}
		}
	}
	
	public int postData(String... params)
	{
		HttpRestAchieve httpRestAchieve = new HttpRestAchieve();
		HashMap<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("workerNo", params[0]); //�����Ա����
		paramsMap.put("workOrderNo", params[1]); //������
		paramsMap.put("problemDescription", params[2]); //��������
		paramsMap.put("solutionDescription", params[3]); //�������
		paramsMap.put("finishTime", params[4]); //���ʱ��
		paramsMap.put("generateNewOne", params[5]); //�Ƿ���Ҫ�����ɷ�����
		
		int back_data = 0;
		//ȡ����ע��ť������¼�				
		try
		{
			NetBackDataEntity netBackData = httpRestAchieve.postRequestData(request_url 
					+ "/fieldworker/biz/apply-remote-solution", paramsMap);
			String data = netBackData.getData();
			Log.d(TAG, "����"+data);
			JSONObject jsonObject = new JSONObject(data);
			boolean result = jsonObject.getBoolean("succ");								
			if (result)
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
