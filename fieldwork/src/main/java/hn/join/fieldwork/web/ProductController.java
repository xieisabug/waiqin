package hn.join.fieldwork.web;

import hn.join.fieldwork.domain.Product;
import hn.join.fieldwork.persistence.SQLQueryResult;
import hn.join.fieldwork.service.ProductService;
import hn.join.fieldwork.utils.JsonUtil;
import hn.join.fieldwork.utils.SystemConstants;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import javax.annotation.PostConstruct;

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
 * 产品管理控制器
 * @author chenjinlong
 *
 */
@Controller
@RequestMapping(value = "/web/product")
public class ProductController extends BaseController {

	private static final Logger LOG = Logger.getLogger(ProductController.class);

	public final static String listProductPermission = "product:list";

	public final static String createProductPermission = "product:create";

	public final static String removeProductPermission = "product:remove";

	@Autowired
	private ProductService productService;

	@PostConstruct
	public void init() {
		this.addPermission(listProductPermission);
		this.addPermission(createProductPermission);
		this.addPermission(removeProductPermission);
	}
	/**
	 * 创建产品
	 * @param currentUserId
	 * @param productId
	 * @param productName
	 * @return
	 */
	@RequestMapping(value = "{currentUserId}/create", method = RequestMethod.POST, headers = "Accept=text/plain")
	@ResponseBody
	public String createProduct(@PathVariable Long currentUserId,
			@RequestParam(required = true) Integer productId,
			@RequestParam(required = true) String productName) {
		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;

		Product product = new Product(productId, productName);
		try {
			productService.createProduct(product);
			resultCode = SystemConstants.result_code_on_success;
		} catch (DataAccessException ex) {
			LOG.error("创建产品失败.productId:" + productId + ",productName:"
					+ productName, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);
	}
	/**
	 * 删除产品 
	 * @param currentUserId
	 * @param productId
	 * @return
	 */
	@RequestMapping(value = "{currentUserId}/remove", method = RequestMethod.POST, headers = "Accept=text/plain")
	@ResponseBody
	public String removeProduct(@PathVariable Long currentUserId,
			@RequestParam(required = true) Integer productId) {
		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;

		try {
			productService.removeProduct(productId);
			resultCode = SystemConstants.result_code_on_success;
		} catch (DataAccessException ex) {
			LOG.error("删除产品失败.productId:" + productId, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);
	}
	/**
	 * 查询产品
	 * @param currentUserId
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(value = "{currentUserId}/find", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public String findProduct(@PathVariable Long currentUserId,
			@RequestParam(required = true) Integer page,
			@RequestParam(required = true) Integer rows) {

		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;

		try {
			SQLQueryResult<Product> queryResult = productService.findBy(page,
					rows);
			results.put("total", queryResult.getTotal());
			results.put("rows", queryResult.getRows());
			resultCode = SystemConstants.result_code_on_success;
		} catch (Exception ex) {
			LOG.error("查询产品失败,page:" + page + ",rows:" + rows, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);
	}
/**
 * 加载产品
 * @return
 */
	@RequestMapping(value = "{currentUserId}/load", headers = "Accept=application/json")
	@ResponseBody
	public String load(){
		Collection<Product> products=Collections.emptyList();
		try {
			SQLQueryResult<Product> queryResult= productService.findBy(null, null);
			products=queryResult.rows;
		} catch (Exception ex) {
			LOG.error("加载产品失败", ex);
		}
		return JsonUtil.toJson(products);
	}

}
