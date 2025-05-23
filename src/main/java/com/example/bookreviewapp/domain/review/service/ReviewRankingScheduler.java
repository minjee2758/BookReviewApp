package com.example.bookreviewapp.domain.review.service;

import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.bookreviewapp.domain.review.entity.Review;
import com.example.bookreviewapp.domain.review.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewRankingScheduler {
	private final ReviewRepository reviewRepository;
	private final RedisTemplate<String, String> redisTemplate;

	@Scheduled(fixedRate = 1000 * 60 * 60 * 24) //하루마다 업뎃
	public void updateTopViewedBooksIn() {
		List<Review> topReviews = reviewRepository.findTop5ByOrderByViewerDesc();

		redisTemplate.delete("review:view:ranking");

		for (Review review : topReviews) {
			String key = "review:" + review.getId();
			redisTemplate.opsForZSet().add("review:view:ranking", key, review.getViewer());
		}
	}
}
