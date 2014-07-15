package hn.join.fieldwork.utils;

import java.util.ArrayList;
import java.util.List;

import com.google.common.primitives.UnsignedInts;
import com.google.common.primitives.UnsignedLongs;
/**
 * 字符串处理类
 * @author chenjinlong
 *
 */
public class StringUtil {
   /**
    * 将字符串中的字符转化成int类型数据
    * @param s
    * @param seq
    * @return
    */
	public static List<Integer> fromStringToIntList(String s, String seq) {
		String[] _array = s.split(seq);
		List<Integer> intList = new ArrayList<Integer>(_array.length);
		for (String _element : _array) {
			intList.add(UnsignedInts.parseUnsignedInt(_element));
		}
		return intList;
	}
	/**
	    * 将字符串中的字符转化成Long类型数据
	    * @param s
	    * @param seq
	    * @return
	    */
	public static List<Long> fromStringToLongList(String s, String seq) {
		String[] _array = s.split(seq);
		List<Long> longList = new ArrayList<Long>(_array.length);
		for (String _element : _array) {
			longList.add( UnsignedLongs.parseUnsignedLong(_element));
		}
		return longList;
	}

}
