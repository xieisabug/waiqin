package hn.join.fieldwork.persistence;

import hn.join.fieldwork.domain.Department;

import java.util.List;

import org.apache.ibatis.annotations.Param;
/**
 * 部门DAO
 * 操作表：tbl_department
 * @author chenjinlong
 *
 */
public interface DepartmentMapper {
	/**
	 * 根据部门ID获取部门信息
	 * @param departmentId
	 * @return
	 */
	public Department getById(Integer departmentId);
	/**
	 * 新增部门记录
	 * @param department
	 */
	public void insertDepartment(Department department);
	/**
	 * 删除部门记录
	 * @param departmentId
	 */
	public void deleteDepartment(Integer departmentId);
	/**
	 * 统计部门信息
	 * @param departmentName
	 * @return
	 */
	public long countBy(@Param(value = "departmentName") String departmentName);
	/**
	 * 查询部门信息
	 * @param departmentName
	 * @param offset
	 * @param limit
	 * @return
	 */
	public List<Department> findBy(
            @Param(value = "departmentName") String departmentName,
            @Param(value = "offset") Integer offset,
            @Param(value = "limit") Integer limit);

}
