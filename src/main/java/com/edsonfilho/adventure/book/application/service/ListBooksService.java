package com.edsonfilho.adventure.book.application.service;

import com.edsonfilho.adventure.book.application.port.in.BookSearchQuery;
import com.edsonfilho.adventure.book.application.port.in.ListBooksUseCase;
import com.edsonfilho.adventure.book.application.port.out.BookRepositoryPort;
import com.edsonfilho.adventure.book.domain.entity.Book;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the {@link ListBooksUseCase}.
 *
 * <p>This class belongs to the application layer. It orchestrates the use case but
 * contains no framework-specific code — it only speaks in terms of domain objects
 * and port interfaces.</p>
 *
 * <p>The actual data retrieval is delegated to {@link BookRepositoryPort}, whose
 * concrete implementation (MongoTemplate-based) lives in the infrastructure layer.</p>
 */
@Service
public class ListBooksService implements ListBooksUseCase {

    private final BookRepositoryPort bookRepositoryPort;

    public ListBooksService(BookRepositoryPort bookRepositoryPort) {
        this.bookRepositoryPort = bookRepositoryPort;
    }

    @Override
    public List<Book> execute(BookSearchQuery query) {
        return bookRepositoryPort.search(query);
    }
}
