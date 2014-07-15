package hn.join.fieldwork.domain;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;
/**
 * 数据域基类
 * 包含visible与invisible两个属性的visibleStatus枚举变量
 * 以及判断对象是否相同 的equals,hashcode两个方法 
 * @author chenjinlong
 *
 */
public abstract class BaseDomain implements Serializable {
	
	public enum VisibleStatus{
		visible,invisible;
	}

	public abstract boolean equals(Object obj);

	public abstract int hashCode();
	
	
	@JsonIgnore 
	protected VisibleStatus visibleStatus;

	public VisibleStatus getVisibleStatus() {
		return visibleStatus;
	}

	public void setVisibleStatus(VisibleStatus visibleStatus) {
		this.visibleStatus = visibleStatus;
	}

	




}
