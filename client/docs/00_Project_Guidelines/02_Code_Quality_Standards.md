# コード品質基準

**バージョン**: v1.1.0
**最終更新**: 2026-02-04
**目的**: コード品質の基準と実践方法を定義

---

## 目次

1. [コード品質の原則](#コード品質の原則)
2. [命名規則](#命名規則)
3. [関数・メソッド設計](#関数メソッド設計)
4. [クラス設計](#クラス設計)
5. [コメント規則](#コメント規則)
6. [エラーハンドリング](#エラーハンドリング)
7. [型安全性](#型安全性)
8. [パフォーマンス](#パフォーマンス)
9. [セキュリティ](#セキュリティ)
10. [コードレビュー基準](#コードレビュー基準)

---

## コード品質の原則

### **SOLID原則**

1. **Single Responsibility Principle（単一責任の原則）**
   - 1つのクラス・関数は1つの責任のみを持つ

2. **Open/Closed Principle（開放/閉鎖の原則）**
   - 拡張に対して開いており、修正に対して閉じている

3. **Liskov Substitution Principle（リスコフの置換原則）**
   - 派生クラスは基底クラスと置き換え可能

4. **Interface Segregation Principle（インターフェース分離の原則）**
   - クライアントは使用しないインターフェースに依存しない

5. **Dependency Inversion Principle（依存性逆転の原則）**
   - 抽象に依存し、具象に依存しない

---

### **DRY原則（Don't Repeat Yourself）**

```typescript
// ❌ 避ける
function calculateOrderTotal(order: Order) {
  return order.items.reduce((sum, item) => sum + item.price * item.quantity, 0) * 1.1;
}

function calculateInvoiceTotal(invoice: Invoice) {
  return invoice.items.reduce((sum, item) => sum + item.price * item.quantity, 0) * 1.1;
}

// ✅ 推奨
function calculateSubtotal(items: LineItem[]) {
  return items.reduce((sum, item) => sum + item.price * item.quantity, 0);
}

function applyTax(amount: number, taxRate = 0.1) {
  return amount * (1 + taxRate);
}

function calculateOrderTotal(order: Order) {
  return applyTax(calculateSubtotal(order.items));
}
```

---

### **KISS原則（Keep It Simple, Stupid）**

```typescript
// ❌ 避ける（過度に複雑）
function isValid(value: string | number | boolean | null | undefined): boolean {
  return typeof value === 'string' ? value.length > 0 : 
         typeof value === 'number' ? !isNaN(value) : 
         typeof value === 'boolean' ? true : false;
}

// ✅ 推奨（シンプル）
function isValidString(value: string): boolean {
  return value.length > 0;
}

function isValidNumber(value: number): boolean {
  return !isNaN(value);
}
```

---

## 命名規則

### **変数・関数名**

| 種類 | 形式 | 例 |
|------|------|-----|
| **変数** | camelCase | `userName`, `orderTotal` |
| **定数** | UPPER_SNAKE_CASE | `MAX_RETRY_COUNT`, `API_BASE_URL` |
| **関数** | camelCase（動詞で始める） | `getUserById`, `calculateTotal` |
| **クラス** | PascalCase | `UserService`, `OrderRepository` |
| **インターフェース** | PascalCase（`I`プレフィックス不要） | `User`, `OrderItem` |
| **型エイリアス** | PascalCase | `UserId`, `OrderStatus` |
| **Enum** | PascalCase | `OrderStatus`, `UserRole` |

---

### **命名のベストプラクティス**

```typescript
// ❌ 避ける
const d = new Date();
const u = await getU(1);
function calc(a: number, b: number) { return a + b; }

// ✅ 推奨
const currentDate = new Date();
const user = await getUserById(1);
function calculateTotal(price: number, quantity: number): number {
  return price * quantity;
}
```

---

### **真偽値の命名**

```typescript
// ✅ 推奨
const isActive = true;
const hasPermission = false;
const canEdit = true;
const shouldRetry = false;
```

---

## 関数・メソッド設計

### **関数の長さ**

- **推奨**: 20行以内
- **最大**: 50行
- **理由**: 可読性、テスト容易性

---

### **引数の数**

- **推奨**: 3個以内
- **最大**: 5個
- **多い場合**: オブジェクトにまとめる

```typescript
// ❌ 避ける
function createUser(
  name: string,
  email: string,
  age: number,
  address: string,
  phone: string,
  role: string
) { }

// ✅ 推奨
interface CreateUserParams {
  name: string;
  email: string;
  age: number;
  address: string;
  phone: string;
  role: string;
}

function createUser(params: CreateUserParams) { }
```

---

### **単一責任**

```typescript
// ❌ 避ける
function processOrder(order: Order) {
  // バリデーション
  if (!order.items.length) throw new Error('No items');
  
  // 在庫確認
  for (const item of order.items) {
    const stock = getStock(item.productId);
    if (stock < item.quantity) throw new Error('Out of stock');
  }
  
  // 価格計算
  const total = order.items.reduce((sum, item) => sum + item.price * item.quantity, 0);
  
  // 保存
  saveOrder(order);
  
  // メール送信
  sendEmail(order.ownerId, 'Order confirmed');
}

// ✅ 推奨
function processOrder(order: Order) {
  validateOrder(order);
  checkStock(order);
  const total = calculateTotal(order);
  saveOrder(order);
  notifyOwner(order);
}
```

---

## クラス設計

### **クラスの責任**

```typescript
// ❌ 避ける（複数の責任）
class User {
  constructor(public name: string, public email: string) {}
  
  save() { /* データベース保存 */ }
  sendEmail() { /* メール送信 */ }
  validateEmail() { /* バリデーション */ }
}

// ✅ 推奨（単一責任）
class User {
  constructor(public name: string, public email: string) {}
}

class UserRepository {
  save(user: User) { /* データベース保存 */ }
}

class EmailService {
  send(to: string, subject: string, body: string) { /* メール送信 */ }
}

class EmailValidator {
  validate(email: string): boolean { /* バリデーション */ }
}
```

---

## コメント規則

### **コメントの原則**

- **「なぜ」を説明**: コードで「何を」しているかは明確に
- **複雑なロジック**: アルゴリズムの説明
- **TODO/FIXME**: 技術的負債の記録

---

### **コメントの例**

```typescript
// ❌ 避ける（自明なコメント）
// ユーザーIDを取得
const userId = user.id;

// ✅ 推奨（理由を説明）
// パフォーマンス向上のため、キャッシュから取得
const cachedUser = await cache.get(`user:${userId}`);

// TODO: リファクタリング必要
// 理由: 複雑なネストが深く、可読性が低い
// 期限: 2025-01-31
// 担当: @username
function complexFunction() {
  // ...
}
```

---

## エラーハンドリング

### **エラーの種類**

```typescript
// カスタムエラークラス
class ApplicationError extends Error {
  constructor(
    message: string,
    public code: string,
    public statusCode: number = 500
  ) {
    super(message);
    this.name = 'ApplicationError';
  }
}

class ValidationError extends ApplicationError {
  constructor(message: string) {
    super(message, 'VALIDATION_ERROR', 400);
  }
}

class NotFoundError extends ApplicationError {
  constructor(message: string) {
    super(message, 'NOT_FOUND', 404);
  }
}
```

---

### **エラーハンドリングのベストプラクティス**

- `@ts-ignore` は原則使用しない。型定義や実装を修正して解決する
- 空の `catch` ブロックは禁止。必ずログ出力・再スロー・明示的な無視理由のいずれかを記述する

```typescript
// ✅ 推奨
async function getUserById(id: string): Promise<User> {
  try {
    const user = await db.user.findUnique({ where: { id } });
    
    if (!user) {
      throw new NotFoundError(`User not found: ${id}`);
    }
    
    return user;
  } catch (error) {
    if (error instanceof NotFoundError) {
      throw error;
    }
    
    logger.error('Failed to get user', { id, error });
    throw new ApplicationError('Failed to get user');
  }
}
```

---

## 型安全性

### **型定義**

```typescript
// ❌ 避ける
function processData(data: any) {
  return data.value * 2;
}

// ✅ 推奨
interface DataInput {
  value: number;
}

function processData(data: DataInput): number {
  return data.value * 2;
}
```

---

### **型ガード**

```typescript
function isUser(value: unknown): value is User {
  return (
    typeof value === 'object' &&
    value !== null &&
    'id' in value &&
    'name' in value
  );
}

function processUser(value: unknown) {
  if (!isUser(value)) {
    throw new ValidationError('Invalid user data');
  }
  
  // ここでvalueはUser型として扱える
  console.log(value.name);
}
```

---

## パフォーマンス

- パフォーマンス最適化よりも、まずシンプルさと保守性を優先する

### **N+1問題の回避**

```typescript
// ❌ 避ける
const users = await db.user.findMany();
for (const user of users) {
  const orders = await db.order.findMany({ where: { userId: user.id } });
}

// ✅ 推奨
const users = await db.user.findMany({
  include: { orders: true }
});
```

---

### **メモ化**

```typescript
import { memoize } from 'lodash';

const expensiveCalculation = memoize((input: number) => {
  // 重い計算
  return input * 2;
});
```

---

## セキュリティ

### **入力検証**

```typescript
import { z } from 'zod';

const UserSchema = z.object({
  name: z.string().min(1).max(100),
  email: z.string().email(),
  age: z.number().int().min(0).max(150),
});

function createUser(input: unknown) {
  const validatedInput = UserSchema.parse(input);
  // ...
}
```

---

### **環境変数**

```typescript
// ❌ 避ける
const API_KEY = 'sk-1234567890abcdef';

// ✅ 推奨
const API_KEY = process.env.API_KEY;
if (!API_KEY) {
  throw new Error('API_KEY is not set');
}
```

---

## コードレビュー基準

### **チェックリスト**

- [ ] 命名規則に従っている
- [ ] 関数は単一責任を持つ
- [ ] エラーハンドリングが適切
- [ ] 型安全性が確保されている
- [ ] テストが書かれている
- [ ] パフォーマンスが考慮されている
- [ ] セキュリティが考慮されている
- [ ] ドキュメントが更新されている

---

**作成者**: [Your Team Name]
**最終更新**: 2026-02-04

