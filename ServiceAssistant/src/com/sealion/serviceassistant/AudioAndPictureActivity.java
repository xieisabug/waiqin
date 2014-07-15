/**   
 * @Title: AudioAndPictureActivity.java 
 * @Package com.sealion.serviceassistant 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author jack.lee titans.lee@gmail.com   
 * @date 2013-3-5 下午3:09:17 
 * @version V1.0
 * Copyright: Copyright (c)2012
 * Company: 湖南中恩通信技术有限公司
 */
package com.sealion.serviceassistant;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.sealion.serviceassistant.adapter.MediaGridAdapter;
import com.sealion.serviceassistant.db.FileManageDB;
import com.sealion.serviceassistant.db.OrderListDB;
import com.sealion.serviceassistant.entity.FileEntity;
import com.sealion.serviceassistant.entity.MediaAttributeEntity;
import com.sealion.serviceassistant.superactivity.OrderActivity;
import com.sealion.serviceassistant.tools.FileTools;

/**
 * 录音与图片处理的Activity.
 */
public class AudioAndPictureActivity extends OrderActivity
{
	private static String TAG = AudioAndPictureActivity.class.getSimpleName();
	private static final String path_prefix = "/mnt/sdcard/sound_recorder/";
	private MediaGridAdapter mgAdapter1 = null;
	private MediaGridAdapter mgAdapter2 = null;
	private MediaAttributeEntity maEntity = null;
	private String path = null;
	private TextView current_state_value = null; // 当前状态信息

	private String filename = "";

	private GridView image_gridview = null;
	private GridView audio_gridview = null;

	private ArrayList<MediaAttributeEntity> image_list = null;
	private ArrayList<MediaAttributeEntity> audio_list = null;

	private FileManageDB fmDB = null;
	private ImageView back_image = null;
	private WindowManager wm = null;
	private View view = null;

	private final int TAKE_PICTURE = 1;
	private final int TAKE_AUDIO = 2;

	private TextView top_bar_title = null;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.audio_and_picture);
		top_bar_title = (TextView) this.findViewById(R.id.top_bar_title);
		top_bar_title.setText(this.getResources().getString(R.string.conplete_panel_btn3));
		current_state_value = (TextView) this.findViewById(R.id.current_state_value);
		current_state_value.setText(getResources().getString(R.string.spot_solve_back_paper));

		fmDB = new FileManageDB(this);
		order_num = this.getIntent().getExtras().getString("order_num");

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
			if (entity.getFiletype() == 0) // 图片
			{
				path = path + entity.getFilename();
				image_bm = decodeBitmap(path);
				maEntity.setBitmap(image_bm);
				maEntity.setFilename(entity.getFilename());
				image_list.add(maEntity);
			}
			else if (entity.getFiletype() == 1) // 音频
			{
				maEntity.setBitmap(audio_bm);
				maEntity.setFilename(entity.getFilename());
				audio_list.add(maEntity);
			}
		}
		refreshImageGridView();

		refreshAudioGridView();

		// 第一次加载创建APP数据文件夹
		if (FileTools.hasSdcard())
		{
			FileTools.createPath(path_prefix);
		}
		else
		{
			Toast.makeText(this, "请插入SD卡.", Toast.LENGTH_LONG).show();
		}
		path = path_prefix + order_num + "/";

		back_image = (ImageView) this.findViewById(R.id.back_image);
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

	// 录音按钮事件
	public void RadioBtnClick(View target)
	{
		// 需要实现一个录音返回值
		Intent intent = new Intent(this, SoundRecorder.class);
		intent.putExtra("order_num", order_num);
		intent.putExtra("sign", 0);
		this.startActivityForResult(intent, TAKE_AUDIO);
	}

	// 拍照按钮事件
	public void PhotoBtnClick(View target)
	{
		filename = UUID.randomUUID().toString() + ".jpg";
		// /order_num/uuid.jpg
		if (FileTools.hasSdcard())
		{
			FileTools.createPath(path);

			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			// 将图片保存至SDcard，相机返回后直接在SDcard读取图片，这样可以解决获取的图片太小的问题。
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(path + filename)));
			startActivityForResult(intent, TAKE_PICTURE);
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{

		if (requestCode == TAKE_PICTURE)
		{
			if (resultCode == RESULT_OK)
			{

				Bitmap bm = decodeBitmap(path + filename);
				// 保存文件路径到数据库
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
		else if (requestCode == TAKE_AUDIO) // 录音文件
		{
			if (resultCode == RESULT_OK)
			{
				String filename = data.getExtras().getString("filename");
				if (filename.length() > 1)
				{
					String[] name = filename.split(",");
					
					for (String s : name)
					{
						// 保存文件路径到数据库
						FileEntity fileEntity = new FileEntity();
						fileEntity.setFilename(s);
						fileEntity.setFilepath(path);
						fileEntity.setFiletype(1);
						fileEntity.setFilestate(0);
						fileEntity.setOrder_num(order_num);

						maEntity = new MediaAttributeEntity();
						Bitmap bp2 = BitmapFactory.decodeResource(this.getResources(), R.drawable.audio_bg);
						maEntity.setBitmap(bp2);
						maEntity.setFilename(s);
						audio_list.add(maEntity);

						fmDB.insert(fileEntity);

						refreshAudioGridView();
					}
				}

			}
		}
	}

	private void createFloatWin()
	{
		if (wm == null && view == null)
		{
			wm = (WindowManager) getApplicationContext().getSystemService("window");
			WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();

			/**
			 * 以下都是WindowManager.LayoutParams的相关属性 具体用途请参考SDK文档
			 */
			wmParams.type = 2002; // 这里是关键，你也可以试试2003
			wmParams.format = 1;
			wmParams.gravity = Gravity.RIGHT | Gravity.BOTTOM;
			/**
			 * 这里的flags也很关键 代码实际是wmParams.flags |= FLAG_NOT_FOCUSABLE;
			 * 40的由来是wmParams的默认属性（32）+ FLAG_NOT_FOCUSABLE（8）
			 */
			wmParams.flags = 40;
			wmParams.width = LayoutParams.WRAP_CONTENT;
			wmParams.height = LayoutParams.WRAP_CONTENT;
			wmParams.y = 150;
			wmParams.x = Gravity.RIGHT;
			view = View.inflate(AudioAndPictureActivity.this, R.layout.pop_media_type_btn, null);

			wm.addView(view, wmParams); // 创建View
		}
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
			final String[] mItems = { "放大显示", "删除" };
			AlertDialog.Builder builder = new AlertDialog.Builder(AudioAndPictureActivity.this);
			builder.setTitle("请选择您要的操作");
			builder.setItems(mItems, new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int which)
				{

					// 点击后弹出窗口选择了第几项
					if (which == 0) // 放大
					{
						Intent intent = new Intent(AudioAndPictureActivity.this, MaxImageActivity.class);
						intent.putExtra("path", path);
						startActivity(intent);
						dialog.dismiss();
					}
					else if (which == 1) // 删除
					{
						dialog.dismiss();
						AlertDialog.Builder builder = new AlertDialog.Builder(AudioAndPictureActivity.this);
						builder.setTitle("是否确认删除");
						builder.setIcon(android.R.drawable.ic_dialog_info);
						builder.setPositiveButton(R.string.make_sure, new DialogInterface.OnClickListener()
						{

							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								fmDB.deleteByFilename(maEntity.getFilename());
								File file = new File(path);
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
								Toast.makeText(AudioAndPictureActivity.this, "删除已经取消!", Toast.LENGTH_LONG).show();
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
			final String[] mItems = { "播放", "删除" };
			AlertDialog.Builder builder = new AlertDialog.Builder(AudioAndPictureActivity.this);
			builder.setTitle("请选择您要的操作");
			builder.setItems(mItems, new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int which)
				{

					// 点击后弹出窗口选择了第几项
					if (which == 0) // 播放
					{
						dialog.dismiss();
						Intent intent = new Intent(AudioAndPictureActivity.this, SoundRecorderActivity.class);
						intent.putExtra("path", path);
						startActivity(intent);
					}
					else if (which == 1) // 删除
					{

						AlertDialog.Builder builder = new AlertDialog.Builder(AudioAndPictureActivity.this);
						builder.setTitle("是否确认删除");
						builder.setIcon(android.R.drawable.ic_dialog_info);
						builder.setPositiveButton(R.string.make_sure, new DialogInterface.OnClickListener()
						{

							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								fmDB.deleteByFilename(maEntity.getFilename());
								File file = new File(path);
								file.delete();

								Iterator<MediaAttributeEntity> iter = audio_list.iterator();
								while (iter.hasNext())
								{
									MediaAttributeEntity mEntity = iter.next();
									if (mEntity.getFilename().equals(maEntity.getFilename()))
									{
										iter.remove();
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
								Toast.makeText(AudioAndPictureActivity.this, "删除已经取消!", Toast.LENGTH_LONG).show();
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
		// 通过这个bitmap获取图片的宽和高
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		if (bitmap == null)
		{
			Log.d(TAG, "bitmap为空");
		}
		float realWidth = options.outWidth;
		float realHeight = options.outHeight;
		Log.d(TAG, "真实图片高度：" + realHeight + "宽度:" + realWidth);
		// 计算缩放比
		int scale = (int) ((realHeight > realWidth ? realHeight : realWidth) / 100);
		if (scale <= 0)
		{
			scale = 1;
		}
		options.inSampleSize = scale;
		options.inJustDecodeBounds = false;
		// 注意这次要把options.inJustDecodeBounds 设为 false,这次图片是要读取出来的。
		bitmap = BitmapFactory.decodeFile(path, options);
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Log.d(TAG, "缩略图高度：" + h + "宽度:" + w);
		return bitmap;
	}

	public void SaveBaseInfoBtnClick(View target)
	{
		Log.d(TAG, image_list.size()+"************&&&&&&&&&&&&&&&&&&&&&&"+audio_list.size());
		if ((image_list != null && image_list.size() > 0) || (audio_list != null && audio_list.size() > 0))
		{
			Log.d(TAG, "^&^&^&^&");
			OrderListDB olDB = new OrderListDB(this);			
			olDB.updateUploadTaskSign(order_num);
		}
		this.finish();
	}

	public void BackBtnClick(View target)
	{
		this.finish();
	}
}
