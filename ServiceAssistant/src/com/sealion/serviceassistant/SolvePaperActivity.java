package com.sealion.serviceassistant;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.lang.OutOfMemoryError;
import org.json.JSONException;
import org.json.JSONObject;

import com.sealion.serviceassistant.adapter.MediaGridAdapter;
import com.sealion.serviceassistant.db.CommonOrderComplishDB;
import com.sealion.serviceassistant.db.FileManageDB;
import com.sealion.serviceassistant.entity.CommonOrderComplishEntity;
import com.sealion.serviceassistant.entity.FileEntity;
import com.sealion.serviceassistant.entity.MediaAttributeEntity;
import com.sealion.serviceassistant.entity.NetBackDataEntity;
import com.sealion.serviceassistant.entity.OrderStepEntity;
import com.sealion.serviceassistant.exception.NetAccessException;
import com.sealion.serviceassistant.net.HttpRestAchieve;
import com.sealion.serviceassistant.superactivity.OrderActivity;
import com.sealion.serviceassistant.tools.DateTools;
import com.sealion.serviceassistant.tools.FileTools;
import com.sealion.serviceassistant.tools.FinalVariables;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * �ֳ��������.
 */
public class SolvePaperActivity extends OrderActivity
{
	private static final String TAG = SolvePaperActivity.class.getSimpleName();

	private static final String path_prefix = "/mnt/sdcard/side_manage/";

	private final int TAKE_PICTURE = 1;
	private final int TAKE_AUDIO = 2;

	private WindowManager wm = null;
	private View view = null;
	private TextView top_bar_title = null;
	private TextView current_state_value = null; // ��ǰ״̬��Ϣ
	private GridView image_gridview = null;
	private GridView audio_gridview = null;

	private ArrayList<MediaAttributeEntity> image_list = null;
	private ArrayList<MediaAttributeEntity> audio_list = null;

	private MediaGridAdapter mgAdapter1 = null;
	private MediaGridAdapter mgAdapter2 = null;

	private FileManageDB fmDB = null;
	private MediaAttributeEntity maEntity = null;

	private String path = null;
	
	private String filename = "";
	
	private EditText order_paper_value = null;
	private RadioButton is_solve_yes = null;
	private RadioButton is_solve_no = null;
	private EditText order_qdescribe_value = null;
	private EditText order_qsolve_value = null;
	private EditText order_software_version_value = null;
	private EditText order_customer_sign_value = null;
	private EditText order_back_remark_value = null;
	private TextView order_arrive_time_value = null;
	private TextView order_complish_time_value = null;
	private TextView order_service_time_value = null;
	
	private ImageView back_image = null;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.solve_back_paper);

		top_bar_title = (TextView) this.findViewById(R.id.top_bar_title);
		current_state_value = (TextView) this.findViewById(R.id.current_state_value);
		current_state_value.setText(getResources().getString(R.string.spot_solve_back_paper));

		Bundle bundle = this.getIntent().getExtras();
		order_num = bundle.getString("order_num");
		top_bar_title.setText("������  " + order_num);
		order_customer_mobile_value_text = bundle.getString("order_customer_mobile_value_text");
		order_customer_phone_value_text = bundle.getString("order_customer_phone_value_text");
		customer_address = bundle.getString("customer_address");
		
		order_paper_value = (EditText)this.findViewById(R.id.order_paper_value);
		is_solve_yes = (RadioButton)this.findViewById(R.id.is_solve_yes);
		is_solve_no = (RadioButton)this.findViewById(R.id.is_solve_no);
		order_qdescribe_value = (EditText)this.findViewById(R.id.order_qdescribe_value);
		order_qsolve_value = (EditText)this.findViewById(R.id.order_qsolve_value);
		order_software_version_value = (EditText)this.findViewById(R.id.order_software_version_value);
		order_customer_sign_value = (EditText)this.findViewById(R.id.order_customer_sign_value);
		order_back_remark_value = (EditText)this.findViewById(R.id.order_back_remark_value);
		order_arrive_time_value = (TextView)this.findViewById(R.id.order_arrive_time_value);
		order_complish_time_value = (TextView)this.findViewById(R.id.order_complish_time_value);
		order_service_time_value = (TextView)this.findViewById(R.id.order_service_time_value);
		
		image_gridview = (GridView) this.findViewById(R.id.image_gridview);
		image_list = new ArrayList<MediaAttributeEntity>();
		mgAdapter1 = new MediaGridAdapter(this, image_list);
		image_gridview.setAdapter(mgAdapter1);
		image_gridview.setOnItemClickListener(new ImageItemClickListener());

		audio_gridview = (GridView) this.findViewById(R.id.audio_gridview);
		audio_list = new ArrayList<MediaAttributeEntity>();
		mgAdapter2 = new MediaGridAdapter(this, audio_list);
		audio_gridview.setAdapter(mgAdapter2);
		audio_gridview.setOnItemClickListener(new AudioItemClickListener());

		fmDB = new FileManageDB(this);

		ArrayList<FileEntity> list = fmDB.getDataByOrderNum(order_num);

		for (FileEntity entity : list)
		{
			maEntity = new MediaAttributeEntity();
			Bitmap audio_bm = BitmapFactory.decodeResource(this.getResources(), R.drawable.audio_bg);
			Bitmap image_bm = null;
			String path = path_prefix + order_num + "/";
			if (entity.getFiletype() == 0) // ͼƬ
			{
				path = path + entity.getFilename();				
				image_bm = decodeBitmap(path);
				maEntity.setBitmap(image_bm);
				maEntity.setFilename(entity.getFilename());
				image_list.add(maEntity);
			}
			else if (entity.getFiletype() == 1) // ��Ƶ
			{
				maEntity.setBitmap(audio_bm);
				maEntity.setFilename(entity.getFilename());
				audio_list.add(maEntity);
			}
		}
		refreshImageGridView();

		refreshAudioGridView();

		// ��һ�μ��ش���APP�����ļ���
		if (FileTools.hasSdcard())
		{
			FileTools.createPath(path_prefix);
		}
		else
		{
			Toast.makeText(this, "�����SD��.", Toast.LENGTH_LONG).show();
		}
		path = path_prefix + order_num + "/";
		
		back_image = (ImageView)this.findViewById(R.id.back_image);
		back_image.setOnClickListener(new View.OnClickListener()
		{			
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
	}

	@Override
	public void onStart()
	{
		super.onStart();
	}

	@Override
	public void onResume()
	{
		super.onResume();
		createFloatWin();
	}

	@Override
	public void onStop()
	{
		super.onStop();
		if (wm != null && view != null)
		{
			wm.removeView(view);
			wm = null;
			view = null;
		}
	}

	private void createFloatWin()
	{
		if (wm == null && view == null)
		{
			wm = (WindowManager) getApplicationContext().getSystemService("window");
			WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();

			/**
			 * ���¶���WindowManager.LayoutParams��������� ������;��ο�SDK�ĵ�
			 */
			wmParams.type = 2002; // �����ǹؼ�����Ҳ��������2003
			wmParams.format = 1;
			wmParams.gravity = Gravity.RIGHT | Gravity.BOTTOM;
			/**
			 * �����flagsҲ�ܹؼ� ����ʵ����wmParams.flags |= FLAG_NOT_FOCUSABLE;
			 * 40��������wmParams��Ĭ�����ԣ�32��+ FLAG_NOT_FOCUSABLE��8��
			 */
			wmParams.flags = 40;
			wmParams.width = LayoutParams.WRAP_CONTENT;
			wmParams.height = LayoutParams.WRAP_CONTENT;
			wmParams.y = 150;
			wmParams.x = Gravity.RIGHT;
			view = View.inflate(SolvePaperActivity.this, R.layout.pop_media_type_btn, null);

			wm.addView(view, wmParams); // ����View
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		
		if (requestCode == TAKE_PICTURE)
		{
			if (resultCode == RESULT_OK)
			{
				
				Bitmap bm  = decodeBitmap(path + filename);
				// �����ļ�·�������ݿ�
				FileEntity fileEntity = new FileEntity();
				fileEntity.setFilename(filename);
				fileEntity.setFilepath(path);
				fileEntity.setFiletype(0);
				fileEntity.setFilestate(0);
				fileEntity.setOrder_num(order_num);
				fmDB.insert(fileEntity);

				maEntity = new MediaAttributeEntity();
				maEntity.setBitmap(bm);
				maEntity.setFilename(filename);
				image_list.add(maEntity);

				refreshImageGridView();
				
			}
		}
		else if (requestCode == TAKE_AUDIO) // ¼���ļ�
		{
			if (resultCode == RESULT_OK)
			{
				String filename = data.getExtras().getString("filename");
				if (filename.length() > 1)
				{
					String[] name = filename.split(",");
					for(String s : name)
					{
						// �����ļ�·�������ݿ�
						FileEntity fileEntity = new FileEntity();
						fileEntity.setFilename(s);
						fileEntity.setFilepath(path);
						fileEntity.setFiletype(1);
						fileEntity.setFilestate(0);
						fileEntity.setOrder_num(order_num);

						maEntity = new MediaAttributeEntity();
						Bitmap bp2 = BitmapFactory.decodeResource(this.getResources(), R.drawable.audio_bg);
						maEntity.setBitmap(bp2);
						maEntity.setFilename(filename);
						audio_list.add(maEntity);

						fmDB.insert(fileEntity);

						refreshAudioGridView();
					}
				}
				
			}
		}
	}

	// ¼����ť�¼�
	public void RadioBtnClick(View target)
	{
		// ��Ҫʵ��һ��¼������ֵ
		Intent intent = new Intent(this, SoundRecorderActivity.class);
		intent.putExtra("order_num", order_num);
		intent.putExtra("sign", 0);
		this.startActivityForResult(intent, TAKE_AUDIO);
	}

	// ���հ�ť�¼�
	public void PhotoBtnClick(View target)
	{
		filename = UUID.randomUUID().toString() + ".jpg";
		// /order_num/uuid.jpg
		if (FileTools.hasSdcard())
		{
			FileTools.createPath(path);
			
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  
	        //��ͼƬ������SDcard��������غ�ֱ����SDcard��ȡͼƬ���������Խ����ȡ��ͼƬ̫С�����⡣  
	        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(path + filename)));  
	        startActivityForResult(intent, TAKE_PICTURE); 
		}		 
	}

	// ��ɹ���
	public void ComplishBtnClick(View target)
	{
		// ���¹���״̬Ϊ���,�������ֳ����״̬
		if ((image_list != null && image_list.size() > 0) || (audio_list != null && audio_list.size() > 0))
		{
			olDB.updateFinishType(order_num, FinalVariables.ORDER_FINISH_TYPE_SPOT_SOLVE);
		}
		else
		{
			olDB.updateFinishType(order_num, FinalVariables.ORDER_FINISH_TYPE_SPOT_SOLVE);
		}
		
		// д�빤����ǰ����״̬���������ݿ�
		OrderStepEntity osEntity = new OrderStepEntity();
		osEntity.setOper_time(DateTools.getFormatDateAndTime());
		osEntity.setOrder_num(order_num);
		osEntity.setOrder_state(FinalVariables.ORDER_STATE_ORDER_COMPLISH); // ��ɹ���
		odListDB.insert(osEntity);
		
		//��ɹ���
		CommonOrderComplishDB cocDB = new CommonOrderComplishDB(this);
		CommonOrderComplishEntity cocEntity = new CommonOrderComplishEntity();
		
		String text_order_paper_value = order_paper_value.getText().toString();
		String text_order_qdescribe_value = order_qdescribe_value.getText().toString();
		String text_order_qsolve_value = order_qsolve_value.getText().toString();
		String text_order_software_version_value = order_software_version_value.getText().toString();
		String text_order_customer_sign_value = order_customer_sign_value.getText().toString();
		String text_order_back_remark_value = order_back_remark_value.getText().toString();
		String text_order_arrive_time_value = order_arrive_time_value.getText().toString();
		String text_order_complish_time_value = order_complish_time_value.getText().toString();
		String text_order_service_time_value = order_service_time_value.getText().toString();
		
		if (text_order_paper_value == null || text_order_paper_value.equals(""))
		{
			Toast.makeText(this, "", Toast.LENGTH_LONG).show();
		}
		
		if (text_order_qdescribe_value == null || text_order_qdescribe_value.equals(""))
		{
			Toast.makeText(this, "", Toast.LENGTH_LONG).show();
		}
		
		if (text_order_qsolve_value == null || text_order_qsolve_value.equals(""))
		{
			Toast.makeText(this, "", Toast.LENGTH_LONG).show();
		}
		
		if (text_order_software_version_value == null || text_order_software_version_value.equals(""))
		{
			Toast.makeText(this, "", Toast.LENGTH_LONG).show();
		}
		
		if (text_order_customer_sign_value == null || text_order_customer_sign_value.equals(""))
		{
			Toast.makeText(this, "", Toast.LENGTH_LONG).show();
		}
		
		if (text_order_back_remark_value == null || text_order_back_remark_value.equals(""))
		{
			Toast.makeText(this, "", Toast.LENGTH_LONG).show();
		}
		
		if (text_order_complish_time_value == null || text_order_complish_time_value.equals(""))
		{
			Toast.makeText(this, "", Toast.LENGTH_LONG).show();
		}
		if(is_solve_yes.isChecked())
		{
			
		}
		if(is_solve_no.isChecked())
		{
			
		}
		
	}

	public void CancelBtnClick(View target)
	{
		this.finish();
	}

	private void refreshImageGridView()
	{
		mgAdapter1 = new MediaGridAdapter(this, image_list);
		mgAdapter1.notifyDataSetChanged();
		image_gridview.setAdapter(mgAdapter1);
	}

	private void refreshAudioGridView()
	{
		mgAdapter2 = new MediaGridAdapter(this, audio_list);
		mgAdapter2.notifyDataSetChanged();
		audio_gridview.setAdapter(mgAdapter2);
	}
	
	class ImageItemClickListener implements OnItemClickListener
	{
		public void onItemClick(AdapterView<?> arg0,// The AdapterView where the
													// click happened
				View arg1,// The view within the AdapterView that was clicked
				int arg2,// The position of the view in the adapter
				long arg3// The row id of the item that was clicked
		)
		{
			ImageView imageView = (ImageView) arg1.findViewById(R.id.media_item_img);
			maEntity = (MediaAttributeEntity) imageView.getTag();
			final String path = path_prefix + order_num + "/" + maEntity.getFilename();
			final String[] mItems = { "�Ŵ���ʾ", "ɾ��" };
			AlertDialog.Builder builder = new AlertDialog.Builder(SolvePaperActivity.this);
			builder.setTitle("��ѡ����Ҫ�Ĳ���");
			builder.setItems(mItems, new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int which)
				{

					// ����󵯳�����ѡ���˵ڼ���
					if (which == 0) // �Ŵ�
					{
						Intent intent = new Intent(SolvePaperActivity.this, MaxImageActivity.class);
						intent.putExtra("path", path);
						startActivity(intent);
						dialog.dismiss();
					}
					else if (which == 1) // ɾ��
					{
						dialog.dismiss();
						AlertDialog.Builder builder = new AlertDialog.Builder(SolvePaperActivity.this);
						builder.setTitle("�Ƿ�ȷ��ɾ��");
						builder.setIcon(android.R.drawable.ic_dialog_info);
						builder.setPositiveButton(R.string.make_sure, new DialogInterface.OnClickListener()
						{

							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								fmDB.deleteByFilename(maEntity.getFilename());
								File file =new File(path);
								file.delete();
								for (MediaAttributeEntity mEntity : image_list)
								{
									if (mEntity.getFilename().equals(maEntity.getFilename()))
									{
										image_list.remove(mEntity);
										refreshImageGridView();
									}
								}
								dialog.dismiss();
							}
						});
						builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
						{

							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								dialog.dismiss();
								Toast.makeText(SolvePaperActivity.this, "ɾ���Ѿ�ȡ��!", Toast.LENGTH_LONG).show();
							}
						});
						
						builder.create().show();
					}

				}
			});
			builder.create().show();
		}

	}

	class AudioItemClickListener implements OnItemClickListener
	{
		public void onItemClick(AdapterView<?> arg0,// The AdapterView where the
													// click happened
				View arg1,// The view within the AdapterView that was clicked
				int arg2,// The position of the view in the adapter
				long arg3// The row id of the item that was clicked
		)
		{
			ImageView imageView = (ImageView) arg1.findViewById(R.id.media_item_img);
			maEntity = (MediaAttributeEntity) imageView.getTag();
			final String path = path_prefix + order_num + "/" + maEntity.getFilename();
			final String[] mItems = { "����", "ɾ��" };
			AlertDialog.Builder builder = new AlertDialog.Builder(SolvePaperActivity.this);
			builder.setTitle("��ѡ����Ҫ�Ĳ���");
			builder.setItems(mItems, new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int which)
				{

					// ����󵯳�����ѡ���˵ڼ���
					if (which == 0) // ����
					{
						dialog.dismiss();
						Intent intent = new Intent(SolvePaperActivity.this, SoundRecorderActivity.class);
						intent.putExtra("path", path);
						intent.putExtra("sign", 1);
						startActivity(intent);
					}
					else if (which == 1) // ɾ��
					{

						AlertDialog.Builder builder = new AlertDialog.Builder(SolvePaperActivity.this);
						builder.setTitle("�Ƿ�ȷ��ɾ��");
						builder.setIcon(android.R.drawable.ic_dialog_info);
						builder.setPositiveButton(R.string.make_sure, new DialogInterface.OnClickListener()
						{

							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								fmDB.deleteByFilename(maEntity.getFilename());
								File file =new File(path);
								file.delete();
								for (MediaAttributeEntity mEntity : audio_list)
								{
									if (mEntity.getFilename().equals(maEntity.getFilename()))
									{
										audio_list.remove(mEntity);
										refreshAudioGridView();
									}
								}
								dialog.dismiss();
							}
						});
						builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
						{

							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								dialog.dismiss();
								Toast.makeText(SolvePaperActivity.this, "ɾ���Ѿ�ȡ��!", Toast.LENGTH_LONG).show();
							}
						});
						builder.create().show();
					}					
				}
			});
			builder.create().show();
		}

	}
	
	public Bitmap decodeBitmap(String path)  
    {  
        BitmapFactory.Options options = new BitmapFactory.Options();  
        options.inJustDecodeBounds = true;  
        // ͨ�����bitmap��ȡͼƬ�Ŀ�͸�         
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);  
        if (bitmap == null)  
        {  
            Log.d(TAG, "bitmapΪ��");  
        }  
        float realWidth = options.outWidth;  
        float realHeight = options.outHeight;  
        Log.d(TAG, "��ʵͼƬ�߶ȣ�" + realHeight + "���:" + realWidth);  
        // �������ű�         
        int scale = (int) ((realHeight > realWidth ? realHeight : realWidth) / 100);  
        if (scale <= 0)  
        {  
            scale = 1;  
        }  
        options.inSampleSize = scale;  
        options.inJustDecodeBounds = false;  
        // ע�����Ҫ��options.inJustDecodeBounds ��Ϊ false,���ͼƬ��Ҫ��ȡ�����ġ�        
        bitmap = BitmapFactory.decodeFile(path, options);  
        int w = bitmap.getWidth();  
        int h = bitmap.getHeight();  
        Log.d(TAG, "����ͼ�߶ȣ�" + h + "���:" + w);  
        return bitmap;  
    }  
	
	//�ύ�ֳ������������
	private class PostSolveData extends AsyncTask<String,Void,Integer>
	{
		@Override
		protected void onPreExecute()
		{
			ShowDialog();
		}
		
		@Override
		protected Integer doInBackground(String... params)
		{
			int back_data = 0;
			HttpRestAchieve httpRestAchieve = new HttpRestAchieve();
			HashMap<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.put("workerNo", params[0]);
			paramsMap.put("workOrderNo", params[1]);
			paramsMap.put("acceptTime", params[2]);
			try
			{
				
				NetBackDataEntity netBackData = httpRestAchieve.postRequestData(request_url + "/fieldworker/biz/accept", paramsMap);
				String data = netBackData.getData();
				JSONObject jsonObject = new JSONObject(data);
				int result = jsonObject.getInt("resultCode");								
				if (result == 1)
				{					
					back_data = 0;
				}
				else
				{
					back_data = 1;
					
				}				
			}
			catch (NotFoundException e)
			{
				back_data = 2;
				e.printStackTrace();
				Log.d(TAG, e.getMessage());
			}
			catch (NetAccessException e)
			{
				back_data = 3;
				e.printStackTrace();
				Log.d(TAG, e.getMessage());
			}
			catch (JSONException e)
			{
				back_data = 4;
				e.printStackTrace();
				Log.d(TAG, e.getMessage());
			}
			return back_data;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			
			//doInBackground����ʱ���������仰˵������doInBackgroundִ����󴥷�
			//�����result��������doInBackgroundִ�к�ķ���ֵ������������"ִ�����"
			CancelDialog();
			
			if (result == 0)
			{
				
			}
			else if (result == 1)
			{
				Toast.makeText(SolvePaperActivity.this, "����ʧ�ܣ������ԣ�", Toast.LENGTH_LONG).show();
			}
			else if (result == 2)
			{
				Toast.makeText(SolvePaperActivity.this, "����������", Toast.LENGTH_LONG).show();
			}
			else if (result == 3)
			{
				Toast.makeText(SolvePaperActivity.this, "����ʧ��,�����쳣��", Toast.LENGTH_LONG).show();
			}
			else if (result == 4)
			{
				Toast.makeText(SolvePaperActivity.this, "����ʧ�ܣ����ݽ����쳣��", Toast.LENGTH_LONG).show();
			}
		}
	}
}
