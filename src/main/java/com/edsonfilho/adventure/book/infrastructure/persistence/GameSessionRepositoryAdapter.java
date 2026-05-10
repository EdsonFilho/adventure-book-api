package com.edsonfilho.adventure.book.infrastructure.persistence;

import com.edsonfilho.adventure.book.application.port.out.GameSessionRepositoryPort;
import com.edsonfilho.adventure.book.domain.entity.GameSession;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/** Adapter implementing {@link GameSessionRepositoryPort} via Spring Data Mongo. */
@Component
public class GameSessionRepositoryAdapter implements GameSessionRepositoryPort {

    private final MongoGameSessionRepository mongoGameSessionRepository;

    public GameSessionRepositoryAdapter(MongoGameSessionRepository mongoGameSessionRepository) {
        this.mongoGameSessionRepository = mongoGameSessionRepository;
    }

    @Override
    public Optional<GameSession> findById(String id) {
        return mongoGameSessionRepository.findById(id);
    }

    @Override
    public GameSession save(GameSession session) {
        return mongoGameSessionRepository.save(session);
    }

    @Override
    public List<GameSession> findByPlayerName(String playerName) {
        return mongoGameSessionRepository.findByPlayerName(playerName);
    }
}
