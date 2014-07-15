package com.sealion.serviceassistant.tools;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * <p>Title: UpdateApplication.java</p>
 * <p>Description: </p>
 * <p>Copyright: hn Copyright (c) 2012</p>
 * <p>Company: hn</p>
 *
 * @author jack.lee E-mail: titans.lee@gmail.com
 * @version 创建时间：2012-10-12 下午05:52:07
 *          类说明:完成程序的升级检查,版本的升级
 */
public class UpdateApplication {
    private String TAG = getClass().getSimpleName();
    private String url = "";
    private int newVerCode = 0;
    private String newVerName = "";
    private Context context;
    private ProgressDialog pBar = null;
    private Handler updateHandler = new Handler();

    public UpdateApplication(String url, Context context) {
        this.url = url;
        this.context = context;
    }

    /**
     * 获取服务器上的程序版本，用于升级程序判断使用
     *
     * @return 服务器上的程序版本
     */
    public int getServerVerCode() {
        try {
            String verjson = NetworkTool.getContent(url + "/" + Config.UPDATE_VERJSON);
            Log.d(TAG,"verjson" + verjson);
            if (!verjson.contains("Error 404 NOT_FOUND")) {
                JSONArray array = new JSONArray(verjson);
                if (array.length() > 0) {
                    JSONObject obj = array.getJSONObject(0);
                    try {
                        newVerCode = Integer.parseInt(obj.getString("verCode"));
                        newVerName = obj.getString("verName");
                    } catch (Exception e) {
                        newVerCode = -1;
                        newVerName = "";
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newVerCode;
    }

    /**
     * 新版本升级的对话框显示
     * 点击更新后进行系统升级文件下载
     */
    public void doNewVersionUpdate() {
        int verCode = Config.getVerCode(context);
        String verName = Config.getVerName(context);

        Dialog dialog = new AlertDialog.Builder(context).setTitle("软件更新")
                .setMessage("当前版本:" + verName + " Code:" + verCode + ", 发现新版本:" + newVerName + " Code:" + newVerCode + ", 是否更新?")
                        // 设置内容
                .setPositiveButton("更新",// 设置确定按钮
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                pBar = new ProgressDialog(context);
                                pBar.setTitle("正在下载");
                                pBar.setMessage("请稍候...");
                                pBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                downFile(url + "/" + Config.UPDATE_APKNAME);
                            }
                        }
                ).setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                }).create();// 创建
        // 显示对话框
        dialog.show();
    }

    /**
     * 下载文件
     *
     * @param url 下载指定的url
     */
    void downFile(final String url) {
        pBar.show();
        new Thread() {
            public void run() {
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(url);
                HttpResponse response;
                try {
                    response = client.execute(get);
                    HttpEntity entity = response.getEntity();
                    InputStream is = entity.getContent();
                    FileOutputStream fileOutputStream = null;
                    if (is != null) {

                        File file = new File(Environment.getExternalStorageDirectory(),
                                Config.UPDATE_SAVENAME);
                        fileOutputStream = new FileOutputStream(file);

                        byte[] buf = new byte[1024];
                        int ch;
                        while ((ch = is.read(buf)) != -1) {
                            fileOutputStream.write(buf, 0, ch);
                        }
                    }
                    if (fileOutputStream != null) {
                        fileOutputStream.flush();
                    }
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    down();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 下载完成后进行状态更新
     */
    void down() {
        updateHandler.post(new Runnable() {
            public void run() {
                pBar.cancel();
                update();
            }
        });
    }

    /**
     * 下载完成后进行程序的更新
     */
    void update() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                Config.UPDATE_SAVENAME)), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }
}
