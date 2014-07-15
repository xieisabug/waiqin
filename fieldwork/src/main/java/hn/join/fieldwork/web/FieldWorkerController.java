package hn.join.fieldwork.web;

import hn.join.fieldwork.domain.FieldWorker;
import hn.join.fieldwork.persistence.SQLQueryResult;
import hn.join.fieldwork.service.FieldWorkerService;
import hn.join.fieldwork.service.FieldWorkerService.LatestFieldWorkerInfoCommand;
import hn.join.fieldwork.utils.JsonUtil;
import hn.join.fieldwork.utils.StringUtil;
import hn.join.fieldwork.utils.SystemConstants;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
/**
 * 业务人员相关信息管理
 * @author chenjinlong
 *
 */
@Controller
@RequestMapping(value = "/web/fieldworker/")
public class FieldWorkerController extends BaseController{
	
	
	public final static String fieldWorkerListPermission = "fieldWorker:list";
	
	public final static String fieldWorkerResetPasswordPermission="fieldWorker:resetPassword";

	public final static String fieldWorkerDevicePermission = "fieldWorker:device";
	
	public final static String fieldWorkerLocatePermission="fieldWorker:locate";

	private static final Logger LOG = Logger
			.getLogger(FieldWorkerController.class);

	@Autowired
	private FieldWorkerService fieldWorkerService;
	
	@PostConstruct
	public void init(){
		this.addPermission(fieldWorkerListPermission);
		this.addPermission(fieldWorkerResetPasswordPermission);
		this.addPermission(fieldWorkerDevicePermission);
		this.addPermission(fieldWorkerLocatePermission);
	}

	@RequestMapping(method = RequestMethod.GET)
	public String fieldWorkerTrackIndex() {
		return "locationCheck";
	}

	@RequestMapping(method = RequestMethod.GET, value = "manage")
	public String fieldWorkerManagerIndex() {
		return "workersMgr";
	}
	/**
	 * 查找外勤人员
	 * @param currentUserId
	 * @param fullname
	 * @param phoneNo
	 * @param city
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(value = "{currentUserId}/find", method = RequestMethod.POST, headers = "Accept=text/plain")
	@ResponseBody
	public String findFieldWorker(@PathVariable Long currentUserId,
			@RequestParam(required = false) String fullname,
			@RequestParam(required = false) String phoneNo,
			@RequestParam(required = false) String city,
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer rows) {

		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;
		try {
			SQLQueryResult<FieldWorker> queryResult = fieldWorkerService
					.findBy(fullname, phoneNo, city, page, rows);
			results.put("total", queryResult.getTotal());
			results.put("rows", queryResult.getRows());
			resultCode = SystemConstants.result_code_on_success;
		} catch (Exception ex) {
			LOG.error("查找外勤人员失败,userId:" + currentUserId + ",fullname:"
					+ fullname + ",phoneNo:" + phoneNo + ",city:" + city
					+ ",page:" + page + ",rows" + rows, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);
	}
	/**
	 * 加载最新轨迹信息
	 * @param currentUserId
	 * @param workerNo
	 * @return
	 */
	@RequestMapping(value = "{currentUserId}/load-trackinfo", method = RequestMethod.POST, headers = "Accept=text/plain")
	@ResponseBody
	public String loadFieldWorkerTrackInfo(@PathVariable Long currentUserId,
			@RequestParam(required = true) String workerNo) {
		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;
		try {

			List<Long> fieldWorkerIdList = StringUtil.fromStringToLongList(
					workerNo, ",");
			List<LatestFieldWorkerInfoCommand> latestFieldWorkerInfoList = fieldWorkerService
					.findLatestFieldWorkerInfoOnLine(fieldWorkerIdList);
			results.put("trackInfoList", latestFieldWorkerInfoList);
			resultCode = SystemConstants.result_code_on_success;
		} catch (Exception ex) {
			LOG.error("加载最新轨迹信息失败,currentUserId:" + currentUserId
					+ ",workerNo:" + workerNo, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);

	}
	/**
	 * 修改外勤人员密码
	 * @param currentUserId
	 * @param fieldWorkerId
	 * @param newPassword
	 * @return
	 */
	@RequestMapping(value = "{currentUserId}/reset-password", method = RequestMethod.POST, headers = "Accept=text/plain")
	@ResponseBody
	public String resetPassword(@PathVariable Long currentUserId,
			@RequestParam(required = true) Long fieldWorkerId,
			@RequestParam(required = true) String newPassword) {

		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;
		try {
			fieldWorkerService.resetPassword(fieldWorkerId, newPassword);
			resultCode = SystemConstants.result_code_on_success;
		} catch (Exception ex) {
			LOG.error("外勤人员修改密码失败,fieldWorkerId:" + fieldWorkerId, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);

	}
	/**
	 * 绑定设备
	 * @param currentUserId
	 * @param fieldWorkerId
	 * @param deviceId
	 * @return
	 */
	@RequestMapping(value = "{currentUserId}/attach-device", method = RequestMethod.POST, headers = "Accept=text/plain")
	@ResponseBody
	public String attachDevice(@PathVariable Long currentUserId,
			@RequestParam(required = true) Long fieldWorkerId,
			@RequestParam(required = true) Long deviceId) {

		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;
		try {
			fieldWorkerService.attachDevice(fieldWorkerId, deviceId);
			resultCode = SystemConstants.result_code_on_success;
		} catch (Exception ex) {
			LOG.error("绑定设备失败,currentUserId:" + currentUserId
					+ ",fieldWorkerId:" + fieldWorkerId + ",deviceId:"
					+ deviceId, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);
	}
	/**
	 * 解绑设备
	 * @param currentUserId
	 * @param fieldWorkerId
	 * @return
	 */

	@RequestMapping(value = "{currentUserId}/unattach-device", method = RequestMethod.POST, headers = "Accept=text/plain")
	@ResponseBody
	public String unattachDevice(@PathVariable Long currentUserId,
			@RequestParam(required = true) Long fieldWorkerId) {
		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;
		try {
			fieldWorkerService.unattachDevice(fieldWorkerId);
			resultCode = SystemConstants.result_code_on_success;
		} catch (Exception ex) {
			LOG.error("解绑设备失败,currentUserId:" + currentUserId
					+ ",fieldWorkerId:" + fieldWorkerId, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);
	}
}
