package com.example.bookreviewapp.domain.like.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthorRankingResponseDto {
    private final Integer rank;
    private final String name;
    private final int score;
}