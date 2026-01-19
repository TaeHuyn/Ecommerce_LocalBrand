# 1. YÊU CẦU MÔI TRƯỜNG

## Phần mềm cần cài đặt
- **Java** 17 trở lên
- **Maven** 3.8 trở lên
- **MySQL** 8.0 trở lên

## Kiểm tra phiên bản đã cài
```bash
java -version
mvn -version
mysql --version
```

# 2.DATABASE SETUP

## Tạo database
- CREATE DATABASE ecommerce_db;

- Cấu hình kết nối Database

- Mở file: src/main/resources/application.properties

## Cấu hình như sau:

```properties
spring.application.name=ecommerce
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db
spring.datasource.username=
spring.datasource.password=
spring.jpa.show-sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect

spring.jpa.hibernate.ddl-auto=update

server.port=8080

# Swagger configuration
springdoc.api-docs.path=/api-docs
springdoc.api-docs.enabled=true
springdoc.swagger-ui.path=/api/docs
springdoc.swagger-ui.tags-sorter=alpha
springdoc.writer-with-order-by-keys=true

# Mail configuration
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=
spring.mail.password=

spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
spring.mail.default-encoding=UTF-8

# Logging
logging.level.org.springframework.mail=DEBUG
```

## Lưu ý: Thay đổi username và password cho phù hợp với môi trường máy local.

# 3.MIGRATION & SEED DATA

## Dữ liệu mẫu được khai báo trong file:
src/main/resources/ecommerce_db.sql

## Khi chạy project lần đầu:
- Hibernate tự động tạo bảng
- Dữ liệu Product và Product Variant được insert tự động 

Không cần chạy migration thủ công

# 4. CHẠY ỨNG DỤNG

## Cách 1: Chạy bằng Maven
```bash
mvn clean install
mvn spring-boot:run
```

## Cách 2: Chạy trong IDE
- Mở project trong IDE (IntelliJ, Eclipse, VSCode)
- Chạy class chính: com.example.ecommerce.EcommerceApplication

# 5. KIỂM TRA SERVER
Sau khi chạy thành công, server hoạt động tại: **http://localhost:8080**

# 6. API DOCUMENTATION
Truy cập tài liệu API tại: **http://localhost:8080/api/docs**

# 7. CHẠY TESTS
```bash
GET /api/products
```

Ví dụ: 
```bash
GET /api/products?page=0&size=6&minPrice=100000&maxPrice=300000
```

## Lấy chi tiết sản phẩm theo slug
```bash
GET /api/products/{slug}
```

Ví dụ:
```bash
GET /api/products/ao-thun-rong-den
```
