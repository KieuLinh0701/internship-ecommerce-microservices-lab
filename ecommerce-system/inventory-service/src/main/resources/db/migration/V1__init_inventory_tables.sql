CREATE TABLE m_category
(
    id         UUID PRIMARY KEY,
    name       VARCHAR(100) NOT NULL,
    parent_id  UUID NULL,
    slug       VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP    NOT NULL DEFAULT NOW(),
    created_by UUID NULL,
    updated_at TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_by UUID NULL,
    is_delete  BOOLEAN      NOT NULL DEFAULT FALSE,
    version    BIGINT       NOT NULL DEFAULT 0,

    CONSTRAINT uq_category_parent_name UNIQUE (parent_id, name),
    CONSTRAINT fk_category_parent
        FOREIGN KEY (parent_id)
            REFERENCES m_category (id)
            ON DELETE SET NULL
);

CREATE TABLE m_brand
(
    id          UUID PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    slug        VARCHAR(100) NOT NULL UNIQUE,
    logo_url    VARCHAR(500),
    status      BOOLEAN      NOT NULL DEFAULT TRUE,
    description TEXT,
    created_at  TIMESTAMP    NOT NULL DEFAULT NOW(),
    created_by  UUID NULL,
    updated_at  TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_by  UUID NULL,
    is_delete   BOOLEAN      NOT NULL DEFAULT FALSE,
    version     BIGINT       NOT NULL DEFAULT 0
);

CREATE TABLE m_product
(
    id          UUID PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    slug        VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    category_id UUID NULL,
    brand_id    UUID NULL,
    base_price  BIGINT       NOT NULL,
    status      VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
    created_at  TIMESTAMP    NOT NULL DEFAULT NOW(),
    created_by  UUID NULL,
    updated_at  TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_by  UUID NULL,
    is_delete   BOOLEAN      NOT NULL DEFAULT FALSE,
    version     BIGINT       NOT NULL DEFAULT 0,

    CONSTRAINT fk_product_category
        FOREIGN KEY (category_id)
            REFERENCES m_category (id)
            ON DELETE SET NULL,

    CONSTRAINT fk_product_brand
        FOREIGN KEY (brand_id)
            REFERENCES m_brand (id)
            ON DELETE SET NULL
);

CREATE TABLE product_images
(
    id           UUID PRIMARY KEY,
    product_id   UUID         NOT NULL,
    image_url    VARCHAR(500) NOT NULL,
    sort_order   INTEGER      NOT NULL DEFAULT 0,
    is_thumbnail BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at   TIMESTAMP    NOT NULL DEFAULT NOW(),
    created_by   UUID NULL,
    updated_at   TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_by   UUID NULL,
    is_delete    BOOLEAN      NOT NULL DEFAULT FALSE,
    version      BIGINT       NOT NULL DEFAULT 0,

    CONSTRAINT fk_product_images_product
        FOREIGN KEY (product_id)
            REFERENCES m_product (id)
            ON DELETE CASCADE
);

CREATE TABLE m_product_variant
(
    id                  UUID PRIMARY KEY,
    product_id          UUID         NOT NULL,
    sku                 VARCHAR(100) NOT NULL UNIQUE,
    price               BIGINT       NOT NULL,
    attribute_value_ids UUID[],
    image_url           VARCHAR(500),
    created_at          TIMESTAMP    NOT NULL DEFAULT NOW(),
    created_by          UUID NULL,
    updated_at          TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_by          UUID NULL,
    is_delete           BOOLEAN      NOT NULL DEFAULT FALSE,
    version             BIGINT       NOT NULL DEFAULT 0,

    CONSTRAINT fk_variant_product
        FOREIGN KEY (product_id)
            REFERENCES m_product (id)
            ON DELETE CASCADE
);

CREATE TABLE m_attribute
(
    id         UUID PRIMARY KEY,
    name       VARCHAR(50) NOT NULL UNIQUE,
    code       VARCHAR(20) NOT NULL UNIQUE,
    sort_order INTEGER     NOT NULL DEFAULT 0,
    status     VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP   NOT NULL DEFAULT NOW(),
    created_by UUID NULL,
    updated_at TIMESTAMP   NOT NULL DEFAULT NOW(),
    updated_by UUID NULL,
    is_delete  BOOLEAN     NOT NULL DEFAULT FALSE,
    version    BIGINT      NOT NULL DEFAULT 0
);

CREATE TABLE m_attribute_value
(
    id           UUID PRIMARY KEY,
    attribute_id UUID        NOT NULL,
    value        VARCHAR(50) NOT NULL,
    code         VARCHAR(20) NOT NULL,
    sort_order   INTEGER     NOT NULL DEFAULT 0,
    status       VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at   TIMESTAMP   NOT NULL DEFAULT NOW(),
    created_by   UUID NULL,
    updated_at   TIMESTAMP   NOT NULL DEFAULT NOW(),
    updated_by   UUID NULL,
    is_delete    BOOLEAN     NOT NULL DEFAULT FALSE,
    version      BIGINT      NOT NULL DEFAULT 0,

    CONSTRAINT fk_attribute_value_attribute
        FOREIGN KEY (attribute_id)
            REFERENCES m_attribute (id)
            ON DELETE CASCADE
);

CREATE TABLE product_variant_attribute_value
(
    id                 UUID PRIMARY KEY,
    variant_id         UUID      NOT NULL,
    attribute_value_id UUID      NOT NULL,
    created_at         TIMESTAMP NOT NULL DEFAULT NOW(),
    created_by         UUID NULL,
    updated_at         TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_by         UUID NULL,
    is_delete          BOOLEAN   NOT NULL DEFAULT FALSE,
    version            BIGINT    NOT NULL DEFAULT 0,

    CONSTRAINT fk_pvav_variant
        FOREIGN KEY (variant_id)
            REFERENCES m_product_variant (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_pvav_attribute_value
        FOREIGN KEY (attribute_value_id)
            REFERENCES m_attribute_value (id)
            ON DELETE CASCADE,

    CONSTRAINT uq_variant_attribute UNIQUE (variant_id, attribute_value_id)
);

CREATE TABLE product_variant_inventory
(
    id                  UUID PRIMARY KEY,
    variant_id          UUID      NOT NULL UNIQUE,
    quantity            INTEGER   NOT NULL DEFAULT 0,
    reserved_quantity   INTEGER   NOT NULL DEFAULT 0,
    available_quantity  INTEGER   NOT NULL DEFAULT 0,
    low_stock_threshold INTEGER   NOT NULL DEFAULT 10,
    created_at          TIMESTAMP NOT NULL DEFAULT NOW(),
    created_by          UUID NULL,
    updated_at          TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_by          UUID NULL,
    is_delete           BOOLEAN   NOT NULL DEFAULT FALSE,
    version             BIGINT    NOT NULL DEFAULT 0,

    CONSTRAINT fk_inventory_variant
        FOREIGN KEY (variant_id)
            REFERENCES m_product_variant (id)
            ON DELETE CASCADE
);