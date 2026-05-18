package org.project.onlinebookstore.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.project.onlinebookstore.dto.order.OrderItemResponseDto;
import org.project.onlinebookstore.dto.order.OrderRequestDto;
import org.project.onlinebookstore.dto.order.OrderResponseDto;
import org.project.onlinebookstore.dto.order.UpdateOrderStatusRequestDto;
import org.project.onlinebookstore.exception.EmptyShoppingCartException;
import org.project.onlinebookstore.exception.EntityNotFoundException;
import org.project.onlinebookstore.mapper.OrderItemMapper;
import org.project.onlinebookstore.mapper.OrderMapper;
import org.project.onlinebookstore.model.cart.CartItem;
import org.project.onlinebookstore.model.cart.ShoppingCart;
import org.project.onlinebookstore.model.order.Order;
import org.project.onlinebookstore.model.order.OrderItem;
import org.project.onlinebookstore.model.order.OrderStatus;
import org.project.onlinebookstore.model.user.User;
import org.project.onlinebookstore.repository.cart.ShoppingCartRepository;
import org.project.onlinebookstore.repository.order.OrderItemRepository;
import org.project.onlinebookstore.repository.order.OrderRepository;
import org.project.onlinebookstore.security.SecurityUtil;
import org.project.onlinebookstore.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final ShoppingCartRepository shoppingCartRepository;

    private final OrderMapper orderMapper;

    private final OrderItemRepository orderItemRepository;

    private final OrderItemMapper orderItemMapper;

    @Override
    public OrderResponseDto createOrder(OrderRequestDto requestDto) {
        User user = SecurityUtil.getUserFromSecurityContext();
        Long userId = user.getId();

        ShoppingCart shoppingCart = shoppingCartRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException(
                        "There is no shopping cart for user with id: " + userId)
        );

        Set<CartItem> cartItems = shoppingCart.getCartItems();
        if (cartItems.isEmpty()) {
            throw new EmptyShoppingCartException("Unable to create an order: cart is empty");
        }

        Order order = buildOrder(user, requestDto, cartItems);
        cartItems.clear();
        shoppingCartRepository.save(shoppingCart);
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponseDto> findAll(Pageable pageable) {
        User user = SecurityUtil.getUserFromSecurityContext();

        return orderRepository.findAllByUserId(user.getId(), pageable)
                .map(orderMapper::toDto);

    }

    @Override
    public OrderResponseDto updateOrderStatus(Long id, UpdateOrderStatusRequestDto requestDto) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("There is no order with id: " + id)
        );

        orderMapper.updateOrderStatusFromDto(order, requestDto);
        orderRepository.save(order);

        return orderMapper.toDto(order);
    }

    private Order buildOrder(User user, OrderRequestDto requestDto, Set<CartItem> cartItems) {
        Order order = new Order();
        Set<OrderItem> orderItems = mapCartItemsToOrderItems(cartItems, order);

        order.setUser(user);
        order.setOrderItems(orderItems);
        order.setShippingAddress(requestDto.shippingAddress());
        order.setOrderDate(LocalDateTime.now());
        order.setTotal(calculateTotal(orderItems));
        order.setStatus(OrderStatus.PROCESSING);

        return order;
    }

    private Set<OrderItem> mapCartItemsToOrderItems(Set<CartItem> cartItems, Order order) {
        return cartItems.stream()
                .map(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setBook(cartItem.getBook());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setPrice(cartItem.getBook().getPrice());
                    orderItem.setOrder(order);
                    return orderItem;
                })
                .collect(Collectors.toSet());
    }

    private BigDecimal calculateTotal(Set<OrderItem> orderItems) {
        return orderItems.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderItemResponseDto> findItemsByOrderId(Long orderId, Pageable pageable) {
        Long userId = SecurityUtil.getUserFromSecurityContext().getId();

        return orderItemRepository.findAllByOrderIdAndOrderUserId(orderId, userId, pageable)
                .map(orderItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderItemResponseDto findItemByIdInOrder(Long orderId, Long itemId) {
        Long userId = SecurityUtil.getUserFromSecurityContext().getId();

        OrderItem orderItem = orderItemRepository
                .findByIdAndOrderIdAndOrderUserId(itemId, orderId, userId)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "There is no order item with id: " + itemId)
                );

        return orderItemMapper.toDto(orderItem);
    }
}
