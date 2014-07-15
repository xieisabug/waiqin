package hn.join.fieldwork.service;

import hn.join.fieldwork.persistence.CheckinMapper;
import hn.join.fieldwork.persistence.WorkOrderMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 清除陈旧任务
 * @author aisino_lzw
 *
 */
@Component(value="sweepStaleDataTask")
public class SweepStaleDataTask {
	
	@Autowired
	private WorkOrderMapper workOrderMapper;
	
	@Autowired
	private CheckinMapper checkinMapper;
	
	public void doTask() {
		workOrderMapper.deleteStaleOrder();
		checkinMapper.deleteStaleCheckin();
	}
	
	

}
