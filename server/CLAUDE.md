# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project overview

Spring Boot 4.1.0 / Java 21 backend (`vn.io.sontd.learning.server`) for a language-learning app (users, study sets, study cards). Uses Spring Data JPA against a MySQL/TiDB database, Spring Security with a custom JWT filter, and Lombok throughout.

## Commands

Use the Maven wrapper (`mvnw` / `mvnw.cmd`), not a globally installed Maven.

- Run the app: `./mvnw spring-boot:run`
- Build: `./mvnw clean package`
- Run all tests: `./mvnw test`
- Run a single test class: `./mvnw test -Dtest=ServerApplicationTests`
- Run a single test method: `./mvnw test -Dtest=ServerApplicationTests#contextLoads`

### Profiles

There are two orthogonal profile mechanisms that must be kept in sync:

- Maven property `build.profile` (`development` default, or `production`) selects which `application.properties` gets bundled into the jar: `src/main/resources` for development, `src/production/resources` for production. Set via `./mvnw clean package -Dbuild.profile=production`.
- `spring.profiles.active` inside each `application.properties` (`development`/`production`) controls Spring profile-conditional beans/config, notably the `logback-spring.xml` appender (console in development, rolling file in production).

Each properties file hardcodes its own datasource URL/credentials and JWT secret/expiration (`thesis.app.jwt-secret`, `thesis.app.jwt-expiration-ms`) — there is no external secrets management, so don't assume env-var overrides exist.

## Database

`db/create.sql` is the hand-maintained source of truth for schema (tables: `users`, `study_sets`, `study_cards`). There is no migration tool (no Flyway/Liquibase) — when a column is added/changed in `create.sql`, the corresponding entity and `TableField` constants must be updated by hand, and vice versa.

## Architecture

Standard layered structure under `vn.io.sontd.learning.server`:

- `controller` / `controller/admin` — `@RestController`s; admin-only endpoints live under the `admin` subpackage with an `/api/admin/...` base path.
- `service` (interfaces) / `service/impl` (implementations) — e.g. `JwtService`/`JwtServiceImpl`.
- `repository` — Spring Data JPA interfaces extending `JpaRepository`.
- `entity` — JPA entities. Every entity extends `BaseEntity` (provides `createdAt`/`updatedAt` via `@EnableJpaAuditing`, set in `ServerApplication`). `@Column(name = ...)` always references a constant from `TableField`, never a string literal.
- `constant` — `TableField` (all table/column name constants), `Constant` (security/header constants, permit-all URL list), `ResponseCode`, `Message`. `constant/enums` holds JPA-mapped enums (e.g. `ERole`, `EGender`, `EUserStatus`), typically annotated `@JsonFormat(shape = NUMBER_INT)` when persisted/serialized as an int.
- `dto` — request/response DTOs, namespaced by feature (e.g. `dto/auth`).
- `response` — API envelope types: `ResponseRoot` (code/payload/msg) wraps `ResponseBody`, with `ResponseData<T>` as the generic data-carrying subtype.
- `config/security` — JWT auth stack (see below).
- `config/aop` — `LoggingAspect` logs request args/response/exceptions around every `@RequestMapping`-annotated method.

### JWT auth flow

- `JwtServiceImpl` signs/parses tokens with HMAC using `thesis.app.jwt-secret`. Token subject = username; a custom claim (`Constant.PASSWORD_CLAIM`) carries the user's *encoded* password.
- `JwtAuthenticationFilter` runs before `UsernamePasswordAuthenticationFilter`. It extracts the bearer token, validates it, extracts username + password claim, loads the user via `UserDetailsServiceImpl` (backed by `UserRepository`), and only authenticates the request if the claim's password matches `UserDetailsImpl.getPassword()` (the encoded password from the DB) — so any code issuing tokens must put the DB-encoded password (not the raw one) into that claim.
- `Constant.INTERNAL_PERMIT_ALL` is the list of URL patterns that bypass the JWT filter/authorization entirely (e.g. login/init endpoints, `/test/**`) — keep it in sync with actual public controller paths.
- `SecurityConfig` wires the filter chain, `DaoAuthenticationProvider`, `BCryptPasswordEncoder`, and CORS (explicit allowed-origins allowlist, not a wildcard).
- Auth failures are rendered as JSON `ResponseRoot` bodies via `FailAuthenticationEntryPoint` (401) and `FailAccessDeniedHandler` (403), both returning HTTP 200 with the real status embedded in `ResponseRoot.code`.
