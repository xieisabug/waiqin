package hn.join.fieldwork.service;

import hn.join.fieldwork.utils.AudioUtil;
import hn.join.fieldwork.utils.ImageUtil;
import hn.join.fieldwork.utils.StreamTool;
import hn.join.fieldwork.utils.SystemConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.io.RandomAccessFile;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * 上传文件服务类
 * @author aisino_lzw
 *
 */
@Component
public class UploadSocketServer {

	private final Logger LOG = Logger.getLogger(getClass());

	private ExecutorService executorService;// 线程池
	private ServerSocket ss = null;
	private volatile boolean quit;// 是否退出

	public UploadSocketServer() {
        LOG.info( "UploadSocketServer init...");
	}

	/**
	 * 开启线程
	 */
	@PostConstruct
	public void start() {
		new Thread(new Runnable() {
			public void run() {
				int availableProcessNum = Runtime.getRuntime()
						.availableProcessors();
				executorService = new ThreadPoolExecutor(availableProcessNum,
						availableProcessNum * 4, 1, TimeUnit.MINUTES,
						new LinkedBlockingQueue<Runnable>());
				try {
					ss = new ServerSocket(
							SystemConstants.getUploadServerSocketPort());
					while (!quit) {
						Socket socket = ss.accept();
						executorService.execute(new SocketTask(socket));// 启动一个线程来处理请求
					}
				} catch (IOException e) {
					LOG.error("",e);
				}

			}
		}).start();
	}

	/**
	 * 关闭socket
	 */
	@PreDestroy
	public void quit() {
		this.quit = true;
		try {
			ss.close();
		} catch (IOException e) {
			LOG.error("", e);
		}
		executorService.shutdown();
	}

	
	/**
	 * 开启线程监听文件上传服务
	 * @author aisino_lzw
	 *
	 */
	private class SocketTask implements Runnable {
		private Socket socket;

		public SocketTask(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			PushbackInputStream socketInputStream = null;
			OutputStream socketOutputStream = null;
			try {
//				System.out.println("accepted connenction from "
//						+ socket.getInetAddress() + " @ " + socket.getPort());
				socketInputStream = new PushbackInputStream(
						socket.getInputStream());
				// 得到客户端发来的第一行协议数据：Content-Length=143253434;filename=xxx.3gp;fieldworkerid=;orderid=;filetype=;
				String head = StreamTool.readLine(socketInputStream);
				LOG.info("SocketTask receiveHead:"+head);
				if (head != null) {
					// 下面从协议数据中读取各种参数值
					String[] items = head.split(";");
					String filelength = items[0].substring(items[0]
							.indexOf("=") + 1);
					String filename = items[1]
							.substring(items[1].indexOf("=") + 1);
					String workOrderNo = items[2]
							.substring(items[2].indexOf("=") + 1);
					String fieldworkerId = items[3].substring(items[3]
							.indexOf("=") + 1);
					String filetype = items[4]
							.substring(items[4].indexOf("=") + 1);
					String basename = FilenameUtils.getBaseName(filename);

					int position = 0;
					File logFile = getLogFile(fieldworkerId, workOrderNo, filetype,
							basename);
					if(!logFile.exists())logFile.createNewFile();
					File dataFile = new File(logFile.getParent(), filename);
					FileInputStream logFileInputStream = new FileInputStream(
							logFile);
					Properties props = new Properties();
					props.load(logFileInputStream);
					if (!props.isEmpty()) {
						position = Integer.valueOf(props.getProperty("length"));// 读取断点位置
					}
					IOUtils.closeQuietly(logFileInputStream);

					socketOutputStream = socket.getOutputStream();
					String response = "filename=" + filename + ";position="
							+ position + "\r\n";
					// 服务器收到客户端的请求信息后，给客户端返回响应信息：filename=?;position=?
					// sourceid由服务生成，唯一标识上传的文件，position指示客户端从文件的什么位置开始上传
					socketOutputStream.write(response.getBytes());
					socketOutputStream.flush();
					RandomAccessFile dataFileOutputStream = new RandomAccessFile(
							dataFile, "rwd");
					int length=0 ;
					if (position == 0){
						length=0;
						dataFileOutputStream.setLength(Integer.valueOf(filelength));}// 设置文件长度?
					else if (position != -1) {
						dataFileOutputStream.seek(position);// 移动文件指定的位置开始写入数据
						length = position;
						
					}
					byte[] buffer = new byte[1024];
					int len = -1;
					while ((len = socketInputStream.read(buffer)) != -1) {// 从输入流中读取数据写入到文件中
						dataFileOutputStream.write(buffer, 0, len);
						length += len;
						props.put("length", String.valueOf(length));
						saveFileLog(logFile, props);// 实时记录文件的最后保存位置
					}
					dataFileOutputStream.close();
				}
			} catch (Exception e) {
				LOG.error("", e);
			} finally {
				IOUtils.closeQuietly(socketInputStream);
				IOUtils.closeQuietly(socketOutputStream);
				try {
					if (socket != null)
						socket.close();
				} catch (IOException e) {
				}
			}
		}

		/**
		 * 获取日志文件
		 * @param fieldworkerId
		 * @param workOrderNo
		 * @param filetype
		 * @param basename
		 * @return
		 */
		private File getLogFile(String fieldworkerId, String workOrderNo,
				String filetype, String basename) {
			File logFileDir;
			if ("image".equals(filetype)) {
				logFileDir = ImageUtil.getWorkOrderImageDir(fieldworkerId,
						workOrderNo);
			} else {
				logFileDir = AudioUtil.getWorkOrderAudioDir(fieldworkerId,
						workOrderNo);
			}
			File logFile = new File(logFileDir, basename + ".log");
			return logFile;
		}

		/**
		 * 保存日志文件
		 * @param logFile
		 * @param props
		 * @throws IOException
		 */
		private void saveFileLog(File logFile, Properties props)
				throws IOException {
			FileOutputStream outputStream = new FileOutputStream(logFile);
			try {
				props.store(outputStream, null);
			} finally {
				IOUtils.closeQuietly(outputStream);
			}
		}

	}

	// public static void main(String[] args) throws Exception {
	// MySocketServer myServer = new MySocketServer(36666);
	// myServer.start();
	//
	// }

}