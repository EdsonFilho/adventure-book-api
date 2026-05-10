package com.edsonfilho.adventure.book.domain.entity;

/**
 * Lifecycle status of a {@link GameSession}.
 */
public enum GameStatus {
    /** The player is alive and has not yet reached an END section. */
    ACTIVE,
    /** The player's health reached zero. No more moves are accepted. */
    DEAD,
    /** The player reached an END section successfully. */
    COMPLETED
}
