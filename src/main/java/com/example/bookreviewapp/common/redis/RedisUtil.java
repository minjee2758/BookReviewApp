package com.example.bookreviewapp.common.redis;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.time.Duration;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
@Component
public class RedisUtil {

    private final StringRedisTemplate redisTemplate;

    // redis 에 내가 조회한 책 카테고리 저장
    public void addViewHistoryCategory(Long userId, String category) {
        String key = "user:view:category:" + userId;
        redisTemplate.opsForZSet().incrementScore(key, category, 1.0);

        Long ttl = redisTemplate.getExpire(key);

        // ttl 이 없으면 ttl 설정, 있으면 재설정x(무한 새로고침으로 반영구 가능)
        if(ttl == null || ttl == -1) {
            redisTemplate.expire(key, 3, TimeUnit.DAYS);
        }
    }

    public void addViewHistoryBookId(Long userId, Long bookId) {
        String key = "user:view:id:" + userId;
        redisTemplate.opsForSet().add(key, String.valueOf(bookId));

        Long ttl = redisTemplate.getExpire(key);

        // ttl 이 없으면 ttl 설정, 있으면 재설정x(무한 새로고침으로 반영구 가능)
        if(ttl == null || ttl == -1) {
            redisTemplate.expire(key, 3, TimeUnit.DAYS);
        }
    }

    // redis 에 저장된 내가 조회한 카테고리 중 가장 높은 조회수 카테고리 반환
    public String getTopCategory(long userId) {
        String key = "user:view:category:" + userId;

        Set<String> result = redisTemplate.opsForZSet().reverseRange(key, 0, 0);

        return result.iterator().next();
    }

    public Set<Long> getViewedBookIds(Long userId) {
        String key = "user:view:id:" + userId;
        return redisTemplate.opsForSet().members(key).stream()
                .map(Long::valueOf)
                .collect(Collectors.toSet());
    }

}
