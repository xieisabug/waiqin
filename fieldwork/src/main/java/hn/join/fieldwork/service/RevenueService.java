package hn.join.fieldwork.service;

import hn.join.fieldwork.domain.Revenue;
import hn.join.fieldwork.persistence.RevenueMapper;
import hn.join.fieldwork.persistence.SQLQueryResult;
import hn.join.fieldwork.service.SystemEventBus.DataUpdateRequest;
import hn.join.fieldwork.service.SystemEventBus.DataUpdateRequest.DataType;
import hn.join.fieldwork.web.command.CreateRevenueCommand;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 税务局服务类
 * @author aisino_lzw
 *
 */
@Service
public class RevenueService {

	@Autowired
	private RevenueMapper revenueMapper;
	
	@Autowired
	private SystemEventBus systemEventBus;


	/**
	 * 新建税务局
	 * @param command
	 */
	@Transactional(rollbackFor = Exception.class)
	public void newRevenue(CreateRevenueCommand command) {
		Revenue _revenue = fromCreateRevenueCommand(command);
		revenueMapper.insertRevenue(_revenue);
		systemEventBus.postDataUpdate(new DataUpdateRequest(DataType.revenue,new Date()));
	}

	/**
	 * 删除税务局
	 * @param revenueId
	 */
	@Transactional(rollbackFor = Exception.class)
	public void removeRevenue(Integer revenueId) {
		revenueMapper.deleteRevenue(revenueId);
		systemEventBus.postDataUpdate(new DataUpdateRequest(DataType.revenue,new Date()));
	}

	/**
	 * 条件查询税务局
	 * @param revenueName
	 * @param page
	 * @param rows
	 * @return
	 */
	public SQLQueryResult<Revenue> findBy(String revenueName, Integer page,
			Integer rows) {
		SQLQueryResult<Revenue> queryResult = null;
		long _count = revenueMapper.countBy(revenueName);
		if (_count != 0) {
			Integer offset = (page - 1) * rows;
			List<Revenue> _result = revenueMapper.findBy(revenueName, offset,
					rows);
			queryResult = new SQLQueryResult(_count, _result);
		} else {
			queryResult = SQLQueryResult.EMPTY_RESULT;
		}
		return queryResult;
	}

	/**
	 * 从表单中添加税务局
	 * @param command
	 * @return
	 */
	private Revenue fromCreateRevenueCommand(CreateRevenueCommand command) {
		Revenue _revenue = new Revenue();
		_revenue.setId(command.getId());
		_revenue.setRevenueName(command.getRevenueName());
		_revenue.setRevenueCode(command.getRevenueCode());
		_revenue.setCityCode(command.getCityCode());
		return _revenue;
	}

}
