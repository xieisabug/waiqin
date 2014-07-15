package hn.join.fieldwork.persistence;

import hn.join.fieldwork.domain.ProblemCategory;

import java.util.List;

import org.apache.ibatis.annotations.Param;
/**
 * 问题类别DAO
 * 操作表tbl_problem_category
 * @author chenjinlong
 *
 */
public interface ProblemCategoryMapper {
	/**
	 * 新增问题分类 
	 * @param problemCategory
	 */
	public void insertProblemCategory(ProblemCategory problemCategory);
	/**
	 * 删除问题类别
	 * @param id
	 */
	public void deleteProblemCategory(Integer id);
/**
 * 统计问题类别
 * @param name
 * @return
 */
	public int countBy(@Param(value = "name") String name);
/**
 * 查询问题类别
 * @param name
 * @param offset
 * @param limit
 * @return
 */
	public List<ProblemCategory> findBy(@Param(value = "name") String name,
                                        @Param(value = "offset") Integer offset,
                                        @Param(value = "limit") Integer limit);
	/**
	 * 查找所有问题分类
	 * @return
	 */
	public List<ProblemCategory> findAll();
}
