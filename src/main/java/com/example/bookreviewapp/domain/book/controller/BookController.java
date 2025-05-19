package com.example.bookreviewapp.domain.book.controller;

import com.example.bookreviewapp.common.code.SuccessStatus;
import com.example.bookreviewapp.common.response.ApiResponse;
import com.example.bookreviewapp.domain.book.dto.request.CreateBookRequestDto;
import com.example.bookreviewapp.domain.book.dto.response.BookResponseDto;
import com.example.bookreviewapp.domain.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<ApiResponse<BookResponseDto>> createBook(@RequestBody CreateBookRequestDto requestDto) {

        BookResponseDto responseDto = bookService.createBook(requestDto.getTitle(), requestDto.getAuthor(), requestDto.getCategory());

        return ApiResponse.onSuccess(SuccessStatus.CREATE_BOOK, responseDto);
    }

}
