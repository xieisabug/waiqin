package hn.join.fieldwork.utils;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;

/**
 * 文件压缩工具类
 * @author chenjinlong
 *
 */
public class ZipUtil {
	public static void createZip(File zipFile, File srcdir,String includePattern) {

		Project prj = new Project();
		Zip zip = new Zip();
		zip.setEncoding("GB2312");
		zip.setProject(prj);
		zip.setDestFile(zipFile);
		FileSet fileSet = new FileSet();
		
		fileSet.setProject(prj);
		fileSet.setDir(srcdir);
		if(!StringUtils.isEmpty(includePattern)){
			fileSet.setIncludes(includePattern);
		}
		
		zip.addFileset(fileSet);

		zip.execute();
	}

	public static void createZipByFile(File zipFile, File srcdir,
			String filename) {

		Project prj = new Project();
		Zip zip = new Zip();
		zip.setEncoding("GB2312");
		zip.setProject(prj);
		zip.setDestFile(zipFile);

		FileSet fileSet = new FileSet();
		fileSet.setProject(prj);
		fileSet.setDir(srcdir);
		fileSet.setIncludes(filename);
		zip.addFileset(fileSet);

		zip.execute();
	}

}
