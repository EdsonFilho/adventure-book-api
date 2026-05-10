package com.edsonfilho.adventure.book.application.validation;

import com.edsonfilho.adventure.book.domain.entity.Book;
import com.edsonfilho.adventure.book.domain.exception.BookValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookValidatorTest {

    @Mock
    private BookValidationRule passingRule1;

    @Mock
    private BookValidationRule passingRule2;

    @Mock
    private BookValidationRule failingRule;

    private BookValidator validator;

    @Test
    void validate_shouldPassWhenAllRulesPass() {
        // Arrange
        validator = new BookValidator(List.of(passingRule1, passingRule2));
        Book book = new Book("1", "Title", "Author", null, null, List.of());

        doNothing().when(passingRule1).validate(any());
        doNothing().when(passingRule2).validate(any());

        // Act & Assert
        assertDoesNotThrow(() -> validator.validate(book));

        verify(passingRule1).validate(book);
        verify(passingRule2).validate(book);
    }

    @Test
    void validate_shouldThrowExceptionWhenAnyRuleFails() {
        // Arrange
        validator = new BookValidator(List.of(passingRule1, failingRule, passingRule2));
        Book book = new Book("1", "Title", "Author", null, null, List.of());

        doNothing().when(passingRule1).validate(any());
        doThrow(new BookValidationException("Rule failed")).when(failingRule).validate(any());

        // Act & Assert
        assertThrows(BookValidationException.class, () -> validator.validate(book));

        verify(passingRule1).validate(book);
        verify(failingRule).validate(book);
        
        // If a rule fails early, subsequent rules are not executed
        verify(passingRule2, never()).validate(any());
    }
}
