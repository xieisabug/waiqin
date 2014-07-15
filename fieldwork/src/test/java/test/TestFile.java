package test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class TestFile {

	/**
	 * @param args
	 * @throws java.io.IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		listFiles("src/test/java");
		
		File a1=new File("D:/java_tookit/simple-xml-2.6.9/tutorial.htm");
		File b1=new File("b1");
		FileUtils.copyFile(a1, b1);
//		FileUtils.write(a1, "hello");
		System.out.println(new Date(b1.lastModified()));
		
		
		List<String> f1=FileUtils.readLines(new File("E:/1.txt"));
		List<String> f2=FileUtils.readLines(new File("E:/2.txt"));
		
		Set<String> s1=new HashSet<String>(f1);
		Set<String> s2=Sets.newHashSetWithExpectedSize(f2.size());
		
		System.out.println(s1);
		
		for(String _s:f2){
			if(_s.startsWith("X")){
				s2.add(_s.substring(1));
			}else{
				s2.add(_s);
			}
		}
		System.out.println(s2);
		Map<String,String> results=Maps.newHashMap();
		for(String toLook:s2){
			for(String item:s1){
				if(item.contains(toLook)){
					results.put(toLook,item);
					break;
				}
			}
		}
		System.out.println(results);
		

	}
	
	public static void listFiles(String dirPath){
		File dir=new File(dirPath);
		System.out.println(Arrays.toString(dir.list()));
		ImmutableList<String> of = ImmutableList.of("a", "b", "c", "d");  
		of.remove(0);
	}

}
