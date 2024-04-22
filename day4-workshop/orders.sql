DROP DATABASE IF EXISTS orders;

CREATE DATABASE orders;

USE orders;

CREATE TABLE customers (
    email varchar(128) NOT NULL PRIMARY KEY,
    name varchar(50) NOT NULL
);

CREATE TABLE orders (
    order_id char(8) NOT NULL PRIMARY KEY,
    email varchar(128) NOT NULL,
    delivery_date datetime NOT NULL,
    rush boolean DEFAULT false,
    comments text,
    last_update datetime DEFAULT current_timestamp ON UPDATE current_timestamp,

    CONSTRAINT fk_email FOREIGN KEY(email)
        REFERENCES customers(email)
);

CREATE TABLE line_items (
    li_id int auto_increment PRIMARY KEY,
    item varchar(32) NOT NULL,
    quantity int DEFAULT 1,
    order_id char(8) NOT NULL,

    CONSTRAINT fk_orders_id FOREIGN KEY(order_id)
        REFERENCES orders(order_id)
);

-- add fake items
INSERT INTO customers(name, email) VALUES ('fred','fred@email.com');
INSERT INTO customers(name, email) VALUES ('betty','betty@email.com');

GRANT ALL PRIVILEGES ON orders.* TO 'betty'@'%';
FLUSH PRIVILEGES;