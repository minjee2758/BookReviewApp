package com.example.bookreviewapp.domain.like.repository;

import com.example.bookreviewapp.domain.book.entity.Book;
import com.example.bookreviewapp.domain.like.entity.Like;
import com.example.bookreviewapp.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    // 페이징 처리 포함 시
    Page<Like> findAllByUserId(Long userId, Pageable pageable);

    Optional<Like> findByUserAndBook(User user, Book book);

    // 좋아요 중복 확인
    boolean existsByUserAndBook(User user, Book book);

    // 좋아요 수 체크
    Long countByBookId(Long bookId);


    @Query("SELECT l.book.id, COUNT(l) FROM Like l GROUP BY l.book.id ORDER BY COUNT(l) DESC")
    List<Object[]> findTop10BooksByLikeCount(Pageable pageable);

    @Query("SELECT l.book.author, COUNT(l) FROM Like l GROUP BY l.book.author ORDER BY COUNT(l) DESC")
    List<Object[]> findTop10AuthorsByBookLikeCount(Pageable pageable);


}
