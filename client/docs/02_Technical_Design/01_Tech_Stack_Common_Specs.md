# 技術スタック・共通仕様（サンプル）

**作成日**: 2026-02-04
**バージョン**: v1.0.0
**目的**: プロジェクト全体で使用する技術スタック、共通コンポーネント、共通関数を一元管理
**ステータス**: サンプル

---

## 目次

1. [対応デバイス](#対応デバイス)
2. [フロントエンド技術スタック](#フロントエンド技術スタック)
3. [バックエンド技術スタック](#バックエンド技術スタック)
4. [データベース](#データベース)
5. [インフラ](#インフラ)
6. [帳票システム](#帳票システム)
7. [共通パッケージ](#共通パッケージ)
8. [共通コンポーネント](#共通コンポーネント)
9. [共通関数・ユーティリティ](#共通関数ユーティリティ)
10. [コーディング規約](#コーディング規約)
11. [更新履歴](#更新履歴)

---

## 対応デバイス

### クライアントデバイス構成

| デバイス | 機種・環境 | ブラウザ | 主な用途 |
|----------|-----------|---------|----------|
| **PC** | Windows 10/11 | Chrome（標準） | 管理業務、マスタ管理、レポート参照 |
| **タブレット** | iPad | Chrome（標準） | 入荷検品、出荷検品、在庫移動 |
| **ハンディターミナル** | デンソーウェーブ Xnavis（Android） | 内蔵ブラウザ | ピッキング、棚卸、バーコードスキャン |

### デバイス別機能対応

| 機能 | PC | タブレット | ハンディターミナル |
|------|:--:|:----------:|:-----------------:|
| ダッシュボード | ○ | ○ | - |
| ASN/ADN管理 | ○ | ○ | - |
| 入荷検品 | ○ | ○ | ○ |
| 在庫一覧・詳細 | ○ | ○ | △（簡易表示） |
| 在庫移動 | ○ | ○ | ○ |
| ピッキング | - | ○ | ○ |
| 出荷検品 | ○ | ○ | ○ |
| 棚卸 | - | ○ | ○ |
| マスタ管理 | ○ | - | - |
| システム設定 | ○ | - | - |

**凡例**: ○=対応、△=制限付き対応、-=非対応

### ハンディターミナル仕様（デンソーウェーブ Xnavis）

| 項目 | 仕様 |
|------|------|
| OS | Android |
| 画面サイズ | 対応（レスポンシブデザイン） |
| バーコード読取 | 1D/2Dバーコード対応 |
| 通信 | WiFi（倉庫内無線LAN） |
| 認証方式 | QRコード認証（ユーザーバッジスキャン） |
| オフライン対応 | Phase 2で検討 |

---

## フロントエンド技術スタック

### **フレームワーク・ライブラリ**

| 技術 | バージョン | 用途 |
|------|-----------|------|
| Vue.js | 3.x | フロントエンドフレームワーク |
| Quasar | 2.x | UIフレームワーク（Vue.js ベース） |
| Pinia | 2.x | 状態管理 |
| Vue Router | 4.x | ルーティング（Quasar統合） |
| Axios | 1.x | HTTPクライアント（Quasar統合） |
| date-fns | 2.x | 日付処理 |

### **Quasar設定**

| 設定項目 | 値 | 説明 |
|---------|-----|------|
| モード | SPA | シングルページアプリケーション |
| アイコンセット | Material Icons | デフォルトアイコン |
| プラグイン | Notify, Dialog, Loading | 通知、ダイアログ、ローディング |
| コンポーネント | すべて自動インポート | Tree-shaking対応 |

### **開発ツール**

| 技術 | バージョン | 用途 |
|------|-----------|------|
| Vite | 5.x | ビルドツール |
| TypeScript | 5.x | 型安全性 |
| ESLint | 8.x | コード品質チェック |
| Prettier | 3.x | コードフォーマット |

---

## バックエンド技術スタック

### **フレームワーク・ライブラリ**

| 技術 | バージョン | 用途 |
|------|-----------|------|
| Fastify | 4.x | バックエンドフレームワーク |
| Prisma | 5.x | ORM |
| JWT | - | 認証 |
| bcrypt | - | パスワードハッシュ化 |

### **開発ツール**

| 技術 | バージョン | 用途 |
|------|-----------|------|
| TypeScript | 5.x | 型安全性 |
| ESLint | 8.x | コード品質チェック |
| Prettier | 3.x | コードフォーマット |
| Jest | 29.x | テストフレームワーク |

---

## データベース

### **RDBMS**

| 技術 | バージョン | 用途 |
|------|-----------|------|
| PostgreSQL | 17.x | メインデータベース |

### **接続プール**

| 設定項目 | 値 | 説明 |
|---------|-----|------|
| 最大接続数 | 20 | 同時接続の最大数 |
| アイドルタイムアウト | 30秒 | アイドル接続のタイムアウト |

---

## インフラ

### **クラウドプラットフォーム**

| 技術 | 用途 |
|------|------|
| AWS | クラウドインフラ |

### **主要サービス**

| サービス | 用途 |
|---------|------|
| EC2 | アプリケーションサーバー |
| RDS (PostgreSQL) | データベース |
| S3 | ファイルストレージ |
| CloudFront | CDN |
| Route 53 | DNS |

**詳細**: `02_Technical_Design/02_AWS_Architecture.md` を参照

---

## 帳票システム

### 帳票作成・印刷ソリューション

| 項目 | 内容 |
|------|------|
| 製品名 | **Create!Form**（インフォテック株式会社） |
| 用途 | 帳票フォーマット作成、帳票印刷 |
| 出力形式 | PDF、プリンター直接出力 |

### 帳票一覧（Phase 1）

| 帳票名 | 用途 | 出力タイミング |
|--------|------|---------------|
| 入荷検品リスト | 入荷予定の検品用リスト | 入荷予定確定時 |
| ピッキングリスト | 出荷指示に基づくピッキング作業用 | 出荷指示確定時 |
| 出荷明細書 | 出荷内容の明細 | 出荷完了時 |
| 棚卸リスト | 棚卸対象ロケーション一覧 | 棚卸開始時 |
| 在庫一覧表 | 現在庫の一覧 | オンデマンド |

### 連携方式

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│  App API    │────▶│ Create!Form │────▶│  プリンター  │
│  (Backend)  │     │   Server    │     │             │
└─────────────┘     └─────────────┘     └─────────────┘
      │                   │
      │                   ▼
      │             ┌─────────────┐
      └────────────▶│  PDF出力    │
                    │  (S3保存)   │
                    └─────────────┘
```

### 技術的考慮事項

| 項目 | 対応方針 |
|------|----------|
| API連携 | Create!Form REST API経由でPDF生成 |
| テンプレート管理 | Create!Form Designerで作成、サーバー管理 |
| 印刷キュー | 非同期処理（バックエンドジョブ） |
| エラーハンドリング | 印刷失敗時のリトライ、通知機能 |

---

## 共通パッケージ

### **フロントエンド**

```json
{
  "dependencies": {
    "vue": "^3.4.0",
    "quasar": "^2.14.0",
    "@quasar/extras": "^1.16.0",
    "pinia": "^2.1.0",
    "axios": "^1.6.0",
    "date-fns": "^2.30.0"
  },
  "devDependencies": {
    "@quasar/vite-plugin": "^1.6.0",
    "vite": "^5.0.0",
    "typescript": "^5.3.0",
    "eslint": "^8.56.0",
    "prettier": "^3.1.0",
    "@vitejs/plugin-vue": "^5.0.0"
  }
}
```

**注**: Quasarは Vue Router と Axios を統合しているため、個別のインストールは不要

### **バックエンド**

```json
{
  "dependencies": {
    "fastify": "^4.25.0",
    "@prisma/client": "^5.8.0",
    "jsonwebtoken": "^9.0.0",
    "bcrypt": "^5.1.0"
  },
  "devDependencies": {
    "prisma": "^5.8.0",
    "typescript": "^5.3.0",
    "eslint": "^8.56.0",
    "prettier": "^3.1.0",
    "jest": "^29.7.0",
    "@types/node": "^20.10.0"
  }
}
```

---

## 共通コンポーネント

### **Quasar標準コンポーネント**

以下のQuasar標準コンポーネントをプロジェクト全体で使用します。

#### **レイアウト**

| コンポーネント | 用途 | 主要Props |
|--------------|------|----------|
| `q-layout` | ページレイアウト | `view: string` |
| `q-header` | ヘッダー | `elevated: boolean` |
| `q-drawer` | サイドバー | `v-model: boolean`, `side: string` |
| `q-page-container` | ページコンテナ | なし |
| `q-page` | ページ | `padding: boolean` |
| `q-toolbar` | ツールバー | なし |
| `q-toolbar-title` | ツールバータイトル | なし |

#### **テーブル**

| コンポーネント | 用途 | 主要Props |
|--------------|------|----------|
| `q-table` | データテーブル | `rows: any[]`, `columns: Column[]`, `row-key: string`, `pagination: object` |
| `q-th` | テーブルヘッダー | なし |
| `q-tr` | テーブル行 | なし |
| `q-td` | テーブルセル | なし |

#### **フォーム**

| コンポーネント | 用途 | 主要Props |
|--------------|------|----------|
| `q-form` | フォーム | `@submit: function` |
| `q-input` | テキスト入力 | `v-model: string`, `label: string`, `rules: Rule[]` |
| `q-select` | セレクトボックス | `v-model: any`, `options: Option[]`, `label: string` |
| `q-date` | 日付選択 | `v-model: string`, `mask: string` |
| `q-checkbox` | チェックボックス | `v-model: boolean`, `label: string` |
| `q-radio` | ラジオボタン | `v-model: any`, `val: any`, `label: string` |

#### **ボタン**

| コンポーネント | 用途 | 主要Props |
|--------------|------|----------|
| `q-btn` | ボタン | `label: string`, `color: string`, `icon: string`, `@click: function` |
| `q-btn-group` | ボタングループ | なし |

#### **ダイアログ・通知**

| コンポーネント | 用途 | 主要Props |
|--------------|------|----------|
| `q-dialog` | ダイアログ | `v-model: boolean` |
| `q-card` | カード | なし |
| `q-notify` | 通知（プラグイン） | `message: string`, `type: string`, `position: string` |

#### **その他**

| コンポーネント | 用途 | 主要Props |
|--------------|------|----------|
| `q-spinner` | ローディング | `size: string`, `color: string` |
| `q-icon` | アイコン | `name: string`, `size: string` |
| `q-badge` | バッジ | `label: string`, `color: string` |
| `q-chip` | チップ | `label: string`, `color: string` |

**参照**: [Quasar公式ドキュメント](https://quasar.dev/vue-components/)

---

### **プロジェクト固有コンポーネント**

Quasar標準コンポーネントをラップした、プロジェクト固有のコンポーネント。

| コンポーネント名 | パス | 用途 | 基底コンポーネント |
|----------------|------|------|--------------------|
| AppHeader | `components/layout/AppHeader.vue` | アプリケーションヘッダー | `q-header`, `q-toolbar` |
| AppSidebar | `components/layout/AppSidebar.vue` | アプリケーションサイドバー | `q-drawer` |
| ProjectProgressTable | `components/dashboard/ProjectProgressTable.vue` | 案件別進捗テーブル | `q-table` |

---

## 共通関数・ユーティリティ

### **日付処理**

| 関数名 | パス | 用途 | 引数 | 戻り値 |
|--------|------|------|------|--------|
| `formatDate()` | `utils/date.ts` | 日付フォーマット | `date: Date \| string`, `format: string` | `string` |
| `parseDate()` | `utils/date.ts` | 日付パース | `dateString: string` | `Date` |
| `isValidDate()` | `utils/date.ts` | 日付検証 | `dateString: string` | `boolean` |
| `addDays()` | `utils/date.ts` | 日付加算 | `date: Date`, `days: number` | `Date` |

### **バリデーション**

| 関数名 | パス | 用途 | 引数 | 戻り値 |
|--------|------|------|------|--------|
| `validateProjectCode()` | `utils/validation.ts` | 案件コード検証 | `code: string` | `boolean` |
| `validateEmail()` | `utils/validation.ts` | メールアドレス検証 | `email: string` | `boolean` |
| `validateRequired()` | `utils/validation.ts` | 必須チェック | `value: any` | `boolean` |
| `validateLength()` | `utils/validation.ts` | 文字数チェック | `value: string`, `min: number`, `max: number` | `boolean` |

### **フォーマット**

| 関数名 | パス | 用途 | 引数 | 戻り値 |
|--------|------|------|------|--------|
| `formatNumber()` | `utils/format.ts` | 数値フォーマット | `value: number`, `decimals: number` | `string` |
| `formatCurrency()` | `utils/format.ts` | 通貨フォーマット | `value: number` | `string` |
| `formatPercentage()` | `utils/format.ts` | パーセンテージフォーマット | `value: number` | `string` |

### **API**

| 関数名 | パス | 用途 | 引数 | 戻り値 |
|--------|------|------|------|--------|
| `apiClient()` | `utils/api.ts` | API クライアント | なし | `AxiosInstance` |
| `handleApiError()` | `utils/api.ts` | API エラーハンドリング | `error: any` | `void` |
| `getAuthToken()` | `utils/auth.ts` | 認証トークン取得 | なし | `string \| null` |
| `setAuthToken()` | `utils/auth.ts` | 認証トークン設定 | `token: string` | `void` |

---

## コーディング規約

### **命名規則**

#### **ファイル名**
- **コンポーネント**: PascalCase（例: `DataTable.vue`, `FormInput.vue`）
- **ユーティリティ**: kebab-case（例: `date.ts`, `validation.ts`）
- **ページ**: PascalCase（例: `Dashboard.vue`, `AsnCreate.vue`）

#### **変数名**
- **変数・関数**: camelCase（例: `projectCode`, `handleClick`）
- **定数**: UPPER_SNAKE_CASE（例: `API_BASE_URL`, `MAX_RETRY_COUNT`）
- **型・インターフェース**: PascalCase（例: `User`, `ProjectProgress`）

#### **コンポーネント名**
- **2単語以上**: `DataTable`, `FormInput`, `ProjectProgressTable`
- **単一単語は避ける**: ❌ `Table`, `Input`

### **ESLint設定**

```json
{
  "extends": [
    "eslint:recommended",
    "plugin:vue/vue3-recommended",
    "plugin:@typescript-eslint/recommended",
    "prettier"
  ],
  "rules": {
    "vue/multi-word-component-names": "error",
    "vue/no-unused-vars": "error",
    "@typescript-eslint/no-explicit-any": "warn",
    "@typescript-eslint/explicit-function-return-type": "off"
  }
}
```

### **Prettier設定**

```json
{
  "semi": true,
  "singleQuote": true,
  "tabWidth": 2,
  "trailingComma": "es5",
  "printWidth": 100,
  "arrowParens": "always"
}
```

### **コメント規約**

#### **関数コメント**
```typescript
/**
 * 案件コードを検証する
 * @param code - 検証する案件コード（YYMXX形式）
 * @returns 有効な場合true、無効な場合false
 */
function validateProjectCode(code: string): boolean {
  // 実装
}
```

#### **コンポーネントコメント**
```vue
<!--
  案件別進捗テーブル

  Props:
  - data: 案件別進捗データ
  - sortable: ソート可能かどうか

  Emits:
  - row-click: 行クリック時
-->
<template>
  <!-- 実装 -->
</template>
```

---

## 更新履歴

| 日付 | バージョン | 内容 |
|------|-----------|------|
| 2026-01-19 | v2.1.0 | デバイス構成（Xnavis）・帳票システム（Create!Form）情報を追加 |
| 2025-12-30 | v2.0.0 | UIフレームワークをQuasarに変更 |
| 2025-12-30 | v1.0.0 | 初版作成 |

---

**作成者**: [Your Team Name]
**最終更新**: 2026-02-04
