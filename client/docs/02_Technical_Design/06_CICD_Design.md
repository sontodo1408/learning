# CI/CD設計書（サンプル）

**バージョン**: v1.0.0
**最終更新**: 2026-02-04
**ステータス**: サンプル

---

## 概要

このドキュメントは、Webアプリケーション向けのCI/CD（継続的インテグレーション/継続的デリバリー）パイプラインのサンプル設計です。
プロジェクトの要件に応じて、適宜カスタマイズしてください。

### 設計方針

1. **自動化優先**: 手動作業を最小化し、ヒューマンエラーを防止
2. **品質ゲート**: 各段階で品質チェックを実施
3. **高速フィードバック**: 問題を早期に検出
4. **環境一貫性**: 全環境で同一のデプロイプロセス
5. **ロールバック対応**: 問題発生時の迅速な復旧

### CI/CDツール

| 用途 | ツール | 理由 |
|------|--------|------|
| CI/CD | GitHub Actions | GitHubとの統合、豊富なアクション |
| コンテナレジストリ | Amazon ECR | AWSとの統合、セキュリティ |
| デプロイ | AWS CDK | Infrastructure as Code |
| シークレット管理 | AWS Secrets Manager | セキュアな認証情報管理 |

---

## パイプライン概要

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           CI/CD Pipeline                                     │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  [Push/PR]                                                                   │
│      │                                                                       │
│      ▼                                                                       │
│  ┌────────────────────────────────────────────────────────────────────┐     │
│  │                        CI Pipeline                                  │     │
│  │  ┌─────────┐  ┌─────────┐  ┌─────────┐  ┌─────────┐  ┌─────────┐  │     │
│  │  │  Lint   │→ │  Test   │→ │  Build  │→ │  Scan   │→ │  Push   │  │     │
│  │  └─────────┘  └─────────┘  └─────────┘  └─────────┘  └─────────┘  │     │
│  └────────────────────────────────────────────────────────────────────┘     │
│      │                                                                       │
│      ▼ (main branch only)                                                   │
│  ┌────────────────────────────────────────────────────────────────────┐     │
│  │                        CD Pipeline                                  │     │
│  │  ┌─────────┐  ┌─────────┐  ┌─────────┐  ┌─────────┐               │     │
│  │  │  Dev    │→ │ Staging │→ │ Approval│→ │  Prod   │               │     │
│  │  │ Deploy  │  │ Deploy  │  │         │  │ Deploy  │               │     │
│  │  └─────────┘  └─────────┘  └─────────┘  └─────────┘               │     │
│  └────────────────────────────────────────────────────────────────────┘     │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## CIパイプライン

### トリガー条件

| イベント | 対象ブランチ | 実行内容 |
|----------|--------------|----------|
| Push | feature/*, fix/* | Lint, Test, Build |
| Pull Request | develop, main | Lint, Test, Build, Scan |
| Merge | main | Full CI + CD |

### ステージ詳細

#### 1. Lint（静的解析）

```yaml
lint:
  runs-on: ubuntu-latest
  steps:
    - uses: actions/checkout@v4

    - name: Setup Node.js
      uses: actions/setup-node@v4
      with:
        node-version: '24'
        cache: 'npm'

    - name: Install dependencies
      run: npm ci

    - name: Run ESLint
      run: npm run lint

    - name: Run Prettier check
      run: npm run format:check

    - name: Run TypeScript check
      run: npm run type-check
```

**チェック項目**:
- ESLint: コード品質、ベストプラクティス
- Prettier: コードフォーマット
- TypeScript: 型チェック

#### 2. Test（テスト）

```yaml
test:
  runs-on: ubuntu-latest
  needs: lint
  services:
    postgres:
      image: postgres:17
      env:
        POSTGRES_USER: test
        POSTGRES_PASSWORD: test
        POSTGRES_DB: app_test
      ports:
        - 5432:5432
    redis:
      image: redis:7
      ports:
        - 6379:6379

  steps:
    - uses: actions/checkout@v4

    - name: Setup Node.js
      uses: actions/setup-node@v4
      with:
        node-version: '24'
        cache: 'npm'

    - name: Install dependencies
      run: npm ci


#### 3. Build（ビルド）

```yaml
build:
  runs-on: ubuntu-latest
  needs: test
  steps:
    - uses: actions/checkout@v4

    - name: Setup Node.js
      uses: actions/setup-node@v4
      with:
        node-version: '24'
        cache: 'npm'

    - name: Install dependencies
      run: npm ci

    - name: Build frontend
      run: npm run build:frontend
      working-directory: ./frontend

    - name: Build backend
      run: npm run build:backend
      working-directory: ./backend

    - name: Build Docker images
      run: |
        docker build -t app-frontend:${{ github.sha }} ./frontend
        docker build -t app-backend:${{ github.sha }} ./backend

    - name: Save Docker images
      run: |
        docker save app-frontend:${{ github.sha }} | gzip > frontend.tar.gz
        docker save app-backend:${{ github.sha }} | gzip > backend.tar.gz

    - name: Upload artifacts
      uses: actions/upload-artifact@v4
      with:
        name: docker-images
        path: |
          frontend.tar.gz
          backend.tar.gz
```

#### 4. Scan（セキュリティスキャン）

```yaml
scan:
  runs-on: ubuntu-latest
  needs: build
  steps:
    - uses: actions/checkout@v4

    - name: Download artifacts
      uses: actions/download-artifact@v4
      with:
        name: docker-images

    - name: Load Docker images
      run: |
        docker load < frontend.tar.gz
        docker load < backend.tar.gz

    - name: Run Trivy vulnerability scanner (Frontend)
      uses: aquasecurity/trivy-action@master
      with:
        image-ref: app-frontend:${{ github.sha }}
        format: 'sarif'
        output: 'trivy-frontend.sarif'
        severity: 'CRITICAL,HIGH'

    - name: Run Trivy vulnerability scanner (Backend)
      uses: aquasecurity/trivy-action@master
      with:
        image-ref: app-backend:${{ github.sha }}
        format: 'sarif'
        output: 'trivy-backend.sarif'
        severity: 'CRITICAL,HIGH'

    - name: Upload Trivy scan results
      uses: github/codeql-action/upload-sarif@v3
      with:
        sarif_file: 'trivy-frontend.sarif'

    - name: Run npm audit
      run: npm audit --audit-level=high
```

**スキャン項目**:
- Trivy: コンテナイメージの脆弱性
- npm audit: 依存パッケージの脆弱性
- CRITICAL/HIGH脆弱性でビルド失敗

#### 5. Push（イメージプッシュ）

```yaml
push:
  runs-on: ubuntu-latest
  needs: scan
  if: github.ref == 'refs/heads/main'
  steps:
    - name: Configure AWS credentials
      uses: aws-actions/configure-aws-credentials@v4
      with:
        role-to-assume: ${{ secrets.AWS_ROLE_ARN }}
        aws-region: ap-northeast-1

    - name: Login to Amazon ECR
      id: login-ecr
      uses: aws-actions/amazon-ecr-login@v2

    - name: Download artifacts
      uses: actions/download-artifact@v4
      with:
        name: docker-images

    - name: Load and push Docker images
      env:
        ECR_REGISTRY: ${{ steps.login-ecr.outputs.registry }}
      run: |
        docker load < frontend.tar.gz
        docker load < backend.tar.gz

        docker tag app-frontend:${{ github.sha }} $ECR_REGISTRY/app-frontend:${{ github.sha }}
        docker tag app-frontend:${{ github.sha }} $ECR_REGISTRY/app-frontend:latest
        docker tag app-backend:${{ github.sha }} $ECR_REGISTRY/app-backend:${{ github.sha }}
        docker tag app-backend:${{ github.sha }} $ECR_REGISTRY/app-backend:latest

        docker push $ECR_REGISTRY/app-frontend:${{ github.sha }}
        docker push $ECR_REGISTRY/app-frontend:latest
        docker push $ECR_REGISTRY/app-backend:${{ github.sha }}
        docker push $ECR_REGISTRY/app-backend:latest
```

---

## CDパイプライン

### 環境構成

| 環境 | 用途 | デプロイ方式 | 承認 |
|------|------|--------------|------|
| Development | 開発・検証 | 自動 | 不要 |
| Staging | 受入テスト | 自動 | 不要 |
| Production | 本番 | 手動トリガー | 必要 |

### デプロイフロー

```yaml
deploy-dev:
  runs-on: ubuntu-latest
  needs: push
  environment: development
  steps:
    - uses: actions/checkout@v4

    - name: Configure AWS credentials
      uses: aws-actions/configure-aws-credentials@v4
      with:
        role-to-assume: ${{ secrets.AWS_ROLE_ARN_DEV }}
        aws-region: ap-northeast-1

    - name: Deploy to ECS
      run: |
        aws ecs update-service \
          --cluster app-dev-cluster \
          --service app-dev-service \
          --force-new-deployment

    - name: Wait for deployment
      run: |
        aws ecs wait services-stable \
          --cluster app-dev-cluster \
          --services app-dev-service

deploy-staging:
  runs-on: ubuntu-latest
  needs: deploy-dev
  environment: staging
  steps:
    - uses: actions/checkout@v4

    - name: Configure AWS credentials
      uses: aws-actions/configure-aws-credentials@v4
      with:
        role-to-assume: ${{ secrets.AWS_ROLE_ARN_STG }}
        aws-region: ap-northeast-1

    - name: Deploy to ECS
      run: |
        aws ecs update-service \
          --cluster app-stg-cluster \
          --service app-stg-service \
          --force-new-deployment

    - name: Run E2E tests
      run: npm run test:e2e
      env:
        BASE_URL: https://staging.app.example.com

deploy-production:
  runs-on: ubuntu-latest
  needs: deploy-staging
  environment: production
  steps:
    - uses: actions/checkout@v4

    - name: Configure AWS credentials
      uses: aws-actions/configure-aws-credentials@v4
      with:
        role-to-assume: ${{ secrets.AWS_ROLE_ARN_PROD }}
        aws-region: ap-northeast-1

    - name: Deploy to ECS (Blue/Green)
      run: |
        aws deploy create-deployment \
          --application-name app-prod \
          --deployment-group-name app-prod-dg \
          --revision revisionType=AppSpecContent,appSpecContent="{...}"

    - name: Wait for deployment
      run: |
        aws deploy wait deployment-successful \
          --deployment-id ${{ steps.deploy.outputs.deployment-id }}
```

---

## デプロイ戦略

### Blue/Greenデプロイ（本番環境）

```
┌─────────────────────────────────────────────────────────────────┐
│                    Blue/Green Deployment                         │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  Step 1: Initial State                                          │
│  ┌─────────────┐                                                │
│  │    ALB      │──────────────▶ Blue (v1.0) [Active]           │
│  └─────────────┘                                                │
│                                                                  │
│  Step 2: Deploy New Version                                     │
│  ┌─────────────┐                                                │
│  │    ALB      │──────────────▶ Blue (v1.0) [Active]           │
│  └─────────────┘               Green (v1.1) [Standby]          │
│                                                                  │
│  Step 3: Switch Traffic                                         │
│  ┌─────────────┐                                                │
│  │    ALB      │──────────────▶ Green (v1.1) [Active]          │
│  └─────────────┘               Blue (v1.0) [Standby]           │
│                                                                  │
│  Step 4: Cleanup (after validation)                             │
│  ┌─────────────┐                                                │
│  │    ALB      │──────────────▶ Green (v1.1) [Active]          │
│  └─────────────┘               Blue (terminated)               │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### ロールバック手順

1. **自動ロールバック**: ヘルスチェック失敗時
2. **手動ロールバック**: 問題検出時

```bash
# 手動ロールバック（前バージョンに切り替え）
aws deploy stop-deployment --deployment-id <deployment-id>

# または前のタスク定義に戻す
aws ecs update-service \
  --cluster app-prod-cluster \
  --service app-prod-service \
  --task-definition app-backend:<previous-revision>
```

---

## 品質ゲート

### 各ステージの品質基準

| ステージ | 基準 | 失敗時の動作 |
|----------|------|--------------|
| Lint | エラー0件 | ビルド停止 |
| Test | カバレッジ80%以上、全テスト成功 | ビルド停止 |
| Scan | CRITICAL/HIGH脆弱性0件 | ビルド停止 |
| Deploy Dev | ヘルスチェック成功 | ロールバック |
| Deploy Staging | E2Eテスト成功 | ロールバック |
| Deploy Prod | ヘルスチェック成功 | 自動ロールバック |

### コードカバレッジ設定

```yaml
# codecov.yml
coverage:
  status:
    project:
      default:
        target: 80%
        threshold: 2%
    patch:
      default:
        target: 80%
```

---

## 環境変数・シークレット管理

### GitHub Secrets

| シークレット名 | 用途 | 環境 |
|----------------|------|------|
| AWS_ROLE_ARN_DEV | AWS IAMロール | Development |
| AWS_ROLE_ARN_STG | AWS IAMロール | Staging |
| AWS_ROLE_ARN_PROD | AWS IAMロール | Production |
| CODECOV_TOKEN | カバレッジレポート | 全環境 |

### AWS Secrets Manager

| シークレット名 | 内容 |
|----------------|------|
| app/dev/database | DB接続情報 |
| app/dev/redis | Redis接続情報 |
| app/dev/jwt | JWT秘密鍵 |
| app/stg/database | DB接続情報 |
| app/stg/redis | Redis接続情報 |
| app/stg/jwt | JWT秘密鍵 |
| app/prod/database | DB接続情報 |
| app/prod/redis | Redis接続情報 |
| app/prod/jwt | JWT秘密鍵 |

---

## 監視・通知

### デプロイ通知

```yaml
notify:
  runs-on: ubuntu-latest
  needs: [deploy-production]
  if: always()
  steps:
    - name: Notify Slack
      uses: slackapi/slack-github-action@v1
      with:
        payload: |
          {
            "text": "Deployment ${{ job.status }}",
            "blocks": [
              {
                "type": "section",
                "text": {
                  "type": "mrkdwn",
                  "text": "*App Deployment*\n*Status:* ${{ job.status }}\n*Version:* ${{ github.sha }}\n*Environment:* Production"
                }
              }
            ]
          }
      env:
        SLACK_WEBHOOK_URL: ${{ secrets.SLACK_WEBHOOK_URL }}
```

### デプロイメトリクス

| メトリクス | 目標 |
|------------|------|
| デプロイ頻度 | 週1回以上 |
| リードタイム | 1日以内 |
| 変更失敗率 | 5%以下 |
| MTTR | 1時間以内 |

---

## 関連ドキュメント

- [AWSアーキテクチャ](./02_AWS_Architecture.md)
- [開発環境セットアップ](./03_Development_Environment_Setup.md)
- [プロジェクト構成](./04_Project_Structure.md)
- [セキュリティ設計書](./05_Security_Design.md)
- [技術スタック共通仕様](./07_Tech_Stack_Common_Specs.md)
