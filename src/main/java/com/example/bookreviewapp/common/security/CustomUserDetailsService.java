package com.example.bookreviewapp.common.security;

import com.example.bookreviewapp.common.code.ErrorStatus;
import com.example.bookreviewapp.common.jwt.JwtCustomException;
import com.example.bookreviewapp.domain.user.entity.User;
import com.example.bookreviewapp.domain.user.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Getter
@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new JwtCustomException(ErrorStatus.USER_NOT_FOUND));

        return new CustomUserDetails(user.getId(), user.getEmail(), user.getUserRole());
    }
}

