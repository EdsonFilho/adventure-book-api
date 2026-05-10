package com.edsonfilho.adventure.book.domain.entity;

import java.util.List;

/**
 * Represents a single node in the book's story graph.
 * Stored as an embedded document inside {@link Book}.
 *
 * Example JSON:
 * <pre>
 * {
 *   "id": 20,
 *   "text": "You don't see anything, it's too dark.",
 *   "type": "NODE",
 *   "options": [ ... ]
 * }
 * </pre>
 *
 * Note: in the JSON files the section {@code id} is sometimes an integer and
 * sometimes a quoted string (e.g. "500"). Using {@code String} handles both cases
 * transparently during deserialization.
 */
public class Section {

    private String id;
    private String text;
    private SectionType type;
    private List<Option> options; // may be null or empty for END sections

    public Section() {}

    public Section(String id, String text, SectionType type, List<Option> options) {
        this.id = id;
        this.text = text;
        this.type = type;
        this.options = options;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public SectionType getType() { return type; }
    public void setType(SectionType type) { this.type = type; }

    public List<Option> getOptions() { return options; }
    public void setOptions(List<Option> options) { this.options = options; }
}
