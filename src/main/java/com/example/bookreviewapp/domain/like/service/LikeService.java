package com.example.bookreviewapp.domain.like.service;

import com.example.bookreviewapp.common.code.ErrorStatus;
import com.example.bookreviewapp.common.error.ApiException;
import com.example.bookreviewapp.domain.book.entity.Book;
import com.example.bookreviewapp.domain.book.repository.BookRepository;
import com.example.bookreviewapp.domain.like.dto.LikeRequestDto;
import com.example.bookreviewapp.domain.like.dto.LikeResponseDto;
import com.example.bookreviewapp.domain.like.entity.Like;
import com.example.bookreviewapp.domain.like.repository.LikeRepository;
import com.example.bookreviewapp.domain.user.entity.User;
import com.example.bookreviewapp.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public void saveLike(Long userId ,LikeRequestDto requestDto){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorStatus.USER_NOT_FOUND));

        Book book = bookRepository.findById(requestDto.getId())
                .orElseThrow(() -> new ApiException(ErrorStatus.BOOK_NOT_FOUND));


        Like like = new Like(user, book, LocalDateTime.now());
        likeRepository.save(like);

    }

    public Page<LikeResponseDto> getAllLists(Long userId, int page, int size) {

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").ascending());
        Page<Like> likePage = likeRepository.findAllByUserId(userId, pageable);
        return likePage.map(LikeResponseDto::fromLiketoDto);

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
