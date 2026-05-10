package com.edsonfilho.adventure.book.application.port.in;

import com.edsonfilho.adventure.book.domain.entity.Section;
import com.edsonfilho.adventure.book.domain.entity.GameSession;

/**
 * View object returned by {@link GetSessionUseCase} and wrapped inside
 * {@link MakeMoveResult}.
 * Contains everything the web adapter needs to render the current game state.
 */
public class GameSessionView {

    private final GameSession session;
    private final Section currentSection;

    public GameSessionView(GameSession session, Section currentSection) {
        this.session = session;
        this.currentSection = currentSection;
    }

    public GameSession getSession() {
        return session;
    }

    public Section getCurrentSection() {
        return currentSection;
    }
}
