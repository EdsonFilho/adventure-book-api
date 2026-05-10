package com.edsonfilho.adventure.book.domain.exception;

public class GameSessionNotFoundException extends RuntimeException {

    private final String sessionId;

    public GameSessionNotFoundException(String sessionId) {
        super(String.format("Game session with id '%s' was not found.", sessionId));
        this.sessionId = sessionId;
    }

    public String getSessionId() { return sessionId; }
}
