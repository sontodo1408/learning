# [番号] [テーブルカテゴリ名]

**作成日**: YYYY-MM-DD  
**バージョン**: v1.0.0  
**対象Phase**: Phase 1 / Phase 2

**例**:
```markdown
# 02 マスタテーブル設計

**作成日**: 2025-12-30  
**バージョン**: v1.0.0  
**対象Phase**: Phase 1
```

---

## 目次

1. [概要](#概要)
2. [テーブル一覧](#テーブル一覧)
3. [テーブル詳細](#テーブル詳細)
4. [関連ドキュメント](#関連ドキュメント)
5. [整合性確認](#整合性確認)
6. [変更履歴](#変更履歴)

---

## 概要

### **このドキュメントの目的**

[このドキュメントが何を定義するかを記載]

**例**:
```markdown
このドキュメントは、システムのマスタテーブル設計を定義します。
マスタテーブルは、ユーザー、ロール、ステータス等の基本情報を管理します。
```

---

### **対象テーブル**

[このドキュメントで定義するテーブルの一覧]

**例**:
```markdown
- Warehouse（倉庫マスタ）
- Location（ロケーションマスタ）
- User（ユーザマスタ）
- Partner（取引先マスタ）
```

---

## テーブル一覧

| テーブル名 | 論理名 | 説明 | Phase |
|-----------|--------|------|-------|
| [TableName] | [論理名] | [説明] | Phase 1 |

**例**:
```markdown
| テーブル名 | 論理名 | 説明 | Phase |
|-----------|--------|------|-------|
| Warehouse | 倉庫マスタ | 倉庫の基本情報を管理 | Phase 1 |
| Location | ロケーションマスタ | 倉庫内のロケーション情報を管理 | Phase 1 |
| User | ユーザマスタ | ユーザの基本情報を管理 | Phase 1 |
```

---

## テーブル詳細

### **[TableName]（[論理名]）**

#### **概要**

[テーブルの目的と役割を記載]

**例**:
```markdown
倉庫の基本情報を管理するマスタテーブル。
複数の倉庫を管理し、各倉庫にロケーションを紐付ける。
```

---

#### **テーブル定義**

| カラム名 | データ型 | NULL | デフォルト | 説明 |
|---------|---------|------|-----------|------|
| id | UUID | NOT NULL | uuid_generate_v4() | 主キー |
| [column_name] | [data_type] | [NULL/NOT NULL] | [default] | [説明] |

**例**:
```markdown
| カラム名 | データ型 | NULL | デフォルト | 説明 |
|---------|---------|------|-----------|------|
| id | UUID | NOT NULL | uuid_generate_v4() | 主キー |
| code | VARCHAR(10) | NOT NULL | - | 倉庫コード |
| name | VARCHAR(100) | NOT NULL | - | 倉庫名 |
| address | TEXT | NULL | - | 住所 |
| is_active | BOOLEAN | NOT NULL | true | 有効フラグ |
| created_at | TIMESTAMP | NOT NULL | CURRENT_TIMESTAMP | 作成日時 |
| updated_at | TIMESTAMP | NOT NULL | CURRENT_TIMESTAMP | 更新日時 |
| deleted_at | TIMESTAMP | NULL | - | 削除日時（論理削除） |
```

---

#### **制約**

- **PRIMARY KEY**: `[constraint_name]` ON `[column_name]`
- **FOREIGN KEY**: `[constraint_name]` ON `[column_name]` REFERENCES `[table_name]([column_name])`
- **UNIQUE**: `[constraint_name]` ON `[column_name]`
- **CHECK**: `[constraint_name]` CHECK `[condition]`

**例**:
```markdown
- **PRIMARY KEY**: `pk_warehouse` ON `id`
- **UNIQUE**: `uq_warehouse_code` ON `code`
- **CHECK**: `ck_warehouse_code_length` CHECK `LENGTH(code) <= 10`
```

---

#### **インデックス**

- `[index_name]` ON `[column_name]` - [目的]

**例**:
```markdown
- `idx_warehouse_code` ON `code` - 倉庫コード検索の高速化
- `idx_warehouse_is_active` ON `is_active` - 有効な倉庫の絞り込み
```

---

#### **リレーション**

- **[RelatedTable]** ([関係]): `[foreign_key_column]` → `[referenced_table].[referenced_column]`

**例**:
```markdown
- **Location** (1:N): `warehouse_id` → `Warehouse.id`
- **Inventory** (1:N): `warehouse_id` → `Warehouse.id`
```

---

#### **ビジネスルール**

1. [ビジネスルール1]
2. [ビジネスルール2]

**例**:
```markdown
1. 倉庫コードは10文字以内の英数字
2. 論理削除を使用（`deleted_at IS NULL` で有効なレコードを判定）
3. 削除済みの倉庫は`is_active = false`
4. 倉庫コードは一意
```

---

#### **パフォーマンス考慮**

[パフォーマンスに関する考慮事項]

**例**:
```markdown
- `code`にインデックスを作成（検索頻度が高い）
- `is_active`にインデックスを作成（有効な倉庫の絞り込みが頻繁）
- 想定データ量: 初年度10件、年間成長率10%
```

---

#### **セキュリティ考慮**

[セキュリティに関する考慮事項]

**例**:
```markdown
- 個人情報は含まない
- 削除時は論理削除を使用（監査証跡のため）
```

---

#### **データ保持期間**

[データの保持期間]

**例**:
```markdown
- 永続保持（マスタデータ）
- 削除済みデータも監査のため永続保持
```

---

### **[次のテーブル名]（[論理名]）**

[上記と同じ構造で記載]

---

## 関連ドキュメント

### **参照元**

- **ビジネスプロセス**: `../01_Business_Process/requirements/`
- **ER図**: `../03_Database_Design/` - ER図ファイル

### **参照先**

- **画面設計書**: `Documents/04_UI_UX_Design/screen_design/`
- **API設計書**: `Documents/05_API_Design/`
- **詳細設計書**: `Documents/06_Detailed_Design/`

---

## 整合性確認

### **チェックリスト**

- [ ] すべてのテーブルに主キーが定義されている
- [ ] すべての外部キーに対応するインデックスが定義されている
- [ ] すべてのカラムにNULL制約が明示されている
- [ ] すべてのテーブルに`created_at`, `updated_at`が定義されている
- [ ] 論理削除を使用する場合、`deleted_at`が定義されている
- [ ] ER図とテーブル定義が一致している
- [ ] ビジネスルールが明確に記載されている
- [ ] 命名規則に従っている（テーブル名: PascalCase、カラム名: snake_case）
- [ ] データ型が適切に選択されている
- [ ] インデックスが適切に設計されている

---

## 変更履歴

| バージョン | 日付 | 変更内容 | 変更者 |
|-----------|------|---------|--------|
| v1.0.0 | YYYY-MM-DD | 初版作成 | [変更者名] |

**例**:
```markdown
| バージョン | 日付 | 変更内容 | 変更者 |
|-----------|------|---------|--------|
| v1.0.0 | YYYY-MM-DD | 初版作成 | [Your Team Name] |
| v1.1.0 | YYYY-MM-DD | テーブル追加 | [Your Team Name] |
```

---

**作成者**: [Your Team Name]
**最終更新**: 2026-02-04

