package com.edsonfilho.adventure.book.domain.exception;

/**
 * Thrown when a book has no END sections at all.
 * A valid book must have at least one terminal section so the story can conclude.
 */
public class NoEndingSectionException extends BookValidationException {

    public NoEndingSectionException() {
        super("A book must have at least one END section, but none were found.");
    }
}
