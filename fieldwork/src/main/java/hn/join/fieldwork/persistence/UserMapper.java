package hn.join.fieldwork.persistence;

import hn.join.fieldwork.domain.User;

import java.util.List;

import org.apache.ibatis.annotations.Param;
/**
 * 用户操作DAO
 * 操作表：tbl_user
 * @author chenjinlong
 *
 */
public interface UserMapper {
	/**
	 * 根据用户ID获取用户信息
	 * @param userId
	 * @return
	 */
	User getById(Long userId);
	/**
	 * 根据用户名获取取用户信息
	 * @param username
	 * @return
	 */
	User getByUsername(String username);
	/**
	 * 新增用户
	 * @param user
	 */
	void insertUser(User user);
	/**
	 * 更新用户
	 * @param user
	 */
	void updateUser(User user);
	/**
	 * 批量删除用户
	 * @param userIdList
	 */
	void deleteUser(List<Long> userIdList);
	/**
	 * 根据用户工号获取用户信息
	 * @param workerNo
	 * @return
	 */
	User getByWorkerNo(String workerNo);
	/**
	 * 更新用户个个信息
	 * @param user
	 */
	void updatePersonSetting(User user);
	/**
	 * 更新用户密码
	 * @param userId
	 * @param password
	 */
	void updatePassword(@Param(value = "userId") Long userId,
                        @Param(value = "password") String password);
/**
 * 按条件统计用户
 * @param fullname
 * @param mobileNo
 * @param areaCode
 * @param selfId
 * @return
 */
	long countBy(@Param(value = "fullname") String fullname,
                 @Param(value = "mobileNo") String mobileNo,
                 @Param(value = "areaCode") String areaCode,
                 @Param(value = "selfId") Long selfId
    );
/**
 * 按条件查询用户
 * @param fullname
 * @param mobileNo
 * @param areaCode
 * @param selfId
 * @param offset
 * @param limit
 * @return
 */
	List<User> findBy(@Param(value = "fullname") String fullname,
                      @Param(value = "mobileNo") String mobileNo,
                      @Param(value = "areaCode") String areaCode,
                      @Param(value = "selfId") Long selfId,
                      @Param(value = "offset") Integer offset,
                      @Param(value = "limit") Integer limit);
/**
 * 按用户名统计用户
 * @param username
 * @return
 */
	long countByUsername(String username);
	/**
	 * 按用户工号统计用户
	 * @param workerNo
	 * @return
	 */
	long countByWorkerNo(String workerNo);
	/**
	 * 按用户电脑号码统计用户
	 * @param mobileNo
	 * @return
	 */
	long countByMobileNo(String mobileNo);
	/**
	 * 按用户电子邮件统计用户
	 * @param email
	 * @return
	 */
	long countByEmail(String email);

}
