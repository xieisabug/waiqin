package hn.join.fieldwork.web;

import hn.join.fieldwork.domain.Notification;
import hn.join.fieldwork.persistence.SQLQueryResult;
import hn.join.fieldwork.service.NotificationService;
import hn.join.fieldwork.utils.JsonUtil;
import hn.join.fieldwork.utils.SystemConstants;
import hn.join.fieldwork.web.command.CreateNotificationCommand;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
/**
 * 通知管理控制器
 * @author chenjinlong
 *
 */
@Controller
@RequestMapping(value = "/web/notification/")
public class NotificationController extends BaseController{
	
	public static final String listNotificationPermission="notification:list";
	
	public static final String createNotificationPermission="notification:create";

	private static final Logger LOG = Logger
			.getLogger(NotificationController.class);

	@Autowired
	private NotificationService notificationService;
	
	@PostConstruct
	public void init(){
		this.addPermission(listNotificationPermission);
		this.addPermission(createNotificationPermission);
	}

	@RequestMapping(method = RequestMethod.GET)
	public String notificationIndex() {
		return "messageMgr";
	}
	/**
	 * 创建通知
	 * @param currentUserId
	 * @param command
	 * @return
	 */
	@RequestMapping(value = "{currentUserId}/create", method = RequestMethod.POST, headers = "Accept=text/plain")
	@ResponseBody
	public String createNotification(@PathVariable Long currentUserId,
			CreateNotificationCommand command) {
		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;

		try {
			notificationService.newNotification(command);
			resultCode = SystemConstants.result_code_on_success;
		} catch (DataAccessException ex) {
			LOG.error("创建通知失败.command:" + command, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);
	}
	/**
	 * 查询通知
	 * @param currentUserId
	 * @param title
	 * @param content
	 * @param publishDate
	 * @param city
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(value = "{currentUserId}/find", method = RequestMethod.POST, headers = "Accept=text/plain")
	@ResponseBody
	public String findNotification(@PathVariable Long currentUserId,
			@RequestParam(required = false) String title,
			@RequestParam(required = false) String content,
			@RequestParam(required = false) String publishDate,
			@RequestParam(required = false) String city,
			@RequestParam(required = true) Integer page,
			@RequestParam(required = true) Integer rows) {

		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;
		try {
			SQLQueryResult<Notification> queryResult = notificationService
					.findBy(title, content, publishDate,city, page, rows);
			results.put("total", queryResult.getTotal());
			results.put("rows", queryResult.getRows());
			resultCode = SystemConstants.result_code_on_success;
		} catch (Exception ex) {
			LOG.error("查找通知失败,userId:" + currentUserId + ",title:" + title
					+ ",content:" + content + ",publishDate:" + publishDate
					+ ",page:" + page + ",rows" + rows, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);
	}
	/**
	 * 加载通知
	 * @param currentUserId
	 * @param notificationId
	 * @return
	 */
	@RequestMapping(value = "{currentUserId}/load-by-id", method = RequestMethod.POST, headers = "Accept=text/plain")
	@ResponseBody
	public String loadNotification(@PathVariable Long currentUserId,@RequestParam(required=true) Integer notificationId){
		Map<String, Object> results = Maps.newHashMap();
		int resultCode = SystemConstants.result_code_on_failure;
		try{
			Notification notification=notificationService.getById(notificationId);
			results.put("notification", notification);
			resultCode = SystemConstants.result_code_on_success;
		}catch(Exception ex){
			LOG.error("加载通知失败,notificationId:"+notificationId, ex);
			results.put("error", ex.getMessage());
		}
		results.put("resultCode", resultCode);
		return JsonUtil.toJson(results);
	}
}
