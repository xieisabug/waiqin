package com.sealion.serviceassistant.entity;

import android.graphics.Bitmap;

/**
 * Ã½ÌåÊµÀý
 */
public class MediaAttributeEntity
{
	private Bitmap bitmap;
	private String filename;
	
	public Bitmap getBitmap()
	{
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap)
	{
		this.bitmap = bitmap;
	}
	public String getFilename()
	{
		return filename;
	}
	public void setFilename(String filename)
	{
		this.filename = filename;
	}
}
