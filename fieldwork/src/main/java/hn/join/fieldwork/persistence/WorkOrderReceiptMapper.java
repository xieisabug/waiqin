package hn.join.fieldwork.persistence;

import hn.join.fieldwork.domain.WorkOrderReceipt;
/**
 * 回单处理DAO
 * 操作表：tbl_work_order_receipt
 * @author chenjinlong
 *
 */
public interface WorkOrderReceiptMapper {
	/**
	 * 新增回单 	
	 * @param receipt
	 */
	void insertWorkOrderReceipt(WorkOrderReceipt receipt);
	/**
	 * 根据工单号码获取回音信息	
	 * @param workOrderNo
	 * @return
	 */
	WorkOrderReceipt getByWorkOrderNo(String workOrderNo);
	/**
	 * 回单统计 
	 * @param workOrderNo
	 * @return
	 */
	int countByWorkOrderNo(String workOrderNo);
	

}
