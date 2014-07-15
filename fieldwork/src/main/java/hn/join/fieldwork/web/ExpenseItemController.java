package hn.join.fieldwork.web;

import hn.join.fieldwork.domain.ExpenseItem;
import hn.join.fieldwork.persistence.SQLQueryResult;
import hn.join.fieldwork.service.ExpenseItemService;
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
 * 同步-费用项目维护
 * @author chenjinlong
 *
 */
@Controller
@RequestMapping(value = "/web/expense-item")
public class ExpenseItemController extends BaseController {
	
	
	public final static String listExpenseItemPermission = "expenseItem:list";

	public final static String createExpenseItemPermission = "expenseItem:create";

	public final static String removeExpenseItemPermission = "expenseItem:remove";

	private static final Logger LOG = Logger
			.getLogger(ExpenseItemController.class);

	@Autowired
	private ExpenseItemService expenseItemService;
	
	@PostConstruct
	public void init(){
		this.addPermission(listExpenseItemPermission);
		this.addPermission(createExpenseItemPermission);
		this.addPermission(removeExpenseItemPermission);
	}
	
	
	

	@RequestMapping(method = RequestMethod.GET)
	public String expenseItemIndex() {
		return "expense-item-index";
	}
	/**
	 * 增加费用项目
	 * @param currentUserId
	 * @param itemId
	 * @param itemName
	 * @return
	 */
	@RequestMapping(value = "{currentUserId}/create", method = RequestMethod.POST, headers = "Accept=text/plain")
	@ResponseBody
	public String createExpenseItem(@PathVariable Long currentUserId,
			@RequestParam(required = true) Integer itemId,
			@RequestParam(required = true) String itemName) {
		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;

		try {
			expenseItemService.newExpenseItem(itemId, itemName);
			resultCode = SystemConstants.result_code_on_success;
		} catch (DataAccessException ex) {
			LOG.error("创建报销配置项失败.itemId:" + itemId + ",itemName:" + itemName,
					ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);
	}
/**
 * 删除费用项目
 * @param currentUserId
 * @param itemId
 * @return
 */
	@RequestMapping(value = "{currentUserId}/remove", method = RequestMethod.POST, headers = "Accept=text/plain")
	@ResponseBody
	public String removeExpenseItem(@PathVariable Long currentUserId,
			@RequestParam(required = true) Integer itemId) {
		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;

		try {
			expenseItemService.removeExpenseItem(itemId);
			resultCode = SystemConstants.result_code_on_success;
		} catch (DataAccessException ex) {
			LOG.error("删除报销配置项失败.problemCategoryId:" + itemId, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);
	}

	@RequestMapping(value = "{currentUserId}/find", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public String findExpenseItem(@PathVariable Long currentUserId,
			@RequestParam(required = false) String searchContent,
			@RequestParam(required = true) Integer page,
			@RequestParam(required = true) Integer rows) {

		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;

		String _itemName = null;
		if (!StringUtils.isEmpty(searchContent)) {
			_itemName = SqlUtil.matchAnywhere(searchContent);
		}

		try {
			SQLQueryResult<ExpenseItem> queryResult = expenseItemService
					.findBy(_itemName, page, rows);
			results.put("total", queryResult.getTotal());
			results.put("rows", queryResult.getRows());
			resultCode = SystemConstants.result_code_on_success;
		} catch (Exception ex) {
			LOG.error("查询报销配置项失败,itemName:" + searchContent
					+ ",page:" + page + ",rows:" + rows, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);
	}

}
