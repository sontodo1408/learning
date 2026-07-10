# プロジェクトガイドライン (Project Guidelines)

**プロジェクト名**: [Your Project Name]
**最終更新**: 2026-02-04
**設計ステータス**: [Phase X 進行中]

---

## 概要

このディレクトリには、プロジェクト全体のガイドラインが格納されています。

すべての開発者、デザイナー、QA担当者は、これらのガイドラインに従って作業を進めてください。

---

## ディレクトリ構成

```
00_Project_Guidelines/
├── README.md                                  ← このファイル
├── 00_AI_Rules.md                             ← AI利用ルール
├── 01_AI_Driven_Development_Guidelines.md    ← AI駆動開発ガイドライン
├── 02_Code_Quality_Standards.md              ← コード品質基準
├── 03_Git_Workflow.md                         ← Git運用ガイドライン
├── 04_Documentation_Standards.md             ← ドキュメント基準
├── 05_Design_Process_Workflow.md             ← 設計プロセスワークフロー
└── 06_Design_Implementation_Guide.md         ← 設計実施ガイド
```

---

## ファイル一覧

| ファイル名 | 内容 | 対象読者 |
|-----------|------|---------|
| `00_AI_Rules.md` | AI利用時のルール・制約 | 全員 |
| `01_AI_Driven_Development_Guidelines.md` | AI駆動開発の基本理念、エラーハンドリング、テスト規律等 | 全員 |
| `02_Code_Quality_Standards.md` | コード品質の原則、命名規則、設計パターン等 | 開発者 |
| `03_Git_Workflow.md` | ブランチ戦略、コミットメッセージ規則、プルリクエスト等 | 全員 |
| `04_Documentation_Standards.md` | ドキュメント作成の基準、記載粒度、Markdown記法等 | 全員 |
| `05_Design_Process_Workflow.md` | 設計プロセスの全体像、各フェーズの実施内容、イテレーション戦略 | 全員 |
| `06_Design_Implementation_Guide.md` | 設計の具体的な実施手順、AI活用のベストプラクティス | 全員 |

---

## 使い方

### **新メンバーのオンボーディング**

1. **必読ドキュメント**（順番に読む）
   - `01_AI_Driven_Development_Guidelines.md`
   - `02_Code_Quality_Standards.md`
   - `03_Git_Workflow.md`
   - `04_Documentation_Standards.md`
   - `05_Design_Process_Workflow.md`
   - `06_Design_Implementation_Guide.md`

2. **技術スタックの確認**
   - `../02_Technical_Design/07_Tech_Stack_Common_Specs.md`

3. **開発環境構築**
   - `../02_Technical_Design/03_Development_Environment_Setup.md`

---

### **開発時の参照**

| 作業内容 | 参照ドキュメント |
|---------|----------------|
| **設計プロセス全体** | `05_Design_Process_Workflow.md` |
| **DB設計** | `06_Design_Implementation_Guide.md` + `../03_Database_Design/00_Database_Design_Guidelines.md` |
| **画面設計** | `06_Design_Implementation_Guide.md` + `../04_Screen_Design/00_Screen_Design_Guidelines.md` |
| **API設計** | `06_Design_Implementation_Guide.md` + `../05_API_Design/00_Common_Specifications.md` |
| **詳細設計** | `06_Design_Implementation_Guide.md` + `../06_Detailed_Design/00_Detailed_Design_Guidelines.md` |
| **コード実装** | `02_Code_Quality_Standards.md` |
| **コミット** | `03_Git_Workflow.md` |
| **プルリクエスト** | `03_Git_Workflow.md` |
| **ドキュメント作成** | `04_Documentation_Standards.md` |
| **エラーハンドリング** | `01_AI_Driven_Development_Guidelines.md` |
| **テスト作成** | `01_AI_Driven_Development_Guidelines.md` |

---

## ガイドラインの概要

### **01_AI_Driven_Development_Guidelines.md**

**内容**:
- 開発の基本理念
- エラーハンドリングの原則
- コード品質の基準
- テスト規律
- 保守性とリファクタリング
- セキュリティの考え方
- パフォーマンスの意識
- 信頼性の確保

**対象読者**: 全員

---

### **02_Code_Quality_Standards.md**

**内容**:
- コード品質の原則（SOLID、DRY、KISS）
- 命名規則
- 関数・メソッド設計
- クラス設計
- コメント規則
- エラーハンドリング
- 型安全性
- パフォーマンス
- セキュリティ

**対象読者**: 開発者

---

### **03_Git_Workflow.md**

**内容**:
- ブランチ戦略
- コミットメッセージ規則（Conventional Commits）
- コミットの粒度
- プルリクエスト
- コードレビュー
- マージ戦略
- タグ管理
- 禁止事項

**対象読者**: 全員

---

### **04_Documentation_Standards.md**

**内容**:
- ドキュメントの原則
- ドキュメント構成
- ファイル命名規則
- 記載粒度
- Markdown記法
- 図表の作成（Mermaid）
- バージョン管理
- 整合性確認

**対象読者**: 全員

---

### **05_Design_Process_Workflow.md**

**内容**:
- 設計プロセスの全体像
- Phase 1: 要件確認・基盤整備
- Phase 2: データベース設計詳細化
- Phase 3: 画面設計詳細化
- Phase 4: API設計
- Phase 5: 詳細設計
- Phase 6: 整合性確認
- Phase 7: レビュー・承認
- イテレーション戦略
- 成果物チェックリスト

**対象読者**: 全員

**重要なポイント**:
- DB → 画面 → API → 詳細設計の順序を守る
- 各フェーズで整合性を継続的に確認
- 機能単位でイテレーション
- モックアップを真実とする

---

### **06_Design_Implementation_Guide.md**

**内容**:
- Phase 2: データベース設計詳細化の実施手順
- Phase 3: 画面設計詳細化の実施手順
- Phase 4: API設計の実施手順
- Phase 5: 詳細設計の実施手順
- 整合性確認の実施手順
- AI活用のベストプラクティス

**対象読者**: 全員

**重要なポイント**:
- 具体的な手順を記載
- テンプレート・ガイドラインの活用方法
- 整合性確認のマトリクス表
- AI活用の効果的なプロンプト例

---

## 重要な原則

### **開発の基本理念**

1. **品質・保守性・安全性を常に意識**
   - 動くコードを書くだけでなく、品質・保守性・安全性を常に意識する

2. **問題を放置しない**
   - 問題を見つけたら放置せず、必ず対処または明示的に記録する

3. **ボーイスカウトルール**
   - コードを見つけた時よりも良い状態で残す

4. **仕様が不明な場合は質問**
   - 仕様が明示されていない部分は必ず質問する

---

### **コミットメッセージ規則**

**Conventional Commits形式**:
```
<type>(<scope>): <subject>

<body>

<footer>
```

**Type**:
- `feat`: 新機能
- `fix`: バグ修正
- `docs`: ドキュメント
- `style`: コードスタイル
- `refactor`: リファクタリング
- `test`: テスト
- `chore`: ビルド・設定

**例**:
```
feat: ダッシュボード画面を追加

- 在庫状況を一目で確認できるようにした
- グラフで入出庫の推移を表示

Closes #123
```

---

### **ドキュメント命名規則**

- **ファイル名**: 英語（Git管理、パス指定の簡便性）
- **内容**: 日本語（開発チームが日本語話者、正確な意図伝達）

**例**:
- `02_Dashboard.md` （ファイル名: 英語）
- 内容: 日本語で記載

---

## 関連ドキュメント

- **技術スタック**: `../02_Technical_Design/07_Tech_Stack_Common_Specs.md`
- **開発環境構築**: `../02_Technical_Design/03_Development_Environment_Setup.md`
- **データベース設計**: `../03_Database_Design/`
- **画面設計**: `../04_Screen_Design/`
- **API設計**: `../05_API_Design/`
- **詳細設計**: `../06_Detailed_Design/`
- **テスト設計**: `../07_Test_Design/`

---

## 注意事項

### **ガイドラインの更新**

- ガイドラインは定期的に見直し、必要に応じて更新する
- 更新時は変更履歴を記録する
- チーム全体で合意を得る

### **ガイドラインの遵守**

- すべての開発者、デザイナー、QA担当者はガイドラインに従う
- 不明点があれば質問する
- 改善提案は歓迎

---

**作成者**: [Your Team Name]
**最終更新**: 2026-02-04
