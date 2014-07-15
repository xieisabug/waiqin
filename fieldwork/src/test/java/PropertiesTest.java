import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;


public class PropertiesTest {
	
	public static void main(String[]args) throws FileNotFoundException, IOException{
		Properties p=new Properties();
//		InputStream in=ClassLoader.getSystemResourceAsStream("checkin-setting.properties");
		URL url=ClassLoader.getSystemResource("checkin-setting.properties");
		p.load(url.openStream());
		System.out.println(p.getProperty("message"));
		System.out.println(p.getProperty("time"));
		System.out.println(url.getFile());
		p.setProperty("message", "中国人");
		p.setProperty("time","20:00");
		p.store(new FileOutputStream(url.getFile()), null);
		
	}
	
	

}
