# Tài liệu yêu cầu — HRM System

**Dự án:** SSV HRM (Human Resource Management)
**Phiên bản:** v2.0.0
**Ngày tạo:** 2026-07-06
**Cập nhật:** 2026-07-07

---

## 1. Tổng quan dự án

### 1.1 Bối cảnh & mục tiêu

Hệ thống HRM nội bộ của **SSV (Saishunkan Systems Vietnam)** phục vụ 2 văn phòng (Hà Nội, Huế), quản lý toàn bộ vòng đời nhân sự: hồ sơ, hợp đồng, chấm công (nghỉ phép/OT/WFH), lương, đào tạo/chứng chỉ, và mạng nội bộ. Hệ thống gồm **Web** (Admin/HR) và **App mobile** (nhân viên).

### 1.2 Scope

**Trong phạm vi:** back-end API và front-end Web (Admin/HR).
**Ngoài phạm vi:** ứng dụng App mobile (do đội khác phát triển — nhưng BE phục vụ cả App).

### 1.3 Stakeholders

| Vai trò | Định danh (enum) | Trách nhiệm |
|---|---|---|
| Nhân viên (Normal User) | `Role.NORMAL` | Đăng ký nghỉ/OT/WFH, xem lương, tham gia feed/game |
| Leader | `JobTitle.LEADER` | Duyệt cấp 1 |
| PM | `JobTitle.PM` | Duyệt cấp 2 |
| PMO | `JobTitle.PMO` | Duyệt nghỉ/OT (gate FE) |
| Director | `JobTitle.DIRECTOR` | Duyệt cấp 2, xem cây PM |
| HR (Hành chính–Nhân sự) | `Role.HR` | Quản lý hồ sơ, hợp đồng, lương, chứng chỉ |
| Admin | `Role.ADMIN` | Toàn quyền cấu hình, kích hoạt thiết bị |
| System | tài khoản hệ thống | Audit mặc định, marker auto-approve |

---

## 2. Yêu cầu chức năng

### 2.1 Danh sách chức năng

| Func ID | Chức năng | Module | Ưu tiên | Màn hình |
|---|---|---|---|---|
| F-001 | Đăng nhập 2 bước + OTP/2FA | M01 | Cao | SCR-01 |
| F-002 | Quản lý hồ sơ nhân viên (CRUD, lịch sử, mật khẩu) | M02 | Cao | SCR-04 |
| F-003 | Thuộc tính mở rộng động (EAV) | M03 | TB | SCR-15, SCR-04 |
| F-004 | Kích hoạt/quản lý thiết bị mobile | M04 | Cao | SCR-05 |
| F-005 | Đăng ký & duyệt nghỉ phép (2 cấp) | M05 | Cao | SCR-06 |
| F-006 | Nghỉ thai sản | M06 | TB | SCR-06 |
| F-007 | Đăng ký & duyệt tăng ca | M07 | Cao | SCR-07 |
| F-008 | Đăng ký & duyệt WFH | M08 | Cao | SCR-08 |
| F-009 | Lịch nghỉ lễ & xoay vòng trực nhật (shurei) | M09 | TB | SCR-09 |
| F-010 | Phát hành & thông báo lương/thưởng (PDF mật khẩu) | M10 | Cao | SCR-03 |
| F-011 | Quản lý khóa học/chứng chỉ + phê duyệt + trợ cấp | M11 | TB | SCR-11, SCR-12 |
| F-012 | Quản lý hợp đồng & phụ lục | M12 | Cao | SCR-13, SCR-14 |
| F-013 | Mạng nội bộ (post/comment/like) | M13 | Thấp | (App) |
| F-014 | Game & bảng xếp hạng | M14 | Thấp | (App) |
| F-015 | Thông báo đẩy (FCM) + cron tự động | M15 | TB | MainLayout (chuông) |
| F-016 | Báo cáo Excel (chấm công, thống kê) | M16 | Cao | nút Export |
| F-017 | Cấu hình ứng dụng | M17 | Thấp | SCR-10 |
| F-018 | Chấm công vân tay & quản lý thời gian | M18 | Cao | SCR-17, SCR-18 |
| F-019 | Đánh giá Quý (tiêu chí, kỳ, chấm điểm, kết quả) | M19 | Cao | SCR-19..22 |
| F-020 | Thưởng dự án | M20 | TB | SCR-23 |
| F-021 | Sơ đồ Tổ chức & Quản lý dự án | M21 | TB | SCR-24, SCR-25 |
| F-022 | Quản lý tài sản/thiết bị | M22 | TB | SCR-26 |
| F-023 | Checklist nhân viên mới/nghỉ việc | M23 | Thấp | SCR-27, SCR-29 |
| F-024 | Tính lương tự động | M24 | Cao | SCR-28 |
| F-025 | Quản lý phân quyền tài khoản | M25 | Cao | SCR-30

### 2.2 Chi tiết chức năng (tiêu biểu)

#### F-001: Đăng nhập + OTP/2FA
- **Use case:** nhân viên/Admin đăng nhập → xác thực OTP (TOTP).
- **User story:** *"Là nhân viên, tôi muốn đăng nhập bằng email/mật khẩu rồi xác thực OTP để đảm bảo an toàn."*
- **Input:** email, password, type ('app'|'web'), otpCode. **Output:** access token + refresh token + userData.
- **Business rule:** BR-AUTH-001..005 (web chỉ Admin/HR; bắt buộc 2FA lần đầu).

#### F-005: Nghỉ phép
- **User story:** *"Là nhân viên, tôi muốn đăng ký nghỉ phép và được leader/manager duyệt; hệ thống tự trừ ngày phép khi duyệt cấp 2."*
- **Input:** category, ngày/giờ bắt đầu-kết thúc, lý do, liên hệ. **Output:** đơn nghỉ (`LeaveMain`) + chi tiết theo ngày (`LeaveDetail`).
- **Business rule:** BR-LEAVE-001..011 (xem `../../06_Detailed_Design/03_Leave_Detailed_Design.md`).

#### F-010: Lương
- **User story:** *"Là HR, tôi muốn upload PDF lương có mật khẩu và gửi email thông báo cho nhân viên."*
- **Input:** PDF payslip, tháng, loại (lương/thưởng). **Output:** bản ghi `Salary` + email thông báo.
- **Business rule:** BR-SAL-001..004.

> **Chi tiết đầy đủ các chức năng còn lại (F-002..F-017):** xem thiết kế chi tiết tại `../../06_Detailed_Design/`. Business rule đầy đủ được mô tả trong `../../06_Detailed_Design/`.

#### F-018: Chấm công vân tay & quản lý thời gian

- **Tổng quan:** *"Quản lý check-in/check-out của nhân viên; thiết lập quy tắc đi muộn và quên đăng ký."*
- **Chi tiết:**
  1. **Màn hình Cài đặt thời gian & Quy tắc (Settings)**
     - Thiết lập ca làm việc (giờ bắt đầu / kết thúc).
     - Thiết lập quy tắc đi muộn: quá X phút tính là muộn; quá Y phút tự động chuyển thành nghỉ nửa ngày (2h–4h).
     - Thiết lập quy tắc phạt / tự động đăng ký nghỉ: VD muộn > 3 lần/tuần thì tự động trừ vào quỹ phép.
     - Ví dụ: cài đi muộn là quá 6 phút, giờ bắt đầu 8:30 → check-in lúc 8:37 được tính là đi muộn; đi muộn 3 ngày trong 1 tuần thì bị khấu trừ 2h phép.
     - Thiết lập quy tắc "Quên đăng ký": định nghĩa thời hạn phải nộp đơn giải trình hoặc đơn xin nghỉ bù nếu vắng mặt không lý do.
  2. **Màn hình Quản lý dữ liệu Check-in**
     - Tìm kiếm theo: tên, mã NV, phòng ban, ngày (From–To).
     - Lọc theo trạng thái: bình thường, đi muộn, vắng mặt không lý do (quên đăng ký), lỗi dữ liệu.
     - Chỉnh sửa check-in thủ công (dành cho HR có quyền, để sửa lỗi hệ thống).

#### F-019: Đánh giá Quý (tiêu chí, kỳ, chấm điểm, kết quả)

- **Tổng quan:** *"Quản lý thiết lập kỳ đánh giá, cập nhật và phản hồi kết quả đánh giá."*
- **Chi tiết:**
  1. **Màn hình Cài đặt Đánh giá (Settings)**
     - Thiết lập bộ tiêu chí đánh giá và thang điểm (VD: thang điểm 5, thang điểm 10); hỗ trợ thêm mục nhập text kèm tiêu đề, có checkbox (có thể thêm nhiều mục).
     - Dựa vào người quản lý trong hồ sơ nhân viên để xác định leader; điểm được thiết lập sẵn cho từng mục (VD: mục "Tuân thủ flow Git" min 0, max 5) — nhân viên tự chọn điểm trong khoảng (0 / 3 / 5...).
     - Phân quyền và gán bộ tiêu chí theo vai trò (bộ tiêu chí cho PM, Dev, Tester...).
  2. **Màn hình Quản lý Kỳ đánh giá (Review Cycles)**
     - Tạo kỳ đánh giá mới (VD: Đánh giá Quý 1/2024); thiết lập thời gian bắt đầu và kết thúc (deadline nộp tự đánh giá, deadline leader chấm điểm).
  3. **Màn hình Thực hiện Đánh giá (dành cho Leader/PM)**
     - Danh sách nhân viên cấp dưới cần đánh giá.
     - Giao diện chấm điểm, viết nhận xét và duyệt kết quả tự đánh giá của nhân viên.
  4. **Màn hình Quản lý Kết quả tổng hợp (dành cho HR)**
     - Theo dõi tiến độ đánh giá của toàn công ty (phòng nào chưa xong, ai chưa nộp).
     - Danh sách kết quả: tìm kiếm theo ngày, chức vụ, phòng ban, trạng thái (chờ đánh giá, đã hoàn thành, có khiếu nại).
     - Chỉnh sửa kết quả (dành cho admin cấp cao khi có quyết định thay đổi cuối cùng).

#### F-020: Thưởng dự án

- **Tổng quan:** *"Quản lý và tạo thưởng dự án."*
- **Chi tiết:**
  1. **Danh sách thưởng dự án**
     - Hiển thị danh sách các thưởng dự án đã tạo; tìm kiếm theo tên dự án, mã nhân viên, tên nhân viên, quý thưởng.
  2. **Tạo mới / Chỉnh sửa / Xóa quyết định thưởng**
     - Nhập thông tin: tên dự án, tổng ngân sách thưởng.
     - Thêm danh sách nhân viên nhận thưởng (import từ Excel hoặc chọn tay từ hệ thống) kèm số tiền cụ thể cho từng người.
     - Kiểm tra tổng tiền người nhận ≤ tổng ngân sách; nếu vượt thì cảnh báo.

#### F-021: Sơ đồ Tổ chức & Quản lý dự án

- **Tổng quan:** *"Quản lý danh sách sơ đồ tổ chức theo tháng và thông tin dự án."*
- **Chi tiết:**
  1. **Danh sách sơ đồ tổ chức**
     - Hiển thị danh sách sắp xếp theo tháng, có trạng thái đã public / chưa public (mặc định hiển thị tháng hiện tại); có nút Xem.
  2. **Tạo mới sơ đồ tổ chức**
     - Hiển thị theo dữ liệu hiện tại; có nút Public và Cập nhật; bấm Public nếu sơ đồ đã đúng.
  3. **Danh sách dự án**
     - Hiển thị danh sách dự án (No, tên dự án, người phụ trách, số thành viên, ngày bắt đầu, ngày kết thúc, nút chỉnh sửa).
  4. **Chỉnh sửa dự án**
     - Tên dự án, người phụ trách, ngày bắt đầu, ngày kết thúc, danh sách thành viên (có nút thêm thành viên; mỗi thành viên có thể set ngày bắt đầu / kết thúc riêng, để trống thì theo ngày bắt đầu / kết thúc của dự án).

#### F-022: Quản lý tài sản/thiết bị

- **Tổng quan:** *"Quản lý các thiết bị trong công ty."*
- **Chi tiết:**
  1. **Danh sách thiết bị**
     - Tìm kiếm theo: mã thiết bị, tên thiết bị, nhãn hiệu, ngày mua, người sử dụng.
     - Hiển thị toàn bộ danh sách thiết bị (mã thiết bị, tên thiết bị, nhãn hiệu, ngày mua, người sử dụng, tình trạng thiết bị [tốt / đã hỏng], nút chỉnh sửa, nút Xem lịch sử dùng).
  2. **Thêm thiết bị**
     - Mã thiết bị, tên thiết bị, nhãn hiệu, ngày mua, hình ảnh, người sử dụng, tình trạng thiết bị (tốt / đã hỏng), ghi chú.
  3. **Chi tiết lịch sử dùng thiết bị**
     - Hiển thị 2 chế độ: dạng danh sách hoặc dạng flow (mặc định dạng flow).
     - VD dạng flow: ChinhHV (20/12/2020 – 25/10/2023) → LuanDD (26/10/2023 – 25/11/2025) → Kho (25/11/2025 → hiện tại).
  4. **Chỉnh sửa người sử dụng thiết bị**
     - Chọn người sử dụng mới, ghi chú, ngày bắt đầu (khi chuyển người sử dụng, người dùng hiện tại tự động được cập nhật ngày kết thúc sử dụng).

#### F-023: Checklist nhân viên mới/nghỉ việc

- **Tổng quan:** *"Quản lý checklist cho nhân viên mới và nhân viên nghỉ việc."*
- **Chi tiết:**
  1. **Màn hình cài đặt checklist (nhân viên mới / nghỉ việc)**
     - Thêm các mục cho checklist như: đào tạo nhân viên mới, cấp thiết bị, bàn giao thiết bị...
     - Có instance checklist gắn từng nhân viên để theo dõi tiến độ.

#### F-024: Tính lương tự động

- **Tổng quan:** *"Thêm màn hình mới để tính lương tự động."*
- **Chi tiết:**
  1. **Danh sách kỳ lương**
     - Hiển thị danh sách các kỳ lương (tìm kiếm theo tháng, mã nhân viên, tên nhân viên).
     - Nút Xác nhận nếu là kỳ lương chưa xác nhận; nút Send nếu ở trạng thái accepted; sau khi send thành công hiển thị nhãn "đã send", thất bại hiển thị nút Re-send.
  2. **Nút tạo danh sách kỳ lương** (chỉ người phụ trách mới có; hiển thị trên menu trái hoặc dashboard)
     - Khi bấm sẽ tự động tính và tạo kỳ lương mới nhất; mỗi bảng lương có một mật khẩu xem riêng.
     - Công thức tính lương sẽ bổ sung sau — để TODO cho phần tính lương tự động.
  3. **Xác nhận kỳ lương**
     - Khi bấm nút xác nhận, hiển thị chi tiết từng bảng lương của mỗi người để người phụ trách kiểm tra và xác nhận; sau khi xác nhận toàn bộ, kỳ lương chuyển sang trạng thái accepted.
  4. **Gửi (Send)**
     - Bấm Send sau khi kỳ lương được accepted để gửi email (đính kèm mật khẩu xem bảng lương) và push thông báo kỳ lương mới cho mọi người.

#### F-025: Quản lý phân quyền tài khoản
- **Tổng quan:** *"quản lý phần quyền tài khoản theo role, group để hiển thị hoặc cho phép sử dụng tính năng theo tài khoản."*
- **Chi tiết:**
  1. **Danh sách phân quyền tài khoản**
     - Hiển thị danh sách tài khoản có các thông tin: No, mã nhân viên, Tên nhân viên, tài khoản, danh sách role và group quyền, button chỉnh sửa quyền có tìm kiếm theo mã nhân viên, tên nhân viên, tên role, tên group
    1.1 **Tạo mới và chỉnh sửa quyền**
      - Hiển thị danh sách role của tài khoản (có thể thêm hoặc xóa role)
      - Hiển thị danh sách group của tài khoản (có thể thêm hoặc xóa group)
  2. **Danh sách role**
     - Hiển thị danh sách các role, tên role, chỉnh sửa, xóa
    2.1 **Tạo mới và chỉnh sửa role**
      - Tên role, setting role dạng json (vd tham khảo, bạn có thể thay đổi để phù hợp: {"employee":{"list":"FULL","create":"FULL","edit":"FULL"}})
  3. **Danh sách group quyền**
     - Hiển thị danh sách các group quyền, tên group, chỉnh sửa, xóa
    3.1 **Tạo mới và chỉnh sửa group quyền**
     - Tên group, danh sách role (có thể thêm hoặc xóa role), danh sách tài khoản (có thể thêm hoặc xóa tài khoản)

---

## 3. Yêu cầu phi chức năng

### 3.1 Hiệu năng
- Connection pool Prisma cấu hình theo tải (giới hạn kết nối MySQL 8).
- Request timeout hợp lý ở FE; body limit BE (mặc định 5MB, cấu hình được).
- Có index trên các cột khóa ngoại/`employeeCode` để bảo đảm hiệu năng khi dữ liệu lớn.
- Báo cáo Excel/PDF là tác vụ nặng — xử lý async/queue khi cần.
- **Mục tiêu:** Response API < 1s (p95) cho CRUD; báo cáo chạy async.

### 3.2 Bảo mật
- Đăng nhập 2 bước bcrypt + TOTP 2FA; JWT access token (15m) + refresh token (cookie httpOnly, SameSite Strict).
- RBAC guard tập trung; kiểm tra quyền và ownership ở tầng service cho mọi endpoint nhạy cảm.
- Các màn hình đều có mã role, các button hoạt động như chỉnh sửa, xóa, xem, export cũng có mã role riêng. khi hiển thị trên menu hay layout màn hình cần kiểm tra role để hiển thị
> Chi tiết: `../02_Technical_Design/05_Security_Design.md`

### 3.3 Khả dụng (Availability)
- Triển khai nhiều instance sau load balancer → chịu tải & tự phục hồi; có health check endpoint.
- **Mục tiêu:** Uptime 99.5%; định nghĩa RTO/RPO kèm chính sách backup MySQL.

### 3.4 Bảo trì
- Chuẩn lint/format thống nhất cho BE & FE (TypeScript). Log tập trung (pino) chuyển tới hệ thống log.
- Có kiểm thử tự động (Vitest) cho service/logic nghiệp vụ trọng yếu.
- Message/nhãn hiển thị tiếng Việt cần giữ khi refactor.

### 3.5 Đa ngôn ngữ / Localization
- 3 locale: vi (mặc định), en, ja. Timezone Asia/Ho_Chi_Minh. Ngày giờ lưu ISO 8601 (UTC), hiển thị localized theo locale người dùng.

### 3.6 Logging & Observability
- Logger (pino) ghi request/response/business/exception + redaction dữ liệu nhạy cảm (prod); chuyển log tới hệ thống quan trắc tập trung.

---

## 4. Ràng buộc

### 4.1 Ràng buộc kỹ thuật
- Công nghệ cố định: **Fastify + TypeScript / Prisma + MySQL 8** (BE), **Vue 3 + Quasar + TypeScript** (FE); API **REST** (lỗi theo RFC 7807), validation **Zod** dùng chung.
- HTTP status chuẩn (200/201/204/400/401/403/404/409/422/500); ngày giờ ISO 8601.
- Đa ngôn ngữ vi/en/ja; Timezone Asia/Ho_Chi_Minh (+7).
- Chuỗi hiển thị & message lỗi bằng tiếng Việt.
- Trong front-end setting dạng modules riêng biết để hiển thị menu và chức năng theo modules.
    VD: module nhân sự sẽ có các setting router, menu trong source của module nhân sự. router và menu tổng sẽ tự động import setting trong module để hiển thị
- Dựa theo data role khi login tài khoản để ẩn hiển tính năng

### 4.2 Ràng buộc lịch trình
Chưa xác định trong phạm vi tài liệu này; sẽ bổ sung theo kế hoạch dự án.

### 4.3 Ràng buộc ngân sách
Chưa xác định trong phạm vi tài liệu này.

---

## 5. Tiền đề (Assumptions)

- Có tài khoản nhân viên đang hoạt động (chưa bị xóa mềm) trong hệ thống.
- Web chỉ dành cho role Admin/HR; App mobile dành cho toàn bộ nhân viên.
- Tích hợp ngoài (lưu trữ file, FCM, Google Calendar, SMTP) yêu cầu credential được cấu hình qua biến môi trường.

---

## 6. Thuật ngữ

| Thuật ngữ | Định nghĩa |
|---|---|
| Shurei | Trực nhật/vệ sinh văn phòng luân phiên |
| WFH | Work From Home |
| Block-time | Các khung giờ nghỉ/WFH hợp lệ (`DATA_LEAVE_BLOCK_TIME`) |
| send_kbn | Trạng thái gửi lương (nhiều bước phát hành) |
| EAV | Entity-Attribute-Value (thuộc tính mở rộng động) |
| privileged (cert) | Admin/HR hoặc người phê duyệt chứng chỉ được chỉ định |
| TOTP | Time-based One-Time Password (2FA) |

---

## 7. Tài liệu tham khảo

- Thiết kế kỹ thuật: `../../02_Technical_Design/`
- Thiết kế CSDL: `../../03_Database_Design/`
- Thiết kế màn hình: `../../04_Screen_Design/`
- Thiết kế API: `../../05_API_Design/`
- Thiết kế chi tiết: `../../06_Detailed_Design/`

---

## Phê duyệt

| Vai trò | Họ tên | Ngày duyệt | Chữ ký |
|---|---|---|---|
| Project Owner | | | |
| Project Manager | | | |

---

## Lịch sử thay đổi

| Phiên bản | Ngày | Nội dung |
|---|---|---|
| v1.0.0 | 2026-07-06 | Bản đầu |
| v2.0.0 | 2026-07-07 | Thiết kế lại theo stack TypeScript (Prisma+MySQL8, Fastify+TS, Vue3+Quasar+TS, REST/RFC7807, Zod) |
