package com.example.bookreviewapp.domain.like.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UserLikesResponseDto {
    private Long userId;
    private List<LikeResponseDto> likes;
    private int page;
    private int size;
    private long totalLikes;
}