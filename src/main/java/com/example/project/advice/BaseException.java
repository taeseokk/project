package com.example.project.advice;

public abstract class BaseException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1620214397387518812L;

	public abstract BaseExceptionType getExceptionType();

}
