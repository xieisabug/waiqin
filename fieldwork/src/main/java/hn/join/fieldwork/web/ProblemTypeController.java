package hn.join.fieldwork.web;

import hn.join.fieldwork.domain.ProblemType;
import hn.join.fieldwork.persistence.SQLQueryResult;
import hn.join.fieldwork.service.ProblemTypeService;
import hn.join.fieldwork.utils.JsonUtil;
import hn.join.fieldwork.utils.SqlUtil;
import hn.join.fieldwork.utils.SystemConstants;

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
 * 问题类型管理控制器
 * @author chenjinlong
 *
 */
@Controller
@RequestMapping(value = "/web/problem-type/")
public class ProblemTypeController extends BaseController{
	
	
	public final static String listProblemTypePermission = "problemType:list";

	public final static String createProblemTypePermission = "problemType:create";

	public final static String removeProblemTypePermission = "problemType:remove";
	
	

	private static final Logger LOG = Logger
			.getLogger(ProblemTypeController.class);

	@Autowired
	private ProblemTypeService problemTypeService;
	
	@PostConstruct
	public void init(){
		this.addPermission(listProblemTypePermission);
		this.addPermission(createProblemTypePermission);
		this.addPermission(removeProblemTypePermission);
	}

	@RequestMapping(method = RequestMethod.GET)
	public String problemTypeIndex() {
		return "problem-type-index";
	}
	/**
	 * 创建问题类型
	 * @param currentUserId
	 * @param problemTypeId
	 * @param problemTypeName
	 * @param problemCategoryId
	 * @return
	 */
	@RequestMapping(value = "{currentUserId}/create", method = RequestMethod.POST, headers = "Accept=text/plain")
	@ResponseBody
	public String createProblemType(@PathVariable Long currentUserId,
			@RequestParam(required = true) Integer problemTypeId,
			@RequestParam(required = true) String problemTypeName,
			@RequestParam(required = true) Integer problemCategoryId) {

		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;

		try {
			problemTypeService.newProblemType(problemTypeId, problemTypeName,
					problemCategoryId);
			resultCode = SystemConstants.result_code_on_success;
		} catch (DataAccessException ex) {
			LOG.error("创建问题类型失败.problemTypeId:" + problemTypeId
					+ ",problemTypeName:" + problemTypeName
					+ ",problemCategoryId:" + problemCategoryId, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);

	}	
	/**
	 * 删除问题类型
	 * @param currentUserId
	 * @param problemTypeId
	 * @return
	 */
	@RequestMapping(value = "{currentUserId}/remove", method = RequestMethod.POST, headers = "Accept=text/plain")
	@ResponseBody
	public String removeProblemType(@PathVariable Long currentUserId,
			@RequestParam(required = true) Integer problemTypeId) {
		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;

		try {
			problemTypeService.removeProblemType(problemTypeId);
			resultCode = SystemConstants.result_code_on_success;
		} catch (DataAccessException ex) {
			LOG.error("删除问题类型失败.problemTypeId:" + problemTypeId, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);
	}
	/**
	 * 查找问题类型
	 * @param currentUserId
	 * @param searchContent
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(value = "{currentUserId}/find", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public String findProblemType(@PathVariable Long currentUserId,
			@RequestParam(required = false) String searchContent,
			@RequestParam(required = true) Integer page,
			@RequestParam(required = true) Integer rows) {

		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;

		String _problemTypeName = null;
		if (!StringUtils.isEmpty(searchContent)) {
			_problemTypeName = SqlUtil.matchAnywhere(searchContent);
		}

		try {
			SQLQueryResult<ProblemType> queryResult = problemTypeService
					.findBy(_problemTypeName, page, rows);
			results.put("total", queryResult.getTotal());
			results.put("rows", queryResult.getRows());
			resultCode = SystemConstants.result_code_on_success;
		} catch (Exception ex) {
			LOG.error("查询问题类型失败,problemTypeName:" + searchContent + ",page:"
					+ page + ",rows:" + rows, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);

	}

}
