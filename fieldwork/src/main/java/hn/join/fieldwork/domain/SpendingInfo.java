package hn.join.fieldwork.domain;
/**
 * Spending对象
 * @author chenjinlong
 *
 */
public class SpendingInfo extends BaseDomain{
	
	
	private static final long serialVersionUID = 4567154145609410536L;
/**主键*/
	private Integer id;
	/**工单编号*/
	private String workOrderNo;
	/**工单项*/
	private Integer spendingItemId;
	/**spending名称*/
	private String spendingName;
	/**spending*/
	private String spending;
	
	
	

	public SpendingInfo() {
		super();
	}

	public SpendingInfo(String workOrderNo, Integer spendingItemId,
			String spendingName, String spending) {
		super();
		this.workOrderNo = workOrderNo;
		this.spendingItemId = spendingItemId;
		this.spendingName = spendingName;
		this.spending = spending;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getWorkOrderNo() {
		return workOrderNo;
	}

	public void setWorkOrderNo(String workOrderNo) {
		this.workOrderNo = workOrderNo;
	}

	public Integer getSpendingItemId() {
		return spendingItemId;
	}

	public void setSpendingItemId(Integer spendingItemId) {
		this.spendingItemId = spendingItemId;
	}

	public String getSpendingName() {
		return spendingName;
	}

	public void setSpendingName(String spendingName) {
		this.spendingName = spendingName;
	}

	public String getSpending() {
		return spending;
	}

	public void setSpending(String spending) {
		this.spending = spending;
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
		SpendingInfo other = (SpendingInfo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
	
	

}
