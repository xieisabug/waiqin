package hn.join.fieldwork.domain;

import java.util.Date;
/**
 * 签到设置
 * @author chenjinlong
 *
 */
public class CheckinSetting extends BaseDomain {
	
	
	
	private static final long serialVersionUID = -9150536000854965669L;

	public enum CheckinStatus{
		ENABLE,DISABLE;
	}

	/**主键*/
	private Long id;
	/**设置日期*/
	private String settingDate;
	
//	private String areaCode;
	/**签到状态，ENABLE,DISABLE*/
	private CheckinStatus status;
	
//	private String message;
	/**创建日期*/
	private Date createTime;
	/**创建人*/
	private User creator;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSettingDate() {
		return settingDate;
	}

	public void setSettingDate(String settingDate) {
		this.settingDate = settingDate;
	}

//	public String getMessage() {
//		return message;
//	}
//
//	public void setMessage(String message) {
//		this.message = message;
//	}
	
	

	public CheckinStatus getStatus() {
		return status;
	}

	public void setStatus(CheckinStatus status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}
	
	

//	public String getAreaCode() {
//		return areaCode;
//	}
//
//	public void setAreaCode(String areaCode) {
//		this.areaCode = areaCode;
//	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((settingDate == null) ? 0 : settingDate.hashCode());
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
		CheckinSetting other = (CheckinSetting) obj;
		if (settingDate == null) {
			if (other.settingDate != null)
				return false;
		} else if (!settingDate.equals(other.settingDate))
			return false;
		return true;
	}
	
	
	
}
