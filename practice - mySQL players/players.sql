DROP DATABASE IF EXISTS game_hub;

CREATE DATABASE game_hub;

USE game_hub;

CREATE TABLE players (
    username varchar(32) PRIMARY KEY,
    password varchar(32) NOT NULL,
    security_question text NOT NULL,
    security_answer varchar(64) NOT NULL,
    wallet int DEFAULT 0,

    CONSTRAINT chk_wallet
        CHECK(wallet >= 0)
);

CREATE TABLE trades (
    trade_id int auto_increment PRIMARY KEY,
    ticker varchar(8) NOT NULL,
    quantity int NOT NULL,
    price DECIMAL(10,2),
    direction char(1) NOT NULL,
    username varchar(32) NOT NULL,
    trade_date DATETIME DEFAULT current_timestamp,

    CONSTRAINT fk_username FOREIGN KEY(username)
        REFERENCES players(username),
    CONSTRAINT chk_quantity
        CHECK(quantity >= 0),
    CONSTRAINT chk_direction
        CHECK(direction = 'B' OR direction = 'S')
);

-- add fake items
INSERT INTO players(username, password, security_question, security_answer) VALUES ('vivientxy','123456789','Who is your favourite Marvel character?','Wolverine');
INSERT INTO trades(ticker, quantity, price, direction, username) VALUES ('AMD',10,35.0,'B','vivientxy');
INSERT INTO trades(ticker, quantity, price, direction, username) VALUES ('AMD',2,32.05,'B','vivientxy');
INSERT INTO trades(ticker, quantity, price, direction, username) VALUES ('AMD',5,31.82,'S','vivientxy');
INSERT INTO trades(ticker, quantity, price, direction, username) VALUES ('AMD',3,36.9,'B','vivientxy');
INSERT INTO trades(ticker, quantity, price, direction, username) VALUES ('AMD',10,42.56,'S','vivientxy');
INSERT INTO trades(ticker, quantity, price, direction, username) VALUES ('AAPL',10,107.3,'B','vivientxy');
INSERT INTO trades(ticker, quantity, price, direction, username) VALUES ('AAPL',15,123.52,'B','vivientxy');
INSERT INTO trades(ticker, quantity, price, direction, username) VALUES ('AAPL',13,118.5,'S','vivientxy');
INSERT INTO trades(ticker, quantity, price, direction, username) VALUES ('AAPL',8,135.15,'B','vivientxy');

GRANT ALL PRIVILEGES ON game_hub.* TO 'betty'@'%';
FLUSH PRIVILEGES;

CREATE VIEW user_positions AS
        SELECT username, ticker, SUM(CASE WHEN direction = 'S'
                    THEN quantity * -1
                    ELSE quantity END) AS open_quantity
        FROM game_hub.trades
        GROUP BY username,ticker;