package com.edsonfilho.adventure.book.domain.exception;

import java.util.List;

/**
 * Thrown when one or more non-END sections have no options defined.
 * Every BEGIN and NODE section must offer at least one choice to the reader.
 *
 * <p>Carries the full list of offending section ids so all problems are reported at once.</p>
 */
public class MissingOptionsException extends BookValidationException {

    private final List<String> sectionIds;

    public MissingOptionsException(List<String> sectionIds) {
        super(String.format(
                "The following non-END sections have no options defined: %s", sectionIds));
        this.sectionIds = List.copyOf(sectionIds);
    }

    /** Returns an unmodifiable list of section ids that are missing options. */
    public List<String> getSectionIds() {
        return sectionIds;
    }
}
