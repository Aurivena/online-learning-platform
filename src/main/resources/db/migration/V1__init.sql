CREATE TYPE roles as ENUM('USER','ADMIN');

CREATE TABLE accounts (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    username VARCHAR(125),
    login VARCHAR(125),
    created_at timestamp default now()
);

CREATE TABLE refresh_tokens(
    id BIGSERIAL PRIMARY KEY ,
    token_hash VARCHAR(255) NOT NULL ,
    account_id BIGINT NOT NULL ,
    expiration timestamp NOT NULL ,
    created timestamp NOT NULL,

    CONSTRAINT fk_refresk_token_to_account_0
        FOREIGN KEY (account_id)
        REFERENCES accounts(id)
        ON DELETE CASCADE
);

-- (пароль 'admin' в BCrypt)
INSERT INTO accounts (email, password_hash, role, username, login)
VALUES ('admin@lms.dev', '$2a$12$0/3t0IVWUy79ICq7iZL/KehehrMhk1WvHpIGBebTNuRG8mKl9MxF6', 'ADMIN', 'Admin', 'admin_boss');