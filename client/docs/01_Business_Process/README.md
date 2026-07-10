# 業務プロセス・要件定義 (Business Process & Requirements)

**プロジェクト名**: [Your Project Name]
**最終更新**: 2026-02-04

---

## 概要

このディレクトリには、プロジェクトの業務プロセス設計と要件定義に関する資料が格納されています。

---

## ディレクトリ構造

```
01_Business_Process/
├── README.md                                    ← このファイル
└── requirements/                                ← 要件定義書
    ├── Requirements_Definition_Template.md     ← 要件定義書テンプレート
    └── Sample_TaskManagement_Requirements.md   ← サンプル（タスク管理システム）
```

---

## ファイル一覧

### 要件定義書テンプレート

| ファイル名 | 内容 | 用途 |
|-----------|------|------|
| `Requirements_Definition_Template.md` | 要件定義書テンプレート | 新規プロジェクトの要件定義作成時に使用 |

### サンプル要件定義書

| ファイル名 | 内容 | 用途 |
|-----------|------|------|
| `Sample_TaskManagement_Requirements.md` | タスク管理システムの要件定義（サンプル） | テンプレート活用例として参照 |

---

## 使い方

### 1. 新規プロジェクトの要件定義作成

1. `Requirements_Definition_Template.md` をコピー
2. ファイル名を `[ProjectName]_Requirements.md` に変更
3. テンプレートに従って要件を記述
4. プロジェクト固有の情報（プロジェクト名、作成者、日付等）を更新

### 2. サンプルの活用

- `Sample_TaskManagement_Requirements.md` を参考に、自プロジェクトの要件定義を作成
- サンプルは削除せず、参考資料として残すことを推奨

### 3. 記載のポイント

- **機能要件**: 機能一覧と詳細を明確に記述
- **非機能要件**: パフォーマンス、セキュリティ、可用性、保守性を記述
- **ビジネスルール**: 業務上の制約・ルールを明記
- **用語集**: プロジェクト固有の用語を定義

---

## 関連ドキュメント

- [プロジェクトガイドライン](../00_Project_Guidelines/) - 開発プロセス全体
- [技術設計](../02_Technical_Design/) - 技術スタック・アーキテクチャ
- [データベース設計](../03_Database_Design/) - DB設計
- [画面設計](../04_Screen_Design/) - 画面設計
- [API設計](../05_API_Design/) - API設計

---

**作成者**: [Your Team Name]
**最終更新**: 2026-02-04
