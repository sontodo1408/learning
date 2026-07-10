# 詳細設計 (Detailed Design)

**プロジェクト名**: [Your Project Name]
**最終更新**: 2026-02-04
**ステータス**: [Phase X 進行中]

---

## 概要

このディレクトリには、プロジェクトの詳細設計書が格納されています。

詳細設計書は、画面設計書とAPI設計書を橋渡しし、実装に必要な詳細情報を記述したドキュメントです。

---

## ディレクトリ構成

```
06_Detailed_Design/
├── README.md                                    ← このファイル
├── 00_Detailed_Design_Guidelines.md            ← 詳細設計書作成ガイドライン
├── 00_Detailed_Design_Template.md              ← 詳細設計書テンプレート
└── （プロジェクト固有の設計書をここに追加）
```

**サンプルプロジェクト（タスク管理システム）の場合**:
```
06_Detailed_Design/
├── README.md
├── 00_Detailed_Design_Guidelines.md
├── 00_Detailed_Design_Template.md
├── 01_Authentication_Detailed_Design.md        ← 認証機能（SCR-01）
├── 02_Project_List_Detailed_Design.md          ← プロジェクト一覧（SCR-02）
├── 03_Project_Detail_Detailed_Design.md        ← プロジェクト詳細（SCR-03）
├── 04_Task_Detail_Detailed_Design.md           ← タスク詳細（SCR-04）
└── 05_User_Management_Detailed_Design.md       ← ユーザー管理（SCR-05）
```

---

## 使い方

### **新しい詳細設計書を作成する**

1. `00_Detailed_Design_Template.md` をコピー
2. ファイル名を `XX_[Function_Name]_Detailed_Design.md` に変更
3. テンプレートに従って記載
4. `00_Detailed_Design_Guidelines.md` を参照して記載粒度を確認

### **ファイル名の命名規則**

```
[番号]_[Function_Name]_Detailed_Design.md
```

**番号はAPI設計書と揃える**:
- `01_Authentication_Detailed_Design.md` ← API設計書: `01_Authentication_API.md`
- `02_Project_List_Detailed_Design.md` ← API設計書: `02_Project_API.md`
- `03_Task_Detail_Detailed_Design.md` ← API設計書: `03_Task_API.md`

---

## ファイル一覧

### **ガイドライン・テンプレート**

| ファイル名 | 内容 | 用途 |
|-----------|------|------|
| `00_Detailed_Design_Guidelines.md` | 詳細設計書作成ガイドライン | 設計時の参照 |
| `00_Detailed_Design_Template.md` | 詳細設計書テンプレート | 新規詳細設計時に使用 |

### **プロジェクト固有の設計書**

プロジェクト開始後、以下のような設計書を作成します:

| ファイル名例 | 内容 | 用途 |
|-----------|------|------|
| `01_Authentication_Detailed_Design.md` | 認証機能詳細設計 | ログイン・認証処理 |
| `02_Project_List_Detailed_Design.md` | プロジェクト一覧詳細設計 | 一覧表示・フィルタ処理 |
| `03_Project_Detail_Detailed_Design.md` | プロジェクト詳細詳細設計 | 詳細表示・編集処理 |
| `04_Task_Detail_Detailed_Design.md` | タスク詳細詳細設計 | タスク管理処理 |
| `05_User_Management_Detailed_Design.md` | ユーザー管理詳細設計 | ユーザーCRUD処理 |

---

## 記載粒度

### 記載する情報
- コンポーネント構成（画面固有のみ）
- 状態管理（ローカル状態、グローバル状態）
- イベントハンドラ（処理フロー）
- ビジネスロジック（計算式、バリデーションルール）
- データフロー図・シーケンス図（Mermaid）
- エラーハンドリング
- パフォーマンス・セキュリティ要件

### 記載しない情報
- 完全な実装コード
- HTML構造（画面設計書を参照）
- API詳細（API設計書を参照）

**詳細**: `00_Detailed_Design_Guidelines.md` を参照

---

## 関連ドキュメント

- **プロジェクトガイドライン**: `../00_Project_Guidelines/`
- **要件定義**: `../01_Business_Process/requirements/`
- **技術設計**: `../02_Technical_Design/`
- **データベース設計**: `../03_Database_Design/`
- **画面設計**: `../04_Screen_Design/`
- **API設計**: `../05_API_Design/`
- **テスト設計**: `../07_Test_Design/`

---

## 注意事項

- DELETE操作は論理削除または無効化を検討すること
- テストケースは別途テスト設計書で定義
- ステータス定義はDDLを正とする
- 設計書間の整合性を常に確認すること

---

**作成者**: [Your Team Name]
**最終更新**: 2026-02-04
