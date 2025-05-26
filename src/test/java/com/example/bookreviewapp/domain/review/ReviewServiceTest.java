package com.example.bookreviewapp.domain.review;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.bookreviewapp.common.code.ErrorStatus;
import com.example.bookreviewapp.common.error.ApiException;
import com.example.bookreviewapp.common.security.CustomUserDetails;
import com.example.bookreviewapp.domain.book.entity.Book;
import com.example.bookreviewapp.domain.book.repository.BookRepository;
import com.example.bookreviewapp.domain.review.dto.ReviewResquestDto;
import com.example.bookreviewapp.domain.review.entity.PinStatus;
import com.example.bookreviewapp.domain.review.entity.Review;
import com.example.bookreviewapp.domain.review.repository.ReviewRepository;
import com.example.bookreviewapp.domain.review.service.ReviewService;
import com.example.bookreviewapp.domain.user.entity.User;
import com.example.bookreviewapp.domain.user.entity.UserRole;
import com.example.bookreviewapp.domain.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {
	@Mock
	private ReviewRepository reviewRepository;
	@Mock
	private UserRepository userRepository;
	@Mock
	private BookRepository bookRepository;
	@InjectMocks
	private ReviewService reviewService;

	/*
	1. 리뷰 등록 postReview
		(1) 리뷰 등록 성공
		(2) 등록 안된 책을 리뷰 등록하려고 했을 때
	 */

	//(1) 리뷰 등록 성공
	@Test
	void 사용자가_리뷰를_정상적으로_등록한다() {
		Long userId = 1L;
		Long bookId = 1L;
		Book book = new Book();
		book.acceptEnroll();
		CustomUserDetails userDetails = new CustomUserDetails(userId, "email@naver.com", UserRole.USER);
		User user = new User();
		ReviewResquestDto dto = new ReviewResquestDto("내용", 5);

		//given
		given(bookRepository.findById(bookId)).willReturn(Optional.of(book));
		given(userRepository.findById(userId)).willReturn(Optional.of(user));

		//when
		reviewService.postReview(userDetails, bookId, dto);

		// then
		verify(reviewRepository, times(1)).save(any(Review.class));
	}

	//(2) 등록안된 책을 등록하려고 할 떄
	@Test
	void 존재하지_않는_책_리뷰등록_실패_예외발생() {
		// given
		Long bookId = 1L;
		Long userId = 1L;
		CustomUserDetails userDetails = new CustomUserDetails(userId, "email@naver.com", UserRole.USER);

		given(bookRepository.findById(bookId)).willReturn(Optional.empty());

		// when & then
		ApiException exception = assertThrows(ApiException.class, () ->
			reviewService.postReview(userDetails, bookId, new ReviewResquestDto("리뷰", 4))
		);

		assertEquals(ErrorStatus.BOOK_NOT_FOUND, exception.getErrorCode());
		verify(reviewRepository, never()).save(any());
	}

	/*
	2. 리뷰 수정
		(1) 리뷰 수정 성공
		(2) 존재하지 않는 책의 리뷰를 수정하려고 할때
		(3) 존재하지 않는 리뷰 일때
		(4) 해당 리뷰를 등록한 유저가 아닐 때
	 */
	@Test
	void 리뷰_수정_성공() {
		// given
		Long bookId = 1L;
		Long reviewId = 1L;

		User user = new User("email@naver.com", "email@naver.com", UserRole.USER);
		CustomUserDetails userDetails = new CustomUserDetails(user.getId(), "email@naver.com", UserRole.USER);

		Book book = new Book();
		book.acceptEnroll();

		Review review = new Review(user, book, "기존 내용", 3, PinStatus.UNPINNED);

		given(bookRepository.existsById(bookId)).willReturn(true);
		given(reviewRepository.findById(reviewId)).willReturn(Optional.of(review));

		ReviewResquestDto dto = new ReviewResquestDto("수정된 내용", 5);

		// when
		reviewService.updateReview(userDetails, bookId, reviewId, dto);

		// then
		assertEquals("수정된 내용", review.getContent());
		assertEquals(5, review.getScore());
		verify(reviewRepository).save(review);
	}

	@Test
	void 존재하지_않는_책의_리뷰_수정_실패() {
		// given
		Long bookId = 999L;
		Long reviewId = 1L;
		Long userId = 1L;

		CustomUserDetails userDetails = new CustomUserDetails(userId, "user@mail.com", UserRole.USER);
		given(bookRepository.existsById(bookId)).willReturn(false);

		// when & then
		ApiException exception = assertThrows(ApiException.class, () ->
			reviewService.updateReview(userDetails, bookId, reviewId, new ReviewResquestDto("내용", 3))
		);

		assertEquals(ErrorStatus.BOOK_NOT_FOUND, exception.getErrorCode());
		verify(reviewRepository, never()).save(any());
	}

	@Test
	void 존재하지_않는_리뷰_수정_실패() {
		// given
		Long bookId = 1L;
		Long reviewId = 999L;
		Long userId = 1L;

		CustomUserDetails userDetails = new CustomUserDetails(userId, "user@mail.com", UserRole.USER);
		given(bookRepository.existsById(bookId)).willReturn(true);
		given(reviewRepository.findById(reviewId)).willReturn(Optional.empty());

		// when & then
		ApiException exception = assertThrows(ApiException.class, () ->
			reviewService.updateReview(userDetails, bookId, reviewId, new ReviewResquestDto("내용", 3))
		);

		assertEquals(ErrorStatus.REVIEW_NOT_FOUND, exception.getErrorCode());
		verify(reviewRepository, never()).save(any());
	}

	@Test
	void 다른_유저가_리뷰_수정_실패() {
		// given
		Long bookId = 1L;
		Long reviewId = 1L;
		Long userId = 1L; // 로그인한 사용자
		CustomUserDetails userDetails = new CustomUserDetails(userId, "user@mail.com", UserRole.USER);

		User otherUser = new User("otheruser@gmail.com", "123**", UserRole.USER);
		Book book = new Book(otherUser, "title", "author", "category");
		book.acceptEnroll();
		Review review = new Review(otherUser, book, "내용", 5, PinStatus.UNPINNED);

		given(bookRepository.existsById(bookId)).willReturn(true);
		given(reviewRepository.findById(reviewId)).willReturn(Optional.of(review));

		// when & then
		ApiException exception = assertThrows(ApiException.class, () ->
			reviewService.updateReview(userDetails, bookId, reviewId, new ReviewResquestDto("내용", 5))
		);

		assertEquals(ErrorStatus.USER_NOT_EQUAL, exception.getErrorCode());
		verify(reviewRepository, never()).save(any());
	}

	@Test
	void 리뷰_삭제_성공() {
		// given
		Long bookId = 1L;
		Long reviewId = 1L;

		User user = new User("otheruser@gmail.com", "123**", UserRole.USER);
		CustomUserDetails userDetails = new CustomUserDetails(user.getId(), "user@mail.com", UserRole.USER);

		Book book = new Book(user, "title", "author", "category");
		Review review = new Review(user, book, "내용", 5, PinStatus.UNPINNED);

		given(bookRepository.existsById(bookId)).willReturn(true);
		given(reviewRepository.existsById(reviewId)).willReturn(true);
		given(reviewRepository.findById(reviewId)).willReturn(Optional.of(review));

		// when
		reviewService.deleteReview(userDetails, bookId, reviewId);

		// then
		verify(reviewRepository).deleteById(reviewId);
	}

	@Test
	void 존재하지_않는_책_리뷰삭제_실패() {
		// given
		Long bookId = 999L;
		Long reviewId = 1L;
		Long userId = 1L;

		CustomUserDetails userDetails = new CustomUserDetails(userId, "user@mail.com", UserRole.USER);

		given(bookRepository.existsById(bookId)).willReturn(false);

		// when & then
		ApiException exception = assertThrows(ApiException.class, () ->
			reviewService.deleteReview(userDetails, bookId, reviewId)
		);

		assertEquals(ErrorStatus.BOOK_NOT_FOUND, exception.getErrorCode());
		verify(reviewRepository, never()).deleteById(any());
	}

	@Test
	void 존재하지_않는_리뷰_삭제_실패() {
		// given
		Long bookId = 1L;
		Long reviewId = 999L;
		Long userId = 1L;

		CustomUserDetails userDetails = new CustomUserDetails(userId, "user@mail.com", UserRole.USER);

		given(bookRepository.existsById(bookId)).willReturn(true);
		given(reviewRepository.existsById(reviewId)).willReturn(false);

		// when & then
		ApiException exception = assertThrows(ApiException.class, () ->
			reviewService.deleteReview(userDetails, bookId, reviewId)
		);

		assertEquals(ErrorStatus.REVIEW_NOT_FOUND, exception.getErrorCode());
		verify(reviewRepository, never()).deleteById(any());
	}

	@Test
	void 다른_유저의_리뷰_삭제_실패() {
		// given
		Long bookId = 1L;
		Long reviewId = 1L;
		Long loggedInUserId = 1L;
		Long authorId = 2L;

		CustomUserDetails userDetails = new CustomUserDetails(loggedInUserId, "user@mail.com", UserRole.USER);

		User user = new User("otheruser@gmail.com", "123**", UserRole.USER);
		Book book = new Book(user, "title", "author", "category");
		book.acceptEnroll();
		Review review = new Review(user, book, "내용", 5, PinStatus.UNPINNED);

		given(bookRepository.existsById(bookId)).willReturn(true);
		given(reviewRepository.existsById(reviewId)).willReturn(true);
		given(reviewRepository.findById(reviewId)).willReturn(Optional.of(review));

		// when & then
		ApiException exception = assertThrows(ApiException.class, () ->
			reviewService.deleteReview(userDetails, bookId, reviewId)
		);

		assertEquals(ErrorStatus.USER_NOT_EQUAL, exception.getErrorCode());
		verify(reviewRepository, never()).deleteById(any());
	}

}
