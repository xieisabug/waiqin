package test;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import com.google.common.collect.Lists;

public class JodaDate {
	
	public static void main(String[] args){
		LocalDate now=new LocalDate();
		System.out.println(now.dayOfWeek().get());
		
		DateTime now1=new DateTime();
		System.out.println(now1.toDateMidnight().toString(DateTimeFormat
				.forPattern("yyyy-MM-dd")));
		
		List<String> list1=Lists.newArrayList("1","2","3");
		List<String> list2=Lists.newArrayList("12","13","4");
		boolean result=list1.removeAll(list2);
		System.out.println(result);
		System.out.println(list1);
		
		
		DateTime _date=now1.toDateMidnight().toDateTime();
		System.out.println(_date.minusSeconds(2).toString("yyyy-MM-dd HH:mm:ss"));
		
		
	}

}
