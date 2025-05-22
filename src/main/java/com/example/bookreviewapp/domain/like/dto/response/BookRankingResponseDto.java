package com.example.bookreviewapp.domain.like.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookRankingResponseDto {
    private String title;
    private int score;
}