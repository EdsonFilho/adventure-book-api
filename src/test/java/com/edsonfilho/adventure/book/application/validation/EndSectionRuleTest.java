package com.edsonfilho.adventure.book.application.validation;

import com.edsonfilho.adventure.book.domain.entity.Book;
import com.edsonfilho.adventure.book.domain.entity.Section;
import com.edsonfilho.adventure.book.domain.entity.SectionType;
import com.edsonfilho.adventure.book.domain.exception.BookValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EndSectionRuleTest {

    private EndSectionRule rule;

    @BeforeEach
    void setUp() {
        rule = new EndSectionRule();
    }

    @Test
    void validate_shouldPassWhenAtLeastOneEndSectionExists() {
        // Arrange
        Section end = new Section();
        end.setType(SectionType.END);

        Book book = new Book("1", "Test", "Author", null, null, List.of(end));

        // Act & Assert
        assertDoesNotThrow(() -> rule.validate(book));
    }

    @Test
    void validate_shouldThrowExceptionWhenNoEndSectionExists() {
        // Arrange
        Section begin = new Section();
        begin.setType(SectionType.BEGIN);

        Book book = new Book("1", "Test", "Author", null, null, List.of(begin));

        // Act & Assert
        assertThrows(BookValidationException.class, () -> rule.validate(book));
    }
}
