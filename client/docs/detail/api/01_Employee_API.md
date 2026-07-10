# API thiết kế - 01 API Nhân viên (Employee)

**Ngày tạo:** 2026-07-09
**Màn hình liên quan:** SCR-04 (Danh sách nhân viên)
**Chức năng liên quan:** F-002-01 ~ F-002-05
**Phase:** **Phase 1**

> **Ghi chú override tuân theo HRM_Requirements.md mục 4.1**: toàn bộ response lỗi của API tuân theo **RFC 7807** (`application/problem+json`), khác với mẫu `{success, error}` mặc định trong `../../05_API_Design/00_Common_Specifications.md` (tài liệu mẫu chung, chưa cập nhật theo ràng buộc kỹ thuật v2.0.0 của HRM). Response thành công dùng envelope tối giản `{data, meta?}` — không dùng `success: true` vì không cần thiết khi đã dùng đúng HTTP status code.

---

## Mục lục

1. [Tổng quan](#tổng-quan)
2. [Danh sách endpoint](#danh-sách-endpoint)
3. [API chi tiết](#api-chi-tiết)
4. [Định nghĩa kiểu TypeScript](#định-nghĩa-kiểu-typescript)
5. [Bảng định tuyến Fastify](#bảng-định-tuyến-fastify)

---

## Tổng quan

### **Mục đích**
Cung cấp API tra cứu, tìm kiếm, lọc và xuất Excel danh sách nhân viên phục vụ màn hình SCR-04 (chế độ danh sách). Các API cho chế độ xem/sửa/tạo mới của cùng SCR-04 được liệt kê để đảm bảo truy vết đầy đủ nhưng thiết kế chi tiết (request/response đầy đủ) sẽ hoàn thiện khi triển khai màn Chi tiết/Sửa/Tạo mới nhân viên.

### **Phạm vi Phase 1**
- ✅ Lấy danh sách nhân viên có tìm kiếm, lọc, sắp xếp, phân trang
- ✅ Xuất Excel danh sách nhân viên theo điều kiện lọc hiện tại

### **Giới hạn Phase 1**
- Xuất Excel xử lý đồng bộ (synchronous) khi số dòng ≤ 5.000; vượt ngưỡng này chuyển sang xử lý bất đồng bộ theo NFR 3.1 — **[Cần xác nhận / To be confirmed]**: ngưỡng cụ thể cần HR xác nhận theo quy mô nhân sự thực tế của SSV.

### **Phạm vi Phase 2 (dự kiến)**
- ⏳ Lọc theo thuộc tính mở rộng EAV (F-003)
- ⏳ Import Excel hàng loạt

### **Chức năng liên quan**
- F-002-01: Xem danh sách nhân viên
- F-002-02: Tìm kiếm / lọc nhân viên
- F-002-03: Xuất Excel danh sách nhân viên (liên quan F-016)
- F-002-04: Xem chi tiết nhân viên *(thiết kế đầy đủ ở tài liệu API của màn Chi tiết nhân viên)*
- F-002-05: Tạo mới / sửa / đặt lại mật khẩu / chuyển trạng thái nghỉ việc *(thiết kế đầy đủ ở tài liệu API của màn Tạo mới/Sửa nhân viên)*

---

## Danh sách endpoint

| Phương thức | Endpoint | Mô tả | Quyền yêu cầu | Phase | Chi tiết |
|---|---|---|---|---|---|
| GET | `/api/v1/employees` | Lấy danh sách nhân viên (tìm kiếm/lọc/sắp xếp/phân trang) | `employee.list = FULL` | Phase 1 | [§1](#1-lấy-danh-sách-nhân-viên) |
| GET | `/api/v1/employees/export` | Xuất Excel danh sách theo điều kiện lọc hiện tại | `employee.export = FULL` | Phase 1 | [§2](#2-xuất-excel-danh-sách-nhân-viên) |
| GET | `/api/v1/employees/:id` | Lấy chi tiết một nhân viên | `employee.view = FULL` | Phase 1 | Thiết kế đầy đủ ở API màn Chi tiết nhân viên |
| POST | `/api/v1/employees` | Tạo mới nhân viên | `employee.create = FULL` | Phase 1 | Thiết kế đầy đủ ở API màn Tạo mới nhân viên |
| PUT | `/api/v1/employees/:id` | Cập nhật hồ sơ nhân viên | `employee.edit = FULL` | Phase 1 | Thiết kế đầy đủ ở API màn Sửa nhân viên |
| PUT | `/api/v1/employees/:id/status` | Chuyển trạng thái (nghỉ việc / tạm nghỉ / đang làm việc) | `employee.delete = FULL` | Phase 1 | Thiết kế đầy đủ ở API màn Sửa nhân viên |
| POST | `/api/v1/employees/:id/reset-password` | Đặt lại mật khẩu, gửi mật khẩu tạm qua email | `employee.reset_password = FULL` | Phase 1 | Thiết kế đầy đủ ở API màn Chi tiết nhân viên |

**Ghi chú thiết kế**: hành động "xóa" ở màn danh sách **không** dùng `DELETE` vật lý hay xóa mềm (`deleted_at`), vì hồ sơ nhân viên phải được giữ lại cho mục đích lịch sử/kiểm toán (BR chung của hệ thống HRM). Thao tác này là **chuyển trạng thái nghiệp vụ** sang `RESIGNED` qua `PUT .../status`.

---

## API chi tiết

## 1. Lấy danh sách nhân viên

### **Endpoint**
```
GET /api/v1/employees
```

### **Mô tả**
Trả về danh sách nhân viên có phân trang, phục vụ bảng ở SCR-04. Áp dụng lọc mặc định `status=ACTIVE` nếu client không truyền `status`.

### **Chức năng liên quan**
- F-002-01, F-002-02

### **Quyền yêu cầu**
- `employee.list = FULL` (kiểm tra ở middleware `authenticate` + guard RBAC tại tầng service, theo NFR 3.2)

### **Tham số truy vấn (query)**

| Tham số | Kiểu | Bắt buộc | Mô tả | Ví dụ |
|---|---|---|---|---|
| `page` | number | ❌ | Số trang (mặc định `1`) | `1` |
| `per_page` | number | ❌ | Số dòng/trang (mặc định `20`, tối đa `100`) | `20` |
| `q` | string | ❌ | Từ khóa tìm theo `employee_code`, `full_name`, `email` | `nguyen` |
| `department_id` | number[] | ❌ | Lọc theo phòng ban (nhiều giá trị, lặp param) | `department_id=1&department_id=2` |
| `job_title_id` | number[] | ❌ | Lọc theo chức vụ | `job_title_id=5` |
| `office` | string | ❌ | `HN` \| `HUE` | `HN` |
| `status` | string[] | ❌ | `ACTIVE` \| `ON_LEAVE` \| `RESIGNED` (mặc định `ACTIVE`) | `status=ACTIVE&status=ON_LEAVE` |
| `hire_date_from` | string (date) | ❌ | Ngày vào làm từ | `2020-01-01` |
| `hire_date_to` | string (date) | ❌ | Ngày vào làm đến | `2026-12-31` |
| `sort_by` | string | ❌ | `employee_code` \| `full_name` \| `department_name` \| `hire_date` (mặc định `employee_code`) | `hire_date` |
| `sort_order` | string | ❌ | `asc` \| `desc` (mặc định `asc`) | `desc` |

### **Response**

#### **Thành công (200 OK)**
```json
{
  "data": [
    {
      "id": 101,
      "employee_code": "SSV-0101",
      "full_name": "Nguyễn Văn A",
      "avatar_url": null,
      "email": "nguyenvana@saishunkansys.com",
      "phone": "0912345678",
      "department": { "id": 3, "name": "Phòng Phát triển phần mềm" },
      "job_title": { "id": 7, "name": "Software Engineer" },
      "office": "HN",
      "hire_date": "2022-03-01",
      "status": "ACTIVE",
      "role": "NORMAL",
      "created_at": "2022-02-20T02:00:00Z",
      "updated_at": "2026-01-10T08:30:00Z"
    }
  ],
  "meta": {
    "page": 1,
    "per_page": 20,
    "total": 214,
    "total_pages": 11
  }
}
```

**Ghi chú**: trường `role` (`employees.role`) chỉ trả về cho client có quyền `employee.view_role = FULL`; nếu không có quyền, trường này bị loại khỏi payload ở tầng service (không trả `null` để tránh lộ cấu trúc dữ liệu ngoài phạm vi quyền).

#### **Lỗi — Bad Request (400, RFC 7807)**
```json
{
  "type": "https://hrm.saishunkansys.com/errors/validation-error",
  "title": "Dữ liệu truy vấn không hợp lệ",
  "status": 400,
  "detail": "Tham số 'per_page' vượt giá trị tối đa cho phép (100).",
  "instance": "/api/v1/employees",
  "errors": [
    { "field": "per_page", "message": "per_page phải nhỏ hơn hoặc bằng 100" }
  ]
}
```

#### **Lỗi — Forbidden (403, RFC 7807)**
```json
{
  "type": "https://hrm.saishunkansys.com/errors/forbidden",
  "title": "Không có quyền truy cập",
  "status": 403,
  "detail": "Tài khoản không có quyền 'employee.list'.",
  "instance": "/api/v1/employees"
}
```

---

## 2. Xuất Excel danh sách nhân viên

### **Endpoint**
```
GET /api/v1/employees/export
```

### **Mô tả**
Xuất file Excel (`.xlsx`) danh sách nhân viên theo đúng điều kiện lọc/tìm kiếm hiện tại của màn hình (không áp dụng phân trang — xuất toàn bộ kết quả khớp điều kiện). Liên quan F-016 (Báo cáo Excel).

### **Chức năng liên quan**
- F-002-03, F-016

### **Quyền yêu cầu**
- `employee.export = FULL`

### **Tham số truy vấn**
- Giống hoàn toàn các tham số lọc/tìm kiếm/sắp xếp của [§1](#1-lấy-danh-sách-nhân-viên) (trừ `page`, `per_page`).

### **Response**

#### **Thành công, đồng bộ (200 OK)** — khi số dòng ≤ ngưỡng đồng bộ
```
Content-Type: application/vnd.openxmlformats-officedocument.spreadsheetml.sheet
Content-Disposition: attachment; filename="danh_sach_nhan_vien_20260709.xlsx"

<dữ liệu nhị phân>
```

#### **Thành công, bất đồng bộ (202 Accepted)** — khi số dòng vượt ngưỡng đồng bộ (NFR 3.1)
```json
{
  "data": {
    "job_id": "exp_8f3a2c",
    "status": "PROCESSING"
  }
}
```
Client theo dõi tiến trình qua `GET /api/v1/exports/:job_id` *(API dùng chung cho mọi loại export trong hệ thống — thiết kế chi tiết ở tài liệu API dùng chung cho báo cáo/export, ngoài phạm vi tài liệu này)*.

---

## Định nghĩa kiểu TypeScript

```typescript
// Request: lấy danh sách nhân viên
export interface GetEmployeeListRequest {
  page?: number;
  per_page?: number;
  q?: string;
  department_id?: number[];
  job_title_id?: number[];
  office?: 'HN' | 'HUE';
  status?: EmployeeStatus[];
  hire_date_from?: string; // YYYY-MM-DD
  hire_date_to?: string;   // YYYY-MM-DD
  sort_by?: 'employee_code' | 'full_name' | 'department_name' | 'hire_date';
  sort_order?: 'asc' | 'desc';
}

export type EmployeeStatus = 'ACTIVE' | 'ON_LEAVE' | 'RESIGNED';
export type EmployeeRole = 'NORMAL' | 'HR' | 'ADMIN';
export type OfficeCode = 'HN' | 'HUE';

// Item trả về trong danh sách (subset các trường phục vụ SCR-04)
export interface EmployeeListItem {
  id: number;
  employee_code: string;
  full_name: string;
  avatar_url: string | null;
  email: string;
  phone: string | null;
  department: { id: number; name: string };
  job_title: { id: number; name: string };
  office: OfficeCode;
  hire_date: string; // ISO 8601 date
  status: EmployeeStatus;
  role?: EmployeeRole; // chỉ có khi có quyền employee.view_role
  created_at: string;  // ISO 8601 datetime
  updated_at: string;  // ISO 8601 datetime
}

export interface PaginationMeta {
  page: number;
  per_page: number;
  total: number;
  total_pages: number;
}

export interface GetEmployeeListResponse {
  data: EmployeeListItem[];
  meta: PaginationMeta;
}

// RFC 7807 Problem Details
export interface ProblemDetails {
  type: string;
  title: string;
  status: number;
  detail?: string;
  instance?: string;
  errors?: Array<{ field: string; message: string }>;
}
```

---

## Bảng định tuyến Fastify

| Loại | Endpoint | Handler | Đường dẫn triển khai (dự kiến) |
|---|---|---|---|
| Danh sách | `GET /api/v1/employees` | `employeeController.getList` | `backend/src/modules/employee/employee.controller.ts` |
| Xuất Excel | `GET /api/v1/employees/export` | `employeeController.exportExcel` | `backend/src/modules/employee/employee.controller.ts` |
| Chi tiết | `GET /api/v1/employees/:id` | `employeeController.getDetail` | `backend/src/modules/employee/employee.controller.ts` |
| Tạo mới | `POST /api/v1/employees` | `employeeController.create` | `backend/src/modules/employee/employee.controller.ts` |
| Cập nhật | `PUT /api/v1/employees/:id` | `employeeController.update` | `backend/src/modules/employee/employee.controller.ts` |
| Chuyển trạng thái | `PUT /api/v1/employees/:id/status` | `employeeController.changeStatus` | `backend/src/modules/employee/employee.controller.ts` |
| Đặt lại mật khẩu | `POST /api/v1/employees/:id/reset-password` | `employeeController.resetPassword` | `backend/src/modules/employee/employee.controller.ts` |

---

## Tổng kết

### **Lưu ý khi triển khai**
1. **Xác thực**: mọi endpoint yêu cầu JWT hợp lệ (`Authorization: Bearer <token>`).
2. **Phân quyền**: kiểm tra mã quyền tương ứng ở tầng service (không chỉ ở FE) — theo NFR 3.2.
3. **Validate**: dùng schema Zod dùng chung cho query/body.
4. **Định dạng lỗi**: RFC 7807 (`application/problem+json`) cho mọi lỗi.
5. **Audit**: log truy vấn xuất Excel (ai xuất, khi nào, điều kiện lọc gì) để phục vụ kiểm toán dữ liệu nhân sự.

### **Tài liệu tham chiếu**
- Quy ước chung (đối chiếu, có override): `../../05_API_Design/00_Common_Specifications.md`
- Thiết kế màn hình: `../screen/01_Employee_List.md`
- Thiết kế chi tiết xử lý: `../detailed/01_Employee_List_Detailed_Design.md`
- Yêu cầu gốc: `../../01_Business_Process/requirements/HRM_Requirements.md`

---

**Tác giả**: HRM Design Team
**Cập nhật lần cuối**: 2026-07-09
