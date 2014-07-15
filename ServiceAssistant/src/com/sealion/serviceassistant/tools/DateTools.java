package com.sealion.serviceassistant.tools;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 时间工具类
 * 包含操作日期或者时间的工具类，拥有许多工具方法
 */
public class DateTools {

    /**
     * 获取到当前经过格式化的日期和时间字符串
     *
     * @return 经过格式化的日期和时间字符串
     */
    public static String getFormatDateAndTime() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sDateFormat.format(new Date());
    }

    /**
     * 获取到当前经过格式化的日期字符串
     *
     * @return 经过格式化的日期字符串
     */
    public static String getFormatDate() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return sDateFormat.format(new Date());
    }

    /**
     * 获取一个根据当前时间的日期变动的格式化日期和时间字符串
     * @param days 偏移的时间
     * @return 格式化的日期和时间字符串
     */
    public static String getFormatDataAndTimeBeforeDay(int days) {
        Calendar calendar = new GregorianCalendar();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr;
        calendar.add(Calendar.DATE, days);
        dateStr = format.format(calendar.getTime());
        return dateStr;
    }

    /**
     * 通过一个字符串获得一个格式化的时间字符串
     *
     * @param str 需要格式化的字符串
     * @return 格式化的时间字符串
     */
    public static String formatDateHours(String str) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("HH:mm:ss");
        Date d;
        try {
            d = DateFormat.getDateTimeInstance().parse(str);
            return sDateFormat.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 根据一个字符串获得一个格式化的日期字符串
     *
     * @param str 需要格式化的字符串
     * @return 格式化的日期字符串
     */
    public static String formatDate(String str) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date d;
        try {
            d = DateFormat.getDateTimeInstance().parse(str);
            return sDateFormat.format(d);
        } catch (ParseException e) {
            return "";
        }
    }

}
