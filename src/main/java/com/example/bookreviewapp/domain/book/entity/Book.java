package com.example.bookreviewapp.domain.book.entity;

import com.example.bookreviewapp.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Book extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String title;
}
