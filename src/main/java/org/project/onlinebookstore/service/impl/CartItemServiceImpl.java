package org.project.onlinebookstore.service.impl;

import lombok.RequiredArgsConstructor;
import org.project.onlinebookstore.dto.cart.CartItemQuantityRequestDto;
import org.project.onlinebookstore.dto.cart.CartItemResponseDto;
import org.project.onlinebookstore.exception.EntityNotFoundException;
import org.project.onlinebookstore.mapper.CartItemMapper;
import org.project.onlinebookstore.model.CartItem;
import org.project.onlinebookstore.model.User;
import org.project.onlinebookstore.repository.cart.CartItemRepository;
import org.project.onlinebookstore.security.SecurityUtil;
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
        Long userId = SecurityUtil.getUserFromSecurityContext().getId();

        if (!cartItemRepository.existsByIdAndShoppingCartUserId(id, userId)) {
            throw new EntityNotFoundException(
                    "There is no cart item with id " + id + " in shopping cart");
        }
        cartItemRepository.deleteById(id);
    }

    private CartItem findCartItemById(Long id) {
        Long userId = SecurityUtil.getUserFromSecurityContext().getId();

        return cartItemRepository.findByIdAndShoppingCartUserId(id, userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "There is no cart item with id " + id + " in shopping cart")
                );
    }
}
