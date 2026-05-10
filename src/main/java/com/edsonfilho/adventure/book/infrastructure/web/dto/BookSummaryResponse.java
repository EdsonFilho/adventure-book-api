package com.edsonfilho.adventure.book.infrastructure.web.dto;

import com.edsonfilho.adventure.book.domain.entity.Difficulty;

import java.util.List;

/**
 * Response DTO for the List/Search Books API.
 *
 * <p>Intentionally excludes the {@code sections} tree — that data is large and
 * only relevant when a reader actually opens a specific book.</p>
 *
 * <p>This class lives in the infrastructure/web layer because it is a web adapter
 * concern: the shape of the JSON response is decided here, independently of the
 * domain model.</p>
 */
public class BookSummaryResponse {

    private String id;
    private String title;
    private String author;
    private Difficulty difficulty;
    private List<String> categories;

    public BookSummaryResponse() {}

    public BookSummaryResponse(String id, String title, String author,
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
