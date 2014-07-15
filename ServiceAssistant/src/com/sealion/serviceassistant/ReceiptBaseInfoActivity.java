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
 * �ֳ������ִ��������Ϣ��д¼��.
 * ���޸Ŀͻ�������Ϣ
 */
public class ReceiptBaseInfoActivity extends OrderActivity {
    public final String TAG = getClass().getSimpleName();

    private TextView current_state_value = null; // ��ǰ״̬

    private EditText receipt_base_customer_name_value = null; // �ͻ�����
    private EditText receipt_base_customer_tax_value = null; // ˰��
    private EditText receipt_base_tax_officer_value = null; // ˰��־�
    private EditText receipt_base_service_address_value = null; // ���ŵ�ַ
    private EditText receipt_base_contact_name_value = null; // ��ϵ��
    private EditText receipt_base_customer_tel_value = null; // �ͻ��绰
    private EditText receipt_base_customer_mobile_value = null; // �ͻ��ֻ�
    private EditText receipt_base_service_time_value = null; // ����ʱ��
    private EditText receipt_base_number_value = null; // ���ݺ�
    private EditText receipt_base_customer_sign_value = null; // �ͻ�ǩ��
    private EditText receipt_base_customer_charge_value = null; // ���ù���
    private EditText receipt_base_customer_remark_value = null; // ��ע
    private EditText fee_value = null; //����ֵ
    private EditText receipt_base_software_type_value = null; // ����ͺż����
    private EditText receipt_base_software_version_value = null; // ����汾��
    private EditText receipt_base_software_env_value = null; // ʹ�û���
    private EditText receipt_base_tec_name_value;//��������ʦ
    private Spinner receipt_type_value = null; // �ط�����
    private EditText product_case_value = null; // ��Ʒ�������
    private EditText dispose_result_value = null; // ������

    private TextView top_bar_title = null;
    private ImageView back_image = null;

    private LinearLayout cost_layout = null;
    private CheckBox is_upload_base_info = null;
    private OrderListDB olDB = null;
    private int id;
    private int workOrderType;

    //����ʽ
    private RadioButton serviceRadio1; //����
    private RadioButton serviceRadio2; //�û�����

    //�Ƿ��շ�
    private RadioButton feeRadio1;
    private RadioButton feeRadio2;

    // �������������
    private RadioButton service_satisfied1; // ������
    private RadioButton service_satisfied2; // ����
    private RadioButton service_satisfied3; // ������

    //��Ʒ����������
    private RadioButton product_satisfied1; // ������
    private RadioButton product_satisfied2; // ����
    private RadioButton product_satisfied3; // ������

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

        // �ͻ�����
        receipt_base_customer_name_value = (EditText) this.findViewById(R.id.receipt_base_customer_name_value);
        receipt_base_customer_name_value.setText(oEntity.getCustomerName());
        // ˰��
        receipt_base_customer_tax_value = (EditText) this.findViewById(R.id.receipt_base_customer_tax_value);
        receipt_base_customer_tax_value.setText(oEntity.getTaxCode());
        // ˰��־�
        receipt_base_tax_officer_value = (EditText) this.findViewById(R.id.receipt_base_tax_officer_value);
        receipt_base_tax_officer_value.setText(oEntity.getRevenueName());
        // ����ʱ��
        receipt_base_service_time_value = (EditText) this.findViewById(R.id.receipt_base_service_time_value);
        receipt_base_service_time_value.setText(DateTools.getFormatDateAndTime());
        // ���ݺ�
        receipt_base_number_value = (EditText) this.findViewById(R.id.receipt_base_number_value);
        receipt_base_number_value.setText(oEntity.getOrderToken());
        // Ӳ���ͺż����
        receipt_base_software_type_value = (EditText) this.findViewById(R.id.receipt_base_software_type_value);

        // ����汾��
        receipt_base_software_version_value = (EditText) this.findViewById(R.id.receipt_base_software_version_value);

        // ʹ�û���
        receipt_base_software_env_value = (EditText) this.findViewById(R.id.receipt_base_software_env_value);

        // ���ŵ�ַ
        receipt_base_service_address_value = (EditText) this.findViewById(R.id.receipt_base_service_address_value);
        receipt_base_service_address_value.setText(oEntity.getCustomerAddr());

        // ��ϵ��
        receipt_base_contact_name_value = (EditText) this.findViewById(R.id.receipt_base_contact_name_value);
        receipt_base_contact_name_value.setText(oEntity.getLinkPerson());
        // �ͻ��绰
        receipt_base_customer_tel_value = (EditText) this.findViewById(R.id.receipt_base_customer_tel_value);
        receipt_base_customer_tel_value.setText(FliterString(oEntity.getCustomerTel()));
        // �ͻ��ֻ�
        receipt_base_customer_mobile_value = (EditText) this.findViewById(R.id.receipt_base_customer_mobile_value);
        receipt_base_customer_mobile_value.setText(FliterString(oEntity.getCustomerMobile()));
        // �ͻ�ǩ��
        receipt_base_customer_sign_value = (EditText) this.findViewById(R.id.receipt_base_customer_sign_value);

        // ���ù���
        receipt_base_customer_charge_value = (EditText) this.findViewById(R.id.receipt_base_customer_charge_value);
        StringUtils.lengthFilter(ReceiptBaseInfoActivity.this, receipt_base_customer_charge_value,
                EditTextLengthConstants.FEE_PROCESS_LENGTH, "������ù��̲��ܳ���" + EditTextLengthConstants.FEE_PROCESS_LENGTH + "����");
        // ��ע
        receipt_base_customer_remark_value = (EditText) this.findViewById(R.id.receipt_base_customer_remark_value);
        StringUtils.lengthFilter(ReceiptBaseInfoActivity.this, receipt_base_customer_remark_value,
                EditTextLengthConstants.REMARK_LENGTH, "���뱸ע���ܳ���" + EditTextLengthConstants.REMARK_LENGTH + "����");

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
//            receipt_type_value.setText(oEntity.getVisit_type());//����Ҫ���ã���Ҫ������Ա��д
            receipt_type_value.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, new String[]{"����","�绰"}));

            product_case_value = (EditText) this.findViewById(R.id.product_case_value);
            product_case_value.setText(FliterString(oEntity.getProduct_status()));
            StringUtils.lengthFilter(ReceiptBaseInfoActivity.this, product_case_value,
                    EditTextLengthConstants.VISIT_PRODUCT_LENGTH, "�����Ʒ����������ܳ���" + EditTextLengthConstants.VISIT_PRODUCT_LENGTH + "����");
            dispose_result_value = (EditText) this.findViewById(R.id.dispose_result_value);
            dispose_result_value.setText(FliterString(oEntity.getHandle_result()));
            StringUtils.lengthFilter(ReceiptBaseInfoActivity.this, dispose_result_value,
                    EditTextLengthConstants.VISIT_PROCESS_LENGTH, "���봦�������ܳ���" + EditTextLengthConstants.VISIT_PROCESS_LENGTH + "����");

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
            if (oEntity.getChargeType() == 1) //�շ�
            {
                feeRadio2.setChecked(true);
                fee_value.setText(oEntity.getChargeMoney());
            } else if (oEntity.getChargeType() == 2) //���շ�
            {
                feeRadio1.setChecked(true);
            }
        }
    }

    /**
     * �����ִ��������Ϣ
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
            Toast.makeText(this, "�ͻ����Ʋ���Ϊ�գ�", Toast.LENGTH_LONG).show();
            return;
        } else if (receipt_base_customer_tax == null || receipt_base_customer_tax.equals("")) {
            Toast.makeText(this, "˰�Ų���Ϊ�գ�", Toast.LENGTH_LONG).show();
            return;
        }
//		else if (receipt_base_tax_officer == null || receipt_base_tax_officer.equals(""))
//		{
//			Toast.makeText(this, "˰��־ֲ���Ϊ�գ�", Toast.LENGTH_LONG).show();
//			return;
//		}
        else if (receipt_base_number == null || receipt_base_number.equals("")) {
            Toast.makeText(this, "���ݺŲ���Ϊ�գ�", Toast.LENGTH_LONG).show();
            return;
        } else if (receipt_base_service_time == null || receipt_base_service_time.equals("")) {
            Toast.makeText(this, "����ʱ�䲻��Ϊ�գ�", Toast.LENGTH_LONG).show();
            return;
        } else if (receipt_base_service_address == null || receipt_base_service_address.equals("")) {
            Toast.makeText(this, "���ŵ�ַ����Ϊ�գ�", Toast.LENGTH_LONG).show();
            return;
        } else if (receipt_base_contact_name == null || receipt_base_contact_name.equals("")) {
            Toast.makeText(this, "��ϵ�˲���Ϊ�գ�", Toast.LENGTH_LONG).show();
            return;
        } else if (receipt_base_customer_tel == null || receipt_base_customer_tel.equals("")) {
            Toast.makeText(this, "�ͻ��绰����Ϊ�գ�", Toast.LENGTH_LONG).show();
            return;
        } else if (receipt_base_customer_mobile == null || receipt_base_customer_mobile.equals("")) {
            Toast.makeText(this, "�ͻ��ֻ�����Ϊ�գ�", Toast.LENGTH_LONG).show();
            return;
        } else if (receipt_base_customer_sign == null || receipt_base_customer_sign.equals("")) {
            Toast.makeText(this, "�ͻ�ǩ������Ϊ�գ�", Toast.LENGTH_LONG).show();
            return;
        } else if (receipt_base_customer_charge == null || receipt_base_customer_charge.equals("")) {
            Toast.makeText(this, "���ù��̲���Ϊ�գ�", Toast.LENGTH_LONG).show();
            return;
        } else if (receipt_base_software_type == null || receipt_base_software_type.equals("")) {
            Toast.makeText(this, "Ӳ���ͺż���Ų���Ϊ�գ�", Toast.LENGTH_LONG).show();
            return;
        } else if (receipt_base_software_version == null || receipt_base_software_version.equals("")) {
            Toast.makeText(this, "����汾�Ų���Ϊ�գ�", Toast.LENGTH_LONG).show();
            return;
        } else if (receipt_base_software_env == null || receipt_base_software_env.equals("")) {
            Toast.makeText(this, "ʹ�û�������Ϊ�գ�", Toast.LENGTH_LONG).show();
            return;
        }

        int is_upload_base_info_value; // �Ƿ��޸����ݷ��͵�������
        if (is_upload_base_info.isChecked()) {
            is_upload_base_info_value = 1; // ��
        } else {
            is_upload_base_info_value = 2; // ��
        }

        String receipt_type = ""; // �ط�����
        String product_case = ""; // ��Ʒ�������
        String dispose_result = ""; // ������

        int is_charge = 0;
        String is_charge_value = "";
        String tec_name = sp.getString("FULLNAME", "");
        int service_evaluate = 0;
        int product_evaluate = 0;
        boolean service_evaluate_value = false;
        boolean product_evaluate_value = false;
        int service_type = 0;

        tec_name = receipt_base_tec_name_value.getText().toString();//��������ʦ
        if (tec_name == null || tec_name.equals("")) {
            Toast.makeText(this, "��������ʦ����Ϊ�գ�", Toast.LENGTH_LONG).show();
            return;
        }


        if (workOrderType == 2) //�طù���
        {
            receipt_type = receipt_type_value.getSelectedItem().toString(); // �ط�����
            product_case = product_case_value.getText().toString(); // ��Ʒ�������
            dispose_result = dispose_result_value.getText().toString(); // ������

            if (receipt_type == null || receipt_type.equals("")) {
                Toast.makeText(this, "�ط����Ͳ���Ϊ�գ�", Toast.LENGTH_LONG).show();
                return;
            }

            if (product_case == null || product_case.equals("")) {
                Toast.makeText(this, "��Ʒ�����������Ϊ�գ�", Toast.LENGTH_LONG).show();
                return;
            }

            if (dispose_result == null || dispose_result.equals("")) {
                Toast.makeText(this, "����������Ϊ�գ�", Toast.LENGTH_LONG).show();
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
                Toast.makeText(this, "��ѡ��������ۣ�", Toast.LENGTH_LONG).show();
                return;
            }

            if (!product_evaluate_value) {
                Toast.makeText(this, "��ѡ���Ʒ���ۣ�", Toast.LENGTH_LONG).show();
                return;
            }
        } else { //���Ź���

//			if (receipt_base_software_type == null || receipt_base_software_type.equals(""))
//			{
//				Toast.makeText(this, "����ͺż���Ų���Ϊ�գ�", Toast.LENGTH_LONG).show();
//				return;
//			}
//			else if (receipt_base_software_version == null || receipt_base_software_version.equals(""))
//			{
//				Toast.makeText(this, "����汾�Ų���Ϊ�գ�", Toast.LENGTH_LONG).show();
//				return;
//			}
//			if (receipt_base_software_env == null || receipt_base_software_env.equals(""))
//			{
//				Toast.makeText(this, "ʹ�û�������Ϊ�գ�", Toast.LENGTH_LONG).show();
//				return;
//			}

            if (!serviceRadio1.isChecked() && !serviceRadio2.isChecked()) {
                Toast.makeText(this, "��ѡ�����ʽ��", Toast.LENGTH_LONG).show();
                return;
            }

            if (!feeRadio1.isChecked() && !feeRadio2.isChecked()) {
                Toast.makeText(this, "��ѡ���Ƿ��շѣ�", Toast.LENGTH_LONG).show();
                return;
            }

            if (feeRadio1.isChecked()) {
                is_charge = 0;
            } else if (feeRadio2.isChecked()) {
                is_charge = 1;
                is_charge_value = fee_value.getText().toString();
                if (is_charge_value == null || is_charge_value.equals("")) {
                    Toast.makeText(this, "����д����ֵ��", Toast.LENGTH_LONG).show();
                    return;
                }
            }

            if (serviceRadio1.isChecked()) {
                service_type = 0;//����
            } else {
                service_type = 1; //�ͻ�����
            }

        }
        //�����Ƿ���������
        if (is_upload_base_info_value == 1) {
            new PostBaseData().execute(target.getContext().getResources().getString(R.string.request_url),
                    receipt_base_customer_name, receipt_base_customer_tax, receipt_base_service_address,
                    receipt_base_contact_name, receipt_base_customer_tel, receipt_base_customer_mobile);
        }

        // �����ص���Ϣд�����ݿ�---------------------------------------------
        CommonOrderComplishDB ccDB = new CommonOrderComplishDB(this);
        CommonOrderComplishEntity ccEntity = new CommonOrderComplishEntity();
        ccEntity.setOrder_num(order_num);
        ccEntity.setCustomer_name(receipt_base_customer_name); // �ͻ�����
        ccEntity.setCustomer_tax(receipt_base_customer_tax); // //˰��
        ccEntity.setTax_officer(receipt_base_tax_officer); // ˰��־�
        ccEntity.setService_address(receipt_base_service_address);// ���ŵ�ַ
        ccEntity.setContact_name(receipt_base_contact_name); // ��ϵ��
        ccEntity.setCustomer_tel(receipt_base_customer_tel);
        ccEntity.setCustomer_mobile(receipt_base_customer_mobile);
        ccEntity.setService_time(receipt_base_service_time);
        ccEntity.setNumber_value(receipt_base_number); // ���ݺ�
        ccEntity.setCustomer_sign(receipt_base_customer_sign); // �ͻ�ǩ��
        ccEntity.setCustomer_charge(receipt_base_customer_charge); // ���ù���
        ccEntity.setCustomer_remark(receipt_base_customer_remark); // ��ע
        ccEntity.setIs_send_to_server(is_upload_base_info_value); // �Ƿ���������
        ccEntity.setCustomer_solve(service_type); // �����ʽ
        ccEntity.setSoftware_type(receipt_base_software_type); // //����ͺż����
        ccEntity.setSoftware_version(receipt_base_software_version); // //����汾��
        ccEntity.setSoftware_env_value(receipt_base_software_env);// ʹ�û���
        ccEntity.setVisit_type(receipt_type.equals("����")?1+"":0+"");//����1 �绰0
        ccEntity.setVisit_product_case(product_case);
        ccEntity.setVisit_dispose_result(dispose_result);
        ccEntity.setIs_charge(is_charge);
        ccEntity.setIs_charge_value(is_charge_value);
        ccEntity.setTec_name(tec_name);
        ccEntity.setService_evaluate(service_evaluate);
        ccEntity.setProduct_evaluate(product_evaluate);

        // -----------------------------------------------------------

        // ���ñ���д�����ݿ�------------------------------------------------
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
            Toast.makeText(this, "�����ɹ�!", Toast.LENGTH_LONG).show();
        } else {
            setResult(RESULT_CANCELED, null);
            Toast.makeText(this, "����ʧ��!", Toast.LENGTH_LONG).show();
        }
        // ------------------------------------------------------------
        this.finish();
    }

    /**
     * ���ص�֮ǰ����
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

    // ����http���¿ͻ�������Ϣ
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
                Log.d(TAG, "�ͻ�������Ϣ���³ɹ���");
            } else {
                Log.d(TAG, "�ͻ�������Ϣ����ʧ�ܣ�");
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
