# File: `docs/TechnicalReport.md`

# Báo cáo kỹ thuật — E-commerce Backend Phase 1

## 1. Tổng quan & Scope
- Mục tiêu: triển khai backend API phục vụ catalog, cart, reservation (10–15 phút), checkout (COD), email tracking, admin orders.
- Must-have: như mục `ecommerce/BusinessRequirements.md`.
- Nice-to-have deferred: SePay thực, auth, admin product CRUD.

## 2. Phân tích yêu cầu & Gap Analysis
- Yêu cầu email khách: SePay tự động cập nhật — chuyển sang mock webhook trong Phase 1.
- Yêu cầu không yêu cầu auth — chấp nhận rủi ro admin API chưa bảo mật, ghi rõ trong report.
- Cam kết hoàn thiện: 85–95% chức năng core (catalog, cart, reservation, checkout COD, email tracking, admin order). SePay hoàn chỉnh và auth là phần cut-scope nếu thời gian thiếu.

## 3. ERD (tóm tắt)
- Bảng chính:
    - `product` (id, title, description, category_id, created_at, updated_at)
    - `sku` (id, product_id, attributes JSON, price_cents, currency, inventory_count, version)
    - `cart` (id, created_at, updated_at)
    - `cart_item` (cart_id, sku_id, quantity, unit_price)
    - `inventory_reservation` (id, sku_id, quantity, reservation_key, expires_at, status, order_id nullable)
    - `order` (id, total_cents, payment_method, status, shipping_address JSON, created_at)
    - `order_item` (order_id, sku_id, quantity, unit_price)
    - `tracking_token` (id, order_id, token_hash, expires_at)

Giải thích: Tách `sku` để quản lý inventory theo variant. `inventory_reservation` tách để tracking TTL và release dễ dàng.

## 4. LLD — API Endpoints (method, URL, mô tả)
Public:
- `GET /api/products` — list products (page,size,sort,minPrice,maxPrice,category,q)
- `GET /api/products/{productId}` — product + skus
- `GET /api/skus/{skuId}` — detail sku
- `POST /api/cart` — tạo/cập nhật item trong cart (body: cartId?, skuId, quantity)
- `PATCH /api/cart/{cartId}/items/{skuId}` — cập nhật qty
- `DELETE /api/cart/{cartId}/items/{skuId}`
- `GET /api/cart/{cartId}`
- `POST /api/checkout` — checkout (cartId, shippingAddress, paymentMethod) → reserve + create order
- `GET /track/{orderId}?token={token}` — tracking view

Admin:
- `GET /api/admin/orders` — list (filter status,date)
- `GET /api/admin/orders/{orderId}`
- `PATCH /api/admin/orders/{orderId}/status` — update status

## 5. Sequence Diagrams (mô tả ngắn)
- Checkout (quan trọng):
    1. Client gọi `POST /api/checkout` với `cartId`.
    2. Server bắt transaction; với mỗi sku: `SELECT ... FOR UPDATE`.
    3. Kiểm tra inventory, decrement inventory_count, tạo `inventory_reservation` với `expires_at`.
    4. Tạo `order` và `order_items`, commit.
    5. Gửi email xác nhận với tracking token.
- Release expired reservation:
    - Scheduled job quét reservation expired → restore sku.inventory_count → mark EXPIRED.

## 6. Testing plan
- Unit tests cho services, repo.
- Integration tests (Testcontainers) cho checkout + reservation flow.
- Concurrency test: simulate N parallel checkout for last-item SKU; assert chỉ 1 success.

## 7. Deployment & config
- Configs: reservation.ttl (default 15m), datasource, mail provider.
- Docker Compose: Postgres, Redis, MailHog.

## 8. Risk & Mitigation
- Concurrency risk: dùng DB row lock `FOR UPDATE` hoặc atomic update.
- Time risk: ưu tiên core flows; mock SePay.

## 9. Deliverables
- Source code trên GitHub, README, migrations/seed, Postman/Swagger, docs/TechnicalReport.md, ERD diagram (image thêm vào docs nếu có).

---
