# Adventure Book API

A Spring Boot REST API for interactive choose-your-own-adventure books. Players can browse a catalogue of books, manage their categories, and play through branching stories where their choices affect health and narrative outcomes.

---

## How to Build and Run

**Prerequisites:** Docker (for MongoDB via Docker Compose), Java 25, Maven.

```bash
# Clone and run — Spring Boot auto-starts MongoDB via docker-compose
./mvnw spring-boot:run
```

The API starts on `http://localhost:8080`.  
Swagger UI: `http://localhost:8080/swagger-ui.html`

### Database UI (Mongo Express)

The `compose.yaml` file includes Mongo Express. Because it is a UI tool and not a direct connection dependency for the Spring application, Spring Boot's Docker Compose integration may not start it automatically. 

To start the database and the UI manually, run:
```bash
docker compose up -d
```
Then navigate to `http://localhost:8081` to manage your MongoDB data. Log in with the default credentials:
- **Username:** `admin`
- **Password:** `pass`

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 25 |
| Framework | Spring Boot 4.x |
| Database | MongoDB |
| API Docs | SpringDoc OpenAPI (Swagger UI at `/swagger-ui.html`) |
| Build | Maven Wrapper (`mvnw`) |
| Containerisation | Docker Compose (auto-managed by Spring Boot) |

---

## Architecture

The project follows **Clean / Hexagonal Architecture**. The dependency rule is strict: inner layers never import outer layers.

```
domain  ←  application  ←  infrastructure
```

```
com.edsonfilho.adventure.book
├── domain/                     Enterprise rules — zero Spring or MongoDB imports
│   ├── entity/                 Book, Section, Option, Consequence, GameSession, enums
│   └── exception/              Domain exceptions (BookNotFoundException, etc.)
│
├── application/                Use cases and port interfaces
│   ├── port/
│   │   ├── in/                 Use case interfaces + input/output objects
│   │   └── out/                Repository port interfaces
│   ├── service/                Use case implementations
│   └── validation/             Structural book validation (rule/strategy pattern)
│
└── infrastructure/             Spring & MongoDB — all framework code lives here
    ├── web/                    REST controllers, DTOs, GlobalExceptionHandler
    ├── persistence/            Spring Data repositories + adapters
```

### Dependency flow example

```
BookController  →  ListBooksUseCase (port/in)
                        ↑ ListBooksService
                            →  BookRepositoryPort (port/out)
                                    ↑ BookRepositoryAdapter  →  MongoTemplate
```

Controllers depend on **interfaces** (use case ports), never on service classes.  
Services depend on **interfaces** (repository ports), never on Spring Data or Mongo.

---

## Domain Model

### Book
A book is the root document stored in MongoDB. It contains a graph of sections linked by player choices.

```
Book
├── id            (MongoDB ObjectId)
├── title
├── author
├── difficulty    (EASY | MEDIUM | HARD)
├── categories    (String array — taggable)
└── sections[]
    ├── id        (String — some are ints in source JSON, normalised to String)
    ├── text      (narrative prose)
    ├── type      (BEGIN | NODE | END)
    └── options[]
        ├── description
        ├── gotoId          (id of the next Section)
        └── consequence?    (optional)
            ├── type        (LOSE_HEALTH | GAIN_HEALTH)
            ├── value       (integer as String)
            └── text        (narrative flavour text)
```

**Rules enforced at validation:**
- Exactly one `BEGIN` section
- At least one `END` section
- Every `gotoId` must reference a valid section id
- Every `BEGIN`/`NODE` section must have at least one option

### GameSession
Tracks a player's progress through a book.

```
GameSession
├── id                (MongoDB ObjectId)
├── bookId
├── currentSectionId
├── health            (starts at 10, min 0)
└── status            (ACTIVE | DEAD | COMPLETED)
```

---

## API Reference

### Books

| Method | Path | Description |
|---|---|---|
| `POST` | `/api/books` | Add a new book (full JSON payload with sections) |
| `GET` | `/api/books` | List all books. Optional query params: `title`, `author`, `category`, `difficulty` |
| `GET` | `/api/books/{id}` | Get book details (title, author, difficulty, categories) |
| `POST` | `/api/books/{id}/categories` | Add a category `{ "category": "fantasy" }` |
| `DELETE` | `/api/books/{id}/categories/{name}` | Remove a category |

**Search example:** `GET /api/books?difficulty=HARD&author=daniel`  
All params are optional, combined with AND logic, case-insensitive.

### Game Sessions

| Method | Path | Description |
|---|---|---|
| `POST` | `/api/sessions` | Start a game `{ "bookId": "..." }` → returns session at BEGIN section |
| `GET` | `/api/sessions/{id}` | Get current session state |
| `POST` | `/api/sessions/{id}/moves` | Make a move `{ "optionIndex": 0 }` |

**Game session response shape:**
```json
{
  "sessionId": "...",
  "bookId": "...",
  "health": 7,
  "status": "ACTIVE",
  "currentSection": {
    "id": "20",
    "text": "You don't see anything, it's too dark.",
    "type": "NODE",
    "options": [
      { "index": 0, "description": "Try to scan the area with your hands" }
    ]
  },
  "consequence": {
    "type": "LOSE_HEALTH",
    "value": "3",
    "text": "You're getting a little more crazier..."
  }
}
```
`consequence` is `null` on `GET /api/sessions/{id}` (no move was made).

### Error responses

All errors follow a consistent shape:
```json
{ "error": "BOOK_NOT_FOUND", "message": "Book with id 'xyz' was not found." }
```

| HTTP | Error code |
|---|---|
| 400 | `INVALID_OPTION` |
| 404 | `BOOK_NOT_FOUND`, `SESSION_NOT_FOUND`, `CATEGORY_NOT_FOUND` |
| 409 | `CATEGORY_ALREADY_EXISTS`, `GAME_OVER` |
| 422 | `BOOK_VALIDATION_ERROR` |

---




## Book Validation

Before accepting a book (e.g. via a future AddBook endpoint), `BookValidator` runs four structural rules:

1. Exactly one `BEGIN` section
2. At least one `END` section
3. All `gotoId` values reference existing section ids
4. All non-`END` sections have at least one option

Rules use the **Strategy pattern** — adding a new rule requires only creating a new `@Component` implementing `BookValidationRule`. `BookValidator` auto-discovers all rules via Spring injection.

---

## Future Improvements

- [x] `AddBookUseCase` — submit a new book via API (runs `BookValidator`)
- [ ] `@Version` on `GameSession` for optimistic locking (concurrent move safety)
- [ ] Add [Mongock](https://www.mongock.io/) for production-grade migrations
- [ ] Authentication / authorisation
- [ ] Player profiles and session history
