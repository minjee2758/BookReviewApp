package com.example.bookreviewapp.domain.review.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bookreviewapp.common.code.SuccessStatus;
import com.example.bookreviewapp.common.response.ApiResponse;
import com.example.bookreviewapp.common.security.CustomUserDetails;
import com.example.bookreviewapp.domain.review.dto.ReviewResponseDto;
import com.example.bookreviewapp.domain.review.dto.ReviewResquestDto;
import com.example.bookreviewapp.domain.review.dto.ReviewTop5ResponseDto;
import com.example.bookreviewapp.domain.review.service.ReviewService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/books")
public class ReviewController {

	private final ReviewService reviewService;

	//리뷰 등록
	@PostMapping("/{bookId}/review")
	public ResponseEntity<ApiResponse<String>> postReview(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable Long bookId,
		@Valid @RequestBody ReviewResquestDto dto
	) {
		reviewService.postReview(userDetails, bookId, dto);

		return ApiResponse.onSuccess(SuccessStatus.CREATE_REVIEW);
	}

	//리뷰 수정
	@PutMapping("/{bookId}/review/{reviewId}")
	public ResponseEntity<ApiResponse<String>> putReview(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable Long bookId,
		@PathVariable Long reviewId,
		@Valid @RequestBody ReviewResquestDto dto
	) {
		reviewService.updateReview(userDetails, bookId, reviewId, dto);
		return ApiResponse.onSuccess(SuccessStatus.MODIFY_REVIEW_SUCCESS);
	}

	//책에 해당하는 모든 리뷰 조회
	@GetMapping("/{bookId}/review")
	public ResponseEntity<ApiResponse<Page<ReviewResponseDto>>> getReviews(
		@PathVariable Long bookId,
		@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		Page<ReviewResponseDto> dto = reviewService.getReviews(bookId, pageable);
		return ApiResponse.onSuccess(SuccessStatus.GET_REVIEWS_SUCCESS, dto);
	}

	@GetMapping("/{bookId}/review/{reviewId}")
	public ResponseEntity<ApiResponse<ReviewResponseDto>> getReviewDetail(
		@PathVariable Long bookId,
		@PathVariable Long reviewId
	) {
		ReviewResponseDto dto = reviewService.getReviewDetail(bookId, reviewId);
		return ApiResponse.onSuccess(SuccessStatus.GET_REVIEW_SUCCESS, dto);
	}

	//리뷰 삭제
	@DeleteMapping("/{bookId}/review/{reviewId}")
	public ResponseEntity<ApiResponse<String>> deleteReview(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable Long bookId,
		@PathVariable Long reviewId
	) {
		reviewService.deleteReview(userDetails, bookId, reviewId);
		return ApiResponse.onSuccess(SuccessStatus.REVIEW_DELETE_SUCCESS);
	}

	@GetMapping("/review/popular")
	public ResponseEntity<ApiResponse<List<ReviewTop5ResponseDto>>> getPopularReviews() {
		List<ReviewTop5ResponseDto> dto = reviewService.getTop5Reviews();
		return ApiResponse.onSuccess(SuccessStatus.GET_REVIEW_SUCCESS, dto);
	}
}
