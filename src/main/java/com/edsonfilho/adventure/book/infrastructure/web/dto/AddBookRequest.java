package com.edsonfilho.adventure.book.infrastructure.web.dto;

import com.edsonfilho.adventure.book.domain.entity.Difficulty;
import com.edsonfilho.adventure.book.domain.entity.Section;

import java.util.List;

/**
 * Request body DTO for {@code POST /api/books}.
 *
 * <p>The book is submitted as a complete JSON document — title, author,
 * difficulty, categories, and the full section graph (sections + options)
 * in one payload. Sections are never created or edited individually.</p>
 *
 * <p>Example payload:</p>
 * <pre>
 * {
 *   "title": "The Lost Temple",
 *   "author": "J. Explorer",
 *   "difficulty": "MEDIUM",
 *   "categories": ["adventure", "mystery"],
 *   "sections": [
 *     { "id": "1", "text": "You stand before a temple.", "type": "BEGIN",
 *       "options": [{ "description": "Enter", "gotoId": 2 }] },
 *     { "id": "2", "text": "You find treasure!", "type": "END", "options": [] }
 *   ]
 * }
 * </pre>
 */
public class AddBookRequest {

    private String title;
    private String author;
    private Difficulty difficulty;
    private List<String> categories;
    private List<Section> sections;

    public AddBookRequest() {}

    public String getTitle()             { return title; }
    public void setTitle(String title)   { this.title = title; }

    public String getAuthor()              { return author; }
    public void setAuthor(String author)   { this.author = author; }

    public Difficulty getDifficulty()                    { return difficulty; }
    public void setDifficulty(Difficulty difficulty)     { this.difficulty = difficulty; }

    public List<String> getCategories()                  { return categories; }
    public void setCategories(List<String> categories)   { this.categories = categories; }

    public List<Section> getSections()                   { return sections; }
    public void setSections(List<Section> sections)      { this.sections = sections; }
}
