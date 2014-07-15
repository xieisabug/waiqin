package com.sealion.serviceassistant.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**   
* @Title: DbHelper.java 
* @Package com.sealion.serviceassistant.db 
* @Description: ���ݿ���������ĳ����ࡣ������ݿ��ĳ�ʼ����
* @author jack.lee titans.lee@gmail.com   
* @date 2013-2-17 ����9:22:03 
* @version V1.0
* Copyright: Copyright (c)2012
* Company: �����ж�ͨ�ż������޹�˾
*/
public abstract class DbHelper extends SQLiteOpenHelper
{
	private static final String TAG = "com.sealion.servicemanage.db.DbHelper";
	protected final static int PAGE_SIZE = 10;
	protected static final int db_version = 3;
	private static final String DB_NAME = "SERVICE_MANAGE_DB";
	
	protected final static String ORDER_LIST_TABLE_NAME = "order_list"; //�����б�
	protected final static String NOTICE_TABLE_NAME = "notice_list"; //֪ͨ�б�
	protected final static String COMMON_ORDER_COMPLISH_TABLE_NAME = "coc_list"; //���Ź�����ִ����
	protected final static String FILE_MANAGE_TABLE_NAME = "file_list";//�����ļ�������ͼƬ����Ƶ��
	protected final static String ORDER_CHANGE_TABLE_NAME = "oc_list";//������ִ����
	protected final static String COST_VALUE_TABLE_NAME = "cost_value_list"; //���ñ�����
	protected final static String PHONE_COMPLISH_TABLE_NAME = "pc_list"; //�绰��ɻ�ִ��
	protected final static String LOCATION_TABLE_NAME = "lo_list"; //�����Աλ����Ϣ
	protected final static String ORDER_STEP_TABLE_NAME = "order_step_list"; //��¼������������
	protected final static String TAX_BUREAU_TABLE_NAME = "tax_list"; //˰����ر�
	protected final static String Q_TYPE_TABLE_NAME = "q_type_list"; //��������
	protected final static String Q_CATEGORY_TABLE_NAME = "q_category_list"; //�������
    protected final static String PRODUCT_TABLE_NAME = "product_list";//��Ʒ��Ϣ
	protected final static String COST_TABLE_NAME = "cost_list"; //�������
	protected final static String QUESTION_TABLE_NAME = "question_list"; //�����
	//���� table sql
	private String order_list_sql = "Create table IF NOT EXISTS " + ORDER_LIST_TABLE_NAME + 
			" (id integer primary key autoincrement, " +
			" workCardId long, " +	//�ɹ���ID int��(����)
			" orderToken text,"+ //���ݺ�
			" serviceDate text, " +	//��������(��ʽ:yyyyMMddHHmmss) (����)
			" workOrderType int, " +	//�ɹ����,int�� 1��ֱ���ɹ���2���ط��ɹ�(����)
			" customerId long, " +	//�ͻ�ID,int��(����)
			" linkPerson int, " +	//�ͻ���ϵ�� (����)
			" customerName int, " +	//�ͻ���˾����(����)
			" taxCode text, " +	//˰��(����)
			" departmentId int,  " +	//��������ID,int��(����)
			" departmentName text, " +	//������������(����)
			" revenueId int, " +		//����˰��־�ID,int��(����)
			" revenueName text, " +	//����˰��־�����(����)
			" customerAddr text, " +	//���ŵ�ַ(����)
			" customerMobile text, " +	//�ͻ��ֻ�(����)
			" customerTel text, " + 	//�ͻ���ϵ�绰(����)
			" customerCounty text, " +	//�ͻ�����������(����)
			" customerLatitude text, " +	//�ͻ���ַ����γ��(����)
			" customerLongitude text, " +	//�ͻ���ַ���ھ���(����)
			" fieldWorkerId int, " +	//������ԱID,int��(����)
			" fieldWorkerName text, " + //������Ա����(����)
			" workOrderDescription text, " + //�ɹ�˵��(����)
			" expectArriveTime text, " + //������ԱԤ�Ƶ���ʱ��(��ʽ:yyyyMMddHHmmss)(����)
			" productName text, " + //������Ʒ����,�طõ��� int��(����)	
			" chargeType int, " +//�Ƿ��շ�(1:��,2:��) int(����)
			" chargeMoney text, " +//�շѽ��(����)
			" urgency int, " + ////�����Ľ����̶�(0:һ��,1:����),int(����)
			" city text, " + //�ͻ����ڳ���(�������ƣ����糤ɳ�У���̶�У�����������)(����)
			" order_sign int, " +//����״̬��ǣ�0��δ�Ķ���1:�Ѿ��Ķ���2:��������
										// 3��������ɣ�4����������ͨ����5����������δͨ����
										// 6�������ֳ���7:�绰���  10��������ɣ�
			" upload_sign int, " + //0��δ�ϴ�����1�������ϴ����
			" finish_type int, " +//�����������  Ĭ��Ϊ0�� 1���ֳ���� ��2���绰�����3������
			" is_task int, " +//�Ƿ�ӵ������Ҫ�ϴ� 0:û�У�1����
			" receiveOrderTime text, " + //��������ʱ��			
			" optimal_path text," +//����·��
			" visit_type text," +//�ط�����
			" product_status text," + //��Ʒ״̬
			" handle_result text);"; //������
	
	//֪ͨ table sql
	private String notice_list_sql = "Create table IF NOT EXISTS " + NOTICE_TABLE_NAME + 
			" (id integer primary key autoincrement, " +
			" title text, " + //֪ͨ�������	
			" content text, " + //����
			" sign int," + //�Ƿ��Ķ���־ 0δ�Ķ���1�Ķ�
			" publish_time text);"; //����ʱ��
	
	//���Ź�����ִ��sql
	private String common_order_complish_sql = "Create table IF NOT EXISTS " + COMMON_ORDER_COMPLISH_TABLE_NAME + 
			" (id integer primary key autoincrement, " +
			" order_num text, " + //������
			" customer_name text, " + //�ͻ�����
			" customer_tax text, " + //˰��
			" tax_officer text, " + //˰��־�
			" service_address text, " + //���ŵ�ַ
			" contact_name text, " + //��ϵ��
			" customer_tel text, " + //�ͻ��绰
			" customer_mobile text, " + //�ͻ��ֻ�
			" service_time text, " + //����ʱ��
			" number_value text, " + //���ݺ�
			" customer_sign text, " + //�ͻ�ǩ��
			" customer_charge text, " + //���ù���
			" customer_remark text, " + //��ע
			" is_send_to_server int," +//�Ƿ����������޸���Ϣ
			" customer_solve int, " + //����ʽ
			" software_type text, " + //����ͺż����
			" software_version text, " + //����汾��
			" software_env_value text,"+ // ʹ�û���
			" is_charge int,"+ //�Ƿ��շ� 0:���� 1����
			" is_charge_value text,"+ //�շѽ��
			" tec_name text,"+ //��������ʦ
			" service_evaluate int,"+ //�������� 0�������⣬1�����⣬2��������
			" product_evaluate int,"+ //��Ʒ����0�������⣬1�����⣬2��������
			" visit_type text, " + //�ط�����
			" visit_product_case text, " + //��Ʒ�������
			" visit_dispose_result text," +//������
			" insert_time text); ";  //д��ʱ��
	
	//���������ļ���¼ table sql
	private String file_manage_sql = "Create table IF NOT EXISTS " + FILE_MANAGE_TABLE_NAME + 
			" (id integer primary key autoincrement, " +
			" order_num text, " + //������
			" filename text," + //�ļ���
			" filestate int," + //�ļ�״̬��0��δ�ϴ���ϣ�1���ϴ���ϣ�		
			" filetype int," +//�ļ����� 0��ͼƬ��1��Ƶ
			" insert_time text);" ;  //д��ʱ��
	
	//������ɱ�
	private String order_change_sql = "Create table IF NOT EXISTS " + ORDER_CHANGE_TABLE_NAME + 
			" (id integer primary key autoincrement, " +
			" order_num text, " + //������
			" change_reason text," + //����ԭ��
			" change_time text);" ; //����ʱ��
	
	
	//�绰��ɻ�ִ����
	private String phone_complish_sql = "Create table IF NOT EXISTS " + PHONE_COMPLISH_TABLE_NAME + 
			" (id integer primary key autoincrement, " +
			" order_num text, " + //������
			" q_describe text," + //��������
			" q_solve int," + //������
			" solve_time text," + //���ʱ��
			" is_create_new_order int);" ; //�Ƿ������¹�����0:��1����
	
	//�û���λ��Ϣ��
	private String lo_list_sql = "Create table  IF NOT EXISTS " + LOCATION_TABLE_NAME + " (id integer primary key autoincrement, " +
			" userid text, " + //�û�ID
			" lititude text, " + //γ��
			" longitude text, " + //����
			" address text, " + //��ַ
			" areacode text, " + //��������
			" time text);"; //��λʱ��
	
	//����״̬���̼�¼
	private String order_step_sql = "Create table IF NOT EXISTS " + ORDER_STEP_TABLE_NAME +" (id integer primary key autoincrement," +
			" order_num text," + //������
			" order_state int," + //����״̬
			" oper_time text);"; //����ʱ��
	//��������
	private String q_type_sql ="Create table IF NOT EXISTS " + Q_TYPE_TABLE_NAME +" (id integer primary key autoincrement," +
			" type_id int," + //����ID
			" q_category_id int," + //���ID
			" name text);"; //��������
	
	//�������
	private String q_category_sql = "Create table IF NOT EXISTS  " + Q_CATEGORY_TABLE_NAME +" (id integer primary key autoincrement," +
			" category_id text," + //���ID
			" name text," +//����
			" productId text," +//��ƷID
			" productName text);";//��Ʒ����

    //��Ʒ��Ϣ
    private String product_sql = "Create table IF NOT EXISTS  " + PRODUCT_TABLE_NAME +" (id integer primary key autoincrement," +
            " productId int," +//��ƷID
            " productName text);";//��Ʒ����
	
	//˰�����
	private String tax_bureau_sql = "Create table IF NOT EXISTS " + TAX_BUREAU_TABLE_NAME +" (id integer primary key autoincrement," +
			" bureau_id int," + //˰���ID
			" bureau_name text," + //˰���������
			" city text);"; //�������д���
	
	//���ñ�
	private String cost_sql = "Create table IF NOT EXISTS " + COST_TABLE_NAME +" (id integer primary key autoincrement," +
			" cost_id int," + //����ID
			" name text);"; //��������
	
	//�����
	private String question_sql = "Create table IF NOT EXISTS " + QUESTION_TABLE_NAME + "(id integer primary key autoincrement, " +
			" order_num text, " +
			" q_id int," +  //����ID
			" q_content text," + //��������
			" q_type_id int," + //��������ID
			" q_category_id int," + //�������ID
			" q_type text,"+ //������������
			" q_category text,"+ //�����������
			" q_answer text," +//������
			" insert_time text," +//д��ʱ��
			" productId text," + //��ƷId
			" productName text);"; //��Ʒ����
	
	//���ñ�����
	private String cost_value_sql = "Create table IF NOT EXISTS " + COST_VALUE_TABLE_NAME +" (id integer primary key autoincrement," +
			" order_num text,"+
			" cost_id int,"+
			" cost_name text," + //��������
			" cost_value text," +//����ֵ
			" insert_time text);"; //д��ʱ��
	
	public DbHelper(Context context)
	{		
		super(context, DB_NAME, null, db_version);
		Log.d(TAG, "��ʼ�����ݿ�.........");
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(order_list_sql);
		db.execSQL(notice_list_sql);
		db.execSQL(common_order_complish_sql);
		db.execSQL(file_manage_sql);
		db.execSQL(order_change_sql);
		db.execSQL(phone_complish_sql);
		db.execSQL(lo_list_sql);
		db.execSQL(order_step_sql);
		db.execSQL(q_type_sql);
		db.execSQL(q_category_sql);
        db.execSQL(product_sql);
        db.execSQL(cost_sql);
		db.execSQL(question_sql);
		db.execSQL(cost_value_sql);
		db.execSQL(tax_bureau_sql);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
//		db.execSQL("DROP TABLE IF EXISTS " + ORDER_LIST_TABLE_NAME);
//		db.execSQL("DROP TABLE IF EXISTS " + NOTICE_TABLE_NAME);
//		db.execSQL("DROP TABLE IF EXISTS " + COMMON_ORDER_COMPLISH_TABLE_NAME);
//		db.execSQL("DROP TABLE IF EXISTS " + FILE_MANAGE_TABLE_NAME);
//		db.execSQL("DROP TABLE IF EXISTS " + ORDER_CHANGE_TABLE_NAME);
//		db.execSQL("DROP TABLE IF EXISTS " + PHONE_COMPLISH_TABLE_NAME);
		//onCreate(db);
	}
	
}
