package com.example.bookreviewapp.domain.review.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.bookreviewapp.common.code.ErrorStatus;
import com.example.bookreviewapp.common.error.ApiException;
import com.example.bookreviewapp.common.security.CustomUserDetails;
import com.example.bookreviewapp.domain.book.entity.Book;
import com.example.bookreviewapp.domain.book.repository.BookRepository;
import com.example.bookreviewapp.domain.review.dto.ReviewResponseDto;
import com.example.bookreviewapp.domain.review.dto.ReviewResquestDto;
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
public class ReviewServiceImpl implements ReviewService {
	private final ReviewRepository reviewRepository;
	private final BookRepository bookRepository;
	private final UserRepository userRepository;

	//리뷰 등록
	@Override
	public void postReview(CustomUserDetails userDetails, Long bookId, ReviewResquestDto dto) {
		Book book = bookRepository.findById(bookId).orElseThrow(
			() -> new ApiException(ErrorStatus.BOOK_NOT_FOUND));

		User user = userRepository.findById(userDetails.getId())
			.orElseThrow(() -> new ApiException(ErrorStatus.USER_NOT_FOUND));

		reviewRepository.save(new Review(user, book, dto.getContent(), dto.getScore(), PinStatus.UNPINNED));
	}

	//리뷰 수정
	@Override
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
	@Override
	public List<ReviewResponseDto> getReviews(Long bookId) {
		List<ReviewResponseDto> reviewList = reviewRepository.getReviews(bookId);
		return null;
	}

	//리뷰 삭제
	@Override
	public void deleteReview(CustomUserDetails userDetails, Long bookId, Long reviewId) {
		if (!bookRepository.existsById(bookId)) {
			throw new ApiException(ErrorStatus.BOOK_NOT_FOUND);
		}
		if (!reviewRepository.existsById(reviewId)) {
			throw new ApiException(ErrorStatus.REVIEW_NOT_FOUND);
		}
		if (reviewRepository.findById(reviewId).get().getUser().getId() != userDetails.getId()) {
			throw new ApiException(ErrorStatus.USER_NOT_EQUAL);
		}

		reviewRepository.deleteById(reviewId);
	}

}
