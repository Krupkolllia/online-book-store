package org.project.onlinebookstore.service;

import org.project.onlinebookstore.dto.cart.CartItemQuantityRequestDto;
import org.project.onlinebookstore.dto.cart.CartItemRequestDto;
import org.project.onlinebookstore.dto.cart.ShoppingCartResponseDto;
import org.project.onlinebookstore.model.cart.ShoppingCart;
import org.project.onlinebookstore.model.user.User;

public interface ShoppingCartService {
    ShoppingCart createShoppingCartForUser(User user);

    ShoppingCartResponseDto findCart();

    ShoppingCartResponseDto addItemToCart(CartItemRequestDto item);

    ShoppingCartResponseDto updateQuantityById(Long id, CartItemQuantityRequestDto dto);

    void deleteById(Long id);
}
