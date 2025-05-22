package com.example.bookreviewapp.domain.search.service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import com.example.bookreviewapp.domain.search.dto.PopularKeywordDto;
import com.example.bookreviewapp.domain.search.dto.SearchResponseDto;
import com.example.bookreviewapp.domain.search.repository.SearchRepositoryCustom;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchService {
	private final SearchRepositoryCustom searchRepository;
	private final RedisTemplate<String, String> redisTemplate;

	public static final String ZSET_KEY = "search:popular";

	public SearchResponseDto search(String keyword,
		Pageable bookPageable,
		Pageable reviewPageable) {

		Double score = redisTemplate.opsForZSet()
			.incrementScore(ZSET_KEY, keyword, 1);


		return new SearchResponseDto(
			searchRepository.searchBooks(keyword, bookPageable),
			searchRepository.searchReviews(keyword, reviewPageable)
		);
	}
	public List<PopularKeywordDto> getPopular(int top) {
		Set<ZSetOperations.TypedTuple<String>> tuples = redisTemplate.opsForZSet()
			.reverseRangeWithScores(ZSET_KEY, 0, top - 1);

		List<PopularKeywordDto> list = new ArrayList<>();
		if (tuples != null) {
			int rank = 1;
			for (ZSetOperations.TypedTuple<String> t : tuples) {
				list.add(new PopularKeywordDto(
					rank++,
					t.getValue(),
					t.getScore() != null
						? t.getScore().longValue()
						: 0L
				));
			}
		}
		return list;
	}
}
