# Git運用ガイドライン

**バージョン**: v1.2.0
**最終更新**: 2026-02-05
**目的**: Git運用の基準とベストプラクティスを定義

---

## 目次

1. [ブランチ戦略](#ブランチ戦略)
2. [コミットメッセージ規則](#コミットメッセージ規則)
3. [コミットの粒度](#コミットの粒度)
4. [プルリクエスト](#プルリクエスト)
5. [コードレビュー](#コードレビュー)
6. [マージ戦略](#マージ戦略)
7. [タグ管理](#タグ管理)
8. [禁止事項](#禁止事項)

---

## ブランチ戦略

### **ブランチ構成**

```
main (本番環境)
  ├── develop (開発環境)
  │   ├── feature/[issue-number]-[feature-name]
  │   ├── bugfix/[issue-number]-[bug-name]
  │   └── refactor/[issue-number]-[refactor-name]
  ├── release/[version]
  └── hotfix/[issue-number]-[hotfix-name]
```

---

### **ブランチの種類**

| ブランチ | 用途 | 命名規則 | 例 |
|---------|------|---------|-----|
| **main** | 本番環境 | `main` | `main` |
| **develop** | 開発環境 | `develop` | `develop` |
| **feature** | 新機能開発 | `feature/[issue-number]-[feature-name]` | `feature/123-add-dashboard` |
| **bugfix** | バグ修正 | `bugfix/[issue-number]-[bug-name]` | `bugfix/456-fix-login-error` |
| **refactor** | リファクタリング | `refactor/[issue-number]-[refactor-name]` | `refactor/789-improve-performance` |
| **release** | リリース準備 | `release/[version]` | `release/1.2.0` |
| **hotfix** | 緊急修正 | `hotfix/[issue-number]-[hotfix-name]` | `hotfix/999-critical-security-fix` |

---

### **ブランチ運用ルール**

1. **main/developへの直接コミット禁止**
   - 必ずプルリクエスト経由でマージ

2. **ブランチは最新の状態から作成**
   ```bash
   git checkout develop
   git pull origin develop
   git checkout -b feature/123-add-dashboard
   ```

3. **定期的にdevelopをマージ**
   ```bash
   git checkout feature/123-add-dashboard
   git merge develop
   ```

4. **作業完了後はブランチを削除**
   ```bash
   git branch -d feature/123-add-dashboard
   git push origin --delete feature/123-add-dashboard
   ```

---

## コミットメッセージ規則

### **Conventional Commits形式**

```
<type>(<scope>): <subject>

<body>

<footer>
```

---

### **Type（必須）**

- コミット type はプロジェクト共通の Rules（00_AI_Rules.md）で定義されたものに従う

| Type | 用途 | 例 |
|------|------|-----|
| **feat** | 新機能 | `feat: ダッシュボード画面を追加` |
| **fix** | バグ修正 | `fix: ログイン時のエラーを修正` |
| **docs** | ドキュメント | `docs: READMEを更新` |
| **refactor** | リファクタリング | `refactor: ユーザーサービスを改善` |
| **test** | テスト | `test: ユーザーサービスのテストを追加` |
| **chore** | ビルド・設定・依存関係更新など | `chore: 依存関係を更新` |
| **revert** | 変更の取り消し（`git revert` による自動生成コミット） | `revert: feat: ダッシュボード画面を追加` |

---

### **Scope（任意）**

```
feat(auth): ログイン機能を追加
fix(dashboard): グラフ表示のバグを修正
docs(api): API設計書を更新
```

---

### **Subject（必須）**

- **形式**: 命令形、現在形
- **長さ**: 50文字以内
- **言語**: 日本語または英語（プロジェクトで統一）
- **末尾**: ピリオド不要

**例**:
```
✅ feat: ダッシュボード画面を追加
✅ fix: ログイン時のエラーを修正
❌ feat: ダッシュボード画面を追加しました。（過去形）
❌ fix: ログインのバグ（不明確）
```

---

### **Body（任意）**

- **内容**: 変更の理由、背景
- **形式**: 箇条書きまたは段落

**例**:
```
feat: ダッシュボード画面を追加

- プロジェクト状況を一目で確認できるようにした
- グラフでタスクの進捗を表示
- リアルタイム更新に対応

関連Issue: #123
```

---

### **Footer（任意）**

- **Breaking Changes**: 破壊的変更
- **Issue参照**: `Closes #123`, `Fixes #456`

**例**:
```
feat: 認証APIを変更

BREAKING CHANGE: 認証トークンの形式をJWTに変更

Closes #123
```

---

## コミットの粒度

### **原子的コミット**

- **1コミット = 1つの変更**
- **理由**: ロールバック、レビュー、履歴追跡が容易

---

### **良いコミット例**

```bash
# ✅ 推奨（原子的）
git commit -m "feat: ユーザー登録機能を追加"
git commit -m "test: ユーザー登録のテストを追加"
git commit -m "docs: ユーザー登録のドキュメントを追加"

# ❌ 避ける（複数の変更）
git commit -m "feat: ユーザー登録機能とログイン機能を追加"
```

---

### **複数ファイル変更時の対応**

#### **関連する変更の場合**

1つのコミットにまとめ、Bodyで詳細を説明：

```
feat(user): ユーザープロフィール機能を追加

以下のファイルを追加・変更：
- src/services/UserProfileService.ts（新規）
- src/api/userProfile.ts（新規）
- src/models/User.ts（プロフィールフィールド追加）
- tests/userProfile.test.ts（新規）

Closes #78
```

#### **無関係な変更の場合**

**別々のコミットに分割**することを推奨。

---

### **コミット前のチェック**

```bash
# 変更内容を確認
git diff

# ステージングを確認
git diff --staged

# 部分的にステージング
git add -p
```

---

### **コミットメッセージのバリデーション**

コミット前に以下を確認：

- [ ] Typeが適切に選択されているか
- [ ] Subjectが日本語で記述されているか
- [ ] Subjectが50文字以内か
- [ ] 破壊的変更がある場合、`!`マークと`BREAKING CHANGE`が記載されているか
- [ ] 関連するIssueが参照されているか（該当する場合）
- [ ] 1つのコミットに1つの論理的変更のみ含まれているか

---

## プルリクエスト

### **プルリクエストのタイトル**

- **形式**: コミットメッセージと同じ（Conventional Commits）
- **例**: `feat: ダッシュボード画面を追加`

---

### **プルリクエストの説明**

```markdown
## 概要
ダッシュボード画面を追加しました。

## 変更内容
- プロジェクト状況を一目で確認できるダッシュボード画面を追加
- グラフでタスクの進捗を表示
- リアルタイム更新に対応

## スクリーンショット
![ダッシュボード](./screenshot.png)

## テスト
- [ ] 単体テスト
- [ ] 統合テスト
- [ ] E2Eテスト

## チェックリスト
- [ ] コードレビュー済み
- [ ] テスト追加済み
- [ ] ドキュメント更新済み

## 関連Issue
Closes #123
```

---

## コードレビュー

### **レビューの姿勢**

- **建設的**: 改善提案として受け取る
- **コードに焦点**: 個人ではなくコードに焦点を当てる
- **理由を説明**: 変更の理由と影響を明確に説明
- **学習機会**: フィードバックを学習機会として歓迎

---

### **レビューコメントの例**

```markdown
# ✅ 推奨（建設的）
この関数は複雑なので、小さな関数に分割することを提案します。
理由: テスト容易性と可読性が向上します。

# ❌ 避ける（批判的）
この関数は複雑すぎます。
```

---

### **レビューチェックリスト**

- [ ] コードが要件を満たしている
- [ ] 命名規則に従っている
- [ ] エラーハンドリングが適切
- [ ] テストが書かれている
- [ ] パフォーマンスが考慮されている
- [ ] セキュリティが考慮されている
- [ ] ドキュメントが更新されている

---

## マージ戦略

### **マージ方法**

| 方法 | 用途 | コマンド |
|------|------|---------|
| **Merge Commit** | 履歴を保持 | `git merge --no-ff` |
| **Squash and Merge** | コミット履歴を整理 | `git merge --squash` |
| **Rebase and Merge** | 線形履歴 | `git rebase` |

---

### **推奨マージ戦略**

- **feature → develop**: Squash and Merge
- **develop → main**: Merge Commit
- **hotfix → main**: Merge Commit

---

## タグ管理

### **バージョンタグ**

- **形式**: `vX.Y.Z`（セマンティックバージョニング）
- **X**: メジャーバージョン（破壊的変更）
- **Y**: マイナーバージョン（機能追加）
- **Z**: パッチバージョン（バグ修正）

**例**:
```bash
git tag -a v1.2.0 -m "Release version 1.2.0"
git push origin v1.2.0
```

---

## 禁止事項

### **絶対にやってはいけないこと**

1. **main/developへの直接コミット**
   - 必ずプルリクエスト経由

2. **強制プッシュ（force push）**
   ```bash
   # ❌ 禁止
   git push -f origin main
   ```

3. **大きなバイナリファイルのコミット**
   - Git LFSを使用

4. **機密情報のコミット**
   - APIキー、パスワード等

5. **コミット履歴の改ざん**
   - 公開済みのコミットをrebase/amendしない

---

**作成者**: [Your Team Name]
**最終更新**: 2026-02-05

