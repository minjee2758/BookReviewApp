package com.example.bookreviewapp.domain.book.dto.response;

import com.example.bookreviewapp.domain.book.entity.EnrollStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class BookDetailsResponseDto {

    private final Long id;

    private final String title;

    private final String author;

    private final String category;

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;

    private final EnrollStatus enrollStatus;

    private final Double rating;

    private final Long reviewCounts;

    private final Long likeCounts;

    private final Long viewer;

}
