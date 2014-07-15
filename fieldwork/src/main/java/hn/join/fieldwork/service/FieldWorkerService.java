package hn.join.fieldwork.service;

import hn.join.fieldwork.domain.FieldWorker;
import hn.join.fieldwork.persistence.FieldWorkerMapper;
import hn.join.fieldwork.persistence.SQLQueryResult;
import hn.join.fieldwork.service.FieldWorkerUpdateListener.LatestFieldWorkerInfo;
import hn.join.fieldwork.utils.DateTimeUtil;
import hn.join.fieldwork.utils.HashUtil;
import hn.join.fieldwork.web.command.FieldWorkerLoginCommand;
import hn.join.fieldwork.web.command.OperationCommand;
import hn.join.fieldwork.web.dto.FieldWorkerDto;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;


/**
 * 外勤员工相关操作服务类
 * @author aisino_lzw
 *
 */
@Service
public class FieldWorkerService {

	@Autowired
	private FieldWorkerUpdateListener fieldWorkerUpdateListener;

	@Autowired
	private FieldWorkerMapper fieldWorkerMapper;

	
	/**
	 * 员工登陆
	 * @param command 员工信息
	 * @return 员工信息
	 */
	public FieldWorker login(FieldWorkerLoginCommand command) {
		FieldWorker _login = fieldWorkerMapper
				.findByFieldWorkerLoginCommand(command);
		if (_login != null) {
			String _password = HashUtil.encodeMd5Hex(command.getPassword());
			if (!_password.equals(_login.getPassword()))
				return null;
		}

		return _login;
	}

	
	/**
	 * 根据员工id查询员工信息
	 * @param fieldWorkerId
	 * @return 员工信息
	 */
	public FieldWorker getById(Long fieldWorkerId) {
		return fieldWorkerMapper.getById(fieldWorkerId);
	}

	
	/**
	 * 员工修改个人密码
	 * @param fieldWorkerId 员工id
	 * @param oldPassword 旧密码
	 * @param newPassword 新密码
	 * @return 
	 * @throws DataAccessException
	 */
	@Transactional(rollbackFor = Exception.class)
	public boolean changePassword(Long fieldWorkerId, String oldPassword,
			String newPassword) throws DataAccessException {
		boolean success = false;
		FieldWorker _fieldWorker = fieldWorkerMapper.getById(fieldWorkerId);
		if (_fieldWorker != null) {
			String _oldPassword = HashUtil.encodeMd5Hex(oldPassword);
			if (_oldPassword.equals(_fieldWorker.getPassword())) {
				String _newPassword = HashUtil.encodeMd5Hex(newPassword);
				fieldWorkerMapper.updatePassword(fieldWorkerId, _newPassword);
				success = true;
			}
		}
		return success;

	}

	/**
	 * 重置密码
	 * @param fieldWorkerId 员工id
	 * @param newPassword 新密码
	 * @throws DataAccessException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void resetPassword(Long fieldWorkerId, String newPassword)
			throws DataAccessException {
		String _newPassword = HashUtil.encodeMd5Hex(newPassword);
		fieldWorkerMapper.updatePassword(fieldWorkerId, _newPassword);
	}

	
	/**
	 * 条件查询员工信息
	 * @param fullname 姓名
	 * @param mobileNo 手机号码
	 * @param areaCode 区域编号
	 * @param page 指定页
	 * @param rows 指定行数
	 * @return 员工信息
	 */
	public SQLQueryResult<FieldWorker> findBy(String fullname, String mobileNo,
			String areaCode, Integer page, Integer rows) {
		SQLQueryResult<FieldWorker> queryResult = null;

		long _count = fieldWorkerMapper.countBy(fullname, mobileNo, areaCode);
		if (_count != 0) {
			Integer offset = null;
			if (page != null && rows != null)
				offset = (page - 1) * rows;
			List<FieldWorker> _result = fieldWorkerMapper.findBy(fullname,
					mobileNo, areaCode, offset, rows);
			queryResult = new SQLQueryResult<FieldWorker>(_count, _result);
		} else {
			queryResult = SQLQueryResult.EMPTY_RESULT;
		}
		return queryResult;
	}

	
	/**
	 * 根据区域编号查询员工
	 * @param areaCode 区域编号
	 * @return 
	 */
	public Collection<Long> getFieldWorkerIdByAreaCodeOnLine(String areaCode) {
		return fieldWorkerUpdateListener.getFieldWorkerIdByAreaCode(areaCode);
	}

	/**
	 * 查询员工最后登录时间
	 * @param workerIdList 员工列表
	 * @return
	 */
	public List<LatestFieldWorkerInfoCommand> findLatestFieldWorkerInfoOnLine(
			Collection<Long> workerIdList) {
		long todayInMills = LocalDate.now().toDate().getTime();
		List<LatestFieldWorkerInfoCommand> latestFieldWorkerInfoList = Lists
				.newArrayListWithExpectedSize(workerIdList.size());
		for (Long _fieldWorkerId : workerIdList) {
			LatestFieldWorkerInfo info = fieldWorkerUpdateListener
					.getLatestFieldWorkerInfoByWorkerNo(_fieldWorkerId);
			if (info != null) {
				if (info.getLastLocateTimeInMill() > todayInMills)
					latestFieldWorkerInfoList.add(LatestFieldWorkerInfoCommand
							.create(info));
			}
		}
		return latestFieldWorkerInfoList;

	}

	
	/**
	 * 绑定员工设备
	 * @param fieldWorkerId 员工id
	 * @param deviceId 设备id
	 * @throws DataAccessException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void attachDevice(Long fieldWorkerId, Long deviceId)
			throws DataAccessException {
		fieldWorkerMapper.attacheDevice(fieldWorkerId, deviceId);
	}

	
	/**
	 * 解绑员工设备
	 * @param fieldWorkerId 员工id
	 * @throws DataAccessException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void unattachDevice(Long fieldWorkerId) throws DataAccessException {
		fieldWorkerMapper.unattachDevice(fieldWorkerId);
	}

	
	/**
	 * 同步员工
	 * @param dto
	 */
	@Transactional(rollbackFor = Exception.class)
	public void syncFieldWorker(FieldWorkerDto dto) {
		OperationCommand op = OperationCommand.valueOf(dto.getOp());
		switch (op) {
		case I: {
			doInsertFieldWorker(dto);
			break;
		}
		case U: {
			doUpdateFieldWorker(dto);
			break;
		}
		case D: {
			doRemoveFieldWorker(dto);
			break;
		}
		default: {
			throw new UnsupportedOperationException("不支持的操作,OperationCommand:"
					+ op);
		}
		}
	}

	
	/**
	 * 新增员工信息
	 * @param dto
	 */
	private void doInsertFieldWorker(FieldWorkerDto dto) {
		FieldWorker fieldWorker = new FieldWorker();
		fieldWorker.setId(new Long(dto.getFieldWorkerId()));
		fieldWorker.setWorkerNo(dto.getFieldWorkerNo());
		fieldWorker.setFullname(dto.getFullname());
		fieldWorker.setDepartmentId(dto.getDepartmentId());
		fieldWorkerMapper.insertFieldWorker(fieldWorker);

	}

	/**
	 * 更新员工信息
	 * @param dto
	 */
	private void doUpdateFieldWorker(FieldWorkerDto dto) {

		FieldWorker fieldWorker = new FieldWorker();
		fieldWorker.setId(new Long(dto.getFieldWorkerId()));
		fieldWorker.setWorkerNo(dto.getFieldWorkerNo());
		fieldWorker.setFullname(dto.getFullname());
		fieldWorker.setDepartmentId(dto.getDepartmentId());
		fieldWorkerMapper.updateFieldWorker(fieldWorker);
	}

	
	/**
	 * 移除员工信息
	 * @param dto
	 */
	private void doRemoveFieldWorker(FieldWorkerDto dto) {
		fieldWorkerMapper
				.deleteFieldWorkerById(new Long(dto.getFieldWorkerId()));
	}

	
	/**
	 * 外勤员工最后登录信息内部类
	 * @author aisino_lzw
	 *
	 */
	public static class LatestFieldWorkerInfoCommand {

		private Long fieldWorkerId;

		private String fieldWorkerName;

		private String lastLocateTime;

		private int pendingWorkOrderSize;

		private float latestLatitude;

		private float latestLongitude;

		private String latestAddress;

		public Long getFieldWorkerId() {
			return fieldWorkerId;
		}

		public void setFieldWorkerId(Long fieldWorkerId) {
			this.fieldWorkerId = fieldWorkerId;
		}

		public String getFieldWorkerName() {
			return fieldWorkerName;
		}

		public void setFieldWorkerName(String fieldWorkerName) {
			this.fieldWorkerName = fieldWorkerName;
		}

		public int getPendingWorkOrderSize() {
			return pendingWorkOrderSize;
		}

		public void setPendingWorkOrderSize(int pendingWorkOrderSize) {
			this.pendingWorkOrderSize = pendingWorkOrderSize;
		}

		public float getLatestLatitude() {
			return latestLatitude;
		}

		public void setLatestLatitude(float latestLatitude) {
			this.latestLatitude = latestLatitude;
		}

		public float getLatestLongitude() {
			return latestLongitude;
		}

		public void setLatestLongitude(float latestLongitude) {
			this.latestLongitude = latestLongitude;
		}

		public String getLastLocateTime() {
			return lastLocateTime;
		}

		public void setLastLocateTime(String lastLocateTime) {
			this.lastLocateTime = lastLocateTime;
		}

		public String getLatestAddress() {
			return latestAddress;
		}

		public void setLatestAddress(String latestAddress) {
			this.latestAddress = latestAddress;
		}

		/**
		 * 新建外勤员工最后登录信息
		 * @param lastLatestFieldWorkerInfo
		 * @return
		 */
		public static LatestFieldWorkerInfoCommand create(
				LatestFieldWorkerInfo lastLatestFieldWorkerInfo) {
			LatestFieldWorkerInfoCommand command = new LatestFieldWorkerInfoCommand();
			command.fieldWorkerId = lastLatestFieldWorkerInfo
					.getFieldWorkerId();
			command.fieldWorkerName = lastLatestFieldWorkerInfo
					.getFieldWorkerName();
			command.lastLocateTime = DateTimeUtil
					.formatAsYYYYMMddHHmmss(new Date(lastLatestFieldWorkerInfo
							.getLastLocateTimeInMill()));
			command.latestLatitude = lastLatestFieldWorkerInfo
					.getLatestLatitude();
			command.latestLongitude = lastLatestFieldWorkerInfo
					.getLatestLongitude();
			command.setLatestAddress(lastLatestFieldWorkerInfo
					.getLatestAddress());
			command.pendingWorkOrderSize = (lastLatestFieldWorkerInfo
					.getPendingWorkOrderNo() == null || lastLatestFieldWorkerInfo
					.getPendingWorkOrderNo().isEmpty()) ? 0
					: lastLatestFieldWorkerInfo.getPendingWorkOrderNo().size();
			return command;

		}

	}

}
