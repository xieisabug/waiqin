package hn.join.fieldwork.domain;

import org.codehaus.jackson.annotate.JsonIgnore;
/**
 * 业务人员数据对象
 * @author chenjinlong
 *
 */
public class FieldWorker extends BaseDomain {

//	public enum FieldWorkerStatus {
//		enable, disable
//	}

	private static final long serialVersionUID = 8996783703882641081L;
	/**主键*/
	private Long id;
	/**姓名*/
	private String fullname;
	/**密码*/
	@JsonIgnore
	private String password;
	/**工号*/
	private String workerNo;
	/**设备*/
	private  Device device;
	/**部门*/
	private Department department;
	/**设备ID*/
	private Integer deviceId;
	/**部门ID*/
	private Integer departmentId;

//	private FieldWorkerStatus status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getWorkerNo() {
		return workerNo;
	}

	public void setWorkerNo(String workerNo) {
		this.workerNo = workerNo;
	}
	

	public Integer getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Integer deviceId) {
		this.deviceId = deviceId;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}


	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

//	public FieldWorkerStatus getStatus() {
//		return status;
//	}
//
//	public void setStatus(FieldWorkerStatus status) {
//		this.status = status;
//	}

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
		FieldWorker other = (FieldWorker) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

    @Override
    public String toString() {
        return "FieldWorker{" +
                "id=" + id +
                ", fullname='" + fullname + '\'' +
                ", password='" + password + '\'' +
                ", workerNo='" + workerNo + '\'' +
                ", device=" + device +
                ", department=" + department +
                ", deviceId=" + deviceId +
                ", departmentId=" + departmentId +
                '}';
    }
}
