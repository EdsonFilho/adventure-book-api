package com.edsonfilho.adventure.book.application.service;

import com.edsonfilho.adventure.book.application.port.in.GameSessionView;
import com.edsonfilho.adventure.book.application.port.in.MakeMoveResult;
import com.edsonfilho.adventure.book.application.port.in.MakeMoveUseCase;
import com.edsonfilho.adventure.book.application.port.out.BookRepositoryPort;
import com.edsonfilho.adventure.book.application.port.out.GameSessionRepositoryPort;
import com.edsonfilho.adventure.book.domain.entity.Book;
import com.edsonfilho.adventure.book.domain.entity.Consequence;
import com.edsonfilho.adventure.book.domain.entity.GameSession;
import com.edsonfilho.adventure.book.domain.entity.Option;
import com.edsonfilho.adventure.book.domain.entity.Section;
import com.edsonfilho.adventure.book.domain.entity.SectionType;
import com.edsonfilho.adventure.book.domain.exception.BookNotFoundException;
import com.edsonfilho.adventure.book.domain.exception.GameOverException;
import com.edsonfilho.adventure.book.domain.exception.GameSessionNotFoundException;
import com.edsonfilho.adventure.book.domain.exception.InvalidOptionException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Orchestrates a single player move:
 * validate → apply consequence → move → check completion → persist.
 */
@Service
public class MakeMoveService implements MakeMoveUseCase {

    private final GameSessionRepositoryPort gameSessionRepositoryPort;
    private final BookRepositoryPort bookRepositoryPort;

    public MakeMoveService(GameSessionRepositoryPort gameSessionRepositoryPort,
                           BookRepositoryPort bookRepositoryPort) {
        this.gameSessionRepositoryPort = gameSessionRepositoryPort;
        this.bookRepositoryPort = bookRepositoryPort;
    }

    @Override
    public MakeMoveResult execute(String sessionId, int optionIndex) {
        // 1. Load session
        GameSession session = gameSessionRepositoryPort.findById(sessionId)
                .orElseThrow(() -> new GameSessionNotFoundException(sessionId));

        // 2. Reject moves on finished sessions
        if (session.isOver()) {
            throw new GameOverException(sessionId, session.getStatus());
        }

        // 3. Load book and find the current section
        // bookId captured separately — session is reassigned later, so it would not be effectively final inside a lambda
        final String bookId = session.getBookId();
        Book book = bookRepositoryPort.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));

        Section currentSection = findSection(book, session.getCurrentSectionId());

        // 4. Validate the chosen option index
        List<Option> options = currentSection.getOptions();
        if (options == null || optionIndex < 0 || optionIndex >= options.size()) {
            throw new InvalidOptionException(optionIndex);
        }
        Option chosenOption = options.get(optionIndex);

        // 5. Apply consequence (may set DEAD status on session)
        Consequence consequence = chosenOption.getConsequence();
        session.applyConsequence(consequence);

        // 6. Always move to gotoId — even if dead, so the narrative advances
        session.moveTo(String.valueOf(chosenOption.getGotoId()));

        // 7. Check if the new section is an END (only if still alive)
        if (!session.isOver()) {
            Section nextSection = findSection(book, session.getCurrentSectionId());
            if (SectionType.END.equals(nextSection.getType())) {
                session.complete();
            }
        }

        // 8. Persist updated session
        session = gameSessionRepositoryPort.save(session);

        // 9. Rebuild the current section after the move
        Section sectionAfterMove = findSection(book, session.getCurrentSectionId());
        GameSessionView view = new GameSessionView(session, sectionAfterMove);
        return new MakeMoveResult(view, consequence);
    }

    private Section findSection(Book book, String sectionId) {
        return book.getSections().stream()
                .filter(s -> sectionId.equals(s.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Section '" + sectionId + "' not found in book '" + book.getId() + "'"));
    }
}
