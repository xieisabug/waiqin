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
 * <p>Description: �����Application�࣬���������ݵĳ�ʼ���Ͱٶȵ�ͼ�Ĺ������ʼ��</p>
 * <p>Copyright: hn Copyright (c) 2012</p>
 * <p>Company: hn</p>
 *
 * @author jack.lee E-mail: titans.lee@gmail.com
 * @version ����ʱ�䣺2012-8-15 ����10:16:29
 *          ��˵��:���ȫ�ֵ�
 */
public class ServiceManageApplication extends Application {
    static ServiceManageApplication mApp;

    //�ٶ�MapAPI�Ĺ�����
    public BMapManager mBMapMan = null;

    // ��ȨKey
    // TODO: ����������Key,
    // �����ַ��http://dev.baidu.com/wiki/static/imap/key/
    public String mStrKey = "B7372D1E5AFA57A8053CDEDF41D868ADB7DAD578";
    boolean m_bKeyRight = true;    // ��ȨKey��ȷ����֤ͨ��

    // �����¼���������������ͨ�������������Ȩ��֤�����
    public static class MyGeneralListener implements MKGeneralListener {
        @Override
        public void onGetNetworkState(int iError) {
            Log.d("MyGeneralListener", "onGetNetworkState error is " + iError);
            Toast.makeText(ServiceManageApplication.mApp.getApplicationContext(), "���������������",
                    Toast.LENGTH_LONG).show();
        }

        @Override
        public void onGetPermissionState(int iError) {
            Log.d("MyGeneralListener", "onGetPermissionState error is " + iError);
            if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
                // ��ȨKey����
                Toast.makeText(ServiceManageApplication.mApp.getApplicationContext(),
                        "����ServiceManageApplication.java�ļ�������ȷ����ȨKey��",
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
        //��ʼ���������ݣ��������ͺ�������𣬱������ͣ�
        //���ñ���

        try {
            CostDB costDB = new CostDB(this);
            if (costDB.getCost().size() == 0) {
                ArrayList<CostEntity> costList = InitialAppData.getCostData(this.getResources().openRawResource(R.raw.cost));
                costDB.batchInsert(costList);
            }

            //��������
            QTypeDB qTypeDB = new QTypeDB(this);
            if (qTypeDB.getCountData() == 0) {
                ArrayList<Q_Type> qTypeList = InitialAppData.getQTypeData(this.getResources().openRawResource(R.raw.question_type));
                qTypeDB.batchInsert(qTypeList);
            }

            //�������
            QCategoryDB qCategoryDB = new QCategoryDB(this);
            if (qCategoryDB.getQ_Category().size() == 0) {
                ArrayList<Q_Category> qCategoryList = InitialAppData.getQCategoryData(this.getResources().openRawResource(R.raw.question_category));
                qCategoryDB.batchInsert(qCategoryList);
            }

            //��Ʒ��Ϣ
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
