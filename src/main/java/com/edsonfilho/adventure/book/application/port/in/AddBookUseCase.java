package com.edsonfilho.adventure.book.application.port.in;

import com.edsonfilho.adventure.book.domain.entity.Book;
import com.edsonfilho.adventure.book.domain.exception.BookValidationException;

/**
 * Input port: add a new book to the collection.
 *
 * <p>The book is submitted as a complete JSON payload (sections and options
 * included). The use case validates the book's structure before persisting it.</p>
 */
public interface AddBookUseCase {

    /**
     * Creates and persists a new book.
     *
     * @param command the full book data
     * @return the saved book, including its assigned id
     * @throws BookValidationException if the book fails any structural validation rule
     */
    Book execute(AddBookCommand command);
}
