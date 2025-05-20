package com.example.bookreviewapp.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewResponseDto {
	private String title;
	private String userName;
	private String content;
	private Integer score;
}
