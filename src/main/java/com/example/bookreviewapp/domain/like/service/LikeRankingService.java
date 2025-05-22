package com.example.bookreviewapp.domain.like.service;

import com.example.bookreviewapp.domain.book.repository.BookRepository;
import com.example.bookreviewapp.domain.like.dto.response.AuthorRankingResponseDto;
import com.example.bookreviewapp.domain.like.dto.response.BookRankingResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeRankingService {

    private final StringRedisTemplate redisTemplate;
    private final BookRepository bookRepository;

    private static final String BOOK_LIKE_KEY = "book:like:ranking";
    private static final String AUTHOR_LIKE_KEY = "author:like:ranking";

    // 책 좋아요 점수 증가
    public void likeBook(String bookId, String authorName) {
        redisTemplate.opsForZSet().incrementScore(BOOK_LIKE_KEY, bookId, 1);
        redisTemplate.opsForZSet().incrementScore(AUTHOR_LIKE_KEY, authorName, 1);
    }

    // 책 좋아요 점수 감소 (0 이하 시 제거)
    public void unlikeBook(String bookId, String authorName) {
        Double bookScore = redisTemplate.opsForZSet().incrementScore(BOOK_LIKE_KEY, bookId, -1);
        if (bookScore != null && bookScore <= 0) {
            redisTemplate.opsForZSet().remove(BOOK_LIKE_KEY, bookId);
        }

        Double authorScore = redisTemplate.opsForZSet().incrementScore(AUTHOR_LIKE_KEY, authorName, -1);
        if (authorScore != null && authorScore <= 0) {
            redisTemplate.opsForZSet().remove(AUTHOR_LIKE_KEY, authorName);
        }
    }

    public List<BookRankingResponseDto> getTop10BooksWithTitle() {
        Set<ZSetOperations.TypedTuple<String>> topBooks = redisTemplate.opsForZSet()
                .reverseRangeWithScores(BOOK_LIKE_KEY, 0, 9);

        if (topBooks == null) return List.of();

        return topBooks.stream()
                .map(tuple -> {
                    Long bookId = Long.parseLong(tuple.getValue());
                    int score = tuple.getScore() != null ? (int) Math.round(tuple.getScore()) : 0;

                    return bookRepository.findById(bookId)
                            .map(book -> new BookRankingResponseDto(book.getTitle(), score))
                            .orElse(null);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<AuthorRankingResponseDto> getTop10Authors() {
        Set<ZSetOperations.TypedTuple<String>> topAuthors = redisTemplate.opsForZSet()
                .reverseRangeWithScores(AUTHOR_LIKE_KEY, 0, 9);

        if (topAuthors == null) return List.of();

        return topAuthors.stream()
                .map(tuple -> new AuthorRankingResponseDto(
                        tuple.getValue(),
                        tuple.getScore() != null ? (int) Math.round(tuple.getScore()) : 0
                ))
                .collect(Collectors.toList());
    }
}
