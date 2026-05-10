package com.edsonfilho.adventure.book.application.validation;

import com.edsonfilho.adventure.book.domain.entity.Book;
import com.edsonfilho.adventure.book.domain.entity.SectionType;
import com.edsonfilho.adventure.book.domain.exception.NoEndingSectionException;
import org.springframework.stereotype.Component;

/**
 * Validates that a book has at least one END section.
 *
 * <p>
 * Rule 2: A book should be invalid if it has no ending (but can have multiple).
 * </p>
 */
@Component
public class EndSectionRule implements BookValidationRule {

    @Override
    public void validate(Book book) {
        if (book.getSections() == null) {
            throw new NoEndingSectionException();
        }

        boolean hasEnding = book.getSections().stream()
                .anyMatch(s -> SectionType.END.equals(s.getType()));

        if (!hasEnding) {
            throw new NoEndingSectionException();
        }
    }
}
