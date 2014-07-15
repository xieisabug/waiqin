package hn.join.fieldwork.service;


/**
 * 自定义外勤员工无效状态异常
 * @author aisino_lzw
 *
 */
public class IllegalFieldWorkerStateException extends RuntimeException {

	private static final long serialVersionUID = 1127049302431632835L;

	public IllegalFieldWorkerStateException(Long fieldWorkerId,
			String requestType) {
		super("fieldWorkerId:" + fieldWorkerId + ",requestType:"
				+ requestType);
	}
	
	public IllegalFieldWorkerStateException(Long fieldWorkerId,String requestType,String message){
		super("fieldWorkerId:" + fieldWorkerId + ",requestType:"
				+ requestType+",message:"+message);
	}

}
