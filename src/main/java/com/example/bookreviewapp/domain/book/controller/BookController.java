package com.example.bookreviewapp.domain.book.controller;

import com.example.bookreviewapp.common.code.SuccessStatus;
import com.example.bookreviewapp.common.response.ApiResponse;
import com.example.bookreviewapp.common.security.CustomUserDetails;
import com.example.bookreviewapp.domain.book.dto.request.CreateBookRequestDto;
import com.example.bookreviewapp.domain.book.dto.request.UpdateBookRequestDto;
import com.example.bookreviewapp.domain.book.dto.response.BookDetailsResponseDto;
import com.example.bookreviewapp.domain.book.dto.response.BookResponseDto;
import com.example.bookreviewapp.domain.book.dto.response.BookViewedTop10ResponseDto;
import com.example.bookreviewapp.domain.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    // 도서 생성
    @PostMapping
    public ResponseEntity<ApiResponse<BookResponseDto>> createBook(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody CreateBookRequestDto requestDto
    ) {

        BookResponseDto responseDto = bookService.createBook(customUserDetails.getId(), requestDto.getTitle(), requestDto.getAuthor(), requestDto.getCategory());

        return ApiResponse.onSuccess(SuccessStatus.CREATE_BOOK, responseDto);
    }

    // 도서 목록 조회
    @GetMapping
    public ResponseEntity<ApiResponse<Page<BookResponseDto>>> findAllBooks(
            @PageableDefault(direction = Sort.Direction.DESC, sort = "createdAt") Pageable pageable
    ) {

        Page<BookResponseDto> responseDto = bookService.findAllBooks(pageable);

        return ApiResponse.onSuccess(SuccessStatus.FIND_BOOK, responseDto);
    }

    // 도서 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookDetailsResponseDto>> findByDetailsBook(@PathVariable Long id) {

        BookDetailsResponseDto responseDto = bookService.findByDetailsBook(id);

        return ApiResponse.onSuccess(SuccessStatus.FIND_BOOK, responseDto);
    }

    // 도서 수정
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BookResponseDto>> editBook(
            @PathVariable Long id,
            @RequestBody UpdateBookRequestDto requestDto
    ) {

        BookResponseDto responseDto = bookService.editBook(id, requestDto.getTitle(), requestDto.getAuthor(), requestDto.getCategory());

        return ApiResponse.onSuccess(SuccessStatus.UPDATE_BOOK, responseDto);
    }

    // 도서 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBook(@PathVariable Long id) {

        bookService.deleteBook(id);

        return ApiResponse.onSuccess(SuccessStatus.DELETE_BOOK);
    }

    // 조회순 Top 10 조회
    @GetMapping("/viewedTop10")
    public ResponseEntity<ApiResponse<List<BookViewedTop10ResponseDto>>> findTop10ViewBooks() {

        List<BookViewedTop10ResponseDto> responseDtoList = bookService.findTop10ViewBooks();

        return ApiResponse.onSuccess(SuccessStatus.FIND_BOOK, responseDtoList);
    }
}
