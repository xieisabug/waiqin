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
 * app启动界面，加载一些资源
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

        //检测网络状态，如果网络不存在，则进行离线登录
        int result = NetworkTool.getNetType(this);
        if (result == -1) {
            loadLoginActivity();
        } else {
            UpdateApplication updateApp = new UpdateApplication(getResources().getString(R.string.request_url), this);

            int newVerCode = updateApp.getServerVerCode();

            int vercode = Config.getVerCode(this);
            Log.d("70apps", "当前版本" + vercode + ",最新版本" + newVerCode);
            //如果当前版本小于服务器的最新版本，则进行更新，否则进入登录界面
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
     * 初始化软件数据和删除历史数据
     */
    class InitDataTask extends AsyncTask<Void, Integer, Integer> {
        Editor edit = init_sp.edit();

        @Override
        protected void onPreExecute() {
            // 第一个执行方法
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Void... params) {
            //删除一个月之前的数据
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
                //初始化成功
                startActivity();
            }

        }
    }
}
