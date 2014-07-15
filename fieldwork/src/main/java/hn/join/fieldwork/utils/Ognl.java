package hn.join.fieldwork.utils;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
/**
 * Ognl工具类
 * @author chenjinlong
 *
 */
public class Ognl {
	/**
	 * 判断对象是否为空值
	 * @param o
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static boolean isEmpty(Object o) throws IllegalArgumentException {
		if (o == null)
			return true;

		if (o instanceof String) {
			if (((String) o).length() == 0) {
				return true;
			}
		} else if (o instanceof Collection) {
			if (((Collection) o).isEmpty()) {
				return true;
			}
		} else if (o.getClass().isArray()) {
			if (Array.getLength(o) == 0) {
				return true;
			}
		} else if (o instanceof Map) {
			if (((Map) o).isEmpty()) {
				return true;
			}
		} else {
			return false;
		}

		return false;
	}

	/**
	 * test for Map,Collection,String,Array isNotEmpty
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isNotEmpty(Object o) {
		return !isEmpty(o);
	}

	public static boolean isNotZero(Number n) {
		if (n == null)
			return false;
		if (n.intValue() < 0)
			return false;
		return true;

	}
	
}
