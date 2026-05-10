package com.edsonfilho.adventure.book.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents one player's active reading session for a specific {@link Book}.
 *
 * <p>Business invariants enforced here (domain layer):</p>
 * <ul>
 *   <li>Health never goes below 0.</li>
 *   <li>Only {@link GameStatus#ACTIVE} sessions accept consequences and moves.</li>
 * </ul>
 */
@Document(collection = "game_sessions")
public class GameSession {

    private static final int STARTING_HEALTH = 10;

    @Id
    private String id;
    private String bookId;
    private String currentSectionId;
    private String playerName;
    private int health;
    private GameStatus status;

    /** Required by Spring Data. */
    public GameSession() {}

    // -------------------------------------------------------------------------
    // Factory
    // -------------------------------------------------------------------------

    /**
     * Creates a new session at the beginning of the given book.
     *
     * @param bookId         the MongoDB id of the book being played
     * @param beginSectionId the id of the book's BEGIN section
     * @param playerName     the name of the player starting the session
     */
    public static GameSession start(String bookId, String beginSectionId, String playerName) {
        GameSession session = new GameSession();
        session.bookId = bookId;
        session.currentSectionId = beginSectionId;
        session.playerName = playerName;
        session.health = STARTING_HEALTH;
        session.status = GameStatus.ACTIVE;
        return session;
    }

    // -------------------------------------------------------------------------
    // Domain behaviour
    // -------------------------------------------------------------------------

    /**
     * Applies a consequence to this session (health change).
     * If health reaches 0, the session is marked {@link GameStatus#DEAD}.
     */
    public void applyConsequence(Consequence consequence) {
        if (consequence == null) return;

        int delta = Integer.parseInt(consequence.getValue());

        if ("LOSE_HEALTH".equals(consequence.getType())) {
            this.health = Math.max(0, this.health - delta);
            if (this.health == 0) {
                this.status = GameStatus.DEAD;
            }
        } else if ("GAIN_HEALTH".equals(consequence.getType())) {
            this.health += delta;
        }
    }

    /**
     * Moves the session to a new section. Always called — even if the player
     * just died — so the narrative still advances to the destination.
     */
    public void moveTo(String sectionId) {
        this.currentSectionId = sectionId;
    }

    /** Marks the session as successfully completed. */
    public void complete() {
        this.status = GameStatus.COMPLETED;
    }

    /** Returns {@code true} if no further moves are allowed. */
    public boolean isOver() {
        return status == GameStatus.DEAD || status == GameStatus.COMPLETED;
    }

    // -------------------------------------------------------------------------
    // Getters / Setters
    // -------------------------------------------------------------------------

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getBookId() { return bookId; }
    public void setBookId(String bookId) { this.bookId = bookId; }

    public String getCurrentSectionId() { return currentSectionId; }
    public void setCurrentSectionId(String currentSectionId) { this.currentSectionId = currentSectionId; }

    public String getPlayerName() { return playerName; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }

    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = health; }

    public GameStatus getStatus() { return status; }
    public void setStatus(GameStatus status) { this.status = status; }
}
