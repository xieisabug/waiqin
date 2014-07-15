package hn.join.fieldwork.web;

import hn.join.fieldwork.service.PasswordNotMatchException;
import hn.join.fieldwork.service.UserService;
import hn.join.fieldwork.utils.JsonUtil;
import hn.join.fieldwork.utils.SystemConstants;
import hn.join.fieldwork.web.command.PersonSettingCommand;

import java.util.Map;

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
 * 用户设置控制器
 * @author chenjinlong
 *
 */
@Controller
@RequestMapping(value = "/web/person-setting/")
public class PersonSettingController {

	private static final Logger LOG = Logger
			.getLogger(PersonSettingController.class);

	@Autowired
	private UserService userService;

	@RequestMapping(value = "{currentUserId}", method = RequestMethod.GET)
	public String personSettingIndex(@PathVariable Long currentUserId) {
		return "countMgr";
	}
	/**
	 * 编辑个人信息
	 * @param currentUserId
	 * @param command
	 * @return
	 */
	@RequestMapping(value = "{currentUserId}/edit", method = RequestMethod.POST, headers = "Accept=text/plain")
	@ResponseBody
	public String editPersonSetting(@PathVariable Long currentUserId,
			PersonSettingCommand command) {
		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;
		try {
			userService.editPersonSetting(currentUserId, command);
			resultCode = SystemConstants.result_code_on_success;
		} catch (Exception ex) {
			LOG.error("编辑个人信息失败,command" + command, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);
	}
	/**
	 * 修改密码
	 * @param currentUserId
	 * @param originalPassword
	 * @param newPassword
	 * @return
	 */
	@RequestMapping(value = "{currentUserId}/update-my-password", method = RequestMethod.POST, headers = "Accept=text/plain")
	@ResponseBody
	public String updateMyPassword(@PathVariable Long currentUserId,
			@RequestParam(required = true) String originalPassword,
			@RequestParam(required = true) String newPassword) {

		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;
		try {
			userService.updateMyPassword(currentUserId, originalPassword,
					newPassword);
			resultCode = SystemConstants.result_code_on_success;
		} catch (PasswordNotMatchException ex) {
			results.put("error", "password_no_match");
		} catch (Exception ex) {
			LOG.error("修改密码失败,userId:" + currentUserId, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);
	}

}
