package com.example.project.advice.exception;

public class NotFoundMemberException extends RuntimeException{
	
/**
	 * 
	 */
	private static final long serialVersionUID = -9052518465086240791L;

public NotFoundMemberException(String msg, Throwable t) {
		
		super(msg, t);
	}
	
	public NotFoundMemberException(String msg) {
		
		super(msg);
	}
	
	public NotFoundMemberException() {
		
		super();
	}

}
