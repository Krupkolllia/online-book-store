package org.project.onlinebookstore.exception.validator.isbn;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IsbnValidator implements ConstraintValidator<Isbn, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null || value.isBlank()) {
            return true;
        }

        String isbn = value.replace("-", "").replace(" ", "");

        return isValidIsbn10(isbn) || isValidIsbn13(isbn);
    }

    private boolean isValidIsbn10(String isbn) {
        if (isbn.length() != 10) {
            return false;
        }

        int sum = 0;
        for (int i = 0; i < 9; i++) {
            if (!Character.isDigit(isbn.charAt(i))) {
                return false;
            }
            sum += (isbn.charAt(i) - '0') * (10 - i);
        }

        char last = isbn.charAt(9);
        sum += (last == 'X') ? 10 : (Character.isDigit(last) ? last - '0' : -1);

        return sum % 11 == 0;
    }

    private boolean isValidIsbn13(String isbn) {
        if (isbn.length() != 13 || !isbn.chars().allMatch(Character::isDigit)) {
            return false;
        }

        int sum = 0;
        for (int i = 0; i < 13; i++) {
            int digit = isbn.charAt(i) - '0';
            sum += (i % 2 == 0) ? digit : digit * 3;
        }

        return sum % 10 == 0;
    }
}
