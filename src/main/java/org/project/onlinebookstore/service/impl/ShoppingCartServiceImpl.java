package org.project.onlinebookstore.service.impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.project.onlinebookstore.dto.cart.CartItemQuantityRequestDto;
import org.project.onlinebookstore.dto.cart.CartItemRequestDto;
import org.project.onlinebookstore.dto.cart.ShoppingCartResponseDto;
import org.project.onlinebookstore.exception.EntityNotFoundException;
import org.project.onlinebookstore.mapper.CartItemMapper;
import org.project.onlinebookstore.mapper.ShoppingCartMapper;
import org.project.onlinebookstore.model.book.Book;
import org.project.onlinebookstore.repository.book.BookRepository;
import org.project.onlinebookstore.model.cart.CartItem;
import org.project.onlinebookstore.model.cart.ShoppingCart;
import org.project.onlinebookstore.model.user.User;
import org.project.onlinebookstore.repository.cart.CartItemRepository;
import org.project.onlinebookstore.repository.cart.ShoppingCartRepository;
import org.project.onlinebookstore.security.SecurityUtil;
import org.project.onlinebookstore.service.ShoppingCartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;

    private final ShoppingCartMapper shoppingCartMapper;

    private final CartItemRepository cartItemRepository;

    private final CartItemMapper cartItemMapper;

    private final BookRepository bookRepository;

    @Override
    public ShoppingCart createShoppingCartForUser(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);

        return shoppingCartRepository.save(shoppingCart);
    }

    @Override
    @Transactional(readOnly = true)
    public ShoppingCartResponseDto findCart() {
        Long userId = SecurityUtil.getUserFromSecurityContext().getId();
        return shoppingCartMapper.toDto(getCartForAuthenticatedUser(userId));
    }

    @Override
    public ShoppingCartResponseDto addItemToCart(CartItemRequestDto itemDto) {
        Book book = bookRepository.findById(itemDto.bookId())
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        Long userId = SecurityUtil.getUserFromSecurityContext().getId();
        ShoppingCart shoppingCart = getCartForAuthenticatedUser(userId);

        Optional<CartItem> existingItem = shoppingCart.getCartItems().stream()
                .filter(item -> item.getBook().getId().equals(itemDto.bookId()))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + itemDto.quantity());
        } else {
            CartItem cartItem = cartItemMapper.toModel(itemDto);
            cartItem.setBook(book);
            cartItem.setShoppingCart(shoppingCart);
            shoppingCart.getCartItems().add(cartItem);
        }

        return shoppingCartMapper.toDto(shoppingCartRepository.save(shoppingCart));
    }

    @Override
    public ShoppingCartResponseDto updateQuantityById(Long id, CartItemQuantityRequestDto dto) {
        Long userId = SecurityUtil.getUserFromSecurityContext().getId();

        CartItem cartItem = findCartItemById(id, userId);
        cartItem.setQuantity(dto.quantity());

        return shoppingCartMapper.toDto(cartItem.getShoppingCart());
    }

    @Override
    public void deleteById(Long id) {
        Long userId = SecurityUtil.getUserFromSecurityContext().getId();
        CartItem cartItem = findCartItemById(id, userId);

        cartItemRepository.delete(cartItem);
    }

    private ShoppingCart getCartForAuthenticatedUser(Long userId) {
        return shoppingCartRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException(
                        "There is no shopping cart for user with id: " + userId)
        );
    }

    private CartItem findCartItemById(Long id, Long userId) {
        return cartItemRepository.findByIdAndShoppingCartId(id, userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "There is no cart item with id " + id + " in shopping cart")
                );
    }
}
