package com.edsonfilho.adventure.book.application.service;

import com.edsonfilho.adventure.book.application.port.in.GetBookUseCase;
import com.edsonfilho.adventure.book.application.port.out.BookRepositoryPort;
import com.edsonfilho.adventure.book.domain.entity.Book;
import com.edsonfilho.adventure.book.domain.exception.BookNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link GetBookUseCase}.
 */
@Service
public class GetBookService implements GetBookUseCase {

    private final BookRepositoryPort bookRepositoryPort;

    public GetBookService(BookRepositoryPort bookRepositoryPort) {
        this.bookRepositoryPort = bookRepositoryPort;
    }

    @Override
    public Book execute(String bookId) {
        return bookRepositoryPort.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));
    }
}
