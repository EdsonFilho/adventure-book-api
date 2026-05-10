package com.edsonfilho.adventure.book.domain.exception;

/**
 * Thrown when a category being removed does not exist on the book (case-insensitive).
 */
public class CategoryNotFoundException extends RuntimeException {

    private final String category;

    public CategoryNotFoundException(String category) {
        super(String.format("Category '%s' was not found on this book.", category));
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}
