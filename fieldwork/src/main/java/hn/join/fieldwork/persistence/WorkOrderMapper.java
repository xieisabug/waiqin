package hn.join.fieldwork.persistence;

import hn.join.fieldwork.domain.WorkOrder;
import hn.join.fieldwork.web.command.WorkOrderLess;
import hn.join.fieldwork.web.command.WorkOrderTimeout;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
/**
 * 工单处理DAO
 * 操作表：tbl_work_order
 * @author chenjinlong
 *
 */
public interface WorkOrderMapper {
	/**
	 * 新增工单 
	 * @param workOrder
	 */
	void insertWorkOrder(WorkOrder workOrder);
	
	/**
	 * 更新工单 
	 * @param workOrder
	 */
	void updateWorkOrder(WorkOrder workOrder);
	
	/**
	 * 根据ID获得工单信息
	 * @param orderId
	 * @return
	 */
	WorkOrder getById(Long orderId);
	/**
	 * 根据工单号获得工单信息
	 * @param workOrderNo
	 * @return
	 */
	WorkOrder getByWorkOrderNo(String workOrderNo);

	// void updateWhenDispatch(@Param(value = "orderId") Long orderId,
	// @Param(value = "fieldworkerId") Long fieldworkerId,
	// @Param(value = "dispatchTime") Date dispatchTime);
	/**
	 * 更新工单确认时间
	 * @param workOrderNo
	 * @param confirmTime
	 */
	void updateWhenConfirm(@Param(value = "workOrderNo") String workOrderNo,
                           @Param(value = "confirmTime") Date confirmTime);
	/**
	 * 更新查看问题时间
	 * @param workOrderNo
	 * @param viewTime
	 */
	void updateWhenView(@Param(value = "workOrderNo") String workOrderNo,
                        @Param(value = "viewTime") Date viewTime);
	/**
	 * 更新外出时间 
	 * @param workOrderNo
	 */
	void updateWhenTimeout(@Param(value = "workOrderNo") String workOrderNo);
	/**
	 * 更新接单时间
	 * @param workOrderNo
	 * @param acceptTime
	 */
	void updateWhenAccept(@Param(value = "workOrderNo") String workOrderNo,
                          @Param(value = "acceptTime") Date acceptTime);
	/**
	 * 更新呼叫客户时间 
	 * @param workOrderNo
	 * @param callTime
	 */
	void updateWhenCallCustomer(
            @Param(value = "workOrderNo") String workOrderNo,
            @Param(value = "callTime") Date callTime);
	/**
	 * 更新二次派工时间
	 * @param workOrderNo
	 * @param reason
	 * @param reassignTime
	 */
	void updateWhenReassign(@Param(value = "workOrderNo") String workOrderNo,
                            @Param(value = "reason") String reason,
                            @Param(value = "reassignTime") Date reassignTime);
	/**
	 * 更新到达现场时间
	 * @param workOrderNo
	 * @param arrivalTime
	 */
	void updateWhenArrival(@Param(value = "workOrderNo") String workOrderNo,
                           @Param(value = "arrivalTime") Date arrivalTime);
	/**
	 * 更新完成工单时间
	 * @param workOrderNo
	 * @param finishTime
	 */
	void updateWhenFinish(@Param(value = "workOrderNo") String workOrderNo,
                          @Param(value = "finishTime") Date finishTime);
	/**
	 * 根据业务员ID查找未完成的工单
	 * @param fieldWorkerId
	 * @return
	 */
	List<String> findUnfinishedWorkOrdersByFieldWorkerId(Long fieldWorkerId);
	/**
	 * 根据工单号查找业务员ID
	 * @param workOrderNo
	 * @return
	 */
	Long findFieldWorkerIdByWorkOrderNo(String workOrderNo);
	/**
	 * 根据时间段查找工单
	 * @param fieldWorkerId
	 * @param fromTime
	 * @param toTime
	 * @return
	 */
	List<WorkOrderLess> findByBetweenFinishTime(
            @Param(value = "fieldWorkerId") Long fieldWorkerId,
            @Param(value = "fromTime") Date fromTime,
            @Param(value = "toTime") Date toTime);
	/**
	 * 根据工单获得客户电话号码
	 * @param workOrderNo
	 * @return
	 */
	String getCustomerMobileByWorkOrderNo(String workOrderNo);
	/**
	 * 根据外勤人员查询超时工单
	 * @param fullname
	 * @param city
	 * @return
	 */
	long countWorkOrderTimeoutBy(@Param(value = "fullname") String fullname,//外勤人员姓名
                                 @Param(value = "city") String city);
	/**
	 * 分布查询超时工单
	 * @param fullname
	 * @param city
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<WorkOrderTimeout> findWorkOrderTimeoutBy(
            @Param(value = "fullname") String fullname,//外勤人员姓名
            @Param(value = "city") String city,
            @Param(value = "offset") Integer offset,
            @Param(value = "limit") Integer limit);
	/**
	 * 根据工单状态统计
	 * @param workOrderNo
	 * @param statusField
	 * @return
	 */
	long  countStatusByWorkOrderNo(@Param(value = "workOrderNo") String workOrderNo, @Param(value = "statusField") String statusField);
	/**
	 * 删除过期工单
	 */
	void  deleteStaleOrder();
	/**
	 * 根据工单号码查询客户ID
	 * @param workOrderNo
	 * @return
	 */
	Long findCustomerIdByWorkOrderNo(String workOrderNo);
	

}
