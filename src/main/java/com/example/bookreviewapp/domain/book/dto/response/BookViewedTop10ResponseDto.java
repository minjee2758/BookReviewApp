package com.example.bookreviewapp.domain.book.dto.response;

import com.example.bookreviewapp.domain.book.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class BookViewedTop10ResponseDto {

    private final Integer rank;

    private final Long id;

    private final String title;

    private final String author;

    private final String category;

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;

    // entity -> dto 변환 메소드 ( + 순위 포함 )
    public static BookViewedTop10ResponseDto from(Book book, Integer rank) {
        return new BookViewedTop10ResponseDto(
                rank,
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getCategory(),
                book.getCreatedAt(),
                book.getUpdatedAt()
        );
    }
}
