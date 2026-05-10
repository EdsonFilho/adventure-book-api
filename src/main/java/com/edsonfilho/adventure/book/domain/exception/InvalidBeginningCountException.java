package com.edsonfilho.adventure.book.domain.exception;

/**
 * Thrown when a book does not have exactly one BEGIN section.
 * A valid book must have precisely one entry point.
 */
public class InvalidBeginningCountException extends BookValidationException {

    private final int count;

    public InvalidBeginningCountException(int count) {
        super(String.format(
                "A book must have exactly one BEGIN section, but found %d.", count));
        this.count = count;
    }

    /** Returns the actual number of BEGIN sections found. */
    public int getCount() {
        return count;
    }
}
