package hn.join.fieldwork.service;

import hn.join.fieldwork.domain.Notification;
import hn.join.fieldwork.persistence.NotificationMapper;
import hn.join.fieldwork.persistence.SQLQueryResult;
import hn.join.fieldwork.utils.DateTimeUtil;
import hn.join.fieldwork.web.command.CreateNotificationCommand;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 公告服务类
 * @author aisino_lzw
 *
 */
@Service
public class NotificationService {
	@Autowired
	private NotificationMapper notificationMapper;

	@Autowired
	private SystemEventBus systemEventBus;

	public void init() {
		systemEventBus.getEventBus().register(this);

	}

	/**
	 * 新建公告
	 * @param command
	 */
	@Transactional(rollbackFor = Exception.class)
	public void newNotification(CreateNotificationCommand command) {
		Notification notification = fromCreateNotificationCommand(command);
		notificationMapper.insertNotification(notification);
		systemEventBus.postNotification(notification);

	}

	/**
	 * 条件查询公告
	 * @param title 标题
	 * @param content 内容
	 * @param publishDate 发布时间
	 * @param city 城市
	 * @param page 指定页
	 * @param rows 指定行数
	 * @return
	 */
	public SQLQueryResult<Notification> findBy(String title, String content,
			String publishDate, String city,Integer page, Integer rows) {
		SQLQueryResult<Notification> queryResult = null;

		long _count = notificationMapper.countBy(title, content, publishDate,city);
		if (_count != 0) {
			Integer offset = (page - 1) * rows;
			List<Notification> _result = notificationMapper.findBy(title, content,publishDate,city,
					offset, rows);
			queryResult = new SQLQueryResult<Notification>(_count, _result);
		} else {
			queryResult = SQLQueryResult.EMPTY_RESULT;
		}
		return queryResult;
	}
	
	/**
	 * 根据公告id获取公告
	 * @param notificationId
	 * @return
	 */
	public Notification getById(Integer notificationId){
		return notificationMapper.getById(notificationId);
	}

	
	/**
	 * 从表单中添加公告
	 * @param command
	 * @return
	 */
	private Notification fromCreateNotificationCommand(
			CreateNotificationCommand command) {
		Notification _notification = new Notification();
		_notification.setTitle(command.getTitle());
		_notification.setContent(command.getContent());
		String _city=StringUtils.isEmpty(command.getCity())?null:command.getCity();
		_notification.setCity(_city);
		String _publishDate = command.getPublishDate();
		if (StringUtils.isEmpty(_publishDate)) {
			_publishDate = DateTime.now().toString("yyyy-MM-dd HH:mm");
		}
		_notification.setPublishDate(_publishDate);
		return _notification;

	}

}
