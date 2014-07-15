package com.sealion.serviceassistant;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.sealion.serviceassistant.db.CommonOrderComplishDB;
import com.sealion.serviceassistant.db.CostDB;
import com.sealion.serviceassistant.db.CostValueDB;
import com.sealion.serviceassistant.db.OrderListDB;
import com.sealion.serviceassistant.entity.CommonOrderComplishEntity;
import com.sealion.serviceassistant.entity.CostEntity;
import com.sealion.serviceassistant.entity.NetBackDataEntity;
import com.sealion.serviceassistant.entity.OrderEntity;
import com.sealion.serviceassistant.exception.NetAccessException;
import com.sealion.serviceassistant.net.HttpRestAchieve;
import com.sealion.serviceassistant.superactivity.OrderActivity;
import com.sealion.serviceassistant.tools.DateTools;
import com.sealion.serviceassistant.tools.EditTextLengthConstants;
import com.sealion.serviceassistant.tools.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 现场解决回执单基本信息填写录入.
 * 可修改客户基本信息
 */
public class ReceiptBaseInfoActivity extends OrderActivity {
    public final String TAG = getClass().getSimpleName();

    private TextView current_state_value = null; // 当前状态

    private EditText receipt_base_customer_name_value = null; // 客户名称
    private EditText receipt_base_customer_tax_value = null; // 税号
    private EditText receipt_base_tax_officer_value = null; // 税务分局
    private EditText receipt_base_service_address_value = null; // 上门地址
    private EditText receipt_base_contact_name_value = null; // 联系人
    private EditText receipt_base_customer_tel_value = null; // 客户电话
    private EditText receipt_base_customer_mobile_value = null; // 客户手机
    private EditText receipt_base_service_time_value = null; // 到达时间
    private EditText receipt_base_number_value = null; // 单据号
    private EditText receipt_base_customer_sign_value = null; // 客户签名
    private EditText receipt_base_customer_charge_value = null; // 费用过程
    private EditText receipt_base_customer_remark_value = null; // 备注
    private EditText fee_value = null; //费用值
    private EditText receipt_base_software_type_value = null; // 软件型号及编号
    private EditText receipt_base_software_version_value = null; // 软件版本号
    private EditText receipt_base_software_env_value = null; // 使用环境
    private EditText receipt_base_tec_name_value;//技术工程师
    private Spinner receipt_type_value = null; // 回访类型
    private EditText product_case_value = null; // 产品运行情况
    private EditText dispose_result_value = null; // 处理结果

    private TextView top_bar_title = null;
    private ImageView back_image = null;

    private LinearLayout cost_layout = null;
    private CheckBox is_upload_base_info = null;
    private OrderListDB olDB = null;
    private int id;
    private int workOrderType;

    //服务方式
    private RadioButton serviceRadio1; //上门
    private RadioButton serviceRadio2; //用户送修

    //是否收费
    private RadioButton feeRadio1;
    private RadioButton feeRadio2;

    // 服务的总体评价
    private RadioButton service_satisfied1; // 很满意
    private RadioButton service_satisfied2; // 满意
    private RadioButton service_satisfied3; // 不满意

    //产品的总体评价
    private RadioButton product_satisfied1; // 很满意
    private RadioButton product_satisfied2; // 满意
    private RadioButton product_satisfied3; // 不满意

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.receipt_base_info);

        Bundle bundle = this.getIntent().getExtras();
        order_num = bundle.getString("order_num");
        workOrderType = bundle.getInt("workOrderType");
        id = bundle.getInt("id");
        olDB = new OrderListDB(this);
        OrderEntity oEntity = olDB.SelectById(id);

        top_bar_title = (TextView) this.findViewById(R.id.top_bar_title);
        top_bar_title.setText(this.getResources().getString(R.string.receipt_base_top_title));
        current_state_value = (TextView) this.findViewById(R.id.current_state_value);
        current_state_value.setText(this.getResources().getString(R.string.spot_solve_back_paper));
        back_image = (ImageView) this.findViewById(R.id.back_image);
        back_image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 客户名称
        receipt_base_customer_name_value = (EditText) this.findViewById(R.id.receipt_base_customer_name_value);
        receipt_base_customer_name_value.setText(oEntity.getCustomerName());
        // 税号
        receipt_base_customer_tax_value = (EditText) this.findViewById(R.id.receipt_base_customer_tax_value);
        receipt_base_customer_tax_value.setText(oEntity.getTaxCode());
        // 税务分局
        receipt_base_tax_officer_value = (EditText) this.findViewById(R.id.receipt_base_tax_officer_value);
        receipt_base_tax_officer_value.setText(oEntity.getRevenueName());
        // 到达时间
        receipt_base_service_time_value = (EditText) this.findViewById(R.id.receipt_base_service_time_value);
        receipt_base_service_time_value.setText(DateTools.getFormatDateAndTime());
        // 单据号
        receipt_base_number_value = (EditText) this.findViewById(R.id.receipt_base_number_value);
        receipt_base_number_value.setText(oEntity.getOrderToken());
        // 硬件型号及编号
        receipt_base_software_type_value = (EditText) this.findViewById(R.id.receipt_base_software_type_value);

        // 软件版本号
        receipt_base_software_version_value = (EditText) this.findViewById(R.id.receipt_base_software_version_value);

        // 使用环境
        receipt_base_software_env_value = (EditText) this.findViewById(R.id.receipt_base_software_env_value);

        // 上门地址
        receipt_base_service_address_value = (EditText) this.findViewById(R.id.receipt_base_service_address_value);
        receipt_base_service_address_value.setText(oEntity.getCustomerAddr());

        // 联系人
        receipt_base_contact_name_value = (EditText) this.findViewById(R.id.receipt_base_contact_name_value);
        receipt_base_contact_name_value.setText(oEntity.getLinkPerson());
        // 客户电话
        receipt_base_customer_tel_value = (EditText) this.findViewById(R.id.receipt_base_customer_tel_value);
        receipt_base_customer_tel_value.setText(FliterString(oEntity.getCustomerTel()));
        // 客户手机
        receipt_base_customer_mobile_value = (EditText) this.findViewById(R.id.receipt_base_customer_mobile_value);
        receipt_base_customer_mobile_value.setText(FliterString(oEntity.getCustomerMobile()));
        // 客户签名
        receipt_base_customer_sign_value = (EditText) this.findViewById(R.id.receipt_base_customer_sign_value);

        // 费用过程
        receipt_base_customer_charge_value = (EditText) this.findViewById(R.id.receipt_base_customer_charge_value);
        StringUtils.lengthFilter(ReceiptBaseInfoActivity.this, receipt_base_customer_charge_value,
                EditTextLengthConstants.FEE_PROCESS_LENGTH, "输入费用过程不能超过" + EditTextLengthConstants.FEE_PROCESS_LENGTH + "个字");
        // 备注
        receipt_base_customer_remark_value = (EditText) this.findViewById(R.id.receipt_base_customer_remark_value);
        StringUtils.lengthFilter(ReceiptBaseInfoActivity.this, receipt_base_customer_remark_value,
                EditTextLengthConstants.REMARK_LENGTH, "输入备注不能超过" + EditTextLengthConstants.REMARK_LENGTH + "个字");

        is_upload_base_info = (CheckBox) this.findViewById(R.id.is_upload_base_info);

        cost_layout = (LinearLayout) this.findViewById(R.id.cost_layout);

        CostDB cDB = new CostDB(this);
        ArrayList<CostEntity> costList = cDB.getCost();
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        for (CostEntity cEntity : costList) {
            View view = inflater.inflate(R.layout.cost_content_layout, null);
            TextView tv = (TextView) view.findViewById(R.id.cost_text);
            tv.setText(cEntity.getName());
            tv.setTag(cEntity.getCost_id());
            cost_layout.addView(view);
        }

        receipt_base_tec_name_value = (EditText) this.findViewById(R.id.receipt_base_tec_name_value);
        receipt_base_tec_name_value.setText(sp.getString("FULLNAME", ""));

        if (workOrderType == 2) {
            LinearLayout maintance_panel = (LinearLayout) this.findViewById(R.id.maintance_panel);
            maintance_panel.setVisibility(View.GONE);
//			LinearLayout tec_name_layout  = (LinearLayout)this.findViewById(R.id.tec_name_layout);
//			tec_name_layout.setVisibility(View.VISIBLE);
            LinearLayout receipt_visit_order = (LinearLayout) this.findViewById(R.id.receipt_visit_order);
            receipt_visit_order.setVisibility(View.VISIBLE);
            receipt_type_value = (Spinner) this.findViewById(R.id.receipt_type_value);
//            receipt_type_value.setText(oEntity.getVisit_type());//不需要设置，需要外勤人员填写
            receipt_type_value.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, new String[]{"上门","电话"}));

            product_case_value = (EditText) this.findViewById(R.id.product_case_value);
            product_case_value.setText(FliterString(oEntity.getProduct_status()));
            StringUtils.lengthFilter(ReceiptBaseInfoActivity.this, product_case_value,
                    EditTextLengthConstants.VISIT_PRODUCT_LENGTH, "输入产品运行情况不能超过" + EditTextLengthConstants.VISIT_PRODUCT_LENGTH + "个字");
            dispose_result_value = (EditText) this.findViewById(R.id.dispose_result_value);
            dispose_result_value.setText(FliterString(oEntity.getHandle_result()));
            StringUtils.lengthFilter(ReceiptBaseInfoActivity.this, dispose_result_value,
                    EditTextLengthConstants.VISIT_PROCESS_LENGTH, "输入处理结果不能超过" + EditTextLengthConstants.VISIT_PROCESS_LENGTH + "个字");

            service_satisfied1 = (RadioButton) this.findViewById(R.id.satisfied1);
            service_satisfied2 = (RadioButton) this.findViewById(R.id.satisfied2);
            service_satisfied3 = (RadioButton) this.findViewById(R.id.satisfied3);

            product_satisfied1 = (RadioButton) this.findViewById(R.id.satisfied21);
            product_satisfied2 = (RadioButton) this.findViewById(R.id.satisfied22);
            product_satisfied3 = (RadioButton) this.findViewById(R.id.satisfied23);
        } else {
            fee_value = (EditText) this.findViewById(R.id.fee_value);
            serviceRadio1 = (RadioButton) this.findViewById(R.id.serviceRadio1);
            serviceRadio2 = (RadioButton) this.findViewById(R.id.serviceRadio2);
            feeRadio1 = (RadioButton) this.findViewById(R.id.feeRadio1);
            feeRadio2 = (RadioButton) this.findViewById(R.id.feeRadio2);
            if (oEntity.getChargeType() == 1) //收费
            {
                feeRadio2.setChecked(true);
                fee_value.setText(oEntity.getChargeMoney());
            } else if (oEntity.getChargeType() == 2) //不收费
            {
                feeRadio1.setChecked(true);
            }
        }
    }

    /**
     * 保存回执单基本信息
     *
     * @param target
     */
    public void SaveBaseInfoBtnClick(View target) {
        String receipt_base_customer_name = receipt_base_customer_name_value.getText().toString();
        String receipt_base_customer_tax = receipt_base_customer_tax_value.getText().toString();
        String receipt_base_tax_officer = receipt_base_tax_officer_value.getText().toString();
        String receipt_base_number = receipt_base_number_value.getText().toString();
        String receipt_base_software_type = receipt_base_software_type_value.getText().toString();
        String receipt_base_software_version = receipt_base_software_version_value.getText().toString();
        String receipt_base_software_env = receipt_base_software_env_value.getText().toString();
        String receipt_base_service_time = receipt_base_service_time_value.getText().toString();
        String receipt_base_service_address = receipt_base_service_address_value.getText().toString();
        String receipt_base_contact_name = receipt_base_contact_name_value.getText().toString();
        String receipt_base_customer_tel = receipt_base_customer_tel_value.getText().toString();
        String receipt_base_customer_mobile = receipt_base_customer_mobile_value.getText().toString();
        String receipt_base_customer_sign = receipt_base_customer_sign_value.getText().toString();
        String receipt_base_customer_charge = receipt_base_customer_charge_value.getText().toString();
        String receipt_base_customer_remark = receipt_base_customer_remark_value.getText().toString();

        if (receipt_base_customer_name == null || receipt_base_customer_name.equals("")) {
            Toast.makeText(this, "客户名称不能为空！", Toast.LENGTH_LONG).show();
            return;
        } else if (receipt_base_customer_tax == null || receipt_base_customer_tax.equals("")) {
            Toast.makeText(this, "税号不能为空！", Toast.LENGTH_LONG).show();
            return;
        }
//		else if (receipt_base_tax_officer == null || receipt_base_tax_officer.equals(""))
//		{
//			Toast.makeText(this, "税务分局不能为空！", Toast.LENGTH_LONG).show();
//			return;
//		}
        else if (receipt_base_number == null || receipt_base_number.equals("")) {
            Toast.makeText(this, "单据号不能为空！", Toast.LENGTH_LONG).show();
            return;
        } else if (receipt_base_service_time == null || receipt_base_service_time.equals("")) {
            Toast.makeText(this, "到达时间不能为空！", Toast.LENGTH_LONG).show();
            return;
        } else if (receipt_base_service_address == null || receipt_base_service_address.equals("")) {
            Toast.makeText(this, "上门地址不能为空！", Toast.LENGTH_LONG).show();
            return;
        } else if (receipt_base_contact_name == null || receipt_base_contact_name.equals("")) {
            Toast.makeText(this, "联系人不能为空！", Toast.LENGTH_LONG).show();
            return;
        } else if (receipt_base_customer_tel == null || receipt_base_customer_tel.equals("")) {
            Toast.makeText(this, "客户电话不能为空！", Toast.LENGTH_LONG).show();
            return;
        } else if (receipt_base_customer_mobile == null || receipt_base_customer_mobile.equals("")) {
            Toast.makeText(this, "客户手机不能为空！", Toast.LENGTH_LONG).show();
            return;
        } else if (receipt_base_customer_sign == null || receipt_base_customer_sign.equals("")) {
            Toast.makeText(this, "客户签名不能为空！", Toast.LENGTH_LONG).show();
            return;
        } else if (receipt_base_customer_charge == null || receipt_base_customer_charge.equals("")) {
            Toast.makeText(this, "费用过程不能为空！", Toast.LENGTH_LONG).show();
            return;
        } else if (receipt_base_software_type == null || receipt_base_software_type.equals("")) {
            Toast.makeText(this, "硬件型号及编号不能为空！", Toast.LENGTH_LONG).show();
            return;
        } else if (receipt_base_software_version == null || receipt_base_software_version.equals("")) {
            Toast.makeText(this, "软件版本号不能为空！", Toast.LENGTH_LONG).show();
            return;
        } else if (receipt_base_software_env == null || receipt_base_software_env.equals("")) {
            Toast.makeText(this, "使用环境不能为空！", Toast.LENGTH_LONG).show();
            return;
        }

        int is_upload_base_info_value; // 是否将修改数据发送到服务器
        if (is_upload_base_info.isChecked()) {
            is_upload_base_info_value = 1; // 是
        } else {
            is_upload_base_info_value = 2; // 否
        }

        String receipt_type = ""; // 回访类型
        String product_case = ""; // 产品运行情况
        String dispose_result = ""; // 处理结果

        int is_charge = 0;
        String is_charge_value = "";
        String tec_name = sp.getString("FULLNAME", "");
        int service_evaluate = 0;
        int product_evaluate = 0;
        boolean service_evaluate_value = false;
        boolean product_evaluate_value = false;
        int service_type = 0;

        tec_name = receipt_base_tec_name_value.getText().toString();//技术工程师
        if (tec_name == null || tec_name.equals("")) {
            Toast.makeText(this, "技术工程师不能为空！", Toast.LENGTH_LONG).show();
            return;
        }


        if (workOrderType == 2) //回访工单
        {
            receipt_type = receipt_type_value.getSelectedItem().toString(); // 回访类型
            product_case = product_case_value.getText().toString(); // 产品运行情况
            dispose_result = dispose_result_value.getText().toString(); // 处理结果

            if (receipt_type == null || receipt_type.equals("")) {
                Toast.makeText(this, "回访类型不能为空！", Toast.LENGTH_LONG).show();
                return;
            }

            if (product_case == null || product_case.equals("")) {
                Toast.makeText(this, "产品运行情况不能为空！", Toast.LENGTH_LONG).show();
                return;
            }

            if (dispose_result == null || dispose_result.equals("")) {
                Toast.makeText(this, "处理结果不能为空！", Toast.LENGTH_LONG).show();
                return;
            }


            if (service_satisfied1.isChecked()) {
                service_evaluate = 2;//2
                service_evaluate_value = true;
            } else if (service_satisfied2.isChecked()) {
                service_evaluate = 1;//1
                service_evaluate_value = true;
            } else if (service_satisfied3.isChecked()) {
                service_evaluate = 0;//0
                service_evaluate_value = true;
            }

            if (product_satisfied1.isChecked()) {
                product_evaluate = 2;//2
                product_evaluate_value = true;
            } else if (product_satisfied2.isChecked()) {
                product_evaluate = 1;//1
                product_evaluate_value = true;
            } else if (product_satisfied3.isChecked()) {
                product_evaluate = 0;//0
                product_evaluate_value = true;
            }

            if (!service_evaluate_value) {
                Toast.makeText(this, "请选择服务评价！", Toast.LENGTH_LONG).show();
                return;
            }

            if (!product_evaluate_value) {
                Toast.makeText(this, "请选择产品评价！", Toast.LENGTH_LONG).show();
                return;
            }
        } else { //上门工单

//			if (receipt_base_software_type == null || receipt_base_software_type.equals(""))
//			{
//				Toast.makeText(this, "软件型号及编号不能为空！", Toast.LENGTH_LONG).show();
//				return;
//			}
//			else if (receipt_base_software_version == null || receipt_base_software_version.equals(""))
//			{
//				Toast.makeText(this, "软件版本号不能为空！", Toast.LENGTH_LONG).show();
//				return;
//			}
//			if (receipt_base_software_env == null || receipt_base_software_env.equals(""))
//			{
//				Toast.makeText(this, "使用环境不能为空！", Toast.LENGTH_LONG).show();
//				return;
//			}

            if (!serviceRadio1.isChecked() && !serviceRadio2.isChecked()) {
                Toast.makeText(this, "请选择服务方式！", Toast.LENGTH_LONG).show();
                return;
            }

            if (!feeRadio1.isChecked() && !feeRadio2.isChecked()) {
                Toast.makeText(this, "请选择是否收费！", Toast.LENGTH_LONG).show();
                return;
            }

            if (feeRadio1.isChecked()) {
                is_charge = 0;
            } else if (feeRadio2.isChecked()) {
                is_charge = 1;
                is_charge_value = fee_value.getText().toString();
                if (is_charge_value == null || is_charge_value.equals("")) {
                    Toast.makeText(this, "请填写费用值！", Toast.LENGTH_LONG).show();
                    return;
                }
            }

            if (serviceRadio1.isChecked()) {
                service_type = 0;//上门
            } else {
                service_type = 1; //客户送修
            }

        }
        //数据是否发往服务器
        if (is_upload_base_info_value == 1) {
            new PostBaseData().execute(target.getContext().getResources().getString(R.string.request_url),
                    receipt_base_customer_name, receipt_base_customer_tax, receipt_base_service_address,
                    receipt_base_contact_name, receipt_base_customer_tel, receipt_base_customer_mobile);
        }

        // 基本回单信息写入数据库---------------------------------------------
        CommonOrderComplishDB ccDB = new CommonOrderComplishDB(this);
        CommonOrderComplishEntity ccEntity = new CommonOrderComplishEntity();
        ccEntity.setOrder_num(order_num);
        ccEntity.setCustomer_name(receipt_base_customer_name); // 客户名称
        ccEntity.setCustomer_tax(receipt_base_customer_tax); // //税号
        ccEntity.setTax_officer(receipt_base_tax_officer); // 税务分局
        ccEntity.setService_address(receipt_base_service_address);// 上门地址
        ccEntity.setContact_name(receipt_base_contact_name); // 联系人
        ccEntity.setCustomer_tel(receipt_base_customer_tel);
        ccEntity.setCustomer_mobile(receipt_base_customer_mobile);
        ccEntity.setService_time(receipt_base_service_time);
        ccEntity.setNumber_value(receipt_base_number); // 单据号
        ccEntity.setCustomer_sign(receipt_base_customer_sign); // 客户签名
        ccEntity.setCustomer_charge(receipt_base_customer_charge); // 费用过程
        ccEntity.setCustomer_remark(receipt_base_customer_remark); // 备注
        ccEntity.setIs_send_to_server(is_upload_base_info_value); // 是否发往服务器
        ccEntity.setCustomer_solve(service_type); // 解决方式
        ccEntity.setSoftware_type(receipt_base_software_type); // //软件型号及编号
        ccEntity.setSoftware_version(receipt_base_software_version); // //软件版本号
        ccEntity.setSoftware_env_value(receipt_base_software_env);// 使用环境
        ccEntity.setVisit_type(receipt_type.equals("上门")?1+"":0+"");//上门1 电话0
        ccEntity.setVisit_product_case(product_case);
        ccEntity.setVisit_dispose_result(dispose_result);
        ccEntity.setIs_charge(is_charge);
        ccEntity.setIs_charge_value(is_charge_value);
        ccEntity.setTec_name(tec_name);
        ccEntity.setService_evaluate(service_evaluate);
        ccEntity.setProduct_evaluate(product_evaluate);

        // -----------------------------------------------------------

        // 费用报销写入数据库------------------------------------------------
        int costCount = cost_layout.getChildCount();

        ArrayList<CostEntity> costList = new ArrayList<CostEntity>();
        CostEntity costEntity;
        for (int i = 0; i < costCount; i++) {
            View view = cost_layout.getChildAt(i);
            EditText et = (EditText) view.findViewById(R.id.cost_value);
            TextView tv = (TextView) view.findViewById(R.id.cost_text);
            float cost_value = 0;
            String value = et.getText().toString();
            if (value != null && !value.equals("")) {
                cost_value = Float.valueOf(value);
                if (cost_value > 0) {
                    costEntity = new CostEntity();
                    costEntity.setName(tv.getText().toString());
                    costEntity.setValue(String.valueOf(cost_value));
                    costEntity.setOrder_num(order_num);
                    costEntity.setCost_id((Integer) tv.getTag());
                    costList.add(costEntity);
                }
            }

        }

        long result = ccDB.insert(ccEntity);
        if (costList.size() > 0) {
            CostValueDB cvDB = new CostValueDB(this);
            cvDB.batchInsert(costList);
        }

        if (result > 0) {
            setResult(RESULT_OK, null);
            Toast.makeText(this, "操作成功!", Toast.LENGTH_LONG).show();
        } else {
            setResult(RESULT_CANCELED, null);
            Toast.makeText(this, "操作失败!", Toast.LENGTH_LONG).show();
        }
        // ------------------------------------------------------------
        this.finish();
    }

    /**
     * 返回到之前界面
     *
     * @param target
     */
    public void BackBtnClick(View target) {
        this.finish();
    }

    public String FliterString(String str) {
        if (str == null || str.equals("") || str.equals("null")) {
            return "";
        } else {
            return str;
        }
    }

    private class PostBaseData extends AsyncTask<String, Void, Integer> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Integer doInBackground(String... params) {
            postData(params[0], params[1], params[2], params[3],params[4],params[5],params[6]);
            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {

        }
    }

    // 发送http更新客户基本信息
    public int postData(String... params) {
        HttpRestAchieve httpRestAchieve = new HttpRestAchieve();
        HashMap<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("cust_name", params[1]);
        paramsMap.put("cust_tax_code", params[2]);
        paramsMap.put("addr", params[3]);
        paramsMap.put("contract", params[4]);
        paramsMap.put("tel", params[5]);
        paramsMap.put("mobile", params[6]);
        Log.d(TAG,"paramsMap : " + paramsMap);
        try {
            String url = params[0] + "/fieldworker/biz/cust_info";
            Log.d(TAG, "postData url : " + url);
            NetBackDataEntity netBackData = httpRestAchieve.postRequestData(url, paramsMap);
            String data = netBackData.getData();
            Log.d(TAG, "postData backData : " + data);
            JSONObject jsonObject = new JSONObject(data);
            int result = jsonObject.getInt("resultCode");
            if (result == 1) {
                Log.d(TAG, "客户基本信息更新成功！");
            } else {
                Log.d(TAG, "客户基本信息更新失败！");
            }

        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        } catch (NetAccessException e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }
        return 0;
    }
}
