package com.example.bookreviewapp.domain.auth.controller;

import com.example.bookreviewapp.common.code.SuccessStatus;
import com.example.bookreviewapp.common.response.ApiResponse;
import com.example.bookreviewapp.domain.auth.dto.SignUpRequestDto;
import com.example.bookreviewapp.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signUp(@Valid @RequestBody SignUpRequestDto requestDto) {

        authService.signUp(requestDto.getEmail(), requestDto.getPassword(), requestDto.getUserRole());

        return ApiResponse.onSuccess(SuccessStatus.SIGNUP_SUCCESS);
    }
}
