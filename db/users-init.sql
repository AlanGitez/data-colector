CREATE ROLE admin;
CREATE ROLE user_A;
CREATE ROLE user_B;
CREATE ROLE user_C;

CREATE SCHEMA users;

CREATE TABLE users.users (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  lastname VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  role TEXT
);

INSERT INTO users.users (name, lastname, email, password, role)
VALUES ('Admin', 'Admin', 'admin@admin.com', 'admin', 'admin');


