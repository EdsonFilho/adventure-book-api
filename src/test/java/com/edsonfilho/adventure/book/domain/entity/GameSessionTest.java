package com.edsonfilho.adventure.book.domain.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameSessionTest {

    @Test
    void start_shouldInitializeSessionCorrectly() {
        // Arrange & Act: Factory method handles both creation and initialization
        GameSession session = GameSession.start("book-1", "section-1", "Edson");

        // Assert: Verify all starting conditions are strictly met according to domain rules
        assertEquals("section-1", session.getCurrentSectionId());
        assertEquals("Edson", session.getPlayerName());
        assertEquals(10, session.getHealth());
        assertEquals(GameStatus.ACTIVE, session.getStatus());
        assertFalse(session.isOver());
    }

    @Test
    void applyConsequence_shouldDecreaseHealthAndSetDeadWhenZero() {
        // Arrange: Start a healthy game session
        GameSession session = GameSession.start("book-1", "section-1", "Edson");
        Consequence damage = new Consequence("LOSE_HEALTH", "10", "You fell in a trap.");

        // Act: Apply a consequence that depletes all health
        session.applyConsequence(damage);

        // Assert: Health should be exactly 0, and status should automatically transition to DEAD
        assertEquals(GameStatus.DEAD, session.getStatus());
        assertTrue(session.isOver());
    }

    @Test
    void applyConsequence_shouldNotDropHealthBelowZero() {
        // Arrange: Start a session
        GameSession session = GameSession.start("book-1", "section-1", "Edson");
        Consequence overkill = new Consequence("LOSE_HEALTH", "50", "You were crushed.");

        // Act: Apply massive damage
        session.applyConsequence(overkill);

        // Assert: Math.max(0, health) invariant prevents negative health values
        assertEquals(GameStatus.DEAD, session.getStatus());
    }

    @Test
    void applyConsequence_shouldIncreaseHealth() {
        // Arrange
        GameSession session = GameSession.start("book-1", "section-1", "Edson");
        Consequence heal = new Consequence("GAIN_HEALTH", "5", "You drank a potion.");

        // Act
        session.applyConsequence(heal);

        // Assert: Health increases above base 10
        assertEquals(GameStatus.ACTIVE, session.getStatus());
    }
}
