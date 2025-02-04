CREATE TABLE account (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE balance (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    account_id BIGINT NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    category ENUM('FOOD', 'MEAL', 'CASH') NOT NULL,
    FOREIGN KEY (account_id) REFERENCES account(id)
);

CREATE TABLE transaction (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    account_id BIGINT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    mcc VARCHAR(4) NOT NULL,
    merchant VARCHAR(255) NOT NULL,
    status ENUM('APPROVED', 'REJECTED', 'FAILED'),
    FOREIGN KEY (account_id) REFERENCES account(id)
);

CREATE TABLE merchant (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    category ENUM('FOOD', 'MEAL', 'CASH') NOT NULL
);
