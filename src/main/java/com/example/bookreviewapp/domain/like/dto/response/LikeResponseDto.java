package com.example.bookreviewapp.domain.like.dto.response;

import com.example.bookreviewapp.domain.like.entity.Like;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class LikeResponseDto {
    private final Long bookId;
    private final String title;
    private final LocalTime likedAt;

    @JsonCreator
    public LikeResponseDto(
        @JsonProperty("bookId") Long bookId,
        @JsonProperty("title")  String title,
        @JsonProperty("likedAt") LocalTime likedAt
    ) {
        this.bookId = bookId;
        this.title  = title;
        this.likedAt = likedAt;
    }

    public static LikeResponseDto fromLiketoDto(Like like) {
        return new LikeResponseDto(
                like.getBook().getId(),
                like.getBook().getTitle(),
                like.getLikedAt().toLocalTime()
        );
    }
}

