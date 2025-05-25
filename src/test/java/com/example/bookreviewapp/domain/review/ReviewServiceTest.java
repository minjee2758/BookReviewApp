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
		(3) 없는 책일 떄
		(4) 유저가 없을 때
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
		Book book = new Book();
		CustomUserDetails userDetails = new CustomUserDetails(userId, "email@naver.com", UserRole.USER);

		//given
		given(bookRepository.findById(bookId)).willReturn(Optional.empty());

		// when & then
		ApiException exception = assertThrows(ApiException.class, () ->
			reviewService.postReview(userDetails, bookId, new ReviewResquestDto("리뷰", 4))
		);

		assertEquals(ErrorStatus.BOOK_NOT_FOUND, exception.getErrorCode());
		verify(reviewRepository, never()).save(any());
	}

}
