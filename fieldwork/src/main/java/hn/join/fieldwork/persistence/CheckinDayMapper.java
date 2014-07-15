package hn.join.fieldwork.persistence;

import hn.join.fieldwork.domain.CheckinDay;

import java.sql.Date;
/**
 * 签到日期DAO
 * 对应表tbl_checkin_day
 * @author chenjinlong
 *
 */
public interface CheckinDayMapper {
	/**
	 * 插入签到日期
	 * @param checkInDay
	 */
	public void insertCheckinDay(CheckinDay checkInDay);
	/**
	 * 查找签到日期
	 * @param date
	 * @return
	 */
	public CheckinDay findByDate(String date);

}
