package hn.join.fieldwork.domain;

import hn.join.fieldwork.utils.CustomDateTimeSerializer;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;
/**
 * 签到对象
 * @author chenjinlong
 *
 */
public class Checkin extends BaseDomain {

	private static final long serialVersionUID = 4181109416758215652L;
	/**
	 * 主键
	 */
	private Long id;
	/**业务人员ID*/
	private Long fieldWorkerId;
	/**人员签到时间*/
	private Date userCheckinDateTime;
	/**系统签到时间*/
	private Date systemCheckinDateTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getFieldWorkerId() {
		return fieldWorkerId;
	}

	public void setFieldWorkerId(Long fieldWorkerId) {
		this.fieldWorkerId = fieldWorkerId;
	}

	@JsonSerialize(using = CustomDateTimeSerializer.class)
	public Date getUserCheckinDateTime() {
		return userCheckinDateTime;
	}

	public void setUserCheckinDateTime(Date userCheckinDateTime) {
		this.userCheckinDateTime = userCheckinDateTime;
	}

	@JsonSerialize(using = CustomDateTimeSerializer.class)
	public Date getSystemCheckinDateTime() {
		return systemCheckinDateTime;
	}

	public void setSystemCheckinDateTime(Date systemCheckinDateTime) {
		this.systemCheckinDateTime = systemCheckinDateTime;
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
		Checkin other = (Checkin) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
