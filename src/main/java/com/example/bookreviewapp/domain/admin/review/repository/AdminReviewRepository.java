package com.example.bookreviewapp.domain.admin.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bookreviewapp.domain.review.entity.Review;

public interface AdminReviewRepository extends JpaRepository<Review, Long> {
}
