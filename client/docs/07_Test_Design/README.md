# テスト設計 (Test Design)

**プロジェクト名**: [Your Project Name]
**最終更新**: 2026-02-04

---

## 概要

このディレクトリには、プロジェクトのテスト仕様書が格納されています。

テスト仕様書は、画面設計書と詳細設計書を元に、テストケースを記述したドキュメントです。

---

## ディレクトリ構成

```
07_Test_Design/
├── README.md                              ← このファイル
├── 00_Test_Specification_Guidelines.md   ← テスト仕様書作成ガイドライン
├── 00_Test_Specification_Template.md     ← テスト仕様書テンプレート
└── （プロジェクト固有のテスト仕様書をここに追加）
```

**サンプルプロジェクト（タスク管理システム）の場合**:
```
07_Test_Design/
├── README.md
├── 00_Test_Specification_Guidelines.md
├── 00_Test_Specification_Template.md
├── 01_Authentication_Test_Spec.md         ← 認証機能テスト
├── 02_Project_Management_Test_Spec.md     ← プロジェクト管理テスト
├── 03_Task_Management_Test_Spec.md        ← タスク管理テスト
└── 04_E2E_Test_Spec.md                    ← E2Eテスト
```

---

## ファイル一覧

### **ガイドライン・テンプレート**

| ファイル名 | 内容 | 用途 |
|-----------|------|------|
| `00_Test_Specification_Guidelines.md` | テスト仕様書作成ガイドライン | テスト設計時の参照 |
| `00_Test_Specification_Template.md` | テスト仕様書テンプレート | 新規テスト仕様書作成時に使用 |

### **プロジェクト固有のテスト仕様書**

プロジェクト開始後、以下のようなテスト仕様書を作成します:

| ファイル名例 | 内容 | 用途 |
|-----------|------|------|
| `01_Authentication_Test_Spec.md` | 認証機能テスト仕様 | ログイン・認証テスト |
| `02_Project_Management_Test_Spec.md` | プロジェクト管理テスト仕様 | プロジェクトCRUDテスト |
| `03_Task_Management_Test_Spec.md` | タスク管理テスト仕様 | タスクCRUDテスト |
| `04_E2E_Test_Spec.md` | E2Eテスト仕様 | ユーザーシナリオテスト |

---

## 使い方

### 新しいテスト仕様書を作成する

1. `00_Test_Specification_Template.md` をコピー
2. ファイル名を `XX_[Function_Name]_Test_Spec.md` に変更
3. テンプレートに従って記載
4. `00_Test_Specification_Guidelines.md` を参照して記載粒度を確認

### ファイル名の命名規則

```
[番号]_[Function_Name]_Test_Spec.md
```

番号は画面設計書・詳細設計書と揃える

---

## 記載粒度

### 記載する情報
- 単体テスト（コンポーネント、関数、バリデーション）
- 統合テスト（API、データベース）
- E2Eテスト（ユーザーシナリオ、エラーシナリオ）
- 性能テスト（負荷条件、性能目標）
- セキュリティテスト（脆弱性、権限）

### 記載しない情報
- テストコード（Jest/Vitest等）
- ビジネスロジック（詳細設計書を参照）
- API詳細（API設計書を参照）

**詳細**: `00_Test_Specification_Guidelines.md` を参照

---

## 関連ドキュメント

- **プロジェクトガイドライン**: `../00_Project_Guidelines/`
- **要件定義**: `../01_Business_Process/requirements/`
- **技術設計**: `../02_Technical_Design/`
- **データベース設計**: `../03_Database_Design/`
- **画面設計**: `../04_Screen_Design/`
- **API設計**: `../05_API_Design/`
- **詳細設計**: `../06_Detailed_Design/`

---

## 注意事項

- テスト仕様書にはテストコードを記載しない（テストコードは実装時に作成）
- **テスト環境**: 対象ブラウザ・デバイスを明記すること
- テストケースは網羅的に記述すること

---

**作成者**: [Your Team Name]
**最終更新**: 2026-02-04
