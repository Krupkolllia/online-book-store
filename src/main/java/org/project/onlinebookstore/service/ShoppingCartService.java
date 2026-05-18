package org.project.onlinebookstore.service;

import org.project.onlinebookstore.dto.cart.CartItemQuantityRequestDto;
import org.project.onlinebookstore.dto.cart.CartItemRequestDto;
import org.project.onlinebookstore.dto.cart.CartItemResponseDto;
import org.project.onlinebookstore.dto.cart.ShoppingCartResponseDto;
import org.project.onlinebookstore.model.ShoppingCart;
import org.project.onlinebookstore.model.User;

public interface ShoppingCartService {
    ShoppingCart createShoppingCartForUser(User user);

    ShoppingCartResponseDto findCart();

    ShoppingCartResponseDto addItemToCart(CartItemRequestDto item);

    CartItemResponseDto updateQuantityById(Long id, CartItemQuantityRequestDto dto);

    void deleteById(Long id);
}
