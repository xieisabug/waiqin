package hn.join.fieldwork.service;


/**
 * 自定义超出预定量异常
 * @author aisino_lzw
 *
 */
public class RequiredMoreInfoException extends RuntimeException{
	
	
	private static final long serialVersionUID = 118514818779256356L;

	public RequiredMoreInfoException(){
		super();
	}
	
	public RequiredMoreInfoException(String message){
		super(message);
	}

	
	public RequiredMoreInfoException(Exception ex){
		super(ex);
	}
	
	public RequiredMoreInfoException(String message,Exception ex){
		super(message,ex);
	}
	

}
