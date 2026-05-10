package com.edsonfilho.adventure.book.application.validation;

import com.edsonfilho.adventure.book.domain.entity.Book;

/**
 * Contract for a single structural validation rule applied to a {@link Book}.
 *
 * <p>
 * Implementations should throw the appropriate {@link BookValidationException}
 * subclass when the rule is violated.
 * </p>
 */
public interface BookValidationRule {

    /**
     * Validates the given book against this rule.
     *
     * @param book the book to validate
     * @throws BookValidationException if the rule is violated
     */
    void validate(Book book);
}
