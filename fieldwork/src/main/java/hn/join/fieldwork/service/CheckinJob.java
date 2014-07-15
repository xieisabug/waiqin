package hn.join.fieldwork.service;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
/**
 * Spring中使用Quartz有两种方法：
 * </br>1.extends QuartzJobBean
 * </br>2.使用MethodInvokingJobDetailFactoryBean来直接指定执行某个对象的方法
 * @author chenjinlong
 *
 */
public class CheckinJob extends QuartzJobBean{
	
	private CheckinTask checkinTask;

	public void setCheckinTask(CheckinTask checkinTask) {
		this.checkinTask = checkinTask;
	}

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		checkinTask.doTask();
	}
	
	
	

}
