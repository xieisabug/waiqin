package test;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;

public class FilenameFilterMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		File audioDir= new File("E:/upload_repository/61/audio");
		File[] files=audioDir.listFiles(new FilenameFilter(){

			@Override
			public boolean accept(File dir, String name) {
				return !name.endsWith("log");
			}
			
		});
		System.out.println(Arrays.toString(files));

	}

}
