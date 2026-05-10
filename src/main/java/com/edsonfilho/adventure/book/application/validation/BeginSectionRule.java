package com.edsonfilho.adventure.book.application.validation;

import com.edsonfilho.adventure.book.domain.entity.Book;
import com.edsonfilho.adventure.book.domain.entity.SectionType;
import com.edsonfilho.adventure.book.domain.exception.InvalidBeginningCountException;
import org.springframework.stereotype.Component;

/**
 * Validates that a book has exactly one BEGIN section.
 *
 * <p>
 * Rule 1: A book should be invalid if it has none, or more than one beginning.
 * </p>
 */
@Component
public class BeginSectionRule implements BookValidationRule {

    @Override
    public void validate(Book book) {
        if (book.getSections() == null) {
            throw new InvalidBeginningCountException(0);
        }

        long beginCount = book.getSections().stream()
                .filter(s -> SectionType.BEGIN.equals(s.getType()))
                .count();

        if (beginCount != 1) {
            throw new InvalidBeginningCountException((int) beginCount);
        }
    }
}
