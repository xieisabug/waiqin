package hn.join.fieldwork.service;


/**
 * 自定义密码不匹配异常
 * @author aisino_lzw
 *
 */
public class PasswordNotMatchException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3272562034975712323L;

	public PasswordNotMatchException(Long userId){
		super("userId:"+userId+" do not match original password");
	}

}
