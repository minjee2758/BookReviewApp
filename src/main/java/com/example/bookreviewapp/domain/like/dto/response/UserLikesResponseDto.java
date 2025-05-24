package com.example.bookreviewapp.domain.like.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class UserLikesResponseDto {
    private final Long userId;
    private final List<LikeResponseDto> likes;
    private final int page;
    private final int size;
    private final long totalLikes;

    @JsonCreator
    public UserLikesResponseDto(
        @JsonProperty("userId")    Long userId,
        @JsonProperty("likes")     List<LikeResponseDto> likes,
        @JsonProperty("page")      int page,
        @JsonProperty("size")      int size,
        @JsonProperty("totalLikes") long totalLikes
    ) {
        this.userId     = userId;
        this.likes      = likes;
        this.page       = page;
        this.size       = size;
        this.totalLikes = totalLikes;
}
}