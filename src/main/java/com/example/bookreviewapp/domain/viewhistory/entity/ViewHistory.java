package com.example.bookreviewapp.domain.viewhistory.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name="viewhistory")
@NoArgsConstructor
public class ViewHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String category;

    public ViewHistory(Long userId, String category) {
        this.userId = userId;
        this.category = category;
    }
}
