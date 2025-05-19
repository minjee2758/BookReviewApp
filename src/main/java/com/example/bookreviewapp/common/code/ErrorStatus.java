package com.example.bookreviewapp.common.code;

import org.springframework.http.HttpStatus;

import com.example.bookreviewapp.common.dto.ErrorReasonDto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorStatus implements BaseErrorCode {
	//user 에러 코드
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "3001", "고객 정보가 없습니다."),

	//book 에러 코드

	//review 에러코드

	;

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;

	public ErrorReasonDto getReason() {
		return ErrorReasonDto.builder()
			.isSuccess(false)
			.code(code)
			.message(message)
			.build();
	}

	public ErrorReasonDto getReasonHttpStatus() {
		return ErrorReasonDto.builder()
			.isSuccess(false)
			.httpStatus(httpStatus)
			.code(code)
			.message(message)
			.build();
	}

}
