package com.example.bookreviewapp.domain.like.dto;

import com.example.bookreviewapp.domain.like.entity.Like;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class LikeResponseDto {
    private Long bookId;
    private String title;
    private LocalTime likedAt;

    public static LikeResponseDto fromLiketoDto(Like like) {
        return new LikeResponseDto(
                like.getBook().getId(),
                like.getBook().getTitle(),
                like.getLikedAt().toLocalTime()
        );
    }
}

