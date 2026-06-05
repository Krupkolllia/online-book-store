package org.project.onlinebookstore.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.project.onlinebookstore.dto.cart.CartItemQuantityRequestDto;
import org.project.onlinebookstore.dto.cart.CartItemRequestDto;
import org.project.onlinebookstore.dto.cart.CartItemResponseDto;
import org.project.onlinebookstore.dto.cart.ShoppingCartResponseDto;
import org.project.onlinebookstore.exception.EntityNotFoundException;
import org.project.onlinebookstore.mapper.CartItemMapper;
import org.project.onlinebookstore.mapper.ShoppingCartMapper;
import org.project.onlinebookstore.model.book.Book;
import org.project.onlinebookstore.model.cart.CartItem;
import org.project.onlinebookstore.model.cart.ShoppingCart;
import org.project.onlinebookstore.model.user.User;
import org.project.onlinebookstore.repository.book.BookRepository;
import org.project.onlinebookstore.repository.cart.CartItemRepository;
import org.project.onlinebookstore.repository.cart.ShoppingCartRepository;
import org.project.onlinebookstore.security.SecurityUtil;
import org.project.onlinebookstore.service.impl.ShoppingCartServiceImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartServiceImplTest {

    private static final Long USER_ID = 322L;

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private ShoppingCartMapper shoppingCartMapper;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private CartItemMapper cartItemMapper;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    private MockedStatic<SecurityUtil> securityUtilMock;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setId(USER_ID);

        securityUtilMock = mockStatic(SecurityUtil.class);
        securityUtilMock.when(SecurityUtil::getUserFromSecurityContext)
                .thenReturn(user);
    }

    @AfterEach
    void tearDown() {
        securityUtilMock.close();
    }

    @Test
    @DisplayName("""
            createShoppingCartForUser method should bind User
            to ShoppingCart, save it and return
            """)
    public void createShoppingCartForUser_ValidCase_ShouldReturnShoppingCart() {
        // Given
        User user = new User();
        user.setId(USER_ID);

        ShoppingCart expected = new ShoppingCart();
        expected.setUser(user);

        when(shoppingCartRepository.save(any(ShoppingCart.class)))
                .thenReturn(expected);

        // When
        ShoppingCart actual = shoppingCartService.createShoppingCartForUser(user);

        // Then
        assertThat(actual).isEqualTo(expected);

        verify(shoppingCartRepository).save(any(ShoppingCart.class));
        verifyNoMoreInteractions(shoppingCartRepository);
    }

    @Test
    @DisplayName("""
            findCart method if ShoppingCart exists for this User
            should return ShoppingCartResponseDto
            """)
    public void findCart_CartExistsForUser_ShouldReturnShoppingCartResponseDto() {
        // Given
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(USER_ID);

        ShoppingCartResponseDto expected = new ShoppingCartResponseDto(
                USER_ID, USER_ID, List.of()
        );

        when(shoppingCartRepository.findById(USER_ID)).thenReturn(Optional.of(shoppingCart));

        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(expected);

        // When
        ShoppingCartResponseDto actual = shoppingCartService.findCart();

        // Then
        assertThat(actual).isEqualTo(expected);

        verify(shoppingCartRepository).findById(USER_ID);
        verifyNoMoreInteractions(shoppingCartRepository);

        verify(shoppingCartMapper).toDto(shoppingCart);
        verifyNoMoreInteractions(shoppingCartMapper);
    }

    @Test
    @DisplayName("""
            findCart method if cart does not exists for User
            should throw EntityNotFoundException
            """)
    public void findCart_NoCartExistsForUser_ShouldThrowException() {
        // Given
        when(shoppingCartRepository.findById(USER_ID))
                .thenReturn(Optional.empty());

        // When
        assertThatThrownBy(() -> shoppingCartService.findCart())
                .isExactlyInstanceOf(EntityNotFoundException.class)
                .hasMessage("There is no shopping cart for user with id: " + USER_ID);

        // Then
        verify(shoppingCartRepository).findById(USER_ID);
        verifyNoMoreInteractions(shoppingCartRepository);

        verifyNoInteractions(shoppingCartMapper);
    }

    @Test
    @DisplayName("""
            addItemToCart method if CartItem does not exists in
            User's ShoppingCart should create and add requested
            CartItem to ShoppingCart and return ShoppingCartResponseDto
            """)
    public void addItemToCart_ItemDoesNotExist_ShouldAddNewItemToCart() {
        // Given
        Long bookId = 21L;
        int quantity = 2;
        Book book = new Book().setId(bookId);
        CartItemRequestDto itemRequestDto = new CartItemRequestDto(
                bookId, quantity
        );

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(USER_ID);
        shoppingCart.setCartItems(new HashSet<>());

        when(shoppingCartRepository.findById(USER_ID)).thenReturn(Optional.of(shoppingCart));

        Long cartItemId = 44L;
        CartItem cartItem = new CartItem();
        cartItem.setId(cartItemId);
        cartItem.setQuantity(quantity);
        when(cartItemMapper.toModel(itemRequestDto)).thenReturn(cartItem);

        when(shoppingCartRepository.save(shoppingCart)).thenReturn(shoppingCart);

        CartItemResponseDto cartItemResponseDto = new CartItemResponseDto(
                cartItemId, bookId, null, quantity
        );

        ShoppingCartResponseDto expected = new ShoppingCartResponseDto(
                USER_ID, USER_ID, List.of(cartItemResponseDto)
        );
        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(expected);

        // When
        ShoppingCartResponseDto actual = shoppingCartService.addItemToCart(itemRequestDto);

        // Then
        assertThat(actual).isEqualTo(expected);

        assertThat(shoppingCart.getCartItems()).containsExactly(cartItem);

        verify(bookRepository).findById(bookId);
        verifyNoMoreInteractions(bookRepository);

        verify(shoppingCartRepository).findById(USER_ID);
        verify(shoppingCartRepository).save(shoppingCart);
        verifyNoMoreInteractions(shoppingCartRepository);

        verify(cartItemMapper).toModel(itemRequestDto);
        verifyNoMoreInteractions(cartItemMapper);

        verify(shoppingCartMapper).toDto(shoppingCart);
        verifyNoMoreInteractions(shoppingCartMapper);
    }

    @Test
    @DisplayName("""
            addItemToCart method if CartItem already exists in User's cart
            should increase quantity requested quantity in CartItemRequestDto
            and return ShoppingCartResponseDto
            """)
    public void addItemToCart_ItemAlreadyExists_ShouldIncreaseQuantity() {
        // Given
        Long bookId = 35L;
        Book book = new Book().setId(bookId);
        CartItemRequestDto itemRequestDto = new CartItemRequestDto(
                bookId, 3
        );
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(USER_ID);

        Long cartItemId = 67L;
        CartItem cartItem = new CartItem();
        cartItem.setId(cartItemId);
        cartItem.setBook(book);
        cartItem.setQuantity(2);

        shoppingCart.setCartItems(Set.of(cartItem));

        when(shoppingCartRepository.findById(USER_ID)).thenReturn(Optional.of(shoppingCart));

        when(shoppingCartRepository.save(shoppingCart)).thenReturn(shoppingCart);

        CartItemResponseDto cartItemResponseDto = new CartItemResponseDto(
                cartItemId, bookId, null, 5
        );

        ShoppingCartResponseDto expected = new ShoppingCartResponseDto(
                USER_ID, USER_ID, List.of(cartItemResponseDto)
        );

        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(expected);

        // When
        ShoppingCartResponseDto actual = shoppingCartService.addItemToCart(itemRequestDto);

        // Then
        assertThat(actual).isEqualTo(expected);
        assertThat(cartItem.getQuantity()).isEqualTo(5);

        verify(bookRepository).findById(bookId);
        verifyNoMoreInteractions(bookRepository);

        verify(shoppingCartRepository).findById(USER_ID);
        verify(shoppingCartRepository).save(shoppingCart);
        verifyNoMoreInteractions(shoppingCartRepository);

        verify(shoppingCartMapper).toDto(shoppingCart);
        verifyNoMoreInteractions(shoppingCartMapper);
    }

    @Test
    @DisplayName("""
            addItemToCart method with requested CartItem's Book
            not existing in database by id should throw
            EntityNotFoundException
            """)
    public void addItemToCart_InvalidBookId_ShouldThrowException() {
        // Given
        Long invalidBookId = 404L;
        CartItemRequestDto itemRequestDto = new CartItemRequestDto(
                invalidBookId, 7
        );

        when(bookRepository.findById(invalidBookId)).thenReturn(Optional.empty());

        // When
        assertThatThrownBy(() -> shoppingCartService.addItemToCart(itemRequestDto))
                .isExactlyInstanceOf(EntityNotFoundException.class)
                .hasMessage("Book not found");

        // Then
        verify(bookRepository).findById(invalidBookId);
        verifyNoMoreInteractions(bookRepository);

        verifyNoInteractions(shoppingCartRepository);
        verifyNoInteractions(shoppingCartMapper);

        verifyNoInteractions(cartItemMapper);
    }

    @Test
    @DisplayName("""
            addItemToCart method for User with no ShoppingCart
            should throw EntityNotFoundException
            """)
    public void addItemToCart_NoCartForUser_ShouldThrowException() {
        // Given
        Long bookId = 12L;
        Book book = new Book().setId(bookId);

        CartItemRequestDto itemRequestDto = new CartItemRequestDto(
                bookId, 44
        );

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        when(shoppingCartRepository.findById(USER_ID)).thenReturn(Optional.empty());

        // When
        assertThatThrownBy(() -> shoppingCartService.addItemToCart(itemRequestDto))
                .isExactlyInstanceOf(EntityNotFoundException.class)
                .hasMessage( "There is no shopping cart for user with id: " + USER_ID);

        // Then
        verify(bookRepository).findById(bookId);
        verifyNoMoreInteractions(bookRepository);

        verify(shoppingCartRepository).findById(USER_ID);
        verifyNoMoreInteractions(shoppingCartRepository);

        verifyNoInteractions(shoppingCartMapper);
        verifyNoInteractions(cartItemMapper);
    }

    @Test
    @DisplayName("""
            updateQuantityById method with id of existing
            cart item should update CartItem's quantity
            and return ShoppingCartResponseDto
            """)
    public void updateQuantityById_WithValidId_ShouldUpdateQuantity() {
        // Given
        Long cartItemId = 22L;
        int quantity = 2;

        CartItemQuantityRequestDto quantityRequestDto = new CartItemQuantityRequestDto(quantity);

        ShoppingCart shoppingCart = new ShoppingCart();

        CartItem cartItem = new CartItem();
        cartItem.setQuantity(99);
        cartItem.setShoppingCart(shoppingCart);

        when(cartItemRepository.findByIdAndShoppingCartId(cartItemId, USER_ID))
                .thenReturn(Optional.of(cartItem));

        CartItemResponseDto cartItemResponseDto = new CartItemResponseDto(
                cartItemId, 3L, null, quantity
        );

        ShoppingCartResponseDto expected = new ShoppingCartResponseDto(
                USER_ID, USER_ID, List.of(cartItemResponseDto)
        );

        when(shoppingCartMapper.toDto(cartItem.getShoppingCart())).thenReturn(expected);

        // When
        ShoppingCartResponseDto actual =
                shoppingCartService.updateQuantityById(cartItemId, quantityRequestDto);

        // Then
        assertThat(actual).isEqualTo(expected);
        assertThat(cartItem.getQuantity()).isEqualTo(quantity);

        verify(cartItemRepository).findByIdAndShoppingCartId(cartItemId, USER_ID);
        verifyNoMoreInteractions(cartItemRepository);

        verify(shoppingCartMapper).toDto(shoppingCart);
        verifyNoMoreInteractions(shoppingCartMapper);
    }

    @Test
    @DisplayName("""
            updateQuantityById method with id of non-existing
            CartItem should throw EntityNotFoundException
            """)
    public void updateQuantityById_WithInvalidId_ShouldThrowException() {
        // Given
        Long invalidCartItemId = 404L;
        CartItemQuantityRequestDto quantityRequestDto = new CartItemQuantityRequestDto(2);

        when(cartItemRepository.findByIdAndShoppingCartId(invalidCartItemId, USER_ID))
                .thenReturn(Optional.empty());
        // When
        assertThatThrownBy(() -> shoppingCartService
                .updateQuantityById(invalidCartItemId, quantityRequestDto))
                .isExactlyInstanceOf(EntityNotFoundException.class)
                .hasMessage("There is no cart item with id " + invalidCartItemId + " in shopping cart");

        // Then
        verify(cartItemRepository).findByIdAndShoppingCartId(invalidCartItemId, USER_ID);
        verifyNoMoreInteractions(cartItemRepository);

        verifyNoInteractions(shoppingCartMapper);
    }

    @Test
    @DisplayName("""
            deleteById method with id of existing CartItem
            should delete it from database
            """)
    public void deleteById_WithValidCartItemId_ShouldDeleteCartItem() {
        // Given
        Long cartItemId = 99L;
        CartItem cartItem = new CartItem();
        cartItem.setId(cartItemId);

        when(cartItemRepository.findByIdAndShoppingCartId(cartItemId, USER_ID))
                .thenReturn(Optional.of(cartItem));

        // When
        shoppingCartService.deleteById(cartItemId);

        // Then
        verify(cartItemRepository).findByIdAndShoppingCartId(cartItemId, USER_ID);
        verify(cartItemRepository).delete(cartItem);
        verifyNoMoreInteractions(cartItemRepository);
    }

    @Test
    @DisplayName("""
            deleteById method with id of non-existing CartItem
            should throw EntityNotFoundException
            """)
    public void deleteById_WithInvalidCartItemId_ShouldThrowException() {
        // Given
        Long invalidCartItemId = 404L;

        when(cartItemRepository.findByIdAndShoppingCartId(invalidCartItemId, USER_ID))
                .thenReturn(Optional.empty());

        // When
        assertThatThrownBy(() -> shoppingCartService.deleteById(invalidCartItemId))
                .isExactlyInstanceOf(EntityNotFoundException.class)
                .hasMessage("There is no cart item with id " + invalidCartItemId + " in shopping cart");

        // Then
        verify(cartItemRepository).findByIdAndShoppingCartId(invalidCartItemId, USER_ID);
        verifyNoMoreInteractions(cartItemRepository);
    }
}
