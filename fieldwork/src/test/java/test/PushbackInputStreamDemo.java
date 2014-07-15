package test;

import java.io.*;

public class PushbackInputStreamDemo
{
	public static void main(String[] args)
	{
		System.out.println(System.getProperty("file.encoding"));
		try
		{
			PushbackInputStream pushbackInputStream =
				new PushbackInputStream(new FileInputStream("D:/f1"));

			byte[] array = new byte[2];

			int tmp = 0;
			int count = 0;

			while((count = pushbackInputStream.read(array))!=-1)
			{
				//两个字节转换为整数 
				tmp = (short)((array[0] << 8) | (array[1] & 0xff));
				tmp = tmp & 0xFFFF;

				//判断是否为BIG5，如果是则显示BIG5中文字
				if(tmp >= 0xA440 && tmp < 0xFFFF)
				{
					System.out.println("BIG5:" + new String(array,"Big5"));
				}
				else
				{
					//将第二个字节推回流
					pushbackInputStream.unread(array,1,1);
					//显示ASCII范围的字符
					System.out.println("ASCII: " + (char)array[0]);
				}
			}
			pushbackInputStream.close();
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			System.out.println("请指定文件名称");
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
