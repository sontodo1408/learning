---
name: check-layering
description: Audit controllers in this Spring Boot project for violations of the controller -> service -> repository layering rule (e.g. a controller injecting a repository directly). Use when asked to check/review controller layering, or before/after adding a new controller endpoint.
---

# Check layering: controller -> service -> repository

This project requires every controller endpoint to go through a `service`
interface before reaching a `repository`. A controller must never inject or
call a `repository` directly (`controller -> repository`), and must never
skip the service layer, even for a trivial single-line lookup.

`TestController` (`/test`) is a scratch endpoint that predates this rule and
is explicitly exempt — do not flag it.

## How to audit

1. List all controllers: `src/main/java/vn/io/sontd/learning/server/controller/**/*.java`, excluding `BaseController` (abstract base, no endpoints) and `TestController` (exempt).
2. For each remaining controller, check its field injections and method bodies:
   - Flag any import of `vn.io.sontd.learning.server.repository.*` in a controller file.
   - Flag any field of a `*Repository` type injected into the controller.
   - Flag any direct repository method call (e.g. `xxxRepository.findBy...`, `.save(...)`, `.findAll()`) from within a controller method.
3. For each flagged controller, verify there is no interceding `service`/`service/impl` call — i.e. the controller calls the repository without going through a `service` interface method.

## Reporting

For each violation found, report:
- File and line of the repository injection/call.
- The endpoint method involved.
- A one-line fix suggestion: introduce (or extend) a `service`/`service/impl` pair with a method that wraps the repository call, and have the controller call that service method instead.

If asked to fix (not just report), apply the fix directly:
- Add or extend a `service` interface method and its `service/impl` implementation that wraps the repository call.
- Update the controller to inject and call the service instead of the repository.
- Keep the change minimal — don't introduce unrelated abstractions or refactor unrelated code.
