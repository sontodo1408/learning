-- ====================================================================
-- CÁC CÂU LỆNH CREATE TABLE (TẠO BẢNG MỚI)
-- ====================================================================
-- BẢNG 1: users (Người dùng)
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
);

-- BẢNG 2: study_sets (Bộ thẻ học tập)
DROP TABLE IF EXISTS study_sets;
CREATE TABLE study_sets (
    id                          BIGINT          AUTO_INCREMENT PRIMARY KEY,
    user_id                     BIGINT          NOT NULL,
    title                       VARCHAR(255)    NOT NULL,
    description                 TEXT,
    is_public                   BOOLEAN         NOT NULL DEFAULT FALSE, -- Lưu số 0/1 dưới DB, JDBC tự dịch thành false/true trong Java
    created_at                  DATETIME,
    updated_at                  DATETIME
);

-- BẢNG 3: study_cards (Chi tiết từng thẻ)
DROP TABLE IF EXISTS study_cards;
CREATE TABLE study_cards (
    id                          BIGINT          AUTO_INCREMENT PRIMARY KEY,
    study_set_id                BIGINT          NOT NULL,
    term                        VARCHAR(255)    NOT NULL,
    definition                  TEXT            NOT NULL,
    pronounce_term              VARCHAR(50),
    pronounce_def               VARCHAR(50),
    img_url                     VARCHAR(255)    DEFAULT NULL,
    display_order               INT             NOT NULL DEFAULT 0, -- Để sắp xếp thứ tự hiển thị các thẻ trong bộ
    created_at                  DATETIME,
    updated_at                  DATETIME
);