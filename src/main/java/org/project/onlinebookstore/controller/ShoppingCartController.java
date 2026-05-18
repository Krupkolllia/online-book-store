package org.project.onlinebookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.project.onlinebookstore.dto.cart.CartItemQuantityRequestDto;
import org.project.onlinebookstore.dto.cart.CartItemRequestDto;
import org.project.onlinebookstore.dto.cart.CartItemResponseDto;
import org.project.onlinebookstore.dto.cart.ShoppingCartResponseDto;
import org.project.onlinebookstore.service.ShoppingCartService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User's shopping cart management")
@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @Operation(summary = "Add an item to shopping cart")
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ShoppingCartResponseDto addItemToCart(@RequestBody @Valid CartItemRequestDto item) {
        return shoppingCartService.addItemToCart(item);
    }

    @Operation(summary = "Get the shopping cart")
    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ShoppingCartResponseDto getShoppingCart() {
        return shoppingCartService.findCart();
    }

    @Operation(summary = "Update (PUT) cart item quantity")
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/items/{cartItemId}")
    public CartItemResponseDto updateCartItemQuantity(
            @PathVariable Long cartItemId, @RequestBody @Valid CartItemQuantityRequestDto dto) {
        return shoppingCartService.updateQuantityById(cartItemId, dto);
    }

    @Operation(summary = "Delete cart item from shopping cart by id")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("items/{cartItemId}")
    public void deleteCartItemFromCartById(@PathVariable Long cartItemId) {
        shoppingCartService.deleteById(cartItemId);
    }
}
