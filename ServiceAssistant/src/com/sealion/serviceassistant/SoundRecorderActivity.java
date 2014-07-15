package com.sealion.serviceassistant;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ¼�����Ž���.
 */
public class SoundRecorderActivity extends Activity
{
	private Button btnStop;
	private Button btnPlay;
	private SeekBar seekbar1;

	private String path;

	private MediaPlayer player;

	private ImageView back_image;
	private TextView top_bar_title;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recordmedia);

		path = this.getIntent().getExtras().getString("path");

		setupViews();

		init();
		setListener();
		
		back_image = (ImageView)this.findViewById(R.id.back_image);
		back_image.setOnClickListener(new View.OnClickListener()
		{			
			@Override
			public void onClick(View v)
			{
				dialog();
			}
		});
		
		top_bar_title = (TextView)this.findViewById(R.id.top_bar_title);
		top_bar_title.setText("������Ƶ");
	}

	private void init()
	{
		if (path == null || path.equals(""))
		{
			btnStop.setEnabled(false);
			btnPlay.setEnabled(false);
			Toast.makeText(this, "���ŵ��ļ�������.", Toast.LENGTH_LONG).show();
		}
		else
		{
			player = MediaPlayer.create(this, Uri.parse(path));
			seekbar1.setMax(player.getDuration());
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
		{
			dialog();
			return true;
		}
		return false;
	}

	private void setupViews()
	{
		btnStop = (Button) findViewById(R.id.button2);
		btnPlay = (Button) findViewById(R.id.button1);
		seekbar1 = (SeekBar) findViewById(R.id.seekbar1);
		
	}

	Handler handler = new Handler();
	Runnable updateThread = new Runnable()
	{
		public void run()
		{
			// ��ø������ڲ���λ�ò����óɲ��Ž�������ֵ
			seekbar1.setProgress(player.getCurrentPosition());
			// ÿ���ӳ�100�����������߳�
			handler.postDelayed(updateThread, 100);
		}
	};

	private void setListener()
	{
		btnPlay.setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				player.start();
				// ����
				handler.post(updateThread);
			}
		});
		btnStop.setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				player.stop();
				// ȡ���߳�
				handler.removeCallbacks(updateThread);
			}
		});
		
		seekbar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
		{
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
			{
				// fromUser�ж����û��ı�Ļ����ֵ
				if (fromUser == true)
				{
					player.seekTo(progress);
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar)
			{
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar)
			{
				
			}
		});
	}

	protected void dialog()
	{
		if (player != null && player.isPlaying())
		{
			final AlertDialog.Builder builder = new Builder(SoundRecorderActivity.this);
			builder.setMessage("���ڲ��ţ�ȷ��Ҫ�˳���?");
			builder.setTitle("��ʾ");
			builder.setPositiveButton("ȷ��", new android.content.DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					if (player != null && player.isPlaying())
					{
						player.stop();
					}
					dialog.dismiss();
					
					finish();
				}
			});
			builder.setNegativeButton("ȡ��", new android.content.DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					dialog.dismiss();
				}
			});
			builder.create().show();
		}
		else
		{
			finish();
		}
	}

}