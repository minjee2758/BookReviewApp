package com.example.bookreviewapp.domain.book.service;

import com.example.bookreviewapp.domain.book.entity.Book;
import com.example.bookreviewapp.domain.book.entity.EnrollStatus;
import com.example.bookreviewapp.domain.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 조회가 가장 많은 책 Top10 자동으로 업데이트 해주는 클래스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BookViewedRankingScheduler {

    private final BookRepository bookRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @Scheduled(fixedRate = 1000 * 60 * 60) // 1000 = 1초 ( 1시간마다 업데이트 )
    public void updateTopViewedBooksIn() {
        log.info("[도서 가장 많은 조회순 시작]");

        // DB 승인된 도서 가장 많은 조회수 순 Top10 가져오기
        List<Book> topBooks = bookRepository.findTop10ByEnrollStatusOrderByViewerDesc(EnrollStatus.ACCEPT);

        // Redis 기존 데이터 삭제
        redisTemplate.delete("book:view:ranking");

        // 새로운 데이터 추가
        for (Book book : topBooks) {
            String key = "book:" + book.getId();
            redisTemplate.opsForZSet().add("book:view:ranking", key, book.getViewer());
        }
        log.info("[도서 가장 많은 조회순 완료]");
    }

}
