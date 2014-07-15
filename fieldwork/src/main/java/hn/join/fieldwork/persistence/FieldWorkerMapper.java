package hn.join.fieldwork.persistence;

import hn.join.fieldwork.domain.FieldWorker;
import hn.join.fieldwork.web.command.FieldWorkerLoginCommand;

import java.util.List;

import org.apache.ibatis.annotations.Param;
/**
 * 业务员DAO
 * 操作表：tbl_fieldworker
 * @author chenjinlong
 *
 */
public interface FieldWorkerMapper {
	/**
	 * 加载所有业务员ID
	 * @return
	 */
	List<Long> loadAllFieldWorkerId();
	/**
	 * 根据ID获取业务员信息
	 * @param fieldWorkerId
	 * @return
	 */
	FieldWorker getById(Long fieldWorkerId);
	/**
	 * 新增业务员信息
	 * @param fieldWorker
	 */
	void insertFieldWorker(@Param(value = "fieldWorker") FieldWorker fieldWorker);
	/**
	 * 更新业务员信息
	 * @param fieldWorker
	 */
	void updateFieldWorker(@Param(value = "fieldWorker") FieldWorker fieldWorker);
	/**
	 * 修改业务员信息
	 * @param fieldWorker
	 */
	void deleteFieldWorkerById(Long fieldWorkerId);
	/**
	 * 根据登录时的业务员信息查找业务员信息
	 * @param fieldWorker
	 */
	FieldWorker findByFieldWorkerLoginCommand(FieldWorkerLoginCommand command);

	// FieldWorker getByWorkerNo(String workerNo);
	/**
	 * 更新业务员登录密码
	 * @param fieldWorkerId
	 * @param password
	 */
	void updatePassword(@Param(value = "fieldWorkerId") Long fieldWorkerId,
                        @Param(value = "password") String password);

	// void updateDevice(@Param(value = "fieldWorkerId") Long fieldWorkerId,
	// @Param(value = "deviceId") Long deviceId);
	/**
	 * 统计
	 * @param fullname
	 * @param mobileNo
	 * @param city
	 * @return
	 */
	long countBy(@Param(value = "fullname") String fullname,
                 @Param(value = "mobileNo") String mobileNo,
                 @Param(value = "city") String city);
	/**
	 * 根据条件查询
	 * @param fullname
	 * @param mobileNo
	 * @param city
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<FieldWorker> findBy(@Param(value = "fullname") String fullname,
                             @Param(value = "mobileNo") String mobileNo,
                             @Param(value = "city") String city,
                             @Param(value = "offset") Integer offset,
                             @Param(value = "limit") Integer limit);
	/**
	 * 关联业务员与设备
	 * @param fieldWorkerId
	 * @param deviceId
	 */
	void attacheDevice(@Param(value = "fieldWorkerId") Long fieldWorkerId,
                       @Param(value = "deviceId") Long deviceId);
	/**
	 * 取消业务员与设备关系 
	 * @param fieldWorkerId
	 */
	void unattachDevice(Long fieldWorkerId);
	/**
	 * 根据区域编码获取业务员
	 * @param areaCode
	 * @return
	 */
	List<Long> findFieldWorkerIdByAreaCode(String areaCode);
	/**
	 * 查询ID列表中的业务员信息及设备信息列表
	 * @param inTimeFieldWorkerIdList
	 * @return
	 */
	List<FieldWorker> findOutTimeFieldWorkers(List<Long> inTimeFieldWorkerIdList);
		/**
		 * 获取某个业务员的信息及设备信息
		 * @param fieldWorkerId
		 * @return
		 */
	FieldWorker getFieldWorkerWithDeviceById(Long fieldWorkerId);
}
