package hn.join.fieldwork.domain;
/**
 * 签到日期对象
 * @author chenjinlong
 *
 */
public class CheckinDay extends BaseDomain {
	
	public static final CheckinDay NO_CHECKIN_DAY=new CheckinDay();
	

	private static final long serialVersionUID = 4715951694644008177L;
	/**主键*/
	public Integer id;
	/**日期*/
	private String date;
	/**时间*/
	private String time;
	
	public CheckinDay(){
		
	}

	public CheckinDay(String date, String time) {
		super();
		this.date = date;
		this.time = time;
	}
	
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
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
		CheckinDay other = (CheckinDay) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		return true;
	}
	
	
	
	
	

}
