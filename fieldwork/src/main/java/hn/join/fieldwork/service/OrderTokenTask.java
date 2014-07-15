package hn.join.fieldwork.service;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 工单任务标记
 * @author aisino_lzw
 *
 */
@Component(value = "orderTokenTask")
public class OrderTokenTask {

	@Autowired
	private OrderTokenService orderTokenService;

	/**
	 * 执行任务
	 */
	public void doTask() {
		orderTokenService.changeTokenSeq(LocalDate.now().toString("yyyyMM"));
	}
}
