package hn.join.fieldwork.web;

import hn.join.fieldwork.domain.Role;
import hn.join.fieldwork.persistence.SQLQueryResult;
import hn.join.fieldwork.service.RoleService;
import hn.join.fieldwork.utils.JsonUtil;
import hn.join.fieldwork.utils.SystemConstants;
import hn.join.fieldwork.web.command.CreateRoleCommand;
import hn.join.fieldwork.web.command.EditRoleCommand;

import java.util.Collections;
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
 * 角色管理控制器
 * @author chenjinlong
 *
 */
@Controller
@RequestMapping(value = "/web/role/")
public class RoleController extends BaseController {

	private static final Logger LOG = Logger.getLogger(RoleController.class);

	public static final String listRolePermission = "role:list";

	public static final String createRolePermission = "role:create";

	public static final String editRolePermission = "role:edit";

	public static final String removeRolePermission = "role:remove";

	@PostConstruct
	public void init() {
		this.addPermission(listRolePermission);
		this.addPermission(createRolePermission);
		this.addPermission(editRolePermission);
		this.addPermission(removeRolePermission);
	}

	@Autowired
	private RoleService roleService;

	@RequestMapping(value = "{currentUserId}", method = RequestMethod.GET)
	public String roleIndex(@PathVariable Long currentUserId) {
		return "roleMgr";
	}
	/**
	 * 加载角色所有权限
	 * @return
	 */
	@RequestMapping(value = "load-all-permission", method = RequestMethod.GET, headers = "Accept=text/plain")
	@ResponseBody
	public String loadAllPermissions() {
		return JsonUtil.toJson(permissionRepository.getPermissionTree());
	}
	/**
	 * 查找角色
	 * @param currentUserId
	 * @param roleName
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(value = "{currentUserId}/find", method = RequestMethod.POST, headers = "Accept=text/plain")
	@ResponseBody
	public String findRole(@PathVariable Long currentUserId,
			@RequestParam(required = false) String roleName,
			@RequestParam(required = true) Integer page,
			@RequestParam(required = true) Integer rows) {

		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;

		try {
			SQLQueryResult<Role> queryResult = roleService.findBy(roleName,
					page, rows);
			results.put("total", queryResult.getTotal());
			results.put("rows", queryResult.getRows());
			resultCode = SystemConstants.result_code_on_success;
		} catch (Exception ex) {
			LOG.error("查找角色失败,userId:" + currentUserId + ",roleName:"
					+ roleName + ",page:" + page + ",rows" + rows, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);
	}
	/**
	 * 加载角色信息
	 * @param currentUserId
	 * @param roleId
	 * @return
	 */
	@RequestMapping(value = "{currentUserId}/load", method = RequestMethod.POST, headers = "Accept=text/plain")
	@ResponseBody
	public String loadRole(@PathVariable Long currentUserId,
			@RequestParam(required = true) Integer roleId) {
		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;
		try {
			Role role = roleService.getById(roleId);
			results.put("role", role);
			resultCode = SystemConstants.result_code_on_success;
		} catch (Exception ex) {
			LOG.error("加载角色失败,currentUserId:" + currentUserId + ",roleId:"
					+ roleId, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);
	}
	/**
	 * 创建角色
	 * @param currentUserId
	 * @param command
	 * @return
	 */
	@RequestMapping(value = "{currentUserId}/create", method = RequestMethod.POST, headers = "Accept=text/plain")
	@ResponseBody
	public String createRole(@PathVariable Long currentUserId,
			CreateRoleCommand command) {
		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;

		try {
			roleService.newRole(command);
			resultCode = SystemConstants.result_code_on_success;
		} catch (Exception ex) {
			LOG.error("创建角色失败.command:" + command, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);
	}
	/**
	 * 编辑角色
	 * @param currentUserId
	 * @param command
	 * @return
	 */
	@RequestMapping(value = "{currentUserId}/edit", method = RequestMethod.POST, headers = "Accept=text/plain")
	@ResponseBody
	public String editUser(@PathVariable Long currentUserId,
			EditRoleCommand command) {
		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;

		try {
			roleService.editRole(command);
			resultCode = SystemConstants.result_code_on_success;
		} catch (Exception ex) {
			LOG.error("编辑角色失败,command:" + command, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);
	}
	/**
	 * 删除角色
	 * @param currentUserId
	 * @param roleId
	 * @return
	 */
	@RequestMapping(value = "{currentUserId}/remove", method = RequestMethod.POST, headers = "Accept=text/plain")
	@ResponseBody
	public String removeRole(@PathVariable Long currentUserId,
			@RequestParam(required = true) Integer roleId) {
		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;
		try {
			roleService.removeRole(roleId);
			resultCode = SystemConstants.result_code_on_success;
		} catch (Exception ex) {
			LOG.error("删除角色失败,roleId:" + roleId, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);
	}
	/**
	 * 加载所有角色
	 * @param currentUserId
	 * @return
	 */
	@RequestMapping(value = "{currentUserId}/list-all", method = RequestMethod.POST, headers = "Accept=text/plain")
	@ResponseBody
	public String listAllRoles(@PathVariable Long currentUserId){
		List<Role> roles;
		try{
			roles=roleService.listAllRole();
		}catch(Exception ex){
			LOG.error("加载所有角色失败", ex);
			roles=Collections.emptyList();
		}
		return JsonUtil.toJson(roles);
	}

}
