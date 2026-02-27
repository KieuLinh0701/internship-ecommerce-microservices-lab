CREATE TABLE m_role
(
    id         UUID PRIMARY KEY     DEFAULT gen_random_uuid(),
    name       VARCHAR(50) NOT NULL UNIQUE,
    status     VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP   NOT NULL DEFAULT NOW(),
    created_by UUID,
    updated_at TIMESTAMP   NOT NULL DEFAULT NOW(),
    updated_by UUID,
    is_delete  BOOLEAN     NOT NULL DEFAULT FALSE,
    version    BIGINT      NOT NULL DEFAULT 0
);

CREATE INDEX idx_role_status_delete
on m_role(status, is_delete);

CREATE TABLE accounts (
    id UUID PRIMARY KEY   DEFAULT gen_random_uuid(),
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'INACTIVE',
    is_verified BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    created_by UUID,
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_by UUID,
    is_delete  BOOLEAN   NOT NULL DEFAULT FALSE,
    version    BIGINT    NOT NULL DEFAULT 0
);

CREATE INDEX idx_account_email_delete
ON accounts(email, is_delete);

CREATE TABLE account_roles (
    account_id UUID NOT NULL,
    role_id UUID NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    created_by UUID,
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_by UUID,
    is_delete  BOOLEAN   NOT NULL DEFAULT FALSE,
    version    BIGINT    NOT NULL DEFAULT 0,
    PRIMARY KEY (account_id, role_id),
    CONSTRAINT fk_account FOREIGN KEY (account_id) REFERENCES accounts(id),
    CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES m_role(id)
);

CREATE INDEX idx_account_role_account_status_delete
ON account_roles(account_id, status, is_delete);