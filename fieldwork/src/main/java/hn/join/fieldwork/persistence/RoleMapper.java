package hn.join.fieldwork.persistence;

import hn.join.fieldwork.domain.Role;

import java.util.List;

import org.apache.ibatis.annotations.Param;
/**
 * 角色DAO
 * 操作表tbl_role
 * @author chenjinlong
 *
 */
public interface RoleMapper {
	/**
	 * 根据ID获得角色信息
	 * @param roleId
	 * @return
	 */
	Role getById(Integer roleId);

	// Role getByName(String roleName);
	/**
	 * 新增角色
	 * @param role
	 */
	void insertRole(Role role);
	/**
	 * 更新角色信息
	 * @param role
	 */
	void updateRole(Role role);
	/**
	 * 删除角色
	 * @param roleId
	 */
	void deleteRole(Integer roleId);

	// List<Role> loadAllRoles();
	/**
	 * 根据角色名统计
	 * @param roleName
	 * @return
	 */
	long countByRoleName(@Param(value = "roleName") String roleName);
	/**
	 * 按条件查询角色	
	 * @param roleName
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<Role> findByRoleName(@Param(value = "roleName") String roleName,
                              @Param(value = "offset") Integer offset,
                              @Param(value = "limit") Integer limit);
	/**
	 * 查询所有的角色
	 * @return
	 */
	List<Role> listAllRoles();

}
