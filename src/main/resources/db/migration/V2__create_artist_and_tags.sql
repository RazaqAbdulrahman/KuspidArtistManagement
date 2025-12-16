CREATE TABLE artists (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    genre VARCHAR(100),
    priority_level VARCHAR(20) NOT NULL,
    tenant_id UUID NOT NULL REFERENCES tenants(id),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

CREATE TABLE tags (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(20) NOT NULL,
    tenant_id UUID NOT NULL REFERENCES tenants(id),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    UNIQUE(name, tenant_id)
);

CREATE TABLE artist_tags (
    artist_id UUID NOT NULL REFERENCES artists(id) ON DELETE CASCADE,
    tag_id UUID NOT NULL REFERENCES tags(id) ON DELETE CASCADE,
    PRIMARY KEY (artist_id, tag_id)
);

CREATE INDEX idx_artist_email ON artists(email);
CREATE INDEX idx_artist_tenant ON artists(tenant_id);
CREATE INDEX idx_artist_priority ON artists(priority_level);
CREATE INDEX idx_artist_genre ON artists(genre);
CREATE INDEX idx_tag_name ON tags(name);
CREATE INDEX idx_tag_type ON tags(type);
CREATE INDEX idx_tag_tenant ON tags(tenant_id);