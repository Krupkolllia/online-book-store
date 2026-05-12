package org.project.onlinebookstore.dto.cart;

import java.util.List;

public record ShoppingCartResponseDto(
        Long id,
        Long userId,
        List<CartItemResponseDto> cartItems
) {}
