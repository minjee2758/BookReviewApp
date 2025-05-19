package com.example.bookreviewapp.common.code;

import org.springframework.http.HttpStatus;

import com.example.bookreviewapp.common.dto.ReasonDto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SuccessStatus implements BaseCode {
	// 1000: 성공 코드
	SIGNUP_SUCCESS(HttpStatus.CREATED, "1001", "회원가입이 완료되었습니다."),

	// 2000: 도서 성공 코드
	FIND_BOOK(HttpStatus.OK, "2000", "도서 조회 성공."),
	CREATE_BOOK(HttpStatus.CREATED, "2001", "도서 생성 성공."),
	UPDATE_BOOK(HttpStatus.OK, "2002", "도서 수정 성공."),
	DELETE_BOOK(HttpStatus.OK, "2003", "도서 삭제 성공.")
	;

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;

	public ReasonDto getReason() {
		return ReasonDto.builder()
			.isSuccess(true)
			.code(code)
			.message(message)
			.build();
	}

	public ReasonDto getReasonHttpStatus() {
		return ReasonDto.builder()
			.isSuccess(true)
			.httpStatus(httpStatus)
			.code(code)
			.message(message)
			.build();
	}
}
