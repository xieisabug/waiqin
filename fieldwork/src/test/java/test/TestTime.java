package test;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

public class TestTime {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// System.out.println(Time.valueOf("09:00:00"));
		System.out.println(java.sql.Time.valueOf(
				(LocalTime.parse("15:00:02").toString("HH:mm:ss"))).getTime());
		System.out.println(new java.sql.Date(LocalDate.parse("2010-11-11")
				.toDate().getTime()).getTime());

		System.out.println(LocalDate.parse("2013-03-10").getDayOfWeek());
		System.out.println(LocalDate.now());
		System.out.println(new Date(25202000 + 1289404800000L));
		System.out.println(LocalTime.parse("15:00:22").millisOfDay().get());

		DateTime dateTimeNow = DateTime.now();
		System.out.println(dateTimeNow.toDate());
		System.out.println(dateTimeNow.toLocalDateTime());
		System.out.println(dateTimeNow.toLocalTime());

		LocalDate localDate = LocalDate.now();
		LocalTime localTime = LocalTime.now();

		DateTime dt1 = new DateTime(2013, 3, 8, 15, 30, 0);
		System.out.println(dt1);

		DateTime now = new DateTime(localDate.getYear(),
				localDate.getMonthOfYear(), localDate.getDayOfMonth(),
				localTime.getHourOfDay(), localTime.getMinuteOfHour(),
				localTime.getSecondOfMinute());

		if (dt1.isAfter(now)) {
			System.out.println((dt1.getMillis() - now.getMillis()) / 1000 / 60);
		}
		System.out.println(now);

	}

}
