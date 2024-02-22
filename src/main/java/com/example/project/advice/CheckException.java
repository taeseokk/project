package com.example.project.advice;

public class CheckException extends BaseException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5699504859280527691L;
	private BaseExceptionType exceptionType;

	public CheckException(BaseExceptionType exceptionType) {
		this.exceptionType = exceptionType;
	}

	@Override
	public BaseExceptionType getExceptionType() {
		return exceptionType;
	}

}
