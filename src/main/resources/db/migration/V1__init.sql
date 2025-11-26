CREATE TYPE roles('USER','ADMIN')

CREATE TABLE accounts (
                          id BIGSERIAL PRIMARY KEY,
                          email VARCHAR(255) NOT NULL UNIQUE,
                          password VARCHAR(255) NOT NULL,
                          role roles NOT NULL,
                          username VARCHAR(125),
                          login VARCHAR(125)
);

-- Можно сразу добавить админа, чтобы потом не ковыряться в базе руками
-- Пароль "admin" в хешированном виде (BCrypt), сгенерируй свой если надо
INSERT INTO accounts (email, password, role, username, login)
VALUES ('admin@lms.dev', '$2a$10$xn3LI/AjqicFYZFruO4hq.khsR.nz.G..8G/G8S0d.A.B.C.D.E', 'Admin', 'Admin', 'admin_boss');