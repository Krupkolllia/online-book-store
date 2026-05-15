package org.project.onlinebookstore.service.impl;

import lombok.RequiredArgsConstructor;
import org.project.onlinebookstore.dto.cart.CartItemQuantityRequestDto;
import org.project.onlinebookstore.dto.cart.CartItemResponseDto;
import org.project.onlinebookstore.exception.EntityNotFoundException;
import org.project.onlinebookstore.mapper.CartItemMapper;
import org.project.onlinebookstore.model.cart.CartItem;
import org.project.onlinebookstore.model.user.User;
import org.project.onlinebookstore.repository.cart.CartItemRepository;
import org.project.onlinebookstore.service.CartItemService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;

    private final CartItemMapper cartItemMapper;

    @Override
    public CartItemResponseDto updateQuantityById(Long id, CartItemQuantityRequestDto dto) {
        CartItem cartItem = findCartItemById(id);
        cartItem.setQuantity(dto.quantity());

        return cartItemMapper.toDto(cartItem);
    }

    @Override
    public void deleteById(Long id) {
        cartItemRepository.delete(findCartItemById(id));
    }

    private CartItem findCartItemById(Long cartItemId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = user.getId();

        return cartItemRepository.findByIdAndShoppingCartUserId(cartItemId, userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "There is no cart item with id " + cartItemId + " in shopping cart")
                );
    }
}
