CREATE TABLE users
(
    id         BIGSERIAL PRIMARY KEY,
    login      VARCHAR(255)      UNIQUE NOT NULL,
    name       VARCHAR(255)             NOT NULL,
    age        INT                      NOT NULL,
    gender     INT                      NOT NULL,
    hair_color VARCHAR(255)             NOT NULL
);

CREATE TABLE friends
(
    user_id   BIGINT NOT NULL,
    friend_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, friend_id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (friend_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE bank_accounts
(
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT           NOT NULL,
    balance    DOUBLE PRECISION NOT NULL
);

ALTER TABLE bank_accounts
ADD CONSTRAINT fk_user_id
FOREIGN KEY (user_id) REFERENCES users (id)
ON DELETE CASCADE;

CREATE TABLE transactions
(
    id               BIGSERIAL PRIMARY KEY,
    transaction_type VARCHAR(255)     NOT NULL,
    amount           DOUBLE PRECISION NOT NULL,
    account_id       BIGINT           NOT NULL,
    FOREIGN KEY (account_id) REFERENCES bank_accounts (id) ON DELETE CASCADE
);