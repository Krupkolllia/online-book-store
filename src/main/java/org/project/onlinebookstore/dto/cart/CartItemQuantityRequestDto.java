package org.project.onlinebookstore.dto.cart;

import jakarta.validation.constraints.Min;

public record CartItemQuantityRequestDto(@Min(1) int quantity) {}
