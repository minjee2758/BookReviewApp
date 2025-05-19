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

	//review 에러코드

	//jwt 에러코드
	TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "3101", "토큰이 없습니다."),
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "3102", "유효하지 않은 토큰입니다."),
	TOKEN_BLACKLISTED(HttpStatus.UNAUTHORIZED, "3103", "블랙리스트에 등록된 토큰입니다.")
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
