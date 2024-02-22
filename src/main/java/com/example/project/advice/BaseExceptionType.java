package com.example.project.advice;

import org.springframework.http.HttpStatus;

public interface BaseExceptionType {

	int getErrorCode();
	
	HttpStatus getHttpStatus();
	
	String getErrorMessage();
}
