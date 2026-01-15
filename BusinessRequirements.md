# Yêu cầu nghiệp vụ → Kỹ thuật (Phase 1)

## Mục tiêu
Xây backend e-commerce Phase 1 (Must-have) phục vụ API cho frontend. Nguyên tắc: đơn giản, an toàn với bài toán tồn kho, hoàn thành trong 2 tuần.

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
- Giữ hàng 10-15 phút (Phức tạp)
- Last item (Bắt buộc xử lý automic)
- Sepay auto detect (khó phụ thuộc vào external)
- Email tracking link (Làm được, dùng token)

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
- Kiểm tra quantity <= inventory hiện tại, không reserve tại thời điểm add-to-cart

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

---
