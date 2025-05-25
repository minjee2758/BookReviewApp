package com.example.bookreviewapp.domain.review.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.bookreviewapp.common.code.ErrorStatus;
import com.example.bookreviewapp.common.error.ApiException;
import com.example.bookreviewapp.common.security.CustomUserDetails;
import com.example.bookreviewapp.domain.book.entity.Book;
import com.example.bookreviewapp.domain.book.entity.EnrollStatus;
import com.example.bookreviewapp.domain.book.repository.BookRepository;
import com.example.bookreviewapp.domain.review.dto.ReviewResponseDto;
import com.example.bookreviewapp.domain.review.dto.ReviewResquestDto;
import com.example.bookreviewapp.domain.review.dto.ReviewTop5ResponseDto;
import com.example.bookreviewapp.domain.review.entity.PinStatus;
import com.example.bookreviewapp.domain.review.entity.Review;
import com.example.bookreviewapp.domain.review.repository.ReviewRepository;
import com.example.bookreviewapp.domain.user.entity.User;
import com.example.bookreviewapp.domain.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {
	private final ReviewRepository reviewRepository;
	private final BookRepository bookRepository;
	private final UserRepository userRepository;
	private final ReviewRankingScheduler reviewRankingScheduler;
	private final RedisTemplate<String, String> redisTemplate;

	//리뷰 등록
	public void postReview(CustomUserDetails userDetails, Long bookId, ReviewResquestDto dto) {
		rejectReview(bookId);

		Book book = bookRepository.findById(bookId).orElseThrow(
			() -> new ApiException(ErrorStatus.BOOK_NOT_FOUND));
		User user = userRepository.findById(userDetails.getId())
			.orElseThrow(() -> new ApiException(ErrorStatus.USER_NOT_FOUND));

		reviewRepository.save(new Review(user, book, dto.getContent(), dto.getScore(), PinStatus.UNPINNED));
	}

	//리뷰 수정
	public void updateReview(CustomUserDetails userDetails, Long bookId, Long reviewId, ReviewResquestDto dto) {
		if (!bookRepository.existsById(bookId)) {
			throw new ApiException(ErrorStatus.BOOK_NOT_FOUND);
		}

		Review review = reviewRepository.findById(reviewId).orElseThrow(
			() -> new ApiException(ErrorStatus.REVIEW_NOT_FOUND));

		if (review.getUser().getId() != userDetails.getId()) {
			throw new ApiException(ErrorStatus.USER_NOT_EQUAL);
		}

		review.updateReview(dto.getContent(), dto.getScore());
		reviewRepository.save(review);
	}

	//리뷰 조회
	public Page<ReviewResponseDto> getReviews(Long bookId, Pageable pageable) {
		rejectReview(bookId);

		Page<Review> reviewPage = reviewRepository.findByBookId(bookId, pageable);

		// DTO로 변환
		return reviewPage.map(review ->
			new ReviewResponseDto(
				review.getBook().getTitle(),
				review.getUser().getEmail(),
				review.getContent(),
				review.getScore(),
				review.getViewer(),
				review.getCreatedAt(),
				review.getUpdatedAt()
			)
		);
	}

	//리뷰 삭제
	public void deleteReview(CustomUserDetails userDetails, Long bookId, Long reviewId) {
		if (!bookRepository.existsById(bookId)) {
			throw new ApiException(ErrorStatus.BOOK_NOT_FOUND);
		}
		if (!reviewRepository.existsById(reviewId)) {
			throw new ApiException(ErrorStatus.REVIEW_NOT_FOUND);
		}
		if (!Objects.equals(reviewRepository.findById(reviewId).get().getUser().getId(), userDetails.getId())) {
			throw new ApiException(ErrorStatus.USER_NOT_EQUAL);
		}

		reviewRepository.deleteById(reviewId);
	}

	public Page<ReviewResponseDto> getReview(Long userId, Pageable pageable) {
		return reviewRepository.findByUserId(userId, pageable)
			.map(ReviewResponseDto::new);
	}

	//리뷰 단건 조회
	public ReviewResponseDto getReviewDetail(Long bookId, Long reviewId) {
		bookRepository.findById(bookId).orElseThrow(
			() -> new ApiException(ErrorStatus.BOOK_NOT_FOUND)
		);

		rejectReview(bookId);

		// Redis에 조회수 증가
		redisTemplate.opsForZSet().incrementScore("review:view:ranking", "review:" + reviewId, 1);

		reviewRepository.increaseViewer(reviewId);
		Review review = reviewRepository.findById(reviewId).orElseThrow(
			() -> new ApiException(ErrorStatus.REVIEW_NOT_FOUND)
		);
		return new ReviewResponseDto(review);
	}

	//책 상태가 REJECT면 조회 거부
	public void rejectReview(Long bookId) {
		Book book = bookRepository.findById(bookId).orElseThrow(
			() -> new ApiException(ErrorStatus.BOOK_NOT_FOUND)
		);

		if (book.getEnrollStatus().equals(EnrollStatus.REJECT)) {
			throw new ApiException(ErrorStatus.BOOK_ENROLLMENT_IS_REJECTED);
		}
	}

	//인기 리뷰 조회
	public List<ReviewTop5ResponseDto> getTop5Reviews() {
		Set<String> topReviewKeys = redisTemplate.opsForZSet().reverseRange("review:view:ranking", 0, 4);

		if (topReviewKeys == null || topReviewKeys.isEmpty()) {
			return List.of();
		}

		List<Long> reviewIds = new ArrayList<>();
		for (String key : topReviewKeys) {
			Long parseLong = Long.parseLong(key.replace("review:", ""));
			reviewIds.add(parseLong);
		}

		List<ReviewTop5ResponseDto> result = new ArrayList<>();

		int rank = 1;

		for (Long reviewId : reviewIds) {
			Review review = reviewRepository.findById(reviewId).orElseThrow();
			result.add(ReviewTop5ResponseDto.from(review, rank++));
		}
		return result;
	}

}
