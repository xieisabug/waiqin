package hn.join.fieldwork.utils;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
/**
 * Json工具类
 * @author chenjinlong
 *
 */
public class JsonUtil {

	private static final Logger LOG = Logger.getLogger(JsonUtil.class);

	private static final ObjectMapper mapper = new ObjectMapper();

	private JsonUtil() {

	}

	/**
	 * @param <T>
	 *            泛型声明
	 * @param bean
	 *            类的实例
	 * @return JSON字符串
	 */
	public static <T> String toJson(T bean) {
		StringWriter sw = new StringWriter();
		try {
			JsonGenerator gen = new JsonFactory().createJsonGenerator(sw);
			mapper.writeValue(gen, bean);
			gen.close();
			return sw.toString();
		} catch (JsonGenerationException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * @param <T>
	 *            泛型声明
	 * @param json
	 *            JSON字符串
	 * @param clzz
	 *            要转换对象的类型
	 * @return
	 */
	public static <T> T fromJson(String json, Class<T> clzz) {

		T t = null;
		try {
			t = mapper.readValue(json, clzz);
		} catch (JsonParseException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return t;
	}

	/**
	 * @param json
	 *            JSON字符串,请保持key是加了双引号的
	 * @return Map对象,默认为HashMap
	 */
	public static Map<String, Object> getJsonObj(String json) {
		try {
			return mapper.readValue(json, Map.class);
		} catch (JsonParseException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static List<Object> parseArray(String jsonString) {
		List<Object> jsonList = null;

		try {
			jsonList = mapper.readValue(jsonString, List.class);
		} catch (Exception e) {
			LOG.error("Parse json to array failed - " + jsonString);
		}

		return jsonList;
	}

	public static <T> List<T> parseArray(String jsonString, Class<?> clazz) {
		try {
			return mapper.readValue(jsonString, mapper.getTypeFactory()
					.constructCollectionType(List.class, clazz));
		} catch (JsonParseException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

}
