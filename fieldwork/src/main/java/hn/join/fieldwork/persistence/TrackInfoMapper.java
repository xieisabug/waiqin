package hn.join.fieldwork.persistence;

import hn.join.fieldwork.domain.TrackInfo;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
/**
 * 跟踪信息DAO
 * 操作表：tbl_track_info
 * @author chenjinlong
 *
 */
public interface TrackInfoMapper {
	/**
	 * 新增跟踪记录
	 * @param trackInfo
	 */
	void insertTrackInfo(TrackInfo trackInfo);
	/**
	 * 查询跟踪信息
	 * @param fieldWorkerId
	 * @param fromTime
	 * @param toTime
	 * @return
	 */
	List<TrackInfo> findInTimeSpan(
            @Param(value = "fieldWorkerId") Long fieldWorkerId,
            @Param(value = "fromTime") Date fromTime,
            @Param(value = "toTime") Date toTime);

}
