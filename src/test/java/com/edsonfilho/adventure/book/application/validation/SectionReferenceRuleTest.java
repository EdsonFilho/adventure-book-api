package com.edsonfilho.adventure.book.application.validation;

import com.edsonfilho.adventure.book.domain.entity.Book;
import com.edsonfilho.adventure.book.domain.entity.Option;
import com.edsonfilho.adventure.book.domain.entity.Section;
import com.edsonfilho.adventure.book.domain.entity.SectionType;
import com.edsonfilho.adventure.book.domain.exception.BookValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SectionReferenceRuleTest {

    private SectionReferenceRule rule;

    @BeforeEach
    void setUp() {
        rule = new SectionReferenceRule();
    }

    @Test
    void validate_shouldPassWhenAllOptionsPointToValidSections() {
        // Arrange
        Option option = new Option();
        option.setGotoId(2);

        Section begin = new Section();
        begin.setId("1");
        begin.setType(SectionType.BEGIN);
        begin.setOptions(List.of(option));

        Section end = new Section();
        end.setId("2");
        end.setType(SectionType.END);

        Book book = new Book("1", "Test", "Author", null, null, List.of(begin, end));

        // Act & Assert
        assertDoesNotThrow(() -> rule.validate(book));
    }

    @Test
    void validate_shouldThrowExceptionWhenOptionPointsToMissingSection() {
        // Arrange
        Option option = new Option();
        option.setGotoId(999); // Invalid ID

        Section begin = new Section();
        begin.setId("1");
        begin.setType(SectionType.BEGIN);
        begin.setOptions(List.of(option));

        Book book = new Book("1", "Test", "Author", null, null, List.of(begin));

        // Act & Assert
        assertThrows(BookValidationException.class, () -> rule.validate(book));
    }
}
