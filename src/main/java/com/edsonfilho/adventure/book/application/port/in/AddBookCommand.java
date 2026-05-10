package com.edsonfilho.adventure.book.application.port.in;

import com.edsonfilho.adventure.book.domain.entity.Difficulty;
import com.edsonfilho.adventure.book.domain.entity.Section;

import java.util.List;

/**
 * Command object for the {@link AddBookUseCase}.
 *
 * <p>Carries the full book payload submitted by the API client.
 * Sections (and their embedded options) are accepted as-is from the JSON — the
 * book is always created as a complete unit, never section-by-section.</p>
 */
public class AddBookCommand {

    private final String title;
    private final String author;
    private final Difficulty difficulty;
    private final List<String> categories;
    private final List<Section> sections;

    public AddBookCommand(String title,
                          String author,
                          Difficulty difficulty,
                          List<String> categories,
                          List<Section> sections) {
        this.title = title;
        this.author = author;
        this.difficulty = difficulty;
        this.categories = categories != null ? categories : List.of();
        this.sections = sections;
    }

    public String getTitle()             { return title; }
    public String getAuthor()            { return author; }
    public Difficulty getDifficulty()    { return difficulty; }
    public List<String> getCategories() { return categories; }
    public List<Section> getSections()  { return sections; }
}
