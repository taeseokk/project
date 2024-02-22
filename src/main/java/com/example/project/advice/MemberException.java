package com.example.project.advice;

public class MemberException extends BaseException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 964632957656766204L;
	
	private BaseExceptionType exceptionType;
	
	public MemberException(BaseExceptionType exceptionType) {
		this.exceptionType = exceptionType;
	}
	
	@Override
	public BaseExceptionType getExceptionType() {
		return exceptionType;
	}

}
