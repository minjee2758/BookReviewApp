package com.example.bookreviewapp.domain.review.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.bookreviewapp.domain.review.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
	Page<Review> findByBookId(Long bookId, Pageable pageable);

	// 평점
	@Query("SELECT AVG(r.score) FROM Review r WHERE r.book.id = :bookId")
	Double averageScore(Long bookId);

	// 리뷰 수 체크
	Long countByBookId(Long bookId);
}
