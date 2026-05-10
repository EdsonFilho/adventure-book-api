package com.edsonfilho.adventure.book.domain.entity;

import com.edsonfilho.adventure.book.domain.exception.CategoryAlreadyExistsException;
import com.edsonfilho.adventure.book.domain.exception.CategoryNotFoundException;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Root document stored in the "books" MongoDB collection.
 * Represents an interactive adventure book with branching sections.
 */
@Document(collection = "books")
public class Book {

    @Id
    private String id; // MongoDB's internal ObjectId

    private String title;
    private String author;
    private Difficulty difficulty;
    private List<String> categories;
    private List<Section> sections;

    public Book() {}

    public Book(String id, String title, String author, Difficulty difficulty,
                List<String> categories, List<Section> sections) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.difficulty = difficulty;
        this.categories = categories;
        this.sections = sections;
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

    public List<Section> getSections() { return sections; }
    public void setSections(List<Section> sections) { this.sections = sections; }

    // -------------------------------------------------------------------------
    // Domain behaviour
    // -------------------------------------------------------------------------

    /**
     * Adds a category to this book.
     * Comparison is case-insensitive to avoid near-duplicate entries.
     *
     * @param category the category to add
     * @throws CategoryAlreadyExistsException if the category is already present
     */
    public void addCategory(String category) {
        if (this.categories == null) {
            this.categories = new ArrayList<>();
        }
        boolean alreadyExists = this.categories.stream()
                .anyMatch(c -> c.equalsIgnoreCase(category));
        if (alreadyExists) {
            throw new CategoryAlreadyExistsException(category);
        }
        this.categories.add(category);
    }

    /**
     * Removes a category from this book.
     * Comparison is case-insensitive.
     *
     * @param category the category to remove
     * @throws CategoryNotFoundException if the category is not present
     */
    public void removeCategory(String category) {
        if (this.categories == null || this.categories.isEmpty()) {
            throw new CategoryNotFoundException(category);
        }
        boolean removed = this.categories.removeIf(c -> c.equalsIgnoreCase(category));
        if (!removed) {
            throw new CategoryNotFoundException(category);
        }
    }
}