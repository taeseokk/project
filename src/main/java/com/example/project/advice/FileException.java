package com.example.project.advice;

public class FileException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7311182882335715027L;

	private BaseExceptionType exceptionType;

	public FileException(BaseExceptionType exceptionType) {
		this.exceptionType = exceptionType;
	}

	@Override
	public BaseExceptionType getExceptionType() {
		return exceptionType;
	}

}
