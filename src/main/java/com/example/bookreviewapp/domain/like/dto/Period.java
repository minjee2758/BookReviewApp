package com.example.bookreviewapp.domain.like.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum Period {
    WEEKLY,
    MONTHLY;

    public static Period fromString(String period) {
        try {
            return Period.valueOf(period.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return WEEKLY;  // 기본값 WEEKLY로 설정
        }
    }
}