package com.sealion.serviceassistant.entity;

/**
 * �ļ�ʵ��
 */
public class FileEntity
{
    /**
     * id
     */
	private int id;
    /**
     * ������
     */
	private String order_num; //������
    /**
     * �ļ���
     */
	private String filename; //�ļ���
    /**
     * �ļ�·��
     */
	private String filepath; //�ļ�·��
	//private String filesize; //�ļ���С
    /**
     * �ļ�״̬
     * 0��δ�ϴ� 1���ϴ����
     */
	private int filestate; //�ļ�״̬��0��δ�ϴ���1���ϴ���ϣ�
	//private int whatfragment; //��ǰ�ϴ����ڼ���
	//private int filefragment; //�ļ�����
    /**
     * �ļ�����
     * 0��ͼƬ 1����Ƶ
     */
	private int filetype; //�ļ����� 0:ͼƬ��1����Ƶ
	
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
