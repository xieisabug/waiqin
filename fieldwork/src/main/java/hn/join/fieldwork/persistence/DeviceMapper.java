package hn.join.fieldwork.persistence;

import hn.join.fieldwork.domain.Device;
import hn.join.fieldwork.domain.DeviceJournal;

import java.util.List;

import org.apache.ibatis.annotations.Param;
/**
 * 设备DAO
 * 操作表：tbl_device
 * @author chenjinlong
 *
 */
public interface DeviceMapper {
	/**
	 * 新增设备记录
	 * @param device
	 */
	void insertDevice(Device device);
	/**
	 * 新增设备日志
	 * @param deviceId
	 * @param journal
	 * @param operatorId
	 */
	void insertDeviceJournal(@Param(value = "deviceId") Long deviceId,
                             @Param(value = "journal") DeviceJournal journal,
                             @Param(value = "operatorId") Long operatorId);
	/**
	 * 更新设备记录
	 * @param device
	 */
	void updateDevice(Device device);
	/**
	 * 删除设备记录
	 * @param deviceId
	 */
	void deleteDevice(Long deviceId);
	/**
	 * 通过ID获取设备信息
	 * @param deviceId
	 * @return
	 */
	Device getById(Long deviceId);
	/**
	 * 根据设备ID获取设备日志
	 * @param deviceId
	 * @return
	 */
	List<DeviceJournal> findJournalByDeviceId(Long deviceId);
	/**
	 * 设备统计
	 * @param areaCode
	 * @param meid
	 * @param phoneNo
	 * @return
	 */
	long countBy(
            @Param(value = "areaCode") String areaCode,
            @Param(value = "meid") String meid,
            @Param(value = "phoneNo") String phoneNo);
	/**
	 * 多条件设备查询
	 * @param areaCode
	 * @param meid
	 * @param phoneNo
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<Device> findBy(
            @Param(value = "areaCode") String areaCode,
            @Param(value = "meid") String meid,
            @Param(value = "phoneNo") String phoneNo,
            @Param(value = "offset") Integer offset,
            @Param(value = "limit") Integer limit);

	/**
	 * 根据MEID统计设备
	 * @param meid
	 * @return
	 */
	long countByMeid(String meid);
	/**
	 * 统计活跃状态的设备
	 * @param areaCode
	 * @param meid
	 * @param phoneNo
	 * @return
	 */
	long countByIdle(
            @Param(value = "areaCode") String areaCode,
            @Param(value = "meid") String meid,
            @Param(value = "phoneNo") String phoneNo);
	/**
	 * 查询活跃状态的设备
	 * @param areaCode
	 * @param meid
	 * @param phoneNo
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<Device> findByIdle(
            @Param(value = "areaCode") String areaCode,
            @Param(value = "meid") String meid,
            @Param(value = "phoneNo") String phoneNo,
            @Param(value = "offset") Integer offset,
            @Param(value = "limit") Integer limit);
}
