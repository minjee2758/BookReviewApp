package com.example.bookreviewapp.domain.search.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bookreviewapp.common.code.SuccessStatus;
import com.example.bookreviewapp.common.response.ApiResponse;
import com.example.bookreviewapp.domain.search.dto.SearchRequestDto;
import com.example.bookreviewapp.domain.search.dto.SearchResponseDto;
import com.example.bookreviewapp.domain.search.service.SearchService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {
	private final SearchService searchService;

	@PostMapping
	public ResponseEntity<ApiResponse<SearchResponseDto>> search(
		@RequestBody SearchRequestDto request,
		@Qualifier("book") @PageableDefault(page = 0, size = 10) Pageable bookPageable,
		@Qualifier("review") @PageableDefault(page = 0, size = 5) Pageable reviewPageable
	) {
		SearchResponseDto result = searchService.search(
			request.getKeyword(),
			bookPageable,
			reviewPageable
		);
		return ApiResponse.onSuccess(SuccessStatus.SEARCH_SUCCESS, result);
	}
}
