package org.project.onlinebookstore.service;

import org.project.onlinebookstore.dto.cart.CartItemResponseDto;

public interface CartItemService {
    CartItemResponseDto updateQuantityById(Long id, int quantity);

    void deleteById(Long id);
}
