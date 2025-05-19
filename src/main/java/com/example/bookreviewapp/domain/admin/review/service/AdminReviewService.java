package com.example.bookreviewapp.domain.admin.review.service;

import com.example.bookreviewapp.domain.admin.review.dto.AdminReviewResponseDto;

public interface AdminReviewService {
	AdminReviewResponseDto pin(Long reviewId);
	AdminReviewResponseDto unpin(Long reviewId);
}
