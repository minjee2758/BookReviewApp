package com.example.bookreviewapp.domain.like.controller;

import com.example.bookreviewapp.common.code.SuccessStatus;
import com.example.bookreviewapp.common.response.ApiResponse;
import com.example.bookreviewapp.common.security.CustomUserDetails;
import com.example.bookreviewapp.domain.like.dto.LikeRequestDto;
import com.example.bookreviewapp.domain.like.dto.UserLikesResponseDto;
import com.example.bookreviewapp.domain.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
    public ResponseEntity<ApiResponse<List<UserLikesResponseDto>>> getUserLikes(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "DESC") String direction
    ) {
        Pageable pageable = PageRequest.of(
                Math.max(0, page - 1),
                size,
                Sort.by(Sort.Direction.fromString(direction), sort)
        );

        UserLikesResponseDto responseDto = likeService.getUserLikes(userDetails.getId(), pageable);
        return ApiResponse.onSuccess(SuccessStatus.GETINFO_LIKE, List.of(responseDto));
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
