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
*  电话解决完成工单.
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
		top_bar_title.setText("电话解决回执单");
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
			Toast.makeText(this, "请填写问题描述", Toast.LENGTH_LONG).show();
			return;
		}
		
		if (solve_method_value == null || solve_method_value.equals(""))
		{
			Toast.makeText(this, "请填写解决方法", Toast.LENGTH_LONG).show();
			return;
		}
		if (radio_btn_yes.isChecked())
		{
			is_repeat_order = "是";
		}
		else if(radio_btn_no.isChecked())
		{
			is_repeat_order = "否";
		}
		
		// 更新工单状态为完成,并且是电话完成状态
		olDB.updateFinishType(order_num, FinalVariables.ORDER_FINISH_TYPE_PHONE_SOLVE);
		
		//写入工单当前状态到本地数据库
		OrderStepEntity osEntity = new OrderStepEntity();
		osEntity.setOper_time(DateTools.getFormatDateAndTime());
		osEntity.setOrder_num(order_num);
		osEntity.setOrder_state(FinalVariables.ORDER_STATE_ORDER_COMPLISH); //完成工单
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
			//doInBackground返回时触发，换句话说，就是doInBackground执行完后触发
			//这里的result就是上面doInBackground执行后的返回值，所以这里是"执行完毕"
			CancelDialog();
			
			if (result == 0)
			{
				Toast.makeText(PhoneSolveFormActivity.this, "操作成功！", Toast.LENGTH_LONG).show();
			}
			else if (result == 1)
			{
				Toast.makeText(PhoneSolveFormActivity.this, "操作失败，请重试！", Toast.LENGTH_LONG).show();
			}
			else if (result == 2)
			{
				Toast.makeText(PhoneSolveFormActivity.this, "服务器错误！", Toast.LENGTH_LONG).show();
			}
			else if (result == 3)
			{
				Toast.makeText(PhoneSolveFormActivity.this, "操作失败,网络异常！", Toast.LENGTH_LONG).show();
			}
			else if (result == 4)
			{
				Toast.makeText(PhoneSolveFormActivity.this, "操作失败，数据解析异常！", Toast.LENGTH_LONG).show();
			}
		}
	}
	
	public int postData(String... params)
	{
		HttpRestAchieve httpRestAchieve = new HttpRestAchieve();
		HashMap<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("workerNo", params[0]); //外服人员工号
		paramsMap.put("workOrderNo", params[1]); //工单号
		paramsMap.put("problemDescription", params[2]); //问题描述
		paramsMap.put("solutionDescription", params[3]); //解决方法
		paramsMap.put("finishTime", params[4]); //完成时间
		paramsMap.put("generateNewOne", params[5]); //是否需要重新派发工单
		
		int back_data = 0;
		//取消关注按钮被点击事件				
		try
		{
			NetBackDataEntity netBackData = httpRestAchieve.postRequestData(request_url 
					+ "/fieldworker/biz/apply-remote-solution", paramsMap);
			String data = netBackData.getData();
			Log.d(TAG, "返回"+data);
			JSONObject jsonObject = new JSONObject(data);
			boolean result = jsonObject.getBoolean("succ");								
			if (result)
			{
				back_data = 0;
				Log.d(TAG, "发送数据成功");
			}
			else
			{
				//记录数据到本地数据库，等待有网络环境则进行同步
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
