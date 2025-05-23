package com.example.bookreviewapp.domain.book.service;

import com.example.bookreviewapp.common.code.ErrorStatus;
import com.example.bookreviewapp.common.error.ApiException;
import com.example.bookreviewapp.domain.book.dto.response.BookDetailsResponseDto;
import com.example.bookreviewapp.domain.book.dto.response.BookResponseDto;
import com.example.bookreviewapp.domain.book.dto.response.BookViewedTop10ResponseDto;
import com.example.bookreviewapp.domain.book.entity.Book;
import com.example.bookreviewapp.domain.book.entity.EnrollStatus;
import com.example.bookreviewapp.domain.book.repository.BookRepository;
import com.example.bookreviewapp.domain.like.repository.LikeRepository;
import com.example.bookreviewapp.domain.review.repository.ReviewRepository;
import com.example.bookreviewapp.domain.user.entity.User;
import com.example.bookreviewapp.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final ReviewRepository reviewRepository;
    private final LikeRepository likeRepository;
    private final RedisTemplate<String, String> redisTemplate;

    // 도서 생성
    @Transactional
    public BookResponseDto createBook(Long userId, String title, String author, String category) {

        // 유저 정보 포함하여 도서 정보 등록
        User findUser = userRepository.findById(userId).orElseThrow(() -> new ApiException(ErrorStatus.USER_NOT_FOUND));

        Book newBook = new Book(findUser, title, author, category);

        Book savedBook = bookRepository.save(newBook);

        return new BookResponseDto(
                savedBook.getId(),
                savedBook.getTitle(),
                savedBook.getAuthor(),
                savedBook.getCategory(),
                savedBook.getCreatedAt(),
                savedBook.getUpdatedAt(),
                savedBook.getEnrollStatus()
        );
    }

    // 도서 목록 조회
    public Page<BookResponseDto> findAllBooks(Pageable pageable) {

        Page<Book> books = bookRepository.findAllByEnrollStatus(EnrollStatus.ACCEPT, pageable);

        // books.map(book -> BookResponseDto.from(book)); 와 동일한 표현
        return books.map(BookResponseDto::from);
    }

    // 도서 상세 조회
    @Transactional
    public BookDetailsResponseDto findByDetailsBook(Long id) {

        // 조회수할때 마다 1씩 증가
        bookRepository.increaseViewer(id);

        // 조회 수 캐싱 (Z-set 기록)
        redisTemplate.opsForZSet().incrementScore("book:view:ranking", "book:" + id, 1);

        Book findBook = bookRepository.findById(id).orElseThrow(() -> new ApiException(ErrorStatus.BOOK_NOT_FOUND));

        // 승인되지 않은 책이면 예외처리
        if (findBook.getEnrollStatus() != EnrollStatus.ACCEPT) {
            throw new ApiException(ErrorStatus.BOOK_NOT_APPROVED);
        }

        // 리뷰 평점
        Double rating = reviewRepository.averageScore(findBook.getId());

        // 리뷰 수 체크
        Long reviewCounts = reviewRepository.countByBookId(findBook.getId());

        // 좋아요 수 체크
        Long likeCounts = likeRepository.countByBookId(findBook.getId());

        return new BookDetailsResponseDto(
                findBook.getId(),
                findBook.getTitle(),
                findBook.getAuthor(),
                findBook.getCategory(),
                findBook.getCreatedAt(),
                findBook.getUpdatedAt(),
                findBook.getEnrollStatus(),
                rating == null ? 0.0 : rating,  // null 방지
                reviewCounts,
                likeCounts,
                findBook.getViewer()
        );
    }

    @Transactional
    public BookResponseDto editBook(Long id, String title, String author, String category) {

        // 도서 id 조회 ( 등록 여부 상관 X )
        Book findBook = bookRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorStatus.BOOK_NOT_FOUND));

        findBook.update(title, author, category);

        Book updatedBook = bookRepository.save(findBook);

        return new BookResponseDto(
                updatedBook.getId(),
                updatedBook.getTitle(),
                updatedBook.getAuthor(),
                updatedBook.getCategory(),
                updatedBook.getCreatedAt(),
                updatedBook.getUpdatedAt(),
                updatedBook.getEnrollStatus()
        );
    }

    // 도서 삭제
    @Transactional
    public void deleteBook(Long id) {

        // 도서 id 조회 ( 등록 여부 상관 X )
        Book findBook = bookRepository.findById(id).orElseThrow(() -> new ApiException(ErrorStatus.BOOK_NOT_FOUND));

        bookRepository.delete(findBook);
    }

    // 조회순 Top 10 조회
    @Transactional(readOnly = true)
    public List<BookViewedTop10ResponseDto> findTop10ViewBooks() {

        // 조회수 내림차순 상위 10개
        Set<String> topBookKeys = redisTemplate.opsForZSet().reverseRange("book:view:ranking", 0, 9);

        if (topBookKeys == null || topBookKeys.isEmpty()) {
            return List.of(); // 빈 리스트 반환
        }

        // "book:1" -> 1 추출 ( Redis 해당하는 id값 ), 문자열에서 id값만 추출해내는 작업
        List<Long> bookIds = topBookKeys.stream()
                .map(key -> Long.parseLong(key.replace("book:", "")))
                .collect(Collectors.toList());

        // DB 실제 책 정보 조회
        List<Book> books = bookRepository.findAllById(bookIds);

        // 승인된 책만 보이게 예외처리
        Map<Long, Book> bookMap = books.stream()
                .filter(book -> book.getEnrollStatus() == EnrollStatus.ACCEPT)
                .collect(Collectors.toMap(Book::getId, Function.identity()));
        // Collectors 는 Java Stream API 에서 스트림의 결과를 원하는 형태로 수집할 때 사용
        // Function.identity(): 객체 자체 (book)를 value 로 사용

        List<BookViewedTop10ResponseDto> result = new ArrayList<>();

        int rank = 1;
        for (Long bookId: bookIds) {
            Book book = bookMap.get(bookId);
            if (book != null) {
                result.add(BookViewedTop10ResponseDto.from(book, rank++));
            }
        }

        return result;
    }
}
