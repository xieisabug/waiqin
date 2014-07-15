package hn.join.fieldwork.persistence;

import hn.join.fieldwork.domain.Product;

import java.util.List;

import org.apache.ibatis.annotations.Param;
/**
 * 产品DAO
 * 操作表tbl_product
 * @author chenjinlong
 *
 */
public interface ProductMapper {
	/**
	 * 统计
	 * @return
	 */
	public long count();
	/**
	 * 查询
	 * @param offset
	 * @param limit
	 * @return
	 */
	public List<Product> find(@Param(value = "offset") Integer offset,
                              @Param(value = "limit") Integer limit);
	/**
	 * 新增 
	 * @param product
	 */
	public void insertProduct(Product product);
	/**
	 * 根据ID删除产品
	 * @param productId
	 */
	public void deleteById(Integer productId);
	/**
	 * 根据ID查询产品信息
	 * @param productId
	 * @return
	 */
	public Product getById(Integer productId);

}
