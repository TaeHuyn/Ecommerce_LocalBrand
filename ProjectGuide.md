# File: `ecommerce/ProjectGuide.md`

# Hướng dẫn triển khai thực hiện dự án — Phase 1 (Tiếng Việt)

## Mục tiêu ngắn gọn
Hoàn thành backend API đáp ứng Acceptance Criteria trong 2 tuần: catalog, cart, reservation (10–15 phút), checkout (COD), order tracking email, admin order APIs.

## Stack đề xuất
- Java 17+, Spring Boot, Spring Data JPA, Maven
- DB: Postgres
- Cache/TTL: Redis (tùy chọn)
- Dev mail: MailHog
- Docker Compose để chạy Postgres + Redis + MailHog

## Checklist / Timeline (2 tuần)

Week 1
- Ngày 1: Đọc yêu cầu, chốt Scope, viết Technical Report khung
- Ngày 2: ERD, API list, sequence diagram cho checkout/reservation
- Ngày 3: Khung dự án (modules), Docker Compose, CI basic
- Ngày 4: CRUD read-only cho Product + SKU, migration + seed
- Ngày 5: Cart API + inventory check on add
- Ngày 6: Bảng `inventory_reservation` + API tạo reservation
- Ngày 7: Unit tests cho product/cart

Week 2
- Ngày 8: Implement checkout: reserve → create order (transaction + lock)
- Ngày 9: Admin order APIs + email tracking token
- Ngày 10: Background job giải phóng expired reservations
- Ngày 11: Integration tests (concurrency last-item)
- Ngày 12: Swagger/Postman, hoàn thiện README
- Ngày 13: Fix bug, chuẩn bị demo
- Ngày 14: Hoàn thiện technical report & push repo

## Lưu ý kỹ thuật quan trọng
- Chiến lược an toàn tồn kho (khuyến nghị):
    1. Trong transaction: SELECT sku FOR UPDATE
    2. Check & decrement inventory_count
    3. Insert inventory_reservation với `expires_at`
- Release reservation: scheduled job hoặc Redis TTL + reconcile
- Idempotency key cho checkout (tránh duplicate orders)

## Lệnh thường dùng (Windows)
- Khởi chạy dịch vụ dev: `docker compose up -d`
- Build & run: `mvn -DskipTests clean package` rồi `mvn spring-boot:run`
- Chạy migration (nếu dùng Flyway): `mvn -DskipTests flyway:migrate`
- Chạy tests: `mvn test`

## Acceptance criteria (dùng để demo)
- API products + SKUs hoạt động với pagination/filer
- Cart enforce inventory on add/update
- Checkout reserve inventory 10–15 phút và tạo order (COD)
- Chạy concurrency test: last-item chỉ cho 1 checkout thành công
- Admin list + update order status
- Email gửi tracking link truy cập không cần login

## Files cần nộp trong repo
- `README.md` (hướng dẫn chạy)
- `docker-compose.yml`
- `src/...` (mã nguồn)
- `src/main/resources/db/migration/*.sql`
- `docs/TechnicalReport.md`
- `postman_collection.json` hoặc `openapi.yaml`

---
