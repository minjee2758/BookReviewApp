package com.example.bookreviewapp.domain.like.controller;

import com.example.bookreviewapp.common.code.SuccessStatus;
import com.example.bookreviewapp.common.response.ApiResponse;
import com.example.bookreviewapp.common.security.CustomUserDetails;
import com.example.bookreviewapp.domain.like.dto.response.AuthorRankingResponseDto;
import com.example.bookreviewapp.domain.like.dto.response.BookRankingResponseDto;
import com.example.bookreviewapp.domain.like.service.LikeRankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ranking")
public class LikeRankingController {

    private final LikeRankingService likeRankingService;

    @GetMapping("/books")
    public ResponseEntity<ApiResponse<List<BookRankingResponseDto>>> getTop10Books(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        List<BookRankingResponseDto> response = likeRankingService.getTop10BooksWithTitle();
        return ApiResponse.onSuccess(SuccessStatus.GET_RANKING_LIKE, response);
    }


    @GetMapping("/authors")
    public ResponseEntity<ApiResponse<List<AuthorRankingResponseDto>>> getTop10Authors(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        List<AuthorRankingResponseDto> response = likeRankingService.getTop10Authors();
        return ApiResponse.onSuccess(SuccessStatus.GET_RANKING_LIKE, response);
    }

}
