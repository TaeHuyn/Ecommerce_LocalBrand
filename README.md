# E-commerce backend (Phase 1)

Hướng dẫn nhanh để chạy dự án dev, migration và API docs.

## Yêu cầu môi trường

- Java 17+
- Maven 3.8+
- MySQL 8+

## Biến môi trường cần cấu hình (recommended)

- `SPRING_DATASOURCE_URL` (ví dụ: `jdbc:mysql://localhost:3306/ecommerce_db`)
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `JWT_SECRET` (sử dụng biến này để override `jwt.secret`)
- `SPRING_MAIL_USERNAME`, `SPRING_MAIL_PASSWORD`

## Tạo database

1. Tạo database: `CREATE DATABASE ecommerce_db;`
2. Cập nhật biến môi trường hoặc file `src/main/resources/application.properties` nếu cần.

## Migration & seed

- Hiện tại dự án dùng `spring.jpa.hibernate.ddl-auto=update` cho môi trường dev và có file seed `src/main/resources/ecommerce_db.sql`.
- Khuyến nghị: thêm Flyway/Liquibase trước khi deploy production.

## Chạy ứng dụng

### Bằng Maven

```bash
mvn clean install
mvn spring-boot:run
```

### Trong IDE

- Mở project và chạy `fpt.com.ecommerce.EcommerceApplication`.

## Kiểm tra

- Server mặc định: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/api/docs`

## Kiểm thử nhanh (API)

- Lấy danh sách sản phẩm:
  `GET /api/products?page=0&size=10`
- Lấy chi tiết sản phẩm:
  `GET /api/products/{slug}`

## Notes

- Không commit `JWT_SECRET` hoặc mail credentials vào repo. Dùng env vars.
- Nếu cần test reservation expiry nhanh, bạn có thể điều chỉnh `reservation.ttl.ms` trong `application.properties`.
