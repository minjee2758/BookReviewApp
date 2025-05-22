package com.example.bookreviewapp.domain.viewhistory.repository;

import com.example.bookreviewapp.domain.viewhistory.entity.ViewHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ViewHistoryRepository extends JpaRepository<ViewHistory, Long> {
}
