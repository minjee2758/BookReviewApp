package com.example.bookreviewapp.domain.book.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateBookRequestDto {

    private final String title;

    private final String author;

    private final String category;
}
