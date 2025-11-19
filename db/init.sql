CREATE TABLE IF NOT EXISTS users(
  id SERIAL PRIMARY KEY,
  name VARCHAR(100),
  email VARCHAR(100),
  phone_number VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS auth(
  id SERIAL PRIMARY KEY,
  otp_clair VARCHAR(100),
  otp_hashe VARCHAR(100),
  id_user INTEGER NOT NULL,
  created_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY(id_user) REFERENCES users(id)
);

INSERT INTO users (name, email, phone_number) VALUES
('Alice Martin', 'alice@example.com', '0601020304'),
('Mehdi Kasmi', 'mehdi@example.com', '0611223344'),
('John Doe', 'john.doe@example.com', '0655443322'),
('Sara Lopez', 'sara.lopez@example.com', '0677889900'),
('Youssef Benali', 'youssef@example.com', '0699887766');
