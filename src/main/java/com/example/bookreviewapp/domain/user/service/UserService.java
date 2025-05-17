package com.example.bookreviewapp.domain.user.service;

import com.example.bookreviewapp.domain.auth.service.AuthService;
import com.example.bookreviewapp.domain.user.dto.UpdateRequestDto;
import com.example.bookreviewapp.domain.user.entity.User;
import com.example.bookreviewapp.domain.user.entity.UserRole;
import com.example.bookreviewapp.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthService authService;

    @Transactional
    public void update(long userId, UpdateRequestDto request, HttpServletRequest httpRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자 정보"));

        if(!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호 불일치");
        }

        if(request.hasNewEmail()) {
            user.changeEmail(request.getNewEmail());
        }

        if(request.hasNewPassword()) {
            user.changePassword(passwordEncoder.encode(request.getNewPassword()));
        }

        authService.logout(httpRequest, userId);
    }
}
