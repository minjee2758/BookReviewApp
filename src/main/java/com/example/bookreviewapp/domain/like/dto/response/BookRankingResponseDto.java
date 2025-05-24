package com.example.bookreviewapp.domain.like.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookRankingResponseDto {
    private final Integer rank;
    private final String title;
    private final int score;
}