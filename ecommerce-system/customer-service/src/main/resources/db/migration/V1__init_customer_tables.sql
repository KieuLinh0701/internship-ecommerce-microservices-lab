CREATE TABLE customers (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    account_id UUID NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    phone VARCHAR(11),
    avatar_url VARCHAR(500),
    created_at TIMESTAMP   NOT NULL DEFAULT NOW(),
    created_by UUID,
    updated_at TIMESTAMP   NOT NULL DEFAULT NOW(),
    updated_by UUID,
    is_delete  BOOLEAN     NOT NULL DEFAULT FALSE,
    version    BIGINT      NOT NULL DEFAULT 0
);

CREATE TABLE customer_addresses (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    customer_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    city_code INT NOT NULL,
    city_name VARCHAR(100) NOT NULL,
    ward_code INT NOT NULL,
    ward_name VARCHAR(100) NOT NULL,
    detail VARCHAR(255) NOT NULL,
    is_default BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP   NOT NULL DEFAULT NOW(),
    created_by UUID,
    updated_at TIMESTAMP   NOT NULL DEFAULT NOW(),
    updated_by UUID,
    is_delete  BOOLEAN     NOT NULL DEFAULT FALSE,
    version    BIGINT      NOT NULL DEFAULT 0,
    CONSTRAINT fk_customer FOREIGN KEY (customer_id) REFERENCES customers(id)
);