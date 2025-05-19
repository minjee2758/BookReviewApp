package com.example.bookreviewapp.common.jwt;

import com.example.bookreviewapp.common.code.ErrorStatus;
import com.example.bookreviewapp.common.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// filter는 return 값을 써서 클라이언트 응답으로 전달할수 없다

public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        try{
            chain.doFilter(request, response);
        } catch (JwtCustomException e) {
            ErrorStatus errorStatus = e.getErrorStatus();

            response.setStatus(errorStatus.getHttpStatus().value());
            response.setContentType("application/json;charset=UTF-8");

            ApiResponse<Void> apiResponse = new ApiResponse<>(
                    false,
                    errorStatus.getCode(),
                    errorStatus.getMessage(),
                    null);

            ObjectMapper objectMapper = new ObjectMapper();

            String json = objectMapper.writeValueAsString(apiResponse);

            response.getWriter().write(json);
        }
    }
}
