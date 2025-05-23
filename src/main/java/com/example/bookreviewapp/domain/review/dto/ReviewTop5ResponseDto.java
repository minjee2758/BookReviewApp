package com.example.bookreviewapp.domain.review.dto;

import java.time.LocalDateTime;

import com.example.bookreviewapp.domain.review.entity.Review;

import lombok.Getter;

@Getter
public class ReviewTop5ResponseDto {
	private final Integer rank;
	private final Long reviewId;
	private final String content;
	private final LocalDateTime createdAt;
	private final LocalDateTime updatedAt;

	public ReviewTop5ResponseDto(Integer rank, Long reviewId, String content, LocalDateTime createdAt,
		LocalDateTime updatedAt) {
		this.rank = rank;
		this.reviewId = reviewId;
		this.content = content;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public static ReviewTop5ResponseDto from(Review review, Integer rank) {
		return new ReviewTop5ResponseDto(
			rank,
			review.getId(),
			review.getContent(),
			review.getCreatedAt(),
			review.getUpdatedAt()
		);
	}
}
