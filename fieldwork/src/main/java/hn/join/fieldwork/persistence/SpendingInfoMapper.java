package hn.join.fieldwork.persistence;

import hn.join.fieldwork.domain.SpendingInfo;

import java.util.List;

import org.apache.ibatis.annotations.Param;
/**
 * 单据花费DAO
 * 操作表tbl_spending_info
 * @author chenjinlong
 *
 */
public interface SpendingInfoMapper {
	/**
	 * 根据工单号码查询工单花费
	 * @param workOrderNo
	 * @return
	 */
	List<SpendingInfo> getByWorkOrderNo(String workOrderNo);
	/**
	 * 新增花费记录
	 * @param spendingInfo
	 */
	void insertSpendingInfo(SpendingInfo spendingInfo);
	/**
	 * 批量新增花费记录
	 * @param spendings
	 */
	void insertSpendings(@Param(value = "spendings") List<SpendingInfo> spendings);


}
