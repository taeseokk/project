package com.example.project.advice.exception;

public class IdExistException extends RuntimeException{
	
/**
	 * 
	 */
	private static final long serialVersionUID = -4465268880242365663L;

public IdExistException(String msg, Throwable t) {
		
		super(msg, t);
	}
	
	public IdExistException(String msg) {
		
		super(msg);
	}
	
	public IdExistException() {
		
		super();
	}

}
