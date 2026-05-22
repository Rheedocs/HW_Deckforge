# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Build
mvn clean package

# Run
mvn spring-boot:run

# Run all tests
mvn test

# Run a single test class
mvn test -Dtest=PlayerServiceTest

# Run a single test method
mvn test -Dtest=PlayerServiceTest#login_withCorrectCredentials_returnsPlayer
```

The app starts on `http://localhost:8080`. MySQL must be running at `localhost:3306` — the database `deckforge` is created automatically on first run. `spring.sql.init.mode=always` means `schema.sql` and `data.sql` re-run on every startup (wipes and reseeds data).

## Architecture

Four-layer package structure under `dk.zealand.hw_deckforge`:

| Layer | Package | Responsibility |
|---|---|---|
| Presentation | `presentation/controllers`, `presentation/helpers`, `presentation/config` | MVC controllers, session interceptor, AuthHelper |
| Application | `application/service`, `application/interfaces` | Business logic, repository interfaces |
| Domain | `domain`, `domain/enums`, `domain/exceptions`, `domain/validation` | Entities, enums, domain validators, custom exceptions |
| Infrastructure | `infrastructure`, `infrastructure/external`, `infrastructure/config` | JDBC repositories, MySQL persistence, Scryfall API client, infrastructure config |

Dependencies follow a Clean Architecture-inspired structure. Controllers depend on services; services depend on repository interfaces and domain objects; repositories implement those interfaces with JdbcTemplate/MySQL. Services may also use validators and infrastructure services such as `ScryfallService` where needed.


## Current Implementation Status

`feature/result` has been merged into `master`. `feature/trade` was already merged into `feature/result`. The current stable state has 211 passing tests and 0 failures.

Major implemented areas:

- Event/result flow completed and collected mostly around event detail pages
- Trade flow cleaned up with double confirmation and completed status
- Custom exceptions: `NotFoundException` and `ValidationException`
- Service validation cleaned up with helper methods and validators
- Mobile views, card pagination, and demo data improved

## Authentication & Access Control

There is no Spring Security filter chain. Auth is handled manually:

- `SessionInterceptor` runs before every request and redirects to `/login` if `session.getAttribute("player")` is null or the player is inactive. Public paths are excluded in `WebConfig`.
- `AuthHelper` (static utility) provides `isAdmin()`, `isSelf()`, `isAdminOrSelf()` — controllers call these directly to guard actions.
- The logged-in `Player` object lives in the session under the key `"player"` and must be refreshed manually after updates (`session.setAttribute("player", ...)`).

## Repository Pattern

Each entity has an interface in `application/interfaces` (e.g. `IPlayerRepository`) and a `JdbcTemplate`-based implementation in `infrastructure/`. Services depend only on the interface.

- JDBC repositories use `JdbcTemplate` and map rows to domain objects.
- Database errors are wrapped as `DatabaseException` where relevant.
- `GlobalExceptionHandler` catches `AccessDeniedException`, `DatabaseException`, `NotFoundException`, `ValidationException`, `IllegalArgumentException`, `RuntimeException`, and generic `Exception`.
- `PlayerRepository.delete()` performs a **soft delete** (`active = FALSE`), not a real SQL DELETE. Normal `findAll()` queries filter by `active = TRUE`; admin flows can use `findAllIncludingInactive()`.

## Key Domain Rules

- **Player delete** is soft (`active = FALSE`). Deleting the last admin is blocked in `PlayerService.isOnlyAdmin()`.
- **Passwords** are BCrypt-encoded. The error message for login failures is intentionally generic to avoid revealing which field is wrong.
- **Deck rules** are handled through `Format` and `FormatValidator`:
    - Commander: exactly 100 cards, max 1 copy of each non-basic card
    - Standard: minimum 60 cards, max 4 copies of each non-basic card
    - Draft: minimum 40 cards
    - Casual: minimum 60 cards, max 4 copies of each non-basic card
    - Basic lands may exceed copy limits
- **Event registration** is validated in `EventService`: event must be upcoming, not full, player must not already be registered, deck must belong to the player, deck format must match event format, and deck size must be valid.
- **Trade flow** uses these statuses: `PENDING`, `ACCEPTED`, `COMPLETED`, `DECLINED`, `CANCELLED`, `EXPIRED`. Accepted trades require double confirmation through `proposerConfirmed` and `receiverConfirmed`; only when both confirm is the trade completed.
- **Result display** uses `Result.getPlacementText()` for labels like `1. plads`.
- **Scryfall integration** (`ScryfallService`) fetches card images from valid `https://scryfall.com/card/...` links by extracting set code and collector number, calling Scryfall's card endpoint, and reading `image_uris.normal` or `card_faces[0].image_uris.normal`. Failures return `null` silently.

## Testing

- **Service tests** (`application/service/`) use Mockito — repositories are mocked, no DB needed.
- **Repository tests** (`infrastructure/`) use `@JdbcTest` with H2 in-memory database. They rely on the seed data in `data.sql`, so test assertions reference specific usernames and IDs from that seed (e.g. `admin` at id=1, `goncalo` at `goncalo@deckforge.dk`).
- UI and controller layer has no tests.

## Language

All UI text, error messages, comments, and variable names in the Danish-facing domain (e.g. `kortCount`, `tilbage`, `fejl`) are in **Danish**. Code identifiers follow standard English Java conventions.

## Code Principles

This is a 2nd semester Datamatiker exam project. Code quality is assessed at the oral exam.

- **SOLID** — all five principles apply:
    - Single Responsibility: each class has exactly one reason to change
    - Open/Closed: extend behavior without modifying existing code
    - Liskov Substitution: implementations must be substitutable for their interfaces
    - Interface Segregation: small focused interfaces, not large catch-all ones
    - Dependency Inversion: depend on abstractions (interfaces), not concrete classes
- **Clean Architecture** — strictly enforced, layers only communicate downward, never skip or reverse layers
- **Separation of Concerns** — each layer and class has isolated responsibility, cross-layer calls only when strictly necessary and no alternative exists
- **KISS** — simplest solution that works, no over-engineering
- **DRY** — no duplicated logic, extract shared behavior
- **Clean Code** (Uncle Bob) — meaningful names, small focused methods, no magic numbers
- **No streams** — avoid Java streams, they are flagged as AI-generated by teachers
- **No business logic in controllers** — controllers only handle HTTP, delegate everything to services
- **Sparse Javadoc** — only where genuinely useful, not on every method
- **Comments in Danish** — inline code comments in Danish, code identifiers in English
- Section dividers use: // --- Sektionsnavn ---
- Commit messages: short and lowercase, max 10 words, conventional commits prefix (feat/fix/test/chore)
- No Co-Authored-By in commit messages
- Static usage: `private static final` constants are fine; avoid static services, static mutable state, and static initializer blocks for application logic

## Project Requirements

See `docs/krav.md` for exam requirements and client interview.
See `docs/holger-dialog.md` for product owner decisions and approved features.

## Project Structure Note

The original Sprint 1 class diagram was used as a starting point, but the current code has moved beyond empty stubs. Trade, event, result, deck validation, custom exceptions, pagination support, Scryfall image lookup, and demo data are now implemented. Keep diagrams and documentation synchronized with the current `master` branch.