CREATE TYPE slide_variation as ENUM ('TEXT','VIDEO','TEST');

CREATE TABLE courses
(
    id              BIGSERIAL PRIMARY KEY,
    title           VARCHAR(255) NOT NULL,
    description     text         NOT NULL,
    price           DECIMAL(8, 2),
    owner           BIGINT,
    organization_id BIGINT,
    created_at      timestamp default now(),

    CONSTRAINT fk_course_owner_id
        FOREIGN KEY (owner)
            REFERENCES accounts (id),

    CONSTRAINT fo_course_organization_id
        FOREIGN KEY (organization_id)
            REFERENCES organizations (id)
);

CREATE TABLE modules
(
    id         BIGSERIAL PRIMARY KEY,
    title      VARCHAR(125) NOT NULL,
    created_at timestamp default now()
);

CREATE TABLE slides
(
    id          BIGSERIAL PRIMARY KEY,
    title       VARCHAR(255),
    description text,
    slide_type  slide_variation NOT NULL,
    payload     jsonb,
    created_at  timestamp default now()
);

CREATE TABLE course_modules
(
    course_id BIGINT,
    module_id BIGINT,
    index     int,

    CONSTRAINT fk_course_modules_course_id
        FOREIGN KEY (course_id)
            REFERENCES courses (id),

    CONSTRAINT fk_course_modules_module_id
        FOREIGN KEY (module_id)
            REFERENCES modules (id),

    CONSTRAINT uq_course_modules_course_id_index UNIQUE (course_id, index),

    PRIMARY KEY (course_id, module_id)
);


CREATE TABLE module_slides
(
    module_id BIGINT,
    slide_id  BIGINT,
    index     int,

    CONSTRAINT fk_module_slides_module_id
        FOREIGN KEY (module_id)
            REFERENCES modules (id),

    CONSTRAINT fk_module_slides_slide_id
        FOREIGN KEY (slide_id)
            REFERENCES slides (id),


    CONSTRAINT uq_module_slides_module_id_index UNIQUE (module_id, index),

    PRIMARY KEY (module_id, slide_id)
);


CREATE TABLE enrollment
(
    account_id       BIGINT,
    course_id        BIGINT,
    completed        boolean   default false,
    current_slide_id BIGINT    default null,
    start_date       timestamp default now(),

    CONSTRAINT fk_enrollment_account_id
        FOREIGN KEY (account_id)
            REFERENCES accounts (id),

    CONSTRAINT fk_enrollment_course_id
        FOREIGN KEY (course_id)
            REFERENCES courses (id),

    CONSTRAINT fk_enrollment_course_current_slide_id_slide
        FOREIGN KEY (current_slide_id)
            REFERENCES slides (id),

    PRIMARY KEY (account_id, course_id)
);

INSERT INTO courses (id, title, description, price, owner, organization_id)
VALUES (1,
        'Java Core: Путь Самурая',
        'Полное погружение в JVM, Garbage Collector и боль.',
        9990.50,
        1,
        1);

INSERT INTO modules (id, title)
VALUES (1, 'Введение и История'),
       (2, 'Синтаксис и Типы данных');

INSERT INTO course_modules (course_id, module_id, index)
VALUES (1, 1, 1),
       (1, 2, 2);

INSERT INTO slides (id, title, description, slide_type, payload)
VALUES (1, 'Что такое Java?', 'Краткий экскурс', 'TEXT',
        '{
          "content": "# Java\nЭто строго типизированный объектно-ориентированный язык..."
        }'::jsonb);

INSERT INTO slides (id, title, description, slide_type, payload)
VALUES (2, 'Лекция от Гослинга', 'Историческое видео', 'VIDEO',
        '{
          "videoUrl": "https://youtube.com/watch?v=dQw4w9WgXcQ",
          "durationSeconds": 1200,
          "platform": "YOUTUBE"
        }'::jsonb);

INSERT INTO slides (id, title, description, slide_type, payload)
VALUES (3, 'Проверка знаний', 'Тест по первой главе', 'TEST',
        '{
          "question": "В каком году вышла Java 1.0?",
          "isMultiSelect": false,
          "options": [
            {
              "id": 1,
              "text": "1990",
              "isCorrect": false
            },
            {
              "id": 2,
              "text": "1995",
              "isCorrect": true
            },
            {
              "id": 3,
              "text": "2024",
              "isCorrect": false
            }
          ]
        }'::jsonb);

INSERT INTO module_slides (module_id, slide_id, index)
VALUES (1, 1, 1),
       (1, 2, 2);

INSERT INTO module_slides (module_id, slide_id, index)
VALUES (2, 3, 1);