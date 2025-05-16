package com.example.bookreviewapp.domain.auth.service;

import com.example.bookreviewapp.domain.user.entity.User;
import com.example.bookreviewapp.domain.user.entity.UserRole;
import com.example.bookreviewapp.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public void signUp(String email, String password, UserRole userRole) {
        String encodePassword = passwordEncoder.encode(password);

        User saveUser = new User(email, encodePassword, userRole);

        userRepository.save(saveUser);
    }

}
