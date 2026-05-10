package com.edsonfilho.adventure.book.application.service;

import com.edsonfilho.adventure.book.application.port.in.ListPlayerSessionsUseCase;
import com.edsonfilho.adventure.book.application.port.out.GameSessionRepositoryPort;
import com.edsonfilho.adventure.book.domain.entity.GameSession;
import org.springframework.stereotype.Service;

import java.util.List;

/** Retrieves all sessions linked to a specific player name. */
@Service
public class ListPlayerSessionsService implements ListPlayerSessionsUseCase {

    private final GameSessionRepositoryPort gameSessionRepositoryPort;

    public ListPlayerSessionsService(GameSessionRepositoryPort gameSessionRepositoryPort) {
        this.gameSessionRepositoryPort = gameSessionRepositoryPort;
    }

    @Override
    public List<GameSession> execute(String playerName) {
        return gameSessionRepositoryPort.findByPlayerName(playerName);
    }
}
