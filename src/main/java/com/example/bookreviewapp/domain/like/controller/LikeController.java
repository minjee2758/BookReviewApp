package com.example.bookreviewapp.domain.like.controller;

import com.example.bookreviewapp.common.code.SuccessStatus;
import com.example.bookreviewapp.common.response.ApiResponse;
import com.example.bookreviewapp.domain.like.dto.LikeRequestDto;
import com.example.bookreviewapp.domain.like.dto.LikeResponseDto;
import com.example.bookreviewapp.domain.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/{userId}/likes")
    public ResponseEntity<ApiResponse<Void>> saveLike(
            @PathVariable Long userId,
            @RequestBody LikeRequestDto requestDto
    ) {
        likeService.saveLike(userId,requestDto);
        return ApiResponse.onSuccess(SuccessStatus.CREATE_LIKE);
    }

    @GetMapping("/{userId}/likes")
    public ResponseEntity<ApiResponse<Page<LikeResponseDto>>> getUserLikes(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<LikeResponseDto> response = likeService.getAllLists(userId, page, size);
        return ApiResponse.onSuccess(SuccessStatus.FIND_BOOK, response);
    }

    @DeleteMapping("/{userId}/likes")
    public ResponseEntity<ApiResponse<Void>> deleteLike(
            @PathVariable Long userId,
            @RequestBody LikeRequestDto requestDto
    ){
        likeService.deleteLike(userId,requestDto);
        return ApiResponse.onSuccess(SuccessStatus.DELETE_LIKE);
    }

}
