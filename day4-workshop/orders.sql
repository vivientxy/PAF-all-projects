drop database if exists orders;

create database orders;

use orders;

create table customers (
    email varchar(128) not null primary key,
    name varchar(50) not null
);

create table orders (
    order_id char(8) not null primary key,
    email varchar(128) not null,
    delivery_date datetime not null,
    rush boolean default false,
    comments text,
    last_update datetime default current_timestamp on update current_timestamp,

    constraint fk_email foreign key(email)
        references customers(email)
);

create table line_items (
    li_id int auto_increment primary key,
    item varchar(32) not null,
    quantity int default 1,
    order_id char(8) not null,

    constraint fk_orders_id foreign key(order_id)
        references orders(order_id)
);

-- add fake items
INSERT INTO customers(name, email) VALUES ('fred','fred@email.com');
INSERT INTO customers(name, email) VALUES ('betty','betty@email.com');

grant all privileges on orders.* to 'betty'@'%';
flush privileges;