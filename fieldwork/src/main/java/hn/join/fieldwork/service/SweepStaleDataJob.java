package hn.join.fieldwork.service;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;


/**
 * 清除陈旧工作
 * @author aisino_lzw
 *
 */
public class SweepStaleDataJob extends QuartzJobBean{
	
	private SweepStaleDataTask sweepStaleDataTask;
	
	
	public void setSweepStaleDataTask(SweepStaleDataTask sweepStaleDataTask) {
		this.sweepStaleDataTask = sweepStaleDataTask;
	}



	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		sweepStaleDataTask.doTask();
	}

}
