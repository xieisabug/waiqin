package hn.join.fieldwork.web.dto;

import java.util.LinkedList;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "Srv_WorkCard_Order")
public class RemoteSolutionFeedbackDto {

	@Element(name = "head")
	public RemoteSolutionHead head;

	@ElementList(name = "question_body")
	public LinkedList<RemoteSolutionQuestion> questionBody = new LinkedList<RemoteSolutionQuestion>();

	public RemoteSolutionFeedbackDto(RemoteSolutionHead head,
			LinkedList<RemoteSolutionQuestion> questionBody) {
		super();
		this.head = head;
		this.questionBody = questionBody;
	}

	public RemoteSolutionFeedbackDto() {
		super();
	}

	public RemoteSolutionHead getHead() {
		return head;
	}

	public void setHead(RemoteSolutionHead head) {
		this.head = head;
	}

	public LinkedList<RemoteSolutionQuestion> getQuestionBody() {
		return questionBody;
	}

	public void setQuestionBody(LinkedList<RemoteSolutionQuestion> questionBody) {
		this.questionBody = questionBody;
	}

	public void addQuestion(RemoteSolutionQuestion question) {
		this.questionBody.add(question);
	}

	@Root(name = "head")
	public static class RemoteSolutionHead {
		/**
		 * <!-- 派工单ID信息 int型 不能为空-->
		 */
		@Element(name = "workCard_id")
		private String workCardId;
		

		public RemoteSolutionHead() {
			super();
		}

		public RemoteSolutionHead(String workCardId) {
			super();
			this.workCardId = workCardId;
		}

		public String getWorkCardId() {
			return workCardId;
		}

		public void setWorkCardId(String workCardId) {
			this.workCardId = workCardId;
		}


		
		

	}

	@Root(name = "problem")
	public static class RemoteSolutionQuestion {
		
		

		/**
		 * 问题ID int 不能为空-
		 */
		@Element(name = "question_id")
		private Long questionId;
		
		@Element(name = "productId")
		private String productId;

		/**
		 * 问题类型ID int 不能为空
		 */
		@Element(name = "question_typ_id")
		private Integer questionTypId;

		/**
		 * 问题类别ID int 不能为空
		 */
		@Element(name = "question_typ_p_id")
		private Integer questionTypPId;

		/**
		 * 问题描述 varchar(1000) 不能为空
		 */
		@Element(name = "question_desc")
		private String questionDesc;

		/**
		 * 解决方法 varchar(1000) 不能为空
		 */
		@Element(name = "solution_method")
		private String solutionMethod;

		
		
		
		public RemoteSolutionQuestion(Long questionId,String productId,
				Integer questionTypId, Integer questionTypPId,
				String questionDesc, String solutionMethod) {
			super();
			this.questionId = questionId;
			this.productId = productId;
			this.questionTypId = questionTypId;
			this.questionTypPId = questionTypPId;
			this.questionDesc = questionDesc;
			this.solutionMethod = solutionMethod;
		}
		
		

		public RemoteSolutionQuestion() {
			super();
		}



		public Long getQuestionId() {
			return questionId;
		}

		public void setQuestionId(Long questionId) {
			this.questionId = questionId;
		}
		

		public String getProductId() {
			return productId;
		}



		public void setProductId(String productId) {
			this.productId = productId;
		}



		public Integer getQuestionTypId() {
			return questionTypId;
		}

		public void setQuestionTypId(Integer questionTypId) {
			this.questionTypId = questionTypId;
		}

		public Integer getQuestionTypPId() {
			return questionTypPId;
		}

		public void setQuestionTypPId(Integer questionTypPId) {
			this.questionTypPId = questionTypPId;
		}

		public String getQuestionDesc() {
			return questionDesc;
		}

		public void setQuestionDesc(String questionDesc) {
			this.questionDesc = questionDesc;
		}

		public String getSolutionMethod() {
			return solutionMethod;
		}

		public void setSolutionMethod(String solutionMethod) {
			this.solutionMethod = solutionMethod;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((questionId == null) ? 0 : questionId.hashCode());
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
			RemoteSolutionQuestion other = (RemoteSolutionQuestion) obj;
			if (questionId == null) {
				if (other.questionId != null)
					return false;
			} else if (!questionId.equals(other.questionId))
				return false;
			return true;
		}

	}

	
}
