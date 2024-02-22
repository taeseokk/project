package com.example.project.advice;

import java.net.BindException;

import com.example.project.advice.exception.IdExistException;
import com.example.project.advice.exception.NotFoundMemberException;
import com.example.project.advice.exception.WrongPasswordException;
import com.example.project.entity.model.CommonResult;
import com.example.project.service.ResponseService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {

	private final ResponseService responseService;
	private final MessageSource messageSource;

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	protected CommonResult defaultException(HttpServletRequest request, Exception e) {

		// 알 수 없는 오류
		return responseService.getFailResult(Integer.valueOf(getMessage("unKnown.code")), getMessage("unKnown.msg"));
	}

	@ExceptionHandler(IdExistException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	protected CommonResult userNotFoundException(HttpServletRequest request, IdExistException e) {
		// 존재 ID
		return responseService.getFailResult(500, "Exist id");
	}

	@ExceptionHandler(NotFoundMemberException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	protected CommonResult userNotFoundException(HttpServletRequest request, NotFoundMemberException e) {
		// member정보 x
		return responseService.getFailResult(500, "Not Found member");

	}

	@ExceptionHandler(WrongPasswordException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	protected CommonResult userNotFoundException(HttpServletRequest request, WrongPasswordException e) {

		// 잘못된 비밀번호
		return responseService.getFailResult(500, "WrongPassword");

	}

	@ExceptionHandler(BaseException.class)
	public ResponseEntity<ExceptionDto> handleBaseEx(BaseException exception) {
		log.error("BaseException errorMessage : {}", exception.getExceptionType().getErrorMessage());
		log.error("BaseException errorCode : {}", exception.getExceptionType().getErrorCode());

		return new ResponseEntity<ExceptionDto>(new ExceptionDto(exception.getExceptionType().getErrorCode()),
				exception.getExceptionType().getHttpStatus());
	}


	@ExceptionHandler(BindException.class)
	public ResponseEntity<ExceptionDto> handleValidEx(BindException exception) {
		log.error("@ValidException : {}", exception.getMessage());
		return new ResponseEntity<ExceptionDto>(new ExceptionDto(2000), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class) // json 파싱 오류
	public ResponseEntity<?> httpMessageNotReadableExc(HttpMessageNotReadableException exception) {
		log.error("Json 파싱 예외", exception.getMessage());
		return new ResponseEntity<ExceptionDto>(new ExceptionDto(3000), HttpStatus.BAD_REQUEST);
	}

	// code 정보에 해당하는 메시지를 조회
	private String getMessage(String code) {

		return getMessage(code, null);
	}

	// code 정보, 추가 argument로 현재 locale에 맞는 메시지를 조회
	private String getMessage(String code, Object[] args) {

		return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
	}

	@Data
	@AllArgsConstructor
	static class ExceptionDto {
		private Integer errorCode;
	}
}

//@ExceptionHandler(참고.class)
//@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//protected CommonResult userNotFoundException(HttpServletRequest request, CUserNotFoundException e) {
//	
//	// 존재 하지 않는 회원
//	return responseService.getFailResult(Integer.valueOf(getMessage("userNotFound.code")), getMessage("userNotFound.msg"));
//}