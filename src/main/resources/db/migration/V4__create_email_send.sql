CREATE TABLE email_sends (
    id UUID PRIMARY KEY,
    artist_id UUID NOT NULL REFERENCES artists(id),
    tenant_id UUID NOT NULL REFERENCES tenants(id),
    pack_id UUID REFERENCES packs(id),
    status VARCHAR(20) NOT NULL,
    retry_count INTEGER NOT NULL DEFAULT 0,
    sent_at TIMESTAMP,
    failed_at TIMESTAMP,
    error_message VARCHAR(1000),
    subject VARCHAR(500),
    email_body TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

CREATE TABLE email_send_beats (
    email_send_id UUID NOT NULL REFERENCES email_sends(id) ON DELETE CASCADE,
    beat_id UUID NOT NULL REFERENCES beats(id),
    PRIMARY KEY (email_send_id, beat_id)
);

CREATE TABLE replies (
    id UUID PRIMARY KEY,
    artist_id UUID NOT NULL REFERENCES artists(id),
    email_send_id UUID REFERENCES email_sends(id),
    content TEXT,
    replied_at TIMESTAMP NOT NULL,
    from_email VARCHAR(255),
    subject VARCHAR(500),
    processed BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

CREATE INDEX idx_email_send_artist ON email_sends(artist_id);
CREATE INDEX idx_email_send_tenant ON email_sends(tenant_id);
CREATE INDEX idx_email_send_status ON email_sends(status);
CREATE INDEX idx_email_send_sent_at ON email_sends(sent_at);
CREATE INDEX idx_reply_artist ON replies(artist_id);
CREATE INDEX idx_reply_email_send ON replies(email_send_id);
CREATE INDEX idx_reply_replied_at ON replies(replied_at);