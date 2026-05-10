package com.edsonfilho.adventure.book.application.port.out;

import com.edsonfilho.adventure.book.domain.entity.GameSession;

import java.util.List;
import java.util.Optional;

/** Output port for game session persistence. */
public interface GameSessionRepositoryPort {
    Optional<GameSession> findById(String id);
    List<GameSession> findByPlayerName(String playerName);
    GameSession save(GameSession session);
}
