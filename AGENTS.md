# Repository Guidelines

## Project Structure & Module Organization
This repository is a Kotlin/Spring Boot backend for Plan Wallet.

- Source: `src/main/kotlin/com/planwallet`
- Tests: `src/test/kotlin/com/planwallet`
- Resources/config: `src/main/resources` (`application.yml`, profile files, i18n)
- DB schema: `db/create.sql`
- Build files: `build.gradle.kts`, `settings.gradle.kts`

Package layout follows layered architecture:
- `presentation`: controllers and request/response DTOs
- `application`: service interfaces and implementations
- `domain`: entities and core models
- `infrastructure`: repositories
- `global`: security, config, and error handling

## Build, Test, and Development Commands
- `./gradlew bootRun`: run server locally.
- `./gradlew test`: run all unit/integration tests (JUnit 5 + Spring test).
- `./gradlew clean build`: full build artifact + test verification.
- `docker-compose up --build`: run app + dependencies in containers.

Swagger (local): `http://localhost:8080/swagger-ui/index.html`

## Coding Style & Naming Conventions
- Language: Kotlin (JVM toolchain 21).
- Use 4-space indentation and idiomatic Kotlin style.
- Types/classes: `PascalCase`; methods/variables: `camelCase`; constants: `UPPER_SNAKE_CASE`.
- Keep package names lowercase (e.g., `com.planwallet.application.stats`).
- Keep DTOs in `presentation/.../dto` and avoid leaking persistence entities through controller responses.

## Testing Guidelines
- Frameworks: JUnit Platform, Spring Boot Test, Spring Security Test, MockK.
- Test file naming: `*Test.kt` (e.g., `StatsServiceImplTest.kt`).
- Mirror production package structure under `src/test/kotlin`.
- Prefer service-level unit tests for business rules and controller tests for API contracts/status codes.
- Run `./gradlew test` before opening a PR.

## Commit & Pull Request Guidelines
- Follow Conventional-style prefixes observed in history: `feat:`, `fix:`, `docs:`, `chore:`, `test:`.
- Keep each commit focused on a single concern.
- PR should include:
  - concise change summary
  - impacted modules/endpoints
  - test evidence (`./gradlew test` output)
  - config/env changes (if any)

## Security & Configuration Tips
- Never commit secrets. Set `JWT_SECRET` and DB credentials via environment variables.
- Use profile-specific configs (`application-local.yml`, `application-prod.yml`) for environment separation.
