package com.example.bookreviewapp.domain.search.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PopularKeywordDto {
	private final int  rank;       // 순위
	private final String keyword;  // 검색어
	private final long score;      // 정수형 점수로 변경
}
