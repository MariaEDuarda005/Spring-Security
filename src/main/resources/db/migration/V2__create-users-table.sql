CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY UNIQUE NOT NULL,
    login VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL
);
