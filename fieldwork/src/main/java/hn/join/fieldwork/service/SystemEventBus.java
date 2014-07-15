package hn.join.fieldwork.service;

import hn.join.fieldwork.domain.Notification;
import hn.join.fieldwork.domain.SyncDataTransferObject;
import hn.join.fieldwork.domain.WorkOrder;
import hn.join.fieldwork.web.dto.CustomerDto;
import hn.join.fieldwork.web.dto.WorkOrderDto;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.google.common.eventbus.EventBus;

/**
 * 同步事件处理机制
 * @author aisino_lzw
 *
 */
@Component
public class SystemEventBus {
	
	private final static Logger LOG = Logger
	.getLogger(SystemEventBus.class);

	private EventBus eventBus;

	public SystemEventBus() {
		this.eventBus = new EventBus();
	}

	public EventBus getEventBus() {
		return this.eventBus;
	}

	/**
	 * 员工确认工单信息同步
	 * @param request
	 */
	public void postWorkOrderConfirmRequest(WorkOrderConfirmRequest request) {
		this.eventBus.post(request);
	}

	// public void postWorkOrderSubstituteRequest(
	// WorkOrderSubstituteRequest request) {
	// this.eventBus.post(request);
	// }

	/**
	 * 员工修改工单信息同步
	 */
	public void postFieldWorkerUpdateRequestOnNewOrder(NewOrderRequest request) {
		LOG.debug("post 消息:"+ request);
		this.eventBus.post(request);
	}

	/**
	 * 员工完成工单信息同步
	 * @param request
	 */
	public void postFieldWorkerUpdateRequestOnFinishOrder(
			FinishOrderRequest request) {
		this.eventBus.post(request);
	}

	/**
	 * 员工轨迹信息同步
	 * @param request
	 */
	public void postFieldWorkerUpdateRequestOnTrackInfo(
			TrackInfoUpdateRequest request) {
		this.eventBus.post(request);
	}

	/**
	 * 公告同步
	 * @param notification
	 */
	public void postNotification(Notification notification) {
		this.eventBus.post(notification);
	}

	/**
	 * 发布同步信息
	 * @param syncObject
	 */
	public void postSyncData(SyncDataTransferObject syncObject) {
		this.eventBus.post(syncObject);
	}

	/**
	 * 发布更新信息
	 * @param dataUpdateRequest
	 */
	public void postDataUpdate(DataUpdateRequest dataUpdateRequest) {
		this.eventBus.post(dataUpdateRequest);
	}

	/**
	 * 发送信息同步
	 * @param sendClientMessageRequest
	 */
	public void postSendClientMessage(
			SendClientMessageRequest sendClientMessageRequest) {
		this.eventBus.post(sendClientMessageRequest);
	}
   /**
    * 发布更新客户信息
    * @author chenjl
    * @version time:2014年6月11日 下午7:08:44
    *
    */
	/*public void postUpdateCustomer(UpdateCustomerRequest updateCustomerRequest) {
		this.eventBus.post(updateCustomerRequest);
	}
	*/
	public static abstract class FieldWorkerUpdateRequest {

		protected final String areaCode;

		protected final String fieldWorkerName;

		private final Long fieldWorkerId;

		protected final long updateTime;

		public FieldWorkerUpdateRequest(String areaCode, Long fieldWorkerId,
				String fieldWorkerName, long updateTime) {
			super();
			this.areaCode = areaCode;
			this.fieldWorkerName = fieldWorkerName;
			this.fieldWorkerId = fieldWorkerId;
			this.updateTime = updateTime;
		}

		public String getAreaCode() {
			return areaCode;
		}

		public String getFieldWorkerName() {
			return fieldWorkerName;
		}

		public long getUpdateTime() {
			return updateTime;
		}

		public Long getFieldWorkerId() {
			return fieldWorkerId;
		}

	}

	/**
	 * 新建工单
	 * @author aisino_lzw
	 *
	 */
	public static class NewOrderRequest extends FieldWorkerUpdateRequest {

		private final String workOrderNo;

		private final WorkOrder workOrder;

		private final WorkOrderDto dto;

		@Override
		public String toString() {
			return "NewOrderRequest [workOrderNo=" + workOrderNo
					+ ", workOrder=" + workOrder + ", dto=" + dto + "]";
		}

		// public NewOrderRequest(String areaCode, String fieldWorkerNo,
		// long updateTime, String workOrderNo) {
		// super(areaCode, fieldWorkerNo, updateTime);
		// this.workOrderNo = workOrderNo;
		// }
		//
		public NewOrderRequest(WorkOrder workOrder, WorkOrderDto dto,
				String fieldWorkerName) {
			super(workOrder.getAreaCode(), workOrder.getFieldWorkerId(),
					fieldWorkerName, System.currentTimeMillis());
			this.workOrder = workOrder;
			this.workOrderNo = workOrder.getWorkOrderNo();
			this.dto = dto;
		}

		public String getWorkOrderNo() {
			return workOrderNo;
		}

		public WorkOrder getWorkOrder() {
			return workOrder;
		}

		public WorkOrderDto getDto() {
			return dto;
		}

	}

	/**
	 * 完成工单
	 * @author aisino_lzw
	 *
	 */
	public static class FinishOrderRequest extends FieldWorkerUpdateRequest {

		private final String workOrderNo;

		public FinishOrderRequest(String areaCode, Long fieldWorkerId,
				String fieldWorkerName, long updateTime, String workOrderNo) {
			super(areaCode, fieldWorkerId, fieldWorkerName, updateTime);
			this.workOrderNo = workOrderNo;
		}

		public String getWorkOrderNo() {
			return workOrderNo;
		}

	}

	/**
	 * 更新轨迹信息
	 * @author aisino_lzw
	 *
	 */
	public static class TrackInfoUpdateRequest extends FieldWorkerUpdateRequest {

		private final float latestLatitude;
		private final float latestLongitude;
		private final String latestAddress;

		public TrackInfoUpdateRequest(String areaCode, Long fieldWorkerId,
				String fieldWorkerName, long updateTime, float latestLatitude,
				float latestLongitude,String latestAddress) {
			super(areaCode, fieldWorkerId, fieldWorkerName, updateTime);
			this.latestLatitude = latestLatitude;
			this.latestLongitude = latestLongitude;
			this.latestAddress=latestAddress;
		}

		public float getLatestLatitude() {
			return latestLatitude;
		}

		public float getLatestLongitude() {
			return latestLongitude;
		}

		public String getLatestAddress() {
			return latestAddress;
		}
		
		
		

	}

	
	/**
	 * 员工确认工单
	 * @author aisino_lzw
	 *
	 */
	public static class WorkOrderConfirmRequest {

		private final String workOrderNo;

		private final Date confirmTime;

		public WorkOrderConfirmRequest(String workOrderNo, Date confirmTime) {
			super();
			this.workOrderNo = workOrderNo;
			this.confirmTime = confirmTime;
		}

		public String getWorkOrderNo() {
			return workOrderNo;
		}

		public Date getConfirmTime() {
			return confirmTime;
		}

	}

	/**
	 * 数据更新
	 * @author aisino_lzw
	 *
	 */
	public static class DataUpdateRequest {
		public enum DataType {
			revenue, problem_category, problem_type, expense_item;
		}

		private final DataType dataType;

		private final Date updateTime;

		public DataUpdateRequest(DataType dataType, Date updateTime) {
			super();
			this.dataType = dataType;
			this.updateTime = updateTime;
		}

		public DataType getDataType() {
			return dataType;
		}

		public Date getUpdateTime() {
			return updateTime;
		}

	}

	// public static class WorkOrderSubstituteRequest {
	//
	// private final ApplySubstitutionCommand applySubstitutionCommand;
	//
	// public WorkOrderSubstituteRequest(ApplySubstitutionCommand command) {
	// this.applySubstitutionCommand = command;
	// }
	//
	// public ApplySubstitutionCommand getApplySubstitutionCommand() {
	// return applySubstitutionCommand;
	// }
	//
	// }

	public static class SendClientMessageRequest {

		private final String workOrderNo;

		public SendClientMessageRequest(String workOrderNo) {
			this.workOrderNo = workOrderNo;
		}

		public String getWorkOrderNo() {
			return workOrderNo;
		}

	}
	/**
	 * 用于修改客户基本信息	 
	 * @author chenjl
	 * @version time:2014年6月11日 下午7:02:44
	 *
	 */
	/*public static class UpdateCustomerRequest{
		private final CustomerDto customerDto;
		public UpdateCustomerRequest(CustomerDto customerDto){
			super();
			this.customerDto=customerDto;
		}
		public CustomerDto getCustomerDto() {
			return customerDto;
		}		
		
	}*/

}
