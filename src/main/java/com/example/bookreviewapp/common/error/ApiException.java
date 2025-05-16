package com.example.bookreviewapp.common.error;

import org.springframework.http.HttpStatus;

import com.example.bookreviewapp.common.code.BaseErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApiException extends RuntimeException {

	private final BaseErrorCode errorCode;

	@Override
	public String getMessage() {
		return errorCode.getReasonHttpStatus().getMessage();
	}

	public String getCode() {
		return errorCode.getReasonHttpStatus().getCode();
	}

	public HttpStatus getHttpStatus() {
		return errorCode.getReasonHttpStatus().getHttpStatus();
	}
}
