package com.edsonfilho.adventure.book.application.service;

import com.edsonfilho.adventure.book.application.port.in.StartGameCommand;
import com.edsonfilho.adventure.book.application.port.in.StartGameUseCase;
import com.edsonfilho.adventure.book.application.port.out.BookRepositoryPort;
import com.edsonfilho.adventure.book.application.port.out.GameSessionRepositoryPort;
import com.edsonfilho.adventure.book.domain.entity.Book;
import com.edsonfilho.adventure.book.domain.entity.GameSession;
import com.edsonfilho.adventure.book.domain.entity.Section;
import com.edsonfilho.adventure.book.domain.entity.SectionType;
import com.edsonfilho.adventure.book.domain.exception.BookNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Creates a new game session at the BEGIN section of the requested book.
 */
@Service
public class StartGameService implements StartGameUseCase {

    private final BookRepositoryPort bookRepositoryPort;
    private final GameSessionRepositoryPort gameSessionRepositoryPort;

    public StartGameService(BookRepositoryPort bookRepositoryPort,
                            GameSessionRepositoryPort gameSessionRepositoryPort) {
        this.bookRepositoryPort = bookRepositoryPort;
        this.gameSessionRepositoryPort = gameSessionRepositoryPort;
    }

    @Override
    public GameSession execute(StartGameCommand command) {
        Book book = bookRepositoryPort.findById(command.getBookId())
                .orElseThrow(() -> new BookNotFoundException(command.getBookId()));

        Section beginSection = book.getSections().stream()
                .filter(s -> SectionType.BEGIN.equals(s.getType()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Book '" + command.getBookId() + "' has no BEGIN section — validation was not run at import time."));

        GameSession session = GameSession.start(command.getBookId(), beginSection.getId(), command.getPlayerName());
        return gameSessionRepositoryPort.save(session);
    }
}
