package com.example.bookreviewapp.domain.review.service;

import java.util.List;
import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisService {
	private final RedisTemplate<String, String> redisTemplate;

	private static final String REVIEW_VIEW_KEY = "review:viewRank";

	public void increaseReviewViewCount(Long reviewId) {
		redisTemplate.opsForZSet().incrementScore(REVIEW_VIEW_KEY, reviewId.toString(), 1);
	}

	public List<Long> getTop5ReviewIds() {
		Set<String> result = redisTemplate.opsForZSet()
			.reverseRange(REVIEW_VIEW_KEY, 0, 4);
		if (result == null)
			return List.of();
		return result.stream().map(Long::parseLong).toList();
	}
}
