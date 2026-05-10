package com.edsonfilho.adventure.book.application.validation;

import com.edsonfilho.adventure.book.domain.entity.Book;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Orchestrates all {@link BookValidationRule}s against a given {@link Book}.
 *
 * <p>
 * Rules are injected by Spring and executed in order. The first failing rule
 * will stop execution and propagate its exception to the caller.
 * </p>
 *
 * <p>
 * Execution order matters: structural rules (BEGIN / END presence) run first
 * so that reference and option rules don't produce misleading errors on a
 * fundamentally broken structure.
 * </p>
 */
@Component
public class BookValidator {

    private final List<BookValidationRule> rules;

    /**
     * Spring will inject all {@link BookValidationRule} beans in the order they
     * are declared in this list.
     */
    public BookValidator(List<BookValidationRule> rules) {
        this.rules = rules;
    }

    /**
     * Validates the given book against all registered rules.
     *
     * @param book the book to validate
     * @throws BookValidationException if any rule is violated
     */
    public void validate(Book book) {
        for (BookValidationRule rule : rules) {
            rule.validate(book);
        }
    }
}
