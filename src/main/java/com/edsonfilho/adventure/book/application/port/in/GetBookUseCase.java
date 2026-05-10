package com.edsonfilho.adventure.book.application.port.in;

import com.edsonfilho.adventure.book.domain.entity.Book;
import com.edsonfilho.adventure.book.domain.exception.BookNotFoundException;

/**
 * Input port: retrieve the full details of a single book by its id.
 */
public interface GetBookUseCase {

    /**
     * @param bookId the MongoDB id of the book
     * @return the found book
     * @throws BookNotFoundException if not found
     */
    Book execute(String bookId);
}
