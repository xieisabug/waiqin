package com.sealion.serviceassistant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import com.sealion.serviceassistant.db.*;
import com.sealion.serviceassistant.tools.Config;
import com.sealion.serviceassistant.tools.DateTools;
import com.sealion.serviceassistant.tools.NetworkTool;
import com.sealion.serviceassistant.tools.UpdateApplication;

/**
 * app�������棬����һЩ��Դ
 */
public class LoadingPageActivity extends Activity {
    private static final String TAG = LoadingPageActivity.class.getSimpleName();
    private SharedPreferences init_sp = null;
    private SharedPreferences setting_sp = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.loading_page);

        init_sp = getSharedPreferences("INIT_DATA", Context.MODE_PRIVATE);
        setting_sp = getSharedPreferences("SETTING_DATA", Context.MODE_PRIVATE);
    }

    public void onStart() {
        super.onStart();

        //�������״̬��������粻���ڣ���������ߵ�¼
        int result = NetworkTool.getNetType(this);
        if (result == -1) {
            loadLoginActivity();
        } else {
            UpdateApplication updateApp = new UpdateApplication(getResources().getString(R.string.request_url), this);

            int newVerCode = updateApp.getServerVerCode();

            int vercode = Config.getVerCode(this);
            Log.d("70apps", "��ǰ�汾" + vercode + ",���°汾" + newVerCode);
            //�����ǰ�汾С�ڷ����������°汾������и��£���������¼����
            if (newVerCode > vercode) {
                updateApp.doNewVersionUpdate();
            } else {
                loadLoginActivity();
            }
        }
    }

    public void loadLoginActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//				Intent mainIntent = new Intent(LoadingPageActivity.this, LoginActivity.class);
//				//Intent mainIntent = new Intent(SplashScreen.this, FloatActivity.class);
//				LoadingPageActivity.this.startActivity(mainIntent);
//				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//				LoadingPageActivity.this.finish();
                new InitDataTask().execute();
            }
        }, 500);
    }

    public void startActivity() {
        Intent mainIntent = new Intent(LoadingPageActivity.this, LoginActivity.class);
        //Intent mainIntent = new Intent(SplashScreen.this, FloatActivity.class);
        LoadingPageActivity.this.startActivity(mainIntent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        LoadingPageActivity.this.finish();
    }

    /**
     * ��ʼ��������ݺ�ɾ����ʷ����
     */
    class InitDataTask extends AsyncTask<Void, Integer, Integer> {
        Editor edit = init_sp.edit();

        @Override
        protected void onPreExecute() {
            // ��һ��ִ�з���
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Void... params) {
            //ɾ��һ����֮ǰ������
            String date = DateTools.getFormatDataAndTimeBeforeDay(-31);

            CommonOrderComplishDB cocDB = new CommonOrderComplishDB(LoadingPageActivity.this);
            cocDB.deleteOldData(date);

            FileManageDB fmDB = new FileManageDB(LoadingPageActivity.this);
            fmDB.deleteOldData(date);

            QuestionListDB qlDB = new QuestionListDB(LoadingPageActivity.this);
            qlDB.deleteOldData(date);

            NoticeDB ntDB = new NoticeDB(LoadingPageActivity.this);
            ntDB.deleteOldData(date);

            OrderChangeDB ocDB = new OrderChangeDB(LoadingPageActivity.this);
            ocDB.deleteOldData(date);

            OrderListDB olDB = new OrderListDB(LoadingPageActivity.this);
            olDB.deleteOldData(date);

            PhoneComplishDB pcDB = new PhoneComplishDB(LoadingPageActivity.this);
            pcDB.deleteOldData(date);

            OrderDetailListDB odDB = new OrderDetailListDB(LoadingPageActivity.this);
            odDB.deleteOldData(date);

            CostValueDB cvDB = new CostValueDB(LoadingPageActivity.this);
            cvDB.deleteOldData(date);
            return 1;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {

        }

        @Override
        protected void onPostExecute(Integer result) {
            if (result == 1) {
                //��ʼ���ɹ�
                startActivity();
            }

        }
    }
}
