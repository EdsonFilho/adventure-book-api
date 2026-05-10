package com.edsonfilho.adventure.book.domain.exception;

import java.util.List;

/**
 * Thrown when one or more options point to a section id that does not exist in the book.
 *
 * <p>Carries the full list of broken references so all problems can be reported at once
 * rather than failing on the first one found.</p>
 */
public class InvalidSectionReferenceException extends BookValidationException {

    private final List<String> brokenReferences;

    public InvalidSectionReferenceException(List<String> brokenReferences) {
        super(String.format(
                "The following section references are invalid (section not found): %s",
                brokenReferences));
        this.brokenReferences = List.copyOf(brokenReferences);
    }

    /**
     * Returns an unmodifiable list of broken references in the format
     * {@code "section <sectionId> → gotoId <gotoId>"}.
     */
    public List<String> getBrokenReferences() {
        return brokenReferences;
    }
}
