package org.project.onlinebookstore.dto.cart;

import jakarta.validation.constraints.Positive;

public record CartItemQuantityRequestDto(@Positive int quantity) {}
