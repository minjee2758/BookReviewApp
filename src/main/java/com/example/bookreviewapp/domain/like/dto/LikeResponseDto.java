package com.example.bookreviewapp.domain.like.dto;

import com.example.bookreviewapp.domain.like.entity.Like;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class LikeResponseDto {
    private Long userId;
    private List<LikeInfoDto> likes;
    private int page;
    private int size;
    private int totalLikes;

    public static LikeResponseDto fromLiketoDto(Like like) {
        LikeInfoDto likeInfo = new LikeInfoDto(
                like.getBook().getId(),
                like.getBook().getTitle(),
                like.getLikedAt().toLocalTime()
        );
        UserLikesDto userLikes = new UserLikesDto(
                like.getUser().getId(),
                List.of(likeInfo),
                1, 1, 1 // 기본값. 페이징 응답이 아닐 경우
        );

        return new LikeResponseDto(userLikes, likeInfo);
    }
}
