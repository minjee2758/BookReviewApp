package com.example.bookreviewapp.domain.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewResquestDto {

	@NotBlank(message = "리뷰 내용은 반드시 작성되어야합니다.")
	private final String content;

	@NotNull(message = "평점 입력은 필수입니다.")
	@Min(value = 0, message = "점수는 0점~5점 사이로 입력 가능합니다")
	@Max(value = 5, message = "점수는 0점~5점 사이로 입력 가능합니다")
	private final Integer score;
}
