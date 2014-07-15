package com.sealion.serviceassistant.tools;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * ʱ�乤����
 * �����������ڻ���ʱ��Ĺ����࣬ӵ����๤�߷���
 */
public class DateTools {

    /**
     * ��ȡ����ǰ������ʽ�������ں�ʱ���ַ���
     *
     * @return ������ʽ�������ں�ʱ���ַ���
     */
    public static String getFormatDateAndTime() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sDateFormat.format(new Date());
    }

    /**
     * ��ȡ����ǰ������ʽ���������ַ���
     *
     * @return ������ʽ���������ַ���
     */
    public static String getFormatDate() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return sDateFormat.format(new Date());
    }

    /**
     * ��ȡһ�����ݵ�ǰʱ������ڱ䶯�ĸ�ʽ�����ں�ʱ���ַ���
     * @param days ƫ�Ƶ�ʱ��
     * @return ��ʽ�������ں�ʱ���ַ���
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
     * ͨ��һ���ַ������һ����ʽ����ʱ���ַ���
     *
     * @param str ��Ҫ��ʽ�����ַ���
     * @return ��ʽ����ʱ���ַ���
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
     * ����һ���ַ������һ����ʽ���������ַ���
     *
     * @param str ��Ҫ��ʽ�����ַ���
     * @return ��ʽ���������ַ���
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
