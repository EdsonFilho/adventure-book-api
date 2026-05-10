package com.edsonfilho.adventure.book.application.service;

import com.edsonfilho.adventure.book.application.port.in.GameSessionView;
import com.edsonfilho.adventure.book.application.port.in.GetSessionUseCase;
import com.edsonfilho.adventure.book.application.port.out.BookRepositoryPort;
import com.edsonfilho.adventure.book.application.port.out.GameSessionRepositoryPort;
import com.edsonfilho.adventure.book.domain.entity.Book;
import com.edsonfilho.adventure.book.domain.entity.GameSession;
import com.edsonfilho.adventure.book.domain.entity.Section;
import com.edsonfilho.adventure.book.domain.exception.BookNotFoundException;
import com.edsonfilho.adventure.book.domain.exception.GameSessionNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Retrieves the current state of an existing game session.
 */
@Service
public class GetSessionService implements GetSessionUseCase {

    private final GameSessionRepositoryPort gameSessionRepositoryPort;
    private final BookRepositoryPort bookRepositoryPort;

    public GetSessionService(GameSessionRepositoryPort gameSessionRepositoryPort,
                             BookRepositoryPort bookRepositoryPort) {
        this.gameSessionRepositoryPort = gameSessionRepositoryPort;
        this.bookRepositoryPort = bookRepositoryPort;
    }

    @Override
    public GameSessionView execute(String sessionId) {
        GameSession session = gameSessionRepositoryPort.findById(sessionId)
                .orElseThrow(() -> new GameSessionNotFoundException(sessionId));

        Book book = bookRepositoryPort.findById(session.getBookId())
                .orElseThrow(() -> new BookNotFoundException(session.getBookId()));

        Section currentSection = findSection(book, session.getCurrentSectionId());
        return new GameSessionView(session, currentSection);
    }

    private Section findSection(Book book, String sectionId) {
        return book.getSections().stream()
                .filter(s -> sectionId.equals(s.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Section '" + sectionId + "' not found in book '" + book.getId() + "'"));
    }
}
