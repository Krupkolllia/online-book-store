package org.project.onlinebookstore.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.project.onlinebookstore.config.MapStructConfig;
import org.project.onlinebookstore.dto.order.OrderResponseDto;
import org.project.onlinebookstore.dto.order.UpdateOrderStatusRequestDto;
import org.project.onlinebookstore.model.order.Order;

@Mapper(config = MapStructConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(target = "orderItems", qualifiedByName = "orderItemsToDto")
    OrderResponseDto toDto(Order order);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateOrderStatusFromDto(@MappingTarget Order model, UpdateOrderStatusRequestDto dto);
}
