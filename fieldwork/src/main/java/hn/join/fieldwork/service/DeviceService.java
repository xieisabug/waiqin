package hn.join.fieldwork.service;

import hn.join.fieldwork.domain.BaseDomain.VisibleStatus;
import hn.join.fieldwork.domain.Device;
import hn.join.fieldwork.domain.Device.DeviceStatus;
import hn.join.fieldwork.domain.DeviceJournal;
import hn.join.fieldwork.persistence.DeviceMapper;
import hn.join.fieldwork.persistence.SQLQueryResult;
import hn.join.fieldwork.utils.JsonUtil;
import hn.join.fieldwork.web.command.CreateDeviceCommand;
import hn.join.fieldwork.web.command.EditDeviceCommand;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;

/**
 * 设备管理
 * @author aisino_lzw
 *
 */
@Service
public class DeviceService {

	@Autowired
	private DeviceMapper deviceMapper;

	
	/**
	 * 添加设备
	 * @param command 设备信息
	 */
	@Transactional(rollbackFor = Exception.class)
	public void newDevice(CreateDeviceCommand command) {
		Device device = fromCreateDeviceCommand(command);
		deviceMapper.insertDevice(device);
	}

	/**
	 * 更新设备信息
	 * @param command 设备信息
	 * @param operatorId 操作员id
	 */
	@Transactional(rollbackFor = Exception.class)
	public void editDevice(EditDeviceCommand command, Long operatorId) {
		Device beforeOne = deviceMapper.getById(Long.parseLong(command
				.getDeviceId()));
		Device device = fromEditDeviceCommand(command);
		DeviceJournal deviceJournal = new DeviceJournal();
		deviceJournal.setMemo(command.getMemo());
		deviceJournal.setPreviousState(generatePreviousDeviceState(beforeOne));
		deviceMapper.updateDevice(device);
		deviceMapper.insertDeviceJournal(Long.parseLong(command.getDeviceId()),
				deviceJournal, operatorId);

	}

	/**
	 * 条件查询指定设备
	 * @param areaCode 区域编号
	 * @param meid 设备识别码
	 * @param phoneNo 电话号码
	 * @param page 指定页
	 * @param rows 指定行数
	 * @return 设备列表
	 */
	public SQLQueryResult<Device> findBy(String areaCode, String meid,
			String phoneNo, Integer page, Integer rows) {
		SQLQueryResult<Device> queryResult = null;
		long _count = deviceMapper.countBy(areaCode, meid, phoneNo);
		if (_count != 0) {
			Integer offset = (page - 1) * rows;
			List<Device> _result = deviceMapper.findBy(areaCode, meid, phoneNo,
					offset, rows);
			queryResult = new SQLQueryResult<Device>(_count, _result);
		} else {
			queryResult = SQLQueryResult.EMPTY_RESULT;
		}
		return queryResult;
	}

	
	/**
	 * 条件查询指定设备
	 * @param areaCode 区域编号
	 * @param meid  设备识别码
	 * @param phoneNo 电话号码
	 * @param page 指定页
	 * @param rows 指定行数
	 * @return 设备列表
	 */
	public SQLQueryResult<Device> findByIdle(String areaCode, String meid,
			String phoneNo, Integer page, Integer rows) {
		SQLQueryResult<Device> queryResult = null;
		long _count = deviceMapper.countByIdle(areaCode, meid, phoneNo);
		if (_count != 0) {
			Integer offset = (page - 1) * rows;
			List<Device> _result = deviceMapper.findByIdle(areaCode, meid,
					phoneNo, offset, rows);
			queryResult = new SQLQueryResult<Device>(_count, _result);
		} else {
			queryResult = SQLQueryResult.EMPTY_RESULT;
		}
		return queryResult;
	}

	
	/**
	 * 删除设备
	 * @param deviceId 设备id
	 * @param memo 备注
	 * @param operatorId 操作员id
	 */
	@Transactional(rollbackFor = Exception.class)
	public void removeDevice(Long deviceId, String memo, Long operatorId) {
		Device beforeOne = deviceMapper.getById(deviceId);
		DeviceJournal deviceJournal = new DeviceJournal();
		deviceJournal.setMemo(memo);
		deviceJournal.setPreviousState(generatePreviousDeviceState(beforeOne));
		deviceMapper.deleteDevice(deviceId);
		deviceMapper.insertDeviceJournal(deviceId, deviceJournal, operatorId);
	}

	/**
	 * 根据设备识别码确定该设备是否存在
	 * @param 设备识别码
	 * @return 
	 */
	public boolean checkMeidAvailable(String meid) {
		long _count = deviceMapper.countByMeid(meid);
		if (_count == 0) {
			return true;
		}
		return false;
	}

	
	/**
	 * 查询设备日志信息
	 * @param deviceId 设备id
	 * @return 设备信息
	 */
	public List<DeviceJournal> loadDeviceJournal(Long deviceId) {
		return deviceMapper.findJournalByDeviceId(deviceId);
	}

	
	/**
	 * 创建设备对象
	 * @param command 创建条件（新建）
	 * @return
	 */
	private Device fromCreateDeviceCommand(CreateDeviceCommand command) {
		Device device = new Device();
		device.setPhoneNo(command.getPhoneNo());
		device.setMeid(command.getMeid());
		if (!StringUtils.isEmpty(command.getCurrentAmount())) {
			device.setCurrentAmount(Float.valueOf(command.getCurrentAmount()));
		}
		device.setConsumeCategory(command.getConsumeCategory());
		device.setModel(command.getModel());
		device.setStatus(DeviceStatus.valueOf(command.getStatus()));
		device.setAreaCode(command.getAreaCode());
		device.setVisibleStatus(VisibleStatus.visible);
		return device;
	}

	/**
	 * 创建设备对象
	 * @param command 创建条件（修改）
	 * @return
	 */
	private Device fromEditDeviceCommand(EditDeviceCommand command) {
		Device device = fromCreateDeviceCommand(command);
		device.setId(Long.valueOf(command.getDeviceId()));
		return device;
	}

	/**
	 * 返回设备信息json数据
	 * @param device 设备
	 * @return
	 */
	private String generatePreviousDeviceState(Device device) {
		Map<String, Object> _map = Maps.newHashMap();
		_map.put("phoneNo", device.getPhoneNo());
		_map.put("meid", device.getMeid());
		_map.put("model", device.getModel());
		_map.put("currentAmount", device.getCurrentAmount());
		_map.put("consumeCategory", device.getConsumeCategory());
		_map.put("status", device.getStatus());
		_map.put("areaCode", device.getAreaCode());
		return JsonUtil.toJson(_map);
	}

	// private String

}
