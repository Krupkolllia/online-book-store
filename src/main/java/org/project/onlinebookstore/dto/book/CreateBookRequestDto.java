package org.project.onlinebookstore.dto.book;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import org.project.onlinebookstore.exception.validator.Isbn;

public record CreateBookRequestDto(
        @Schema(description = "Book title", example = "Clean code")
        @NotBlank
        String title,
        @Schema(description = "Book author", example = "Robert C. Martin")
        @NotBlank
        String author,
        @Schema(description = "A unique ISBN number of book", example = "978-0135398548")
        @Isbn
        String isbn,
        @Schema(description = "Price of the book", example = "100.00")
        @NotNull
        @DecimalMin(value = "0.0")
        BigDecimal price,
        @Schema(description = "Book's description", example = "Awesome book")
        String description,
        @Schema(description = "An URL to book's cover image",
                example = "https://example.com/clean-code.jpg")
        String coverImage
) {}
