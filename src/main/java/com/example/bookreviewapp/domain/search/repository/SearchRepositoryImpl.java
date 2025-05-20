package com.example.bookreviewapp.domain.search.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.util.List;

import com.example.bookreviewapp.domain.book.dto.response.BookResponseDto;
import com.example.bookreviewapp.domain.book.entity.EnrollStatus;
import com.example.bookreviewapp.domain.book.entity.QBook;
import com.example.bookreviewapp.domain.review.dto.ReviewResponseDto;
import com.example.bookreviewapp.domain.review.entity.QReview;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SearchRepositoryImpl implements SearchRepositoryCustom{

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<BookResponseDto> searchBooks(String keyword, Pageable pageable) {
		QBook book = QBook.book;

		List<BookResponseDto> content =queryFactory
			.select(Projections.constructor(
				BookResponseDto.class,
				book.id,book.title,book.author,book.category,
				book.createdAt,book.updatedAt,book.enrollStatus
			))
			.from(book)
			.where(
				book.enrollStatus.eq(EnrollStatus.ACCEPT),
				book.title.containsIgnoreCase(keyword)
				.or(book.author.containsIgnoreCase(keyword))
				.or(book.category.containsIgnoreCase(keyword)))
			.orderBy(book.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = queryFactory
			.select(book.count())
			.from(book)
			.where(book.title.containsIgnoreCase(keyword)
				.or(book.author.containsIgnoreCase(keyword)))
			.fetchOne();
		long totalCount = (total != null ? total : 0L);

		return new PageImpl<>(content,pageable,totalCount);
	}

	@Override
	public Page<ReviewResponseDto> searchReviews(String keyword, Pageable pageable) {
		QReview review = QReview.review;
		QBook book = QBook.book;

		List<ReviewResponseDto> content = queryFactory
			.select(Projections.constructor(
				ReviewResponseDto.class,
				book.title,
				review.user.email,
				review.content,
				review.score
			))
			.from(review)
			.join(review.book, book)
			.where(
				book.enrollStatus.eq(EnrollStatus.ACCEPT),
				book.title.containsIgnoreCase(keyword)
				.or(review.content.containsIgnoreCase(keyword)))
			.orderBy(review.isPinned.asc(), review.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = queryFactory
			.select(review.count())
			.from(review)
			.where(book.title.containsIgnoreCase(keyword)
				.or(review.content.containsIgnoreCase(keyword)))
			.fetchOne();
		long totalCount = (total != null ? total : 0L);

		return new PageImpl<>(content, pageable, totalCount);
	}
}
