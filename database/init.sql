-- ConsulterDepenses - PostgreSQL initialization script

CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE SCHEMA IF NOT EXISTS consulter_depenses;
SET search_path TO consulter_depenses;

CREATE TABLE "user" (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email       VARCHAR(255) NOT NULL UNIQUE,
    last_name   VARCHAR(100),
    first_name  VARCHAR(100),
    created_at  TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE category (
    id      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    label   VARCHAR(100) NOT NULL,
    color   VARCHAR(7),
    icon    VARCHAR(50)
);

INSERT INTO category (label, color, icon) VALUES
    ('Food',        '#F59E0B', 'shopping-cart'),
    ('Transport',   '#3B82F6', 'car'),
    ('Housing',     '#10B981', 'home'),
    ('Health',      '#EF4444', 'heart'),
    ('Leisure',     '#8B5CF6', 'star'),
    ('Clothing',    '#EC4899', 'shirt'),
    ('Education',   '#06B6D4', 'book'),
    ('Other',       '#6B7280', 'tag');

CREATE TABLE account (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    owner_id    UUID NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
    name        VARCHAR(100) NOT NULL,
    balance     NUMERIC(12, 2) NOT NULL DEFAULT 0,
    currency    CHAR(3) NOT NULL DEFAULT 'EUR',
    created_at  TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Members who can read/write movements on a shared account
CREATE TABLE account_member (
    account_id  UUID NOT NULL REFERENCES account(id) ON DELETE CASCADE,
    user_id     UUID NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
    joined_at   TIMESTAMP NOT NULL DEFAULT NOW(),
    PRIMARY KEY (account_id, user_id)
);

CREATE TYPE movement_type AS ENUM ('DEBIT', 'CREDIT');

CREATE TABLE movement (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    account_id      UUID NOT NULL REFERENCES account(id) ON DELETE CASCADE,
    created_by      UUID NOT NULL REFERENCES "user"(id) ON DELETE RESTRICT,
    category_id     UUID REFERENCES category(id) ON DELETE SET NULL,
    type            movement_type NOT NULL,
    amount          NUMERIC(12, 2) NOT NULL CHECK (amount > 0),
    description     VARCHAR(255),
    movement_date   DATE NOT NULL,
    created_at      TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_account_owner        ON account(owner_id);
CREATE INDEX idx_account_member       ON account_member(user_id);
CREATE INDEX idx_movement_account     ON movement(account_id);
CREATE INDEX idx_movement_created_by  ON movement(created_by);
CREATE INDEX idx_movement_date        ON movement(movement_date);
CREATE INDEX idx_movement_category    ON movement(category_id);
