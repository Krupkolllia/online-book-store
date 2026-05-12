package org.project.onlinebookstore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.project.onlinebookstore.config.MapStructConfig;
import org.project.onlinebookstore.dto.cart.CartItemRequestDto;
import org.project.onlinebookstore.dto.cart.CartItemResponseDto;
import org.project.onlinebookstore.model.CartItem;
import java.util.List;
import java.util.Set;

@Mapper(config = MapStructConfig.class, uses = BookMapper.class)
public interface CartItemMapper {
    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    CartItemResponseDto toDto(CartItem cartItem);

    @Mapping(source = "bookId", target = "book", qualifiedByName = "bookFromId")
    CartItem toModel(CartItemRequestDto requestDto);

    @Named("modelsToResponsesDto")
    default List<CartItemResponseDto> modelsToResponsesDto(Set<CartItem> cartItemSet) {
        return cartItemSet.stream()
                .map(this::toDto)
                .toList();
    }
}
