INSERT INTO users (username, password, enabled) VALUES
    ('admin', '$2a$10$5vIYhH5ZGmK3B0gCCs5v2.Wm7v6c8w4BLjhMQ2ShC5.kZXqvQ6Nka', true);

INSERT INTO user_roles (username, role) VALUES
    ('admin', 'ROLE_ADMIN');
