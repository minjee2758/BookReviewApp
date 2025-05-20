package com.example.bookreviewapp.domain.search.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class SearchRequestDto {
	private final String keyword;

	@JsonCreator
	public SearchRequestDto(
		@JsonProperty("keyword") String keyword   // ← 여기에 붙입니다
	) {
		this.keyword = keyword;
	}
}