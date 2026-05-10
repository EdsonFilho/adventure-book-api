package com.edsonfilho.adventure.book.application.port.out;

import com.edsonfilho.adventure.book.application.port.in.BookSearchQuery;
import com.edsonfilho.adventure.book.domain.entity.Book;

import java.util.List;
import java.util.Optional;

/**
 * Output port for book persistence.
 *
 * <p>The application layer depends on this interface only — no Spring Data or
 * Mongo types ever appear here.</p>
 */
public interface BookRepositoryPort {

    /**
     * Returns all books matching the given search criteria.
     * Blank/null fields in {@code query} are ignored.
     */
    List<Book> search(BookSearchQuery query);

    /**
     * Finds a single book by its id.
     *
     * @return an {@link Optional} containing the book, or empty if not found
     */
    Optional<Book> findById(String id);

    /**
     * Persists (inserts or updates) a book and returns the saved state.
     */
    Book save(Book book);
}
