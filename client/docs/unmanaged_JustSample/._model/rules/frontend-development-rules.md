---
type: "manual"
description: "Example description"
---

# Frontend Development Rules

---

## Rule 1: Follow the Development Workflow

When implementing ANY new feature, you MUST follow this exact order:

```text
Step 1: Types → Step 2: API → [Step 3: Store (optional)] → Step 4: Page → Step 5: Route → Step 6: i18n
```

### Required Steps (Always)

1. **Define Types** (`src/types/[feature].ts`)
   - Create TypeScript interfaces BEFORE writing any code
   - Mirror backend DTOs for consistency
   - Export all interfaces

2. **Implement API Client** (`src/api/[feature].ts`)
   - Use the `api` instance from `src/boot/axios.ts`
   - Export as object with async methods

3. **Create Page Component** (`src/pages/[Feature]Page.vue`)
   - Use `<script setup lang="ts">` with Composition API
   - Use Quasar components exclusively
   - Follow CLAUDE.md Japanese business UI rules

4. **Add Route** (`src/router/routes.ts`)
   - Use lazy loading: `() => import('pages/...')`
   - Set appropriate meta (requiresAuth, guestOnly)

5. **Add i18n Translations** (`src/i18n/ja/index.ts`)
   - Add ALL UI text in Japanese
   - Use polite form (です/ます)
   - Include validation and error messages

6. **Add page to drawer navigation** (`src/layouts/MainLayout.vue`)
   - Add a new `<q-item>` or `<q-expansion-item>` with icon and translation to `<q-list>`

7. **Lint and Typecheck**
   - Run `npm run lint` and fix all errors
   - Run `npm run typecheck` and fix all errors

### Optional Step (Only When Needed)

**Step 3: Create Pinia Store** (`src/stores/[feature].ts`)

**ONLY create a store when:**

- State is accessed by multiple pages/components
- State needs to persist across navigation
- Complex state logic needs centralization

**DO NOT create a store when:**

- State is only used in one page/component
- Simple form data that doesn't need sharing
- Temporary UI state (loading, errors) for single page

**Default approach**: Use local `ref()` and call API directly. Only refactor to store when state sharing becomes necessary.

---

## Rule 2: State Management Decision

Before writing ANY component, ask:

**"Does this state need to be shared across multiple pages/components?"**

- **NO** → Use local `ref()` + direct API call
- **YES** → Create Pinia store

### Examples of Shared State (Use Store)

- Authentication (user, tokens)
- Global settings
- User preferences

### Examples of Local State (Use ref)

- Form data
- Loading states
- Error messages
- Page-specific data (details page, user profile view)

---

## Rule 3: Code Organization Checklist

Before submitting ANY code, verify:

- [ ] Types defined in `src/types/[feature].ts`
- [ ] API client created in `src/api/[feature].ts`
- [ ] Store created ONLY if state is shared (optional)
- [ ] Page component in `src/pages/[Feature]Page.vue`
- [ ] Route added to `src/router/routes.ts`
- [ ] All text translated in `src/i18n/ja/index.ts`
- [ ] Follows CLAUDE.md UI guidelines
- [ ] Uses Quasar components exclusively
- [ ] TypeScript types used throughout
- [ ] Error handling implemented
- [ ] Loading states managed

---

## Rule 4: Prohibited Patterns

**NEVER do these:**

- Create a store "just in case" without clear need for sharing
- Skip type definitions
- Use raw HTML instead of Quasar components
- Hardcode text instead of using i18n
- Create pages without adding routes
- Use `any` type in TypeScript
- Call API directly from components when store exists for that feature
- Create multiple stores for the same feature
- Mix Options API and Composition API

---

## Rule 5: When in Doubt

**Question**: "Should I create a Pinia store?"
**Answer**: Start with local state. Refactor to store only when you need to share.

**Question**: "Can I skip a step?"
**Answer**: NO. All required steps must be completed in order.

---

## Enforcement

Any code that does not follow these rules MUST be refactored before merging.

**Review checklist**:

1. Does it follow the 6-step workflow?
2. Is the store only used for shared state?
3. Are all types defined?
4. Is all text translated? If reference a mockup, does the texts match the mockup?
5. Does it follow CLAUDE.md UI rules?
6. Does `npm run typecheck` and `npm run lint` pass?

If any answer is NO, the code must be revised.

---

## Quick Reference Card

```text
┌─────────────────────────────────────────────────────────────┐
│  FRONTEND DEVELOPMENT WORKFLOW - MANDATORY ORDER            │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  1. TYPES      → src/types/[feature].ts                    │
│                  Define all interfaces first                │
│                                                             │
│  2. API        → src/api/[feature].ts                      │
│                  Create API client with typed methods       │
│                                                             │
│  3. STORE      → src/stores/[feature].ts (OPTIONAL)        │
│                  ONLY if state is shared across pages       │
│                  YES: Auth, Cart, Global Settings           │
│                  NO:  Form data, Loading, Page-specific     │
│                                                             │
│  4. PAGE       → src/pages/[Feature]Page.vue               │
│                  Use Quasar components + Composition API    │
│                                                             │
│  5. ROUTE      → src/router/routes.ts                      │
│                  Add lazy-loaded route with meta            │
│                                                             │
│  6. I18N       → src/i18n/ja/index.ts                      │
│                  Translate ALL text to Japanese             │
│                                                             │
├─────────────────────────────────────────────────────────────┤
│  DEFAULT: Use local ref() + direct API call                │
│  STORE: Only when sharing state across pages               │
│  ALWAYS: Follow CLAUDE.md Japanese business UI rules       │
└─────────────────────────────────────────────────────────────┘
```
