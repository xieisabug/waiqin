package com.sealion.serviceassistant;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;
import com.sealion.serviceassistant.adapter.ProductAdapter;
import com.sealion.serviceassistant.adapter.QCategoryAdapter;
import com.sealion.serviceassistant.adapter.QTypeAdapter;
import com.sealion.serviceassistant.db.ProductDB;
import com.sealion.serviceassistant.db.QCategoryDB;
import com.sealion.serviceassistant.db.QTypeDB;
import com.sealion.serviceassistant.db.QuestionListDB;
import com.sealion.serviceassistant.entity.*;
import com.sealion.serviceassistant.exception.NetAccessException;
import com.sealion.serviceassistant.net.HttpRestAchieve;
import com.sealion.serviceassistant.superactivity.OrderActivity;
import com.sealion.serviceassistant.tools.EditTextLengthConstants;
import com.sealion.serviceassistant.tools.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * ������Activity.
 */
public class AnswerQuestionActivity extends OrderActivity {
    private final String TAG = getClass().getSimpleName();

    private TextView current_state_value = null;
    private LinearLayout q_a_panel = null;
    private QCategoryAdapter qcAdapter = null;
    private QTypeAdapter qtAdapter = null;
    private ProductAdapter pAdapter = null;
    private QTypeDB qtDB = null;
    private QCategoryDB qcDB = null;
    private ProductDB pDB = null;
    private Spinner q_type_spinner = null;
    private Spinner q_category_spinner = null;
    private Spinner service_product_value = null;
    private QuestionListDB qlDB = null;
    private TextView top_bar_title = null;
    private ImageView back_image = null;
    private ArrayList<Q_Type> qList = null;

    private HashMap<Integer, TextView> textMap = new HashMap<Integer, TextView>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.answer_question);

        top_bar_title = (TextView) this.findViewById(R.id.top_bar_title);
        top_bar_title.setText(this.getResources().getString(R.string.answer_question));
        back_image = (ImageView) this.findViewById(R.id.back_image);
        back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        current_state_value = (TextView) this.findViewById(R.id.current_state_value);
        current_state_value.setText(this.getResources().getString(R.string.spot_solve_back_paper));

        q_a_panel = (LinearLayout) this.findViewById(R.id.q_a_panel);

        order_num = this.getIntent().getExtras().getString("order_num");

        qlDB = new QuestionListDB(this);
        ArrayList<QuestionEntity> qeList = qlDB.getQuestionByOrderNum(order_num);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        int i = 1;
        for (QuestionEntity qe : qeList) {
            //���ݹ���ID��ȡ��������
            View view = inflater.inflate(R.layout.answer_question_content, null);

            TextView question_number = (TextView) view.findViewById(R.id.question_number);
            question_number.setText("����" + i + ":");

            TextView question_content = (TextView) view.findViewById(R.id.question_content); //��������
            question_content.setText(qe.getQ_content());
            question_content.setTag(qe.getQ_id());

            q_type_spinner = (Spinner) view.findViewById(R.id.q_type_spinner); //��������
            q_category_spinner = (Spinner) view.findViewById(R.id.q_category_spinner);//�������
            service_product_value = (Spinner) view.findViewById(R.id.service_product_value);//��Ʒѡ��
//            service_product_value.setText(qe.getProductName());
//            textMap.put(qe.getQ_category_id(), service_product_value);

            EditText answer_content = (EditText) view.findViewById(R.id.answer_content); //����ش�
            StringUtils.lengthFilter(AnswerQuestionActivity.this, answer_content,
                    EditTextLengthConstants.QUESTION_ANSWER_LENGTH, "�����������ܳ���" + EditTextLengthConstants.QUESTION_ANSWER_LENGTH + "����");
            answer_content.setText(qe.getQ_answer());

            qcDB = new QCategoryDB(this);
            qtDB = new QTypeDB(this);

            List<Q_Category> mList = qcDB.getQ_Category();
            qcAdapter = new QCategoryAdapter(this, mList);
            //��adapter ��ӵ�spinner��
            q_category_spinner.setAdapter(qcAdapter);
            //����¼�Spinner�¼�����
            q_category_spinner.setOnItemSelectedListener(new QCategorySpinnerSelectedListener(q_type_spinner));
            //q_category_spinner.setVisibility(View.VISIBLE);

            pDB = new ProductDB(this);
            List<ProductEntity> pList = pDB.getAllProduct();
            pAdapter = new ProductAdapter(this, pList);
            int productId = Integer.parseInt(qe.getProductId());
            int z = 0;
            for (ProductEntity productEntity : pList) {
                z++;
                if (productId == productEntity.getProductId()) {
                    Log.d(TAG, "product id : " + productEntity.getProductId() + "; and z is :" + z + "; and product name is: " + productEntity.getProductName());
                    break;
                }
            }
            //��������������
            service_product_value.setAdapter(pAdapter);
            service_product_value.setSelection(z - 1, true);

            Log.d(TAG, "question category id : " + qe.getQ_category_id());
            Log.d(TAG, "question type id : " + qe.getQ_type_id());
            //����Ĭ��ֵ
            int m = 0;
            for (Q_Category qc : mList) {
                m++;
                Log.d(TAG, "question category id : " + qc.getCategory_id() + "; and m is :" + m);
                if (qc.getCategory_id() == qe.getQ_category_id()) {
                    q_category_spinner.setSelection(m - 1, true);
                    qList = qtDB.getQTypeByCategoryId(qc.getCategory_id() + "");
                    break;
                }
            }


            //TODO ��ҪΪtype��һ����ʼֵ
            if (qList != null) {
                int n = 0;
                for (Q_Type qt : qList) {
                    n++;
                    Log.d(TAG, "question type id : " + qe.getQ_type_id() + "; and n is :" + n);
                    if (qt.getType_id() == qe.getQ_type_id()) {
                        q_type_spinner.setSelection(n - 1, true);
                        break;
                    }
                }

            }

            q_a_panel.addView(view);
            i++;
            qList = null;
        }

    }

    //��������������¼�
    class QCategorySpinnerSelectedListener implements OnItemSelectedListener {

        private Spinner spinner = null;

        public QCategorySpinnerSelectedListener(Spinner spinner) {
            this.spinner = spinner;
        }

        public void onItemSelected(AdapterView<?> arg0, View view, int arg2,
                                   long arg3) {

            TextView _TextView1 = (TextView) view.findViewById(R.id.name);
            Q_Category q_Category = (Q_Category) _TextView1.getTag();
            ArrayList<Q_Type> qList = qtDB.getQTypeByCategoryId(q_Category.getCategory_id() + "");
            Log.d(TAG, "onItemSelected category id is : " + q_Category.getCategory_id());

            TextView tv = textMap.get(q_Category.getCategory_id());
            if (tv != null) {
                tv.setText(q_Category.getProductName());
            }

            qtAdapter = new QTypeAdapter(getBaseContext(), qList);
            spinner.setAdapter(qtAdapter);
            qtAdapter.notifyDataSetChanged();

        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    public void QuestionContentBtnClick(View target) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        final View v1 = inflater.inflate(R.layout.alert_question_content, null);
        //R.layout.login��login.xml�ļ�����Ӧ,��loginת����View����
        AlertDialog.Builder dialog = new AlertDialog.Builder(AnswerQuestionActivity.this);
        dialog.setTitle("�����޸�");
        dialog.setView(v1);//����ʹ��View
        //���ÿؼ�Ӧ����v1.findViewById �������
        final TextView tv = (TextView) target.findViewById(R.id.question_content);
        final EditText et = (EditText) v1.findViewById(R.id.alert_q_content);
        et.setText(tv.getText().toString());

        dialog.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                tv.setText(et.getText());
            }
        });
        dialog.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    /**
     * ����������
     *
     * @param target
     */
    public void SaveBaseInfoBtnClick(View target) {
        boolean sign = false;
        int q_count = q_a_panel.getChildCount();
        View view;
        TextView question_content;
        Spinner q_type_spinner;
        View q_type_select_view;
        TextView q_type_tv;

        Spinner q_category_spinner;
        View q_category_select_view;
        TextView q_category_tv;

        Spinner service_product_value;
        View service_product_value_view;
        TextView service_product_value_tv;

        TextView answer_content;

        ArrayList<QuestionEntity> qList = new ArrayList<QuestionEntity>();
        QuestionEntity qEntity;
        for (int i = 0; i < q_count; i++) {
            qEntity = new QuestionEntity();

            qEntity.setOrder_num(order_num);
            view = q_a_panel.getChildAt(i);
            question_content = (TextView) view.findViewById(R.id.question_content);

            q_type_spinner = (Spinner) view.findViewById(R.id.q_type_spinner);
            q_type_select_view = q_type_spinner.getSelectedView();
            q_type_tv = (TextView) q_type_select_view.findViewById(R.id.name);

            q_category_spinner = (Spinner) view.findViewById(R.id.q_category_spinner);
            q_category_select_view = q_category_spinner.getSelectedView();
            q_category_tv = (TextView) q_category_select_view.findViewById(R.id.name);

            service_product_value = (Spinner) view.findViewById(R.id.service_product_value);
            service_product_value_view = service_product_value.getSelectedView();
            service_product_value_tv = (TextView) service_product_value_view.findViewById(R.id.name);

            Q_Category q_Category = (Q_Category) q_category_tv.getTag();
            answer_content = (TextView) view.findViewById(R.id.answer_content); //����ش�

            String q_content = question_content.getText().toString();//��������
            String q_id = question_content.getTag().toString();//����id
            int q_type_id = Integer.parseInt(q_type_tv.getTag().toString()); //��������id
            String q_type = q_type_tv.getText().toString(); //��������
            int q_category_id = q_Category.getCategory_id(); //�������id
            String q_category = q_category_tv.getText().toString(); //�������
            String productId = service_product_value_tv.getTag().toString(); //��Ʒid
            Log.d(TAG, "productId : " + productId);
            String productName = service_product_value_tv.getText().toString(); //��Ʒ����
            Log.d(TAG, "productName : " + productName);
            String answer = answer_content.getText().toString();//����ش�

            qEntity.setQ_content(q_content);
            qEntity.setQ_id(q_id);
            qEntity.setQ_type_id(q_type_id);
            qEntity.setQ_type(q_type);
            qEntity.setQ_category_id(q_category_id);
            qEntity.setQ_category(q_category);
            qEntity.setProductId(productId);
            qEntity.setProductName(productName);

            if (answer == null || answer.equals("")) {
                sign = true;
                Toast.makeText(this, "����д�����", Toast.LENGTH_LONG).show();
                break;
            } else {
                qEntity.setQ_answer(answer);
            }
            qList.add(qEntity);

        }

        if (!sign) {
            if (qList.size() > 0) {
                QuestionListDB qListDB = new QuestionListDB(this);
                for (QuestionEntity entity : qList) {
                    qListDB.updateQuestionContent(entity.getQ_id() + "", entity);
//                    new PostData().execute(target.getContext().getResources().getString(R.string.request_url),
//                            entity.getQ_id(), entity.getQ_type_id()+"",entity.getQ_type(),entity.getQ_answer());
                }
                Toast.makeText(this, "�����ɹ�!", Toast.LENGTH_LONG).show();
                this.finish();
            } else {
                Toast.makeText(this, "��������!", Toast.LENGTH_LONG).show();
            }
        }


    }

    /**
     * ����
     *
     * @param target
     */
    public void BackBtnClick(View target) {
        finish();
    }

    private class PostData extends AsyncTask<String, Void, Integer> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Integer doInBackground(String... params) {
            postData(params[0], params[1], params[2], params[3],params[4]);
            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {

        }
    }

    // ����http����֪ͨ�����Ѿ����յ�����
    public int postData(String... params) {
        HttpRestAchieve httpRestAchieve = new HttpRestAchieve();
        HashMap<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("questionId", params[1]);
        paramsMap.put("questionTypeId", params[2]);
        paramsMap.put("questionType", params[3]);
        paramsMap.put("questionAnswer", params[4]);
        try {
            String url = params[0] + "/fieldworker/biz/confirm";
            Log.d(TAG, "postData url : " + url);
            NetBackDataEntity netBackData = httpRestAchieve.postRequestData(url, paramsMap);
            String data = netBackData.getData();
            Log.d(TAG, "postData backData : " + data);
            JSONObject jsonObject = new JSONObject(data);
            int result = jsonObject.getInt("resultCode");
            if (result == 1) {
                Log.d(TAG, "֪ͨ�õ������ɹ���");
            } else {
                Log.d(TAG, "֪ͨ�õ�����ʧ�ܣ�");
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
