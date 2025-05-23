package com.example.bookreviewapp.domain.review.entity;

import com.example.bookreviewapp.common.entity.BaseEntity;
import com.example.bookreviewapp.domain.book.entity.Book;
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

@Getter
@Entity
@NoArgsConstructor
@Table(name = "review")
public class Review extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "book_id", nullable = false)
	private Book book;

	private String content;

	private Integer score;

	@Enumerated(EnumType.STRING)
	private PinStatus isPinned = PinStatus.UNPINNED;

	//리뷰 조회수
	@Column(nullable = false)
	private Long viewer;

	//핀 설정
	public void pinned() {
		this.isPinned = PinStatus.PINNED;
	}

	//핀 해제
	public void unpinned() {
		this.isPinned = PinStatus.UNPINNED;
	}

	public Review(User user, Book book, String content, Integer score, PinStatus isPinned) {
		this.user = user;
		this.book = book;
		this.content = content;
		this.score = score;
		this.isPinned = isPinned;
		this.viewer = 0L;
	}

	//리뷰 수정시
	public void updateReview(String content, Integer score) {
		this.content = content;
		this.score = score;
	}
}