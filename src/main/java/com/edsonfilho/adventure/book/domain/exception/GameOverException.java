package com.edsonfilho.adventure.book.domain.exception;

import com.edsonfilho.adventure.book.domain.entity.GameStatus;

public class GameOverException extends RuntimeException {

    private final String sessionId;
    private final GameStatus status;

    public GameOverException(String sessionId, GameStatus status) {
        super(String.format("Session '%s' is already over with status %s.", sessionId, status));
        this.sessionId = sessionId;
        this.status = status;
    }

    public String getSessionId() { return sessionId; }
    public GameStatus getStatus() { return status; }
}
