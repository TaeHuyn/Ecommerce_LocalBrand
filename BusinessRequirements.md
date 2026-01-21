# Yêu cầu nghiệp vụ → Kỹ thuật (Phase 1)

## Mục tiêu

Xây backend e-commerce Phase 1 (Must-have) phục vụ API cho frontend. Nguyên tắc: đơn giản, an toàn với bài toán tồn kho, hoàn thành trong 2 tuần.

## Nguyên tắc thiết kế (Design Principles)

- Ưu tiên tính đúng đắn của tồn kho hơn tốc độ (Correctness > Performance).
- Không giữ tồn kho ở giai đoạn add-to-cart để tránh dead stock.
- Chỉ giữ hàng khi người dùng có ý định mua thật (checkout).
- Mọi thao tác ảnh hưởng tồn kho phải nằm trong transaction.
- Thiết kế tối giản, có thể mở rộng cho Phase 2 (Payment gateway, Redis).

## Scope (Phase 1 — Phải có)

- Catalog sản phẩm (read-only) + SKU/variant
- Phân trang và lọc (giá, category, tìm kiếm)
- Giỏ hàng: thêm / sửa / xóa
- Kiểm tra tồn kho khi add/update cart
- Giữ tồn kho (reservation) khi checkout (TTL 10–15 phút)
- Tạo đơn hàng
- Phương thức thanh toán: COD (giai đoạn 1)
- Luồng trạng thái đơn hàng; admin API: list + update status
- Email xác nhận đơn + link tracking token-based

## Nice-to-have (Phase 1)

- Sepay tự động xác thực thanh toán thông qua webhook
- Redis-based distributed lock cho tồn kho
- Xác thực quản trị viên
- Tối ưu hóa hiệu suất cho chương trình khuyến mãi

## Out of scope (Phase 1)

- Tích hợp SePay thực tế (mock/manual)
- Auth người dùng / login
- Admin product CRUD (nhập liệu bằng DB/seed)

## Gap analysis (Email -> Thực tế)

- Giữ hàng 10–15 phút:
  - Phức tạp do cần xử lý timeout, race-condition và release tự động.
  - Cần đảm bảo hệ thống không giữ stock vĩnh viễn nếu user abandon checkout.

- Last item:
  - Bắt buộc xử lý atomic để tránh oversell.
  - Yêu cầu lock ở mức database row (pessimistic lock) hoặc atomic update.

## Acceptance Criteria (Tiêu chí nghiệm thu tối thiểu)

- API `GET /api/products` trả danh sách phân trang, hỗ trợ filter `minPrice,maxPrice,category,q`.
- API giỏ hàng: `POST /api/cart`, `PATCH /api/cart/{cartId}/items`, `DELETE /api/cart/{cartId}/items`, `GET /api/cart/{cartId}` hoạt động đúng.
- Khi `POST /api/checkout`, hệ thống phải reserve hàng (TTL 10–15 phút) cho toàn bộ cart trong một transaction; nếu thiếu hàng, trả lỗi và không tạo order.
- Scheduler phải giải phóng reservation đã hết hạn và phục hồi số lượng tồn kho.
- Tạo `Order` thành công cho phương thức COD và gửi email xác nhận kèm tracking link.
- Admin API `/api/admin/orders` (GET) và `/api/admin/orders/{id}/status` (PATCH) chỉ truy cập khi có token admin hợp lệ.

## Ghi chú cho developer

- Trong môi trường dev, `spring.jpa.hibernate.ddl-auto=update` có thể dùng để tạo nhanh schema; production cần migration scripts (Flyway/Liquibase).
- JWT secret và mail credentials không commit vào repo; sử dụng environment variables.

## Cam kết

- Hoàn thiện ~80-90% dự án
- Riêng Sepay: giả lập webhook

## Yêu cầu chức năng chi tiết

### Catalog (read-only)

- API: `GET /api/products`, `GET /api/products/{id}`, `GET /api/skus/{skuId}`
- SKU lưu giá, inventory, attributes (size/color)

### Pagination & Filter

- Hỗ trợ `page`, `size`, `sort`, `minPrice`, `maxPrice`, `categoryId`, `q`

### Cart

- Mô hình session-less: server trả `cartId` hoặc client giữ `cartId`
- Endpoints:
  - `POST /api/cart` (tạo/cập nhật hàng trong giỏ)
  - `PATCH /api/cart/{cartId}/items/{skuId}`
  - `DELETE /api/cart/{cartId}/items/{skuId}`
  - `GET /api/cart/{cartId}`

### Inventory check khi add/update

- Chỉ kiểm tra tồn kho, không reserve.
- Lý do:
  - Tránh tình trạng nhiều user add cart nhưng không checkout.
  - Giảm dead stock trong các đợt sale.
  - Phù hợp với traffic cao và hành vi người dùng thực tế.

### Inventory reservation khi checkout

- Khi checkout: tạo `InventoryReservation` (TTL 10–15 phút), decrement inventory trong transaction hoặc bằng lock
- Nếu thiếu tồn kho → trả lỗi
- Scheduled job giải phóng reservation hết hạn

### Order creation & Payment

- Tạo order sau reserve thành công
- COD: order tạo xong, trạng thái ban đầu `PLACED`
- SePay: mock cho Phase 1

### Tracking email

- Gửi email chứa tracking link `/track/{orderId}?token={token}` (token là JWT hoặc token random lưu hash trong DB)
- Link trả trạng thái đơn không cần login

### Admin APIs

- `GET /api/admin/orders`
- `GET /api/admin/orders/{orderId}`
- `PATCH /api/admin/orders/{orderId}/status`

## Data model (tóm tắt)

- `product`, `sku`, `cart`, `cart_item`, `inventory_reservation`, `order`, `order_item`, `tracking_token`

## Considerations kỹ thuật

- Dùng MySQL cho giao dịch mạnh, Redis optional cho reservation nhanh
- Chiến lược an toàn cho "last item": Pessimistic lock (`SELECT ... FOR UPDATE`) hoặc atomic DB update
- TTL cấu hình (mặc định 15 phút)
- Idempotency cho checkout

## Tài liệu thiết kế đính kèm

- ERD: xem Hình 1 – mô hình dữ liệu tổng thể cho Product, Variant, Cart, Reservation, Order.
- Sequence Diagram:
  - Hình 2: Luồng Checkout & Giữ hàng (Inventory Reservation).
  - Hình 3: Luồng Admin huỷ đơn & hoàn tồn kho.
  - Hình 4: Luồng Scheduler release reservation hết hạn.
