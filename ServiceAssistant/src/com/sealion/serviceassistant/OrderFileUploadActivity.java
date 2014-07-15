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
 * �ϴ��������ͼƬ��¼�Ƶ���Ƶ�ļ��������DesktopActivity
 */
public class OrderFileUploadActivity extends OrderActivity
{
	private static final String path_prefix = "/mnt/sdcard/sound_recorder/";

	private TextView upload_tips = null;
	private ListView filelist = null;
	private FileManageDB fmDB = null;
	private ProgressBar pb = null;

	private boolean upload_sign = false; // ��ʶ��ǰ�Ƿ����ļ����ϴ� true:�����ϴ���false:���ϴ�

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
		top_bar_title.setText("�ϴ��ļ�");
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
		upload_tips.setText("��ǰ�� " + orderList.size() + " �����������ļ�δ�ϴ��������� " + count[0] + " ��ͼƬ," + count[1] + "����Ƶ��");

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
					Toast.makeText(OrderFileUploadActivity.this, "��ǰ���ļ������ϴ�����ȴ�...", Toast.LENGTH_LONG).show();
				}
				else
				{
					if (NetworkTool.getNetType(OrderFileUploadActivity.this) == 0)
					{
						OrderEntity oEntity = (OrderEntity) filelist.getItemAtPosition(arg2);

						ArrayList<FileEntity> fileList = fmDB.getDataByOrderNum(oEntity.getWorkCardId() + "");
						if (fileList.size() > 0)
						{
							Toast.makeText(OrderFileUploadActivity.this, "��ǰ�����ܹ��� " + fileList.size() + " ���ļ���Ҫ�ϴ�", Toast.LENGTH_LONG).show();
							pb.setVisibility(View.VISIBLE);
							upload_sign = true;
							UploadTask dTask = new UploadTask();
							dTask.execute(fileList);
						}
						else
						{
							upload_tips.setText("��ǰû����Ҫ�ϴ����ļ�");
							upload_btn.setVisibility(View.GONE);
						}
					}
					else
					{
						upload_tips.setText("��ǰû����Ҫ�ϴ����ļ�");
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
			Toast.makeText(this, "��ǰ���ļ������ϴ�����ȴ�...", Toast.LENGTH_LONG).show();
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
					upload_tips.setText("��ǰû����Ҫ�ϴ����ļ�");
				}
			}
			else
			{
				Toast.makeText(this, "��ǰ���粻��WIFI�����������ϴ�", Toast.LENGTH_LONG).show();
			}
		}
	}

	class UploadTask extends AsyncTask<ArrayList<FileEntity>, Integer, Integer>
	{
		// ����������ڷֱ��ǲ��������������߳���Ϣʱ�䣩������(publishProgress�õ�)������ֵ ����
		private int percent = 0;

		@Override
		protected void onPreExecute()
		{
			// ��һ��ִ�з���
			super.onPreExecute();
		}

		@Override
		protected Integer doInBackground(ArrayList<FileEntity>... params)
		{
			int result = 0;
			int length = params[0].size();
			percent = 100 / length;
			FileEntity entity = null;
			// �ڶ���ִ�з���,onPreExecute()ִ�����ִ��
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
				
				// socket�ϴ�
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
			// ���������doInBackground����publishProgressʱ��������Ȼ����ʱֻ��һ������
			// ��������ȡ������һ������,����Ҫ��progesss[0]��ȡֵ
			// ��n����������progress[n]��ȡֵ
			upload_tips.setText("��ǰ�ϴ���" + progress[0] / percent + "���ļ�");
			super.onProgressUpdate(progress);
		}

		@Override
		protected void onPostExecute(Integer result)
		{
			super.onPostExecute(result);
			// doInBackground����ʱ���������仰˵������doInBackgroundִ����󴥷�
			// �����result��������doInBackgroundִ�к�ķ���ֵ������������"ִ�����"
			if (result == 0)
			{
				upload_tips.setText("��ǰִ����ϣ�");
				pb.setVisibility(View.GONE);
				upload_btn.setVisibility(View.GONE);
				refreshListView();
			}
			else if (result == 1)
			{
				upload_tips.setText("�޷���½��������");
			}
			else if (result == 2)
			{
				upload_tips.setText("��ȡ�ļ����󣬻����޷���½�����������ļ���");
			}
			else if (result == 3)
			{
				upload_tips.setText("��������");
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
