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

	//admin 에러코드
	BOOK_ADD_REQUEST_ALREADY_ACCEPT(HttpStatus.BAD_REQUEST,"4001","이미 승인된 요청입니다."),
	REVIEW_ALREADY_PINNED(HttpStatus.BAD_REQUEST,"4002","이미 고정된 리뷰입니다."),
	REVIEW_ALREADY_UNPINNED(HttpStatus.BAD_REQUEST,"4003","리뷰가 고정되어 있지 않습니다.")
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
