package hn.join.fieldwork.service;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
 * 签到处理定时任务，用于创建签到日期
 * @author chenjinlong
 *
 */
@Component(value="checkinTask")
public class CheckinTask {

	@Autowired
	private CheckinService checkinService;

	public void doTask() {
		checkinService.createCheckinDay(new LocalDate());
	}

}
