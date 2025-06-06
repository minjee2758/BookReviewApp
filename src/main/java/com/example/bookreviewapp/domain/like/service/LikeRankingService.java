package com.example.bookreviewapp.domain.like.service;

import com.example.bookreviewapp.domain.book.repository.BookRepository;
import com.example.bookreviewapp.domain.like.dto.response.AuthorRankingResponseDto;
import com.example.bookreviewapp.domain.like.dto.response.BookRankingResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class LikeRankingService {

    @Qualifier("redisTemplateForObject")
    private final RedisTemplate<String, Object> redisTemplate;
    private final BookRepository bookRepository;

    private static final Duration TTL = Duration.ofMinutes(60);

    private static final String BOOK_SCORE_KEY = "book:like:ranking";
    private static final String AUTHOR_SCORE_KEY = "author:like:ranking";

    private static final String BOOK_CACHE_KEY = "ranking:book";
    private static final String AUTHOR_CACHE_KEY = "ranking:author";

    // 책 좋아요 점수 증가
    public void likeBook(String bookId, String authorName) {
        redisTemplate.opsForZSet().incrementScore(BOOK_SCORE_KEY, bookId, 1);
        redisTemplate.opsForZSet().incrementScore(AUTHOR_SCORE_KEY, authorName, 1);

        // 캐시 무효화
        redisTemplate.delete(BOOK_CACHE_KEY);
        redisTemplate.delete(AUTHOR_CACHE_KEY);
    }

    // 책 좋아요 점수 감소 (0 이하 시 제거)
    public void unlikeBook(String bookId, String authorName) {
        Double bookScore = redisTemplate.opsForZSet().incrementScore(BOOK_SCORE_KEY, bookId, -1);
        if (bookScore != null && bookScore <= 0) {
            redisTemplate.opsForZSet().remove(BOOK_SCORE_KEY, bookId);
        }

        Double authorScore = redisTemplate.opsForZSet().incrementScore(AUTHOR_SCORE_KEY, authorName, -1);
        if (authorScore != null && authorScore <= 0) {
            redisTemplate.opsForZSet().remove(AUTHOR_SCORE_KEY, authorName);
        }

        // 캐시 무효화
        redisTemplate.delete(BOOK_CACHE_KEY);
        redisTemplate.delete(AUTHOR_CACHE_KEY);
    }

    // 책 랭킹 조회 (캐시 적용)
    public List<BookRankingResponseDto> getTop10BooksWithTitle() {
        List<BookRankingResponseDto> cached = (List<BookRankingResponseDto>) redisTemplate.opsForValue().get(BOOK_CACHE_KEY);
        if (cached != null) return cached;

        Set<ZSetOperations.TypedTuple<Object>> topBooks = redisTemplate.opsForZSet()
                .reverseRangeWithScores(BOOK_SCORE_KEY, 0, 9);

        if (topBooks == null) return List.of();

        List<ZSetOperations.TypedTuple<Object>> topBooksList = new ArrayList<>(topBooks);

        List<BookRankingResponseDto> ranking = IntStream.range(0, topBooksList.size())
                .mapToObj(i -> {
                    ZSetOperations.TypedTuple<Object> tuple = topBooksList.get(i);
                    Long bookId = Long.parseLong((String) tuple.getValue());
                    int score = tuple.getScore() != null ? (int) Math.round(tuple.getScore()) : 0;
                    int rank = i + 1;
                    return bookRepository.findById(bookId)
                            .map(book -> new BookRankingResponseDto(rank, book.getTitle(), score))
                            .orElse(null);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        redisTemplate.opsForValue().set(BOOK_CACHE_KEY, ranking, TTL);
        return ranking;
    }

    // 작가 랭킹 조회 (캐시 적용)
    public List<AuthorRankingResponseDto> getTop10Authors() {
        List<AuthorRankingResponseDto> cached = (List<AuthorRankingResponseDto>) redisTemplate.opsForValue().get(AUTHOR_CACHE_KEY);
        if (cached != null) return cached;

        Set<ZSetOperations.TypedTuple<Object>> topAuthors = redisTemplate.opsForZSet()
                .reverseRangeWithScores(AUTHOR_SCORE_KEY, 0, 9);

        if (topAuthors == null) return List.of();

        List<ZSetOperations.TypedTuple<Object>> topAuthorsList = new ArrayList<>(topAuthors);

        List<AuthorRankingResponseDto> ranking = IntStream.range(0, topAuthorsList.size())
                .mapToObj(i -> {
                    var tuple = topAuthorsList.get(i);
                    String authorName = (String) tuple.getValue();
                    int score = tuple.getScore() != null ? (int) Math.round(tuple.getScore()) : 0;
                    int rank = i + 1;
                    return new AuthorRankingResponseDto(rank, authorName, score);
                })
                .collect(Collectors.toList());

        redisTemplate.opsForValue().set(AUTHOR_CACHE_KEY, ranking, TTL);
        return ranking;
    }

    // 매일 자정 스케줄러가 호출할 랭킹 캐시 갱신 메서드
    @Transactional
    public void refreshRankingCaches() {
        // 책 랭킹 캐시 갱신
        getTop10BooksWithTitle();
        // 작가 랭킹 캐시 갱신
        getTop10Authors();
    }

}
