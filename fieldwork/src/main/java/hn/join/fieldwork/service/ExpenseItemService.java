package hn.join.fieldwork.service;

import hn.join.fieldwork.domain.ExpenseItem;
import hn.join.fieldwork.persistence.ExpenseItemMapper;
import hn.join.fieldwork.persistence.SQLQueryResult;
import hn.join.fieldwork.service.SystemEventBus.DataUpdateRequest;
import hn.join.fieldwork.service.SystemEventBus.DataUpdateRequest.DataType;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 费用信息服务类
 * @author aisino_lzw
 *
 */
@Service
public class ExpenseItemService {
	
	@Autowired
	private ExpenseItemMapper expenseItemMapper;
	
	@Autowired
	private SystemEventBus systemEventBus;

	
	/**
	 * 添加费用信息
	 * @param itemId  费用id
	 * @param itemName 费用名称
	 */
	@Transactional(rollbackFor=Exception.class)
	public void newExpenseItem(Integer itemId,String itemName){
		ExpenseItem _item=new ExpenseItem();
		_item.setId(itemId);
		_item.setName(itemName);
		expenseItemMapper.insertExpenseItem(_item);
		systemEventBus.postDataUpdate(new DataUpdateRequest(DataType.expense_item,new Date()));
		
	}
	
	/**
	 * 删除费用
	 * @param itemId 费用id
	 */
	@Transactional(rollbackFor=Exception.class)
	public void removeExpenseItem(Integer itemId){
		expenseItemMapper.deleteExpenseItem(itemId);
		systemEventBus.postDataUpdate(new DataUpdateRequest(DataType.expense_item,new Date()));
	}
	
	
	/**
	 * 查询费用信息
	 * @param itemName 费用名称
	 * @param page 指定页
	 * @param rows 指定行数
	 * @return 费用信息
	 */
	public SQLQueryResult<ExpenseItem> findBy(String itemName,Integer page,Integer rows){
		SQLQueryResult<ExpenseItem> queryResult=null;
		long _count=expenseItemMapper.countBy(itemName);
		if(_count!=0){
				Integer offset=(page-1)*rows;
			List<ExpenseItem> _result=expenseItemMapper.findBy(itemName, offset, rows);
			queryResult=new SQLQueryResult<ExpenseItem>(_count,_result);
		}else{
			queryResult= SQLQueryResult.EMPTY_RESULT;
		}
		return queryResult;
	}
}
