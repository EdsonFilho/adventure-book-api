package com.edsonfilho.adventure.book.application.port.in;

import com.edsonfilho.adventure.book.domain.entity.Book;
import com.edsonfilho.adventure.book.domain.exception.BookNotFoundException;
import com.edsonfilho.adventure.book.domain.exception.CategoryAlreadyExistsException;

/**
 * Input port: add a category to an existing book.
 */
public interface AddCategoryUseCase {

    /**
     * @param bookId   the MongoDB id of the book
     * @param category the category name to add (case-insensitive duplicate check)
     * @return the updated book
     * @throws BookNotFoundException          if the book is not found
     * @throws CategoryAlreadyExistsException if the category already exists
     */
    Book execute(String bookId, String category);
}
