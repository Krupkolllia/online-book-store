package org.project.onlinebookstore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.project.onlinebookstore.config.MapStructConfig;
import org.project.onlinebookstore.dto.cart.ShoppingCartResponseDto;
import org.project.onlinebookstore.model.ShoppingCart;

@Mapper(config = MapStructConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {
    @Mapping(target = "cartItems", qualifiedByName = "modelsToResponsesDto")
    ShoppingCartResponseDto toDto(ShoppingCart shoppingCart);
}
