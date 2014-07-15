package hn.join.fieldwork.utils;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.transform.RegistryMatcher;
import org.simpleframework.xml.transform.Transform;
/**
 * 对象与XML数据互相转化的工具类
 * @author chenjinlong
 *
 */
public class XmlUtil {

	private static final Serializer serializer;

	static {
//		DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		RegistryMatcher m = new RegistryMatcher();
		m.bind(Date.class, new DateFormatTransformer());
		serializer = new Persister(m);

	}

	public static String toXml(Object bean) throws Exception {
		StringWriter stringWriter = new StringWriter();
		toXml(bean, stringWriter);
		return stringWriter.toString();
	}

	public static void toXml(Object bean, Writer writer) throws Exception {
		serializer.write(bean, writer);
	}

	public static <T> T toObject(Class<? extends T> type, String xml)
			throws Exception {
		StringReader stringReader = new StringReader(xml);
		return toObject(type, stringReader);
	}

	public static <T> T toObject(Class<? extends T> type, Reader reader)
			throws Exception {
		T t = serializer.read(type, reader);
		return t;

	}

	private static class DateFormatTransformer implements Transform<Date> {
		private DateFormat ymdDateFormat=new SimpleDateFormat("yyyy-MM-dd");
		
		private DateFormat ymdHmsDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		
		
		public DateFormatTransformer() {
			
		}

		@Override
		public Date read(String value) throws Exception {
			Date result;
			if(value!=null){
				if(value.length()==10){
					result= ymdDateFormat.parse(value);
				}else if(value.length()==19){
					result= ymdHmsDateFormat.parse(value);
				}else{
					result =null;
				}
			}else{
				result=null;
			}
			return result;
		
		}

		@Override
		public String write(Date value) throws Exception {
			return ymdHmsDateFormat.format(value);
		}
	}

}
