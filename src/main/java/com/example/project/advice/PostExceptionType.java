package com.example.project.advice;

import org.springframework.http.HttpStatus;

public enum PostExceptionType implements BaseExceptionType {

    POST_NOT_FOUND(700, HttpStatus.NOT_FOUND, "찾는 포스트 x"),
    NOT_AUTHORITY_UPDATE_POST(701, HttpStatus.FORBIDDEN, "업데이트 권한 x"),
    NOT_AUTHORITY_DELETE_POST(702, HttpStatus.FORBIDDEN, "삭제 권한 x");


    private int errorCode;
    private HttpStatus httpStatus;
    private String errorMessage;

    PostExceptionType(int errorCode, HttpStatus httpStatus, String errorMessage) {
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
