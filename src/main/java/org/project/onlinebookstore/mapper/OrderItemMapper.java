package org.project.onlinebookstore.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.project.onlinebookstore.config.MapStructConfig;
import org.project.onlinebookstore.dto.order.OrderItemResponseDto;
import org.project.onlinebookstore.model.order.OrderItem;

@Mapper(config = MapStructConfig.class)
public interface OrderItemMapper {
    @Mapping(source = "book.id", target = "bookId")
    OrderItemResponseDto toDto(OrderItem orderItem);

    @Named("orderItemsToDto")
    default Set<OrderItemResponseDto> orderItemsToDto(Set<OrderItem> orderItems) {
        return orderItems.stream()
                .map(this::toDto)
                .collect(Collectors.toSet());
    }
}
