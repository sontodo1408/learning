# セキュリティ設計書（サンプル）

**バージョン**: v1.0.0
**最終更新**: 2026-02-04
**ステータス**: サンプル

---

## 概要

このドキュメントは、Webアプリケーションのセキュリティ設計のサンプルです。
プロジェクトの要件に応じて、適宜カスタマイズしてください。

### 設計方針

1. **多層防御（Defense in Depth）**: 複数のセキュリティレイヤーで保護
2. **最小権限の原則（Principle of Least Privilege）**: 必要最小限の権限のみ付与
3. **ゼロトラスト（Zero Trust）**: すべてのアクセスを検証
4. **データ保護**: 保存時・転送時の暗号化
5. **監査証跡**: すべての操作を記録
6. **コンプライアンス**: 個人情報保護法等への準拠

### セキュリティ対策の考慮事項

#### IP制限（オプション）

必要に応じて、特定のIPアドレスからのアクセスのみを許可：

| 項目 | 設定例 |
|------|------|
| **実装レイヤー** | ALB（Application Load Balancer）レベル |
| **許可IP範囲** | 組織の固定IPアドレス |
| **拒否動作** | 許可IP以外からのアクセスは403 Forbiddenを返す |
| **管理方法** | AWS WAF IP Setで管理 |

**IP制限を実施する場合でも暗号化・マスキングを実施する理由**:
1. **多層防御**: VPN侵害、内部犯行、設定ミスへの対策
2. **法令遵守**: 個人情報保護法の「技術的安全管理措置」義務
3. **監査対応**: ログ内の個人情報保護
4. **将来拡張**: 外部アクセス対応時の準備

---

## セキュリティ要件

### 機密性（Confidentiality）

| 要件ID | 要件 | 対策 |
|--------|------|------|
| SEC-C-01 | ユーザー認証情報の保護 | パスワードハッシュ化（bcrypt）、JWT暗号化 |
| SEC-C-02 | 個人情報の保護 | データベース暗号化（AES-256） |
| SEC-C-03 | 通信の暗号化 | TLS 1.3、HTTPS強制 |
| SEC-C-04 | 機密情報の保管 | AWS Secrets Manager |
| SEC-C-05 | バックアップの暗号化 | RDS自動暗号化、S3暗号化 |

### 完全性（Integrity）

| 要件ID | 要件 | 対策 |
|--------|------|------|
| SEC-I-01 | データ改ざん防止 | トランザクション管理、楽観的ロック |
| SEC-I-02 | 入力検証 | Zodスキーマバリデーション |
| SEC-I-03 | SQLインジェクション対策 | Prisma ORM（パラメータ化クエリ） |
| SEC-I-04 | XSS対策 | Vue.jsエスケープ、CSPヘッダー |
| SEC-I-05 | CSRF対策 | CSRFトークン、SameSite Cookie |

### 可用性（Availability）

| 要件ID | 要件 | 対策 |
|--------|------|------|
| SEC-A-01 | DDoS攻撃対策 | CloudFront、AWS Shield Standard |
| SEC-A-02 | レート制限 | Fastify rate-limit |
| SEC-A-03 | バックアップ | RDS自動バックアップ（7日保持） |
| SEC-A-04 | 障害復旧 | Multi-AZ構成、自動フェイルオーバー |
| SEC-A-05 | 監視・アラート | CloudWatch、異常検知 |

### 説明責任（Accountability）

| 要件ID | 要件 | 対策 |
|--------|------|------|
| SEC-AC-01 | 操作ログ記録 | 全操作の監査ログ |
| SEC-AC-02 | アクセスログ | ALBアクセスログ、CloudTrail |
| SEC-AC-03 | ログ保管 | 7年間保管（法令準拠） |
| SEC-AC-04 | ログ改ざん防止 | S3 Object Lock、CloudWatch Logs |
| SEC-AC-05 | 監査証跡 | 変更履歴テーブル（created_by, updated_by） |

---

## 認証・認可

### 認証方式

#### 1. パスワード認証 + QRコード認証（デュアル認証）

本システムは2つの認証方式をサポートします：

**A. パスワード認証（PC・タブレット）**
- ユーザーコード + パスワード入力
- JWT（JSON Web Token）発行

**B. QRコード認証（ハンディターミナル・タブレット）**
- ユーザー固有のQRコードをスキャン（ユーザーバッジ）
- QRコード内にユーザーコードを埋め込み（暗号化）
- スキャンのみで認証完了（PIN入力不要）
- JWT発行

**対応デバイス**:
| デバイス | 機種 | 認証方式 |
|----------|------|----------|
| ハンディターミナル | デンソーウェーブ Xnavis（Android） | QRコード |
| タブレット | iPad（Chrome） | パスワード or QRコード |
| PC | Windows（Chrome） | パスワード |

**注**: IP制限により倉庫内からのアクセスのみ許可されるため、QRコードスキャンのみで十分なセキュリティを確保

#### 2. JWT（JSON Web Token）構造

```typescript
{
  "header": {
    "alg": "HS256",
    "typ": "JWT"
  },
  "payload": {
    "sub": "user_id",
    "user_code": "USR001",
    "name": "山田太郎",
    "email": "yamada@example.com",
    "role": "staff",
    "permissions": ["task:read", "task:write", "project:write"],
    "device_type": "pc",
    "iat": 1699999999,
    "exp": 1700003599
  },
  "signature": "..."
}
```


---

## 認可（RBAC）

### ロール定義

| ロール | 説明 | 対象ユーザー |
|--------|------|--------------|
| `admin` | システム管理者 | IT管理者、システム運用担当 |
| `manager` | 倉庫管理者 | 倉庫長、リーダー |
| `staff` | 一般作業者 | 倉庫作業員 |
| `viewer` | 閲覧専用 | 荷主担当者、監査担当 |

### 権限マトリクス（Phase 1: 27画面）

#### 入荷管理

| 画面 | admin | manager | staff | viewer |
|------|-------|---------|-------|--------|
| ASN一覧 | CRUD | CRUD | R | R |
| ASN登録 | CRUD | CRUD | - | - |
| ASN詳細 | CRUD | CRUD | R | R |
| 入荷予定一覧 | CRUD | CRUD | R | R |
| 入荷検品 | CRUD | CRUD | CRU | - |
| 入荷実績一覧 | CRUD | CRUD | R | R |

#### 在庫管理

| 画面 | admin | manager | staff | viewer |
|------|-------|---------|-------|--------|
| 在庫一覧 | CRUD | CRUD | R | R |
| 在庫詳細 | CRUD | CRUD | R | R |
| 在庫移動 | CRUD | CRUD | CRU | - |
| 棚卸一覧 | CRUD | CRUD | R | R |
| 棚卸実施 | CRUD | CRUD | CRU | - |
| 棚卸差異確認 | CRUD | CRUD | R | R |

#### 出荷管理

| 画面 | admin | manager | staff | viewer |
|------|-------|---------|-------|--------|
| 出荷指示一覧 | CRUD | CRUD | R | R |
| 出荷指示登録 | CRUD | CRUD | - | - |
| ピッキング | CRUD | CRUD | CRU | - |
| 出荷検品 | CRUD | CRUD | CRU | - |
| 出荷実績一覧 | CRUD | CRUD | R | R |

#### マスタ管理

| 画面 | admin | manager | staff | viewer |
|------|-------|---------|-------|--------|
| 商品マスタ | CRUD | CRUD | R | R |
| ロケーションマスタ | CRUD | CRUD | R | R |
| 荷主マスタ | CRUD | R | - | - |
| 倉庫マスタ | CRUD | R | - | - |
| ユーザーマスタ | CRUD | R | - | - |

#### システム管理

| 画面 | admin | manager | staff | viewer |
|------|-------|---------|-------|--------|
| ダッシュボード | CRUD | CRUD | R | R |
| システム設定 | CRUD | R | - | - |
| 監査ログ | R | R | - | - |

**凡例**: C=Create, R=Read, U=Update, D=Delete, -=アクセス不可

---

## データ保護

### 暗号化

#### 1. 転送時の暗号化（Encryption in Transit）

| 対象 | 方式 | 設定 |
|------|------|------|
| クライアント-ALB間 | TLS 1.3 | HTTPS強制、HSTS有効 |
| ALB-ECS間 | TLS 1.2+ | 内部通信も暗号化 |
| ECS-RDS間 | TLS 1.2+ | SSL接続必須 |
| ECS-ElastiCache間 | TLS 1.2+ | 暗号化有効 |

#### 2. 保存時の暗号化（Encryption at Rest）

| 対象 | 方式 | 鍵管理 |
|------|------|--------|
| RDS | AES-256 | AWS KMS（CMK） |
| S3 | AES-256 | AWS KMS（CMK） |
| ElastiCache | AES-256 | AWS KMS |
| EBS | AES-256 | AWS KMS |
| CloudWatch Logs | AES-256 | AWS KMS |

### 個人情報保護

#### 1. 個人情報の分類

| 分類 | 項目例 | 保護レベル |
|------|--------|------------|
| 機密 | パスワード、APIキー | ハッシュ化/暗号化必須 |
| 個人情報 | 氏名、電話番号、住所 | 暗号化推奨、マスキング |
| 業務情報 | 商品コード、在庫数 | アクセス制御 |
| 公開情報 | 倉庫名、エリア名 | 制限なし |

#### 2. マスキング処理

```typescript
// 表示時のマスキング例
const maskingRules = {
  phone: (value: string) => value.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2'),
  email: (value: string) => value.replace(/(.{2}).*(@.*)/, '$1***$2'),
  address: (value: string) => value.substring(0, 10) + '***',
  name: (value: string) => value.charAt(0) + '**',
};
```

---

## 監査ログ

### ログ設計

#### 1. 監査ログテーブル

```sql
CREATE TABLE audit_logs (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  timestamp TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  user_id UUID REFERENCES users(id),
  user_code VARCHAR(20),
  action VARCHAR(50) NOT NULL,
  resource_type VARCHAR(50) NOT NULL,
  resource_id VARCHAR(100),
  old_value JSONB,
  new_value JSONB,
  ip_address INET,
  user_agent TEXT,
  device_type VARCHAR(20),
  result VARCHAR(20) NOT NULL,
  error_message TEXT,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_audit_logs_timestamp ON audit_logs(timestamp);
CREATE INDEX idx_audit_logs_user_id ON audit_logs(user_id);
CREATE INDEX idx_audit_logs_action ON audit_logs(action);
CREATE INDEX idx_audit_logs_resource ON audit_logs(resource_type, resource_id);
```

#### 2. ログ出力項目

| 項目 | 説明 | 例 |
|------|------|-----|
| timestamp | 操作日時（UTC） | 2026-01-19T10:30:00Z |
| user_id | ユーザーID | uuid |
| user_code | ユーザーコード | USR001 |
| action | 操作種別 | CREATE, UPDATE, DELETE, LOGIN |
| resource_type | リソース種別 | asn, inventory, shipment |
| resource_id | リソースID | ASN-20260119-001 |
| old_value | 変更前の値 | JSON |
| new_value | 変更後の値 | JSON |
| ip_address | クライアントIP | 192.168.1.100 |
| user_agent | ユーザーエージェント | Mozilla/5.0... |
| device_type | デバイス種別 | pc, tablet, handy |
| result | 結果 | success, failure |
| error_message | エラーメッセージ | Validation failed |

### ログ保管

| 項目 | 設定 |
|------|------|
| 保管期間 | 7年間（法令準拠） |
| 保管場所 | CloudWatch Logs → S3（Glacier） |
| 改ざん防止 | S3 Object Lock（Governance Mode） |
| アクセス制御 | IAMポリシーで制限 |

---

## セキュリティ実装

### 入力検証

```typescript
// Zodスキーマによる入力検証
import { z } from 'zod';

export const createAsnSchema = z.object({
  shipper_id: z.string().uuid(),
  expected_arrival_date: z.string().datetime(),
  items: z.array(z.object({
    product_id: z.string().uuid(),
    expected_quantity: z.number().int().positive().max(999999),
    lot_number: z.string().max(50).optional(),
  })).min(1).max(1000),
  notes: z.string().max(1000).optional(),
});
```

### SQLインジェクション対策

```typescript
// Prisma ORMによるパラメータ化クエリ
const inventory = await prisma.inventory.findMany({
  where: {
    product_id: productId,  // 自動エスケープ
    quantity: { gt: 0 },
  },
});

// 生SQLが必要な場合もパラメータ化
const result = await prisma.$queryRaw`
  SELECT * FROM inventory
  WHERE product_id = ${productId}
  AND quantity > ${minQuantity}
`;
```

### XSS対策

```typescript
// Vue.jsのテンプレートは自動エスケープ
<template>
  <div>{{ userInput }}</div>  <!-- 自動エスケープ -->
  <div v-html="sanitizedHtml"></div>  <!-- 明示的にサニタイズ -->
</template>

// CSPヘッダー設定
app.use(helmet({
  contentSecurityPolicy: {
    directives: {
      defaultSrc: ["'self'"],
      scriptSrc: ["'self'"],
      styleSrc: ["'self'", "'unsafe-inline'"],
      imgSrc: ["'self'", "data:", "https:"],
    },
  },
}));
```

### CSRF対策

```typescript
// CSRFトークン生成・検証
import { fastifyCsrf } from '@fastify/csrf-protection';

app.register(fastifyCsrf, {
  cookieOpts: {
    signed: true,
    httpOnly: true,
    sameSite: 'strict',
    secure: true,
  },
});
```

---

## セキュリティ監視

### 監視項目

| 項目 | 閾値 | アラート |
|------|------|----------|
| ログイン失敗 | 5回/分/IP | 警告 |
| 認証エラー | 10回/分 | 警告 |
| 権限エラー | 5回/分/ユーザー | 警告 |
| 異常なAPI呼び出し | 100回/分/ユーザー | 警告 |
| SQLエラー | 1回 | 調査 |

### インシデント対応

1. **検知**: CloudWatch Alarms、GuardDuty
2. **分析**: CloudWatch Logs Insights、Athena
3. **対応**: 自動ブロック、手動調査
4. **報告**: インシデントレポート作成
5. **改善**: 再発防止策の実施

---

## 関連ドキュメント

- [AWSアーキテクチャ](./01_AWS_Architecture.md)
- [開発環境セットアップ](./02_Development_Environment_Setup.md)
- [プロジェクト構成](./03_Project_Structure.md)
- [CI/CD設計書](./05_CICD_Design.md)
- [技術スタック共通仕様](./06_Tech_Stack_Common_Specs.md)

---

## 変更履歴

| バージョン | 日付 | 変更内容 |
|-----------|------|---------|
| v1.0.0 | 2026-02-04 | テンプレート化（汎用サンプル） |

---

**作成者**: [Your Team Name]
**最終更新**: 2026-02-04
