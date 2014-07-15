package com.sealion.serviceassistant;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**   
* 放大显示现场拍的照片
*/
public class MaxImageActivity extends Activity
{
	private TextView top_bar_text = null;
	private ImageView back_image = null;
	private ImageView max_image = null;
	private Bitmap image_bm = null;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.order_img_view);
		
		top_bar_text = (TextView)this.findViewById(R.id.top_bar_title);
		back_image = (ImageView)this.findViewById(R.id.back_image);
		top_bar_text.setText("放大图片");
		back_image.setBackgroundResource(R.drawable.back_btn);
		
		back_image.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
		
		String path = this.getIntent().getExtras().getString("path");
		
		max_image = (ImageView)this.findViewById(R.id.max_image);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		options.inPurgeable = true;
		options.inInputShareable = true;
        image_bm = BitmapFactory.decodeFile(path,options);
        max_image.setImageBitmap(image_bm);
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		if (image_bm != null)
		{
			image_bm.recycle();
		}
	}
}
