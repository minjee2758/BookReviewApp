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
import java.util.Set;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Modifying // 없으면 UPDATE / DELETE 쿼리가 실행이 안됨.
    @Query("UPDATE Book b SET b.viewer = b.viewer + 1 WHERE b.id = :id")
    void increaseViewer(@Param("id") Long id);

    Page<Book> findAllByEnrollStatus(EnrollStatus enrollStatus, Pageable pageable);

    @Query("SELECT b FROM Book b LEFT JOIN Review r ON r.book = b WHERE b.category = :category AND b.enrollStatus = 'ACCEPT' AND b.id NOT IN :viewedBookIds GROUP BY b ORDER BY COUNT(r.id) desc")
    Page<Book> findBooksByCategory(@Param("category") String category, @Param("viewedBookIds") Set<Long> viewedBookIds, Pageable pageable);

    List<Book> findTop10ByEnrollStatusOrderByViewerDesc(EnrollStatus enrollStatus);

}
