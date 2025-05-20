package com.example.bookreviewapp.domain.review.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ReviewResquestDto {

	@NotBlank(message = "리뷰 내용은 반드시 작성되어야합니다.")
	private final String content;

	@NotNull(message = "평점 입력은 필수입니다.")
	@Min(value = 0, message = "점수는 0점~5점 사이로 입력 가능합니다")
	@Max(value = 5, message = "점수는 0점~5점 사이로 입력 가능합니다")
	private final Integer score;

	@JsonCreator
	public ReviewResquestDto(
		@JsonProperty("content") String content,
		@JsonProperty("score") Integer score) {
		this.content = content;
		this.score = score;
	}
}
