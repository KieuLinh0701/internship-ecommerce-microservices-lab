CREATE TABLE refresh_tokens
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    account_id  UUID NOT NULL,
    token       VARCHAR(255) NOT NULL,
    is_used     BOOLEAN NOT NULL DEFAULT FALSE,
    expires_at  TIMESTAMP NOT NULL,
    created_at  TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_refresh_token_account
        FOREIGN KEY (account_id) REFERENCES accounts(id)
);

-- Verify token when refresh
CREATE INDEX idx_refresh_token_verify
    ON refresh_tokens(token)
    WHERE is_used = false;

-- Revoke all token of an account (logout all devices, change password)
CREATE INDEX idx_refresh_token_by_account
    ON refresh_tokens(account_id)
    WHERE is_used = false;