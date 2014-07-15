package hn.join.fieldwork.web;

import hn.join.fieldwork.domain.FieldWorker;
import hn.join.fieldwork.service.CheckinService;
import hn.join.fieldwork.service.CheckinService.CheckinResult;
import hn.join.fieldwork.service.FieldWorkerService;
import hn.join.fieldwork.utils.JsonUtil;
import hn.join.fieldwork.utils.SystemConstants;
import hn.join.fieldwork.web.command.FieldWorkerLoginCommand;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
/**
 * 外勤人员人员安全管理
 * @author chenjinlong
 *
 */
@Controller
public class FieldWorkerSecurityController {

	private static final Logger LOG = Logger
			.getLogger(FieldWorkerSecurityController.class);

	@Autowired
	private FieldWorkerService fieldWorkerService;

	@Autowired
	private CheckinService checkinService;
	/**
	 * 外勤人员登录
	 * @param command
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/fieldworker/login", headers = "Accept=application/json")
	public @ResponseBody
	String login(@ModelAttribute FieldWorkerLoginCommand command,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;
		try {
			FieldWorker fieldWorker = fieldWorkerService.login(command);
			
			if (fieldWorker != null) {
				resultCode = SystemConstants.result_code_on_success;
				results.put("fieldWorker", fieldWorker);
			}else{
				resultCode = SystemConstants.result_code_on_auth_fail;
			}
		} catch (Exception ex) {
			LOG.error("外勤人员登录失败,command:" + command, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);
	}

	// @RequestMapping(value = "/fieldworker/get", method = RequestMethod.GET,
	// headers = "Accept=application/json")
	// @ResponseBody
	// public String get(@RequestParam Long fieldWorkerId) {
	// Map<String, Object> results = Maps.newHashMap();
	// FieldWorker o = fieldWorkerService.getById(fieldWorkerId);
	//
	// results.put("o", o);
	// return JsonUtil.toJson(results);
	//
	// }
	/**
	 * 外勤人员签到
	 * @param fieldWorkerNo
	 * @return
	 */
	@RequestMapping(value = "/fieldworker/checkin", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public String checkin(@RequestParam(required = true) String fieldWorkerNo) {
		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_success;
		Long fieldWorkerId = Long.parseLong(fieldWorkerNo);
		CheckinResult result;
		try {
			result = checkinService.newCheckin(fieldWorkerId);
		} catch (Exception ex) {
			LOG.error("外勤人员签到失败,fieldWorkerNo:" + fieldWorkerNo, ex);
			result = CheckinResult.checkin_fail_ex;

		}
		results.put("result", result.getCode());
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);

	}
	/**
	 * 外勤人员修改密码
	 * @param fieldWorkerNo
	 * @param oldPassword
	 * @param newPassword
	 * @return
	 */
	@RequestMapping(value = "/fieldworker/change-password", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody
	String changePassword(@RequestParam(required = true) String fieldWorkerNo,
			@RequestParam(required = true) String oldPassword,
			@RequestParam(required = true) String newPassword) {
		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;
		Long fieldWorkerId = Long.parseLong(fieldWorkerNo);
		try {
			boolean success = fieldWorkerService.changePassword(fieldWorkerId,
					oldPassword, newPassword);
			if (success)
				resultCode = SystemConstants.result_code_on_success;
			else {
				results.put("message", "员工工号与原始密码不匹配");
			}
		} catch (Exception ex) {
			LOG.error("外勤人员修改密码失败,fieldWorkerNo:" + fieldWorkerNo, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);

	}
}
