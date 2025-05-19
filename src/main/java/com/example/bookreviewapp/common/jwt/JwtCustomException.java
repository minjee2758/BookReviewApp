package com.example.bookreviewapp.common.jwt;


import com.example.bookreviewapp.common.code.ErrorStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class JwtCustomException extends RuntimeException {
    private final ErrorStatus errorStatus;

    public JwtCustomException(ErrorStatus errorStatus) {
        super(errorStatus.getMessage());
        this.errorStatus = errorStatus;
    }

    public ErrorStatus getErrorStatus() {
        return errorStatus;
    }
}
