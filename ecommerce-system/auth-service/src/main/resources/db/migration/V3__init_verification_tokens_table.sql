CREATE TABLE verification_tokens
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    account_id  UUID NOT NULL,
    token       VARCHAR(255) NOT NULL,
    type        VARCHAR(50) NOT NULL,
    is_used     BOOLEAN NOT NULL DEFAULT FALSE,
    expires_at  TIMESTAMP NOT NULL,
    created_at  TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_verification_token_account
        FOREIGN KEY (account_id) REFERENCES accounts(id)
);

-- Using mark token as used
CREATE INDEX idx_verification_token_mark_old
ON verification_tokens(account_id, type)
WHERE is_used = false;

-- Using verify token
CREATE INDEX idx_verification_token_verify
ON verification_tokens(token, account_id, type)
WHERE is_used = false;