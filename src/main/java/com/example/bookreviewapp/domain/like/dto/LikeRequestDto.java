package com.example.bookreviewapp.domain.like.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LikeRequestDto {

    @JsonProperty("bookId")
    private Long id;

}
