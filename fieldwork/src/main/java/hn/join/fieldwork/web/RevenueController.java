package hn.join.fieldwork.web;

import hn.join.fieldwork.domain.Revenue;
import hn.join.fieldwork.persistence.SQLQueryResult;
import hn.join.fieldwork.service.RevenueService;
import hn.join.fieldwork.utils.JsonUtil;
import hn.join.fieldwork.utils.SqlUtil;
import hn.join.fieldwork.utils.SystemConstants;
import hn.join.fieldwork.web.command.CreateRevenueCommand;

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
 * 税务分局管理控制器
 * @author chenjinlong
 *
 */
@Controller
@RequestMapping(value = "/web/revenue/")
public class RevenueController extends BaseController {

	private static final Logger LOG = Logger.getLogger(RevenueController.class);
	
	public final static String listRevenuePermission = "revenue:list";

	public final static String createRevenuePermission = "revenue:create";

	public final static String removeRevenuePermission = "revenue:remove"; 

	@Autowired
	private RevenueService revenueService;
	
	@PostConstruct
	public void init(){
		this.addPermission(listRevenuePermission);
		this.addPermission(createRevenuePermission);
		this.addPermission(removeRevenuePermission);
	}

	@RequestMapping(method = RequestMethod.GET)
	public String revenueIndex() {
		return "revenue-index";
	}
	/**
	 * 创建税务分局
	 * @param currentUserId
	 * @param command
	 * @return
	 */
	@RequestMapping(value = "{currentUserId}/create", method = RequestMethod.POST, headers = "Accept=text/plain")
	@ResponseBody
	public String createRevenue(@PathVariable Long currentUserId,
			CreateRevenueCommand command) {
		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;

		try {
			revenueService.newRevenue(command);
			resultCode = SystemConstants.result_code_on_success;
		} catch (DataAccessException ex) {
			LOG.error("创建税务分局失败.command:" + command, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);
	}
	/**
	 * 删除税务分局
	 * @param currentUserId
	 * @param revenueId
	 * @return
	 */
	@RequestMapping(value = "{currentUserId}/remove", method = RequestMethod.POST, headers = "Accept=text/plain")
	@ResponseBody
	public String removeRevenue(@PathVariable Long currentUserId,
			@RequestParam(required = true) Integer revenueId) {
		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;

		try {
			revenueService.removeRevenue(revenueId);
			resultCode = SystemConstants.result_code_on_success;
		} catch (DataAccessException ex) {
			LOG.error("删除税务分局失败.revenueId:" + revenueId, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);
	}
	/**
	 * 查找税务分局
	 * @param currentUserId
	 * @param searchContent
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(value = "{currentUserId}/find", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public String findRevenue(@PathVariable Long currentUserId,
			@RequestParam(required = false) String searchContent,
			@RequestParam(required = true) Integer page,
			@RequestParam(required = true) Integer rows) {

		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;

		String _revenueName = null;
		if (!StringUtils.isEmpty(searchContent)) {
			_revenueName = SqlUtil.matchAnywhere(searchContent);
		}

		try {
			SQLQueryResult<Revenue> queryResult = revenueService.findBy(
					_revenueName, page, rows);
			results.put("total", queryResult.getTotal());
			results.put("rows", queryResult.getRows());
			resultCode = SystemConstants.result_code_on_success;
		} catch (Exception ex) {
			LOG.error("查询税务分局失败,revenueName:" + searchContent + ",page:" + page
					+ ",rows:" + rows, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);

	}

}
