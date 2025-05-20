package com.example.bookreviewapp.domain.admin.book.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bookreviewapp.common.code.SuccessStatus;
import com.example.bookreviewapp.common.response.ApiResponse;
import com.example.bookreviewapp.domain.admin.review.service.AdminReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/reviews")
@RequiredArgsConstructor
public class AdminReviewController {

	private final AdminReviewService adminReviewService;

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/{reviewId}/pin")
	public ResponseEntity<ApiResponse<Void>> pin(@PathVariable Long reviewId) {
		adminReviewService.pin(reviewId);
		return ApiResponse.onSuccess(SuccessStatus.REVIEW_PIN_SUCCESS);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/{reviewId}/unpin")
	public ResponseEntity<ApiResponse<Void>> unpin(@PathVariable Long reviewId) {
		adminReviewService.unpin(reviewId);
		return ApiResponse.onSuccess(SuccessStatus.REVIEW_UNPIN_SUCCESS);
	}

}