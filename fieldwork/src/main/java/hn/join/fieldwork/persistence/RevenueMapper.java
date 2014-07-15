package hn.join.fieldwork.persistence;

import hn.join.fieldwork.domain.Revenue;

import java.util.List;

import org.apache.ibatis.annotations.Param;
/**
 * 税务局DAO
 * 操作表tbl_revenue
 * @author chenjinlong
 *
 */
public interface RevenueMapper {
	/**
	 * 根据ID获取税务局信息
	 * @param revenueId
	 * @return
	 */
	public Revenue getById(Integer revenueId);
	/**
	 * 新增税务局信息
	 * @param revenue
	 */
	public void insertRevenue(Revenue revenue);
	/**
	 * 删除税务局
	 * @param revenueId
	 */
	public void deleteRevenue(Integer revenueId);
	/**
	 * 统计
	 * @param revenueName
	 * @return
	 */
	public long countBy(@Param(value = "revenueName") String revenueName);
/**
 * 按条件查询税务局
 * @param revenueName
 * @param offset
 * @param limit
 * @return
 */
	public List<Revenue> findBy(
            @Param(value = "revenueName") String revenueName,
            @Param(value = "offset") Integer offset,
            @Param(value = "limit") Integer limit);
	
	
	public List<Revenue> findAll();

}
