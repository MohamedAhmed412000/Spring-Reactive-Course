DROP TABLE IF EXISTS "ORDER";
DROP TABLE IF EXISTS CUSTOMER;
DROP TABLE IF EXISTS PRODUCT;

CREATE TABLE CUSTOMER (
    ID int AUTO_INCREMENT primary key,
    NAME VARCHAR(100),
    EMAIL VARCHAR(100)
);

CREATE TABLE PRODUCT (
    ID int AUTO_INCREMENT primary key,
    DESCRIPTION VARCHAR(100),
    PRICE int
);

CREATE TABLE "ORDER" (
    ORDER_ID uuid default random_uuid() primary key,
    CUSTOMER_ID int,
    PRODUCT_ID int,
    AMOUNT int,
    ORDER_DATE TIMESTAMP WITH TIME ZONE default CURRENT_TIMESTAMP,
    foreign key (CUSTOMER_ID) references CUSTOMER(ID) on delete cascade,
    foreign key (PRODUCT_ID) references PRODUCT(ID)
);

INSERT INTO CUSTOMER(NAME, EMAIL)
VALUES
    ('sam', 'sam@gmail.com'),
    ('mike', 'mike@gmail.com'),
    ('jake', 'jake@gmail.com'),
    ('emily', 'emily@example.com'),
    ('sophia', 'sophia@example.com'),
    ('liam', 'liam@example.com'),
    ('olivia', 'olivia@example.com'),
    ('noah', 'noah@example.com'),
    ('ava', 'ava@example.com'),
    ('ethan', 'ethan@example.com');

INSERT INTO PRODUCT(DESCRIPTION, PRICE)
VALUES
    ('iphone 20', 1000),
    ('iphone 18', 750),
    ('ipad', 800),
    ('mac pro', 3000),
    ('apple watch', 400),
    ('macbook air', 1200),
    ('airpods pro', 250),
    ('imac', 2000),
    ('apple tv', 200),
    ('homepod', 300);

-- Order 1: sam buys an iphone 20 & iphone 18
INSERT INTO "ORDER" (CUSTOMER_ID, PRODUCT_ID, AMOUNT, ORDER_DATE)
VALUES
    (1, 1, 950, CURRENT_TIMESTAMP),
    (1, 2, 850, CURRENT_TIMESTAMP);

-- Order 2: mike buys an iphone 20 and mac pro
INSERT INTO "ORDER" (CUSTOMER_ID, PRODUCT_ID, AMOUNT, ORDER_DATE)
VALUES
    (2, 1, 975, CURRENT_TIMESTAMP),
    (2, 4, 2999, CURRENT_TIMESTAMP);

-- Order 3: jake buys an iphone 18 & ipad
INSERT INTO "ORDER" (CUSTOMER_ID, PRODUCT_ID, AMOUNT, ORDER_DATE)
VALUES
    (3, 2, 750, CURRENT_TIMESTAMP),
    (3, 2, 775, CURRENT_TIMESTAMP);