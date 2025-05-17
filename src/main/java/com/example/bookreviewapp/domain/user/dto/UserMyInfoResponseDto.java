package com.example.bookreviewapp.domain.user.dto;

import com.example.bookreviewapp.domain.user.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserMyInfoResponseDto {
    private String email;

    private UserRole userROle;

}
