package com.example.bookreviewapp.domain.like.aop;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * Redis의 setIfAbsent (SETNX)를 활용한 간단한 분산 락
*/
@Service
@RequiredArgsConstructor
public class RedisLockService {

    private final StringRedisTemplate redisTemplate;
    private static final long LOCK_TIMEOUT = 3_000; // 3초

    public boolean lock(String key) {
        Boolean success = redisTemplate.opsForValue().setIfAbsent("lock:" + key, "locked", Duration.ofMillis(LOCK_TIMEOUT));
        return Boolean.TRUE.equals(success);
    }

    public void unlock(String key) {
        redisTemplate.delete("lock:" + key);
    }
}
