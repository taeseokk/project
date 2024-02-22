package com.example.project.advice;

public class PostException extends BaseException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8839824901906408012L;
	private BaseExceptionType exceptionType;

	public PostException(BaseExceptionType exceptionType) {
		this.exceptionType = exceptionType;
	}
	
	@Override
	public BaseExceptionType getExceptionType() {
		// TODO Auto-generated method stub
		return exceptionType;
	}

}
