package com.edsonfilho.adventure.book.application.port.in;

import com.edsonfilho.adventure.book.domain.entity.GameSession;

/** Input port: start a new game session for a given book. */
public interface StartGameUseCase {
    GameSession execute(StartGameCommand command);
}
