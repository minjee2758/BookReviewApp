package com.example.bookreviewapp.domain.review.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.bookreviewapp.domain.review.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
	Page<Review> findByBookId(Long bookId, Pageable pageable);

	Page<Review> findByUserId(Long userId, Pageable pageable);

	// 평점
	@Query("SELECT AVG(r.score) FROM Review r WHERE r.book.id = :bookId")
	Double averageScore(Long bookId);

	// 리뷰 수 체크
	Long countByBookId(Long bookId);

	//조회수 +1
	@Modifying
	@Query("UPDATE Review r SET r.viewer = r.viewer + 1 WHERE r.id = :id")
	void increaseViewer(@Param("id") Long id);

	//인기있는 리뷰 id를 리스트로 조회
	List<Review> findByIdIn(List<Long> ids);
}


