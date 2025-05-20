package com.example.bookreviewapp.domain.like.controller;

import com.example.bookreviewapp.common.code.SuccessStatus;
import com.example.bookreviewapp.common.response.ApiResponse;
import com.example.bookreviewapp.common.security.CustomUserDetails;
import com.example.bookreviewapp.domain.like.dto.LikeRequestDto;
import com.example.bookreviewapp.domain.like.dto.LikeResponseDto;
import com.example.bookreviewapp.domain.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/likes")
    public ResponseEntity<ApiResponse<Void>> saveLike(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody LikeRequestDto requestDto
    ) {

        likeService.saveLike(userDetails.getId(), requestDto);
        return ApiResponse.onSuccess(SuccessStatus.CREATE_LIKE);
    }

    @GetMapping("/likes")
    public ResponseEntity<ApiResponse<Page<LikeResponseDto>>> getUserLikes(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<LikeResponseDto> response = likeService.getAllLists(userDetails.getId(), pageable);
        return ApiResponse.onSuccess(SuccessStatus.FIND_BOOK, response);
    }

    @DeleteMapping("/likes")
    public ResponseEntity<ApiResponse<Void>> deleteLike(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody LikeRequestDto requestDto
    ){
        likeService.deleteLike(userDetails.getId(), requestDto);
        return ApiResponse.onSuccess(SuccessStatus.DELETE_LIKE);
    }

}
