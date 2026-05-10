package com.edsonfilho.adventure.book.application.validation;

import com.edsonfilho.adventure.book.domain.entity.Book;
import com.edsonfilho.adventure.book.domain.entity.Option;
import com.edsonfilho.adventure.book.domain.entity.Section;
import com.edsonfilho.adventure.book.domain.exception.InvalidSectionReferenceException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Validates that every option's {@code gotoId} refers to a section that
 * actually exists in the book.
 *
 * <p>
 * Rule 3: A book should be invalid if it has an invalid next section id.
 * </p>
 */
@Component
public class SectionReferenceRule implements BookValidationRule {

    @Override
    public void validate(Book book) {
        if (book.getSections() == null) {
            return;
        }

        Set<String> validIds = book.getSections().stream()
                .map(Section::getId)
                .collect(Collectors.toSet());

        List<String> brokenReferences = new ArrayList<>();

        for (Section section : book.getSections()) {
            if (section.getOptions() == null)
                continue;

            for (Option option : section.getOptions()) {
                String gotoId = String.valueOf(option.getGotoId());
                if (!validIds.contains(gotoId)) {
                    brokenReferences.add(String.format(
                            "section '%s' → gotoId '%s'", section.getId(), gotoId));
                }
            }
        }

        if (!brokenReferences.isEmpty()) {
            throw new InvalidSectionReferenceException(brokenReferences);
        }
    }
}
