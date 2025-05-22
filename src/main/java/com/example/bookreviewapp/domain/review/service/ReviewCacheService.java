package com.example.bookreviewapp.domain.review.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewCacheService {

	private final ReviewService reviewService;

	// 10분마다 캐시 갱신
	@Scheduled(fixedRate = 600_000)
	@CacheEvict(value = "popularReviews", key = "'top5'")
	public void refreshPopularReviewsCache() {
		reviewService.getPopularReviews(); // 캐시 재생성
	}
}
