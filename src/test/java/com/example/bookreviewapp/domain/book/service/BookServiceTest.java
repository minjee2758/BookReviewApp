package com.example.bookreviewapp.domain.book.service;

import com.example.bookreviewapp.common.redis.RedisUtil;
import com.example.bookreviewapp.domain.book.dto.response.BookViewedTop10ResponseDto;
import com.example.bookreviewapp.domain.book.entity.Book;
import com.example.bookreviewapp.domain.book.entity.EnrollStatus;
import com.example.bookreviewapp.domain.book.repository.BookRepository;
import com.example.bookreviewapp.domain.like.repository.LikeRepository;
import com.example.bookreviewapp.domain.review.repository.ReviewRepository;
import com.example.bookreviewapp.domain.user.entity.User;
import com.example.bookreviewapp.domain.user.repository.UserRepository;
import com.example.bookreviewapp.domain.viewhistory.repository.ViewHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private BookRepository bookRepository;
    @Mock private ReviewRepository reviewRepository;
    @Mock private LikeRepository likeRepository;
    @Mock private ViewHistoryRepository viewHistoryRepository;
    @Mock private RedisUtil redisUtil;
    @Mock private RedisTemplate<String, String> redisTemplate;
    @Mock private ZSetOperations<String, String> zSetOperations;

    private BookService bookService;

    @BeforeEach
    void setUp() {
        bookService = new BookService(
                userRepository,
                bookRepository,
                reviewRepository,
                likeRepository,
                viewHistoryRepository,
                redisUtil,
                redisTemplate
        );
    }

    @Test
    void 도서_생성() {
        // given
        Book book = new Book(null, "제목1", "저자1", "액션");
        setField(book, "id", 10L); // book id도 강제 설정

        // when
        bookRepository.save(book);

        // then
        verify(bookRepository).save(book);
        assertThat(book.getTitle()).isEqualTo("제목1");
        assertThat(book.getAuthor()).isEqualTo("저자1");
        assertThat(book.getCategory()).isEqualTo("액션");
    }

    @Test
    void 도서_목록조회() {
        // given
        Book book = new Book(null, "제목1", "저자1", "액션");
        setField(book, "id", 1L);

        // when
        bookRepository.findAllByEnrollStatus(EnrollStatus.ACCEPT, Pageable.ofSize(10));

        // then
        verify(bookRepository).findAllByEnrollStatus(EnrollStatus.ACCEPT, Pageable.ofSize(10));
    }

    @Test
    void 도서_수정() {
        // given
        Book book = new Book(null, "기존 제목", "기존 저자", "액션");
        setField(book, "id", 1L);

        // when
        book.update("수정 제목", "수정 저자", "판타지");
        bookRepository.save(book);

        // then
        assertThat(book.getTitle()).isEqualTo("수정 제목");
        assertThat(book.getAuthor()).isEqualTo("수정 저자");
        assertThat(book.getCategory()).isEqualTo("판타지");
    }

    @Test
    void 도서_삭제() {
        // given
        Book book = new Book(null, "제목1", "저자1", "액션");
        setField(book, "id", 1L);

        // when
        bookRepository.delete(book);

        // then
        verify(bookRepository).delete(book);// delete가 한 번 호출되었는지 확인
    }


    @Test
    void 조회가_가장많은_top10() {
        // given
        Set<String> redisTopBooks = new LinkedHashSet<>(Arrays.asList("book:1", "book:2"));
        when(redisTemplate.opsForZSet()).thenReturn(zSetOperations);
        when(zSetOperations.reverseRange("book:view:ranking", 0, 9)).thenReturn(redisTopBooks);

        User user = mock(User.class);

        Book book1 = new Book(user, "title1", "author1", "category1");
        setField(book1, "id", 1L);
        setField(book1, "enrollStatus", EnrollStatus.ACCEPT);

        Book book2 = new Book(user, "title2", "author2", "category2");
        setField(book2, "id", 2L);
        setField(book2, "enrollStatus", EnrollStatus.ACCEPT);

        when(bookRepository.findAllById(Arrays.asList(1L, 2L)))
                .thenReturn(List.of(book1, book2));

        // when
        List<BookViewedTop10ResponseDto> result = bookService.findTop10ViewBooks();

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getRank()).isEqualTo(1);
        assertThat(result.get(0).getTitle()).isEqualTo("title1");
        assertThat(result.get(1).getRank()).isEqualTo(2);
        assertThat(result.get(1).getTitle()).isEqualTo("title2");
    }

    // setter 가 없는 엔티티에 값을 강제로 주입 ( 리플렉션 )
    public static void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException("리플렉션 필드 설정 실패: " + fieldName, e);
        }
    }
}
