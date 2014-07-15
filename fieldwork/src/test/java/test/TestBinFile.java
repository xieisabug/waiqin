package test;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class TestBinFile {

	/**
	 * @param args
	 * @throws java.io.IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		File f=new File("C:/2.bin");
		DataOutputStream fout=new DataOutputStream(new FileOutputStream(f)); 
		fout.writeBytes("hello");
		fout.writeBytes("world!");
		fout.writeInt(2);
		fout.writeFloat(45.123f);
		fout.flush();
		fout.close();
		

	}

}
