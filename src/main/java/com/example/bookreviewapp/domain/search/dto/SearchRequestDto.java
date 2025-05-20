package com.example.bookreviewapp.domain.search.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor

public class SearchRequestDto {
	private String keyword;

	public SearchRequestDto(String keyword){
		this.keyword = keyword;
	}
}
