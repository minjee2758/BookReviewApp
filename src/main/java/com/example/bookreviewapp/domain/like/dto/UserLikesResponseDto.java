package com.example.bookreviewapp.domain.like.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;
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