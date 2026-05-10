package com.edsonfilho.adventure.book.domain.exception;

public class InvalidOptionException extends RuntimeException {

    private final int optionIndex;

    public InvalidOptionException(int optionIndex) {
        super(String.format("Option index %d is not valid for the current section.", optionIndex));
        this.optionIndex = optionIndex;
    }

    public int getOptionIndex() { return optionIndex; }
}
