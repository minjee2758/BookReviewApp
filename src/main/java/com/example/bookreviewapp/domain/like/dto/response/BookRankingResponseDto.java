package com.example.bookreviewapp.domain.like.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class BookRankingResponseDto {
    private final Integer rank;
    private final String title;
    private final int score;

    @JsonCreator
    public BookRankingResponseDto(
        @JsonProperty("rank")  Integer rank,
        @JsonProperty("title") String title,
        @JsonProperty("score") int score
    ) {
        this.rank  = rank;
        this.title = title;
        this.score = score;
    }
}