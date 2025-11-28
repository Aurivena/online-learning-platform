CREATE TABLE course
(
    id          BIGSERIAL PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    description text         NOT NULL,
    price       DECIMAL(8, 2),
    owner       BIGINT,

    CONSTRAINT fk_course_0
        FOREIGN KEY (owner)
            REFERENCES accounts (id)
);

CREATE TABLE slide
(
    id          BIGSERIAL PRIMARY KEY,
    title       VARCHAR(255),
    description text,
    content     text
);

CREATE TABLE course_slides
(
    course_id BIGINT,
    slide_id  BIGINT,
    index     int,

    CONSTRAINT fk_course_slides_0
        FOREIGN KEY (course_id)
            REFERENCES course (id),

    CONSTRAINT fk_course_slides_1
        FOREIGN KEY (slide_id)
            REFERENCES slide (id),

    CONSTRAINT uq_course_slides_0 UNIQUE (course_id, index),

    PRIMARY KEY (course_id, slide_id)
);

CREATE TABLE enrollment
(
    account_id      BIGINT,
    course_id       BIGINT,
    completed       boolean   default false,
    completed_slide int       default 0,
    start_date      timestamp default now(),

    CONSTRAINT fk_enrollment_0
        FOREIGN KEY (account_id)
            REFERENCES accounts (id),

    CONSTRAINT fk_enrollment_1
        FOREIGN KEY (course_id)
            REFERENCES course (id),

    PRIMARY KEY (account_id, course_id)
);