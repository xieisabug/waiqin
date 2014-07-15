package hn.join.fieldwork.persistence;

import hn.join.fieldwork.domain.SystemParameter;

import java.util.List;

import org.apache.ibatis.annotations.Param;
/**
 * 系统变量操作DAO
 * 操作表：tbl_system_parameter
 * @author chenjinlong
 *
 */
public interface SystemParameterMapper {
	/**
	 * 新增系统变量
	 * @param systemParameter
	 */
	void insertSystemParameter(SystemParameter systemParameter);
	/**
	 * 加载系统变量
	 * @return
	 */
	List<SystemParameter> loadAll();
	/**
	 * 修改系统变量
	 * @param paramName
	 * @param paramValue
	 */
	void updateSystemParameter(@Param(value = "paramName") String paramName,
                               @Param(value = "paramValue") String paramValue);

}
