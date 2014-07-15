package hn.join.fieldwork.domain;

import java.util.Date;
/**
 * 数据同步对象
 * @author chenjinlong
 *
 */
public class SyncDataTransferObject {

	public enum DtoType {
		order_received, order_view, order_reassign,customer_call,solution_by_remote,dest_arrive,order_finish,receipt_feed,cust_info;
	}
	/**主键*/
	private Integer id;
	/**DTO类型*/
	private DtoType dtoType;
	/**DTO对象*/
	private String dto;
	/**同步时间*/
	private Date syncTime;
	/**结果*/
	private Integer result;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public DtoType getDtoType() {
		return dtoType;
	}

	public void setDtoType(DtoType dtoType) {
		this.dtoType = dtoType;
	}

	public String getDto() {
		return dto;
	}

	public void setDto(String dto) {
		this.dto = dto;
	}

	public Date getSyncTime() {
		return syncTime;
	}

	public void setSyncTime(Date syncTime) {
		this.syncTime = syncTime;
	}

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	public SyncDataTransferObject() {
		super();
	}

	public SyncDataTransferObject(DtoType dtoType, String dto) {
		super();
		this.dtoType = dtoType;
		this.dto = dto;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SyncDataTransferObject other = (SyncDataTransferObject) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
	

}
