package com.example.bookreviewapp.domain.user.service;

import com.example.bookreviewapp.common.code.ErrorStatus;
import com.example.bookreviewapp.common.error.ApiException;
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
                .orElseThrow(() -> new ApiException(ErrorStatus.USER_NOT_FOUND));

        if(!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new ApiException(ErrorStatus.USER_NOT_MATCH);
        }

        if(request.hasNewEmail()) {
            user.changeEmail(request.getNewEmail());
        }

        if(request.hasNewPassword()) {
            user.changePassword(passwordEncoder.encode(request.getNewPassword()));
        }

        authService.logout(httpRequest, userId);
    }

    @Transactional
    public void delete(Long userId, HttpServletRequest httpRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorStatus.USER_NOT_FOUND));

        user.deactivate();

        authService.logout(httpRequest, userId);
    }
}
