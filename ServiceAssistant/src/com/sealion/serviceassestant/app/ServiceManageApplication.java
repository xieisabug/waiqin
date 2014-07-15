package com.sealion.serviceassestant.app;

import java.util.ArrayList;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKEvent;
import com.baidu.mapapi.MKGeneralListener;
import com.sealion.serviceassistant.R;
import com.sealion.serviceassistant.db.CostDB;
import com.sealion.serviceassistant.db.ProductDB;
import com.sealion.serviceassistant.db.QTypeDB;
import com.sealion.serviceassistant.db.QCategoryDB;
import com.sealion.serviceassistant.entity.CostEntity;
import com.sealion.serviceassistant.entity.ProductEntity;
import com.sealion.serviceassistant.entity.Q_Category;
import com.sealion.serviceassistant.entity.Q_Type;
import com.sealion.serviceassistant.tools.InitialAppData;

import android.app.Application;
import android.content.res.Resources.NotFoundException;
import android.util.Log;
import android.widget.Toast;

/**
 * <p>Title: ServiceManageApplication.java</p>
 * <p>Description: 程序的Application类，进行了数据的初始化和百度地图的管理类初始化</p>
 * <p>Copyright: hn Copyright (c) 2012</p>
 * <p>Company: hn</p>
 *
 * @author jack.lee E-mail: titans.lee@gmail.com
 * @version 创建时间：2012-8-15 上午10:16:29
 *          类说明:完成全局的
 */
public class ServiceManageApplication extends Application {
    static ServiceManageApplication mApp;

    //百度MapAPI的管理类
    public BMapManager mBMapMan = null;

    // 授权Key
    // TODO: 请输入您的Key,
    // 申请地址：http://dev.baidu.com/wiki/static/imap/key/
    public String mStrKey = "B7372D1E5AFA57A8053CDEDF41D868ADB7DAD578";
    boolean m_bKeyRight = true;    // 授权Key正确，验证通过

    // 常用事件监听，用来处理通常的网络错误，授权验证错误等
    public static class MyGeneralListener implements MKGeneralListener {
        @Override
        public void onGetNetworkState(int iError) {
            Log.d("MyGeneralListener", "onGetNetworkState error is " + iError);
            Toast.makeText(ServiceManageApplication.mApp.getApplicationContext(), "您的网络出错啦！",
                    Toast.LENGTH_LONG).show();
        }

        @Override
        public void onGetPermissionState(int iError) {
            Log.d("MyGeneralListener", "onGetPermissionState error is " + iError);
            if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
                // 授权Key错误：
                Toast.makeText(ServiceManageApplication.mApp.getApplicationContext(),
                        "请在ServiceManageApplication.java文件输入正确的授权Key！",
                        Toast.LENGTH_LONG).show();
                ServiceManageApplication.mApp.m_bKeyRight = false;
            }
        }
    }

    @Override
    public void onCreate() {
        Log.v("BMapApiDemoApp", "onCreate");
        mApp = this;
        mBMapMan = new BMapManager(this);
        mBMapMan.init(this.mStrKey, new MyGeneralListener());
        mBMapMan.getLocationManager().setNotifyInternal(10, 5);

        //-----------------------------------------------------
        //初始化基础数据（问题类型和问题类别，报销类型）
        //费用报销

        try {
            CostDB costDB = new CostDB(this);
            if (costDB.getCost().size() == 0) {
                ArrayList<CostEntity> costList = InitialAppData.getCostData(this.getResources().openRawResource(R.raw.cost));
                costDB.batchInsert(costList);
            }

            //问题类型
            QTypeDB qTypeDB = new QTypeDB(this);
            if (qTypeDB.getCountData() == 0) {
                ArrayList<Q_Type> qTypeList = InitialAppData.getQTypeData(this.getResources().openRawResource(R.raw.question_type));
                qTypeDB.batchInsert(qTypeList);
            }

            //问题类别
            QCategoryDB qCategoryDB = new QCategoryDB(this);
            if (qCategoryDB.getQ_Category().size() == 0) {
                ArrayList<Q_Category> qCategoryList = InitialAppData.getQCategoryData(this.getResources().openRawResource(R.raw.question_category));
                qCategoryDB.batchInsert(qCategoryList);
            }

            //产品信息
            ProductDB productDB = new ProductDB(this);
            if (productDB.getAllProduct().size() == 0) {
                ArrayList<ProductEntity> productList = InitialAppData.getProductData(this.getResources().openRawResource(R.raw.product_list));
                productDB.batchInsert(productList);
            }

        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onCreate();
    }

    @Override
    public void onTerminate() {
        if (mBMapMan != null) {
            mBMapMan.destroy();
            mBMapMan = null;
        }
        super.onTerminate();
    }
}
