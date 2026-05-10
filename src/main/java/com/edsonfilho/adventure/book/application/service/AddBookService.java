package com.edsonfilho.adventure.book.application.service;

import com.edsonfilho.adventure.book.application.port.in.AddBookCommand;
import com.edsonfilho.adventure.book.application.port.in.AddBookUseCase;
import com.edsonfilho.adventure.book.application.port.out.BookRepositoryPort;
import com.edsonfilho.adventure.book.application.validation.BookValidator;
import com.edsonfilho.adventure.book.domain.entity.Book;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link AddBookUseCase}.
 *
 * <p>Orchestration: map command → validate → save.
 * The {@link BookValidator} chain (BEGIN section, END section, options on
 * non-END sections, valid cross-section references) runs automatically and
 * throws {@code BookValidationException} on the first failing rule.</p>
 */
@Service
public class AddBookService implements AddBookUseCase {

    private final BookRepositoryPort bookRepositoryPort;
    private final BookValidator bookValidator;

    public AddBookService(BookRepositoryPort bookRepositoryPort,
                          BookValidator bookValidator) {
        this.bookRepositoryPort = bookRepositoryPort;
        this.bookValidator = bookValidator;
    }

    @Override
    public Book execute(AddBookCommand command) {
        // Map command → domain entity (id is null — MongoDB assigns it on save)
        Book book = new Book(
                null,
                command.getTitle(),
                command.getAuthor(),
                command.getDifficulty(),
                command.getCategories(),
                command.getSections()
        );

        // Validate structural integrity before touching persistence
        bookValidator.validate(book);

        return bookRepositoryPort.save(book);
    }
}
