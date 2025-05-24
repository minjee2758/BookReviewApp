package com.example.bookreviewapp.domain.like.service;

import com.example.bookreviewapp.domain.book.repository.BookRepository;
import com.example.bookreviewapp.domain.like.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeRankingSchedular {


    private final LikeRankingService likeRankingService;

    @Transactional
    @Scheduled(cron = "0 0 0 * * *") // 매일 자정 00:00:00
    public void updateBookAndAuthorLikeRanking() {
        likeRankingService.refreshRankingCaches();
    }

}
