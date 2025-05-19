package com.example.bookreviewapp.domain.like.repository;

import com.example.bookreviewapp.domain.like.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {

}
