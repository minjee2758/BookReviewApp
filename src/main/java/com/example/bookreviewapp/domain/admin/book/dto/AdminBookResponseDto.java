package com.example.bookreviewapp.domain.admin.book.dto;

import com.example.bookreviewapp.domain.book.entity.EnrollStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AdminBookResponseDto {
	private final Long bookId;
	private final EnrollStatus enrollStatus;
}