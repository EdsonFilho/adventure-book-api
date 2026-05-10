package com.edsonfilho.adventure.book.domain.entity;

/**
 * Represents the side-effect of choosing an option.
 * Stored as an embedded document inside {@link Option}.
 *
 * Example JSON:
 * <pre>
 * {
 *   "type": "LOSE_HEALTH",
 *   "value": "6",
 *   "text": "You cut yourself on a rusty nail."
 * }
 * </pre>
 */
public class Consequence {

    private String type;
    private String value;
    private String text;

    public Consequence() {}

    public Consequence(String type, String value, String text) {
        this.type = type;
        this.value = value;
        this.text = text;
    }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}
