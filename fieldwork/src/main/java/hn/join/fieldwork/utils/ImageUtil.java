package hn.join.fieldwork.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

public class ImageUtil extends FileUtil {

	public static File getWorkOrderImageDir(String fieldworkerId, String workOrderNo) {
//		File fieldworkerDir = new File(SystemConstants.getUploadRepository(),
//				fieldworkerId);
//		if (!fieldworkerDir.exists())
//			fieldworkerDir.mkdir();
		File orderDir = new File(SystemConstants.getUploadRepository(), workOrderNo);
		if (!orderDir.exists()) {
			orderDir.mkdir();
		}
		File imageDir = new File(orderDir, "images");
		if (!imageDir.exists()) {
			imageDir.mkdir();
		}
		return imageDir;
	}

	// public static void addImageFile(String workOrderNo,MultipartFile
	// uploadImageFile) throws IOException{
	// File imageDir=getWorkOrderImageDir(workOrderNo);
	// String filename =
	// FilenameUtils.getName(uploadImageFile.getOriginalFilename());
	// File imageFile=new File(imageDir,filename);
	// FileUtils.copyInputStreamToFile(uploadImageFile.getInputStream(),
	// imageFile);
	// }

	public static boolean removeImageFile(String fieldworkderId,String workOrderNo, String filename) {
		File imageDir = getWorkOrderImageDir(fieldworkderId,workOrderNo);
		File imageFile = new File(imageDir, filename);
		return FileUtils.deleteQuietly(imageFile);
	}

}
