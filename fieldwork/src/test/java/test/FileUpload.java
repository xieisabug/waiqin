package test;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import test.StreamTool;

public class FileUpload {
	public static void main(String[] args) throws IOException {
		String address="222.240.218.91";
		int port=20000;		
		String filePath="D:\\a.png";
		File file = new File(filePath);
		String order_num="123456";
		String file_type="image";
		String fieldWorkId="159023";
		UploadFileBySocket(address, port, file, order_num, file_type, fieldWorkId);
	}
	 /**
     * 通过Socket上传文件
     * @param address       地址
     * @param port          端口
     * @param uploadFile    上传的文件文件路径
     * @param order_num     工单号
     * @param file_type     文件类型 “image”:图片，“audio”:音频
     * @param fieldworkerid 员工id
     * @throws IOException
     */
    public static void UploadFileBySocket(String address, int port, File uploadFile, String order_num, String file_type, String fieldworkerid) throws IOException {
        String head = "Content-Length=" + uploadFile.length() + ";filename=" + uploadFile.getName()
                + ";orderid=" + order_num + ";fieldworkerid=" + fieldworkerid + ";filetype=" + file_type + "\r\n";

        Socket socket = new Socket(address, port);
        OutputStream outStream = socket.getOutputStream();
        outStream.write(head.getBytes());

        PushbackInputStream inStream = new PushbackInputStream(socket.getInputStream());
        String response = StreamTool.readLine(inStream);
        String[] items = response.split(";");
        String responseid = items[0].substring(items[0].indexOf("=") + 1);
        String position = items[1].substring(items[1].indexOf("=") + 1);

        RandomAccessFile fileOutStream = new RandomAccessFile(uploadFile, "r");
        fileOutStream.seek(Integer.valueOf(position));
        byte[] buffer = new byte[1024];
        int len = -1;
        int length = Integer.valueOf(position);
        while ((len = fileOutStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
            length += len;
        }
        fileOutStream.close();
        outStream.close();
        inStream.close();
        socket.close();
    }
}
