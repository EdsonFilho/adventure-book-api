package com.edsonfilho.adventure.book.application.port.in;

import com.edsonfilho.adventure.book.domain.entity.Book;

import java.util.List;

/**
 * Input port for the "list / search books" use case.
 *
 * <p>The controller (infrastructure layer) depends on this interface, never on the
 * concrete service class. This keeps the web adapter decoupled from the implementation.</p>
 */
public interface ListBooksUseCase {

    /**
     * Returns all books that match the supplied search criteria.
     * Any field left blank in {@code query} is ignored (not filtered on).
     *
     * @param query the optional search filters
     * @return list of matching books, never {@code null}
     */
    List<Book> execute(BookSearchQuery query);
}
