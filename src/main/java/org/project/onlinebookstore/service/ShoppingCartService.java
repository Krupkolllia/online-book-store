package org.project.onlinebookstore.service;

import org.project.onlinebookstore.dto.cart.CartItemRequestDto;
import org.project.onlinebookstore.dto.cart.ShoppingCartResponseDto;

public interface ShoppingCartService {
    ShoppingCartResponseDto findCart();

    ShoppingCartResponseDto addItemToCart(CartItemRequestDto item);
}
