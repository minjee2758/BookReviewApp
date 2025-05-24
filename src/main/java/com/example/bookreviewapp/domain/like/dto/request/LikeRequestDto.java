package com.example.bookreviewapp.domain.like.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LikeRequestDto {
    @JsonProperty("bookId")
    private Long id;
}
