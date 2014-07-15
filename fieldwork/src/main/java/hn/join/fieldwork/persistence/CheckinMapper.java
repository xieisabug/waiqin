package hn.join.fieldwork.persistence;

import hn.join.fieldwork.domain.Checkin;
import hn.join.fieldwork.web.command.CheckinCommand;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
 /**
  * 签到处理DAO
  * 操作表tbl_checkin
  * @author chenjinlong
  *
  */
public interface CheckinMapper {
	/**
	 * 插入1条签到记录
	 * @param checkin
	 */
	void insertCheckin(Checkin checkin);
	/**
	 * 插入多条签到记录
	 * @param list
	 */
	void insertCheckinList(List<Checkin> list);
	/**
	 * 根据业务员ID以及签到时间查询签到记录
	 * @param fieldWorkerId
	 * @param systemCheckinDateTime
	 * @return
	 */
	Long getCheckinIdByFieldWorkerIdAndSystemCheckinDateTime(@Param(value = "fieldWorkerId") Long fieldWorkerId, @Param(value = "systemCheckinDateTime") Date systemCheckinDateTime);
	/**
	 * 通过签到记录ID修改签到时间
	 * @param checkinId
	 * @param userCheckinDateTime
	 */
	void updateUserCheckinDateTimeByCheckinId(@Param(value = "checkinId") Long checkinId, @Param(value = "userCheckinDateTime") Date userCheckinDateTime);

	// List<Long> getFieldWorkerInTimeSpan(@Param(value = "fromTime") Date from,
	// @Param(value = "toTime") Date toTime);

	// List<CheckinCommand> findByDateSpan(
	// @Param(value = "departmentId") Integer departmentId,
	// @Param(value = "fieldWorkerId") Integer fieldWorkerId,
	// @Param(value = "fromTime") Date fromTime,
	// @Param(value = "toTime") Date toTime);
	/**
	 * 签到统计
	 * @param fromDate
	 * @param toDate
	 * @param showAll
	 * @param fieldWorkerName
	 * @param city
	 * @return
	 */
	long countBy(@Param(value = "fromDate") Date fromDate,
                 @Param(value = "toDate") Date toDate,
                 @Param(value = "showAll") Boolean showAll,
                 @Param(value = "fieldWorkerName") String fieldWorkerName,
                 @Param(value = "city") String city);
	/**
	 * 签到记录查询
	 * @param fromDate
	 * @param toDate
	 * @param showAll
	 * @param fieldWorkerName
	 * @param city
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<CheckinCommand> findBy(@Param(value = "fromDate") Date fromDate,
                                @Param(value = "toDate") Date toDate,
                                @Param(value = "showAll") Boolean showAll,
                                @Param(value = "fieldWorkerName") String fieldWorkerName,
                                @Param(value = "city") String city,
                                @Param(value = "offset") Integer offset,
                                @Param(value = "limit") Integer limit);
	/**
	 * 删除过期签到记录
	 */
	void deleteStaleCheckin();

}
