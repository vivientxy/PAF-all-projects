DROP DATABASE IF EXISTS orders;

CREATE DATABASE orders;

USE orders;

CREATE TABLE orders (
    order_id int auto_increment PRIMARY KEY,
    order_date date,
    customer_name varchar(128),
    ship_address varchar(128),
    notes text,
    tax decimal(2,2) DEFAULT 0.05
);

CREATE TABLE orders_details (
    id int auto_increment PRIMARY KEY,
    product varchar(64),
    unit_price decimal(3,2),
    discount decimal(3,2) DEFAULT 1.0,
    quantity int,
    order_id int,

    CONSTRAINT fk_order_id FOREIGN KEY(order_id)
        REFERENCES orders(order_id)
);

GRANT ALL PRIVILEGES ON orders.* TO 'betty'@'%';
FLUSH PRIVILEGES;