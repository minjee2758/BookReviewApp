package com.example.bookreviewapp.domain.admin.book.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bookreviewapp.common.code.SuccessStatus;
import com.example.bookreviewapp.common.response.ApiResponse;
import com.example.bookreviewapp.domain.admin.book.service.AdminBookService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/books")
@RequiredArgsConstructor
public class AdminBookController {

	private final AdminBookService adminBookService;

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/{bookId}/approve")
	public ResponseEntity<ApiResponse<Void>> approve(@PathVariable Long bookId) {
		adminBookService.approve(bookId);
		return ApiResponse.onSuccess(SuccessStatus.BOOK_ADD_ACCEPT_SUCCESS);
	}

}