package com.example.bookreviewapp.domain.like.service;

import com.example.bookreviewapp.common.code.ErrorStatus;
import com.example.bookreviewapp.common.error.ApiException;
import com.example.bookreviewapp.domain.book.entity.Book;
import com.example.bookreviewapp.domain.book.repository.BookRepository;
import com.example.bookreviewapp.domain.like.dto.LikeRequestDto;
import com.example.bookreviewapp.domain.like.dto.LikeResponseDto;
import com.example.bookreviewapp.domain.like.dto.UserLikesResponseDto;
import com.example.bookreviewapp.domain.like.entity.Like;
import com.example.bookreviewapp.domain.like.repository.LikeRepository;
import com.example.bookreviewapp.domain.user.entity.User;
import com.example.bookreviewapp.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public void saveLike(Long userId, LikeRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorStatus.USER_NOT_FOUND));

        Book book = bookRepository.findById(requestDto.getId())
                .orElseThrow(() -> new ApiException(ErrorStatus.BOOK_NOT_FOUND));

        // 중복 체크
        boolean exists = likeRepository.existsByUserAndBook(user, book);
        if (exists) {
            throw new ApiException(ErrorStatus.LIKE_ALREADY_EXISTS); // 중복 좋아요 예외
        }

        Like like = new Like(user, book, LocalDateTime.now());
        likeRepository.save(like);
    }

    public UserLikesResponseDto getUserLikes(Long userId, Pageable pageable) {
        Page<Like> likePage = likeRepository.findAllByUserId(userId, pageable);

        List<LikeResponseDto> likeDtoList = likePage.getContent().stream()
                .map(LikeResponseDto::fromLiketoDto)
                .collect(Collectors.toList());

        return new UserLikesResponseDto(
                userId,
                likeDtoList,
                pageable.getPageNumber() + 1,
                pageable.getPageSize(),
                likePage.getTotalElements()
        );
    }

    public void deleteLike(Long userId, LikeRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorStatus.USER_NOT_FOUND));

        Book book = bookRepository.findById(requestDto.getId())
                .orElseThrow(() -> new ApiException(ErrorStatus.BOOK_NOT_FOUND));


        Like like = likeRepository.findByUserAndBook(user, book)
                .orElseThrow(() -> new ApiException(ErrorStatus.LIKE_NOT_FOUND));

        likeRepository.delete(like);
    }

}
