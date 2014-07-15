package com.sealion.serviceassistant;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sealion.serviceassistant.db.QuestionListDB;
import com.sealion.serviceassistant.entity.QuestionEntity;
import com.sealion.serviceassistant.superactivity.OrderActivity;
import com.sealion.serviceassistant.superactivity.ServiceAssistantActivity;

/**
 * 问题详情
 */
public class CheckQuestionActivity extends OrderActivity
{
	private LinearLayout question_content = null;
	private QuestionListDB questionDB = null;
	private ImageView back_image = null;
	private TextView top_bar_title = null;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.check_question);
		
		order_num = this.getIntent().getExtras().getString("order_num");
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
		top_bar_title.setText("问题详情");
		
		question_content = (LinearLayout)this.findViewById(R.id.question_content);
		
		questionDB = new QuestionListDB(this);
		
		ArrayList<QuestionEntity> questionList = questionDB.getQuestionByOrderNum(order_num);
		TextView q_title = null;
		TextView q_content_text = null;
		TextView q_answer_title = null;
		TextView q_answer_text = null; 
		//TextView q_type_text = null;
		//TextView q_category_text = null;
		
		int i = 1;
		for (QuestionEntity qEntity : questionList)
		{
			q_title = new TextView(this);			
			q_title.setText("问题"+i);
			q_title.setTextColor(this.getResources().getColor(R.color.black));
			q_title.setTextSize(20);
			question_content.addView(q_title);
			
			q_content_text = new TextView(this);
			q_content_text.setText(qEntity.getQ_content());
			q_content_text.setTextColor(this.getResources().getColor(R.color.black));
			q_content_text.setTextSize(18);
			question_content.addView(q_content_text);
			
			q_answer_title = new TextView(this);
			q_answer_title.setText("解答");
			q_answer_title.setTextColor(this.getResources().getColor(R.color.black));
			q_answer_title.setTextSize(20);
			question_content.addView(q_answer_title);
			
			q_answer_text = new TextView(this);
			q_answer_text.setText(qEntity.getQ_answer());
			q_answer_text.setTextColor(this.getResources().getColor(R.color.black));
			q_answer_text.setTextSize(18);
			question_content.addView(q_answer_text);
			//q_type_text = new TextView(this);
			//q_type_text.setText(qEntity.get);
			i++;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
		{
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
