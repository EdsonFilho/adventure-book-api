# AGENT.md — Adventure Book API

This file is intended for AI coding assistants working on this codebase.
Read it before making any changes.

---

## Project Purpose

A REST API for interactive choose-your-own-adventure books. Players browse a book catalogue,
manage book categories, and play through branching stories where choices affect a health counter.

---

## Architecture — The One Rule That Overrides Everything Else

This project follows **Clean / Hexagonal Architecture**. The single rule:

> **Inner layers never import outer layers.**
> `domain` ← `application` ← `infrastructure`

**Never** put Spring annotations (`@Service`, `@Component`, `@RestController`, `@Document`) or
MongoDB types (`MongoTemplate`, `Criteria`, `MongoRepository`) in the `domain` or `application`
packages. The only exceptions already in place are `@Id` and `@Document` on `Book` and
`GameSession` — these are an accepted pragmatic compromise with Spring Data MongoDB.

---

## Package Layout

```
com.edsonfilho.adventure.book
│
├── domain/
│   ├── entity/        Pure Java classes: Book, Section, Option, Consequence,
│   │                  GameSession, GameStatus, Difficulty, SectionType
│   └── exception/     All domain exceptions extend RuntimeException
│
├── application/
│   ├── port/in/       Use case interfaces + their input/output value objects
│   ├── port/out/      Repository port interfaces (BookRepositoryPort, GameSessionRepositoryPort)
│   ├── service/       One service class per use case — implements a port/in interface
│   └── validation/    BookValidationRule (interface) + 4 rule implementations + BookValidator
│
└── infrastructure/
    ├── web/           @RestController classes, DTOs, GlobalExceptionHandler
    ├── persistence/   MongoRepository interfaces + adapter classes implementing port/out
```

---

## How to Add a New Use Case (Required Pattern)

Follow these steps every time — don't skip layers.

### 1. Domain (if needed)
- Add methods to an existing entity, or create a new entity in `domain/entity/`
- Add new exceptions in `domain/exception/`
- Zero framework imports

### 2. Application — port/in
Create `application/port/in/XxxUseCase.java`:
```java
public interface XxxUseCase {
    SomeReturnType execute(String param);
}
```
If the use case needs a complex input or output, create a plain Java class alongside it
(e.g. `BookSearchQuery`, `GameSessionView`, `MakeMoveResult`).

### 3. Application — port/out (if new persistence is needed)
Create `application/port/out/XxxRepositoryPort.java`:
```java
public interface XxxRepositoryPort {
    Optional<Xxx> findById(String id);
    Xxx save(Xxx entity);
}
```

### 4. Application — service
Create `application/service/XxxService.java`:
```java
@Service
public class XxxService implements XxxUseCase {
    // Inject port/out interfaces only — never concrete adapters
}
```

### 5. Infrastructure — persistence (if new port/out was created)
- `MongoXxxRepository extends MongoRepository<Xxx, String>` — Spring Data CRUD
- `XxxRepositoryAdapter implements XxxRepositoryPort` — delegates to the above; owns `MongoTemplate` for complex queries

### 6. Infrastructure — web
- Add endpoint to an existing `@RestController` or create a new one
- Controller injects **use case interfaces** (port/in), never service classes
- All domain → DTO mapping happens in the controller (web adapter responsibility)
- Add new exception handlers to `GlobalExceptionHandler`

---

## Existing Use Cases (do not duplicate)

| Use Case Interface | Service | Description |
|---|---|---|
| `AddBookUseCase` | `AddBookService` | Add a new book (with validation) |
| `ListBooksUseCase` | `ListBooksService` | Search/list books |
| `GetBookUseCase` | `GetBookService` | Get book details by id |
| `AddCategoryUseCase` | `AddCategoryService` | Add category to a book |
| `RemoveCategoryUseCase` | `RemoveCategoryService` | Remove category from a book |
| `StartGameUseCase` | `StartGameService` | Create a game session |
| `GetSessionUseCase` | `GetSessionService` | Get session state |
| `MakeMoveUseCase` | `MakeMoveService` | Player makes a move |

---

## Validation Pattern

`BookValidator` discovers all `BookValidationRule` implementations via Spring constructor injection:
```java
@Component
public class BookValidator {
    private final List<BookValidationRule> rules; // Spring injects all @Component rules
}
```
To add a new rule: create a class implementing `BookValidationRule`, annotate with `@Component`.
**Do not modify `BookValidator`.**

Current rules: `BeginSectionRule`, `EndSectionRule`, `SectionReferenceRule`, `NonEndingSectionOptionsRule`.

---

## Exception Handling

All domain exceptions extend `RuntimeException` and are caught in `GlobalExceptionHandler`
(`@RestControllerAdvice` in `infrastructure/web`).

**Pattern:** throw domain exceptions from services/entities → let them bubble up → handler maps to HTTP.

```
BookNotFoundException          → 404
CategoryNotFoundException       → 404
GameSessionNotFoundException    → 404
CategoryAlreadyExistsException  → 409
GameOverException               → 409
InvalidOptionException          → 400
BookValidationException         → 422
```

When adding a new exception: create it in `domain/exception/`, throw it in the service or entity,
add a handler method in `GlobalExceptionHandler`.

---

## Naming Conventions

| Artefact | Convention | Example |
|---|---|---|
| Use case interface | `XxxUseCase` | `AddCategoryUseCase` |
| Use case service | `XxxService` | `AddCategoryService` |
| Repository port | `XxxRepositoryPort` | `BookRepositoryPort` |
| Spring Data repo | `MongoXxxRepository` | `MongoBookRepository` |
| Persistence adapter | `XxxRepositoryAdapter` | `BookRepositoryAdapter` |
| Web DTO | `XxxRequest` / `XxxResponse` | `CategoryRequest`, `BookDetailsResponse` |

---

## Domain Entity Rules

- Domain entities are **plain Java classes** with private fields and `getX()`/`setX()` accessors
- Business methods go on the entity when they enforce domain rules (e.g. `Book.addCategory()`,
  `GameSession.applyConsequence()`)
- No records — the project uses standard classes for consistency
- `Section.id` is `String` (source JSON mixes integer and string ids)
- `Option.gotoId` is `int` — convert with `String.valueOf()` when matching against `Section.id`

---



## API Base URLs

| Resource | Base path |
|---|---|
| Books | `/api/books` |
| Game sessions | `/api/sessions` |
| Swagger UI | `/swagger-ui.html` |

---



## Known Gaps / Future Work

- `GameSession` has no `@Version` field — concurrent moves on the same session can cause lost
  updates. Add `@Version Long version` and handle `OptimisticLockingFailureException` in the handler.
- No authentication layer.
- Use [Mongock](https://www.mongock.io/) for production-grade versioned migrations.
