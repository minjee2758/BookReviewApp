package com.example.bookreviewapp.domain.review.dto;

import java.time.LocalDateTime;

import com.example.bookreviewapp.domain.review.entity.Review;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewResponseDto {
	private String title;
	private String email;
	private String content;
	private Integer score;
	private Long viewer;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public ReviewResponseDto(Review review) {
		this.title = review.getBook().getTitle();
		this.email = review.getUser().getEmail();
		this.content = review.getContent();
		this.score = review.getScore();
		this.viewer = review.getViewer();
		this.createdAt = review.getCreatedAt();
		this.updatedAt = review.getUpdatedAt();
	}
}
