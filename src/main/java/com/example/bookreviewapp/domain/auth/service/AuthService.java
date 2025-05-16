package com.example.bookreviewapp.domain.auth.service;

import com.example.bookreviewapp.common.jwt.JwtUtil;
import com.example.bookreviewapp.common.jwt.TokenService;
import com.example.bookreviewapp.domain.auth.dto.TokenDto;
import com.example.bookreviewapp.domain.user.entity.User;
import com.example.bookreviewapp.domain.user.entity.UserRole;
import com.example.bookreviewapp.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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

        userRepository.save(saveUser);
    }

    public TokenDto login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("이메일이 없음"));

        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호");
        }

        String accessToken = jwtUtil.createAccessToken(user.getId(), user.getEmail(), user.getUserRole());

        String refreshToken = jwtUtil.createRefreshToken(user.getId());

//        tokenService.saveRefreshToken(user.getId(), refreshToken);

        TokenDto tokenDto = new TokenDto(accessToken, refreshToken);

        return tokenDto;
    }

//    public void logout(HttpServletRequest request, long userId) {
//        tokenService.blacklistAccessToken(jwtUtil.resolveAccessToken(request));
//        tokenService.deleteRefreshToken(userId);
//    }

    public User testLogin(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자 정보"));

        return user;
    }

}
