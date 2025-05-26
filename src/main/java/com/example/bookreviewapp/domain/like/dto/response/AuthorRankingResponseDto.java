package com.example.bookreviewapp.domain.like.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class AuthorRankingResponseDto {
    private final Integer rank;
    private final String name;
    private final int score;

    @JsonCreator
    public AuthorRankingResponseDto(
        @JsonProperty("rank") Integer rank,
        @JsonProperty("name") String name,
        @JsonProperty("score") int score
    ) {
        this.rank  = rank;
        this.name  = name;
        this.score = score;
    }
}