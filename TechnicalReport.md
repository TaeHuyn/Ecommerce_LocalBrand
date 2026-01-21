# Báo cáo kỹ thuật — E-commerce Backend Phase 1

Tài liệu này được chuẩn hoá theo yêu cầu trong phần "Assignment Instruction". Nội dung trình bày bằng tiếng Việt, bao gồm: phân tích yêu cầu, thiết kế hệ thống (ERD + LLD), mô tả các API quan trọng, flow checkout/reservation, kế hoạch kiểm thử, hướng dẫn triển khai và các deliverable cần nộp.

**Phiên bản tài liệu**: cập nhật lần cuối bởi dev: (repo snapshot)

## 1. Tóm tắt & Phạm vi (Scope)

- Mục tiêu: xây backend headless để phục vụ frontend cho catalog, cart, checkout (COD), reservation (TTL 10–15 phút), email tracking, admin order management.
- Scope Phase 1 (Must-have):
  - Catalog + SKU/variant (read-only)
  - Cart (add/update/delete)
  - Inventory reservation on checkout (10–15 phút TTL) và scheduler để release
  - Checkout → Tạo order (COD supported)
  - Email order confirmation + tracking link (no login required)
  - Admin APIs: list orders, update order status (JWT-protected)

## 2. Đánh giá yêu cầu & Gap Analysis

- So sánh Email khách và hiện trạng code:
  - Email yêu cầu: reservation 10–15 phút, last-item correctness, admin order APIs, tracking email — hiện tại code đã implement reservation table, reservation scheduler, checkout creates reservations and commits them. Admin JWT auth + admin endpoints present.
  - Gap hiện tại: SePay integration chưa thực hiện (phase 2), token refresh/revocation optional (we added blacklist option optionally), cần migration SQL và integration tests chưa đầy đủ.

- Đánh giá khả năng hoàn thiện: codebase hiện tại đáp ứng ~85–90% yêu cầu Phase 1 nếu hoàn thiện migration & tests. Thiếu: SePay, formal refresh-token, integration tests, infra secrets handling.

## 3. Thiết kế dữ liệu (ERD) — tóm tắt

- Các bảng chính hiện có trong codebase:
  - `products` (`Product` entity)
  - `product_variants` (`ProductVariant`)
  - `carts`, `cart_items`
  - `reservations` (Inventory reservation records)
  - `orders`, `order_items`
  - `blacklisted_tokens` (optional, cho logout)
  - `admin_user` / `account` (admin credentials)

## 3.5 Tổng quan thiết kế hệ thống

Hệ thống được thiết kế theo mô hình layered architecture:

- Controller: nhận request, validate input.
- Service: xử lý nghiệp vụ (order, inventory, reservation).
- Repository: thao tác DB.
- Scheduler: xử lý nghiệp vụ nền (release reservation).

Các luồng quan trọng đều được thiết kế theo hướng:

- Transactional
- Idempotent
- Fail-fast khi thiếu tồn kho
