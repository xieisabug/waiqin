package hn.join.fieldwork.web.command;

import org.apache.commons.lang3.StringUtils;

import hn.join.fieldwork.service.RequiredMoreInfoException;

public class ApplySubstitutionCommand {
	//申请改派的外勤人员ID
	private String applyWorkerNo;
	 //工单号
	private String workOrderNo;
	////改派原因
	private String substituteReason;
	//客户要求
	private String requirement;
	
	private String recommendWorkerName;
	
	//推荐工程师
	private String recommendWorkerNo;
	//申请时间
	private String applyTime;
	//是否同意该派
	private boolean agree;
	//不同意改派理由
	private String rejectReason;
	
	public String getApplyWorkerNo() {
		return applyWorkerNo;
	}
	public void setApplyWorkerNo(String applyWorkerNo) {
		this.applyWorkerNo = applyWorkerNo;
	}
	public String getWorkOrderNo() {
		return workOrderNo;
	}
	public void setWorkOrderNo(String workOrderNo) {
		this.workOrderNo = workOrderNo;
	}

	public String getRequirement() {
		return requirement;
	}
	public void setRequirement(String requirement) {
		this.requirement = requirement;
	}

	public String getApplyTime() {
		return applyTime;
	}
	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}
	
	
	public String getSubstituteReason() {
		return substituteReason;
	}
	public void setSubstituteReason(String substituteReason) {
		this.substituteReason = substituteReason;
	}
	public String getRecommendWorkerNo() {
		return recommendWorkerNo;
	}
	public void setRecommendWorkerNo(String recommendWorkerNo) {
		this.recommendWorkerNo = recommendWorkerNo;
	}
	public boolean isAgree() {
		return agree;
	}
	public void setAgree(boolean agree) {
		this.agree = agree;
	}
	public String getRejectReason() {
		return rejectReason;
	}
	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}
	
	
	

	public String getRecommendWorkerName() {
		return recommendWorkerName;
	}
	public void setRecommendWorkerName(String recommendWorkerName) {
		this.recommendWorkerName = recommendWorkerName;
	}
	public void check(){
		if(StringUtils.isEmpty(applyWorkerNo))
			throw new RequiredMoreInfoException("applyWorkerNo is null,command:"+this);
		if(StringUtils.isEmpty(workOrderNo))
			throw new RequiredMoreInfoException("workOrderNo is null,command:"+this);
		if(StringUtils.isEmpty(recommendWorkerName))
			throw new RequiredMoreInfoException("recomendWorkerName is null,command:"+this);
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ApplySubstitutionCommand [applyWorkerNo=")
				.append(applyWorkerNo).append(", workOrderNo=")
				.append(workOrderNo).append(", substituteReason=")
				.append(substituteReason).append(", requirement=")
				.append(requirement).append(", recommendWorkerNo=")
				.append(recommendWorkerNo).append(", applyTime=")
				.append(applyTime).append(", agree=").append(agree)
				.append(", rejectReason=").append(rejectReason).append("]");
		return builder.toString();
	}

	
	
	

}
