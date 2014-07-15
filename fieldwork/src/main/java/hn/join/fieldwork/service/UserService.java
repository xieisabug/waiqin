package hn.join.fieldwork.service;

import hn.join.fieldwork.domain.User;
import hn.join.fieldwork.persistence.SQLQueryResult;
import hn.join.fieldwork.persistence.UserMapper;
import hn.join.fieldwork.utils.HashUtil;
import hn.join.fieldwork.utils.StringUtil;
import hn.join.fieldwork.web.command.CreateUserCommand;
import hn.join.fieldwork.web.command.EditUserCommand;
import hn.join.fieldwork.web.command.PersonSettingCommand;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 用户服务类
 * @author aisino_lzw
 *
 */
@Service
public class UserService {

	@Autowired
	private UserMapper userMapper;


	/**
	 * 获取当前用户
	 * @return
	 */
	public User getCurrentUser() {
		final Long currentUserId = (Long) SecurityUtils.getSubject()
				.getPrincipal();
		if (currentUserId != null) {
			return getUser(currentUserId);
		} else {
			return null;
		}
	}

	/**
	 * 根据id获取用户
	 * @param userId
	 * @return
	 */
	public User getUser(Long userId) {
		return userMapper.getById(userId);
	}

	/**
	 * 新建用户
	 * @param command
	 * @throws DataAccessException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void newUser(CreateUserCommand command) throws DataAccessException {
		User user = fromCreateUserCommand(command);
		userMapper.insertUser(user);
	}

	/**
	 * 修改用户信息
	 * @param command
	 * @throws DataAccessException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void editUser(EditUserCommand command) throws DataAccessException {
		User user = fromEditUserCommand(command);
		userMapper.updateUser(user);
	}
	
	/**
	 * 修改用户密码
	 * @param userId
	 * @param newPassword
	 */
	@Transactional(rollbackFor = Exception.class)
	public void changeUserPassword(Long userId,String newPassword){
		String hashNewPassword = HashUtil.encodeMd5Hex(newPassword); 
		userMapper.updatePassword(userId, hashNewPassword);
	}

	/**
	 * 删除用户
	 * @param userId
	 * @throws DataAccessException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void removeUser(String userId) throws DataAccessException {
		userMapper.deleteUser(StringUtil.fromStringToLongList(userId, ","));
	}

	/**
	 * 检查用户是否存在
	 * @param username
	 * @return
	 */
	public boolean checkUsernameAvailable(String username) {
		long _count = userMapper.countByUsername(username);
		if (_count == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 按用户工号查询用户是否存在
	 * @param workerNo
	 * @return
	 */
	public boolean checkWorkerNoAvailable(String workerNo) {
		long _count = userMapper.countByWorkerNo(workerNo);
		if (_count == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 按用户手机号码查询用户是否存在
	 * @param mobileNo
	 * @return
	 */
	public boolean checkMobileNoAvailable(String mobileNo) {
		long _count = userMapper.countByMobileNo(mobileNo);
		if (_count == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 按用户邮箱查询用户是否存在
	 * @param email
	 * @return
	 */
	public boolean checkEmailAvailable(String email) {
		long _count = userMapper.countByEmail(email);
		if (_count == 0) {
			return true;
		}
		return false;
	}

	
	/**
	 * 条件查询用户信息
	 * @param fullname
	 * @param mobileNo
	 * @param areaCode
	 * @param currentUserId
	 * @param page
	 * @param rows
	 * @return
	 */
	public SQLQueryResult<User> findBy(String fullname, String mobileNo,
			String areaCode, Long currentUserId, Integer page, Integer rows) {
		SQLQueryResult<User> queryResult = null;

		long _count = userMapper.countBy(fullname, mobileNo, areaCode,
				currentUserId);
		if (_count != 0) {
			Integer offset = (page - 1) * rows;
			List<User> _result = userMapper.findBy(fullname, mobileNo,
					areaCode, currentUserId, offset, rows);
			queryResult = new SQLQueryResult<User>(_count, _result);
		} else {
			queryResult = SQLQueryResult.EMPTY_RESULT;
		}
		return queryResult;
	}

	
	/**
	 * 	个人设置
	 * @param userId
	 * @param command
	 */
	@Transactional(rollbackFor = Exception.class)
	public void editPersonSetting(Long userId, PersonSettingCommand command) {
		User user = fromPersonSettingCommand(command);
		user.setId(userId);
		userMapper.updatePersonSetting(user);
	}

	/**
	 * 修改个人密码
	 * @param userId
	 * @param originalPassword
	 * @param newPassword
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updateMyPassword(Long userId, String originalPassword,
			String newPassword) {
		User user = userMapper.getById(userId);
		String _hashOriginalPassword = HashUtil.encodeMd5Hex(originalPassword);
		if (!_hashOriginalPassword.equals(user.getPassword())) {
			throw new PasswordNotMatchException(userId);
		}
		String _hashNewPassword = HashUtil.encodeMd5Hex(newPassword);
		userMapper.updatePassword(userId, _hashNewPassword);
	}

	


	/**
	 * 根据表单创建用户
	 * @param command
	 * @return
	 */
	private User fromCreateUserCommand(CreateUserCommand command) {
		User user = new User();
		user.setUsername(command.getUsername());
		if(!StringUtils.isEmpty(command.getPassword()))
				user.setPassword(HashUtil.encodeMd5Hex(command.getPassword()));
		user.setFullname(command.getFullname());
		user.setEmail(command.getEmail());
		user.setAreaCode(command.getAreaCode());
		user.setWorkerNo(command.getWorkerNo());
		user.setMobileNo(command.getMobileNo());
		user.setTelNo(command.getTelNo());
		user.setRoleId(command.getRoleId());
		return user;
	}

	/**
	 * 根据表单修改用户
	 * @param command
	 * @return
	 */
	private User fromEditUserCommand(EditUserCommand command) {
		User user = fromCreateUserCommand(command);
		user.setId(Long.parseLong(command.getId()));
		return user;

	}



	/**
	 * 根据表单个人设置
	 * @param command
	 * @return
	 */
	private User fromPersonSettingCommand(PersonSettingCommand command) {
		User user = new User();
		user.setFullname(command.getFullname());
		user.setTelNo(command.getTelNo());
		user.setMobileNo(command.getMobileNo());
		user.setEmail(command.getEmail());
		return user;
	}
	
	
}
