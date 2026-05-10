package com.edsonfilho.adventure.book.application.service;

import com.edsonfilho.adventure.book.application.port.in.AddCategoryUseCase;
import com.edsonfilho.adventure.book.application.port.out.BookRepositoryPort;
import com.edsonfilho.adventure.book.domain.entity.Book;
import com.edsonfilho.adventure.book.domain.exception.BookNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link AddCategoryUseCase}.
 *
 * <p>The duplicate-category check lives in the {@link Book} entity
 * (domain rule), not here. This service only orchestrates: load → mutate → save.</p>
 */
@Service
public class AddCategoryService implements AddCategoryUseCase {

    private final BookRepositoryPort bookRepositoryPort;

    public AddCategoryService(BookRepositoryPort bookRepositoryPort) {
        this.bookRepositoryPort = bookRepositoryPort;
    }

    @Override
    public Book execute(String bookId, String category) {
        Book book = bookRepositoryPort.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));

        book.addCategory(category); // domain rule: throws if duplicate
        return bookRepositoryPort.save(book);
    }
}
