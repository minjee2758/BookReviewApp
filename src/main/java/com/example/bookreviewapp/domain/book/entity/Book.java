package com.example.bookreviewapp.domain.book.entity;

import com.example.bookreviewapp.common.entity.BaseEntity;
import com.example.bookreviewapp.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name= "book")
@NoArgsConstructor
public class Book extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnrollStatus enrollStatus = EnrollStatus.REJECT;

    public Book(String title, String author, String category) {
        this.title = title;
        this.author = author;
        this.category = category;
    }

    // 승인 메소드
    public void acceptEnroll() {
        this.enrollStatus = EnrollStatus.ACCEPT;
    }
}
