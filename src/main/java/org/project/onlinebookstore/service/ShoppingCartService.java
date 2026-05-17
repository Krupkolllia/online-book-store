package org.project.onlinebookstore.service;

import org.project.onlinebookstore.dto.cart.CartItemRequestDto;
import org.project.onlinebookstore.dto.cart.ShoppingCartResponseDto;
import org.project.onlinebookstore.model.ShoppingCart;
import org.project.onlinebookstore.model.User;

public interface ShoppingCartService {
    ShoppingCart createShoppingCartForUser(User user);

    ShoppingCartResponseDto findCart();

    ShoppingCartResponseDto addItemToCart(CartItemRequestDto item);
}
