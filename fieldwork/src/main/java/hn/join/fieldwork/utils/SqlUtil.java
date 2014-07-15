package hn.join.fieldwork.utils;

import org.apache.commons.lang3.StringUtils;
/**
 * Sql工具类，主要用来处理模糊查询
 * @author chenjinlong
 *
 */
public class SqlUtil {

	public static String matchTail(String criteria) {
		if (!StringUtils.isEmpty(criteria))
			return "%" + criteria;
		return null;

	}

	public static String matchHead(String criteria) {
		if (!StringUtils.isEmpty(criteria))
			return criteria + "%";
		return null;

	}

	public static String matchAnywhere(String criteria) {
		if (!StringUtils.isEmpty(criteria))
			return "%" + criteria + "%";
		return null;
	}

}
