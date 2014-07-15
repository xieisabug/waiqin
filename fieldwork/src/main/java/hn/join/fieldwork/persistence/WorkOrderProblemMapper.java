package hn.join.fieldwork.persistence;

import hn.join.fieldwork.domain.WorkOrderProblem;

import java.util.List;
/**
 * 工单问题DAO
 * 操作表：tbl_work_order_problem
 * @author chenjinlong
 *
 */
public interface WorkOrderProblemMapper {
	/**
	 * 根据工单号获得工单问题
	 * @param workOrderNo
	 * @return
	 */
	List<WorkOrderProblem> getByWorkOrderNo(String workOrderNo);
	/**
	 * 根据问题ID获得问题信息
	 * @param problemId
	 * @return
	 */
	WorkOrderProblem getById(Integer problemId);
	/**
	 * 新增问题
	 * @param problem
	 */
	void insertProblem(WorkOrderProblem problem);
	/**
	 * 批量新增问题
	 * @param problems
	 */
	void insertProblems(List<WorkOrderProblem> problems);
	/**
	 * 批量更新问题
	 * @param problems
	 */
	void updateProblemList(WorkOrderProblem problem);
	/**
	 * 删除问题
	 * @param problme
	 */
	void updateProblem(WorkOrderProblem problme);

}
