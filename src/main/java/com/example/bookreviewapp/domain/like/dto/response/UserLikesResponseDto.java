package com.example.bookreviewapp.domain.like.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UserLikesResponseDto {
    private final Long userId;
    private final List<LikeResponseDto> likes;
    private final int page;
    private final int size;
    private final long totalLikes;
}