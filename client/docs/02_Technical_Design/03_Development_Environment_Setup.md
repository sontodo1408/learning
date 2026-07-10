# 開発環境構築手順書（サンプル）

**バージョン**: v1.0.0
**最終更新**: 2026-02-04
**対象OS**: Windows 10/11, macOS, Linux

---

## 概要

このドキュメントは、Webアプリケーションの開発環境を構築するための手順書のサンプルです。
プロジェクトの技術スタックに応じて、適宜カスタマイズしてください。

### 前提条件
- Git がインストールされていること
- インターネット接続が可能であること
- 管理者権限（一部インストール時に必要）

### 構築する環境（例）
- **フロントエンド**: Vue.js 3 / React + TypeScript
- **バックエンド**: Node.js + Express/Fastify + TypeScript
- **データベース**: PostgreSQL
- **コンテナ**: Docker + Docker Compose

---

## 構築手順の全体像

```
1. 必須ツールのインストール
   ├── Node.js (LTS版)
   ├── Docker Desktop
   ├── Git
   └── VSCode

2. プロジェクトのセットアップ
   ├── リポジトリクローン
   ├── 依存関係インストール
   └── 環境変数設定

3. データベースのセットアップ
   ├── Docker Composeでコンテナ起動
   ├── マイグレーション実行
   └── シードデータ投入

4. 開発サーバーの起動
   ├── バックエンドAPI起動
   └── フロントエンド開発サーバー起動

5. 開発ツールの設定
   ├── VSCode拡張機能
   ├── ESLint/Prettier
   └── Git Hooks
```

---

## 1. 必須ツールのインストール

### 1.1 Node.js (LTS版)

#### Windows
1. [Node.js公式サイト](https://nodejs.org/)から「LTS版」をダウンロード
2. インストーラーを実行
3. インストール確認:
   ```powershell
   node --version
   npm --version
   ```

#### macOS
```bash
# Homebrewを使用
brew install node

# 確認
node --version
npm --version
```

#### Linux (Ubuntu/Debian)
```bash
# NodeSourceリポジトリを追加（最新LTS版）
curl -fsSL https://deb.nodesource.com/setup_lts.x | sudo -E bash -

# インストール
sudo apt-get install -y nodejs

# 確認
node --version
npm --version
```

---

### 1.2 Docker Desktop

#### Windows
1. [Docker Desktop for Windows](https://www.docker.com/products/docker-desktop/)をダウンロード
2. インストーラーを実行
3. WSL 2バックエンドを有効化（推奨）
4. 再起動後、Docker Desktopを起動
5. 確認:
   ```powershell
   docker --version
   docker-compose --version
   ```

#### macOS
```bash
# Homebrewを使用
brew install --cask docker

# Docker Desktopを起動
open /Applications/Docker.app

# 確認
docker --version
docker-compose --version
```

#### Linux (Ubuntu/Debian)
```bash
# Docker Engineインストール
sudo apt-get update
sudo apt-get install -y docker.io docker-compose

# ユーザーをdockerグループに追加
sudo usermod -aG docker $USER

# 再ログイン後、確認
docker --version
docker-compose --version
```

---

### 1.3 Git

#### Windows
1. [Git for Windows](https://git-scm.com/download/win)をダウンロード
2. インストーラーを実行（デフォルト設定でOK）
3. 確認:
   ```powershell
   git --version
   ```

#### macOS
```bash
# Homebrewを使用
brew install git

# 確認
git --version
```

#### Linux (Ubuntu/Debian)
```bash
sudo apt-get install -y git

# 確認
git --version
```

---

### 1.4 Visual Studio Code

#### 全OS共通
1. [VSCode公式サイト](https://code.visualstudio.com/)からダウンロード
2. インストーラーを実行
3. 起動確認

---

## 2. プロジェクトのセットアップ

### 2.1 リポジトリクローン

```bash
# プロジェクトディレクトリに移動
cd ~/projects  # または任意のディレクトリ

# リポジトリクローン
git clone https://github.com/your-org/your-project.git
cd your-project

# ブランチ確認
git branch -a
```

---

### 2.2 依存関係インストール

#### バックエンド
```bash
cd backend

# 依存関係インストール
npm install

# TypeScriptコンパイル確認
npm run build

# 戻る
cd ..
```

#### フロントエンド
```bash
cd frontend

# 依存関係インストール
npm install

# ビルド確認
npm run build

# 戻る
cd ..
```

---

### 2.3 環境変数設定

#### バックエンド環境変数

```bash
cd backend

# .env.exampleをコピー
cp .env.example .env

# .envを編集
```

**`.env` ファイル内容**:
```bash
# アプリケーション設定
NODE_ENV=development
PORT=8080
HOST=0.0.0.0

# データベース設定
DATABASE_URL=postgresql://app_dev:app_dev_password@localhost:5432/app_development

# JWT設定
JWT_SECRET=your-super-secret-jwt-key-change-in-production
JWT_EXPIRES_IN=24h

# CORS設定
CORS_ORIGIN=http://localhost:9000

# ログ設定
LOG_LEVEL=debug

# S3設定（ローカル開発ではMinIOを使用）
S3_ENDPOINT=http://localhost:9001
S3_BUCKET=app-dev-attachments
S3_ACCESS_KEY=minioadmin
S3_SECRET_KEY=minioadmin
S3_REGION=ap-northeast-1

# メール設定（開発環境ではMailHogを使用）
SMTP_HOST=localhost
SMTP_PORT=1025
SMTP_USER=
SMTP_PASSWORD=
SMTP_FROM=noreply@app-dev.local
```

#### フロントエンド環境変数

```bash
cd ../frontend

# .env.exampleをコピー
cp .env.example .env

# .envを編集
```

**`.env` ファイル内容**:
```bash
# API設定
VITE_API_BASE_URL=http://localhost:8080/api
VITE_WS_URL=ws://localhost:8080

# アプリケーション設定
VITE_APP_TITLE=アプリケーション（開発環境）
VITE_APP_VERSION=1.0.0

# 機能フラグ
VITE_ENABLE_DEBUG=true
VITE_ENABLE_MOCK_DATA=false
```

---

## 3. Docker Composeでデータベース環境構築

### 3.1 Docker Compose設定ファイル

プロジェクトルートに `docker-compose.yml` を作成します。

**`docker-compose.yml`**:
```yaml
version: '3.8'

services:
  # PostgreSQL データベース
  postgres:
    image: postgres:17-alpine
    container_name: app-postgres
    environment:
      POSTGRES_USER: app_dev
      POSTGRES_PASSWORD: app_dev_password
      POSTGRES_DB: app_development
      TZ: Asia/Tokyo
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
      - ./docs/03_Database_Design/init.sql:/docker-entrypoint-initdb.d/01_init.sql
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U app_dev"]
      interval: 10s
      timeout: 5s
      retries: 5

  # pgAdmin（データベース管理ツール）
  pgadmin:
    image: dpage/pgadmin4:latest
    container_name: app-pgadmin
    environment:
      PGADMIN_DEFAULT_EMAIL: admin@app-dev.local
      PGADMIN_DEFAULT_PASSWORD: admin
      PGADMIN_CONFIG_SERVER_MODE: 'False'
    ports:
      - "5050:80"
    volumes:
      - pgadmin_data:/var/lib/pgadmin
    depends_on:
      - postgres

  # MinIO（S3互換オブジェクトストレージ）
  minio:
    image: minio/minio:latest
    container_name: app-minio
    command: server /data --console-address ":9001"
    environment:
      MINIO_ROOT_USER: minioadmin
      MINIO_ROOT_PASSWORD: minioadmin
    ports:
      - "9000:9000"  # API
      - "9001:9001"  # Console
    volumes:
      - minio_data:/data
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:9000/minio/health/live"]
      interval: 30s
      timeout: 20s
      retries: 3

  # MailHog（メールテスト用SMTPサーバー）
  mailhog:
    image: mailhog/mailhog:latest
    container_name: app-mailhog
    ports:
      - "1025:1025"  # SMTP
      - "8025:8025"  # Web UI
    healthcheck:
      test: ["CMD", "wget", "--quiet", "--tries=1", "--spider", "http://localhost:8025"]
      interval: 30s
      timeout: 10s
      retries: 3

volumes:
  postgres_data:
  pgadmin_data:
  minio_data:
```

---

### 3.2 コンテナ起動

```bash
# プロジェクトルートで実行
cd ~/projects/your-project

# コンテナをバックグラウンドで起動
docker-compose up -d

# ログ確認
docker-compose logs -f

# コンテナ状態確認
docker-compose ps
```

**期待される出力**:
```
NAME            IMAGE                    STATUS         PORTS
app-postgres    postgres:17-alpine       Up (healthy)   0.0.0.0:5432->5432/tcp
app-pgadmin     dpage/pgadmin4:latest    Up             0.0.0.0:5050->80/tcp
app-minio       minio/minio:latest       Up (healthy)   0.0.0.0:9000-9001->9000-9001/tcp
app-mailhog     mailhog/mailhog:latest   Up (healthy)   0.0.0.0:1025->1025/tcp, 0.0.0.0:8025->8025/tcp
```

---

### 3.3 データベース接続確認

#### psqlコマンドで接続
```bash
# PostgreSQLコンテナに接続
docker exec -it app-postgres psql -U app_dev -d app_development

# テーブル一覧確認
\dt

# 接続終了
\q
```

#### pgAdminで接続
1. ブラウザで `http://localhost:5050` を開く
2. ログイン:
   - Email: `admin@app-dev.local`
   - Password: `admin`
3. サーバー追加:
   - Name: `App Development`
   - Host: `postgres`（コンテナ名）
   - Port: `5432`
   - Username: `app_dev`
   - Password: `app_dev_password`

---

### 3.4 Prismaマイグレーション

#### Prismaスキーマ設定

**`backend/prisma/schema.prisma`**:
```prisma
generator client {
  provider = "prisma-client-js"
}

datasource db {
  provider = "postgresql"
  url      = env("DATABASE_URL")
}

// データベース設計書から自動生成
// または手動でモデル定義
```

#### マイグレーション実行

```bash
cd backend

# Prismaクライアント生成
npx prisma generate

# 既存DBからスキーマを取得（初回のみ）
npx prisma db pull

# マイグレーション作成
npx prisma migrate dev --name init

# Prisma Studioでデータ確認
npx prisma studio
```

**Prisma Studio**: `http://localhost:5555` で起動

---

## 4. 開発サーバーの起動

### 4.1 バックエンドAPI起動

```bash
cd backend

# 開発モードで起動（ホットリロード有効）
npm run dev
```

**期待される出力**:
```
Server listening on http://0.0.0.0:8080
Database connected: app_development
Health check: http://localhost:8080/health
```

#### APIエンドポイント確認

```bash
# ヘルスチェック
curl http://localhost:8080/health

# レスポンス例
{
  "status": "ok",
  "timestamp": "2026-01-19T12:00:00.000Z",
  "database": "connected"
}
```

---

### 4.2 フロントエンド開発サーバー起動

```bash
# 新しいターミナルを開く
cd frontend

# 開発サーバー起動
npm run dev
```

**期待される出力**:
```
  VITE v5.x.x  ready in 1234 ms

  Local:   http://localhost:9000/
  Network: http://192.168.1.100:9000/
  press h to show help
```

#### ブラウザで確認

1. ブラウザで `http://localhost:9000` を開く
2. ログイン画面が表示されることを確認
3. テストユーザーでログイン:
   - ユーザーコード: `admin`
   - パスワード: `admin123`（開発環境用）

---

## 5. VSCode拡張機能・開発ツール設定

### 5.1 推奨VSCode拡張機能

#### 必須拡張機能

| 拡張機能 | ID | 用途 |
|---------|-------|------|
| ESLint | `dbaeumer.vscode-eslint` | コード品質チェック |
| Prettier | `esbenp.prettier-vscode` | コードフォーマット |
| Vue Language Features (Volar) | `Vue.volar` | Vue.js開発 |
| TypeScript Vue Plugin (Volar) | `Vue.vscode-typescript-vue-plugin` | Vue + TypeScript |
| Prisma | `Prisma.prisma` | Prismaスキーマ編集 |

#### 推奨拡張機能

| 拡張機能 | ID | 用途 |
|---------|-------|------|
| GitLens | `eamodio.gitlens` | Git履歴可視化 |
| Docker | `ms-azuretools.vscode-docker` | Docker管理 |
| REST Client | `humao.rest-client` | API テスト |
| Error Lens | `usernamehw.errorlens` | エラー表示強化 |
| Path Intellisense | `christian-kohler.path-intellisense` | パス補完 |

#### 一括インストール

**`.vscode/extensions.json`** をプロジェクトルートに作成:
```json
{
  "recommendations": [
    "dbaeumer.vscode-eslint",
    "esbenp.prettier-vscode",
    "Vue.volar",
    "Vue.vscode-typescript-vue-plugin",
    "Prisma.prisma",
    "eamodio.gitlens",
    "ms-azuretools.vscode-docker",
    "humao.rest-client",
    "usernamehw.errorlens",
    "christian-kohler.path-intellisense"
  ]
}
```

VSCodeで開くと、推奨拡張機能のインストールを促されます。

---

### 5.2 VSCode設定

**`.vscode/settings.json`**:
```json
{
  "editor.formatOnSave": true,
  "editor.defaultFormatter": "esbenp.prettier-vscode",
  "editor.codeActionsOnSave": {
    "source.fixAll.eslint": true
  },
  "eslint.validate": [
    "javascript",
    "javascriptreact",
    "typescript",
    "typescriptreact",
    "vue"
  ],
  "typescript.tsdk": "node_modules/typescript/lib",
  "typescript.enablePromptUseWorkspaceTsdk": true,
  "[prisma]": {
    "editor.defaultFormatter": "Prisma.prisma"
  },
  "files.associations": {
    "*.css": "css",
    "*.vue": "vue"
  },
  "search.exclude": {
    "**/node_modules": true,
    "**/dist": true,
    "**/.git": true
  }
}
```

---

### 5.3 デバッグ設定

**`.vscode/launch.json`**:
```json
{
  "version": "0.2.0",
  "configurations": [
    {
      "name": "Backend: Debug",
      "type": "node",
      "request": "launch",
      "runtimeExecutable": "npm",
      "runtimeArgs": ["run", "dev"],
      "cwd": "${workspaceFolder}/backend",
      "console": "integratedTerminal",
      "internalConsoleOptions": "neverOpen",
      "skipFiles": ["<node_internals>/**"]
    },
    {
      "name": "Frontend: Debug (Chrome)",
      "type": "chrome",
      "request": "launch",
      "url": "http://localhost:9000",
      "webRoot": "${workspaceFolder}/frontend/src",
      "sourceMapPathOverrides": {
        "webpack:///src/*": "${webRoot}/*"
      }
    }
  ],
  "compounds": [
    {
      "name": "Full Stack: Debug",
      "configurations": ["Backend: Debug", "Frontend: Debug (Chrome)"]
    }
  ]
}
```

---

## 6. 開発ワークフロー

### 6.1 日常的な開発フロー

```bash
# 1. 最新コードを取得
git pull origin develop

# 2. 機能ブランチ作成
git checkout -b feature/your-feature-name

# 3. Dockerコンテナ起動（初回 or 停止していた場合）
docker-compose up -d

# 4. バックエンド起動（ターミナル1）
cd backend
npm run dev

# 5. フロントエンド起動（ターミナル2）
cd frontend
npm run dev

# 6. コード編集・テスト

# 7. コミット（自動的にlint-stagedが実行される）
git add .
git commit -m "feat: 新機能追加"

# 8. プッシュ
git push origin feature/your-feature-name

# 9. プルリクエスト作成
```

---

### 6.2 テスト実行

#### バックエンドテスト

```bash
cd backend

# 単体テスト
npm run test

# カバレッジ付き
npm run test:coverage

# E2Eテスト
npm run test:e2e

# 特定のテストファイルのみ
npm run test -- user.test.ts
```

#### フロントエンドテスト

```bash
cd frontend

# 単体テスト（Vitest）
npm run test:unit

# E2Eテスト（Cypress）
npm run test:e2e

# Cypressインタラクティブモード
npm run test:e2e:open
```

---

### 6.3 ビルド確認

#### バックエンド

```bash
cd backend

# TypeScriptコンパイル
npm run build

# ビルド成果物確認
ls -la dist/

# 本番モードで起動確認
NODE_ENV=production node dist/index.js
```

#### フロントエンド

```bash
cd frontend

# 本番ビルド
npm run build

# ビルド成果物確認
ls -la dist/

# ビルド結果をプレビュー
npm run preview
```

---

## 7. トラブルシューティング

### 7.1 Dockerコンテナが起動しない

#### ポート競合
```bash
# ポート使用状況確認
# Windows
netstat -ano | findstr :5432

# macOS/Linux
lsof -i :5432

# 競合プロセスを停止するか、docker-compose.ymlのポートを変更
```

#### コンテナ再起動
```bash
# 全コンテナ停止・削除
docker-compose down

# ボリュームも削除（データベースリセット）
docker-compose down -v

# 再起動
docker-compose up -d
```

---

### 7.2 npm installが失敗する

#### キャッシュクリア
```bash
# npmキャッシュクリア
npm cache clean --force

# node_modules削除
rm -rf node_modules package-lock.json

# 再インストール
npm install
```

---

### 7.3 Prismaマイグレーションエラー

#### データベース接続確認
```bash
# DATABASE_URL確認
echo $DATABASE_URL  # macOS/Linux
echo %DATABASE_URL%  # Windows

# PostgreSQL接続テスト
docker exec -it app-postgres psql -U app_dev -d app_development -c "SELECT version();"
```

#### マイグレーションリセット
```bash
cd backend

# マイグレーション履歴リセット
npx prisma migrate reset

# 再マイグレーション
npx prisma migrate dev
```

---

## 8. 構築完了チェックリスト

### 環境構築
- [ ] Node.js 24.x LTS インストール完了
- [ ] Docker Desktop インストール・起動完了
- [ ] Git インストール完了
- [ ] VSCode インストール完了

### プロジェクトセットアップ
- [ ] リポジトリクローン完了
- [ ] バックエンド依存関係インストール完了
- [ ] フロントエンド依存関係インストール完了
- [ ] 環境変数ファイル（.env）設定完了

### データベース
- [ ] Docker Compose起動完了
- [ ] PostgreSQL接続確認完了
- [ ] Prismaマイグレーション完了
- [ ] シードデータ投入完了

### 開発サーバー
- [ ] バックエンドAPI起動確認（http://localhost:8080/health）
- [ ] フロントエンド起動確認（http://localhost:9000）
- [ ] ログイン動作確認

### 開発ツール
- [ ] VSCode拡張機能インストール完了
- [ ] ESLint動作確認
- [ ] Prettier動作確認
- [ ] Git Hooks（Husky）動作確認

### 動作確認
- [ ] バックエンドテスト実行成功
- [ ] フロントエンドテスト実行成功
- [ ] ビルド成功確認

---

## 関連ドキュメント

### 参照元
- **技術スタック共通仕様**: `Documents/02_Technical_Design/07_Tech_Stack_Common_Specs.md`

### 参照先
- **プロジェクト構造設計書**: `Documents/02_Technical_Design/04_Project_Structure.md`
- **AWS構成設計書**: `Documents/02_Technical_Design/02_AWS構成設計書.md`
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

