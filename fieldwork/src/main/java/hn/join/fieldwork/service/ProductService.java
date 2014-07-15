package hn.join.fieldwork.service;

import hn.join.fieldwork.domain.Product;
import hn.join.fieldwork.persistence.ProductMapper;
import hn.join.fieldwork.persistence.SQLQueryResult;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;

/**
 * 产品服务类
 * @author aisino_lzw
 *
 */
@Service
public class ProductService {
	
	@Autowired
	private ProductMapper productMapper;
	
	private final Map<Integer,String> productMap=Maps.newHashMap();
	
	/**
	 * 数据初始化
	 */
	@PostConstruct
	public void init(){
		List<Product> products=productMapper.find(0, Integer.MAX_VALUE);
		for(Product _product:products){
			productMap.put(_product.getId(), _product.getName());
		}
		
	}
	
	
	/**
	 * 添加产品
	 */
	public void createProduct(Product product){
		productMapper.insertProduct(product);
		productMap.put(product.getId(), product.getName());
	}
	
	/**
	 * 删除产品
	 * @param productId
	 */
	public void removeProduct(Integer productId){
		productMapper.deleteById(productId);
		productMap.remove(productId);
	}
	
	
	/**
	 * 条件查询产品
	 * @param page
	 * @param rows
	 * @return
	 */
	public SQLQueryResult<Product> findBy(Integer page, Integer rows){
		SQLQueryResult<Product> queryResult = null;

		long _count = productMapper.count();
		if (_count != 0) {
			Integer offset = null;
			if (page != null && rows != null)
				offset = (page - 1) * rows;
			List<Product> _result = productMapper.find(offset, rows);
			queryResult = new SQLQueryResult<Product>(_count, _result);
		} else {
			queryResult = SQLQueryResult.EMPTY_RESULT;
		}
		return queryResult;
	}
	
	/**
	 * 根据产品id查询产品
	 * @param productId
	 * @return
	 */
	public String getNameById(Integer productId){
		return productMap.get(productId);
	}
	
	

}
