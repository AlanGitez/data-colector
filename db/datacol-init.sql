CREATE SCHEMA dataType_A;
CREATE SCHEMA dataType_B;
CREATE SCHEMA dataType_C;

CREATE TABLE dataType_A.data (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  province VARCHAR(255) NOT NULL,
  phone VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  message VARCHAR(255),
  dni BIGINT,
  type VARCHAR,
  count INT
);
CREATE TABLE dataType_B.data (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  province VARCHAR(255) NOT NULL,
  phone VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  message VARCHAR(255),
  dni BIGINT,
  type VARCHAR,
  count INT
);
CREATE TABLE dataType_C.data (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  province VARCHAR(255) NOT NULL,
  phone VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  message VARCHAR(255),
  dni BIGINT,
  type VARCHAR,
  count INT
);


