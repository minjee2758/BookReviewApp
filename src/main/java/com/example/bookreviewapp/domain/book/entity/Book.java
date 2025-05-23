package com.example.bookreviewapp.domain.book.entity;

import com.example.bookreviewapp.common.entity.BaseEntity;
import com.example.bookreviewapp.domain.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "book")
@NoArgsConstructor
public class Book extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
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

	@Column(nullable = false)
	private Long viewer;

	public Book(User user, String title, String author, String category) {
		this.user = user;
		this.title = title;
		this.author = author;
		this.category = category;
		this.viewer = 0L;
	}

	// 수정 메소드
	public void update(String title, String author, String category) {
		this.title = title;
		this.author = author;
		this.category = category;
	}

	// 승인 메소드
	public void acceptEnroll() {
		this.enrollStatus = EnrollStatus.ACCEPT;
	}

}
