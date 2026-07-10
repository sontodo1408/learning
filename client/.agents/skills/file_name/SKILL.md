content = """# Frontend Guidelines: File Naming & i18n Management

This document defines the naming conventions for screen structures and the management of multilingual (i18n) files for the project. All development team members and AI assistants must strictly adhere to these rules.

---

## 1. Screen File Naming Convention (Pages/Views)

- **Format:** `[Screen Code]_[Screen Name].vue`
- **Screen Code:** Starts with the letter `S` followed by 4 digits (e.g., `S0001`, `S0002`, `S0003`).
- **Screen Name:** Must be extremely concise, **maximum 2 words**, written in `PascalCase`.
- **Valid Examples (Correct):**
  - `S0001_LoginPage.vue` (Code S0001 + 2-word name: Login Page)
  - `S0002_Home.vue` (Code S0002 + 1-word name: Home)
  - `S0003_UserList.vue` (Code S0003 + 2-word name: User List)
- **Invalid Examples (Incorrect):**
  - `S0004_UserPaymentHistoryPage.vue` (Exceeds the 2-word limit)
  - `LoginPage.vue` (Missing screen code)
  - `s0001_login_page.vue` (Incorrect PascalCase format and lowercase screen code)

---

## 2. i18n Management Rules (Multilingual)

- **System File Quantity:** Maintain exactly 2 language files:
  - Vietnamese: `vi.json` (or `vi.js`)
  - English: `en.json` (or `en.js`)
- **Root JSON Hierarchy Structure:**
  - `common`: Contains shared vocabulary/labels used across the system (e.g., Save, Cancel, OK, Confirm...).
  - `app`: Contains overall information and general configuration of the application (App name, version, copyright...).
  - `[Screen Code]`: Each screen (e.g., `S0001`, `S0002`) will be a distinct top-level object, at the same level as `common` and `app`.
- **Internal Screen Code Structure:** Text fields must be clearly categorized by specific element types such as `title`, `label`, `button`, `placeholder`, `message`... to prevent clutter and disorganization.

---

## 3. Standard i18n Structure Template (Template for en.json)

```json
{
  "common": {
    "save": "Save",
    "cancel": "Cancel",
    "confirm": "Confirm"
  },
  "app": {
    "name": "Application Name",
    "version": "Version 1.0"
  },
  "S0001": {
    "title": "Login",
    "label": {
      "username": "Username",
      "password": "Password"
    },
    "button": {
      "login": "Login",
      "forgotPassword": "Forgot Password?"
    },
    "placeholder": {
      "username": "Enter your username",
      "password": "Enter your password"
    },
    "message": {
      "loginSuccess": "Login successful!",
      "loginFail": "Invalid username or password."
    }
  },
  "S0002": {
    "title": "Home",
    "label": {
      "welcome": "Welcome back"
    },
    "button": {
      "logout": "Logout"
    }
  }
}
```
