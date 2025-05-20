package com.example.bookreviewapp.domain.like.entity;

import java.time.LocalDateTime;

import com.example.bookreviewapp.domain.book.entity.Book;
import com.example.bookreviewapp.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "`like`")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    private LocalDateTime likedAt;


    public Like(User user, Book book, LocalDateTime likedAt) {
        this.user = user;
        this.book = book;
        this.likedAt = likedAt;
    }
}
