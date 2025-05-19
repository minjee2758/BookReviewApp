package com.example.bookreviewapp.domain.book.service;

import com.example.bookreviewapp.domain.book.dto.response.BookResponseDto;
import com.example.bookreviewapp.domain.book.entity.Book;
import com.example.bookreviewapp.domain.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

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
}
