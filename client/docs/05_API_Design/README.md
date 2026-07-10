# API設計 (API Design)

**プロジェクト名**: [Your Project Name]
**最終更新**: 2026-02-04
**ステータス**: [Phase X 進行中]

---

## 概要

このディレクトリには、プロジェクトのバックエンドAPI設計書が格納されています。

### 技術スタック例
- **バックエンド**: Node.js / Java / Python + TypeScript
- **データベース**: PostgreSQL / MySQL
- **ORM**: Prisma / TypeORM / JPA
- **認証**: JWT + Passport.js / Spring Security
- **バリデーション**: Zod / class-validator

### API設計の基本方針
- **RESTful設計**: リソース指向のURL設計
- **一貫性**: 統一されたレスポンス形式
- **セキュリティ**: JWT認証、入力バリデーション
- **パフォーマンス**: ページネーション、キャッシング
- **拡張性**: バージョニング（`/api/v1/...`）、モジュール化

---

## ディレクトリ構成

```
05_API_Design/
├── README.md                          ← このファイル
├── 00_API_Design_Template.md         ← API設計書テンプレート
├── 00_Common_Specifications.md       ← API共通仕様
└── （プロジェクト固有の設計書をここに追加）
```

**サンプルプロジェクト（タスク管理システム）の場合**:
```
05_API_Design/
├── README.md
├── 00_API_Design_Template.md
├── 00_Common_Specifications.md
├── 01_Authentication_API.md           ← 認証API
├── 02_Project_API.md                  ← プロジェクトAPI
├── 03_Task_API.md                     ← タスクAPI
├── 04_Comment_API.md                  ← コメントAPI
└── 05_User_Management_API.md          ← ユーザー管理API
```

---

## ファイル一覧

### **ガイドライン・テンプレート**

| ファイル名 | 内容 | 用途 |
|-----------|------|------|
| `00_API_Design_Template.md` | API設計書テンプレート | 新規API設計時に使用 |
| `00_Common_Specifications.md` | API共通仕様（認証・エラーハンドリング等） | 全API共通の仕様定義 |

### **プロジェクト固有の設計書**

プロジェクト開始後、以下のような設計書を作成します：

| ファイル名例 | 内容 | 用途 |
|-----------|------|------|
| `01_Authentication_API.md` | 認証API（ログイン・ログアウト） | 認証機能 |
| `02_Project_API.md` | プロジェクトAPI（CRUD） | プロジェクト管理 |
| `03_Task_API.md` | タスクAPI（CRUD、ステータス更新） | タスク管理 |
| `04_Comment_API.md` | コメントAPI（CRUD） | コメント機能 |
| `05_User_Management_API.md` | ユーザー管理API（CRUD） | ユーザー管理 |

---

## 想定エンドポイント一覧の例

### **タスク管理システムのエンドポイント例**

#### **1. 認証（4エンドポイント）**
| メソッド | エンドポイント | 説明 | 対応画面 |
|---------|--------------|------|---------|
| POST | `/api/v1/auth/login` | ログイン | SCR-01 |
| POST | `/api/v1/auth/logout` | ログアウト | SCR-01 |
| GET | `/api/v1/auth/me` | 現在のユーザー情報取得 | 全画面 |
| POST | `/api/v1/auth/refresh` | トークンリフレッシュ | 全画面 |

#### **2. プロジェクト管理（6エンドポイント）**
| メソッド | エンドポイント | 説明 | 対応画面 |
|---------|--------------|------|---------|
| GET | `/api/v1/projects` | プロジェクト一覧取得 | SCR-02 |
| GET | `/api/v1/projects/:id` | プロジェクト詳細取得 | SCR-03 |
| POST | `/api/v1/projects` | プロジェクト作成 | SCR-02 |
| PUT | `/api/v1/projects/:id` | プロジェクト更新 | SCR-03 |
| DELETE | `/api/v1/projects/:id` | プロジェクト削除 | SCR-03 |
| GET | `/api/v1/projects/:id/members` | プロジェクトメンバー一覧取得 | SCR-03 |

#### **3. タスク管理（8エンドポイント）**
| メソッド | エンドポイント | 説明 | 対応画面 |
|---------|--------------|------|---------|
| GET | `/api/v1/tasks` | タスク一覧取得（フィルタ・ソート対応） | SCR-03 |
| GET | `/api/v1/tasks/:id` | タスク詳細取得 | SCR-04 |
| POST | `/api/v1/tasks` | タスク作成 | SCR-03 |
| PUT | `/api/v1/tasks/:id` | タスク更新 | SCR-04 |
| DELETE | `/api/v1/tasks/:id` | タスク削除 | SCR-04 |
| PUT | `/api/v1/tasks/:id/status` | タスクステータス更新 | SCR-04 |
| POST | `/api/v1/tasks/:id/assign` | タスク担当者割当 | SCR-04 |
| GET | `/api/v1/tasks/:id/history` | タスク変更履歴取得 | SCR-04 |

#### **4. コメント管理（4エンドポイント）**
| メソッド | エンドポイント | 説明 | 対応画面 |
|---------|--------------|------|---------|
| GET | `/api/v1/tasks/:taskId/comments` | コメント一覧取得 | SCR-04 |
| POST | `/api/v1/tasks/:taskId/comments` | コメント作成 | SCR-04 |
| PUT | `/api/v1/comments/:id` | コメント更新 | SCR-04 |
| DELETE | `/api/v1/comments/:id` | コメント削除 | SCR-04 |

#### **5. ユーザー管理（5エンドポイント）**
| メソッド | エンドポイント | 説明 | 対応画面 |
|---------|--------------|------|---------|
| GET | `/api/v1/users` | ユーザー一覧取得 | SCR-05 |
| GET | `/api/v1/users/:id` | ユーザー詳細取得 | SCR-05 |
| POST | `/api/v1/users` | ユーザー作成 | SCR-05 |
| PUT | `/api/v1/users/:id` | ユーザー更新 | SCR-05 |
| DELETE | `/api/v1/users/:id` | ユーザー削除 | SCR-05 |


---

## 設計原則

### **1. RESTful設計**
- **リソース指向**: URL設計はリソース（名詞）を基本とする
- **HTTPメソッド**: GET（取得）、POST（作成）、PUT（更新）、DELETE（削除）
- **ステータスコード**: 200（成功）、201（作成）、400（バリデーションエラー）、401（認証エラー）、404（Not Found）、500（サーバーエラー）

### **2. URL命名規則**
- **ベースURL**: `/api/v1/...`（バージョニング）
- **リソース名**: 複数形（`/users`, `/projects`, `/tasks`）
- **ケバブケース**: `/project-members`（スネークケース不可）
- **階層構造**: `/projects/:id/tasks`（親子関係を表現）

### **3. レスポンス形式**
```json
{
  "success": true,
  "data": { ... },
  "message": "操作が成功しました"
}
```

### **4. エラーレスポンス形式**
```json
{
  "success": false,
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "入力内容に誤りがあります",
    "details": {
      "field_name": "エラーメッセージ"
    }
  }
}
```

### **5. ページネーション**
```
GET /api/v1/tasks?page=1&per_page=20&sort=created_at&order=desc
```

### **6. 認証**
- **JWT**: `Authorization: Bearer <token>`
- **リフレッシュトークン**: セキュアなトークン更新

### **7. バリデーション**
- **Zod / class-validator**: スキーマ定義
- **OpenAPI**: API仕様の自動生成

---

## API設計書の作成方法

### **1. テンプレートをコピー**

`00_API_Design_Template.md` をコピーして、新しいAPI設計書を作成します。

```bash
cp 00_API_Design_Template.md 02_Project_API.md
```

### **2. ファイル名の命名規則**

```
[番号]_[機能名]_API.md
```

**例**:
- `01_Authentication_API.md`
- `02_Project_API.md`
- `03_Task_API.md`
- `04_Comment_API.md`

### **3. 各セクションの記述方法**

#### **概要**
- 機能の目的を記述
- 実装範囲を明記
- 対応画面を記述

#### **エンドポイント一覧**
- 表形式でエンドポイント一覧を記述
- メソッド、エンドポイント、説明、認証を明記

#### **API詳細**
- 各エンドポイントの詳細を記述
- リクエストパラメータ、レスポンス、エラーレスポンスを記述
- JSON例を記述

#### **型定義**
- リクエスト型、レスポンス型を記述
- バリデーションスキーマを記述

#### **ルート定義**
- フレームワーク固有のルート定義例を記述
- OpenAPI/Swagger自動生成対応

### **4. 注意事項**

- **削除方式**: 以下の2種類を区別してください
  - **論理削除**（`deleted_at`を持つテーブル）: DELETEメソッドを使用
  - **無効化**（`is_active`フラグを持つテーブル）: PUTメソッドで`is_active = false`に更新
- **認証**: すべてのエンドポイントでJWT認証が必要（ログインAPIを除く）
- **バリデーション**: スキーマで厳密にバリデーション
- **エラーハンドリング**: 統一されたエラーレスポンス形式

---

## 使い方

### **1. 設計レビュー時**
- API設計書を参照して、API設計の方向性を確認
- 画面設計との対応関係を確認
- エンドポイント一覧で全体像を把握

### **2. 実装時**
- API設計書を参照して、スキーマ定義を作成
- OpenAPI/Swaggerが自動生成される
- 画面設計の順序に従って実装

### **3. 詳細化時**
- テンプレート（`00_API_Design_Template.md`）を使用して、新しいAPI設計書を作成
- 既存のAPI設計書と同じパターンで詳細化

---

## 関連ドキュメント

- **プロジェクトガイドライン**: `../00_Project_Guidelines/`
- **要件定義**: `../01_Business_Process/requirements/`
- **技術設計**: `../02_Technical_Design/`
- **データベース設計**: `../03_Database_Design/`
- **画面設計**: `../04_Screen_Design/`
- **詳細設計**: `../06_Detailed_Design/`
- **テスト設計**: `../07_Test_Design/`

---

**作成者**: [Your Team Name]
**最終更新**: 2026-02-04
