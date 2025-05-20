package com.example.bookreviewapp.domain.search.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.bookreviewapp.domain.search.dto.SearchResponseDto;
import com.example.bookreviewapp.domain.search.repository.SearchRepositoryCustom;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchService {
	private final SearchRepositoryCustom searchRepository;

	public SearchResponseDto search(String keyword,
		Pageable bookPageable,
		Pageable reviewPageable) {
		return new SearchResponseDto(
			searchRepository.searchBooks(keyword, bookPageable),
			searchRepository.searchReviews(keyword, reviewPageable)
		);
	}
}
