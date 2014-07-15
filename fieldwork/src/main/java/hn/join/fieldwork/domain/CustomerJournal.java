package hn.join.fieldwork.domain;

import java.util.Date;
/**
 * 客户日志
 * @author chenjinlong
 *
 */
public class CustomerJournal extends BaseDomain{

	
	private static final long serialVersionUID = 6280446044931732733L;
	/**主键*/
	private Long id;
	/**创建日期*/
	private Date createTime;
	/**原始状态*/
	private String previousState;
	/**备忘录||通知*/
	private String memo;
	/**操作人员*/
	private User operator;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getPreviousState() {
		return previousState;
	}

	public void setPreviousState(String previousState) {
		this.previousState = previousState;
	}


	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public User getOperator() {
		return operator;
	}

	public void setOperator(User operator) {
		this.operator = operator;
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
		CustomerJournal other = (CustomerJournal) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
