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
	BOOK_NOT_FOUND(HttpStatus.NOT_FOUND, "2100", "도서 정보를 찾을수 없습니다."),
	//review 에러코드

	// 3000 : review 에러코드
	REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "3001", "해당 리뷰를 찾을 수 없습니다"),
	USER_NOT_EQUAL(HttpStatus.BAD_REQUEST, "3002", "해당 리뷰를 작성한 유저가 아닙니다");

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
