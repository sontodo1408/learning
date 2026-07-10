# 画面設計 (Screen Design)

**プロジェクト名**: [Your Project Name]
**最終更新**: 2026-02-04
**ステータス**: [Phase X 進行中]

---

## 概要

このディレクトリには、プロジェクトの画面設計書が格納されています。

---

## ディレクトリ構成

```
04_Screen_Design/
├── README.md                                ← このファイル
├── 00_Screen_Design_Guidelines.md          ← 画面設計書作成ガイドライン
├── 00_Screen_Design_Template.md            ← 画面設計書テンプレート
└── （プロジェクト固有の設計書をここに追加）
```

**サンプルプロジェクト（タスク管理システム）の場合**:
```
04_Screen_Design/
├── README.md
├── 00_Screen_Design_Guidelines.md
├── 00_Screen_Design_Template.md
├── 01_Screen_List_Transition.md            ← 画面一覧・遷移図
├── 02_Login.md                             ← ログイン画面（SCR-01）
├── 03_Project_List.md                      ← プロジェクト一覧（SCR-02）
├── 04_Project_Detail.md                    ← プロジェクト詳細（SCR-03）
├── 05_Task_Detail.md                       ← タスク詳細（SCR-04）
└── 06_User_Management.md                   ← ユーザー管理（SCR-05）
```

---

## ファイル一覧

### **ガイドライン・テンプレート**

| ファイル名 | 内容 | 用途 |
|-----------|------|------|
| `00_Screen_Design_Guidelines.md` | 画面設計書作成ガイドライン | 設計時の参照 |
| `00_Screen_Design_Template.md` | 画面設計書テンプレート | 新規画面設計時に使用 |

### **プロジェクト固有の設計書**

プロジェクト開始後、以下のような設計書を作成します：

| ファイル名例 | 内容 | 用途 |
|-----------|------|------|
| `01_Screen_List_Transition.md` | 画面一覧・遷移図 | 全体像の把握 |
| `02_Login.md` | ログイン画面 | 認証機能 |
| `03_Project_List.md` | プロジェクト一覧画面 | 一覧表示 |
| `04_Project_Detail.md` | プロジェクト詳細画面 | 詳細表示・編集 |
| `05_Task_Detail.md` | タスク詳細画面 | タスク管理 |
| `06_User_Management.md` | ユーザー管理画面 | ユーザーCRUD |

---

## 使い方

### **新しい画面設計書を作成する**

1. `00_Screen_Design_Template.md` をコピー
2. ファイル名を `XX_[Function_Name].md` に変更
3. テンプレートに従って記載
4. `00_Screen_Design_Guidelines.md` を参照して記載粒度を確認

### **既存の画面設計書を更新する**

1. 該当ファイルを開く
2. バージョン番号を更新
3. 変更内容を記載
4. レビューリストで整合性を確認

---

## 関連ドキュメント

- **ガイドライン**: `00_Screen_Design_Guidelines.md`
- **テンプレート**: `00_Screen_Design_Template.md`
- **プロジェクトガイドライン**: `../00_Project_Guidelines/`
- **要件定義**: `../01_Business_Process/requirements/`
- **技術設計**: `../02_Technical_Design/`
- **データベース設計**: `../03_Database_Design/`
- **API設計**: `../05_API_Design/`
- **詳細設計**: `../06_Detailed_Design/`
- **テスト設計**: `../07_Test_Design/`

---

## 注意事項

### **記載粒度**
- ✅ **記載する**: 画面要素、レイアウト、基本機能
- ❌ **記載しない**: ビジネスロジック、実装コード、技術スタック

### **ファイル名規則**
- **形式**: `[番号]_[Function_Name].md`
- **番号**: 2桁（01-99）
- **Function_Name**: 英語、アンダースコア区切り

**例**:
- `01_Screen_List_Transition.md`
- `02_Dashboard.md`
- `03_ASN_ADN_Management.md`

### **バージョン管理**
- **v1.0.0**: 初版
- **v1.1.0**: 機能追加・大幅修正
- **v1.0.1**: 軽微な修正

---

**作成者**: [Your Team Name]
**最終更新**: 2026-02-04
