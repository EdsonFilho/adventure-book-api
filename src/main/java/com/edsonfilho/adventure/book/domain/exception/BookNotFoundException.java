package com.edsonfilho.adventure.book.domain.exception;

/**
 * Thrown when a book with the given id cannot be found in the repository.
 */
public class BookNotFoundException extends RuntimeException {

    private final String bookId;

    public BookNotFoundException(String bookId) {
        super(String.format("Book with id '%s' was not found.", bookId));
        this.bookId = bookId;
    }

    public String getBookId() {
        return bookId;
    }
}
