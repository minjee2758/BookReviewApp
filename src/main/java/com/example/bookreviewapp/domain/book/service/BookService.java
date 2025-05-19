package com.example.bookreviewapp.domain.book.service;

import com.example.bookreviewapp.domain.book.dto.response.BookResponseDto;
import com.example.bookreviewapp.domain.book.entity.Book;
import com.example.bookreviewapp.domain.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    @Transactional
    public BookResponseDto createBook(String title, String author, String category) {

        Book newBook = new Book(title, author, category);

        Book savedBook = bookRepository.save(newBook);

        return new BookResponseDto(
                savedBook.getId(),
                savedBook.getTitle(),
                savedBook.getAuthor(),
                savedBook.getCategory(),
                savedBook.getCreatedAt(),
                savedBook.getUpdatedAt(),
                savedBook.getEnrollStatus()
        );
    }

    public Page<BookResponseDto> findAllBooks(Pageable pageable) {
        Page<Book> books = bookRepository.findAll(pageable);

        // books.map(book -> BookResponseDto.from(book)); 와 동일한 표현
        return books.map(BookResponseDto::from);
    }
}
