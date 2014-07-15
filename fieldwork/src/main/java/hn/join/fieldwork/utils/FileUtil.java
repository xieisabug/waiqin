package hn.join.fieldwork.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
/**
 * 文件操作工具类
 * @author chenjinlong
 *
 */
public class FileUtil {
	
	
	public static File getSystemTempDir() {
		File dir = new File(System.getProperty("java.io.tmpdir"), "fieldwork");
		if (!dir.exists()) {
			dir.mkdir();
		}
		return dir;
	}

	public static File createTempDir(String dirname) {
		File tempDir = new File(getSystemTempDir(), dirname);
		if (!tempDir.exists())
			tempDir.mkdir();
		return tempDir;
	}

	public static File getTempDir(String dirname) {
		File tempDir = new File(getSystemTempDir(), dirname);
		if (tempDir.exists())
			return tempDir;
		else
			return null;
	}
	/**
	 * 将字符串写入文件
	 * @version 2014年6月13日 下午5:01:57
	 * @param content
	 * @param file
	 */
	public static void writeFile(String content,File file) {		
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
	}

}
