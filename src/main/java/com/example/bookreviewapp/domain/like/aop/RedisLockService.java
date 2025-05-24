package com.example.bookreviewapp.domain.like.aop;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RedisLockService {


    private final StringRedisTemplate stringRedisTemplate;
    private static final Duration LOCK_DURATION = Duration.ofSeconds(3); // Deadlock 방지

    /**
     * 락을 시도하고 성공 시 UUID 반환, 실패 시 null 반환
     */
    public String lock(String key) {
        String uuid = UUID.randomUUID().toString();
        Boolean success = stringRedisTemplate
                .opsForValue()
                .setIfAbsent(key, uuid, LOCK_DURATION);

        return Boolean.TRUE.equals(success) ? uuid : null;
    }

    /**
     * 락 해제 – 자신이 획득한 락인 경우에만 삭제
     */
    public void unlock(String key, String lockId) {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        String currentValue = ops.get(key);

        if (lockId.equals(currentValue)) {
            stringRedisTemplate.delete(key);
        }
    }
}