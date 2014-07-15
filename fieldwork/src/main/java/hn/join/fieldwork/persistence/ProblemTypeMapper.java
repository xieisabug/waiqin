package hn.join.fieldwork.persistence;

import hn.join.fieldwork.domain.ProblemType;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
/**
 * 问题类别DAO
 * 操作表tbl_problem_type
 * @author chenjinlong
 *
 */
public interface ProblemTypeMapper {
	/**
	 * 新增问题类型
	 * @param problemCategory
	 */
	public void insertProblemType(
            @Param(value = "problemType") ProblemType problemType,
            @Param(value = "problemCategoryId") Integer problemCategoryId);
	/**
	 * 删除问题类型
	 * @param problemCategory
	 */
	public void deleteProblemType(@Param(value = "problemTypeId") Integer problemTypeId);
	/**
	 * 统计问题类型 
	 * @param problemCategory
	 */
	public int countBy(@Param(value = "name") String name);
	/**
	 * 查找问题分型 
	 * @param problemCategory
	 */
	public List<ProblemType> findBy(@Param(value = "name") String name,
                                    @Param(value = "offset") Integer offset,
                                    @Param(value = "limit") Integer limit);
	/**
	 * 查找所有问题类型
	 * @param problemCategory
	 */
	public List<Map<String, Object>> findAll();

}
