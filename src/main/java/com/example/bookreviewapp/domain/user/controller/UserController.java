package com.example.bookreviewapp.domain.user.controller;

import com.example.bookreviewapp.common.code.SuccessStatus;
import com.example.bookreviewapp.common.response.ApiResponse;
import com.example.bookreviewapp.common.security.CustomUserDetails;
import com.example.bookreviewapp.domain.book.dto.response.BookResponseDto;
import com.example.bookreviewapp.domain.book.entity.Book;
import com.example.bookreviewapp.domain.review.dto.ReviewResponseDto;
import com.example.bookreviewapp.domain.review.entity.Review;
import com.example.bookreviewapp.domain.review.service.ReviewService;
import com.example.bookreviewapp.domain.user.dto.UpdateRequestDto;
import com.example.bookreviewapp.domain.user.dto.UserMyInfoResponseDto;
import com.example.bookreviewapp.domain.user.service.UserService;
import com.example.bookreviewapp.domain.viewhistory.service.ViewHistoryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private final ReviewService reviewService;

    private final ViewHistoryService viewHistoryService;

    @GetMapping
    public ResponseEntity<ApiResponse<UserMyInfoResponseDto>> getMyInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiResponse.onSuccess(SuccessStatus.GETMYINFO_SUCCESS, new UserMyInfoResponseDto(userDetails.getEmail(), userDetails.getUserRole()));
    }

    @PatchMapping
    public ResponseEntity<ApiResponse<Void>> update(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody UpdateRequestDto request, HttpServletRequest httpRequest) {
        userService.update(userDetails.getId(), request, httpRequest);
        return ApiResponse.onSuccess(SuccessStatus.UPDATEINFO_SUCCESS);
    }
    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> delete(@AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest httpRequest) {
        userService.delete(userDetails.getId(), httpRequest);
        return ApiResponse.onSuccess(SuccessStatus.DELETE_SUCCESS);
    }

    @GetMapping("/reviews")
    public ResponseEntity<ApiResponse<Page<ReviewResponseDto>>> getReview(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                          @PageableDefault Pageable pageable) {
        Page<ReviewResponseDto> reviewResponse = reviewService.getReview(userDetails.getId(), pageable);
        return ApiResponse.onSuccess(SuccessStatus.GET_REVIEW_SUCCESS, reviewResponse);
    }

    @GetMapping("/recommend")
    public ResponseEntity<ApiResponse<Page<BookResponseDto>>> getRecommend(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                           @PageableDefault Pageable pageable) {
        Page<BookResponseDto> response = viewHistoryService.getRecommend(userDetails.getId(), pageable);

        return ApiResponse.onSuccess(SuccessStatus.GET_RECOMMEND_SUCCESS, response);
    }

}
