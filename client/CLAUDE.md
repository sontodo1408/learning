# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```
npm run dev      # start Vite dev server
npm run build    # production build (outputs to dist/)
npm run preview  # preview the production build locally
```

There is no lint, format, or test tooling configured in this project (no ESLint/Prettier config, no test runner).

## Architecture

This is a Vue 3 SPA scaffolded with Vite, using Quasar as the component/UI framework, Tailwind CSS v4 for utility styling, Vue Router for navigation, and Pinia for state management. It is an early-stage project — `src/` still mostly holds the Vite/Vue starter template (`HelloWorld.vue`) and has not yet been built out.

- **Entry point**: [src/main.js](src/main.js) creates the Vue app, installs Pinia, Vue Router, then Quasar (in that order), and imports global styles: Tailwind (`assets/css/tailwind.css`) → Quasar CSS → Quasar Material Icons → local `style.css`.
- **Routing**: [src/router/index.js](src/router/index.js) defines routes with `createWebHistory`. Page-level components live in `src/views/`, grouped by area (e.g. [views/admin/S0001_Home.vue](src/views/admin/S0001_Home.vue), [views/study/S0004_Flashcard.vue](src/views/study/S0004_Flashcard.vue)); reusable UI pieces live in `src/components/`. [App.vue](src/App.vue) is just a `<RouterView />` shell.
- **Layouts & shared routes**: each top-level route group mounts one file from `src/layouts/` (e.g. `AdminLayout.vue`, `BlankLayout.vue`, `StudyLayout.vue`) as its `component`, with the actual screens as `children` rendered through that layout's `<router-view />`. A layout owns anything shared across its screens (header, sidebar/tab bar, common nav) so screens only render their own content — see `StudyLayout.vue` (mounted at `/study/:setId`) for the pattern: it renders a desktop sidebar or mobile top+bottom bar depending on `$q.screen.width`, and its 3 children (Flashcard/Learn/Test mode) only contain the content area. Layouts are not "screens" and don't follow the `<code>_<Name>` naming convention below.
- **Screen naming convention**: every file directly under `src/views/` (including subfolders like `admin/`) is a "screen" and must be named `<code>_<Name>.vue`, where `<code>` is a zero-padded sequential id (`S0001`, `S0002`, ...) assigned in the order screens were created — e.g. `S0001_Home.vue`, `S0002_VideoVocab.vue`, `S0003_Login.vue`. `<code>` alone (no `_Name` suffix) is also the i18n namespace for that screen (see below) — the code is already a unique identifier, no need to repeat the descriptive name there.
- **Global common components**: [src/components/common/](src/components/common/) holds the base input primitives — `CBtn`, `CDatePicker`, `CSelect` — wrapping the equivalent Quasar components. They are registered globally via `app.component(...)` in [main.js](src/main.js), so use `<CBtn>` / `<CDatePicker>` / `<CSelect>` directly in templates (no import needed) instead of the raw `q-btn` / `q-input` (date) / `q-select`.
- **State management**: Pinia is installed via `createPinia()` in main.js. Stores live under `src/stores/` (e.g. [src/stores/auth-store.js](src/stores/auth-store.js), [src/stores/dialog-store.js](src/stores/dialog-store.js)), following the options-store `defineStore("name", { state, getters, actions })` convention.
- **i18n**: [src/i18n/index.js](src/i18n/index.js) sets up `vue-i18n` (Composition API mode, `globalInjection: true`) with Vietnamese (`vi`, default) and English (`en`, fallback), each fed by its own file: [src/i18n/messages/vi.js](src/i18n/messages/vi.js) and [src/i18n/messages/en.js](src/i18n/messages/en.js). Each file's shape is `{ common, S0001, S0002, ... }`:
  - `common` — shared/chrome strings used by `AdminLayout` (app name, nav labels), used as `common.*`. Other layouts get their own namespace named after the layout instead of overloading `common` — e.g. `StudyLayout`'s sidebar/tab-bar text lives under `study.*`.
  - One key per screen code (matching its file name prefix, e.g. `S0001_Home.vue` → `S0001`), each holding a `btn` object for button text and a `label` object for standalone text/placeholders, e.g. `S0002.btn.addCard`, `S0002.label.termPlaceholder`. Interpolated messages use vue-i18n's `{named}` syntax, e.g. `S0004.label.cardProgress: 'Card {current} / {total}'` called as `t('S0004.label.cardProgress', { current, total })`.
  - In components: `const { t } = useI18n()`, then `t('common.nav.dashboard')` or `t('S0002.btn.addCard')`. Never hardcode user-facing text. When adding a new screen, add its entry under the matching code in both `messages/vi.js` and `messages/en.js`.
  - Switch locales at runtime with `setLocale(code)` exported from `@/i18n`; the choice persists to `localStorage`.
- **Path alias**: `@` maps to `src/` (configured in [vite.config.js](vite.config.js)).
- **Styling stack — three systems coexist, be aware of precedence and prefixing**:
  - Quasar SCSS variables (colors, dark mode, etc.) are overridden in [src/assets/css/quasar-variables.sass](src/assets/css/quasar-variables.sass) and wired into the build via the `quasar` Vite plugin's `sassVariables` option.
  - Tailwind v4 is imported via `@import "tailwindcss" prefix(tw)` in [src/assets/css/tailwind.css](src/assets/css/tailwind.css) — **all Tailwind utility classes must use the `tw-` prefix** (e.g. `tw-text-white`) to avoid colliding with Quasar's own class names. Custom theme tokens (e.g. `--color-lime-*`) are added there via `@theme`.
  - Plain global CSS/custom properties live in [src/style.css](src/style.css) (design tokens like `--text`, `--bg`, `--accent`, with `prefers-color-scheme: dark` overrides).
- **Environment config**: `env.development` / `env.production` define `VITE_API_URL` (backend API base URL), consumed via Vite's `import.meta.env`.
- **Services**: [src/services/](src/services/) holds one class per backend resource, each extending [src/services/base-service.js](src/services/base-service.js) and exported as a ready-to-use singleton (`export default new XxxService();`) — e.g. [src/services/auth-service.js](src/services/auth-service.js). `BaseService` just forwards `get`/`post`/`put`/`delete` to an internal [src/services/rest-client.js](src/services/rest-client.js) (an axios wrapper bound to the resource's path, e.g. `/auth`), so subclasses never touch axios directly. `RestClient` attaches the JWT from `useAuthStore().token` as a Bearer header on every request, signs the user out on a `401`, and resolves/rejects with the server's raw `{ code, msg, payload }` envelope (see server's `ResponseRoot`) — each service method is responsible for unwrapping `.payload` before returning to its own callers (see `AuthService#login`). Add new resources as new `<name>-service.js` classes following `AuthService`'s shape, not by growing an existing one.
- Vue SFCs use `<script setup>` syntax throughout.

## Design

Before building new UI or visually reshaping an existing screen, read and follow [.agents/skills/frontend-design/SKILL.md](.agents/skills/frontend-design/SKILL.md) — it covers aesthetic direction, typography, and layout choices so screens don't read as generic/templated defaults.
