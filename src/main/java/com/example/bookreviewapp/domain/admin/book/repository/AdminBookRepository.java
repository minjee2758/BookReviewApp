package com.example.bookreviewapp.domain.admin.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bookreviewapp.domain.book.entity.Book;

@Repository
public interface AdminBookRepository extends JpaRepository<Book, Long> {

}