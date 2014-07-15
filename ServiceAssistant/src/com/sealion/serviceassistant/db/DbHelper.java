package com.sealion.serviceassistant.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**   
* @Title: DbHelper.java 
* @Package com.sealion.serviceassistant.db 
* @Description: 数据库基本操作的抽象类。完成数据库表的初始化。
* @author jack.lee titans.lee@gmail.com   
* @date 2013-2-17 上午9:22:03 
* @version V1.0
* Copyright: Copyright (c)2012
* Company: 湖南中恩通信技术有限公司
*/
public abstract class DbHelper extends SQLiteOpenHelper
{
	private static final String TAG = "com.sealion.servicemanage.db.DbHelper";
	protected final static int PAGE_SIZE = 10;
	protected static final int db_version = 3;
	private static final String DB_NAME = "SERVICE_MANAGE_DB";
	
	protected final static String ORDER_LIST_TABLE_NAME = "order_list"; //工单列表
	protected final static String NOTICE_TABLE_NAME = "notice_list"; //通知列表
	protected final static String COMMON_ORDER_COMPLISH_TABLE_NAME = "coc_list"; //上门工单回执单表
	protected final static String FILE_MANAGE_TABLE_NAME = "file_list";//工单文件表（包括图片和音频）
	protected final static String ORDER_CHANGE_TABLE_NAME = "oc_list";//工单回执单表
	protected final static String COST_VALUE_TABLE_NAME = "cost_value_list"; //费用报销表
	protected final static String PHONE_COMPLISH_TABLE_NAME = "pc_list"; //电话完成回执单
	protected final static String LOCATION_TABLE_NAME = "lo_list"; //外服人员位置信息
	protected final static String ORDER_STEP_TABLE_NAME = "order_step_list"; //记录工单处理流程
	protected final static String TAX_BUREAU_TABLE_NAME = "tax_list"; //税务机关表
	protected final static String Q_TYPE_TABLE_NAME = "q_type_list"; //问题类型
	protected final static String Q_CATEGORY_TABLE_NAME = "q_category_list"; //问题类别
    protected final static String PRODUCT_TABLE_NAME = "product_list";//产品信息
	protected final static String COST_TABLE_NAME = "cost_list"; //费用类别
	protected final static String QUESTION_TABLE_NAME = "question_list"; //问题表
	//工单 table sql
	private String order_list_sql = "Create table IF NOT EXISTS " + ORDER_LIST_TABLE_NAME + 
			" (id integer primary key autoincrement, " +
			" workCardId long, " +	//派工单ID int型(必填)
			" orderToken text,"+ //单据号
			" serviceDate text, " +	//服务日期(格式:yyyyMMddHHmmss) (必填)
			" workOrderType int, " +	//派工类别,int型 1：直接派工；2：回访派工(必填)
			" customerId long, " +	//客户ID,int型(必填)
			" linkPerson int, " +	//客户联系人 (必填)
			" customerName int, " +	//客户公司名称(必填)
			" taxCode text, " +	//税号(可填)
			" departmentId int,  " +	//所属部门ID,int型(必填)
			" departmentName text, " +	//所属部门名称(必填)
			" revenueId int, " +		//所属税务分局ID,int型(必填)
			" revenueName text, " +	//所属税务分局名称(必填)
			" customerAddr text, " +	//上门地址(必填)
			" customerMobile text, " +	//客户手机(必填)
			" customerTel text, " + 	//客户联系电话(必填)
			" customerCounty text, " +	//客户所在区、县(可填)
			" customerLatitude text, " +	//客户地址所在纬度(可填)
			" customerLongitude text, " +	//客户地址所在经度(可填)
			" fieldWorkerId int, " +	//服务人员ID,int型(必填)
			" fieldWorkerName text, " + //服务人员姓名(必填)
			" workOrderDescription text, " + //派工说明(可填)
			" expectArriveTime text, " + //服务人员预计到达时间(格式:yyyyMMddHHmmss)(可填)
			" productName text, " + //所属产品名称,回访单用 int型(必填)	
			" chargeType int, " +//是否收费(1:是,2:否) int(必填)
			" chargeMoney text, " +//收费金额(可填)
			" urgency int, " + ////工单的紧急程度(0:一般,1:紧急),int(必填)
			" city text, " + //客户所在城市(城市名称，比如长沙市，湘潭市，湘西自治州)(必填)
			" order_sign int, " +//工单状态标记（0：未阅读，1:已经阅读，2:接受任务，
										// 3：申请改派，4：改派审批通过，5：改派审批未通过，
										// 6：到达现场，7:电话解决  10：工单完成）
			" upload_sign int, " + //0：未上传任务，1：任务上传完成
			" finish_type int, " +//工单完成类型  默认为0， 1：现场解决 ，2：电话解决，3，改派
			" is_task int, " +//是否拥有任务要上传 0:没有，1：有
			" receiveOrderTime text, " + //工单接收时间			
			" optimal_path text," +//最优路径
			" visit_type text," +//回访类型
			" product_status text," + //产品状态
			" handle_result text);"; //处理结果
	
	//通知 table sql
	private String notice_list_sql = "Create table IF NOT EXISTS " + NOTICE_TABLE_NAME + 
			" (id integer primary key autoincrement, " +
			" title text, " + //通知公告标题	
			" content text, " + //内容
			" sign int," + //是否阅读标志 0未阅读，1阅读
			" publish_time text);"; //发布时间
	
	//上门工单回执单sql
	private String common_order_complish_sql = "Create table IF NOT EXISTS " + COMMON_ORDER_COMPLISH_TABLE_NAME + 
			" (id integer primary key autoincrement, " +
			" order_num text, " + //工单号
			" customer_name text, " + //客户名称
			" customer_tax text, " + //税号
			" tax_officer text, " + //税务分局
			" service_address text, " + //上门地址
			" contact_name text, " + //联系人
			" customer_tel text, " + //客户电话
			" customer_mobile text, " + //客户手机
			" service_time text, " + //到达时间
			" number_value text, " + //单据号
			" customer_sign text, " + //客户签名
			" customer_charge text, " + //费用过程
			" customer_remark text, " + //备注
			" is_send_to_server int," +//是否发往服务器修改信息
			" customer_solve int, " + //服务方式
			" software_type text, " + //软件型号及编号
			" software_version text, " + //软件版本号
			" software_env_value text,"+ // 使用环境
			" is_charge int,"+ //是否收费 0:不收 1：收
			" is_charge_value text,"+ //收费金额
			" tec_name text,"+ //技术工程师
			" service_evaluate int,"+ //服务评价 0：很满意，1：满意，2：不满意
			" product_evaluate int,"+ //产品评价0：很满意，1：满意，2：不满意
			" visit_type text, " + //回访类型
			" visit_product_case text, " + //产品运行情况
			" visit_dispose_result text," +//处理结果
			" insert_time text); ";  //写入时间
	
	//工单服务文件记录 table sql
	private String file_manage_sql = "Create table IF NOT EXISTS " + FILE_MANAGE_TABLE_NAME + 
			" (id integer primary key autoincrement, " +
			" order_num text, " + //工单号
			" filename text," + //文件名
			" filestate int," + //文件状态（0：未上传完毕，1：上传完毕）		
			" filetype int," +//文件类型 0：图片，1音频
			" insert_time text);" ;  //写入时间
	
	//申请改派表
	private String order_change_sql = "Create table IF NOT EXISTS " + ORDER_CHANGE_TABLE_NAME + 
			" (id integer primary key autoincrement, " +
			" order_num text, " + //工单号
			" change_reason text," + //改派原因
			" change_time text);" ; //申请时间
	
	
	//电话完成回执单表
	private String phone_complish_sql = "Create table IF NOT EXISTS " + PHONE_COMPLISH_TABLE_NAME + 
			" (id integer primary key autoincrement, " +
			" order_num text, " + //工单号
			" q_describe text," + //问题描述
			" q_solve int," + //问题解决
			" solve_time text," + //解决时间
			" is_create_new_order int);" ; //是否生成新工单，0:否，1：是
	
	//用户定位信息表
	private String lo_list_sql = "Create table  IF NOT EXISTS " + LOCATION_TABLE_NAME + " (id integer primary key autoincrement, " +
			" userid text, " + //用户ID
			" lititude text, " + //纬度
			" longitude text, " + //经度
			" address text, " + //地址
			" areacode text, " + //地区代码
			" time text);"; //定位时间
	
	//工单状态过程记录
	private String order_step_sql = "Create table IF NOT EXISTS " + ORDER_STEP_TABLE_NAME +" (id integer primary key autoincrement," +
			" order_num text," + //工单号
			" order_state int," + //工单状态
			" oper_time text);"; //操作时间
	//问题类型
	private String q_type_sql ="Create table IF NOT EXISTS " + Q_TYPE_TABLE_NAME +" (id integer primary key autoincrement," +
			" type_id int," + //类型ID
			" q_category_id int," + //类别ID
			" name text);"; //类型名称
	
	//问题类别
	private String q_category_sql = "Create table IF NOT EXISTS  " + Q_CATEGORY_TABLE_NAME +" (id integer primary key autoincrement," +
			" category_id text," + //类别ID
			" name text," +//名称
			" productId text," +//产品ID
			" productName text);";//产品名称

    //产品信息
    private String product_sql = "Create table IF NOT EXISTS  " + PRODUCT_TABLE_NAME +" (id integer primary key autoincrement," +
            " productId int," +//产品ID
            " productName text);";//产品名称
	
	//税务机关
	private String tax_bureau_sql = "Create table IF NOT EXISTS " + TAX_BUREAU_TABLE_NAME +" (id integer primary key autoincrement," +
			" bureau_id int," + //税务局ID
			" bureau_name text," + //税务机关名称
			" city text);"; //所属城市代码
	
	//费用表
	private String cost_sql = "Create table IF NOT EXISTS " + COST_TABLE_NAME +" (id integer primary key autoincrement," +
			" cost_id int," + //费用ID
			" name text);"; //费用名称
	
	//问题表
	private String question_sql = "Create table IF NOT EXISTS " + QUESTION_TABLE_NAME + "(id integer primary key autoincrement, " +
			" order_num text, " +
			" q_id int," +  //问题ID
			" q_content text," + //问题内容
			" q_type_id int," + //问题类型ID
			" q_category_id int," + //问题类别ID
			" q_type text,"+ //问题类型名称
			" q_category text,"+ //问题类别名称
			" q_answer text," +//问题解答
			" insert_time text," +//写入时间
			" productId text," + //产品Id
			" productName text);"; //产品名称
	
	//费用报销表
	private String cost_value_sql = "Create table IF NOT EXISTS " + COST_VALUE_TABLE_NAME +" (id integer primary key autoincrement," +
			" order_num text,"+
			" cost_id int,"+
			" cost_name text," + //费用名称
			" cost_value text," +//费用值
			" insert_time text);"; //写入时间
	
	public DbHelper(Context context)
	{		
		super(context, DB_NAME, null, db_version);
		Log.d(TAG, "初始化数据库.........");
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
