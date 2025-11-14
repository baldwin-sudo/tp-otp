CREATE TABLE IF NOT EXISTS users(
  id SERIAL PRIMARY KEY,
  name VARCHAR(100),
  email VARCHAR(100),
phone_number varchar(100)
);
INSERT INTO users (name, email, phone_number) VALUES
('Alice Martin', 'alice@example.com', '0601020304'),
('Mehdi Kasmi', 'mehdi@example.com', '0611223344'),
('John Doe', 'john.doe@example.com', '0655443322'),
('Sara Lopez', 'sara.lopez@example.com', '0677889900'),
('Youssef Ben', 'youssef@example.com', '0622334455');
