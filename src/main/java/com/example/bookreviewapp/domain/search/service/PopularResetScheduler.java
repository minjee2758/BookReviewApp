package com.example.bookreviewapp.domain.search.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PopularResetScheduler {
	private final RedisTemplate<String, String> redisTemplate;

	@Scheduled(cron = "0 0 0 * * *")
	public void resetDailyPopular() {
		redisTemplate.delete(SearchService.ZSET_KEY);
	}
}
