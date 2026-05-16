package org.project.onlinebookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.project.onlinebookstore.dto.order.OrderItemResponseDto;
import org.project.onlinebookstore.dto.order.OrderRequestDto;
import org.project.onlinebookstore.dto.order.OrderResponseDto;
import org.project.onlinebookstore.dto.order.UpdateOrderStatusRequestDto;
import org.project.onlinebookstore.service.OrderItemService;
import org.project.onlinebookstore.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Book orders management API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    private final OrderItemService orderItemService;

    @Operation(summary = "Place an order with books shipping to requested address")
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public OrderResponseDto placeOrder(@RequestBody @Valid OrderRequestDto requestDto) {
        return orderService.createOrder(requestDto);
    }

    @Operation(summary = "Get orders history")
    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public Page<OrderResponseDto> getOrdersHistory(Pageable pageable) {
        return orderService.findAll(pageable);
    }

    @Operation(summary = "Update (PATCH) order's status")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public OrderResponseDto updateOrderStatusById(
            @PathVariable Long id,
            @RequestBody @Valid UpdateOrderStatusRequestDto requestDto) {
        return orderService.updateOrderStatus(id, requestDto);
    }

    @Operation(summary = "Get all items from order by id")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("{orderId}/items")
    public Page<OrderItemResponseDto> getItemsFromOrder(
            @PathVariable Long orderId, Pageable pageable) {
        return orderItemService.findItemsByOrderId(orderId, pageable);
    }

    @Operation(summary = "Get an item by id from order by id")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("{orderId}/items/{itemId}")
    public OrderItemResponseDto getItemFromOrder(
            @PathVariable Long orderId, @PathVariable Long itemId) {
        return orderItemService.findItemByIdInOrder(orderId, itemId);
    }
}
