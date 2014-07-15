/**   
* @Title: PhoneSolvePanelActivity.java 
* @Package com.sealion.serviceassistant 
* @Description: TODO(用一句话描述该文件做什么) 
* @author jack.lee titans.lee@gmail.com   
* @date 2013-3-4 下午6:38:58 
* @version V1.0
* Copyright: Copyright (c)2012
* Company: 湖南中恩通信技术有限公司
*/
package com.sealion.serviceassistant;

import android.os.Bundle;
import android.view.View;

import com.sealion.serviceassistant.superactivity.ServiceAssistantActivity;


/**   
 * 电话解决回执单操作Panel
 */
public class PhoneSolvePanelActivity extends ServiceAssistantActivity
{
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.phonesolvepanel);
	}
	
	//基本信息和费用报销录入
	public void BaseInfoAndFeeBtnClick(View target)
	{
			
	}
		
	//问题解答
	public void AnswerQuestionBtnClick(View target)
	{
			
	}
}
