package com.edsonfilho.adventure.book.infrastructure.web.dto;

/**
 * Request body for adding a category to a book.
 *
 * <pre>
 * POST /api/books/{id}/categories
 * { "category": "fantasy" }
 * </pre>
 */
public class CategoryRequest {

    private String category;

    public CategoryRequest() {}

    public CategoryRequest(String category) {
        this.category = category;
    }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}
