package com.example.bookreviewapp.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateRequestDto {
    private String oldPassword;

    private String newEmail;

    private String newPassword;

    public boolean hasNewEmail() {
        return newEmail != null && !newEmail.isBlank();
    }

    public boolean hasNewPassword() {
        return newPassword != null && !newPassword.isBlank();
    }
}
