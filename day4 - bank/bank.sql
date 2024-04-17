drop database if exists bank;

create database bank;

use bank;

create table accounts (
    acc_id char(4) not null,
    name varchar(32) not null,
    balance decimal(10,2) default 0.0,
    last_update datetime default current_timestamp on update current_timestamp,

    constraint pk_acc_id primary key(acc_id),
    constraint chk_balance check(balance >= 0)
);

insert into accounts(acc_id, name, balance) values
    ('abcd','fred',500.00),
    ('1234','barney',500.00);

-- TRANSACTION
start transaction;

update accounts
    set balance = balance - 100.00
    where acc_id = '1234';

update accounts
    set balance = balance + 100.00
    where acc_id = 'abcd';

commit;
-- END OF TRANSACTION