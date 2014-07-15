package hn.join.fieldwork.persistence;

import hn.join.fieldwork.domain.Notification;

import java.util.List;

import org.apache.ibatis.annotations.Param;
/**
 * 通知DAO
 * 操作表tbl_notification
 * @author chenjinlong
 *
 */
public interface NotificationMapper {
	/**
	 * 新增通知
	 * @param notification
	 */
	void insertNotification(Notification notification);
	/**
	 * 根据ID获取通知
	 * @param notificationId
	 * @return
	 */
	Notification getById(Integer notificationId);
	/**
	 * 根据条件统计
	 * @param title
	 * @param content
	 * @param publishDate
	 * @param city
	 * @return
	 */
	long countBy(@Param(value = "title") String title,
                 @Param(value = "content") String content,
                 @Param(value = "publishDate") String publishDate,
                 @Param(value = "city") String city);
	/**
	 * 根据条件分页查询通知
	 * @param title
	 * @param content
	 * @param publishDate
	 * @param city
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<Notification> findBy(@Param(value = "title") String title,
                              @Param(value = "content") String content,
                              @Param(value = "publishDate") String publishDate,
                              @Param(value = "city") String city,
                              @Param(value = "offset") Integer offset,
                              @Param(value = "limit") Integer limit);

}
