---
name: file_name
description: Naming and placement conventions for screens, dialogs, and i18n files in this project. Read before creating a new view, dialog, or i18n entry.
---

# Frontend Guidelines: File Naming & i18n Management

This document defines the naming conventions for screen/dialog structures and the management of multilingual (i18n) files for the project. All development team members and AI assistants must strictly adhere to these rules.

---

## 1. Screen File Naming Convention (Pages/Views)

- **Location:** directly under `src/views/` (including subfolders like `admin/`, `study/`).
- **Format:** `<code>_<Name>.vue`
- **Screen Code:** Starts with the letter `S` followed by 4 digits (e.g., `S0001`, `S0002`, `S0003`), assigned sequentially in the order screens were created — the sequence is global across the whole `src/views/` tree, not per-folder.
- **Screen Name:** Concise, written in `PascalCase`.
- **Valid Examples:**
  - `S0001_Home.vue`
  - `S0002_VideoVocab.vue`
  - `S0003_Login.vue`
- **Invalid Examples:**
  - `LoginPage.vue` (missing screen code)
  - `s0001_login_page.vue` (lowercase code, not PascalCase)

`<code>` alone (no `_Name` suffix) also doubles as that screen's i18n namespace (see below) — the code is already a unique identifier, no need to repeat the descriptive name there.

---

## 2. Dialog File Naming & Placement Convention

- **Format:** `<code>_<Name>.vue`, same shape as screens but with a `D` code instead of `S` — e.g. a component that used to be named `StudySetPickerDialog.vue` becomes `D0001_StudySetPicker` (drop the redundant `Dialog` suffix; the `D` prefix already says what it is).
- **Dialog Code:** Starts with the letter `D` followed by 4 digits (`D0001`, `D0002`, ...), assigned sequentially in the order dialogs were created — one global sequence across the whole project, independent of the `S` sequence.
- **Placement:** a dialog file lives **next to the screen (or other component) that opens it**, not in `src/components/`. `src/components/` stays reserved for reusable UI primitives (e.g. `src/components/common/`) and other non-dialog shared pieces — a dialog is owned by its one caller, so it belongs in that caller's own directory. If more than one screen ends up needing the exact same dialog, move it to the lowest directory that is a common ancestor of its callers at that point, not before.
- **Usage:** a dialog component is opened via `dialog.showContent(title, DialogComponent, config)` from `@/utilities/dialog` (see [src/utilities/dialog.js](../../../src/utilities/dialog.js)) — the component receives `config.params` as props (if any) and resolves the dialog by emitting `done` with whatever value the caller should get back (or `null`/`undefined` to signal "cancelled").
- **Example:** [src/views/admin/D0001_StudySetPicker.vue](../../../src/views/admin/D0001_StudySetPicker.vue), opened from [src/views/admin/S0002_VideoVocab.vue](../../../src/views/admin/S0002_VideoVocab.vue) in the same folder.
- **Content padding:** `DialogFrame` (see [src/components/DialogFrame.vue](../../../src/components/DialogFrame.vue)) renders a `showContent` component directly with no padding of its own — unlike `showMessage`/`showConfirm` dialogs, which get Quasar's default `q-card-section` padding for free. Every dialog content component must therefore add its own `padding: 20px` (all four sides) in its root/scoped style, so content doesn't sit flush against the dialog edges.

Dialog files placed under `src/views/` this way are *not* screens — they have no route and are not counted in the `S` sequence — the naming rule above is what marks them as dialogs instead.

---

## 3. i18n Management Rules (Multilingual)

- **System File Quantity:** Maintain exactly 2 language files: [src/i18n/messages/vi.js](../../../src/i18n/messages/vi.js) (Vietnamese, default) and [src/i18n/messages/en.js](../../../src/i18n/messages/en.js) (English, fallback).
- **Root Hierarchy Structure:**
  - `common` — shared/chrome strings used across the app (e.g. Save, Cancel, nav labels).
  - `<code>` (matching a screen's or dialog's file-name prefix, e.g. `S0001`, `D0001`) — each is its own top-level object, at the same level as `common`.
- **Internal Structure:** within a screen/dialog's namespace, group text under `btn` (button labels) and `label` (standalone text/placeholders) — see existing entries (e.g. `S0002.btn.addCard`, `S0002.label.termPlaceholder`) for the pattern actually used in this codebase.
- Interpolated messages use vue-i18n's `{named}` syntax, e.g. `S0004.label.cardProgress: 'Card {current} / {total}'` called as `t('S0004.label.cardProgress', { current, total })`.
- In components: `const { t } = useI18n()`, then `t('common.nav.dashboard')` or `t('S0002.btn.addCard')`. Never hardcode user-facing text. When adding a new screen or dialog, add its entry under the matching code in **both** `vi.js` and `en.js`.
