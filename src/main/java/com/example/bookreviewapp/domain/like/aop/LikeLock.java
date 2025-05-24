package com.example.bookreviewapp.domain.like.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 특정 파라미터 조합에 대해 락을 걸어 중복 요청 방지
*/

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LikeLock {
    String key(); // 예: "#userId + ':' + #requestDto.id"
}