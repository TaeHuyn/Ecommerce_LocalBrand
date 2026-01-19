DROP DATABASE IF EXISTS ecommerce_db;

-- CREATE DATABASE
CREATE DATABASE ecommerce_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- USE DATABASE
USE ecommerce_db;

-- CREATE TABLE
CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    name VARCHAR(255) NOT NULL,
    slug VARCHAR(255) NOT NULL UNIQUE,
    category VARCHAR(100),
    description TEXT,
    thumbnail_url VARCHAR(500) NOT NULL,

    min_price INT NOT NULL,
    max_price INT NOT NULL,

    status VARCHAR(50) NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- CREATE TABLE product_variant
CREATE TABLE product_variants (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    product_id BIGINT NOT NULL,

    size VARCHAR(50),
    color VARCHAR(50),

    price INT NOT NULL,
    stock INT NOT NULL,
    reserved_stock INT NOT NULL DEFAULT 0,

    CONSTRAINT uq_product_variant UNIQUE (product_id, size, color),

    CONSTRAINT fk_variant_product
        FOREIGN KEY (product_id)
        REFERENCES products(id)
        ON DELETE CASCADE
);

-- CREATE TABLE carts
CREATE TABLE carts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    cart_token VARCHAR(255) NOT NULL UNIQUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP
);

-- CREATE TABLE cart_items
CREATE TABLE cart_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    cart_id BIGINT NOT NULL,
    product_variant_id BIGINT NOT NULL,

    quantity INT NOT NULL,
    price INT NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT uq_cart_variant UNIQUE (cart_id, product_variant_id),

    CONSTRAINT fk_cart_item_cart
        FOREIGN KEY (cart_id)
        REFERENCES carts(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_cart_item_variant
        FOREIGN KEY (product_variant_id)
        REFERENCES product_variants(id)
);

-- CREATE TABLE orders
CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    order_code VARCHAR(100) NOT NULL UNIQUE,

    customer_name VARCHAR(255),
    phone VARCHAR(50),
    address VARCHAR(500),

    payment_method VARCHAR(50),
    order_status VARCHAR(50),

    total_amount INT,
    expires_at TIMESTAMP,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP
);

-- CREATE TABLE order_items
CREATE TABLE order_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    order_id BIGINT NOT NULL,
    product_variant_id BIGINT NOT NULL,

    quantity INT NOT NULL,
    price INT NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_order_item_order
        FOREIGN KEY (order_id)
        REFERENCES orders(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_order_item_variant
        FOREIGN KEY (product_variant_id)
        REFERENCES product_variants(id)
);

-- Mock data
INSERT INTO products
(id, name, slug, category, description, thumbnail_url, min_price, max_price, status, created_at)
VALUES
(1, 'Áo Thun Rồng', 'ao-thun-rong', 'T-SHIRT',
 'Áo thun cotton in hình rồng phong cách streetwear',
 'https://picsum.photos/seed/ao-thun-rong/600/600',
 199000, 249000, 'ACTIVE', NOW()),

(2, 'Hoodie Local Brand', 'hoodie-local-brand', 'HOODIE',
 'Hoodie nỉ dày, form rộng unisex',
 'https://picsum.photos/seed/hoodie-local-brand/600/600',
 399000, 459000, 'ACTIVE', NOW()),

(3, 'Áo Thun Basic', 'ao-thun-basic', 'T-SHIRT',
 'Áo thun trơn, dễ phối đồ',
 'https://picsum.photos/seed/ao-thun-basic/600/600',
 149000, 179000, 'ACTIVE', NOW());

------------------------------------------------------------------
INSERT INTO product_variants
(id, product_id, size, color, price, stock, reserved_stock)
VALUES
-- Áo Thun Rồng
(1, 1, 'M', 'BLACK', 199000, 10, 0),
(2, 1, 'L', 'BLACK', 199000, 2, 0),
(3, 1, 'XL', 'WHITE', 249000, 5, 0),

-- Hoodie Local Brand
(4, 2, 'M', 'GRAY', 399000, 3, 0),
(5, 2, 'L', 'BLACK', 459000, 1, 0), -- last item
(6, 2, 'XL', 'BLACK', 459000, 0, 0), -- out of stock

-- Áo Thun Basic
(7, 3, 'S', 'WHITE', 149000, 20, 0),
(8, 3, 'M', 'WHITE', 169000, 15, 0),
(9, 3, 'L', 'BLACK', 179000, 5, 0);





