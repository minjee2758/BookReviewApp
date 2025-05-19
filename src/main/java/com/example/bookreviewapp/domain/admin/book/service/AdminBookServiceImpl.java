package com.example.bookreviewapp.domain.admin.book.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.bookreviewapp.common.code.ErrorStatus;
import com.example.bookreviewapp.common.error.ApiException;
import com.example.bookreviewapp.domain.admin.book.dto.AdminBookResponseDto;
import com.example.bookreviewapp.domain.admin.book.repository.AdminBookRepository;
import com.example.bookreviewapp.domain.book.entity.Book;
import com.example.bookreviewapp.domain.book.entity.EnrollStatus;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminBookServiceImpl implements AdminBookService {
	private final AdminBookRepository adminBookRepository;

	@Override
	public AdminBookResponseDto approve(Long bookId) {
		Book book = adminBookRepository.findById(bookId)
			.orElseThrow(() -> new ApiException(ErrorStatus.BOOK_NOT_FOUND));
		if (book.getEnrollStatus() == EnrollStatus.ACCEPT) {
			throw new ApiException(ErrorStatus.BOOK_ADD_REQUEST_ALREADY_ACCEPT);
		}
		book.acceptEnroll();
		return new AdminBookResponseDto(book.getId(), book.getEnrollStatus());

	}
}