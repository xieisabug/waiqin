package hn.join.fieldwork.domain;

import java.util.Date;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;
/**
 * 设备对象
 * @author chenjinlong
 *
 */
public class Device extends BaseDomain {

	private static final long serialVersionUID = -7436891866310898590L;

	public enum DeviceStatus {
		ACTIVE, LOSS, DAMAGE;
	}
	/**主键*/
	private Long id;
	/**电话号码*/
	private String phoneNo;
	/**动设备识别码*/
	private String meid;
	/**模式*/
	private String model;
	/**当前状态*/
	private Float currentAmount;
	/**消费类别*/
	private String consumeCategory;
	/**设备状态ACTIVE, LOSS, DAMAGE*/
	private DeviceStatus status;
	/**区域编码*/
	private String areaCode;
	/**创建时间*/
	private Date createTime;

	@JsonIgnore
	private Set<DeviceJournal> journals;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getMeid() {
		return meid;
	}

	public void setMeid(String meid) {
		this.meid = meid;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Float getCurrentAmount() {
		return currentAmount;
	}

	public void setCurrentAmount(Float currentAmount) {
		this.currentAmount = currentAmount;
	}

	public String getConsumeCategory() {
		return consumeCategory;
	}

	public void setConsumeCategory(String consumeCategory) {
		this.consumeCategory = consumeCategory;
	}

	public DeviceStatus getStatus() {
		return status;
	}

	public void setStatus(DeviceStatus status) {
		this.status = status;
	}
	
	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Set<DeviceJournal> getJournals() {
		return journals;
	}

	public void setJournals(Set<DeviceJournal> journals) {
		this.journals = journals;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((meid == null) ? 0 : meid.hashCode());
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
		Device other = (Device) obj;
		if (meid == null) {
			if (other.meid != null)
				return false;
		} else if (!meid.equals(other.meid))
			return false;
		return true;
	}

}
