package org.project.onlinebookstore.dto.order;

public record OrderItemResponseDto(
        Long id,
        Long bookId,
        int quantity
) {}
