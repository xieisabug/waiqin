package hn.join.fieldwork.web.command;

import hn.join.fieldwork.domain.WorkOrder.WorkOrderType;
import hn.join.fieldwork.utils.CustomDateTimeSerializer;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

public class WorkOrderLess {
	
	private String workOrderNo;
	
	private String orderToken;
	
	private String customerName;
	
	@JsonSerialize(using = CustomDateTimeSerializer.class)
	private Date finishTime;
	
	private WorkOrderType workOrderType;

	public String getWorkOrderNo() {
		return workOrderNo;
	}

	public void setWorkOrderNo(String workOrderNo) {
		this.workOrderNo = workOrderNo;
	}

	
	public String getOrderToken() {
		return orderToken;
	}

	public void setOrderToken(String orderToken) {
		this.orderToken = orderToken;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	public WorkOrderType getWorkOrderType() {
		return workOrderType;
	}

	public void setWorkOrderType(WorkOrderType workOrderType) {
		this.workOrderType = workOrderType;
	}
	
	

}
