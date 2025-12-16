CREATE TABLE beats (
    id UUID PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    bpm INTEGER,
    genre VARCHAR(100),
    mood VARCHAR(100),
    file_url VARCHAR(500) NOT NULL,
    file_size BIGINT,
    duration_seconds INTEGER,
    tenant_id UUID NOT NULL REFERENCES tenants(id),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

CREATE TABLE packs (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    tenant_id UUID NOT NULL REFERENCES tenants(id),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

CREATE TABLE pack_beats (
    pack_id UUID NOT NULL REFERENCES packs(id) ON DELETE CASCADE,
    beat_id UUID NOT NULL REFERENCES beats(id) ON DELETE CASCADE,
    position INTEGER,
    PRIMARY KEY (pack_id, beat_id)
);

CREATE INDEX idx_beat_tenant ON beats(tenant_id);
CREATE INDEX idx_beat_genre ON beats(genre);
CREATE INDEX idx_beat_title ON beats(title);
CREATE INDEX idx_pack_tenant ON packs(tenant_id);
CREATE INDEX idx_pack_name ON packs(name);