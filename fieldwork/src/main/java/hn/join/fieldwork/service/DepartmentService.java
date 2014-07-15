package hn.join.fieldwork.service;

import hn.join.fieldwork.domain.Department;
import hn.join.fieldwork.domain.Device;
import hn.join.fieldwork.persistence.DepartmentMapper;
import hn.join.fieldwork.persistence.SQLQueryResult;
import hn.join.fieldwork.web.command.CreateDepartmentCommand;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 部门信息相关操作服务类
 * @author aisino_lzw
 *
 */
@Service
public class DepartmentService {

	@Autowired
	private DepartmentMapper departmentMapper;

	/**
	 * 新建部门
	 * @param command 部门信息
	 * @throws DataAccessException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void newDepartment(CreateDepartmentCommand command)
			throws DataAccessException {
		Department _department = fromCreateDepartmentCommand(command);
		departmentMapper.insertDepartment(_department);
	}

	/**
	 * 删除部门
	 * @param departmentId 部门id
	 * @throws DataAccessException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void removeDepartment(Integer departmentId)
			throws DataAccessException {
		departmentMapper.deleteDepartment(departmentId);
	}

	/**
	 * 根据部门名称查询部门信息
	 * @param departmentName 部门名称
	 * @param page 指定页
	 * @param rows 指定行数
	 * @return 部门列表
	 */
	public SQLQueryResult<Department> findBy(String departmentName,
			Integer page, Integer rows) {
		SQLQueryResult<Department> queryResult = null;
		long _count = departmentMapper.countBy(departmentName);
		if (_count != 0) {
			Integer offset = (page - 1) * rows;
			List<Department> _result = departmentMapper.findBy(departmentName,
					offset, rows);
			queryResult = new SQLQueryResult(_count, _result);
		} else {
			queryResult = SQLQueryResult.EMPTY_RESULT;
		}
		return queryResult;
	}

	
	/**
	 * 新建部门对象
	 * @param command
	 * @return
	 */
	private Department fromCreateDepartmentCommand(
			CreateDepartmentCommand command) {
		Department _department = new Department();
		_department.setId(command.getId());
		_department.setDepartmentCode(command.getDepartmentCode());
		_department.setDepartmentName(command.getDepartmentName());
		_department.setCity(command.getCity());
		return _department;
	}

}
