CREATE TYPE slide_variation as ENUM ('TEXT','VIDEO','TEST');

CREATE TABLE course
(
    id          BIGSERIAL PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    description text         NOT NULL,
    price       DECIMAL(8, 2),
    owner       BIGINT,

    CONSTRAINT fk_course_owner_id
        FOREIGN KEY (owner)
            REFERENCES accounts (id)
);

CREATE TABLE module
(
    id    BIGSERIAL PRIMARY KEY,
    title VARCHAR(125) NOT NULL
);

CREATE TABLE slide
(
    id          BIGSERIAL PRIMARY KEY,
    title       VARCHAR(255),
    description text,
    slide_type  slide_variation NOT NULL,
    payload     jsonb
);

CREATE TABLE course_modules
(
    course_id BIGINT,
    module_id BIGINT,
    index     int,

    CONSTRAINT fk_course_modules_course_id
        FOREIGN KEY (course_id)
            REFERENCES course (id),

    CONSTRAINT fk_course_modules_module_id
        FOREIGN KEY (module_id)
            REFERENCES module (id),

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
            REFERENCES module (id),

    CONSTRAINT fk_module_slides_slide_id
        FOREIGN KEY (slide_id)
            REFERENCES slide (id),


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
            REFERENCES course (id),

    CONSTRAINT fk_enrollment_course_current_slide_id_slide
        FOREIGN KEY (current_slide_id)
            REFERENCES slide (id),

    PRIMARY KEY (account_id, course_id)
);