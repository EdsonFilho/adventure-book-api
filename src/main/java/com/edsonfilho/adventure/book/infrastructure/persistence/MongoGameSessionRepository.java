package com.edsonfilho.adventure.book.infrastructure.persistence;

import com.edsonfilho.adventure.book.domain.entity.GameSession;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/** Spring Data MongoDB repository for {@link GameSession} documents. */
public interface MongoGameSessionRepository extends MongoRepository<GameSession, String> {
    List<GameSession> findByPlayerName(String playerName);
}
