package com.example.project.advice;

import org.springframework.http.HttpStatus;

public enum CheckExceptionType implements BaseExceptionType {

	REQUIRED_PARAMETER(400, HttpStatus.BAD_REQUEST, "파라미터 누락"),
	FORBIDDEN(403,HttpStatus.FORBIDDEN,"인가 x"),
	UNAUTHORIZED(401,HttpStatus.UNAUTHORIZED,"인증 x 로그인 실패");
	
	private int errorCode;
	private HttpStatus httpStatus;
	private String errorMessage;

	CheckExceptionType(int errorCode, HttpStatus httpStatus, String errorMessage) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

	@Override
	public int getErrorCode() {
		return this.errorCode;
	}

	@Override
	public HttpStatus getHttpStatus() {
		return this.httpStatus;
	}

	@Override
	public String getErrorMessage() {
		return this.errorMessage;
	}

}
