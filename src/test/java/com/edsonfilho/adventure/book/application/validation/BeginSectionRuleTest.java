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

class BeginSectionRuleTest {

    private BeginSectionRule rule;

    @BeforeEach
    void setUp() {
        rule = new BeginSectionRule();
    }

    @Test
    void validate_shouldPassWhenExactlyOneBeginSectionExists() {
        // Arrange
        Section begin = new Section();
        begin.setType(SectionType.BEGIN);
        
        Section end = new Section();
        end.setType(SectionType.END);

        Book book = new Book("1", "Test", "Author", null, null, List.of(begin, end));

        // Act & Assert
        assertDoesNotThrow(() -> rule.validate(book));
    }

    @Test
    void validate_shouldThrowExceptionWhenNoBeginSectionExists() {
        // Arrange
        Section node = new Section();
        node.setType(SectionType.NODE);

        Book book = new Book("1", "Test", "Author", null, null, List.of(node));

        // Act & Assert
        assertThrows(BookValidationException.class, () -> rule.validate(book));
    }

    @Test
    void validate_shouldThrowExceptionWhenMultipleBeginSectionsExist() {
        // Arrange
        Section begin1 = new Section();
        begin1.setType(SectionType.BEGIN);
        
        Section begin2 = new Section();
        begin2.setType(SectionType.BEGIN);

        Book book = new Book("1", "Test", "Author", null, null, List.of(begin1, begin2));

        // Act & Assert
        assertThrows(BookValidationException.class, () -> rule.validate(book));
    }
}
