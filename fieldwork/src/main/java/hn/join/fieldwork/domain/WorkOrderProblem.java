package hn.join.fieldwork.domain;

public class WorkOrderProblem {
	/**主键*/
	private Integer id;
	/**工单编号*/
	private String workOrderNo;

	private Long sId;
	/**问题类型ID*/
	private Integer problemTypeId;
	/**问题类别ID*/
	private Integer problemCategoryId;
	/**问题详情*/
	private String problemDetail;
	/**解决方案*/
	private String solutionMethod;
	/**问题类型名称*/
	private String problemTypeName;
	/**问题类别名称*/
	private String problemCategoryName;
	/**产品ID*/
	private String productId;
	/**产品名称*/
	private String productName;

	public WorkOrderProblem(String workOrderNo, Long sId,
			Integer problemTypeId, Integer problemCategoryId,
			String problemDetail, String solutionMethod,String productId,String productName) {
		super();
		this.workOrderNo = workOrderNo;
		this.sId = sId;
		this.problemTypeId = problemTypeId;
		this.problemCategoryId = problemCategoryId;
		this.problemDetail = problemDetail;
		this.solutionMethod = solutionMethod;
		this.productId = productId;
		this.productName = productName;
	}
	
	public WorkOrderProblem(String workOrderNo,String productId, Long sId,
			Integer problemTypeId, Integer problemCategoryId,
			String problemDetail, String solutionMethod) {
		super();
		this.workOrderNo = workOrderNo;
		this.productId = productId;
		this.sId = sId;
		this.problemTypeId = problemTypeId;
		this.problemCategoryId = problemCategoryId;
		this.problemDetail = problemDetail;
		this.solutionMethod = solutionMethod;
	}

	public WorkOrderProblem() {
		super();
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

	public Long getsId() {
		return sId;
	}

	public void setsId(Long sId) {
		this.sId = sId;
	}

	public Integer getProblemTypeId() {
		return problemTypeId;
	}

	public void setProblemTypeId(Integer problemTypeId) {
		this.problemTypeId = problemTypeId;
	}

	public Integer getProblemCategoryId() {
		return problemCategoryId;
	}

	public void setProblemCategoryId(Integer problemCategoryId) {
		this.problemCategoryId = problemCategoryId;
	}

	public String getProblemDetail() {
		return problemDetail;
	}

	public void setProblemDetail(String problemDetail) {
		this.problemDetail = problemDetail;
	}

	public String getSolutionMethod() {
		return solutionMethod;
	}

	public void setSolutionMethod(String solutionMethod) {
		this.solutionMethod = solutionMethod;
	}

	public String getProblemTypeName() {
		return problemTypeName;
	}

	public void setProblemTypeName(String problemTypeName) {
		this.problemTypeName = problemTypeName;
	}

	public String getProblemCategoryName() {
		return problemCategoryName;
	}

	public void setProblemCategoryName(String problemCategoryName) {
		this.problemCategoryName = problemCategoryName;
	}
	
	

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	
	
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sId == null) ? 0 : sId.hashCode());
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
		WorkOrderProblem other = (WorkOrderProblem) obj;
		if (sId == null) {
			if (other.sId != null)
				return false;
		} else if (!sId.equals(other.sId))
			return false;
		return true;
	}

}
