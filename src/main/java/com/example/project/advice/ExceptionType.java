package com.example.project.advice;

import org.springframework.http.HttpStatus;

public enum ExceptionType implements BaseExceptionType{
	
	ALREADY(400,HttpStatus.CONFLICT,"존재 아이디"),
	WRONG_PASSWORD(400,HttpStatus.BAD_REQUEST,"잘못된 비밀번호"),
	NOT_FOUND_MEMBER(404,HttpStatus.NOT_FOUND,"회원정보x");

	private int errorCode;
	private HttpStatus httpStatus;
	private String errorMessage;
	
	ExceptionType(int errorCode, HttpStatus httpStatus, String errorMessage) {
		this.errorCode = errorCode;
		this.httpStatus = httpStatus;
		this.errorMessage = errorMessage;
	}
	
	@Override
	public int getErrorCode() {

		return this.getErrorCode();
	}

	@Override
	public HttpStatus getHttpStatus() {

		return this.getHttpStatus();
	}

	@Override
	public String getErrorMessage() {

		return this.getErrorMessage();
	}

}
