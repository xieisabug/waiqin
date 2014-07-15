package hn.join.fieldwork.web;

import hn.join.fieldwork.domain.User;
import hn.join.fieldwork.persistence.SQLQueryResult;
import hn.join.fieldwork.service.UserService;
import hn.join.fieldwork.utils.JsonUtil;
import hn.join.fieldwork.utils.SqlUtil;
import hn.join.fieldwork.utils.SystemConstants;
import hn.join.fieldwork.web.command.CreateUserCommand;
import hn.join.fieldwork.web.command.EditUserCommand;

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

import com.alibaba.druid.util.StringUtils;
import com.google.common.collect.Maps;
/**
 * 用户管理控制器
 * @author chenjinlong
 *
 */
@Controller
@RequestMapping(value = "/web/user/")
public class UserController extends BaseController {

	private static final Logger LOG = Logger.getLogger(UserController.class);

	public static final String listUserPermission = "user:list";

	public static final String createUserPermission = "user:create";

	public static final String editUserPermission = "user:edit";

	public static final String removeUserPermission = "user:remove";

	@Autowired
	private UserService userService;

	public UserController() {

	}

	@PostConstruct
	public void init() {
		this.addPermission(listUserPermission);
		this.addPermission(createUserPermission);
		this.addPermission(editUserPermission);
		this.addPermission(removeUserPermission);
	}

	@RequestMapping(value = "{currentUserId}", method = RequestMethod.GET)
	public String userIndex(@PathVariable Long currentUserId) {
		return "adminsMgr";
	}
	/**
	 * 创建用户
	 * @param currentUserId
	 * @param command
	 * @return
	 */
	@RequestMapping(value = "{currentUserId}/create", method = RequestMethod.POST, headers = "Accept=text/plain")
	@ResponseBody
	public String createUser(@PathVariable Long currentUserId,
			CreateUserCommand command) {
		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;

		try {
			userService.newUser(command);
			resultCode = SystemConstants.result_code_on_success;
		} catch (Exception ex) {
			LOG.error("创建用户失败.command:" + command, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);

	}
	/**
	 * 编辑用户
	 * @param currentUserId
	 * @param command
	 * @return
	 */
	@RequestMapping(value = "{currentUserId}/edit", method = RequestMethod.POST, headers = "Accept=text/plain")
	@ResponseBody
	public String editUser(@PathVariable Long currentUserId,
			EditUserCommand command) {
		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;

		try {
			userService.editUser(command);
			resultCode = SystemConstants.result_code_on_success;
		} catch (Exception ex) {
			LOG.error("编辑用户失败,command:" + command, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);
	}
	/**
	 * 修改用户密码
	 * @param currentUserId
	 * @param userId
	 * @param newPassword
	 * @return
	 */
	@RequestMapping(value = "{currentUserId}/change-password", method = RequestMethod.POST, headers = "Accept=text/plain")
	@ResponseBody
	public String changePassword(@PathVariable Long currentUserId,@RequestParam Long userId,@RequestParam String newPassword){
		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;

		try {
			userService.changeUserPassword(userId,newPassword);
			resultCode = SystemConstants.result_code_on_success;
		} catch (Exception ex) {
			LOG.error("修改用户密码失败,userId:" + userId, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);
		
	}
	
	
	/**
	 * 删除用户
	 * @param currentUserId
	 * @param userId
	 * @return
	 */

	@RequestMapping(value = "{currentUserId}/remove", method = RequestMethod.POST, headers = "Accept=text/plain")
	@ResponseBody
	public String removeUser(@PathVariable Long currentUserId,
			@RequestParam(required = true) String userId) {
		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;
		try {
			userService.removeUser(userId);
			resultCode = SystemConstants.result_code_on_success;
		} catch (Exception ex) {
			LOG.error("删除用户失败,userId:" + userId, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);
	}

	@RequestMapping(value = "{currentUserId}/find", method = RequestMethod.POST, headers = "Accept=text/plain")
	@ResponseBody
	public String findUser(@PathVariable Long currentUserId,
			@RequestParam(required = false) String fullname,
			@RequestParam(required = false) String mobileNo,
			@RequestParam(required = false) String areaCode,
			@RequestParam(required = true) Integer page,
			@RequestParam(required = true) Integer rows) {

		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;

		String _fullname = null;
		if (!StringUtils.isEmpty(fullname)) {
			_fullname = SqlUtil.matchAnywhere(fullname);
		}

		String _mobileNo = null;
		if (!StringUtils.isEmpty(mobileNo)) {
			_mobileNo = SqlUtil.matchAnywhere(mobileNo);
		}

		try {
			SQLQueryResult<User> queryResult = userService.findBy(_fullname,
					_mobileNo, areaCode, currentUserId, page, rows);
			results.put("total", queryResult.getTotal());
			results.put("rows", queryResult.getRows());
			resultCode = SystemConstants.result_code_on_success;
		} catch (Exception ex) {
			LOG.error("查找用户失败,userId:" + currentUserId + ",fullname:"
					+ fullname + ",mobileNo:" + mobileNo + ",areaCode:"
					+ areaCode + ",page:" + page + ",rows" + rows, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);
	}
	/**
	 * 加载用户信息
	 * @param currentUserId
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "{currentUserId}/load", method = RequestMethod.POST, headers = "Accept=text/plain")
	@ResponseBody
	public String loadUser(@PathVariable Long currentUserId,
			@RequestParam(required = true) Long userId) {
		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;
		try {
			User user = userService.getUser(userId);
			results.put("user", user);
			resultCode = SystemConstants.result_code_on_success;
		} catch (Exception ex) {
			LOG.error("加载用户失败,currentUserId:" + currentUserId + ",userId:"
					+ userId, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);
	}

	@RequestMapping(value = "{currentUserId}/check-username", method = RequestMethod.POST, headers = "Accept=text/plain")
	@ResponseBody
	public String checkUsernameAvailable(@PathVariable Long currentUserId,
			@RequestParam(required = true) String username) {

		boolean available = false;
		try {
			available = userService.checkUsernameAvailable(username);
		} catch (Exception ex) {
			LOG.error("检查用户名是否可用失败,currentUserId:" + currentUserId
					+ ",username:" + username, ex);
		}
		return String.valueOf(available);
	}

	@RequestMapping(value = "{currentUserId}/check-email", method = RequestMethod.POST, headers = "Accept=text/plain")
	@ResponseBody
	public String checkEmailAvailable(@PathVariable Long currentUserId,
			@RequestParam(required = true) String email) {

		boolean available = false;
		try {
			available = userService.checkEmailAvailable(email);
		} catch (Exception ex) {
			LOG.error("检查邮箱名是否可用失败,currentUserId:" + currentUserId + ",email:"
					+ email, ex);
		}
		return String.valueOf(available);
	}
	/**
	 * 检查工号是否可用 
	 * @param currentUserId
	 * @param workerNo
	 * @return
	 */
	@RequestMapping(value = "{currentUserId}/check-workerno", method = RequestMethod.POST, headers = "Accept=text/plain")
	@ResponseBody
	public String checkWorkerNoAvailable(@PathVariable Long currentUserId,
			@RequestParam(required = true) String workerNo) {

		boolean available = false;
		try {
			available = userService.checkWorkerNoAvailable(workerNo);
		} catch (Exception ex) {
			LOG.error("检查工号是否可用失败,currentUserId:" + currentUserId
					+ ",workerNo:" + workerNo, ex);
		}
		return String.valueOf(available);
	}
	/**
	 * 检查手机号是否可用 
	 * @param currentUserId
	 * @param mobileNo
	 * @return
	 */
	@RequestMapping(value = "{currentUserId}/check-mobileno", method = RequestMethod.POST, headers = "Accept=text/plain")
	@ResponseBody
	public String checkMobileNoAvailable(@PathVariable Long currentUserId,
			@RequestParam(required = true) String mobileNo) {

		boolean available = false;
		try {
			available = userService.checkMobileNoAvailable(mobileNo);
		} catch (Exception ex) {
			LOG.error("检查手机号是否可用失败,currentUserId:" + currentUserId
					+ ",mobileNo:" + mobileNo, ex);
		}
		return String.valueOf(available);
	}

}
