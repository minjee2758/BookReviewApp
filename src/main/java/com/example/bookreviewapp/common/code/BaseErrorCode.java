package com.example.bookreviewapp.common.code;

import com.example.bookreviewapp.common.dto.ErrorReasonDto;

public interface BaseErrorCode {
	ErrorReasonDto getReason();

	ErrorReasonDto getReasonHttpStatus();
}
