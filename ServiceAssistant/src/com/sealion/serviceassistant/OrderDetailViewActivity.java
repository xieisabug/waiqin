package com.sealion.serviceassistant;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.sealion.serviceassistant.adapter.MediaGridAdapter;
import com.sealion.serviceassistant.db.CommonOrderComplishDB;
import com.sealion.serviceassistant.db.CostValueDB;
import com.sealion.serviceassistant.db.FileManageDB;
import com.sealion.serviceassistant.db.OrderChangeDB;
import com.sealion.serviceassistant.db.OrderDetailListDB;
import com.sealion.serviceassistant.db.OrderListDB;
import com.sealion.serviceassistant.db.QuestionListDB;
import com.sealion.serviceassistant.entity.CommonOrderComplishEntity;
import com.sealion.serviceassistant.entity.CostEntity;
import com.sealion.serviceassistant.entity.FileEntity;
import com.sealion.serviceassistant.entity.MediaAttributeEntity;
import com.sealion.serviceassistant.entity.OrderChangeEntity;
import com.sealion.serviceassistant.entity.OrderEntity;
import com.sealion.serviceassistant.entity.OrderStepEntity;
import com.sealion.serviceassistant.entity.QuestionEntity;
import com.sealion.serviceassistant.superactivity.OrderActivity;
import com.sealion.serviceassistant.tools.FinalVariables;

/**
 * �ɹ�������Ԥ������
 */
public class OrderDetailViewActivity extends OrderActivity
{
	private static final String TAG = OrderDetailViewActivity.class.getSimpleName();
	private static final String path_prefix = "/mnt/sdcard/sound_recorder/";
	
	private LinearLayout content_panel = null;
	private int id;
	private TextView top_bar_title = null;
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
	private TextView order_optimal_path_value = null; //�Ƽ�·��
	private TextView dispose_process_step = null;
	
	private TextView current_state_complish_value = null;
	private OrderListDB olDB = null; //����ԭ����Ϣ
	private OrderDetailListDB odListDB = null; //����״̬�׶���Ϣ
	
	LayoutInflater inflater = null; 
		
	private ArrayList<MediaAttributeEntity> image_list = null;
	private ArrayList<MediaAttributeEntity> audio_list = null;

	private MediaGridAdapter mgAdapter1 = null;
	private MediaGridAdapter mgAdapter2 = null;
	private GridView view_image_gridview = null;
	private GridView view_audio_gridview = null;
	private ImageView back_image = null;
	private LinearLayout hide_panel = null;
	private int workOrderType = 0;
	private OrderEntity oEntity = null;
	private Button print_btn;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.order_view_detail);
		content_panel = (LinearLayout)this.findViewById(R.id.dynamic_content_panel);
		top_bar_title = (TextView)this.findViewById(R.id.top_bar_title);
		order_num = this.getIntent().getExtras().getString("order_num");
		id = this.getIntent().getExtras().getInt("id");
		//��ȡ������Ϣ
		top_bar_title.setText("������  "+order_num);
		
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
		
		current_state_complish_value = (TextView)this.findViewById(R.id.current_state_complish_value);
		dispose_process_step = (TextView)this.findViewById(R.id.dispose_process_step);
		olDB = new OrderListDB(this);
		oEntity = olDB.SelectById(id);
		current_state_value.setText(CurrentOrderState(oEntity.getOrder_sign()));
		order_id_value.setText(oEntity.getWorkCardId() + "");
		order_optimal_path_value.setText(FliterString(oEntity.getOptimal_path()));
		order_service_time_value.setText(oEntity.getServiceDate()); // ��������
		workOrderType = oEntity.getWorkOrderType();
		if (workOrderType == 1)
		{
			order_send_type_value.setText("�����ɹ�"); // �ɹ����
		}
		else if (workOrderType == 2)
		{
			order_send_type_value.setText("�ط��ɹ�");
		}
		order_remark_value.setText(oEntity.getWorkOrderDescription()); // �ɹ�˵��
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
		

		order_contact_name_value.setText(oEntity.getLinkPerson()); // �ͻ���ϵ��
		order_customer_name_value.setText(oEntity.getCustomerName()); // �ͻ���˾����
		order_contact_tax_num_value.setText(oEntity.getTaxCode()); // ˰��
		order_tax_department_value.setText(oEntity.getRevenueName()); // ����˰��־�����
		order_customer_address_value.setText(FliterString(oEntity.getCustomerAddr())); // ���ŵ�ַ
		order_customer_mobile_value.setText(FliterString(oEntity.getCustomerMobile())); // �ͻ��ֻ�
		order_customer_phone_value.setText(FliterString(oEntity.getCustomerTel())); // �ͻ���ϵ�绰
		
		odListDB = new OrderDetailListDB(this);
		
		inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		back_image = (ImageView)this.findViewById(R.id.back_image);
		back_image.setOnClickListener(new View.OnClickListener()
		{			
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
		
		hide_panel = (LinearLayout)this.findViewById(R.id.hide_panel);
		hide_panel.setVisibility(View.VISIBLE);
		
		print_btn = (Button)this.findViewById(R.id.print_btn);
		print_btn.setVisibility(View.VISIBLE);
	}
	
	public void onStart()
	{
		super.onStart();
		if (oEntity != null)
		{
			current_state_value.setText(CurrentOrderState(oEntity.getFinish_type()));
			
			//���ع����׶�ִ����Ϣ
			ArrayList<OrderStepEntity> stepList = odListDB.getOrderStepByOrderNum(order_num);
						
			StringBuilder sBuilder = new StringBuilder();
			for (OrderStepEntity osEntity : stepList)
			{
				sBuilder.append(stateTransfText(osEntity.getOrder_state()) +" "+ osEntity.getOper_time() +"\n");
			}
			
			dispose_process_step.setText(sBuilder.toString());
			
			if (content_panel != null)
			{
				content_panel.removeAllViews();
			}
			//-------------------------------------------
					
			if (oEntity.getFinish_type() == FinalVariables.ORDER_FINISH_TYPE_SPOT_SOLVE) //�����ֳ������ִ����ϸ��Ϣ
			{
				current_state_complish_value.setText("�ֳ������ִ��");
				loadingSpotSolveInfo();				
			}
			else if (oEntity.getFinish_type() == FinalVariables.ORDER_FINISH_TYPE_PHONE_SOLVE) //���ص绰�����ִ����Ϣ
			{
				current_state_complish_value.setText("�绰�����ִ��");
				if (print_btn != null)
				{
					print_btn.setVisibility(View.GONE);
				}
				loadingPhoneSolveInfo();
			}
			else if (oEntity.getFinish_type() == FinalVariables.ORDER_FINISH_TYPE_CHANGE_SOLVE) //���ɹ�����ִ��
			{
				current_state_complish_value.setText("��������ύ��");
				loadingChangeSolveInfo();
			}
			
			else if (oEntity.getFinish_type() == FinalVariables.ORDER_FINISH_TYPE_VISIT_SOLVE) //�طù���
			{
				current_state_complish_value.setText("�طù���");
				loadingSpotSolveInfo();	
			}
		}	
	}
	
	@Override
	public void onDestroy()
	{
		
		super.onDestroy();
	}
	
	public void PrintBtnClick(View target)
	{
		Intent intent = new Intent(OrderDetailViewActivity.this, BTPrinterActivity.class);
		intent.putExtra("order_num", order_num);
		this.startActivity(intent);
	}
	
	//�����ֳ������ִ����ϸ��Ϣ
	public void loadingSpotSolveInfo() //�����ֳ������ִ����ϸ��Ϣ
	{
		if (order_num!= null && !order_num.equals(""))
		{
			if (inflater != null)
			{
				CommonOrderComplishDB cocDB = new CommonOrderComplishDB(this); //��ȡ�ص���Ϣ
				CommonOrderComplishEntity ccEntity = cocDB.SelectById(order_num);
				if (ccEntity != null)
				{
					View view = inflater.inflate(R.layout.view_spot_solve, null);
					TextView receipt_base_customer_name_value = (TextView)view.findViewById(R.id.receipt_base_customer_name_value);
					TextView receipt_base_customer_tax_value = (TextView)view.findViewById(R.id.receipt_base_customer_tax_value);
					TextView receipt_base_tax_officer_value = (TextView)view.findViewById(R.id.receipt_base_tax_officer_value);
					TextView receipt_base_service_address_value = (TextView)view.findViewById(R.id.receipt_base_service_address_value);
					TextView receipt_base_contact_name_value = (TextView)view.findViewById(R.id.receipt_base_contact_name_value);
					TextView receipt_base_customer_tel_value = (TextView)view.findViewById(R.id.receipt_base_customer_tel_value);
					TextView receipt_base_customer_mobile_value = (TextView)view.findViewById(R.id.receipt_base_customer_mobile_value);				
					TextView receipt_base_number_value = (TextView)view.findViewById(R.id.receipt_base_number_value);
					TextView receipt_base_customer_sign_value = (TextView)view.findViewById(R.id.receipt_base_customer_sign_value);
					TextView receipt_base_customer_charge_value = (TextView)view.findViewById(R.id.receipt_base_customer_charge_value);
					TextView receipt_base_customer_remark_value = (TextView)view.findViewById(R.id.receipt_base_customer_remark_value);
					TextView receipt_base_customer_solve_value = (TextView)view.findViewById(R.id.receipt_base_customer_solve_value);
					
					TextView is_upload_base_info_value = (TextView)view.findViewById(R.id.is_upload_base_info_value);
					
					view_image_gridview = (GridView)view.findViewById(R.id.view_image_gridview);
					view_audio_gridview = (GridView)view.findViewById(R.id.view_audio_gridview);
										
					if (ccEntity.getIs_send_to_server() == 1)
					{
						is_upload_base_info_value.setText("��");
					}
					else if (ccEntity.getIs_send_to_server() == 2)
					{
						is_upload_base_info_value.setText("��");
					}
					
					receipt_base_customer_name_value.setText(FliterString(ccEntity.getContact_name()));
					receipt_base_customer_tax_value.setText(FliterString(ccEntity.getCustomer_tax()));
					receipt_base_tax_officer_value.setText(FliterString(ccEntity.getTax_officer()));
					receipt_base_service_address_value.setText(ccEntity.getService_address());
					receipt_base_contact_name_value.setText(ccEntity.getContact_name());
					receipt_base_customer_tel_value.setText(FliterString(ccEntity.getCustomer_tel()));
					receipt_base_customer_mobile_value.setText(FliterString(ccEntity.getCustomer_mobile()));
					receipt_base_number_value.setText(ccEntity.getNumber_value());
					receipt_base_customer_sign_value.setText(FliterString(ccEntity.getCustomer_sign()));				
					receipt_base_customer_charge_value.setText(FliterString(ccEntity.getCustomer_charge()));
					receipt_base_customer_remark_value.setText(FliterString(ccEntity.getCustomer_remark()));
					
					// 0:���� 1���û�����
					if (ccEntity.getCustomer_solve() == 0)
					{
						receipt_base_customer_solve_value.setText("���� ");
					}
					else
					{
						receipt_base_customer_solve_value.setText("�û�����");
					}
					
					
					if (workOrderType == 2) //�طù���
					{
						RelativeLayout layout_qa = (RelativeLayout)view.findViewById(R.id.layout_qa);
						layout_qa.setVisibility(View.GONE);
						TableRow visit_type_row = (TableRow)view.findViewById(R.id.visit_type_row);
						TableRow dispose_result_row = (TableRow)view.findViewById(R.id.dispose_result_row);
						TableRow product_case_row = (TableRow)view.findViewById(R.id.product_case_row);
						visit_type_row.setVisibility(View.VISIBLE);
						dispose_result_row.setVisibility(View.VISIBLE);
						product_case_row.setVisibility(View.VISIBLE);
						
						TextView receipt_type_value = (TextView)view.findViewById(R.id.receipt_type_value);
						receipt_type_value.setText(FliterString(ccEntity.getVisit_type()));
						TextView product_case_value = (TextView)view.findViewById(R.id.product_case_value);
						product_case_value.setText(FliterString(ccEntity.getVisit_product_case()));
						TextView dispose_result_value = (TextView)view.findViewById(R.id.dispose_result_value);
						dispose_result_value.setText(FliterString(ccEntity.getVisit_dispose_result()));
					}
					else
					{
						TableRow receipt_base_software_env_row = (TableRow)view.findViewById(R.id.receipt_base_software_env_row);
						TableRow receipt_base_software_version_row = (TableRow)view.findViewById(R.id.receipt_base_software_version_row);
						TableRow receipt_base_software_type_row = (TableRow)view.findViewById(R.id.receipt_base_software_type_row);
						TableRow order_is_charge_row = (TableRow)view.findViewById(R.id.order_is_charge_row);
						TableRow order_charge_money_row = (TableRow)view.findViewById(R.id.order_charge_money_row);
						order_is_charge_row.setVisibility(View.VISIBLE);
						order_charge_money_row.setVisibility(View.VISIBLE);
						receipt_base_software_env_row.setVisibility(View.VISIBLE);
						receipt_base_software_version_row.setVisibility(View.VISIBLE);
						receipt_base_software_type_row.setVisibility(View.VISIBLE);
						TextView receipt_base_software_type_value = (TextView)view.findViewById(R.id.receipt_base_software_type_value);
						TextView receipt_base_software_version_value = (TextView)view.findViewById(R.id.receipt_base_software_version_value);
						TextView receipt_base_software_env_value = (TextView)view.findViewById(R.id.receipt_base_software_env_value);
						TextView order_is_charge_value = (TextView)view.findViewById(R.id.order_is_charge_value);
						TextView order_charge_money_value = (TextView)view.findViewById(R.id.order_charge_money_value);
						if (ccEntity.getIs_charge() == 0)
						{
							order_is_charge_value.setText("��");
						}
						else
						{
							order_is_charge_value.setText("��");
						}
						
						order_charge_money_value.setText(ccEntity.getIs_charge_value());
						
						receipt_base_software_type_value.setText(FliterString(ccEntity.getSoftware_type()));
						receipt_base_software_version_value.setText(FliterString(ccEntity.getSoftware_version()));
						receipt_base_software_env_value.setText(FliterString(ccEntity.getSoftware_env_value()));					
					}
					QuestionListDB qlDB = new QuestionListDB(this);
					ArrayList<QuestionEntity> qlList = qlDB.getQuestionByOrderNum(order_num);
					LinearLayout q_a_content = (LinearLayout)view.findViewById(R.id.q_a_content);
					for (QuestionEntity qEntity : qlList)
					{
						View qa_view = inflater.inflate(R.layout.view_qa, null);
						TextView q_type_value = (TextView)qa_view.findViewById(R.id.q_type_value);
						TextView q_category_value = (TextView)qa_view.findViewById(R.id.q_category_value);
						TextView question_describe_value = (TextView)qa_view.findViewById(R.id.question_describe_value);
						TextView question_solve_value = (TextView)qa_view.findViewById(R.id.question_solve_value);
						q_type_value.setText(qEntity.getQ_type());
						q_category_value.setText(qEntity.getQ_category());
						question_describe_value.setText(qEntity.getQ_content());
						question_solve_value.setText(qEntity.getQ_answer());
						q_a_content.addView(qa_view);
					}
									
					CostValueDB cvDB = new CostValueDB(this);
					ArrayList<CostEntity> cList = cvDB.getCost(order_num);
					LinearLayout expense_content = (LinearLayout)view.findViewById(R.id.expense_content);
					if (cList.size() > 0)
					{					
						for (CostEntity cEntity : cList)
						{
							View e_view = inflater.inflate(R.layout.spinner_template, null);
							TextView tv = (TextView)e_view.findViewById(R.id.name);
							tv.setTextSize(14);
							tv.setText(cEntity.getName()+": "+cEntity.getValue());
							expense_content.addView(e_view);
						}
					}
					else
					{
						View e_view = inflater.inflate(R.layout.spinner_template, null);
						TextView tv = (TextView)e_view.findViewById(R.id.name);
						tv.setTextSize(16);
						tv.setText("�޷��ñ�����Ŀ");
						expense_content.addView(e_view);
					}
					FileManageDB fmDB = new FileManageDB(this); //��ȡ��Ƶ����Ƶ��Ϣ			
					
					ArrayList<FileEntity> list = fmDB.getAllDataByOrderNum(order_num);
					MediaAttributeEntity maEntity = null;
					
					image_list = new ArrayList<MediaAttributeEntity>();
					audio_list = new ArrayList<MediaAttributeEntity>();
					for (FileEntity entity : list)
					{
						maEntity = new MediaAttributeEntity();
						Bitmap audio_bm = BitmapFactory.decodeResource(this.getResources(), R.drawable.audio_bg);
						Bitmap image_bm = null;
						String path = path_prefix + order_num + "/";
						if (entity.getFiletype() == 0) // ͼƬ
						{
							path = path + entity.getFilename();
							BitmapFactory.Options options = new BitmapFactory.Options();
							options.inSampleSize = 2;
							image_bm = BitmapFactory.decodeFile(path, options);
							maEntity.setBitmap(image_bm);
							maEntity.setFilename(entity.getFilename());
							image_list.add(maEntity);
						}
						else if (entity.getFiletype() == 1) // ��Ƶ
						{
							maEntity.setBitmap(audio_bm);
							maEntity.setFilename(entity.getFilename());
							audio_list.add(maEntity);
						}
					}
									
					refreshImageGridView();

					refreshAudioGridView();
					
					view_image_gridview.setOnItemClickListener(new ImageItemClickListener());
					view_audio_gridview.setOnItemClickListener(new AudioItemClickListener());
					
					content_panel.addView(view);
				}
				else
				{
					if (print_btn != null)
					{
						print_btn.setVisibility(View.GONE);
					}
					
					FileManageDB fmDB = new FileManageDB(this); //��ȡ��Ƶ����Ƶ��Ϣ			
					
					ArrayList<FileEntity> list = fmDB.getAllDataByOrderNum(order_num);
					if (list.size() > 0)
					{
						View view = inflater.inflate(R.layout.audio_pic, null);
						view_image_gridview = (GridView)view.findViewById(R.id.view_image_gridview);
						view_audio_gridview = (GridView)view.findViewById(R.id.view_audio_gridview);
						
						MediaAttributeEntity maEntity = null;
						
						image_list = new ArrayList<MediaAttributeEntity>();
						audio_list = new ArrayList<MediaAttributeEntity>();
						for (FileEntity entity : list)
						{
							maEntity = new MediaAttributeEntity();
							Bitmap audio_bm = BitmapFactory.decodeResource(this.getResources(), R.drawable.audio_bg);
							Bitmap image_bm = null;
							String path = path_prefix + order_num + "/";
							if (entity.getFiletype() == 0) // ͼƬ
							{
								path = path + entity.getFilename();
								BitmapFactory.Options options = new BitmapFactory.Options();
								options.inSampleSize = 2;
								image_bm = BitmapFactory.decodeFile(path, options);
								maEntity.setBitmap(image_bm);
								maEntity.setFilename(entity.getFilename());
								image_list.add(maEntity);
							}
							else if (entity.getFiletype() == 1) // ��Ƶ
							{
								maEntity.setBitmap(audio_bm);
								maEntity.setFilename(entity.getFilename());
								audio_list.add(maEntity);
							}
						}
										
						refreshImageGridView();

						refreshAudioGridView();
						
						view_image_gridview.setOnItemClickListener(new ImageItemClickListener());
						view_audio_gridview.setOnItemClickListener(new AudioItemClickListener());
						content_panel.addView(view);
					}
					Toast.makeText(this, "����δ��д�ص���Ϣ,û�ص���Ϣ���أ�", Toast.LENGTH_LONG).show();
				}
			}			
		}		
	}
	
	//���ص绰�����ִ����ϸ��Ϣ
	public void loadingPhoneSolveInfo()
	{
		if (order_num!= null && !order_num.equals(""))
		{
			if (inflater != null)
			{
				QuestionListDB qlDB = new QuestionListDB(this);
				ArrayList<QuestionEntity> qlList = qlDB.getQuestionByOrderNum(order_num);
				for (QuestionEntity qEntity : qlList)
				{
					View qa_view = inflater.inflate(R.layout.view_qa, null);
					TextView q_type_value = (TextView)qa_view.findViewById(R.id.q_type_value);
					TextView q_category_value = (TextView)qa_view.findViewById(R.id.q_category_value);
					TextView question_describe_value = (TextView)qa_view.findViewById(R.id.question_describe_value);
					TextView question_solve_value = (TextView)qa_view.findViewById(R.id.question_solve_value);
					q_type_value.setText(qEntity.getQ_type());
					q_category_value.setText(qEntity.getQ_category());
					question_describe_value.setText(qEntity.getQ_content());
					question_solve_value.setText(qEntity.getQ_answer());
					content_panel.addView(qa_view);
				}
			}
		}
	}
	
	//���ظ��ɻ�ִ����ϸ��Ϣ
	public void loadingChangeSolveInfo()
	{
		if (order_num!= null && !order_num.equals(""))
		{
			if (inflater != null)
			{
				View view = inflater.inflate(R.layout.view_change_solve, null);
				TextView change_course_text = (TextView)view.findViewById(R.id.change_course_text);				
				OrderChangeDB ocDB = new OrderChangeDB(this);
				OrderChangeEntity ocEntity = ocDB.selectById(order_num);
				change_course_text.setText(ocEntity.getChange_reason());
				content_panel.addView(view);
			}
		}
	}
	
	public String CurrentOrderState(int finish_type)
	{
		if (finish_type == FinalVariables.ORDER_FINISH_TYPE_SPOT_SOLVE) //�ֳ����
		{
			return this.getResources().getString(R.string.spot_finish);
		}
		else if (finish_type == FinalVariables.ORDER_FINISH_TYPE_PHONE_SOLVE) //�绰���
		{
			return this.getResources().getString(R.string.phone_finish);
		}
		else if (finish_type == FinalVariables.ORDER_FINISH_TYPE_CHANGE_SOLVE) //����
		{
			return this.getResources().getString(R.string.change_finish);
		}
		else if (finish_type == FinalVariables.ORDER_FINISH_TYPE_VISIT_SOLVE)
		{
			return this.getResources().getString(R.string.visit_finish);
		}
		return "";
	}
	
	private void refreshImageGridView()
	{
		mgAdapter1 = new MediaGridAdapter(this, image_list);
		mgAdapter1.notifyDataSetChanged();
		view_image_gridview.setAdapter(mgAdapter1);
	}

	private void refreshAudioGridView()
	{
		mgAdapter2 = new MediaGridAdapter(this, audio_list);
		mgAdapter2.notifyDataSetChanged();
		view_audio_gridview.setAdapter(mgAdapter2);
	}
	
	class ImageItemClickListener implements OnItemClickListener
	{
		public void onItemClick(AdapterView<?> arg0,// The AdapterView where the
													// click happened
				View arg1,// The view within the AdapterView that was clicked
				int arg2,// The position of the view in the adapter
				long arg3// The row id of the item that was clicked
		)
		{
			MediaAttributeEntity maEntity = null;
			ImageView imageView = (ImageView) arg1.findViewById(R.id.media_item_img);
			maEntity = (MediaAttributeEntity) imageView.getTag();
			final String path = path_prefix + order_num + "/" + maEntity.getFilename();
			final String[] mItems = { "�Ŵ���ʾ"};
			AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailViewActivity.this);
			builder.setTitle("��ѡ����Ҫ�Ĳ���");
			builder.setItems(mItems, new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int which)
				{

					// ����󵯳�����ѡ���˵ڼ���
					if (which == 0) // �Ŵ�
					{
						Intent intent = new Intent(OrderDetailViewActivity.this, MaxImageActivity.class);
						intent.putExtra("path", path);
						startActivity(intent);
						dialog.dismiss();
					}
				}
			});
			builder.create().show();
		}

	}

	class AudioItemClickListener implements OnItemClickListener
	{
		public void onItemClick(AdapterView<?> arg0,// The AdapterView where the
													// click happened
				View arg1,// The view within the AdapterView that was clicked
				int arg2,// The position of the view in the adapter
				long arg3// The row id of the item that was clicked
		)
		{
			ImageView imageView = (ImageView) arg1.findViewById(R.id.media_item_img);
			MediaAttributeEntity maEntity = null;
			maEntity = (MediaAttributeEntity) imageView.getTag();
			final String path = path_prefix + order_num + "/" + maEntity.getFilename();
			final String[] mItems = { "����"};
			AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailViewActivity.this);
			builder.setTitle("��ѡ����Ҫ�Ĳ���");
			builder.setItems(mItems, new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int which)
				{
					// ����󵯳�����ѡ���˵ڼ���
					if (which == 0) // ����
					{
						dialog.dismiss();
						Intent intent = new Intent(OrderDetailViewActivity.this, SoundRecorderActivity.class);
						intent.putExtra("path", path);
						startActivity(intent);
					}		
				}
			});
			builder.create().show();
		}
	}
	
	public String stateTransfText(int state)
	{
		String stateText = "";
		if (state == FinalVariables.ORDER_STATE_NOT_READ)
		{
			stateText = "���յ��¹���";
		}
		else if (state == FinalVariables.ORDER_STATE_IS_READ)
		{
			stateText = "���Ķ�����";
		}
		else if (state == FinalVariables.ORDER_STATE_APPLY_CHANGE)
		{
			stateText = "�������";
		}
		else if (state == FinalVariables.ORDER_STATE_ARRIVE_TARGET)
		{
			stateText = "�����ֳ�";
		}
		else if (state == FinalVariables.ORDER_STATE_ACCEPT_TASK)
		{
			stateText = "��������";
		}
		else if (state == FinalVariables.ORDER_STATE_PHONE_SOLVE)
		{
			stateText = "�绰���";
		}
		else if (state == FinalVariables.ORDER_STATE_ORDER_COMPLISH)
		{
			stateText = "�������";
		}
		return stateText;
	}
	
	public String FliterString(String str)
	{
		if (str == null || str.equals("") || str.equals("null"))
		{
			return "";
		}
		else
		{
			return str; // �ͻ��ֻ�
		}
	}
}
