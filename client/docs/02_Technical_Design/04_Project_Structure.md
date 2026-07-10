# プロジェクト構造設計書（サンプル）

**バージョン**: v1.0.0
**最終更新**: 2026-02-04
**ステータス**: サンプル

---

## 概要

このドキュメントは、Webアプリケーションのプロジェクト構造、コーディング規約、アーキテクチャパターンのサンプルです。
プロジェクトの技術スタックに応じて、適宜カスタマイズしてください。

### 設計方針
1. **保守性**: 明確な責務分離、モジュール化
2. **拡張性**: 新機能追加が容易な構造
3. **可読性**: 一貫した命名規則、適切なコメント
4. **テスタビリティ**: テストしやすい設計
5. **型安全性**: TypeScriptの型システムを最大限活用

---

## プロジェクト全体構造

```
project-root/
├── backend/                    # バックエンド（Node.js + Express/Fastify + TypeScript）
├── frontend/                   # フロントエンド（Vue.js/React + TypeScript）
├── shared/                     # 共通ライブラリ（型定義、ユーティリティ）
├── docker/                     # Docker関連ファイル
├── docs/                       # 技術ドキュメント
├── scripts/                    # ビルド・デプロイスクリプト
├── .github/                    # GitHub Actions（CI/CD）
├── docker-compose.yml          # ローカル開発環境
├── .gitignore
├── .prettierrc
├── .eslintrc.json
├── package.json                # ルートpackage.json（ワークスペース管理）
└── README.md
```

---

## バックエンド構造

### ディレクトリ構造

```
backend/
├── src/
│   ├── index.ts                      # エントリーポイント
│   ├── app.ts                        # Fastifyアプリケーション設定
│   ├── server.ts                     # サーバー起動
│   │
│   ├── config/                       # 設定ファイル
│   │   ├── index.ts                  # 設定エクスポート
│   │   ├── database.ts               # データベース設定
│   │   ├── jwt.ts                    # JWT設定
│   │   └── s3.ts                     # S3設定
│   │
│   ├── modules/                      # 機能モジュール（ドメイン駆動）
│   │   ├── auth/                     # 認証・認可（SCR-27）
│   │   │   ├── auth.controller.ts
│   │   │   ├── auth.service.ts
│   │   │   ├── auth.schema.ts        # Zodスキーマ
│   │   │   ├── auth.types.ts         # 型定義
│   │   │   └── auth.test.ts
│   │   │
│   │   ├── dashboard/                # ダッシュボード（SCR-01）
│   │   │   ├── dashboard.controller.ts
│   │   │   └── dashboard.service.ts
│   │   │
│   │   ├── asn/                      # ASN/ADN管理（SCR-02〜06）
│   │   │   ├── asn.controller.ts
│   │   │   ├── asn.service.ts
│   │   │   ├── asn.repository.ts
│   │   │   ├── asn.schema.ts
│   │   │   ├── asn.types.ts
│   │   │   ├── csv-import.service.ts # CSV取り込み
│   │   │   └── asn.test.ts
│   │   │
│   │   ├── inbound/                  # 入庫管理（SCR-07〜11）
│   │   │   ├── inbound.controller.ts
│   │   │   ├── inbound.service.ts
│   │   │   ├── inbound.repository.ts
│   │   │   ├── pallet.service.ts     # パレットID発行
│   │   │   └── location.service.ts   # ロケーション割当
│   │   │
│   │   ├── inventory/                # 在庫管理（SCR-12〜15）
│   │   │   ├── inventory.controller.ts
│   │   │   ├── inventory.service.ts
│   │   │   ├── inventory.repository.ts
│   │   │   ├── discharge.service.ts  # 払出依頼
│   │   │   └── operation.service.ts  # 移動・分割
│   │   │
│   │   ├── outbound/                 # 出庫管理（SCR-16〜21）
│   │   │   ├── outbound.controller.ts
│   │   │   ├── outbound.service.ts
│   │   │   ├── outbound.repository.ts
│   │   │   ├── allocation.service.ts # 引当処理（SCR-19）
│   │   │   ├── picking.service.ts    # ピッキング（SCR-20）
│   │   │   └── inspection.service.ts # 出庫検品（SCR-21）
│   │   │
│   │   ├── vanning/                  # バンニング管理（SCR-22〜23）
│   │   │   ├── vanning.controller.ts
│   │   │   ├── vanning.service.ts
│   │   │   └── vanning.repository.ts
│   │   │
│   │   ├── report/                   # レポート（SCR-24）
│   │   │   ├── report.controller.ts
│   │   │   ├── report.service.ts
│   │   │   └── templates/            # レポートテンプレート
│   │   │
│   │   └── system/                   # システム管理（SCR-25〜26）
│   │       ├── user/                 # ユーザー管理（SCR-25）
│   │       │   ├── user.controller.ts
│   │       │   ├── user.service.ts
│   │       │   └── user.repository.ts
│   │       │
│   │       └── log/                  # 操作ログ（SCR-26）
│   │           ├── log.controller.ts
│   │           ├── log.service.ts
│   │           └── log.repository.ts
│   │
│   ├── shared/                       # 共通機能
│   │   ├── database/                 # データベース
│   │   │   ├── prisma.ts             # Prismaクライアント
│   │   │   └── transaction.ts        # トランザクション管理
│   │   │
│   │   ├── middleware/               # ミドルウェア
│   │   │   ├── auth.middleware.ts    # 認証ミドルウェア
│   │   │   ├── error.middleware.ts   # エラーハンドリング
│   │   │   ├── logger.middleware.ts  # ロギング
│   │   │   └── validation.middleware.ts # バリデーション
│   │   │
│   │   ├── utils/                    # ユーティリティ
│   │   │   ├── logger.ts             # ロガー
│   │   │   ├── date.ts               # 日付処理
│   │   │   ├── crypto.ts             # 暗号化
│   │   │   └── file.ts               # ファイル処理
│   │   │
│   │   ├── errors/                   # カスタムエラー
│   │   │   ├── base.error.ts
│   │   │   ├── validation.error.ts
│   │   │   ├── auth.error.ts
│   │   │   └── database.error.ts
│   │   │
│   │   └── constants/                # 定数
│   │       ├── status.ts             # ステータス定数
│   │       ├── roles.ts              # ロール定数
│   │       └── messages.ts           # メッセージ定数
│   │
│   └── types/                        # グローバル型定義
│       ├── index.ts
│       ├── fastify.d.ts              # Fastify型拡張
│       └── environment.d.ts          # 環境変数型定義
│
├── prisma/
│   ├── schema.prisma                 # Prismaスキーマ
│   ├── migrations/                   # マイグレーション
│   └── seed.ts                       # シードデータ
│
├── tests/
│   ├── unit/                         # 単体テスト
│   ├── integration/                  # 統合テスト
│   └── fixtures/                     # テストデータ
│
├── .env.example
├── .eslintrc.json
├── .prettierrc
├── tsconfig.json
├── jest.config.js
├── package.json
└── README.md
```

---

## フロントエンド構造

### ディレクトリ構造

```
frontend/
├── src/
│   ├── main.ts                       # エントリーポイント
│   ├── App.vue                       # ルートコンポーネント
│   │
│   ├── router/                       # ルーティング
│   │   ├── index.ts                  # ルーター設定
│   │   ├── routes.ts                 # ルート定義
│   │   └── guards.ts                 # ナビゲーションガード
│   │
│   ├── stores/                       # Pinia ストア
│   │   ├── index.ts
│   │   ├── auth.ts                   # 認証ストア
│   │   ├── asn.ts                    # ASN/ADNストア
│   │   ├── inbound.ts                # 入庫ストア
│   │   ├── inventory.ts              # 在庫ストア
│   │   ├── outbound.ts               # 出庫ストア
│   │   ├── vanning.ts                # バンニングストア
│   │   └── ui.ts                     # UI状態ストア
│   │
│   ├── views/                        # ページコンポーネント（28画面）
│   │   ├── Login.vue                 # SCR-27: ログイン
│   │   ├── Dashboard.vue             # SCR-01: ダッシュボード
│   │   │
│   │   ├── asn/                      # ASN/ADN管理（5画面）
│   │   │   ├── ShipmentNoticeList.vue # SCR-02: 入出庫予定一覧
│   │   │   ├── AsnCreate.vue         # SCR-03: 入庫予定作成
│   │   │   ├── AsnEdit.vue           # SCR-04: 入庫予定編集
│   │   │   ├── AdnCreate.vue         # SCR-05: 出庫予定作成
│   │   │   └── AdnEdit.vue           # SCR-06: 出庫予定編集
│   │   │
│   │   ├── inbound/                  # 入庫管理（5画面）
│   │   │   ├── InboundList.vue       # SCR-07: 入庫管理一覧
│   │   │   ├── InboundDetail.vue     # SCR-08: 入庫管理詳細
│   │   │   ├── InboundRequest.vue    # SCR-09: 入庫依頼作成
│   │   │   ├── InboundWork.vue       # SCR-10: 入庫実績登録
│   │   │   └── LocationAssign.vue    # SCR-11: ロケーション割当
│   │   │
│   │   ├── inventory/                # 在庫管理（4画面）
│   │   │   ├── InventoryList.vue     # SCR-12: 在庫一覧
│   │   │   ├── InventoryDetail.vue   # SCR-13: 在庫詳細
│   │   │   ├── DischargeRequest.vue  # SCR-14: 在庫払出依頼
│   │   │   └── InventoryOperation.vue # SCR-15: 在庫移動・分割
│   │   │
│   │   ├── outbound/                 # 出庫管理（6画面）
│   │   │   ├── OutboundList.vue      # SCR-16: 出庫・バンニング依頼一覧
│   │   │   ├── OutboundDetail.vue    # SCR-17: 出庫依頼詳細
│   │   │   ├── OutboundRequest.vue   # SCR-18: 出庫依頼作成
│   │   │   ├── Allocation.vue        # SCR-19: 引当処理 ★NEW
│   │   │   ├── Picking.vue           # SCR-20: ピッキング作業
│   │   │   └── OutboundInspection.vue # SCR-21: 出庫検品
│   │   │
│   │   ├── vanning/                  # バンニング管理（2画面）
│   │   │   ├── VanningDetail.vue     # SCR-22: バンニング依頼詳細
│   │   │   └── VanningWork.vue       # SCR-23: バンニング・封印作業
│   │   │
│   │   ├── report/                   # レポート（1画面）
│   │   │   └── ReportList.vue        # SCR-24: 実績レポート出力
│   │   │
│   │   ├── system/                   # システム管理（2画面）
│   │   │   ├── UserManagement.vue    # SCR-25: ユーザー管理
│   │   │   └── OperationLogs.vue     # SCR-26: 操作ログ管理
│   │   │
│   │   └── work/                     # 共通作業（1画面）
│   │       └── QrScan.vue            # SCR-28: 統一QRスキャン
│   │
│   ├── components/                   # 再利用可能コンポーネント
│   │   ├── common/                   # 共通コンポーネント
│   │   │   ├── AppHeader.vue
│   │   │   ├── AppSidebar.vue
│   │   │   ├── AppFooter.vue
│   │   │   ├── LoadingSpinner.vue
│   │   │   └── ErrorMessage.vue
│   │   │
│   │   ├── form/                     # フォームコンポーネント
│   │   │   ├── FormInput.vue
│   │   │   ├── FormSelect.vue
│   │   │   ├── FormDatePicker.vue
│   │   │   └── FormFileUpload.vue
│   │   │
│   │   ├── table/                    # テーブルコンポーネント
│   │   │   ├── DataTable.vue
│   │   │   ├── TablePagination.vue
│   │   │   └── TableFilter.vue
│   │   │
│   │   └── business/                 # 業務固有コンポーネント
│   │       ├── QrScanner.vue         # QRスキャナー
│   │       ├── PalletCard.vue        # パレットカード
│   │       └── StatusBadge.vue       # ステータスバッジ
│   │
│   ├── composables/                  # Composition API
│   │   ├── useAuth.ts                # 認証
│   │   ├── useApi.ts                 # API通信
│   │   ├── useNotification.ts        # 通知
│   │   ├── useQrScanner.ts           # QRスキャン
│   │   └── useExport.ts              # エクスポート
│   │
│   ├── api/                          # API クライアント
│   │   ├── client.ts                 # Axiosクライアント
│   │   ├── auth.api.ts               # 認証API
│   │   ├── asn.api.ts                # ASN/ADN API
│   │   ├── inbound.api.ts            # 入庫API
│   │   ├── inventory.api.ts          # 在庫API
│   │   ├── outbound.api.ts           # 出庫API
│   │   ├── vanning.api.ts            # バンニングAPI
│   │   └── report.api.ts             # レポートAPI
│   │
│   ├── types/                        # 型定義
│   │   ├── index.ts
│   │   ├── api.types.ts              # API型
│   │   ├── models.types.ts           # モデル型
│   │   └── ui.types.ts               # UI型
│   │
│   ├── utils/                        # ユーティリティ
│   │   ├── date.ts                   # 日付処理
│   │   ├── format.ts                 # フォーマット
│   │   ├── validation.ts             # バリデーション
│   │   └── export.ts                 # エクスポート
│   │
│   ├── constants/                    # 定数
│   │   ├── status.ts
│   │   ├── routes.ts
│   │   └── messages.ts
│   │
│   ├── assets/                       # 静的アセット
│   │   ├── images/
│   │   ├── icons/
│   │   └── styles/
│   │       ├── main.scss
│   │       ├── variables.scss
│   │       └── mixins.scss
│   │
│   └── plugins/                      # Vueプラグイン
│       ├── quasar.ts
│       └── i18n.ts
│
├── public/                           # 公開ファイル
│   ├── favicon.ico
│   └── robots.txt
│
├── tests/
│   ├── unit/                         # 単体テスト
│   └── fixtures/                     # テストデータ
│
├── .env.example
├── .eslintrc.json
├── .prettierrc
├── tsconfig.json
├── vite.config.ts
├── vitest.config.ts
├── package.json
└── README.md
```

---

## 共通ライブラリ構造

```
shared/
├── src/
│   ├── types/                        # 共通型定義
│   │   ├── api/                      # API型
│   │   │   ├── request.types.ts
│   │   │   ├── response.types.ts
│   │   │   └── error.types.ts
│   │   │
│   │   ├── models/                   # ドメインモデル型
│   │   │   ├── asn.types.ts
│   │   │   ├── inventory.types.ts
│   │   │   ├── pallet.types.ts
│   │   │   └── user.types.ts
│   │   │
│   │   └── enums/                    # 列挙型
│   │       ├── status.enum.ts
│   │       ├── role.enum.ts
│   │       └── storage-temp.enum.ts
│   │
│   ├── constants/                    # 共通定数
│   │   ├── status.ts
│   │   ├── validation.ts
│   │   └── messages.ts
│   │
│   └── utils/                        # 共通ユーティリティ
│       ├── date.ts
│       ├── format.ts
│       └── validation.ts
│
├── tsconfig.json
├── package.json
└── README.md
```

---

## コーディング規約

### TypeScript規約

#### 型定義

```typescript
// Good: 明示的な型定義
interface User {
  id: number;
  name: string;
  email: string;
  role: UserRole;
  createdAt: Date;
}

type UserRole = 'admin' | 'clerk' | 'worker';

// Good: 関数の戻り値型を明示
function getUser(id: number): Promise<User> {
  return prisma.user.findUnique({ where: { id } });
}

// Bad: any型の使用は禁止
// function processData(data: any): any { ... }

// Good: ジェネリクスを使用
function processData<T>(data: T): T {
  return data;
}
```

#### 命名規則

| 種類 | 規則 | 例 |
|------|------|-----|
| 変数・関数 | camelCase | `userName`, `getUserById()` |
| クラス・インターフェース | PascalCase | `User`, `IUserRepository` |
| 型エイリアス | PascalCase | `UserRole`, `ApiResponse` |
| 定数 | UPPER_SNAKE_CASE | `MAX_RETRY_COUNT`, `API_BASE_URL` |
| ファイル名（コンポーネント） | PascalCase | `UserList.vue`, `AppHeader.vue` |
| ファイル名（その他） | kebab-case | `user.service.ts`, `auth.middleware.ts` |

#### インポート順序

```typescript
// 1. Node.js組み込みモジュール
import { readFile } from 'fs/promises';

// 2. 外部ライブラリ
import { FastifyInstance } from 'fastify';
import { z } from 'zod';

// 3. 内部モジュール（絶対パス）
import { prisma } from '@/shared/database/prisma';
import { logger } from '@/shared/utils/logger';

// 4. 相対パス
import { UserService } from './user.service';
import type { User } from './user.types';
```

### Vue.js規約

#### Composition API

```vue
<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/auth';

interface Props {
  userId: number;
  showDetails?: boolean;
}

const props = withDefaults(defineProps<Props>(), {
  showDetails: false,
});

const emit = defineEmits<{
  (e: 'update', user: User): void;
  (e: 'delete', id: number): void;
}>();

const authStore = useAuthStore();
const router = useRouter();

const user = ref<User | null>(null);
const isLoading = ref(false);

const fullName = computed(() => {
  return user.value ? `${user.value.firstName} ${user.value.lastName}` : '';
});

async function loadUser() {
  isLoading.value = true;
  try {
    user.value = await fetchUser(props.userId);
  } catch (error) {
    console.error('Failed to load user', error);
  } finally {
    isLoading.value = false;
  }
}

onMounted(() => {
  loadUser();
});
</script>

<template>
  <div class="user-card">
    <q-spinner v-if="isLoading" />
    <div v-else-if="user">
      <h2>{{ fullName }}</h2>
      <p v-if="showDetails">{{ user.email }}</p>
    </div>
  </div>
</template>

<style scoped lang="scss">
.user-card {
  padding: 16px;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
}
</style>
```

### ファイル命名規則

#### バックエンド

| ファイル種類 | 命名規則 | 例 |
|------------|---------|-----|
| Controller | `*.controller.ts` | `user.controller.ts` |
| Service | `*.service.ts` | `user.service.ts` |
| Repository | `*.repository.ts` | `user.repository.ts` |
| Schema（Zod） | `*.schema.ts` | `user.schema.ts` |
| 型定義 | `*.types.ts` | `user.types.ts` |
| テスト | `*.test.ts` | `user.test.ts` |
| ミドルウェア | `*.middleware.ts` | `auth.middleware.ts` |

#### フロントエンド

| ファイル種類 | 命名規則 | 例 |
|------------|---------|-----|
| ページコンポーネント | `PascalCase.vue` | `UserList.vue` |
| 共通コンポーネント | `PascalCase.vue` | `AppHeader.vue` |
| Composable | `use*.ts` | `useAuth.ts` |
| Store | `*.ts` | `auth.ts`, `user.ts` |
| API | `*.api.ts` | `user.api.ts` |
| 型定義 | `*.types.ts` | `user.types.ts` |
| テスト | `*.spec.ts` | `UserList.spec.ts` |

---

## アーキテクチャパターン

### バックエンド: レイヤードアーキテクチャ

```
┌─────────────────────────────────────────┐
│         Controller Layer                │  ← HTTPリクエスト処理
│  - ルーティング                          │
│  - リクエスト/レスポンス変換              │
│  - バリデーション                        │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│         Service Layer                   │  ← ビジネスロジック
│  - ドメインロジック                      │
│  - トランザクション管理                  │
│  - 外部API呼び出し                       │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│         Repository Layer                │  ← データアクセス
│  - データベース操作                      │
│  - クエリ構築                            │
│  - データマッピング                      │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│         Database (Prisma)               │  ← データ永続化
└─────────────────────────────────────────┘
```

### フロントエンド: Composition API + Pinia

```
┌─────────────────────────────────────────┐
│         View (Component)                │  ← UI表示
│  - テンプレート                          │
│  - ユーザーインタラクション              │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│         Composable                      │  ← ロジック再利用
│  - 状態管理                              │
│  - 副作用処理                            │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│         Store (Pinia)                   │  ← グローバル状態
│  - 状態管理                              │
│  - アクション                            │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│         API Client                      │  ← API通信
│  - HTTPリクエスト                        │
│  - エラーハンドリング                    │
└─────────────────────────────────────────┘
```

---

## エラーハンドリング

### バックエンド カスタムエラークラス

```typescript
// shared/errors/base.error.ts
export class BaseError extends Error {
  constructor(
    public message: string,
    public statusCode: number = 500,
    public code?: string
  ) {
    super(message);
    this.name = this.constructor.name;
    Error.captureStackTrace(this, this.constructor);
  }
}

// shared/errors/validation.error.ts
export class ValidationError extends BaseError {
  constructor(message: string, public fields?: Record<string, string>) {
    super(message, 400, 'VALIDATION_ERROR');
  }
}

// shared/errors/auth.error.ts
export class UnauthorizedError extends BaseError {
  constructor(message: string = 'Unauthorized') {
    super(message, 401, 'UNAUTHORIZED');
  }
}

export class ForbiddenError extends BaseError {
  constructor(message: string = 'Forbidden') {
    super(message, 403, 'FORBIDDEN');
  }
}
```

---

## Git運用ルール

### ブランチ戦略（Git Flow）

```
main (本番環境)
  │
  ├─ develop (開発環境)
  │    │
  │    ├─ feature/user-management    # 機能開発
  │    ├─ feature/inventory-search
  │    │
  │    ├─ bugfix/fix-login-error     # バグ修正
  │    │
  │    └─ release/v1.0.0             # リリース準備
  │
  └─ hotfix/critical-bug-fix         # 緊急修正
```

### ブランチ命名規則

| ブランチタイプ | 命名規則 | 例 |
|--------------|---------|-----|
| 機能開発 | `feature/<機能名>` | `feature/asn-upload` |
| バグ修正 | `bugfix/<バグ内容>` | `bugfix/fix-date-format` |
| リリース | `release/v<バージョン>` | `release/v1.0.0` |
| 緊急修正 | `hotfix/<修正内容>` | `hotfix/security-patch` |

### コミットメッセージ規約（Conventional Commits）

| Type | 説明 | 例 |
|------|------|-----|
| feat | 新機能 | `feat(asn): ASNアップロード機能追加` |
| fix | バグ修正 | `fix(auth): ログイン時のトークン検証エラー修正` |
| docs | ドキュメント | `docs(readme): セットアップ手順を追加` |
| style | コードスタイル | `style(user): ESLintエラー修正` |
| refactor | リファクタリング | `refactor(inventory): 在庫引当ロジックを改善` |
| perf | パフォーマンス改善 | `perf(query): N+1問題を解決` |
| test | テスト追加・修正 | `test(user): ユーザー作成テストを追加` |
| chore | ビルド・ツール | `chore(deps): 依存関係を更新` |

---

## 関連ドキュメント

### 参照元
- **モックアップ**: `Documents/04_UI_UX_Design/mockups_v3/README.md`
- **Phase 1要件チェックリスト**: `Documents/00_Project_Guidelines/Phase1_Requirements_Checklist.md`

### 参照先
- **技術スタック共通仕様**: `Documents/02_Technical_Design/07_Tech_Stack_Common_Specs.md`
- **AWS構成設計書**: `Documents/02_Technical_Design/02_AWS構成設計書.md`
- **開発環境構築手順書**: `Documents/02_Technical_Design/03_開発環境構築手順書.md`
- **データベース設計**: `Documents/01_Database_Design/`
- **API設計書**: `Documents/05_API_Design/`

---

## 変更履歴

| バージョン | 日付 | 変更内容 |
|-----------|------|---------|
| v1.0.0 | 2026-02-04 | テンプレート化（汎用サンプル） |

---

**作成者**: [Your Team Name]
**最終更新**: 2026-02-04

