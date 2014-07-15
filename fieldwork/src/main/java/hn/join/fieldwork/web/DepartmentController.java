package hn.join.fieldwork.web;

import hn.join.fieldwork.domain.Department;
import hn.join.fieldwork.persistence.SQLQueryResult;
import hn.join.fieldwork.service.DepartmentService;
import hn.join.fieldwork.utils.JsonUtil;
import hn.join.fieldwork.utils.SqlUtil;
import hn.join.fieldwork.utils.SystemConstants;
import hn.join.fieldwork.web.command.CreateDepartmentCommand;

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
 * 部门维护
 * @author chenjinlong
 *
 */
@Controller
@RequestMapping(value = "/web/department/")
public class DepartmentController extends BaseController {
	
	
	public final static String listDepartmentPermission = "department:list";

	public final static String createDepartmentPermission = "department:create";

	public final static String removeDepartmentPermission = "department:remove";

	private static final Logger LOG = Logger
			.getLogger(DepartmentController.class);

	@Autowired
	private DepartmentService departmentService;
	
	@PostConstruct
	public void init(){
		this.addPermission(listDepartmentPermission);
		this.addPermission(createDepartmentPermission);
		this.addPermission(removeDepartmentPermission);
	}

	@RequestMapping(method = RequestMethod.GET)
	public String departmentIndex() {
		return "syncMgr";
	}
	/**
	 * 创建部门 
	 * @param currentUserId
	 * @param command
	 * @return
	 */
	@RequestMapping(value = "{currentUserId}/create", method = RequestMethod.POST, headers = "Accept=text/plain")
	@ResponseBody
	public String createDepartment(@PathVariable Long currentUserId,
			CreateDepartmentCommand command) {

		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;

		try {
			departmentService.newDepartment(command);
			resultCode = SystemConstants.result_code_on_success;
		} catch (DataAccessException ex) {
			LOG.error("创建部门失败.command:" + command, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);

	}
	/**
	 * 删除部门 
	 * @param currentUserId
	 * @param departmentId
	 * @return
	 */
	@RequestMapping(value = "{currentUserId}/remove", method = RequestMethod.POST, headers = "Accept=text/plain")
	@ResponseBody
	public String removeDepartment(@PathVariable Long currentUserId,
			@RequestParam(required = true) Integer departmentId) {

		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;

		try {
			departmentService.removeDepartment(departmentId);
			resultCode = SystemConstants.result_code_on_success;
		} catch (DataAccessException ex) {
			LOG.error("删除部门失败.departmentId:" + departmentId, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);

	}
	/**
	 * 查找部门
	 * @param currentUserId
	 * @param searchContent
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(value = "{currentUserId}/find", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public String findDepartment(@PathVariable Long currentUserId,
			@RequestParam(required = false) String searchContent,
			@RequestParam(required = true) Integer page,
			@RequestParam(required = true) Integer rows) {

		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;

		String _departmentName = null;
		if (!StringUtils.isEmpty(searchContent)) {
			_departmentName = SqlUtil.matchAnywhere(searchContent);
		}

		try {
			SQLQueryResult<Department> queryResult = departmentService.findBy(
					_departmentName, page, rows);
			results.put("total", queryResult.getTotal());
			results.put("rows", queryResult.getRows());
			resultCode = SystemConstants.result_code_on_success;
		} catch (Exception ex) {
			LOG.error("查询部门失败,departmentName:" + searchContent + ",page:"
					+ page + ",rows:" + rows, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);

	}

}
