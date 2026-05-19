package org.project.onlinebookstore.dto.cart;

public record CartItemResponseDto(
        Long id,
        Long bookId,
        String bookTitle,
        int quantity
) {}
