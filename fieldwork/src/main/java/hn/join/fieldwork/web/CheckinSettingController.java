package hn.join.fieldwork.web;

import hn.join.fieldwork.domain.CheckinSetting;
import hn.join.fieldwork.persistence.SQLQueryResult;
import hn.join.fieldwork.service.CheckinService;
import hn.join.fieldwork.service.SystemParameterService;
import hn.join.fieldwork.utils.JsonUtil;
import hn.join.fieldwork.utils.SystemConstants;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;

@Controller
@RequestMapping(value = "/web/checkin-setting/")
public class CheckinSettingController extends BaseController {

	private static final Logger LOG = Logger
			.getLogger(CheckinSettingController.class);

//	public final static String listCheckinSettingPermission = "checkinSetting:list";

//	public final static String createCheckinSettingPermission = "checkinSetting:create";

	public final static String editCheckinSettingPermission = "checkinSetting:edit";

//	public final static String removeCheckinSettingPermission = "checkinSetting:remove";

	@Autowired
	private CheckinService checkinService;
	
	@Autowired
	private SystemParameterService systemParameterService; 

	public CheckinSettingController() {

	}

	@PostConstruct
	public void init() {
//		addPermission(listCheckinSettingPermission);
//		addPermission(createCheckinSettingPermission);
		addPermission(editCheckinSettingPermission);
//		addPermission(removeCheckinSettingPermission);
	}
	/**
	 * 显示签到设置页面，并设置初始信息
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String checkinSettingIndex(Model model) {
		String checkInMessage = systemParameterService.getValueByName(SystemParameterService.PARAM_NAME_CHECKIN_SMS_MESSAGE);
		String checkInTime = systemParameterService.getValueByName(SystemParameterService.PARAM_NAME_CHECKIN_TIME);
		String checkInAheadTime=systemParameterService.getValueByName(SystemParameterService.PARAM_NAME_CHECKIN_AHEAD_MINUTES);
		String checkInSmsEnable=systemParameterService.getValueByName(SystemParameterService.PARAM_NAME_CHECKIN_SMS_ENABLE);
		model.addAttribute("checkInMessage", checkInMessage);
		model.addAttribute("checkInTime", checkInTime);
		model.addAttribute("checkInAheadTime", checkInAheadTime);
		model.addAttribute("checkInSmsEnable", checkInSmsEnable);
		return "checkIn";

	}

//	@RequestMapping(value = "{currentUserId}/set-message-and-time", method = RequestMethod.POST, headers = "Accept=application/json")
//	@ResponseBody
//	public String setMessageAndTime(@PathVariable Long currentUserId,
//			@RequestParam(required = false) String message,
//			@RequestParam(required = false) String time) {
//		Map<String, Object> results = Maps.newHashMap();
//		int resultCode = SystemConstants.result_code_on_failure;
//		if (checkinService.setCheckInMessageAndTime(message, time)) {
//			resultCode = SystemConstants.result_code_on_success;
//		}
//		results.put("resultCode", resultCode);
//		return JsonUtil.toJson(results);
//
//	}
	/**
	 * 保存签到设置
	 * @param currentUserId
	 * @param fromDate
	 * @param toDate
	 * @param checkStatus
	 * @return
	 */
	@RequestMapping(value = "{currentUserId}/create", method = RequestMethod.POST, headers = "Accept=text/plain")
	@ResponseBody
	public String createCheckinSetting(@PathVariable Long currentUserId,
			@RequestParam(required = true) String fromDate,
			@RequestParam(required = true) String toDate,
			@RequestParam(required = true) String checkStatus) {

		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;
		try {
			checkinService.newCheckinSetting(fromDate, toDate, checkStatus,
					currentUserId);
			resultCode = SystemConstants.result_code_on_success;
		} catch (Exception ex) {
			LOG.error("签到日期设置失败,fromDate:" + fromDate + ",toDate:" + toDate
					+ ",checkStatus:" + checkStatus, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);
	}

	@RequestMapping(value = "{currentUserId}/find", method = RequestMethod.POST,headers = "Accept=text/plain")
	@ResponseBody
	public String findCheckinSetting(@PathVariable Long currentUserId,
			@RequestParam(required = false) String fromDate,
			@RequestParam(required = false) String toDate,
			@RequestParam(required = true) Integer page,
			@RequestParam(required = true) Integer rows) {
		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;
		try {
			SQLQueryResult<CheckinSetting> queryResult = checkinService.findCheckinSettingBy(
					fromDate, toDate, page, rows);
			results.put("total", queryResult.getTotal());
			results.put("rows", queryResult.getRows());
			resultCode = SystemConstants.result_code_on_success;
		} catch (Exception ex) {
			LOG.error("查询签到设定失败,fromDate:" + fromDate + ",toDate:" + toDate
					+ ",page:" + page + ",rows:" + rows, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);
	}

}
