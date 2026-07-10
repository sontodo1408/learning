# API設計書 - [番号] [機能名]API

**作成日:** YYYY-MM-DD
**対象画面:** SCR-XX（画面名）、SCR-YY（画面名）
**対応機能:** [機能コード]-01〜[機能コード]-XX
**Phase:** **Phase [1/2]**

---

## 目次

1. [概要](#概要)
2. [エンドポイント一覧](#エンドポイント一覧)
3. [API詳細](#api詳細)
4. [TypeScript型定義](#typescript型定義)
5. [Fastifyルート定義](#fastifyルート定義)

---

## 概要

### **目的**
- [この機能の目的を記述]

### **Phase 1の実装範囲**
- ✅ [Phase 1で実装する機能1]
- ✅ [Phase 1で実装する機能2]

### **Phase 1の制約**
- [Phase 1での制約事項を記述]

### **Phase 2の実装範囲**
- ⏳ [Phase 2で実装する機能1]
- ⏳ [Phase 2で実装する機能2]

### **対応機能**
- [機能コード]-01: [機能名]
- [機能コード]-02: [機能名]

---

## エンドポイント一覧

| メソッド | エンドポイント | 説明 | 認証 | Phase |
|---------|--------------|------|------|-------|
| GET | `/api/v1/[resource]` | [リソース]一覧取得 | JWT | Phase 1 |
| GET | `/api/v1/[resource]/:id` | [リソース]詳細取得 | JWT | Phase 1 |
| POST | `/api/v1/[resource]` | [リソース]作成 | JWT | Phase 1 |
| PUT | `/api/v1/[resource]/:id` | [リソース]更新 | JWT | Phase 1 |
| DELETE | `/api/v1/[resource]/:id` | [リソース]論理削除 | JWT | Phase 1 |

---

## 1. [リソース]一覧取得

### **エンドポイント**
```
GET /api/v1/[resource]
```

### **概要**
- [エンドポイントの概要を記述]

### **対応機能**
- [機能コード]-XX: [機能名]

### **リクエストパラメータ（クエリパラメータ）**

| パラメータ | 型 | 必須 | 説明 | 例 |
|-----------|-----|------|------|-----|
| `page` | number | ❌ | ページ番号（デフォルト: 1） | `1` |
| `per_page` | number | ❌ | 1ページあたりの件数（デフォルト: 20） | `20` |
| `search` | string | ❌ | 検索キーワード | `keyword` |
| `sort_by` | string | ❌ | ソート項目 | `created_at` |
| `sort_order` | string | ❌ | ソート順（`asc`/`desc`） | `desc` |

### **レスポンス**

#### **成功時（200 OK）**
```json
{
  "success": true,
  "data": {
    "items": [
      {
        "id": 1,
        "field1": "value1",
        "field2": "value2",
        "created_at": "2025-01-15T10:00:00+09:00",
        "updated_at": "2025-01-15T10:00:00+09:00"
      }
    ],
    "pagination": {
      "total": 100,
      "page": 1,
      "per_page": 20,
      "total_pages": 5
    }
  }
}
```

#### **エラー時（400 Bad Request）**
```json
{
  "success": false,
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "バリデーションエラー",
    "details": [
      {
        "field": "page",
        "message": "ページ番号は1以上の整数である必要があります"
      }
    ]
  }
}
```

---

## 2. [リソース]作成

### **エンドポイント**
```
POST /api/v1/[resource]
```

### **概要**
- [エンドポイントの概要を記述]

### **対応機能**
- [機能コード]-XX: [機能名]

### **リクエストボディ**

```json
{
  "field1": "value1",
  "field2": "value2",
  "field3": 123
}
```

| フィールド | 型 | 必須 | 説明 | 例 |
|-----------|-----|------|------|-----|
| `field1` | string | ✅ | [説明] | `value1` |
| `field2` | string | ❌ | [説明] | `value2` |
| `field3` | number | ✅ | [説明] | `123` |

### **レスポンス**

#### **成功時（201 Created）**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "field1": "value1",
    "field2": "value2",
    "field3": 123,
    "created_at": "2025-01-15T10:00:00+09:00",
    "updated_at": "2025-01-15T10:00:00+09:00",
    "created_by": 1,
    "updated_by": 1
  }
}
```

#### **エラー時（400 Bad Request）**
```json
{
  "success": false,
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "バリデーションエラー",
    "details": [
      {
        "field": "field1",
        "message": "field1は必須です"
      }
    ]
  }
}
```

---

## TypeScript型定義

### **リクエスト型**
```typescript
// 一覧取得リクエスト
export interface GetResourceListRequest {
  page?: number;
  per_page?: number;
  search?: string;
  sort_by?: 'created_at' | 'updated_at';
  sort_order?: 'asc' | 'desc';
}

// 作成リクエスト
export interface CreateResourceRequest {
  field1: string;
  field2?: string;
  field3: number;
}

// 更新リクエスト
export interface UpdateResourceRequest {
  field1?: string;
  field2?: string;
  field3?: number;
}
```

### **レスポンス型**
```typescript
// リソース型
export interface Resource {
  id: number;
  field1: string;
  field2: string | null;
  field3: number;
  created_at: string;
  updated_at: string;
  created_by: number;
  updated_by: number;
  deleted_at: string | null;
  deleted_by: number | null;
}

// 一覧レスポンス
export interface GetResourceListResponse {
  success: true;
  data: {
    items: Resource[];
    pagination: {
      total: number;
      page: number;
      per_page: number;
      total_pages: number;
    };
  };
}

// 詳細レスポンス
export interface GetResourceDetailResponse {
  success: true;
  data: Resource;
}

// 作成レスポンス
export interface CreateResourceResponse {
  success: true;
  data: Resource;
}

// 更新レスポンス
export interface UpdateResourceResponse {
  success: true;
  data: Resource;
}

// 論理削除レスポンス
export interface DeleteResourceResponse {
  success: true;
  data: {
    id: number;
    deleted_at: string;
    deleted_by: number;
  };
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
```

---

## Fastifyルート定義

このセクションでは、実装コードではなく「どのエンドポイントがどのハンドラに対応するか」を表形式で記載し、実装コードへのパスのみを明記する。
実際のFastifyルート実装（TypeScriptコード）はソースコードを参照し、API設計書には記載しない。

### **ルート対応表（例）**

| 種別 | エンドポイント | ハンドラ名 | 実装パス |
|------|----------------|-----------|---------|
| 一覧 | `GET /api/v1/[resource]` | `resourceController.getList` | `backend/routes/[resource].ts` |
| 詳細 | `GET /api/v1/[resource]/:id` | `resourceController.getDetail` | `backend/routes/[resource].ts` |
| 作成 | `POST /api/v1/[resource]` | `resourceController.create` | `backend/routes/[resource].ts` |
| 更新 | `PUT /api/v1/[resource]/:id` | `resourceController.update` | `backend/routes/[resource].ts` |
| 論理削除 | `DELETE /api/v1/[resource]/:id` | `resourceController.softDelete` | `backend/routes/[resource].ts` |

---

## まとめ

### **実装時の注意点**
1. **認証**: すべてのエンドポイントでJWT認証が必要
2. **バリデーション**: Zodスキーマで厳密にバリデーション
3. **エラーハンドリング**: 統一されたエラーレスポンス形式
4. **論理削除**: DELETE操作は論理削除（`deleted_at`, `deleted_by`を設定）
5. **監査証跡**: `created_by`, `updated_by`, `deleted_by`を記録

### **参照ドキュメント**
- 共通仕様: `../05_API_Design/00_Common_Specifications.md`
- 画面設計書: `../04_Screen_Design/` - 画面設計書
- データベース設計: `../03_Database_Design/` - テーブル設計書

---

**作成者**: [作成者名]
**最終更新**: YYYY-MM-DD
```

