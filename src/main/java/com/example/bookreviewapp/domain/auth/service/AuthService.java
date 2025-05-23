package com.example.bookreviewapp.domain.auth.service;

import com.example.bookreviewapp.common.code.ErrorStatus;
import com.example.bookreviewapp.common.error.ApiException;
import com.example.bookreviewapp.common.jwt.JwtUtil;
import com.example.bookreviewapp.common.jwt.TokenService;
import com.example.bookreviewapp.domain.auth.dto.TokenDto;
import com.example.bookreviewapp.domain.user.entity.User;
import com.example.bookreviewapp.domain.user.entity.UserRole;
import com.example.bookreviewapp.domain.user.entity.UserStatus;
import com.example.bookreviewapp.domain.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    private final TokenService tokenService;

    public void signUp(String email, String password, UserRole userRole) {
        String encodePassword = passwordEncoder.encode(password);

        User saveUser = new User(email, encodePassword, userRole);
        try {
            userRepository.save(saveUser);
        } catch (DataIntegrityViolationException e) {
            throw new ApiException(ErrorStatus.DUPLICATE_EMAIL);
        }
    }

    public TokenDto login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(ErrorStatus.USER_NOT_FOUND));

        if(user.getUserStatus() != UserStatus.ACTIVE) {
            throw new ApiException(ErrorStatus.USER_DEACTIVATE);
        }

        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new ApiException(ErrorStatus.USER_NOT_MATCH);
        }

        String accessToken = jwtUtil.createAccessToken(user.getId(), user.getEmail(), user.getUserRole());

        String refreshToken = jwtUtil.createRefreshToken(user.getId());

        tokenService.saveRefreshToken(user.getId(), refreshToken);

        TokenDto tokenDto = new TokenDto(accessToken, refreshToken);

        return tokenDto;
    }

    public void logout(HttpServletRequest request, long userId) {
        tokenService.blacklistAccessToken(jwtUtil.resolveAccessToken(request));
        tokenService.deleteRefreshToken(userId);
    }

    public String reissueToken(HttpServletRequest request) {
        String refreshToken = extractTokenFromCookie(request, "refreshToken");

        Long userId = jwtUtil.getUserIdFromToken(refreshToken);

        String savedRefreshToken = tokenService.getRefreshToken(userId);

        if(!refreshToken.equals(savedRefreshToken)) {
            throw new ApiException(ErrorStatus.INVALID_TOKEN);
        }

        String oldAccessToken = jwtUtil.resolveAccessToken(request);

        if(!jwtUtil.isExpired(oldAccessToken)) { // 기존 어세스 토큰이 살아있다면 블랙리스트 처리
            tokenService.blacklistAccessToken(oldAccessToken);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorStatus.USER_NOT_FOUND));

        String newAccessToken = jwtUtil.createAccessToken(userId, user.getEmail(), user.getUserRole());

        return newAccessToken;
    }

    private String extractTokenFromCookie(HttpServletRequest request, String name) {
        if(request.getCookies() == null) return null;

        for(Cookie cookie : request.getCookies()) {
            if(cookie.getName().equals(name)) {
                return cookie.getValue();
            }
        }
        return null;
    }

}
