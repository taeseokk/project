package com.example.project.advice.exception;

public class WrongPasswordException extends RuntimeException{
	
/**
	 * 
	 */
	private static final long serialVersionUID = 1788504691869077339L;

public WrongPasswordException(String msg, Throwable t) {
		
		super(msg, t);
	}
	
	public WrongPasswordException(String msg) {
		
		super(msg);
	}
	
	public WrongPasswordException() {
		
		super();
	}

}
