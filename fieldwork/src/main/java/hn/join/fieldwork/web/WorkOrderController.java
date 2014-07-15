package hn.join.fieldwork.web;

import hn.join.fieldwork.domain.Customer;
import hn.join.fieldwork.domain.WorkOrderProblem;
import hn.join.fieldwork.persistence.SQLQueryResult;
import hn.join.fieldwork.service.CustomerService;
import hn.join.fieldwork.service.WorkOrderService;
import hn.join.fieldwork.utils.JsonUtil;
import hn.join.fieldwork.utils.SystemConstants;
import hn.join.fieldwork.web.command.WorkOrderTimeout;

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
 * 工单管理控制器
 * @author chenjinlong
 *
 */
@Controller
@RequestMapping(value = "/web/order/")
public class WorkOrderController extends BaseController {

	private static final Logger LOG = Logger
			.getLogger(WorkOrderController.class);

	public static final String listOrderTimeoutPermission = "workOrder:listTimeout";

	@Autowired
	private WorkOrderService workOrderService;

	@Autowired
	private CustomerService customerService;
	
	@PostConstruct
	public void init(){
		super.addPermission(listOrderTimeoutPermission);
	}

	@RequestMapping(method = RequestMethod.GET)
	public String workOrderIndex() {
		return "checkOrder";
	}
	/**
	 * 查找超时工单
	 * @param currentUserId
	 * @param fullname
	 * @param city
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(value = "{currentUserId}/list", method = RequestMethod.POST, headers = "Accept=text/plain")
	@ResponseBody
	public String listTimeoutWorkOrder(@PathVariable Long currentUserId,
			@RequestParam(required = false) String fullname,
			@RequestParam(required = false) String city,
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer rows) {

		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;
		try {
			SQLQueryResult<WorkOrderTimeout> queryResult = workOrderService
					.findWorkOrderTimeout(fullname, city, page, rows);
			results.put("total", queryResult.getTotal());
			results.put("rows", queryResult.getRows());
			resultCode = SystemConstants.result_code_on_success;
		} catch (Exception ex) {
			LOG.error("查找超时失败,userId:" + currentUserId + ",fullname:"
					+ fullname + ",city:" + city + ",page:" + page + ",rows"
					+ rows, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);

	}
	/**
	 * 查看工单详情
	 * @param currentUserId
	 * @param workOrderNo
	 * @param customerId
	 * @return
	 */
	@RequestMapping(value = "{currentUserId}/show", method = RequestMethod.GET, headers = "Accept=text/plain")
	@ResponseBody
	public String showWorkOrderDetail(@PathVariable Long currentUserId,
			@RequestParam(required = true) String workOrderNo,
			@RequestParam(required = true) Long customerId) {

		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;
		try {
			Customer customer = customerService.getById(customerId);
			List<WorkOrderProblem> problems = workOrderService
					.findWorkOrderProblem(workOrderNo);
			results.put("customer", customer);
			results.put("problems", problems);
			resultCode = SystemConstants.result_code_on_success;
		} catch (Exception ex) {
			LOG.error("查看工单详情失败,userId:" + currentUserId + ",workOrderNo:"
					+ workOrderNo + ",customerId:" + customerId, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);
	}
}
