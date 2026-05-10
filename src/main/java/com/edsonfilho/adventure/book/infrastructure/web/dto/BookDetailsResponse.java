package com.edsonfilho.adventure.book.infrastructure.web.dto;

import com.edsonfilho.adventure.book.domain.entity.Difficulty;

import java.util.List;

/**
 * Response DTO for the Get Book Details endpoint.
 *
 * <p>Kept separate from {@link BookSummaryResponse} to allow independent evolution —
 * details may later include extra fields (e.g. section count, play stats) without
 * affecting the list endpoint.</p>
 */
public class BookDetailsResponse {

    private String id;
    private String title;
    private String author;
    private Difficulty difficulty;
    private List<String> categories;

    public BookDetailsResponse() {}

    public BookDetailsResponse(String id, String title, String author,
                               Difficulty difficulty, List<String> categories) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.difficulty = difficulty;
        this.categories = categories;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public Difficulty getDifficulty() { return difficulty; }
    public void setDifficulty(Difficulty difficulty) { this.difficulty = difficulty; }

    public List<String> getCategories() { return categories; }
    public void setCategories(List<String> categories) { this.categories = categories; }
}
