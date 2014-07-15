package hn.join.fieldwork.web;

import hn.join.fieldwork.domain.ProblemCategory;
import hn.join.fieldwork.persistence.SQLQueryResult;
import hn.join.fieldwork.service.ProblemCategoryService;
import hn.join.fieldwork.service.ProductService;
import hn.join.fieldwork.utils.JsonUtil;
import hn.join.fieldwork.utils.SqlUtil;
import hn.join.fieldwork.utils.SystemConstants;

import java.util.Collections;
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
 * 问题类别管理 控制器
 * @author chenjinlong
 *
 */
@Controller
@RequestMapping(value = "/web/problem-category/")
public class ProblemCategoryController extends BaseController {

	public final static String listProblemCategoryPermission = "problemCategory:list";

	public final static String createProblemCategoryPermission = "problemCategory:create";

	public final static String removeProblemCategoryPermission = "problemCategory:remove";

	private static final Logger LOG = Logger
			.getLogger(ProblemCategoryController.class);

	@Autowired
	private ProblemCategoryService problemCategoryService;
	
	@Autowired
	private ProductService productService;

	@PostConstruct
	public void init() {
		this.addPermission(listProblemCategoryPermission);
		this.addPermission(createProblemCategoryPermission);
		this.addPermission(removeProblemCategoryPermission);
	}

	@RequestMapping(method = RequestMethod.GET)
	public String problemCategoryIndex() {
		return "problem-category-index";
	}
	/**
	 * 创建问题分类 
	 * @param currentUserId
	 * @param problemCategoryId
	 * @param problemCategoryName
	 * @param productId
	 * @return
	 */
	@RequestMapping(value = "{currentUserId}/create", method = RequestMethod.POST, headers = "Accept=text/plain")
	@ResponseBody
	public String createProblemCategory(@PathVariable Long currentUserId,
			@RequestParam(required = true) Integer problemCategoryId,
			@RequestParam(required = true) String problemCategoryName,
			@RequestParam(required = true) Integer productId) {
		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;

		try {
			String productName=productService.getNameById(productId);
			problemCategoryService.newProblemCategory(problemCategoryId,
					problemCategoryName,productId,productName);
			resultCode = SystemConstants.result_code_on_success;
		} catch (DataAccessException ex) {
			LOG.error("创建问题类别失败.problemCategoryId:" + problemCategoryId
					+ ",problemCategoryName:" + problemCategoryName, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);
	}
	/**
	 * 删除问题类别
	 * @param currentUserId
	 * @param problemCategoryId
	 * @return
	 */
	@RequestMapping(value = "{currentUserId}/remove", method = RequestMethod.POST, headers = "Accept=text/plain")
	@ResponseBody
	public String removeProblemCategory(@PathVariable Long currentUserId,
			@RequestParam(required = true) Integer problemCategoryId) {
		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;

		try {
			problemCategoryService.removeProblemCategory(problemCategoryId);
			resultCode = SystemConstants.result_code_on_success;
		} catch (DataAccessException ex) {
			LOG.error("删除问题类别失败.problemCategoryId:" + problemCategoryId, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);
	}
	/**
	 * 查找问题类别
	 * @param currentUserId
	 * @param searchContent
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(value = "{currentUserId}/find", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public String findProblemCategory(@PathVariable Long currentUserId,
			@RequestParam(required = false) String searchContent,
			@RequestParam(required = true) Integer page,
			@RequestParam(required = true) Integer rows) {

		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;

		String _problemCategoryName = null;
		if (!StringUtils.isEmpty(searchContent)) {
			_problemCategoryName = SqlUtil.matchAnywhere(searchContent);
		}

		try {
			SQLQueryResult<ProblemCategory> queryResult = problemCategoryService
					.findBy(_problemCategoryName, page, rows);
			results.put("total", queryResult.getTotal());
			results.put("rows", queryResult.getRows());
			resultCode = SystemConstants.result_code_on_success;
		} catch (Exception ex) {
			LOG.error("查询问题类别失败,problemCategoryName:" + searchContent
					+ ",page:" + page + ",rows:" + rows, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);
	}
	/**
	 * 加载问题类别
	 * @param currentUserId
	 * @return
	 */
	@RequestMapping(value = "{currentUserId}/load", method = RequestMethod.GET, headers = "Accept=text/plain")
	@ResponseBody
	public String loadProblemCategory(@PathVariable Long currentUserId) {

		try {
			SQLQueryResult<ProblemCategory> queryResult = problemCategoryService
					.findBy(null, null, null);
			return JsonUtil.toJson(queryResult.getRows());
		} catch (Exception ex) {
			LOG.error("加载问题类别失败", ex);
			return JsonUtil.toJson(Collections.emptyList());
		}

	}

}
