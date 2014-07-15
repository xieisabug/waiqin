package hn.join.fieldwork.domain;
/**
 * 序列对象
 * @author chenjinlong
 *
 */
public class Sequence extends BaseDomain{
	/**序列名称*/
	private String seqName;
	/**当前值*/
	private Integer currentValue;
	/**增量*/
	private Integer increment;
	
	

	public Sequence(String seqName, Integer currentValue, Integer increment) {
		super();
		this.seqName = seqName;
		this.currentValue = currentValue;
		this.increment = increment;
	}
	
	

	public Sequence() {
		super();
	}



	public String getSeqName() {
		return seqName;
	}

	public void setSeqName(String seqName) {
		this.seqName = seqName;
	}

	public Integer getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(Integer currentValue) {
		this.currentValue = currentValue;
	}

	public Integer getIncrement() {
		return increment;
	}

	public void setIncrement(Integer increment) {
		this.increment = increment;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((seqName == null) ? 0 : seqName.hashCode());
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
		Sequence other = (Sequence) obj;
		if (seqName == null) {
			if (other.seqName != null)
				return false;
		} else if (!seqName.equals(other.seqName))
			return false;
		return true;
	}
	
	

}
