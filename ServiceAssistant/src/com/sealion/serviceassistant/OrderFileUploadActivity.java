package com.sealion.serviceassistant;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.sealion.serviceassistant.adapter.OrderListAdapter;
import com.sealion.serviceassistant.db.FileManageDB;
import com.sealion.serviceassistant.entity.FileEntity;
import com.sealion.serviceassistant.entity.OrderEntity;
import com.sealion.serviceassistant.superactivity.OrderActivity;
import com.sealion.serviceassistant.tools.FileTools;
import com.sealion.serviceassistant.tools.NetworkTool;

/**
 * 上传所拍摄的图片和录制的音频文件，入口在DesktopActivity
 */
public class OrderFileUploadActivity extends OrderActivity
{
	private static final String path_prefix = "/mnt/sdcard/sound_recorder/";

	private TextView upload_tips = null;
	private ListView filelist = null;
	private FileManageDB fmDB = null;
	private ProgressBar pb = null;

	private boolean upload_sign = false; // 标识当前是否有文件在上传 true:正在上传，false:无上传

	private String upload_socket_address = "";
	private int port;

	private Button upload_btn = null;

	private ArrayList<OrderEntity> orderList = null;
	private OrderListAdapter oListAdapter = null;

	private ImageView back_image;
	private TextView top_bar_title;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.file_upload);

		filelist = (ListView) this.findViewById(R.id.filelist);
		upload_tips = (TextView) this.findViewById(R.id.upload_tips);
		pb = (ProgressBar) this.findViewById(R.id.rectangleProgressBar);
		upload_btn = (Button) this.findViewById(R.id.upload_btn);
		top_bar_title = (TextView) this.findViewById(R.id.top_bar_title);
		top_bar_title.setText("上传文件");
		back_image = (ImageView) this.findViewById(R.id.back_image);
		back_image.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				finish();
			}
		});

		orderList = olDB.getDataByNoUpload();

		fmDB = new FileManageDB(this);
		int[] count = fmDB.countUnUpLoadFiles();
		upload_tips.setText("当前有 " + orderList.size() + " 个工单存在文件未上传，其中有 " + count[0] + " 张图片," + count[1] + "个音频。");

		if (orderList.size() == 0)
		{
			upload_btn.setVisibility(View.GONE);
		}

		oListAdapter = new OrderListAdapter(this, orderList);
		filelist.setAdapter(oListAdapter);

		filelist.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3)
			{
				if (upload_sign)
				{
					Toast.makeText(OrderFileUploadActivity.this, "当前有文件正在上传，请等待...", Toast.LENGTH_LONG).show();
				}
				else
				{
					if (NetworkTool.getNetType(OrderFileUploadActivity.this) == 0)
					{
						OrderEntity oEntity = (OrderEntity) filelist.getItemAtPosition(arg2);

						ArrayList<FileEntity> fileList = fmDB.getDataByOrderNum(oEntity.getWorkCardId() + "");
						if (fileList.size() > 0)
						{
							Toast.makeText(OrderFileUploadActivity.this, "当前工单总共有 " + fileList.size() + " 个文件需要上传", Toast.LENGTH_LONG).show();
							pb.setVisibility(View.VISIBLE);
							upload_sign = true;
							UploadTask dTask = new UploadTask();
							dTask.execute(fileList);
						}
						else
						{
							upload_tips.setText("当前没有需要上传的文件");
							upload_btn.setVisibility(View.GONE);
						}
					}
					else
					{
						upload_tips.setText("当前没有需要上传的文件");
					}
				}
			}

		});

		upload_socket_address = getResources().getString(R.string.upload_socket_address);
		port = Integer.parseInt(getResources().getString(R.string.upload_socket_port));
	}

	public void AllUploadClick(View target)
	{
		if (upload_sign)
		{
			Toast.makeText(this, "当前有文件正在上传，请等待...", Toast.LENGTH_LONG).show();
		}
		else
		{
			if (NetworkTool.getNetType(this) == 0)
			{
				ArrayList<FileEntity> fileList = fmDB.getAllUnUploadData();
				if (fileList.size() > 0)
				{
					upload_sign = true;
					pb.setVisibility(View.VISIBLE);
					UploadTask dTask = new UploadTask();
					dTask.execute(fileList);
				}
				else
				{
					upload_tips.setText("当前没有需要上传的文件");
				}
			}
			else
			{
				Toast.makeText(this, "当前网络不是WIFI环境，不能上传", Toast.LENGTH_LONG).show();
			}
		}
	}

	class UploadTask extends AsyncTask<ArrayList<FileEntity>, Integer, Integer>
	{
		// 后面尖括号内分别是参数（例子里是线程休息时间），进度(publishProgress用到)，返回值 类型
		private int percent = 0;

		@Override
		protected void onPreExecute()
		{
			// 第一个执行方法
			super.onPreExecute();
		}

		@Override
		protected Integer doInBackground(ArrayList<FileEntity>... params)
		{
			int result = 0;
			int length = params[0].size();
			percent = 100 / length;
			FileEntity entity = null;
			// 第二个执行方法,onPreExecute()执行完后执行
			for (int i = 0; i < length; i++)
			{
				entity = params[0].get(i);
				String file_type = "";
				if (entity.getFilename().contains("jpg") || entity.getFilename().contains("jpeg") || entity.getFilename().contains("png"))
				{
					file_type = "image";
				}
				else
				{
					file_type = "audio";
				}
				
				// socket上传
				try
				{
					FileTools.UploadFileBySocket(upload_socket_address, port, 
							new File(path_prefix + entity.getOrder_num() + "/", 
							entity.getFilename()), entity.getOrder_num(),
							file_type, sp.getString("USERNAME", ""));
					fmDB.updateUploadState(entity.getId());
					if (fmDB.countUnUpLoadFilesByOrderNum(entity.getOrder_num()) == 0)
					{
						olDB.updateUploadFinishType(entity.getOrder_num());
					}
					pb.setProgress((i + 1) * percent);
					publishProgress((i + 1) * percent);
					result = 0;
				}
				catch (UnknownHostException e)
				{
					result = 1;
					e.printStackTrace();
				}
				catch (IOException e)
				{
					result = 2;
					e.printStackTrace();
				}
				catch (Exception e)
				{
					result = 3;
					e.printStackTrace();
				}
			}
			upload_sign = false;
			return result;
		}

		@Override
		protected void onProgressUpdate(Integer... progress)
		{
			// 这个函数在doInBackground调用publishProgress时触发，虽然调用时只有一个参数
			// 但是这里取到的是一个数组,所以要用progesss[0]来取值
			// 第n个参数就用progress[n]来取值
			upload_tips.setText("当前上传第" + progress[0] / percent + "个文件");
			super.onProgressUpdate(progress);
		}

		@Override
		protected void onPostExecute(Integer result)
		{
			super.onPostExecute(result);
			// doInBackground返回时触发，换句话说，就是doInBackground执行完后触发
			// 这里的result就是上面doInBackground执行后的返回值，所以这里是"执行完毕"
			if (result == 0)
			{
				upload_tips.setText("当前执行完毕！");
				pb.setVisibility(View.GONE);
				upload_btn.setVisibility(View.GONE);
				refreshListView();
			}
			else if (result == 1)
			{
				upload_tips.setText("无法登陆服务器！");
			}
			else if (result == 2)
			{
				upload_tips.setText("读取文件错误，或者无法登陆服务器传送文件！");
			}
			else if (result == 3)
			{
				upload_tips.setText("其他错误！");
			}
		}

	}

	private void refreshListView()
	{
		if (olDB != null)
		{
			orderList = olDB.getDataByNoUpload();
			oListAdapter = new OrderListAdapter(this, orderList);
			filelist.setAdapter(oListAdapter);
			oListAdapter.notifyDataSetChanged();
		}
	}
}
