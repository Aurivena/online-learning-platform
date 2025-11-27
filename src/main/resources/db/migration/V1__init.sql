CREATE TYPE roles as ENUM('USER','ADMIN')

CREATE TABLE accounts (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    username VARCHAR(125),
    login VARCHAR(125),
    created_at timestamp default now()
);

CREATE TABLE refresh_token(
    id BIGSERIAL PRIMARY KEY ,
    tokenHash VARCHAR(255) NOT NULL ,
    account_id BIGINT NOT NULL ,
    expiration timestamp NOT NULL ,
    created timestamp NOT NULL,

    CONSTRAINT fk_refresk_token_to_account_0 account_id
        FOREIGN KEY (account_id)
        REFERENCES accounts(id)
        ON DELETE CASCADE;
);

-- (пароль 'admin' в BCrypt)
INSERT INTO accounts (email, password_hash, role, username, login)
VALUES ('admin@lms.dev', '$2a$10$xn3LI/AjqicFYZFruO4hq.khsR.nz.G..8G/G8S0d.A.B.C.D.E', 'ADMIN', 'Admin', 'admin_boss');