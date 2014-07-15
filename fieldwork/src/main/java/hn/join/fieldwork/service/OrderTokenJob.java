package hn.join.fieldwork.service;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;


/**
 * 标记工单
 * @author aisino_lzw
 *
 */
public class OrderTokenJob extends QuartzJobBean {

	private OrderTokenTask orderTokenTask;

	public void setOrderTokenTask(OrderTokenTask orderTokenTask) {
		this.orderTokenTask = orderTokenTask;
	}
	
	/**
	 * 执行工单
	 */
	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		orderTokenTask.doTask();
	}

}
