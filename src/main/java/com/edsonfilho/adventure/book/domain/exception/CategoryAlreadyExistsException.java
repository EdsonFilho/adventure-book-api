package com.edsonfilho.adventure.book.domain.exception;

/**
 * Thrown when a category being added already exists on the book (case-insensitive).
 */
public class CategoryAlreadyExistsException extends RuntimeException {

    private final String category;

    public CategoryAlreadyExistsException(String category) {
        super(String.format("Category '%s' already exists on this book.", category));
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}
