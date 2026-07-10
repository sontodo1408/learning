# 01 Danh sách nhân viên

**Ngày tạo**: 2026-07-09
**Phiên bản**: v1.0.0
**Căn cứ yêu cầu**: `../../01_Business_Process/requirements/HRM_Requirements.md` — F-002 (M02, Ưu tiên Cao)
**Màn hình**: SCR-04
**Phase**: Phase 1 (Iteration 1 — Nhân sự cơ bản)

---

## Mục lục

1. [Tổng quan màn hình](#tổng-quan-màn-hình)
2. [Bố cục màn hình](#bố-cục-màn-hình)
3. [Chi tiết các thành phần màn hình](#chi-tiết-các-thành-phần-màn-hình)
4. [Đặc tả chức năng](#đặc-tả-chức-năng)
5. [Yêu cầu dữ liệu](#yêu-cầu-dữ-liệu)
6. [Chuyển màn hình](#chuyển-màn-hình)
7. [Tài liệu liên quan](#tài-liệu-liên-quan)
8. [Xác nhận tính nhất quán](#xác-nhận-tính-nhất-quán)
9. [Checklist review](#checklist-review)
10. [Lịch sử cập nhật](#lịch-sử-cập-nhật)

---

## Tổng quan màn hình

### **Thông tin cơ bản**

| Mục | Nội dung |
|---|---|
| Màn hình ID | SCR-04 |
| Tên màn hình | Danh sách nhân viên |
| URL | `/employees` |
| Module | M02 — Quản lý hồ sơ nhân viên |
| Đối tượng sử dụng | `Role.HR`, `Role.ADMIN` (Web chỉ dành cho Admin/HR — theo mục 5 Tiền đề của HRM_Requirements.md) |
| Thiết bị hỗ trợ | PC (Web Admin/HR) |

### **Mục đích**

Cho phép HR/Admin tra cứu, lọc, tìm kiếm toàn bộ nhân viên trong công ty (2 văn phòng Hà Nội, Huế) và là điểm truy cập vào các thao tác quản lý hồ sơ (xem chi tiết, tạo mới, sửa, đặt lại mật khẩu, chuyển trạng thái nghỉ việc).

### **Phạm vi Phase 1**

- Xem danh sách nhân viên có phân trang.
- Tìm kiếm nhanh theo mã nhân viên / họ tên / email.
- Lọc theo phòng ban, chức vụ, văn phòng, trạng thái làm việc.
- Sắp xếp theo các cột chính.
- Điều hướng sang màn tạo mới / chi tiết / sửa nhân viên.
- Xuất Excel danh sách theo điều kiện lọc hiện tại (F-016).
- Ẩn/hiện cột "Vai trò hệ thống" và các nút hành động theo mã quyền (role/permission) của người đăng nhập (F-025, NFR 3.2).

### **Dự kiến Phase 2**

- Bộ lọc nâng cao theo thuộc tính mở rộng (EAV — F-003).
- Import Excel hàng loạt.
- Xem nhanh (quick view) hồ sơ nhân viên dạng side panel.

---

## Bố cục màn hình

### **Cấu trúc tổng thể**

```
┌─────────────────────────────────────────────────────────────────┐
│ AppHeader (chung toàn hệ thống)                                 │
├───────────┬───────────────────────────────────────────────────────┤
│           │  Danh sách nhân viên                    [+ Thêm mới] [Xuất Excel] │
│           │  ┌─────────────────────────────────────────────────┐ │
│           │  │ [🔍 Tìm theo mã NV / họ tên / email...........] │ │
│  AppSidebar│  │ [Phòng ban ▾] [Chức vụ ▾] [Văn phòng ▾] [Trạng thái ▾] │ │
│  (chung)  │  └─────────────────────────────────────────────────┘ │
│           │  ┌─────────────────────────────────────────────────┐ │
│           │  │ Mã NV │Ảnh│Họ tên│Phòng ban│Chức vụ│Văn phòng│  │ │
│           │  │ Email │SĐT│Ngày vào làm│Trạng thái│Vai trò│Hành động│ │
│           │  │ ................. (dữ liệu phân trang) ......... │ │
│           │  └─────────────────────────────────────────────────┘ │
│           │  [ ‹ 1 2 3 ... 10 › ]           Hiển thị 20/mỗi trang│
└───────────┴───────────────────────────────────────────────────────┘
```

### **Đáp ứng đa kích thước**

- **PC (≥ 1280px)**: bố cục như trên, `q-table` hiển thị đầy đủ cột.
- **Màn hình nhỏ hơn (≤ 1279px, tablet ngang)**: `AppSidebar` thu gọn về icon; các cột phụ (Email, SĐT, Ngày vào làm) ẩn vào cột "..." mở rộng (`q-table` `dense` + `column visibility` do người dùng bật/tắt).
- Không hỗ trợ độ rộng dưới 768px ở Phase 1 (đây là màn hình quản trị nội bộ, dùng trên PC).

---

## Chi tiết các thành phần màn hình

### **1. Thanh tiêu đề trang**

**Component**: `q-toolbar` + `q-toolbar-title`

| Thành phần | Nội dung | Ghi chú |
|---|---|---|
| Tiêu đề trang | "Danh sách nhân viên" | i18n key `employee.list.title` |
| Nút "Thêm mới" | `q-btn` icon `add`, màu primary | Hiện khi có quyền `employee.create = FULL` |
| Nút "Xuất Excel" | `q-btn` icon `file_download`, outline | Hiện khi có quyền `employee.export = FULL`; áp dụng bộ lọc hiện tại |

### **2. Thanh tìm kiếm & bộ lọc**

**Component**: `q-card` chứa `q-input` + `q-select`

| Thành phần | Loại | Nội dung | Bắt buộc | Ghi chú |
|---|---|---|---|---|
| Ô tìm kiếm | `q-input` (debounce 400ms) | Tìm theo mã NV / họ tên / email | Không | Placeholder: "Tìm theo mã nhân viên, họ tên, email..." |
| Lọc Phòng ban | `q-select` (options API) | Danh sách phòng ban | Không | Nhiều lựa chọn (`multiple`) |
| Lọc Chức vụ | `q-select` (options API) | Danh sách chức vụ | Không | Nhiều lựa chọn |
| Lọc Văn phòng | `q-select` | Hà Nội / Huế | Không | Đơn lựa chọn |
| Lọc Trạng thái | `q-select` | Đang làm việc / Tạm nghỉ (thai sản, không lương) / Đã nghỉ việc | Không | Mặc định: "Đang làm việc" |
| Nút "Xóa lọc" | `q-btn` flat | Đặt lại toàn bộ điều kiện lọc | - | - |

### **3. Bảng danh sách nhân viên**

**Component**: `q-table` (`flat`, `bordered`, server-side pagination `binary-state-sort`)

| Cột | Nội dung | Sắp xếp | Ghi chú |
|---|---|---|---|
| Mã NV | `employee_code` | ✅ | Link sang màn chi tiết |
| Ảnh đại diện | `avatar_url` | ❌ | `q-avatar`, hiển thị chữ cái đầu nếu không có ảnh |
| Họ và tên | `full_name` | ✅ | - |
| Phòng ban | `department_name` | ✅ | - |
| Chức vụ | `job_title_name` | ❌ | - |
| Văn phòng | `office` | ❌ | Badge "Hà Nội" / "Huế" |
| Email | `email` | ❌ | Ẩn ở độ phân giải nhỏ |
| Số điện thoại | `phone` | ❌ | Ẩn ở độ phân giải nhỏ |
| Ngày vào làm | `hire_date` | ✅ | Định dạng theo locale (`dd/MM/yyyy` cho vi) |
| Trạng thái | `status` | ❌ | `StatusBadge`: xanh (Đang làm việc) / cam (Tạm nghỉ) / xám (Đã nghỉ việc) |
| Vai trò hệ thống | `role` | ❌ | Chỉ hiển thị khi người xem có quyền `employee.view_role = FULL` (mặc định chỉ Admin) |
| Hành động | - | ❌ | Xem/Sửa/Đặt lại mật khẩu/Vô hiệu hóa — mỗi nút gate theo mã quyền tương ứng (xem [Đặc tả chức năng](#đặc-tả-chức-năng)) |

**Phân trang**: `q-table` pagination server-side, mặc định 20 dòng/trang, tùy chọn 20/50/100.

---

## Đặc tả chức năng

### **1. Tìm kiếm nhanh**

**Hành vi**: Gõ từ khóa vào ô tìm kiếm, sau 400ms không gõ thêm sẽ tự động gọi lại danh sách với từ khóa đó.

**Trigger**: `input` trên ô tìm kiếm (debounce).

**Luồng xử lý**:
1. Cập nhật state `search`.
2. Reset về trang 1.
3. Gọi API `GET /api/v1/employees` với tham số `q`.
4. Cập nhật bảng.

**Ghi chú**: chi tiết triển khai xem tài liệu thiết kế chi tiết.

### **2. Lọc theo phòng ban / chức vụ / văn phòng / trạng thái**

**Hành vi**: Chọn một hoặc nhiều điều kiện lọc → bảng cập nhật ngay.

**Trigger**: `update:model-value` trên các `q-select`.

### **3. Sắp xếp**

**Hành vi**: Click vào tiêu đề cột có hỗ trợ sắp xếp → đổi chiều tăng/giảm.

**Trigger**: click header `q-table`.

### **4. Phân trang**

**Hành vi**: Chuyển trang hoặc đổi số dòng/trang → gọi lại API với `page`/`per_page` mới.

### **5. Hành động theo dòng (RBAC theo mã quyền)**

| Hành động | Mã quyền yêu cầu | Điều hướng / kết quả |
|---|---|---|
| Xem chi tiết | `employee.view = FULL` | Sang màn Chi tiết nhân viên (SCR-04 — chế độ xem) |
| Sửa | `employee.edit = FULL` | Sang màn Sửa nhân viên (SCR-04 — chế độ sửa) |
| Đặt lại mật khẩu | `employee.reset_password = FULL` | Hộp thoại xác nhận → gọi API, gửi mật khẩu tạm qua email |
| Vô hiệu hóa (nghỉ việc) | `employee.delete = FULL` | Hộp thoại xác nhận (`q-dialog`, có ô nhập ngày nghỉ việc) → chuyển `status` sang "Đã nghỉ việc" |

**Nguyên tắc chung**: nút không có quyền tương ứng thì **ẩn hoàn toàn**, không hiển thị dạng disabled (tránh lộ thông tin về sự tồn tại chức năng ngoài phạm vi quyền — theo NFR 3.2 và `02_Technical_Design/05_Security_Design.md`).

### **6. Xuất Excel**

**Hành vi**: Xuất toàn bộ danh sách theo điều kiện lọc/tìm kiếm hiện tại ra file Excel (F-016).

**Trigger**: click nút "Xuất Excel".

**Luồng xử lý**:
1. Gửi yêu cầu `GET /api/v1/employees/export` kèm toàn bộ query hiện tại.
2. Hiển thị trạng thái "Đang tạo file..." (vì NFR 3.1 yêu cầu xử lý báo cáo async khi dữ liệu lớn).
3. Nhận file và tự động tải xuống, hoặc nhận link tải khi xử lý async xong.

**Ghi chú**: chi tiết luồng async xem tài liệu thiết kế chi tiết.

---

## Yêu cầu dữ liệu

### **Nguồn dữ liệu**

- Bảng dữ liệu (tham chiếu — thiết kế DB chính thức sẽ hoàn thiện ở `docs/03_Database_Design/` theo đúng thứ tự Phase; danh sách trường dưới đây là input cho phase đó): `employees`, `departments`, `job_titles`.
- API: `GET /api/v1/employees` (xem `../api/01_Employee_API.md`).

### **Các trường hiển thị**

| Trường hiển thị | Nguồn dữ liệu | Kiểu | Ghi chú |
|---|---|---|---|
| Mã NV | `employees.employee_code` | string | Duy nhất, ví dụ `SSV-0001` |
| Ảnh đại diện | `employees.avatar_url` | string \| null | - |
| Họ và tên | `employees.full_name` | string | - |
| Phòng ban | `departments.name` (qua `employees.department_id`) | string | - |
| Chức vụ | `job_titles.name` (qua `employees.job_title_id`) | string | - |
| Văn phòng | `employees.office` | enum(`HN`,`HUE`) | - |
| Email | `employees.email` | string | - |
| Số điện thoại | `employees.phone` | string \| null | - |
| Ngày vào làm | `employees.hire_date` | date | ISO 8601, hiển thị localized |
| Trạng thái | `employees.status` | enum(`ACTIVE`,`ON_LEAVE`,`RESIGNED`) | - |
| Vai trò hệ thống | `employees.role` | enum(`NORMAL`,`HR`,`ADMIN`) | Tương ứng `Role` trong HRM_Requirements.md mục 1.3 |

### **Điều kiện lọc**

- Mặc định: `status = ACTIVE` (chỉ hiện nhân viên đang làm việc khi mới vào màn hình).
- Có thể lọc kết hợp: `department_id[]`, `job_title_id[]`, `office`, `status[]`, `q` (từ khóa), `hire_date_from`, `hire_date_to`.

### **Thứ tự sắp xếp**

- Mặc định: `employee_code` tăng dần.

---

## Chuyển màn hình

### **Màn hình nguồn**

- MainLayout (menu "Nhân sự" > "Danh sách nhân viên"): điều hướng trực tiếp.

### **Màn hình đích**

- SCR-04 (chế độ Tạo mới): click "Thêm mới".
- SCR-04 (chế độ Xem/Sửa): click "Xem"/"Sửa" trên một dòng.
- SCR-30 (Quản lý phân quyền — F-025): không điều hướng trực tiếp từ màn này ở Phase 1; chỉ hiển thị cột "Vai trò hệ thống" ở dạng đọc.

**Sơ đồ chuyển màn hình**:
```
MainLayout (menu Nhân sự) → [SCR-04] Danh sách nhân viên → [SCR-04] Chi tiết / Sửa / Tạo mới nhân viên
```

---

## Tài liệu liên quan

### **Tài liệu thiết kế chi tiết**
- `../detailed/01_Employee_List_Detailed_Design.md`

### **Tài liệu thiết kế API**
- `../api/01_Employee_API.md`

### **Tài liệu thiết kế CSDL**
- `../../03_Database_Design/` (sẽ bổ sung bảng `employees`, `departments`, `job_titles` theo đúng Phase)

### **Yêu cầu gốc**
- `../../01_Business_Process/requirements/HRM_Requirements.md` — F-002, F-025, NFR 3.2, NFR 3.5

---

## Xác nhận tính nhất quán

### **Đối chiếu với danh sách chức năng**
- [x] Mã chức năng: F-002 (M02)
- [x] Phạm vi triển khai Phase 1 đã rõ

### **Đối chiếu với thiết kế CSDL**
- [ ] Bảng `employees`, `departments`, `job_titles` — **[Cần xác nhận / To be confirmed]**: chưa có thiết kế CSDL chính thức tại `03_Database_Design/`; danh sách trường ở mục [Yêu cầu dữ liệu](#yêu-cầu-dữ-liệu) là đề xuất cần HR/PM xác nhận trước khi chốt DDL.

### **Đối chiếu với thiết kế API**
- [x] API sử dụng đã được thiết kế: `GET /api/v1/employees`, `GET /api/v1/employees/export`
- [x] Endpoint và tham số khớp với `../api/01_Employee_API.md`

---

## Checklist review

- [x] Màn hình ID, tên, URL đã ghi rõ
- [x] Mục đích và phạm vi Phase 1 rõ ràng
- [x] Bố cục tổng thể và đáp ứng đa kích thước đã mô tả
- [x] Thành phần màn hình đã liệt kê đầy đủ theo dạng bảng
- [x] Chức năng cơ bản đã mô tả (không đi sâu logic — xem tài liệu chi tiết)
- [x] Yêu cầu dữ liệu đã liệt kê, có ghi chú [Cần xác nhận] cho phần chưa chốt CSDL
- [x] Chuyển màn hình đã mô tả
- [x] Liên kết tới tài liệu API / thiết kế chi tiết đầy đủ

---

## Lịch sử cập nhật

| Ngày | Phiên bản | Nội dung |
|---|---|---|
| 2026-07-09 | v1.0.0 | Khởi tạo — màn hình đầu tiên được thiết kế chi tiết cho HRM (SCR-04, chế độ danh sách) |

---

**Tác giả**: HRM Design Team
**Cập nhật lần cuối**: 2026-07-09
