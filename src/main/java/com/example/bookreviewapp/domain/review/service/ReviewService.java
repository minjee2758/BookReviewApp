package com.example.bookreviewapp.domain.review.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.bookreviewapp.common.security.CustomUserDetails;
import com.example.bookreviewapp.domain.review.dto.ReviewResponseDto;
import com.example.bookreviewapp.domain.review.dto.ReviewResquestDto;

import jakarta.validation.Valid;

@Service
public interface ReviewService {
	void postReview(CustomUserDetails userDetails, Long bookId, ReviewResquestDto dto);

	void updateReview(CustomUserDetails userDetails, Long bookId, Long reviewId, @Valid ReviewResquestDto dto);

	List<ReviewResponseDto> getReviews(Long bookId);

	void deleteReview(CustomUserDetails userDetails, Long bookId, Long reviewId);
}
