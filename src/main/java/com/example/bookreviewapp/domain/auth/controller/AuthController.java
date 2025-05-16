package com.example.bookreviewapp.domain.auth.controller;

import com.example.bookreviewapp.common.code.SuccessStatus;
import com.example.bookreviewapp.common.response.ApiResponse;
import com.example.bookreviewapp.common.security.CustomUserDetails;
import com.example.bookreviewapp.domain.auth.dto.LoginRequestDto;
import com.example.bookreviewapp.domain.auth.dto.SignUpRequestDto;
import com.example.bookreviewapp.domain.auth.dto.TestLoginDto;
import com.example.bookreviewapp.domain.auth.dto.TokenDto;
import com.example.bookreviewapp.domain.auth.service.AuthService;
import com.example.bookreviewapp.domain.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody LoginRequestDto requestDto, HttpServletResponse response) {

        TokenDto tokenDto = authService.login(requestDto.getEmail(), requestDto.getPassword());

//        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", tokenDto.getRefreshToken()) // 쿠키의 이름과 값을 설정하는 부분
//                .httpOnly(true) // JS 에서의 접근을 막는다
//                .secure(true)         // HTTPS만 사용할 경우 true
//                .path("/")            // 전체 경로에서 사용 가능
//                .maxAge(60 * 60)      // 1시간
//                .sameSite("Lax") // 브라우저가 이 쿠키를 어떤 상황에서 요청에 자동으로 포함시킬지, Lax = 같은 사이트 + 대부분의 get요청 허용
//                .build();
//
//        // HTTP 응답 헤더에 Set-Cookie로 추가
//        response.addHeader("Set-Cookie", refreshCookie.toString());

        return ApiResponse.onSuccess(SuccessStatus.LOGIN_SUCCESS, tokenDto.getAccessToken());
    }

//    @PostMapping("/logout")
//    public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {
//        authService.logout(request, userDetails.getId());
//
//        return ApiResponse.onSuccess(SuccessStatus.LOGOUT_SUCCESS);
//    }

//    @PostMapping("/reissue")
//    public ResponseEntity<?> reissueToken(HttpServletRequest request) {
//
//        String accessToken = authService.reissueToken(request);
//
//        return new ResponseEntity<>(accessToken, HttpStatus.OK);
//    }

    @GetMapping("/testLogin")
    public ResponseEntity<TestLoginDto> testLogin(@AuthenticationPrincipal CustomUserDetails userDetails) {

        User user = authService.testLogin(userDetails.getId());

        TestLoginDto response = new TestLoginDto(user.getEmail(), user.getUserRole());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
