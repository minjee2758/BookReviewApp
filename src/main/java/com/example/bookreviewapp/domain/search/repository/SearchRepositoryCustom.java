package com.example.bookreviewapp.domain.search.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.bookreviewapp.domain.book.dto.response.BookResponseDto;
import com.example.bookreviewapp.domain.review.dto.ReviewResponseDto;

public interface SearchRepositoryCustom {
	Page<BookResponseDto> searchBooks(String keyword, Pageable pageable);
	Page<ReviewResponseDto> searchReviews(String keyword, Pageable pageable);

}
