package org.project.onlinebookstore.dto.book;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import org.project.onlinebookstore.exception.validator.Isbn;

public record CreateBookRequestDto(
        @NotBlank
        String title,
        @NotBlank
        String author,
        @Isbn
        String isbn,
        @NotNull
        @DecimalMin(value = "0.0")
        BigDecimal price,
        String description,
        String coverImage
) {}
