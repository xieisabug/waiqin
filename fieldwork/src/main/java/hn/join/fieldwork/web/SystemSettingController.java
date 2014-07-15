package hn.join.fieldwork.web;

import hn.join.fieldwork.service.SystemParameterService;
import hn.join.fieldwork.utils.JsonUtil;
import hn.join.fieldwork.utils.SystemConstants;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
/**
 * 系统参数设置控制器
 * @author chenjinlong
 *
 */
@Controller
@RequestMapping(value = "/web/system-setting/")
public class SystemSettingController extends BaseController {

	private static final Logger logger = Logger
			.getLogger(SystemSettingController.class);

	@Autowired
	private SystemParameterService systemParameterService;
	/**
	 * 进入系统设置页面
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String systemSettingIndex(Model model) {
		model.addAttribute(
				SystemParameterService.PARAM_NAME_VISIT_TOKEN_PREFIX,
				systemParameterService
						.getValueByName(SystemParameterService.PARAM_NAME_VISIT_TOKEN_PREFIX));
		model.addAttribute(
				SystemParameterService.PARAM_NAME_REPAIR_TOKEN_PREFIX,
				systemParameterService
						.getValueByName(SystemParameterService.PARAM_NAME_REPAIR_TOKEN_PREFIX));
		model.addAttribute(
				SystemParameterService.PARAM_NAME_ORDER_TIMEOUT,
				systemParameterService
						.getValueByName(SystemParameterService.PARAM_NAME_ORDER_TIMEOUT));
		model.addAttribute(
				SystemParameterService.PARAM_NAME_SMS_CUSTOMER_ON_ACCEPT_ENABLE,
				systemParameterService
						.getValueByName(SystemParameterService.PARAM_NAME_SMS_CUSTOMER_ON_ACCEPT_ENABLE));
		model.addAttribute(
				SystemParameterService.PARAM_NAME_SMS_CUSTOMER_ON_ACCEPT_MESSAGE,
				systemParameterService
						.getValueByName(SystemParameterService.PARAM_NAME_SMS_CUSTOMER_ON_ACCEPT_MESSAGE));
		model.addAttribute(
				SystemParameterService.PARAM_NAME_SMS_FIELDWORKER_ON_TIMEOUT_MESSAGE,
				systemParameterService
						.getValueByName(SystemParameterService.PARAM_NAME_SMS_FIELDWORKER_ON_TIMEOUT_MESSAGE));
		model.addAttribute(
				SystemParameterService.PARAM_NAME_SMS_FIELDWORKER_ON_TIMEOUT_ENABLE,
				systemParameterService
						.getValueByName(SystemParameterService.PARAM_NAME_SMS_FIELDWORKER_ON_TIMEOUT_ENABLE));
		return "systemParams";
	}
/**
 * 修改系统参数
 * @param paramName
 * @param paramValue
 * @return
 */
	@RequestMapping(value = "edit", method = RequestMethod.POST,headers = "Accept=application/json")
	@ResponseBody
	public String editSystemParameter(
			@RequestParam(required = true) String paramName,
			@RequestParam(required = true) String paramValue) {
		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;
		try {
			systemParameterService.updateParameter(paramName, paramValue);
			resultCode = SystemConstants.result_code_on_success;
		} catch (Exception ex) {
			logger.error("修改系统参数失败,paramName:" + paramValue, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);
	}

}
