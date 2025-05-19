package com.example.bookreviewapp.domain.like.repository;

import com.example.bookreviewapp.domain.book.entity.Book;
import com.example.bookreviewapp.domain.like.entity.Like;
import com.example.bookreviewapp.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    // 페이징 처리 포함 시
    Page<Like> findAllByUserId(Long userId, Pageable pageable);

    Optional<Like> findByUserAndBook(User user, Book book);
}
