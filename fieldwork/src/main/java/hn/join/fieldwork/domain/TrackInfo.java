package hn.join.fieldwork.domain;

import hn.join.fieldwork.utils.CustomDateTimeSerializer;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;
/**
 * 跟踪信息对象
 * @author chenjinlong
 *
 */
public class TrackInfo extends BaseDomain{
	
	
	private static final long serialVersionUID = 6544819224054777069L;
	/**主键*/
	private Long id;
	/**工单ID*/
	private Long fieldWorkerId;
	/**区域编码*/
	private String areaCode;
	/**业务员*/
	@JsonIgnore
	private FieldWorker fieldworker;
	/**经度*/
	private Float latitude;
	/**纬度*/
	private Float longitude;
	/**地址*/
	private String address;
	/**创建时间*/
	private Date createTime;
	
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

	public FieldWorker getFieldworker() {
		return fieldworker;
	}

	public void setFieldworker(FieldWorker fieldworker) {
		this.fieldworker = fieldworker;
	}

	public Float getLatitude() {
		return latitude;
	}

	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}

	public Float getLongitude() {
		return longitude;
	}

	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	@JsonSerialize(using = CustomDateTimeSerializer.class)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
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
		TrackInfo other = (TrackInfo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

    @Override
    public String toString() {
        return "TrackInfo{" +
                "id=" + id +
                ", fieldWorkerId=" + fieldWorkerId +
                ", areaCode='" + areaCode + '\'' +
                ", fieldworker=" + fieldworker +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", address='" + address + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
