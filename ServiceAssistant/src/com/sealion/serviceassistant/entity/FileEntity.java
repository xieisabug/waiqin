package com.sealion.serviceassistant.entity;

/**
 * 文件实例
 */
public class FileEntity
{
    /**
     * id
     */
	private int id;
    /**
     * 工单号
     */
	private String order_num; //工单号
    /**
     * 文件名
     */
	private String filename; //文件名
    /**
     * 文件路径
     */
	private String filepath; //文件路径
	//private String filesize; //文件大小
    /**
     * 文件状态
     * 0：未上传 1：上传完毕
     */
	private int filestate; //文件状态（0：未上传，1：上传完毕）
	//private int whatfragment; //当前上传到第几块
	//private int filefragment; //文件块数
    /**
     * 文件类型
     * 0：图片 1：音频
     */
	private int filetype; //文件类型 0:图片，1：音频
	
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public String getOrder_num()
	{
		return order_num;
	}
	public void setOrder_num(String order_num)
	{
		this.order_num = order_num;
	}
	public String getFilename()
	{
		return filename;
	}
	public void setFilename(String filename)
	{
		this.filename = filename;
	}
	
	public int getFilestate()
	{
		return filestate;
	}
	public void setFilestate(int filestate)
	{
		this.filestate = filestate;
	}

	public int getFiletype()
	{
		return filetype;
	}
	public void setFiletype(int filetype)
	{
		this.filetype = filetype;
	}
	public String getFilepath()
	{
		return filepath;
	}
	public void setFilepath(String filepath)
	{
		this.filepath = filepath;
	}
		
}
