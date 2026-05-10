package com.edsonfilho.adventure.book.domain.entity;

/**
 * Represents one choice available to the reader at the end of a section.
 * Stored as an embedded document inside {@link Section}.
 *
 * Example JSON:
 * <pre>
 * {
 *   "description": "You look under the bed",
 *   "gotoId": 20,
 *   "consequence": { ... }   // optional
 * }
 * </pre>
 *
 * A class is used instead of a record because {@code consequence} is an optional
 * field (absent in many options), which requires null-safe handling at the setter level.
 */
public class Option {

    private String description;
    private int gotoId;
    private Consequence consequence; // optional – may be null

    public Option() {}

    public Option(String description, int gotoId, Consequence consequence) {
        this.description = description;
        this.gotoId = gotoId;
        this.consequence = consequence;
    }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getGotoId() { return gotoId; }
    public void setGotoId(int gotoId) { this.gotoId = gotoId; }

    public Consequence getConsequence() { return consequence; }
    public void setConsequence(Consequence consequence) { this.consequence = consequence; }
}
