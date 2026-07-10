# 技術設計 (Technical Design)

**プロジェクト名**: [Your Project Name]
**最終更新**: 2026-02-04

---

## 概要

このディレクトリには、プロジェクトの技術設計に関するドキュメントが含まれています。
技術スタックの選定、インフラ構成設計、開発環境構築、セキュリティ設計など、システム実装の技術的基盤を定義します。

---

## ディレクトリ構成

```
02_Technical_Design/
├── README.md                              ← このファイル
├── 01_AWS_Architecture.md                 ← AWS構成設計
├── 02_Development_Environment_Setup.md   ← 開発環境構築手順
├── 03_Project_Structure.md               ← プロジェクト構造設計
├── 04_Security_Design.md                 ← セキュリティ設計
├── 05_CICD_Design.md                     ← CI/CD設計
└── 06_Tech_Stack_Common_Specs.md         ← 技術スタック共通仕様
```

---

## ドキュメント一覧

| ファイル名 | 内容 | 状態 |
|-----------|------|------|
| `01_AWS_Architecture.md` | AWS構成設計（ECS/RDS/S3等） | ✅ サンプル |
| `02_Development_Environment_Setup.md` | ローカル開発環境構築手順 | ✅ サンプル |
| `03_Project_Structure.md` | ディレクトリ構造・コーディング規約 | ✅ サンプル |
| `04_Security_Design.md` | 認証・認可・暗号化・監査ログ | ✅ サンプル |
| `05_CICD_Design.md` | CI/CDパイプライン設計 | ✅ サンプル |
| `06_Tech_Stack_Common_Specs.md` | 技術スタック共通仕様 | ✅ サンプル |

---

## 技術スタック選択肢

このテンプレートは技術スタック非依存ですが、以下の組み合わせで実績があります。

### パターン1: Vue.js + Node.js + PostgreSQL

**フロントエンド**:
- Vue.js 3 + TypeScript
- Quasar Framework / Vuetify
- Pinia（状態管理）
- Vue Router 4
- Vite

**バックエンド**:
- Node.js 20.x+
- Fastify / Express + TypeScript
- Prisma（ORM）

**データベース**:
- PostgreSQL 15+

**インフラ**:
- AWS（ECS Fargate / EC2）
- Docker + Docker Compose

### パターン2: React + Node.js + PostgreSQL

**フロントエンド**:
- React 18+ + TypeScript
- Next.js / Vite
- Material-UI / Ant Design
- Redux Toolkit / Zustand

**バックエンド**:
- Node.js 20.x+
- Express / Fastify + TypeScript
- Prisma（ORM）

**データベース**:
- PostgreSQL 15+

**インフラ**:
- AWS（ECS Fargate / EC2）
- Vercel（フロントエンド）

### パターン3: Vue.js + Java + PostgreSQL

**フロントエンド**:
- Vue.js 3 + TypeScript
- Vuetify / Quasar Framework
- Pinia

**バックエンド**:
- Java 17+ + Spring Boot
- JPA / Hibernate（ORM）

**データベース**:
- PostgreSQL 15+

**インフラ**:
- AWS（ECS Fargate / EC2）
- Docker + Docker Compose

---

## 関連ドキュメント

- **プロジェクトガイドライン**: `../00_Project_Guidelines/`
- **業務プロセス・要件定義**: `../01_Business_Process/`
- **データベース設計**: `../03_Database_Design/`
- **画面設計**: `../04_Screen_Design/`
- **API設計**: `../05_API_Design/`
- **詳細設計**: `../06_Detailed_Design/`

---

**作成者**: [Your Team Name]
**最終更新**: 2026-02-04
