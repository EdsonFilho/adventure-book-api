package com.edsonfilho.adventure.book.infrastructure.web.dto;

import java.util.List;

/**
 * Unified response DTO for all three game session endpoints.
 * The {@code consequence} field is {@code null} on GET (no move was made).
 */
public class GameSessionResponse {

    private String sessionId;
    private String bookId;
    private String playerName;
    private int health;
    private String status;
    private SectionResponse currentSection;
    private ConsequenceResponse consequence; // null when there was no consequence

    public GameSessionResponse() {}

    // -----------------------------------------------------------------------
    // Nested DTOs (defined as inner static classes for locality)
    // -----------------------------------------------------------------------

    public static class SectionResponse {
        private String id;
        private String text;
        private String type;
        private List<OptionResponse> options;

        public SectionResponse() {}
        public SectionResponse(String id, String text, String type, List<OptionResponse> options) {
            this.id = id; this.text = text; this.type = type; this.options = options;
        }
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public List<OptionResponse> getOptions() { return options; }
        public void setOptions(List<OptionResponse> options) { this.options = options; }
    }

    public static class OptionResponse {
        private int index;
        private String description;

        public OptionResponse() {}
        public OptionResponse(int index, String description) {
            this.index = index; this.description = description;
        }
        public int getIndex() { return index; }
        public void setIndex(int index) { this.index = index; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    public static class ConsequenceResponse {
        private String type;
        private String value;
        private String text;

        public ConsequenceResponse() {}
        public ConsequenceResponse(String type, String value, String text) {
            this.type = type; this.value = value; this.text = text;
        }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getValue() { return value; }
        public void setValue(String value) { this.value = value; }
        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
    }

    // -----------------------------------------------------------------------
    // Getters / Setters
    // -----------------------------------------------------------------------

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
    public SectionResponse getCurrentSection() { return currentSection; }
    public void setCurrentSection(SectionResponse currentSection) { this.currentSection = currentSection; }
    public ConsequenceResponse getConsequence() { return consequence; }
    public void setConsequence(ConsequenceResponse consequence) { this.consequence = consequence; }
}
