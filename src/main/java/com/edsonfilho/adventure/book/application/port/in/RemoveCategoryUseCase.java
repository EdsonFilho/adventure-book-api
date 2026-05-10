package com.edsonfilho.adventure.book.application.port.in;

import com.edsonfilho.adventure.book.domain.entity.Book;
import com.edsonfilho.adventure.book.domain.exception.BookNotFoundException;
import com.edsonfilho.adventure.book.domain.exception.CategoryNotFoundException;

/**
 * Input port: remove a category from an existing book.
 */
public interface RemoveCategoryUseCase {

    /**
     * @param bookId   the MongoDB id of the book
     * @param category the category name to remove (case-insensitive)
     * @return the updated book
     * @throws BookNotFoundException     if the book is not found
     * @throws CategoryNotFoundException if the category is not present
     */
    Book execute(String bookId, String category);
}
