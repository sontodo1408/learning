-- ====================================================================
-- CREATE TABLE STATEMENTS (NEW TABLE CREATION)
-- ====================================================================
-- TABLE 1: users
DROP TABLE IF EXISTS users;
CREATE TABLE users (
    id                          BIGINT          AUTO_INCREMENT PRIMARY KEY,
    username                    VARCHAR(50)     NOT NULL UNIQUE,
    password                    VARCHAR(255)    NOT NULL,
    full_name                   VARCHAR(25)     NOT NULL,
    email                       VARCHAR(255)    NOT NULL,
    phone_number                VARCHAR(25),
    birthday                    DATE,
    gender                      TINYINT,
    role                        VARCHAR(25)     NOT NULL,
    status                      TINYINT         NOT NULL DEFAULT 1, -- 1 = ACTIVE
    created_at                  DATETIME,
    updated_at                  DATETIME
) AUTO_INCREMENT = 1;

-- TABLE 2: study_sets (study card sets)
DROP TABLE IF EXISTS study_sets;
CREATE TABLE study_sets (
    id                          BIGINT          AUTO_INCREMENT PRIMARY KEY,
    user_id                     BIGINT          NOT NULL,
    title                       VARCHAR(255)    NOT NULL,
    description                 TEXT,
    is_public                   BOOLEAN         NOT NULL DEFAULT FALSE, -- Stored as 0/1 in the DB, JDBC automatically maps it to false/true in Java
    created_at                  DATETIME,
    updated_at                  DATETIME
) AUTO_INCREMENT = 1;

-- TABLE 3: study_cards (individual card details)
DROP TABLE IF EXISTS study_cards;
CREATE TABLE study_cards (
    id                          BIGINT          AUTO_INCREMENT PRIMARY KEY,
    study_set_id                BIGINT          NOT NULL,
    term                        VARCHAR(255)    NOT NULL,
    definition                  TEXT            NOT NULL,
    pronounce_term              VARCHAR(50),
    pronounce_def               VARCHAR(50),
    img_url                     VARCHAR(255)    DEFAULT NULL,
    display_order               INT             NOT NULL DEFAULT 0, -- Used to order how cards are displayed within a set
    created_at                  DATETIME,
    updated_at                  DATETIME
) AUTO_INCREMENT = 1;

-- TABLE 4: study_set_views (tracks the study sets a user recently viewed)
DROP TABLE IF EXISTS study_set_views;
CREATE TABLE study_set_views (
    id                          BIGINT          AUTO_INCREMENT PRIMARY KEY,
    user_id                     BIGINT          NOT NULL,
    study_set_id                BIGINT          NOT NULL,
    viewed_at                   DATETIME        NOT NULL, -- Refreshed to the current time on every view (BaseEntity's updated_at is non-updatable, so it can't serve this purpose)
    created_at                  DATETIME,
    updated_at                  DATETIME,
    UNIQUE KEY uk_user_study_set (user_id, study_set_id) -- One row per user/study-set pair; each new view upserts this row instead of inserting a duplicate
) AUTO_INCREMENT = 1;
