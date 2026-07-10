# データベース設計 (Database Design)

**プロジェクト名**: [Your Project Name]
**最終更新**: 2026-02-04
**ステータス**: [Phase X 進行中]

---

## 概要

このディレクトリには、プロジェクトのデータベース設計書が格納されています。

データベース設計書は、テーブル定義、制約、インデックス、ビジネスルールを記述したドキュメントです。

**重要**: このドキュメントは実装ツール（Prisma、TypeORM等）に依存しない設計書です。DDLが唯一の真実（Single Source of Truth）です。

---

## ディレクトリ構成

```
03_Database_Design/
├── README.md                                 ← このファイル
├── 00_Database_Design_Guidelines.md         ← データベース設計ガイドライン
├── 00_Database_Design_Template.md           ← テーブル設計書テンプレート
└── （プロジェクト固有の設計書をここに追加）
```

**サンプルプロジェクト（タスク管理システム）の場合**:
```
03_Database_Design/
├── README.md
├── 00_Database_Design_Guidelines.md
├── 00_Database_Design_Template.md
├── 01_Database_Overview.md                  ← データベース全体概要
├── 02_Table_Design_Master.md                ← マスタテーブル設計（users等）
├── 03_Table_Design_Transaction.md           ← トランザクションテーブル設計（projects, tasks等）
├── 04_ER_Diagram.md                         ← ER図
├── 05_Constraints_Indexes.md                ← 制約・インデックス設計
└── 06_DDL.sql                               ← DDL（CREATE TABLE文）
```

---

## 使い方

### **新しいデータベース設計書を作成する**

1. `00_Database_Design_Template.md` をコピー
2. ファイル名を `XX_[Category_Name].md` に変更
3. テンプレートに従って記載
4. `00_Database_Design_Guidelines.md` を参照して記載粒度を確認

### **ファイル名の命名規則**

```
[番号]_[Category_Name].md
```

**例**:
- `01_Database_Overview.md`
- `02_Table_Design_Master.md`
- `03_Table_Design_Transaction.md`

---

## 記載粒度

### **記載する情報**
- ✅ テーブル名、カラム名
- ✅ データ型（PostgreSQL標準型）
- ✅ 制約（PRIMARY KEY, FOREIGN KEY, UNIQUE, NOT NULL, CHECK）
- ✅ インデックス
- ✅ デフォルト値
- ✅ ビジネスルール
- ✅ リレーション
- ✅ ER図（Mermaid）

### **記載しない情報**
- ❌ Prismaスキーマ
- ❌ TypeORM Entity
- ❌ SQL文
- ❌ マイグレーションツールの選定
- ❌ ORM固有の設定

**理由**: 実装ツールは開発者が選択するため、設計書には記載しない

**詳細**: `00_Database_Design_Guidelines.md` を参照

---

## ファイル一覧

### **ガイドライン・テンプレート**

| ファイル名 | 内容 | 用途 |
|-----------|------|------|
| `00_Database_Design_Guidelines.md` | データベース設計書作成ガイドライン | 設計時の参照 |
| `00_Database_Design_Template.md` | データベース設計書テンプレート | 新規テーブル設計時に使用 |

### **プロジェクト固有の設計書**

プロジェクト開始後、以下のような設計書を作成します：

| ファイル名例 | 内容 | 用途 |
|-----------|------|------|
| `01_Database_Overview.md` | データベース全体概要 | 全体像の把握 |
| `02_Table_Design_Master.md` | マスタテーブル設計 | マスタデータの定義 |
| `03_Table_Design_Transaction.md` | トランザクションテーブル設計 | 業務データの定義 |
| `04_ER_Diagram.md` | ER図（Mermaid形式） | テーブル間の関係を可視化 |
| `05_Constraints_Indexes.md` | 制約・インデックス設計 | パフォーマンス・整合性確保 |
| `06_DDL.sql` | DDL（CREATE TABLE文） | 実装用SQL |

---

## テーブルカテゴリの例

### **マスタテーブル**
システムの基本情報を管理するテーブル

**例（タスク管理システム）**:
- `users`: ユーザーマスタ
- `roles`: ロールマスタ
- `statuses`: ステータスマスタ
- `priorities`: 優先度マスタ

### **トランザクションテーブル**
業務データを管理するテーブル

**例（タスク管理システム）**:
- `projects`: プロジェクト
- `tasks`: タスク
- `comments`: コメント
- `attachments`: 添付ファイル

### **関連テーブル（中間テーブル）**
多対多の関係を管理するテーブル

**例（タスク管理システム）**:
- `project_members`: プロジェクトメンバー（users ↔ projects）
- `task_assignees`: タスク担当者（users ↔ tasks）

### **履歴・ログテーブル**
操作履歴や監査ログを管理するテーブル

**例（タスク管理システム）**:
- `audit_logs`: 操作ログ（すべての重要操作を記録）
- `task_history`: タスク変更履歴

---

## 命名規則

### **テーブル名**
- **形式**: スネークケース（小文字 + アンダースコア）
- **言語**: 英語
- **複数形**: 基本的に単数形（例外: `inventory_holds`）

**例**:
- ✅ `shipment_notice_in`, `inbound_request`, `user_master`
- ❌ `ShipmentNoticeIn`, `入庫依頼`, `inbound_requests`

### **カラム名**
- **形式**: スネークケース（小文字 + アンダースコア）
- **言語**: 英語
- **主キー**: `id`（SERIAL型）
- **外部キー**: `{参照先テーブル名}_id`（例: `warehouse_id`, `owner_id`）
- **日付**: `{項目名}_on`（例: `vanning_on`, `delivery_on`）
- **日時**: `{項目名}_at`（例: `created_at`, `updated_at`）
- **フラグ**: `is_{項目名}`, `flag_{項目名}`（例: `is_bonded`, `flag_exp`）

**例**:
- ✅ `user_id`, `created_at`, `is_active`, `vanning_on`, `flag_exp`
- ❌ `userId`, `CreatedAt`, `作成日時`, `vanningDate`

---

## データ型

### **PostgreSQL標準型を使用**

| 用途 | データ型 | 例 |
|------|---------|-----|
| **主キー** | SERIAL | `id SERIAL PRIMARY KEY` |
| **文字列（短）** | VARCHAR(n) | `code VARCHAR(10)` |
| **文字列（長）** | TEXT | `notes TEXT` |
| **整数** | INT | `quantity INT` |
| **小数** | NUMERIC(p, s) | `price NUMERIC(10, 2)` |
| **真偽値** | BOOLEAN | `is_active BOOLEAN` |
| **日付** | DATE | `expected_date DATE` |
| **日時** | TIMESTAMP | `created_at TIMESTAMP` |
| **JSON** | JSONB | `metadata JSONB` |

**注**: 主キーはSERIAL型（自動採番）を使用します。UUID使用は分散システム化する場合に検討します。

---

## 関連ドキュメント

### **設計ガイドライン・テンプレート**
- `00_Database_Design_Guidelines.md`: データベース設計ガイドライン
- `00_Database_Design_Template.md`: テーブル設計書テンプレート

### **プロジェクトガイドライン**
- `../00_Project_Guidelines/05_Design_Process_Workflow.md`: 設計プロセスワークフロー
- `../00_Project_Guidelines/06_Design_Implementation_Guide.md`: 設計実施ガイド

### **業務プロセス・要件定義**
- `../01_Business_Process/requirements/`: 要件定義書

### **技術設計**
- `../02_Technical_Design/`: 技術スタック・インフラ設計

### **画面設計**
- `../04_Screen_Design/`: 画面設計書

### **API設計**
- `../05_API_Design/`: API設計書

---

## 注意事項

### **実装ツールについて**
- データベース設計書は実装ツール（Prisma、TypeORM等）に依存しない
- 実装ツールの選定は開発者が行う
- 実装段階でPrismaスキーマ、TypeORM Entity等を作成

### **マイグレーション管理**
- マイグレーションツールの選定は開発者が行う
- Prisma、TypeORM、Knex.js等を検討
- マイグレーション履歴は`prisma/migrations/`等に格納

---

**作成者**: [Your Team Name]
**最終更新**: 2026-02-04
