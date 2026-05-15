package org.project.onlinebookstore.dto.order;

import jakarta.validation.constraints.NotNull;
import org.project.onlinebookstore.model.order.OrderStatus;

public record UpdateOrderStatusRequestDto(@NotNull OrderStatus status) {}
