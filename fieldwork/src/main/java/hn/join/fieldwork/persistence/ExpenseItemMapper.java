package hn.join.fieldwork.persistence;

import hn.join.fieldwork.domain.ExpenseItem;

import java.util.List;

import org.apache.ibatis.annotations.Param;
/**
 * 出差报销项目DAO
 * 操作表tbl_expense_item
 * @author chenjinlong
 *
 */
public interface ExpenseItemMapper {
	/**
	 * 新增报销项目
	 * @param expenseItem
	 */
	void insertExpenseItem(ExpenseItem expenseItem);
	/**
	 * 删除报销项目
	 * @param itemId
	 */
	void deleteExpenseItem(@Param(value = "itemId") Integer itemId);
	/**
	 * 根据报销项目名统计
	 * @param itemName
	 * @return
	 */
	public int countBy(@Param(value = "itemName") String itemName);
	/**
	 * 分页查询报销项目
	 * @param itemName
	 * @param offset
	 * @param limit
	 * @return
	 */
	public List<ExpenseItem> findBy(@Param(value = "itemName") String itemName,
                                    @Param(value = "offset") Integer offset,
                                    @Param(value = "limit") Integer limit);
	/**
	 * 查询报销项目所有记录
	 * @return
	 */
	public List<ExpenseItem> findAll();

}
