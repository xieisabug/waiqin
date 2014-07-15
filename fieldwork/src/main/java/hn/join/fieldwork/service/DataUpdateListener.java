package hn.join.fieldwork.service;

import hn.join.fieldwork.domain.ExpenseItem;
import hn.join.fieldwork.domain.Notification;
import hn.join.fieldwork.domain.ProblemCategory;
import hn.join.fieldwork.domain.Revenue;
import hn.join.fieldwork.persistence.ExpenseItemMapper;
import hn.join.fieldwork.persistence.ProblemCategoryMapper;
import hn.join.fieldwork.persistence.ProblemTypeMapper;
import hn.join.fieldwork.persistence.RevenueMapper;
import hn.join.fieldwork.service.SystemEventBus.DataUpdateRequest;
import hn.join.fieldwork.service.SystemEventBus.DataUpdateRequest.DataType;
import hn.join.fieldwork.utils.DateTimeUtil;
import hn.join.fieldwork.utils.JsonUtil;
import hn.join.fieldwork.utils.SystemConstants;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.eventbus.Subscribe;


/**
 * 数据更新监听器
 * @author aisino_lzw
 *
 */
@Component
public class DataUpdateListener {
	private static final Logger LOG = Logger
			.getLogger(DataUpdateListener.class);

	@Autowired
	private ProblemCategoryMapper problemCategoryMapper;

	@Autowired
	private ProblemTypeMapper problemTypeMapper;

	@Autowired
	private RevenueMapper revenueMapper;

	@Autowired
	private ExpenseItemMapper expenseItemMapper;

	@Autowired
	private SystemEventBus systemEventBus;

	@PostConstruct
	public void init() {
		systemEventBus.getEventBus().register(this);
	}

	/**
	 * 指定数据输出
	 * @param request 指定类别
	 */
	@Subscribe
	public void onDateUpate(DataUpdateRequest request) {
		DataType dataType = request.getDataType();
		try {
			switch (dataType) {
			case problem_category: {
				generateProblemCategoryUpdateFile();

				break;
			}
			case problem_type: {
				generateProblemTypeUpdateFile();
				break;
			}
			case revenue: {
				generateRevenueUpdateFile();
				break;
			}

			case expense_item: {
				generateExpenseItemUpdateFile();
				break;
			}
			default: {
				throw new RuntimeException("no such dataType:" + dataType);
			}
			}

			Notification notification = generateNotification(request);
			systemEventBus.postNotification(notification);

		} catch (IOException ex) {
			LOG.error("数据更新文件生成失败,dataType:" + dataType, ex);
		}

	}

	/**
	 * 查找所有问题分类
	 * @throws IOException
	 */
	private void generateProblemCategoryUpdateFile() throws IOException {

		List<ProblemCategory> problemCategories = problemCategoryMapper
				.findAll();
		String content = JsonUtil.toJson(problemCategories);
		FileUtils.write(SystemConstants.getProblemCategoryUpdateFile(),
				content, "UTF-8");

	}

	/**
	 * 查询问题类别表内容写入文件
	 * @throws IOException
	 */
	private void generateProblemTypeUpdateFile() throws IOException {
		List<Map<String, Object>> problemTypes = problemTypeMapper.findAll();
		String content = JsonUtil.toJson(problemTypes);
		FileUtils.write(SystemConstants.getProblemTypeUpdateFile(), content,
				"UTF-8");
	}

	/**
	 * 查询税务表内容写入文件
	 * @throws IOException
	 */
	private void generateRevenueUpdateFile() throws IOException {
		List<Revenue> revenues = revenueMapper.findAll();
		String content = JsonUtil.toJson(revenues);
		FileUtils.write(SystemConstants.getRevenueUpdateFile(), content,
				"UTF-8");
	}

	
	/**
	 * 查询经费表内容写入文件
	 * @throws IOException
	 */
	private void generateExpenseItemUpdateFile() throws IOException {

		List<ExpenseItem> expenseItems = expenseItemMapper.findAll();

		String content = JsonUtil.toJson(expenseItems);

		FileUtils.write(SystemConstants.getExpenseItemUpdateFile(), content,
				"UTF-8");
	}

	/**
	 * 设置通知对象
	 * @param request
	 * @return
	 */
	private Notification generateNotification(DataUpdateRequest request) {
		Notification notification = new Notification();
		notification.setTitle("data_update_notification");
		notification.setContent(request.getDataType().name());
		notification.setPublishDate(DateTimeUtil.formatAsYYYYMMddHHmmss(request
				.getUpdateTime()));
		return notification;
	}
}
