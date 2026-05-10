package com.edsonfilho.adventure.book.application.validation;

import com.edsonfilho.adventure.book.domain.entity.Book;
import com.edsonfilho.adventure.book.domain.entity.Section;
import com.edsonfilho.adventure.book.domain.entity.SectionType;
import com.edsonfilho.adventure.book.domain.exception.MissingOptionsException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Validates that every non-END section has at least one option defined.
 *
 * <p>
 * Rule 4: A book should be invalid if it has a non-ending section with no
 * options.
 * </p>
 */
@Component
public class NonEndingSectionOptionsRule implements BookValidationRule {

    @Override
    public void validate(Book book) {
        if (book.getSections() == null) {
            return;
        }

        List<String> sectionsWithoutOptions = book.getSections().stream()
                .filter(s -> !SectionType.END.equals(s.getType()))
                .filter(s -> s.getOptions() == null || s.getOptions().isEmpty())
                .map(Section::getId)
                .collect(Collectors.toList());

        if (!sectionsWithoutOptions.isEmpty()) {
            throw new MissingOptionsException(sectionsWithoutOptions);
        }
    }
}
