package com.example.bookreviewapp.domain.like.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class LikeResponseDto {

    private

    @JsonFormat(pattern = "yyyy-mm-dd hh:mm:ss")
    private LocalDateTime createAt;
}
