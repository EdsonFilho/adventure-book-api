package com.edsonfilho.adventure.book.infrastructure.web;

import com.edsonfilho.adventure.book.application.port.in.AddBookCommand;
import com.edsonfilho.adventure.book.application.port.in.AddBookUseCase;
import com.edsonfilho.adventure.book.application.port.in.AddCategoryUseCase;
import com.edsonfilho.adventure.book.application.port.in.BookSearchQuery;
import com.edsonfilho.adventure.book.application.port.in.GetBookUseCase;
import com.edsonfilho.adventure.book.application.port.in.ListBooksUseCase;
import com.edsonfilho.adventure.book.application.port.in.RemoveCategoryUseCase;
import com.edsonfilho.adventure.book.domain.entity.Book;
import com.edsonfilho.adventure.book.infrastructure.web.dto.AddBookRequest;
import com.edsonfilho.adventure.book.infrastructure.web.dto.BookDetailsResponse;
import com.edsonfilho.adventure.book.infrastructure.web.dto.BookSummaryResponse;
import com.edsonfilho.adventure.book.infrastructure.web.dto.CategoryRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST adapter for the Books resource.
 *
 * <p>This class depends only on use case port interfaces — never on concrete
 * services or MongoDB types.</p>
 *
 * <h2>Endpoints</h2>
 * <pre>
 * POST   /api/books                           → add a new book (full JSON payload)
 * GET    /api/books                           → list / search books
 * GET    /api/books/{id}                      → get book details
 * POST   /api/books/{id}/categories           → add a category  { "category": "fantasy" }
 * DELETE /api/books/{id}/categories/{name}    → remove a category
 * </pre>
 */
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final AddBookUseCase addBookUseCase;
    private final ListBooksUseCase listBooksUseCase;
    private final GetBookUseCase getBookUseCase;
    private final AddCategoryUseCase addCategoryUseCase;
    private final RemoveCategoryUseCase removeCategoryUseCase;

    public BookController(AddBookUseCase addBookUseCase,
                          ListBooksUseCase listBooksUseCase,
                          GetBookUseCase getBookUseCase,
                          AddCategoryUseCase addCategoryUseCase,
                          RemoveCategoryUseCase removeCategoryUseCase) {
        this.addBookUseCase = addBookUseCase;
        this.listBooksUseCase = listBooksUseCase;
        this.getBookUseCase = getBookUseCase;
        this.addCategoryUseCase = addCategoryUseCase;
        this.removeCategoryUseCase = removeCategoryUseCase;
    }

    // -------------------------------------------------------------------------
    // POST /api/books  — create new book
    // -------------------------------------------------------------------------

    /**
     * Adds a new book to the collection.
     *
     * <p>The full book (title, author, difficulty, categories, and all sections
     * with their options) must be submitted as a single JSON payload. Sections
     * are never created or edited individually.</p>
     *
     * @param request the book payload
     * @return 201 Created with the saved book details (including its assigned id),
     *         or 422 Unprocessable Entity if the book fails structural validation
     */
    @PostMapping
    public ResponseEntity<BookDetailsResponse> addBook(@RequestBody AddBookRequest request) {
        AddBookCommand command = new AddBookCommand(
                request.getTitle(),
                request.getAuthor(),
                request.getDifficulty(),
                request.getCategories(),
                request.getSections()
        );
        Book saved = addBookUseCase.execute(command);
        URI location = URI.create("/api/books/" + saved.getId());
        return ResponseEntity.created(location).body(toDetailsResponse(saved));
    }

    // -------------------------------------------------------------------------
    // GET /api/books  — list / search
    // -------------------------------------------------------------------------

    /**
     * Lists all books, optionally filtered by any combination of search parameters.
     * All params are combined with AND logic.
     *
     * @param title      partial title match (case-insensitive), optional
     * @param author     partial author match (case-insensitive), optional
     * @param category   case-insensitive match against the book's categories array, optional
     * @param difficulty exact difficulty: {@code EASY}, {@code MEDIUM}, or {@code HARD}, optional
     * @return 200 OK with a (possibly empty) list of book summaries
     */
    @GetMapping
    public ResponseEntity<List<BookSummaryResponse>> getBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String difficulty) {

        BookSearchQuery query = new BookSearchQuery(title, author, category, difficulty);

        List<BookSummaryResponse> response = listBooksUseCase.execute(query)
                .stream()
                .map(this::toSummaryResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    // -------------------------------------------------------------------------
    // GET /api/books/{id}  — details
    // -------------------------------------------------------------------------

    /**
     * Returns the details (title, author, difficulty, categories) of a single book.
     *
     * @param id the book's MongoDB id
     * @return 200 OK with book details, or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookDetailsResponse> getBook(@PathVariable String id) {
        Book book = getBookUseCase.execute(id);
        return ResponseEntity.ok(toDetailsResponse(book));
    }

    // -------------------------------------------------------------------------
    // POST /api/books/{id}/categories  — add
    // -------------------------------------------------------------------------

    /**
     * Adds a category to the specified book.
     *
     * @param id      the book's MongoDB id
     * @param request body containing the category name
     * @return 200 OK with updated book details, 404 if book not found, 409 if category already exists
     */
    @PostMapping("/{id}/categories")
    public ResponseEntity<BookDetailsResponse> addCategory(
            @PathVariable String id,
            @RequestBody CategoryRequest request) {

        Book updated = addCategoryUseCase.execute(id, request.getCategory());
        return ResponseEntity.ok(toDetailsResponse(updated));
    }

    // -------------------------------------------------------------------------
    // DELETE /api/books/{id}/categories/{name}  — remove
    // -------------------------------------------------------------------------

    /**
     * Removes a category from the specified book.
     *
     * @param id       the book's MongoDB id
     * @param name     the category name to remove (case-insensitive)
     * @return 200 OK with updated book details, 404 if book or category not found
     */
    @DeleteMapping("/{id}/categories/{name}")
    public ResponseEntity<BookDetailsResponse> removeCategory(
            @PathVariable String id,
            @PathVariable String name) {

        Book updated = removeCategoryUseCase.execute(id, name);
        return ResponseEntity.ok(toDetailsResponse(updated));
    }

    // -------------------------------------------------------------------------
    // Mapping — domain → web DTO (web adapter responsibility)
    // -------------------------------------------------------------------------

    private BookSummaryResponse toSummaryResponse(Book book) {
        return new BookSummaryResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getDifficulty(),
                book.getCategories()
        );
    }

    private BookDetailsResponse toDetailsResponse(Book book) {
        return new BookDetailsResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getDifficulty(),
                book.getCategories()
        );
    }
}
