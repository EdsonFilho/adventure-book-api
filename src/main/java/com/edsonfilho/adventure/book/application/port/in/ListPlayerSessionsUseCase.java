package com.edsonfilho.adventure.book.application.port.in;

import com.edsonfilho.adventure.book.domain.entity.GameSession;
import java.util.List;

/** Input port: list all game sessions for a given player. */
public interface ListPlayerSessionsUseCase {
    List<GameSession> execute(String playerName);
}
