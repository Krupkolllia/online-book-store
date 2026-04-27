package org.project.onlinebookstore.dto.book;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import org.hibernate.validator.constraints.URL;
import org.project.onlinebookstore.exception.validator.Isbn;

public record UpdateBookRequestDto(
        @NotBlank
        String title,

        @NotBlank
        String author,

        @Isbn
        String isbn,

        @NotNull
        @DecimalMin(value = "0.0")
        BigDecimal price,

        @NotBlank
        String description,

        @NotBlank
        @URL
        String coverImage
) {}
