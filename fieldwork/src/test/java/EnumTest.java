import hn.join.fieldwork.utils.JsonUtil;

import java.util.Map;

import com.google.common.collect.Maps;

public class EnumTest {
	enum Week {
		// 定义枚举类的值
		Monday("MON", "星期一"), Tuesday("TUE", "星期二"), Wednesday("WED", "星期三"), Thursday(
				"THU", "星期四"), Friday("FRI", "星期五"), Saturday("SAT", "星期六") {
			@Override
			public boolean isRest() {
				return true;
			}
		},
		Sunday("SUN", "星期日") {
			@Override
			public boolean isRest() {
				return true;
			}
		};
		private String abbreviation = "";// 缩写
		private String chineseName = "";// 中文名字

		// 定义自己的构造器
		private Week(String abbreviation, String chineseName) {
			this.abbreviation = abbreviation;
			this.chineseName = chineseName;
		}

		public String abbreviation() {
			return abbreviation;
		}

		public String getChineseName() {
			return chineseName;
		}

		// 周六和周日应该返回true，此方法在周六和周日的值中被重载
		public boolean isRest() {
			return false;
		}

		// 重载，对它进行稍稍的改动
		@Override
		public String toString() {
			return this.getClass().getName() + "." + this.name();
		}
	}

	public static void main(String[] args) {
		for (Week week : Week.values()) {
			System.out.println("-----------------------------------------");
			System.out.println("ordinal():" + week.ordinal());
			System.out.println("name():" + week.name());
			System.out.println("getChineseName():" + week.getChineseName());
			System.out.println("abbreviation():" + week.abbreviation());
			System.out.println("isRest():" + week.isRest());
			System.out.println("toString():" + week);
		}
		
		System.out.println(Week.valueOf("Sunday"));
		Map<String,Object> results=Maps.newHashMap();
		results.put("workerNo", "12121abc");
		results.put("success", true);
		System.out.println(JsonUtil.toJson(results));
		boolean success=true;
		String workOrderNo="sdrfd1121";
		String message="{\"success\":"+success+",\"workerNo\":\""+workOrderNo+"\"}";
		System.out.println(message);
		
	}
}
