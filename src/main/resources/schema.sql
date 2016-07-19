DROP TABLE IF EXISTS CLIENTS;
DROP TABLE IF EXISTS ORDERS;
DROP TABLE IF EXISTS REQUESTS;
DROP TABLE IF EXISTS PRODUCTS;


CREATE TABLE CLIENTS (
       ID BIGINT NOT NULL AUTO_INCREMENT
     , FIRST_NAME VARCHAR(40)
     , LAST_NAME VARCHAR(40)
     , COMPANY_NAME VARCHAR (100) NOT NULL
     , PHONE_NUMBER VARCHAR (20) NOT NULL
     , ADDRESS VARCHAR (100) NOT NULL
     , UNIQUE UQ_CLIENTS_1 (COMPANY_NAME)
     , PRIMARY KEY (ID)
);

CREATE TABLE ORDERS (
       ID BIGINT NOT NULL AUTO_INCREMENT
     , CLIENTS_ID BIGINT NOT NULL
     , SHIP_DATA DATETIME NOT NULL
     , REQUEST_ID BIGINT NOT NULL
     , UNIQUE UQ_ORDERS_1 (SHIP_DATA)
     , CONSTRAINT FK_ORDERS_CLIENTS FOREIGN KEY (CLIENTS_ID)
                  REFERENCES CLIENTS (ID)
     , CONSTRAINT FK_ORDERS_REQUESTS FOREIGN KEY (REQUESTS_ID)
                  REFERENCES REQUESTS (ID)
     , PRIMARY KEY (ID)
);

CREATE TABLE REQUESTS (
       ID BIGINT NOT NULL AUTO_INCREMENT
     , ORDER_ID BIGINT NOT NULL
     , PRODUCTS_LIST VARCHAR (160) NOT NULL
     , READINESS_DATA DATETIME NOT NULL
     , PRIMARY KEY (ID)
     , CONSTRAINT FK_REQUESTS_ORDERS FOREIGN KEY (ORDERS_ID)
                  REFERENCES ORDERS (ID)
     , UNIQUE UQ_REQUESTS_1 (SHIP_DATA)
);

CREATE TABLE PRODUCTS (
       ID BIGINT NOT NULL AUTO_INCREMENT
     , CLIENTS_LIST VARCHAR (160) NOT NULL
     , NAME VARCHAR (160) NOT NULL
     , TYPE VARCHAR (160) NOT NULL
     , INNER_LENGTH DECIMAL (10,3) NOT NULL
     , INNER_WIDTH DECIMAL (10,3) NOT NULL
     , INNER_HEIGHT DECIMAL (10,3) NOT NULL
     , GRADE VARCHAR (20) NOT NULL
     , PROFILE VARCHAR (20) NOT NULL
     , COLOUR VARCHAR (20) NOT NULL
     , PRINT VARCHAR (100) NOT NULL
     , PRIMARY KEY (ID)
     , CONSTRAINT FK_PRODUCTS_CLIENTS FOREIGN KEY (CLIENTS_ID)
                  REFERENCES CLIENTS (ID)
);

