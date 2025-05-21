package com.example.bookreviewapp.domain.book.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class CreateBookRequestDto {

    private final String title;

    private final String author;

    private final String category;

    @JsonCreator
    public CreateBookRequestDto(
            @JsonProperty("title") String title,
            @JsonProperty("author") String author,
            @JsonProperty("category") String category
    ) {
        this.title = title;
        this.author = author;
        this.category = category;
    }
}
