package com.example.bookreviewapp.domain.admin.review.dto;

import com.example.bookreviewapp.domain.review.entity.PinStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AdminReviewResponseDto {
	private final Long reviewId;
	private final PinStatus pinStatus;
}
