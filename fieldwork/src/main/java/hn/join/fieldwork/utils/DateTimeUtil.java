package hn.join.fieldwork.utils;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.google.common.collect.Lists;
/**
 * 日期处理类
 * @author chenjinlong
 *
 */
public class DateTimeUtil {
	
	public static class OneDayTimeSpan{
		private final DateTime fromTime;
		
		private final DateTime toTime;

		public OneDayTimeSpan(DateTime fromTime, DateTime toTime) {
			super();
			this.fromTime = fromTime;
			this.toTime = toTime;
		}

		public DateTime getFromTime() {
			return fromTime;
		}

		public DateTime getToTime() {
			return toTime;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("OneDayTimeSpan [fromTime=").append(fromTime)
					.append(", toTime=").append(toTime).append("]");
			return builder.toString();
		}
		
		
	}
	

	private DateTimeUtil() {
	}
	
	
	public static List<OneDayTimeSpan> divideIntoTimeSpan(String fromTime,String toTime){
		return divideIntoTimeSpan(_YMdHms.parseDateTime(fromTime),_YMdHms.parseDateTime(toTime));
	}
	
	public static List<OneDayTimeSpan> divideIntoTimeSpan(Date fromTime,Date toTime){
		return divideIntoTimeSpan(new DateTime(fromTime),new DateTime(toTime));
	}
	
	public static List<OneDayTimeSpan> divideIntoTimeSpan(DateTime fromTime,DateTime toTime){
		if(fromTime.dayOfYear().get()==toTime.dayOfYear().get()){
			return Lists.newArrayList(new OneDayTimeSpan(fromTime,toTime));
		}else{
			long span=(toTime.toLocalDate().toDate().getTime()-  fromTime.toLocalDate().toDate().getTime())/ONE_DAY_IN_MILLS;
			DateTime _previous=fromTime;
			DateTime _next=fromTime.toDateMidnight().plusDays(1).toDateTime();
			List<OneDayTimeSpan> results=Lists.newArrayList();
			for(int i=0;i<span;i++){
				results.add(new OneDayTimeSpan(_previous,_next));
				_previous=_next;
				_next=_next.plusDays(1).toDateTime();
			}
			results.add(new OneDayTimeSpan(_previous,toTime));
			return results;
		}
	}
	
	
	

	public static final long ONE_DAY_IN_MILLS=24*60*60*1000L;
	
	private static final DateTimeFormatter _YMd = DateTimeFormat
			.forPattern("yyyy-MM-dd");

	private static final DateTimeFormatter _YMdHms = DateTimeFormat
			.forPattern("yyyy-MM-dd HH:mm:ss");

	public static String formatAsYYYYMMdd(Date date) {
		if (date != null)
			return _YMd.print(date.getTime());
		return null;
	}

	public static String formatAsYYYYMMddHHmmss(Date date) {
		if (date != null)
			return _YMdHms.print(date.getTime());
		return null;
	}

	public static Date parseAsYYYYMMdd(String _dateString) {
		if (!StringUtils.isEmpty(_dateString)) {
			return _YMd.parseDateTime(_dateString).toDate();
		}
		return null;
	}

	public static Date parseAsYYYYMMddHHmmss(String _dateString) {
		if (!StringUtils.isEmpty(_dateString)) {
			return _YMdHms.parseDateTime(_dateString).toDate();
		}
		return null;
	}

	public static DateTimeFormatter getYmdFormatter() {
		return _YMd;
	}

	public static DateTimeFormatter getYmdhmsFormatter() {
		return _YMdHms;
	}

	public static void main(String[] args) {
		String s1 = "2012-11-24 00:00:00";
		String s2="2012-11-30 00:00:00";
		System.out.println(divideIntoTimeSpan(s1,s2));
	}
}
