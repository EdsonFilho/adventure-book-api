package com.edsonfilho.adventure.book.domain.exception;

/**
 * Base exception for all book structural validation failures.
 * Extend this class to represent specific rule violations.
 */
public class BookValidationException extends RuntimeException {

    public BookValidationException(String message) {
        super(message);
    }
}
