package hn.join.fieldwork.web;

import hn.join.fieldwork.domain.Customer;
import hn.join.fieldwork.domain.User;
import hn.join.fieldwork.persistence.SQLQueryResult;
import hn.join.fieldwork.service.CustomerService;
import hn.join.fieldwork.service.UserService;
import hn.join.fieldwork.utils.JsonUtil;
import hn.join.fieldwork.utils.SqlUtil;
import hn.join.fieldwork.utils.StringUtil;
import hn.join.fieldwork.utils.SystemConstants;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;

@Controller
@RequestMapping("/web/customer/")
public class CustomerController extends BaseController{
	
	public final static String listCustomerPermission = "customer:list";

	public final static String markCustomerPermission = "customer:mark";
	
	


	private static final Logger LOG = Logger
			.getLogger(CustomerController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private CustomerService customerService;

	/**
	 * 初始化
	 */
	@PostConstruct
	public void init(){
		this.addPermission(listCustomerPermission);
		this.addPermission(markCustomerPermission);
	}
	
	/**
	 * 显示客户管理页面
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String customerIndex(Model model) {
		User user = userService.getCurrentUser();
		long customerCount = customerService.countByAreaCode(
				user.getAreaCode(), null);
		model.addAttribute("customerCount", customerCount);
		return "clientMgr";
	}

	/**
	 * 加载客户信息
	 * @param currentUserId
	 * @param areaCode
	 * @param idStart
	 * @param limit
	 * @return
	 */

	@RequestMapping(value = "{currentUserId}/load", headers = "Accept=text/plain")
	@ResponseBody
	public String loadBy(@PathVariable Long currentUserId,
			@RequestParam(required = false) String areaCode,
			@RequestParam(required = true) Long idStart,
			@RequestParam(required = true) Integer limit) {

		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;

		try {
			List<Customer> customers = customerService.loadByAreaCode(areaCode,
					idStart, limit);
			results.put("customers", customers);
			resultCode = SystemConstants.result_code_on_success;
		} catch (Exception ex) {
			LOG.error("加载客户信息失败,currentUserId:" + currentUserId + ",areaCode:"
					+ areaCode + ",idStart:" + idStart + ",limit:" + limit, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);
	}
	/**
	 * 查找客户
	 * @param currentUserId
	 * @param areaCode
	 * @param customerAddress
	 * @param customerName
	 * @param tels
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(value = "{currentUserId}/find", method = RequestMethod.POST, headers = "Accept=text/plain")
	@ResponseBody
	public String findBy(@PathVariable Long currentUserId,
			@RequestParam(required = false) String areaCode,
			@RequestParam(required = false) String customerAddress,
			@RequestParam(required = false) String customerName,
			@RequestParam(required = false) String tels,
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer rows) {

		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;
		String _customerAddress = null;
		if (!StringUtils.isEmpty(customerAddress)) {
			_customerAddress = SqlUtil.matchAnywhere(customerAddress);
		}

		String _customerName = null;
		if (!StringUtils.isEmpty(customerName)) {
			_customerName = SqlUtil.matchAnywhere(customerName);
		}

		String _tels = null;
		if (!StringUtils.isEmpty(tels)) {
			_tels = SqlUtil.matchHead(tels);
		}

		try {

			long customerCount = customerService.countBy(areaCode,
					_customerAddress, _customerName, _tels);
			List<Customer> customers;
			if (customerCount == 0) {
				customers = Collections.emptyList();
			} else {
				Integer offset = 0;
				if (page != null && rows != null)
					offset = ((page - 1) * rows);
				customers = customerService.findBy(areaCode, _customerAddress,
						_customerName, _tels, offset, rows);
			}

			results.put("total", customerCount);
			results.put("rows", customers);
			resultCode = SystemConstants.result_code_on_success;
		} catch (Exception ex) {
			LOG.error("查找客户信息失败,currentUserId:" + currentUserId + ",areaCode:"
					+ areaCode + ",page:" + page + ",rows:" + rows, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);
	}
	/**
	 * 近坐标查询客户
	 * @param currentUserId
	 * @param latitude
	 * @param longitude
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(value="{currentUserId}/findByCoordinate", method = RequestMethod.POST, headers = "Accept=text/plain")
	@ResponseBody
	public String findByCoordinate(@PathVariable Long currentUserId,
			@RequestParam(required = true) Float latitude,
			@RequestParam(required = true) Float longitude,
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer rows) {

		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;
		try {
			SQLQueryResult<Customer> queryResult = customerService
					.findByCoordinate(latitude, longitude, page, rows);
			long customerCount = queryResult.total;
			List<Customer> customers = queryResult.rows;
			results.put("total", customerCount);
			results.put("rows", customers);
			resultCode = SystemConstants.result_code_on_success;
		} catch (Exception ex) {
			LOG.error("按坐标查找客户信息失败,currentUserId:" + currentUserId
					+ ",latitude:" + latitude + ",longitude:" + longitude
					+ ",page:" + page + ",rows:" + rows, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);

	}
	/**
	 * 标记客户
	 * @param currentUserId
	 * @param customerId
	 * @param tag
	 * @return
	 */
	//tag 0:no_star;1:city_star;2:prov_star;3:prov_city_star
	@RequestMapping(value = "{currentUserId}/mark", method = RequestMethod.POST, headers = "Accept=text/plain")
	@ResponseBody
	public String mark(@PathVariable Long currentUserId,
			@RequestParam(required = true) String customerId,
			@RequestParam(required = true) Short tag) {
		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;
		try {
			List<Integer> customerIdList = StringUtil.fromStringToIntList(
					customerId, ",");
			customerService.markCustomer(customerIdList, tag);
			resultCode = SystemConstants.result_code_on_success;
		} catch (Exception ex) {
			LOG.error("标记客户失败,currentUserId:" + currentUserId + ",customerId:"
					+ customerId + ",tag:" + tag, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);
	}

}
