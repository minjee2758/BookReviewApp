package com.example.bookreviewapp.domain.book.dto.response;

import com.example.bookreviewapp.domain.book.entity.Book;
import com.example.bookreviewapp.domain.book.entity.EnrollStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class BookResponseDto {

    private final Long id;

    private final String title;

    private final String author;

    private final String category;

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;

    private final EnrollStatus enrollStatus;

    // entity -> dto 변환
    public static BookResponseDto from(Book book) {
        return new BookResponseDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getCategory(),
                book.getCreatedAt(),
                book.getUpdatedAt(),
                book.getEnrollStatus()
        );
    }
}
