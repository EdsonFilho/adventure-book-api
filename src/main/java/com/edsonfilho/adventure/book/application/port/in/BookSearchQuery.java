package com.edsonfilho.adventure.book.application.port.in;

/**
 * Encapsulates the optional filter parameters for the "list books" use case.
 *
 * <p>All fields are optional. A {@code null} or blank value means "no filter on this field".</p>
 */
public class BookSearchQuery {

    private final String title;
    private final String author;
    private final String category;
    private final String difficulty;

    public BookSearchQuery(String title, String author, String category, String difficulty) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.difficulty = difficulty;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    public String getDifficulty() { return difficulty; }
}
