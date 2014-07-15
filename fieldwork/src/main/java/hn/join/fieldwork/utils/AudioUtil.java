package hn.join.fieldwork.utils;

import java.io.File;

import org.apache.commons.io.FileUtils;
/**
 * 多媒体文件操作工具类
 * @author chenjinlong
 *
 */
public class AudioUtil extends FileUtil{
	/**
	 * 获取工单音频文件位置
	 * @param fieldworkerId
	 * @param workOrderNo
	 * @return
	 */
	public static File getWorkOrderAudioDir(String fieldworkerId, String workOrderNo) {
//		File fieldworkerDir = new File(SystemConstants.getUploadRepository(),
//				fieldworkerId);
//		if (!fieldworkerDir.exists())
//			fieldworkerDir.mkdir();
		File orderDir = new File(SystemConstants.getUploadRepository(), workOrderNo);
		if (!orderDir.exists()) {
			orderDir.mkdir();
		}
		File imageDir = new File(orderDir, "audio");
		if (!imageDir.exists()) {
			imageDir.mkdir();
		}
		return imageDir;
	}
	/**
	 * 删除文件
	 * @param fieldworkderId
	 * @param workOrderNo
	 * @param filename
	 * @return
	 */
	public static boolean removeAudioFile(String fieldworkderId,String workOrderNo,String filename){
		File audioDir=getWorkOrderAudioDir(fieldworkderId,workOrderNo);
		File audioFile=new File(audioDir,filename);
		return FileUtils.deleteQuietly(audioFile);
	}
	
//	public static List

}
