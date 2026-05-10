package com.edsonfilho.adventure.book.application.service;

import com.edsonfilho.adventure.book.application.port.in.RemoveCategoryUseCase;
import com.edsonfilho.adventure.book.application.port.out.BookRepositoryPort;
import com.edsonfilho.adventure.book.domain.entity.Book;
import com.edsonfilho.adventure.book.domain.exception.BookNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link RemoveCategoryUseCase}.
 *
 * <p>The "category must exist" check lives in the {@link Book} entity
 * (domain rule), not here.</p>
 */
@Service
public class RemoveCategoryService implements RemoveCategoryUseCase {

    private final BookRepositoryPort bookRepositoryPort;

    public RemoveCategoryService(BookRepositoryPort bookRepositoryPort) {
        this.bookRepositoryPort = bookRepositoryPort;
    }

    @Override
    public Book execute(String bookId, String category) {
        Book book = bookRepositoryPort.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));

        book.removeCategory(category); // domain rule: throws if not found
        return bookRepositoryPort.save(book);
    }
}
