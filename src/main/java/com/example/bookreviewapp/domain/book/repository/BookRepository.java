package com.example.bookreviewapp.domain.book.repository;

import com.example.bookreviewapp.domain.book.entity.Book;
import com.example.bookreviewapp.domain.book.entity.EnrollStatus;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Modifying // 없으면 UPDATE / DELETE 쿼리가 실행이 안됨.
    @Query("UPDATE Book b SET b.viewer = b.viewer + 1 WHERE b.id = :id")
    void increaseViewer(@Param("id") Long id);

    Page<Book> findAllByEnrollStatus(EnrollStatus enrollStatus, Pageable pageable);

    List<Book> findTop10ByEnrollStatusOrderByViewerDesc(EnrollStatus enrollStatus);


}
