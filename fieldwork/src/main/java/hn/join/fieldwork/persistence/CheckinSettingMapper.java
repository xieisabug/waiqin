package hn.join.fieldwork.persistence;

import hn.join.fieldwork.domain.CheckinSetting;

import java.util.List;

import org.apache.ibatis.annotations.Param;
/**
 * 签到设置DAO
 * 操作表：tbl_checkin_setting
 * @author chenjinlong
 *
 */
public interface CheckinSettingMapper {
	/**
	 * 插入签到设置记录
	 * @param checkinSetting
	 * @param creatorId
	 */
	public void insertCheckinSetting(
            @Param(value = "checkinSetting") CheckinSetting checkinSetting,
            @Param(value = "creatorId") Long creatorId);

//	public void updateCheckinSetting(CheckinSetting checkinSetting);
	/**
	 * 根据日期获得签到设置记录
	 * @param date
	 * @return
	 */
	public CheckinSetting getByDate(String date);
	/**
	 * 统计某段时间内的签到设置
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	
	public long countBy(@Param(value = "fromDate") String fromDate, @Param(value = "toDate") String toDate);
	/**
	 * 获取某段时间内的签到设置记录
	 * @param fromDate
	 * @param toDate
	 * @param offset
	 * @param limit
	 * @return
	 */
	public List<CheckinSetting> findBy(@Param(value = "fromDate") String fromDate, @Param(value = "toDate") String toDate, @Param(value = "offset") Integer offset, @Param(value = "limit") Integer limit);

}
