package org.project.onlinebookstore.service;

import org.project.onlinebookstore.dto.cart.CartItemQuantityRequestDto;
import org.project.onlinebookstore.dto.cart.CartItemResponseDto;

public interface CartItemService {
    CartItemResponseDto updateQuantityById(Long id, CartItemQuantityRequestDto dto);

    void deleteById(Long id);
}
