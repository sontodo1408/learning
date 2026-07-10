# API共通仕様

**作成日**: YYYY-MM-DD
**最終更新**: 2026-02-04
**バージョン**: v1.2.0
**対象**: 全APIエンドポイント

---

## 目次

1. [API設計原則](#api設計原則)
2. [ベースURL・バージョニング](#ベースurlバージョニング)
3. [認証・認可](#認証認可)
4. [共通リクエストヘッダー](#共通リクエストヘッダー)
5. [共通レスポンス形式](#共通レスポンス形式)
6. [エラーハンドリング](#エラーハンドリング)
7. [ページネーション](#ページネーション)
8. [ソート・フィルタリング](#ソートフィルタリング)
9. [ファイル操作](#ファイル操作)
10. [共通TypeScript型定義](#共通typescript型定義)

---

## API設計原則

### **1. RESTful設計**
- リソース指向のURL設計
- HTTPメソッドの適切な使用（GET, POST, PUT, DELETE）
- ステートレスな設計

### **2. 命名規則**
- **URL**: ケバブケース（`/api/v1/shipment-notices`）
- **JSONキー**: スネークケース（`user_code`, `created_at`）
- **TypeScript型**: パスカルケース（`UserResponse`, `LoginRequest`）

### **3. 一貫性**
- 全エンドポイントで統一されたレスポンス形式
- 統一されたエラーハンドリング
- 統一されたページネーション

### **4. 画面設計準拠**
- 28画面（SCR-01〜SCR-28）に完全対応
- 画面IDをコメントで明記

---

## ベースURL・バージョニング

### **ベースURL**

| 環境 | ベースURL |
|------|----------|
| **開発** | `http://localhost:8080/api/v1` |
| **ステージング** | `https://stg-api.your-domain.com/api/v1` |
| **本番** | `https://api.your-domain.com/api/v1` |

### **バージョニング戦略**

**Phase 1**: `/api/v1/...` を採用（明示的バージョニング）

**Phase 2以降**: 破壊的変更時に `/api/v2/...` を追加
- v1とv2を並行稼働可能
- v1サポート期間: v2リリース後6ヶ月

**バージョンアップのルール**:
- **メジャーバージョン（v1 → v2）**: 破壊的変更
  - エンドポイントURL変更
  - レスポンス構造の大幅変更
  - 必須パラメータの追加・削除
- **マイナーバージョン（v1内での更新）**: 後方互換性あり
  - オプションパラメータの追加
  - レスポンスフィールドの追加
  - 新エンドポイントの追加

---

## 認証・認可

### **認証方式**

#### **1. JWT（JSON Web Token）ベース認証**

**認証フロー**:
```
1. POST /api/v1/auth/login または POST /api/v1/auth/login/qrcode
2. レスポンスでJWTトークンを取得
3. 以降のリクエストでAuthorizationヘッダーにトークンを含める
4. トークン有効期限: 1時間（リフレッシュトークン: 7日間）
```

**Authorizationヘッダー**:
```
Authorization: Bearer <JWT_TOKEN>
```

### **JWT構造**

```typescript
{
  "header": {
    "alg": "HS256",
    "typ": "JWT"
  },
  "payload": {
    "sub": "user_id",                    // ユーザーID
    "user_code": "USR001",               // ユーザーコード
    "name": "山田太郎",                   // 氏名
    "email": "yamada@example.com",   // メールアドレス
    "role": "staff",                     // ロール（5段階）
    "permissions": ["asn:read", "asn:write"],
    "device_type": "pc",                 // デバイス種別（pc/tablet/ht）
    "iat": 1699999999,                   // 発行時刻
    "exp": 1700003599                    // 有効期限（1時間）
  }
}
```

### **権限チェック**

詳細は[セキュリティ設計書](../02_Technical_Design/05_セキュリティ設計書.md)を参照。

---

## 共通リクエストヘッダー

| ヘッダー | 必須 | 説明 | 例 |
|---------|------|------|-----|
| `Authorization` | ○ | JWTトークン | `Bearer eyJhbGc...` |
| `Content-Type` | ○ | リクエストボディの形式 | `application/json` |
| `Accept` | - | レスポンス形式 | `application/json` |
| `X-Request-ID` | - | リクエストID（トレーシング用） | `req-123456` |

---

## 共通レスポンス形式

### **成功レスポンス**

#### **単一リソース取得**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "user_code": "USR001",
    "name": "山田太郎"
  }
}
```

#### **リスト取得（ページネーションあり）**
```json
{
  "success": true,
  "data": [
    { "id": 1, "name": "..." },
    { "id": 2, "name": "..." }
  ],
  "pagination": {
    "page": 1,
    "per_page": 20,
    "total": 100,
    "total_pages": 5
  }
}
```

#### **作成・更新・削除**
```json
{
  "success": true,
  "message": "ユーザーを作成しました",
  "data": {
    "id": 1,
    "user_code": "USR001"
  }
}
```

---

## エラーハンドリング

### **エラーレスポンス形式**

```json
{
  "success": false,
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "入力内容に誤りがあります",
    "details": [
      {
        "field": "user_code",
        "message": "ユーザーコードは必須です"
      }
    ]
  }
}
```

### **HTTPステータスコード**

| コード | 説明 | 使用例 |
|-------|------|--------|
| **200** | OK | 成功（GET, PUT, DELETE） |
| **201** | Created | 作成成功（POST） |
| **400** | Bad Request | バリデーションエラー |
| **401** | Unauthorized | 認証エラー |
| **403** | Forbidden | 権限エラー |
| **404** | Not Found | リソースが見つからない |
| **409** | Conflict | 重複エラー |
| **422** | Unprocessable Entity | ビジネスロジックエラー |
| **500** | Internal Server Error | サーバーエラー |

### **エラーコード一覧**

| コード | HTTPステータス | 説明 |
|--------|--------------|------|
| `VALIDATION_ERROR` | 400 | バリデーションエラー |
| `UNAUTHORIZED` | 401 | 認証エラー（トークン無効・期限切れ） |
| `FORBIDDEN` | 403 | 権限エラー（アクセス権限なし） |
| `NOT_FOUND` | 404 | リソースが見つからない |
| `DUPLICATE_ERROR` | 409 | 重複エラー（ユニーク制約違反） |
| `BUSINESS_LOGIC_ERROR` | 422 | ビジネスロジックエラー |
| `INTERNAL_SERVER_ERROR` | 500 | サーバーエラー |

---

## ページネーション

### **リクエストパラメータ**

| パラメータ | 型 | デフォルト | 説明 |
|-----------|---|----------|------|
| `page` | number | 1 | ページ番号（1始まり） |
| `per_page` | number | 20 | 1ページあたりの件数 |

**例**:
```
GET /api/v1/partners?page=2&per_page=50
```

### **レスポンス**

```json
{
  "success": true,
  "data": [...],
  "pagination": {
    "page": 2,
    "per_page": 50,
    "total": 150,
    "total_pages": 3
  }
}
```

---

## ソート・フィルタリング

### **ソート**

| パラメータ | 型 | 説明 | 例 |
|-----------|---|------|-----|
| `sort_by` | string | ソート対象フィールド | `created_at` |
| `sort_order` | string | ソート順序（`asc`/`desc`） | `desc` |

**例**:
```
GET /api/v1/partners?sort_by=created_at&sort_order=desc
```

### **フィルタリング**

各エンドポイント固有のフィルタパラメータを使用。

**例（取引先一覧）**:
```
GET /api/v1/partners?type=owner&status=active
```

### **検索**

| パラメータ | 型 | 説明 |
|-----------|---|------|
| `q` | string | 全文検索キーワード |

**例**:
```
GET /api/v1/partners?q=NAX
```

---

## ファイル操作

### **ファイルアップロード**

**リクエスト**:
```
POST /api/v1/shipment-notices/import/csv
Content-Type: multipart/form-data

file: <CSVファイル>
```

**レスポンス**:
```json
{
  "success": true,
  "message": "CSVファイルを取り込みました",
  "data": {
    "imported_count": 10,
    "error_count": 2,
    "errors": [
      {
        "line": 3,
        "message": "荷主コードが存在しません"
      }
    ]
  }
}
```

### **ファイルダウンロード**

**リクエスト**:
```
GET /api/v1/reports/inbound?format=excel&start_date=2025-01-01&end_date=2025-01-31
```

**レスポンス**:
```
Content-Type: application/vnd.openxmlformats-officedocument.spreadsheetml.sheet
Content-Disposition: attachment; filename="inbound_report_20250101_20250131.xlsx"

<バイナリデータ>
```

---

## 共通TypeScript型定義

### **共通レスポンス型**

```typescript
// 成功レスポンス（単一リソース）
export interface SuccessResponse<T> {
  success: true;
  data: T;
  message?: string;
}

// 成功レスポンス（リスト）
export interface SuccessListResponse<T> {
  success: true;
  data: T[];
  pagination: PaginationResponse;
}

// エラーレスポンス
export interface ErrorResponse {
  success: false;
  error: {
    code: string;
    message: string;
    details?: Array<{
      field: string;
      message: string;
    }>;
  };
}

// ページネーション
export interface PaginationResponse {
  page: number;
  per_page: number;
  total: number;
  total_pages: number;
}

// ページネーションリクエスト
export interface PaginationRequest {
  page?: number;
  per_page?: number;
}

// ソートリクエスト
export interface SortRequest {
  sort_by?: string;
  sort_order?: 'asc' | 'desc';
}
```

### **共通フィールド型**

```typescript
// タイムスタンプ
export interface Timestamps {
  created_at: string;  // ISO 8601形式
  updated_at: string;  // ISO 8601形式
}

// ステータス
export type Status = 'active' | 'inactive';

// ロール（5段階）
export type Role = 'admin' | 'manager_office' | 'manager_warehouse' | 'staff' | 'worker';

// デバイス種別
export type DeviceType = 'pc' | 'tablet' | 'ht';
```

---

## 関連ドキュメント

- [セキュリティ設計書](../02_Technical_Design/05_セキュリティ設計書.md) - 認証・認可の詳細
- [プロジェクト構造設計書](../02_Technical_Design/04_プロジェクト構造設計書.md) - バックエンド構造
- [データベース設計書](../01_Database_Design/) - テーブル定義

---

**次のステップ**: 各カテゴリのAPI設計書を参照

