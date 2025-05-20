package com.example.bookreviewapp.domain.search.dto;



import org.springframework.data.domain.Page;

import com.example.bookreviewapp.domain.book.dto.response.BookResponseDto;
import com.example.bookreviewapp.domain.review.dto.ReviewResponseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SearchResponseDto {
	private Page<BookResponseDto> books;
	private Page<ReviewResponseDto> reviews;

	public SearchResponseDto(Page<BookResponseDto> books,
		                     Page<ReviewResponseDto> reviews){
		this.books = books;
		this.reviews = reviews;
	}
}
