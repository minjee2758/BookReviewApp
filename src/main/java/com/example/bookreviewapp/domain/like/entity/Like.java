package com.example.bookreviewapp.domain.like.entity;

import java.time.LocalDateTime;

import com.example.bookreviewapp.domain.book.entity.Book;
import com.example.bookreviewapp.domain.user.entity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor

@Table(name = "`like`")

public class Like {

	@Id
	@GeneratedValue
	private Long id;

	@Setter
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Setter
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "book_id")
	private Book book;

	private LocalDateTime likedAt;

	public Like(User user, Book book, LocalDateTime now) {
		this.user = user;
		this.book = book;
		this.likedAt = likedAt;
	}
}
