package com.edsonfilho.adventure.book.domain.entity;

/**
 * Represents the role of a section within an adventure book's story graph.
 * <ul>
 *   <li>{@link #BEGIN} – The starting section of the book (there is exactly one).</li>
 *   <li>{@link #NODE}  – An intermediate section with one or more choices.</li>
 *   <li>{@link #END}   – A terminal section with no further options (game over / victory).</li>
 * </ul>
 */
public enum SectionType {
    BEGIN,
    NODE,
    END
}
