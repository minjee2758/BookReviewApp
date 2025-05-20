package com.example.bookreviewapp.domain.admin.review.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bookreviewapp.common.code.ErrorStatus;
import com.example.bookreviewapp.common.error.ApiException;;
import com.example.bookreviewapp.domain.admin.review.dto.AdminReviewResponseDto;
import com.example.bookreviewapp.domain.admin.review.repository.AdminReviewRepository;
import com.example.bookreviewapp.domain.review.entity.PinStatus;
import com.example.bookreviewapp.domain.review.entity.Review;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminReviewService {
	private final AdminReviewRepository adminReviewRepository;

	public AdminReviewResponseDto pin(Long reviewId) {
		Review review = adminReviewRepository.findById(reviewId)
			.orElseThrow(() -> new ApiException(ErrorStatus.REVIEW_NOT_FOUND));
		if (review.getIsPinned() == PinStatus.PINNED) {
			throw new ApiException(ErrorStatus.REVIEW_ALREADY_PINNED);
		}
		review.pinned();
		return new AdminReviewResponseDto(review.getId(), review.getIsPinned());
	}
	public AdminReviewResponseDto unpin(Long reviewId) {
		Review review = adminReviewRepository.findById(reviewId)
			.orElseThrow(() -> new ApiException(ErrorStatus.REVIEW_NOT_FOUND));
		if (review.getIsPinned() == PinStatus.UNPINNED) {
			throw new ApiException(ErrorStatus.REVIEW_ALREADY_UNPINNED);
		}
		review.unpinned();
		return new AdminReviewResponseDto(review.getId(), review.getIsPinned());
	}
}
