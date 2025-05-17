package com.example.bookreviewapp.domain.user.controller;

import com.example.bookreviewapp.common.code.SuccessStatus;
import com.example.bookreviewapp.common.response.ApiResponse;
import com.example.bookreviewapp.common.security.CustomUserDetails;
import com.example.bookreviewapp.domain.user.dto.UpdateRequestDto;
import com.example.bookreviewapp.domain.user.dto.UserMyInfoResponseDto;
import com.example.bookreviewapp.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<UserMyInfoResponseDto>> getMyInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiResponse.onSuccess(SuccessStatus.GETMYINFO_SUCCESS, new UserMyInfoResponseDto(userDetails.getEmail(), userDetails.getUserRole()));
    }

    @PatchMapping
    public ResponseEntity<ApiResponse<Void>> update(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody UpdateRequestDto request, HttpServletRequest httpRequest) {
        userService.update(userDetails.getId(), request, httpRequest);
        return ApiResponse.onSuccess(SuccessStatus.UPDATEINFO_SUCCESS);
    }

}
