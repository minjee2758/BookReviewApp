package com.example.bookreviewapp.domain.like.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class LikeInfoDto {
    private Long bookId;
    private String title;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime likedAt;
}