package hn.join.fieldwork.web;

import hn.join.fieldwork.domain.Device;
import hn.join.fieldwork.domain.DeviceJournal;
import hn.join.fieldwork.persistence.SQLQueryResult;
import hn.join.fieldwork.service.DeviceService;
import hn.join.fieldwork.utils.JsonUtil;
import hn.join.fieldwork.utils.SqlUtil;
import hn.join.fieldwork.utils.SystemConstants;
import hn.join.fieldwork.web.command.CreateDeviceCommand;
import hn.join.fieldwork.web.command.EditDeviceCommand;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
/**
 * 设备管理控制器
 * @author chenjinlong
 *
 */
@Controller
@RequestMapping(value = "/web/device/")
public class DeviceController extends BaseController {

	private static final Logger LOG = Logger.getLogger(DeviceController.class);

	public final static String listDevicePermission = "device:list";

	public final static String createDevicePermission = "device:create";

	public final static String editDevicePermission = "device:edit";

	public final static String removeDevicePermission = "device:remove";

	@Autowired
	private DeviceService deviceService;

	public DeviceController() {

	}

	@PostConstruct
	public void init() {
		this.addPermission(listDevicePermission);
		this.addPermission(createDevicePermission);
		this.addPermission(editDevicePermission);
		this.addPermission(removeDevicePermission);
	}

	@RequestMapping(method = RequestMethod.GET)
	public String deviceIndex() {
		return "deviceMgr";
	}
	/**
	 * 增加设备
	 * @param currentUserId
	 * @param command
	 * @return
	 */
	@RequestMapping(value = "{currentUserId}/create", method = RequestMethod.POST, headers = "Accept=text/plain")
	@ResponseBody
	public String createDevice(@PathVariable Long currentUserId,
			CreateDeviceCommand command) {
		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;

		try {
			deviceService.newDevice(command);
			resultCode = SystemConstants.result_code_on_success;
		} catch (DataAccessException ex) {
			LOG.error("创建设备失败.command:" + command, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);

	}
	/**
	 * 修改设备信息
	 * @param currentUserId
	 * @param command
	 * @return
	 */
	@RequestMapping(value = "{currentUserId}/edit", method = RequestMethod.POST, headers = "Accept=text/plain")
	@ResponseBody
	public String editDevice(@PathVariable Long currentUserId,
			EditDeviceCommand command) {
		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;

		try {
			deviceService.editDevice(command, currentUserId);
			resultCode = SystemConstants.result_code_on_success;
		} catch (DataAccessException ex) {
			LOG.error("编辑设备失败.command:" + command, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);

	}
	/**
	 * 删除设备
	 * @param currentUserId
	 * @param deviceId
	 * @param memo
	 * @return
	 */
	@RequestMapping(value = "{currentUserId}/remove", method = RequestMethod.POST, headers = "Accept=text/plain")
	@ResponseBody
	public String removeDevice(@PathVariable Long currentUserId,
			@RequestParam(required = true) Long deviceId,
			@RequestParam(required = false) String memo) {
		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;

		try {
			deviceService.removeDevice(deviceId, memo, currentUserId);
			resultCode = SystemConstants.result_code_on_success;
		} catch (DataAccessException ex) {
			LOG.error("移除设备失败.deviceId:" + deviceId + ",memo:" + memo, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);
	}
	/**
	 * 查找设备信息
	 * @param currentUserId
	 * @param areaCode
	 * @param meid
	 * @param phoneNo
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(value = "{currentUserId}/find", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public String findDevice(@PathVariable Long currentUserId,
			@RequestParam(required=false) String areaCode,
			@RequestParam(required = false) String meid,
			@RequestParam(required = false) String phoneNo,
			@RequestParam(required = true) Integer page,
			@RequestParam(required = true) Integer rows) {

		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;

		String _meid = null;
		if (!StringUtils.isEmpty(meid)) {
			_meid = SqlUtil.matchAnywhere(meid);
		}

		String _phoneNo = null;
		if (!StringUtils.isEmpty(phoneNo)) {
			_phoneNo = SqlUtil.matchAnywhere(phoneNo);
		}

		try {
			SQLQueryResult<Device> queryResult = deviceService.findBy(areaCode,_meid,
					_phoneNo, page, rows);
			results.put("total", queryResult.getTotal());
			results.put("rows", queryResult.getRows());
			resultCode = SystemConstants.result_code_on_success;
		} catch (Exception ex) {
			LOG.error("查询设备失败,meid:" + meid + ",phoneNo:" + phoneNo + ",page:"
					+ page + ",rows:" + rows, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);

	}
	/**
	 * 检查手机meid是否可用
	 * @param currentUserId
	 * @param meid
	 * @return
	 */
	@RequestMapping(value = "{currentUserId}/check-meid", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public String checkMeidAvailable(@PathVariable Long currentUserId,
			@RequestParam(required = true) String meid) {

		boolean available = false;
		try {
			available = deviceService.checkMeidAvailable(meid);
		} catch (Exception ex) {
			LOG.error("检查设备ID号是否可用失败,currentUserId:" + currentUserId + ",meid:"
					+ meid, ex);
		}
		return String.valueOf(available);
	}
	/**
	 * 查询空闲设备
	 * @param currentUserId
	 * @param areaCode
	 * @param meid
	 * @param phoneNo
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(value = "{currentUserId}/find-idle", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public String findDeviceByIdle(@PathVariable Long currentUserId,
			@RequestParam(required=false) String areaCode,
			@RequestParam(required = false) String meid,
			@RequestParam(required = false) String phoneNo,
			@RequestParam(required = true) Integer page,
			@RequestParam(required = true) Integer rows) {
		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;

		String _meid = null;
		if (!StringUtils.isEmpty(meid)) {
			_meid = SqlUtil.matchAnywhere(meid);
		}

		String _phoneNo = null;
		if (!StringUtils.isEmpty(phoneNo)) {
			_phoneNo = SqlUtil.matchAnywhere(phoneNo);
		}

		try {
			SQLQueryResult<Device> queryResult = deviceService.findByIdle(areaCode,
					_meid, _phoneNo, page, rows);
			results.put("total", queryResult.getTotal());
			results.put("rows", queryResult.getRows());
			resultCode = SystemConstants.result_code_on_success;
		} catch (Exception ex) {
			LOG.error("查询空闲设备失败,meid:" + meid + ",phoneNo:" + phoneNo
					+ ",page:" + page + ",rows:" + rows, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);
	}
	/**
	 * 查看设备以往记录
	 * @param currentUserId
	 * @param deviceId
	 * @return
	 */
	@RequestMapping(value = "{currentUserId}/load-journal", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public String loadDeviceJournal(@PathVariable Long currentUserId,
			@RequestParam(required = true) Long deviceId) {
		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;
		try {
			List<DeviceJournal> journals = deviceService
					.loadDeviceJournal(deviceId);
			results.put("journals", journals);
			resultCode=SystemConstants.result_code_on_success;
		} catch (Exception ex) {
			LOG.error("加载日志记录失败,deviceId:"+deviceId, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);
	}

}
