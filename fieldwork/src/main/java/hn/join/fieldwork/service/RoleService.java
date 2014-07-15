package hn.join.fieldwork.service;

import hn.join.fieldwork.domain.Permission;
import hn.join.fieldwork.domain.Role;
import hn.join.fieldwork.persistence.RoleMapper;
import hn.join.fieldwork.persistence.SQLQueryResult;
import hn.join.fieldwork.web.command.CreateRoleCommand;
import hn.join.fieldwork.web.command.EditRoleCommand;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;


/**
 * 角色服务类
 * @author aisino_lzw
 *
 */
@Service
public class RoleService {

	@Autowired
	private RoleMapper roleMapper;
	
	/**
	 * 根据id获取角色
	 * @param roleId
	 * @return
	 */
	public Role getById(Integer roleId){
		return roleMapper.getById(roleId);
	}
	
	/**
	 * 条件查询角色
	 * @param roleName
	 * @param page
	 * @param rows
	 * @return
	 */
	public SQLQueryResult<Role> findBy(String roleName, Integer page, Integer rows){
		SQLQueryResult<Role> queryResult = null;

		long _count = roleMapper.countByRoleName(roleName);
		if (_count != 0) {
			Integer offset = null;
			if (page != null && rows != null)
				offset = (page - 1) * rows;
			List<Role> _result = roleMapper.findByRoleName(roleName, offset, rows);
			queryResult = new SQLQueryResult<Role>(_count, _result);
		} else {
			queryResult = SQLQueryResult.EMPTY_RESULT;
		}
		return queryResult;
	}

	/**
	 * 新建角色
	 * @param command
	 * @throws DataAccessException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void newRole(CreateRoleCommand command) throws DataAccessException {
		Role role = fromCreateRoleCommand(command);
		roleMapper.insertRole(role);
	}

	/**
	 * 修改角色
	 * @param command
	 * @throws DataAccessException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void editRole(EditRoleCommand command) throws DataAccessException {
		Role role = fromEditRoleCommand(command);
		roleMapper.updateRole(role);
	}

	/**
	 * 删除角色
	 * @param roleId
	 * @throws DataAccessException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void removeRole(Integer roleId) throws DataAccessException {
		roleMapper.deleteRole(roleId);
	}

	
	/**
	 * 获取所有角色
	 * @return
	 */
	public List<Role> listAllRole(){
		return roleMapper.listAllRoles();
	}
	

	/**
	 * 从表单中添加角色
	 * @param command
	 * @return
	 */
	private Role fromCreateRoleCommand(CreateRoleCommand command) {
		Role role = new Role();
		role.setRoleName(command.getRoleName());
		role.setPermissions(Sets.newHashSet(Collections2.transform(
				command.getPermissionSet(), new Function<String, Permission>() {
					@Override
					public Permission apply(String input) {
						return Permission.createByShiroFormat(input);
					}

				})));
		return role;
	}

	/**
	 * 接收表单数据修改角色
	 * @param command
	 * @return
	 */
	private Role fromEditRoleCommand(EditRoleCommand command) {
		Role role = fromCreateRoleCommand(command);
		role.setId(Integer.parseInt(command.getRoleId()));
		return role;
	}

}
