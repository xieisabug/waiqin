package hn.join.fieldwork.service;

import hn.join.fieldwork.domain.Sequence;
import hn.join.fieldwork.persistence.SequenceMapper;

import javax.annotation.PostConstruct;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 标记工单服务类
 * @author aisino_lzw
 *
 */
@Service
public class OrderTokenService {
	
	private static final String visitSeq="visit-";
	
	private static final String repairSeq="repair-";

	private final String messageFormat = "%s%s%05d";

	
	private String currentMonth;

	@Autowired
	private SequenceMapper sequenceMapper;

	@Autowired
	private SystemParameterService systemParameterService;

	/**
	 * 数据初始化
	 */
	@PostConstruct
	public void init() {
		currentMonth = LocalDate.now().toString("yyyyMM");
	}
	
	/**
	 * 改变序列标志
	 * @param todayAtMonth
	 */
	public void changeTokenSeq(String todayAtMonth){
		if(!todayAtMonth.equals(currentMonth)){
			currentMonth=todayAtMonth;
			Sequence newVisitSeq=new Sequence(visitSeq+todayAtMonth,1,1);
			sequenceMapper.insertSequence(newVisitSeq);
			Sequence newRepairSeq=new Sequence(repairSeq+todayAtMonth,1,1);
			sequenceMapper.insertSequence(newRepairSeq);
		}
		
	}
	

	/**
	 * 获取权限标志
	 * @return
	 */
	public String getVisitToken() {
		String seqName = visitSeq+ currentMonth;
		int tokenValue = sequenceMapper.getNextvalBySeq(seqName);
		return String.format(messageFormat, systemParameterService
				.getValueByName(SystemParameterService.PARAM_NAME_VISIT_TOKEN_PREFIX),currentMonth, tokenValue);
	}

	/**
	 * 修改标志
	 * @return
	 */
	public String getRepairToken() {
		String seqName = repairSeq+ currentMonth;
		int tokenValue = sequenceMapper.getNextvalBySeq(seqName);
		return String.format(messageFormat, systemParameterService
				.getValueByName(SystemParameterService.PARAM_NAME_REPAIR_TOKEN_PREFIX),currentMonth, tokenValue);
	}
	
	
	

	public static void main(String[] args) {
		LocalDate localDate = LocalDate.now();
		System.out.println(localDate.getYear());
		System.out.println(localDate.getMonthOfYear());
		System.out.println(String.format("%s%d%02d%05d", "df",
				localDate.getYear(), localDate.getMonthOfYear(), 1));
	}

}
