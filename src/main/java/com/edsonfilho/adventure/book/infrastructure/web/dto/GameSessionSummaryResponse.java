package com.edsonfilho.adventure.book.infrastructure.web.dto;

/** Lightweight summary of a GameSession for listing purposes. */
public class GameSessionSummaryResponse {
    private String sessionId;
    private String bookId;
    private String playerName;
    private int health;
    private String status;

    public GameSessionSummaryResponse(String sessionId, String bookId, String playerName, int health, String status) {
        this.sessionId = sessionId;
        this.bookId = bookId;
        this.playerName = playerName;
        this.health = health;
        this.status = status;
    }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public String getBookId() { return bookId; }
    public void setBookId(String bookId) { this.bookId = bookId; }

    public String getPlayerName() { return playerName; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }

    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = health; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
