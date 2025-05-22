package com.example.bookreviewapp.domain.viewhistory.service;

import com.example.bookreviewapp.common.redis.RedisUtil;
import com.example.bookreviewapp.domain.book.dto.response.BookResponseDto;
import com.example.bookreviewapp.domain.book.entity.Book;
import com.example.bookreviewapp.domain.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ViewHistoryService {

    private final RedisUtil redisUtil;

    private final BookRepository bookRepository;

    public Page<BookResponseDto> getRecommend(Long userId, Pageable pageable) {
        String topCategory = redisUtil.getTopCategory(userId);

        System.out.println(redisUtil.getTime(userId));

        return bookRepository.findBooksByCategory(topCategory, pageable)
                .map(BookResponseDto::from);
    }


}
