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
- **Routing**: [src/router/index.js](src/router/index.js) defines routes with `createWebHistory`. Page-level components live in `src/views/` (e.g. [HomeView.vue](src/views/HomeView.vue)); reusable UI pieces live in `src/components/`. [App.vue](src/App.vue) is just a `<RouterView />` shell.
- **State management**: Pinia is installed via `createPinia()` in main.js; no stores exist yet. When adding one, put it under `src/stores/` following the standard `defineStore("name", () => {...})` (setup-store) or options-store convention.
- **Path alias**: `@` maps to `src/` (configured in [vite.config.js](vite.config.js)).
- **Styling stack — three systems coexist, be aware of precedence and prefixing**:
  - Quasar SCSS variables (colors, dark mode, etc.) are overridden in [src/assets/css/quasar-variables.sass](src/assets/css/quasar-variables.sass) and wired into the build via the `quasar` Vite plugin's `sassVariables` option.
  - Tailwind v4 is imported via `@import "tailwindcss" prefix(tw)` in [src/assets/css/tailwind.css](src/assets/css/tailwind.css) — **all Tailwind utility classes must use the `tw-` prefix** (e.g. `tw-text-white`) to avoid colliding with Quasar's own class names. Custom theme tokens (e.g. `--color-lime-*`) are added there via `@theme`.
  - Plain global CSS/custom properties live in [src/style.css](src/style.css) (design tokens like `--text`, `--bg`, `--accent`, with `prefers-color-scheme: dark` overrides).
- **Environment config**: `env.development` / `env.production` define `VITE_API_URL` (backend API base URL), consumed via Vite's `import.meta.env`.
- Vue SFCs use `<script setup>` syntax throughout.
